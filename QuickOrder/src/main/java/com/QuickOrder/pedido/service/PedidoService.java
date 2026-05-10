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

    private final String CLIENTES_API_URL = "http://localhost:8081/api/v1/clientes/";
    private final String INVENTARIO_API_URL = "http://localhost:8082/api/v1/inventario/";

    public PedidoService(PedidoRepository pedidoRepository, RestTemplate restTemplate) {
        this.pedidoRepository = pedidoRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional(readOnly = true)
    public List<Pedido> obtenerTodos() {
        log.info("Consultando todos los pedidos");
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
    }

    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        log.info("Validando si el cliente ID {} existe...", pedido.getClienteId());

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(CLIENTES_API_URL + pedido.getClienteId(), Map.class);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("El cliente no existe en el sistema.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error de validación: El cliente ID " + pedido.getClienteId() + " no existe.");
        }

        log.info("Cliente validado. Guardando pedido...");
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = obtenerPorId(id);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }
}