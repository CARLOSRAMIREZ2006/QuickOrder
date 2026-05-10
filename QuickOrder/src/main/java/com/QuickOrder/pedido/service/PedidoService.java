package com.QuickOrder.pedido.service;

import com.QuickOrder.pedido.model.Pedido;
import com.QuickOrder.pedido.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);
    private final PedidoRepository pedidoRepository;
    private final RestTemplate restTemplate;

    private final String CLIENTES_API_URL = "http://localhost:8080/api/v1/clientes/";
    private final String INVENTARIO_API_URL = "http://localhost:8080/api/v1/inventario/";

    public PedidoService(PedidoRepository pedidoRepository, RestTemplate restTemplate) {
        this.pedidoRepository = pedidoRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional(readOnly = true)
    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
    }

    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        log.info("Guardando pedido directo para el cliente ID: {}", pedido.getClienteId());
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = obtenerPorId(id);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void actualizarEstadoPedido(Long id, String nuevoEstado) {
        Pedido pedido = obtenerPorId(id);
        pedido.setEstado(nuevoEstado);
        pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido confirmarPedidoYDescontarStock(Long id, Long productoId, Integer cantidad) {
        Pedido pedido = obtenerPorId(id);
        try {
            String url = INVENTARIO_API_URL + productoId + "/descontar?cantidad=" + cantidad;
            restTemplate.put(url, null);
        } catch (Exception e) {
            log.error("Error al contactar API Inventario. Causa real: {}", e.getMessage());
            throw new RuntimeException("Error al descontar stock para el producto ID: " + productoId);
        }
        pedido.setEstado("CONFIRMADO");
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }
}