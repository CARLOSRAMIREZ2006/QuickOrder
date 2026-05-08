package com.QuickOrder.pedido.service;

import com.QuickOrder.pedido.model.Pedido;
import com.QuickOrder.pedido.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
        log.info("Iniciando creación de pedido para el cliente ID: {}", pedido.getClienteId());

        verificarClienteExiste(pedido.getClienteId());

        pedido.setEstado("PENDIENTE");
        Pedido nuevoPedido = pedidoRepository.save(pedido);

        log.info("Pedido PENDIENTE creado con ID: {}", nuevoPedido.getId());
        return nuevoPedido;
    }

    @Transactional
    public Pedido confirmarPedidoYDescontarStock(Long pedidoId, Long productoId, Integer cantidad) {
        log.info("Confirmando pedido ID: {}", pedidoId);
        Pedido pedido = obtenerPorId(pedidoId);

        if (!pedido.getEstado().equals("PENDIENTE")) {
            throw new RuntimeException("Solo se pueden confirmar pedidos en estado PENDIENTE");
        }

        try {
            String url = INVENTARIO_API_URL + "producto/" + productoId + "/descontar?cantidad=" + cantidad;
            restTemplate.put(url, null); // PUT para descontar stock
            log.info("Stock descontado exitosamente en el microservicio de Inventario");
        } catch (HttpClientErrorException e) {
            log.error("Error al descontar stock. Es posible que no haya stock suficiente.");
            throw new RuntimeException("No se pudo confirmar el pedido: Stock insuficiente o producto no encontrado.");
        }

        pedido.setEstado("CONFIRMADO");
        return pedidoRepository.save(pedido);
    }

    private void verificarClienteExiste(Long clienteId) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(CLIENTES_API_URL + clienteId, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Cliente no encontrado en el sistema");
            }
            log.info("Validación exitosa: El cliente ID {} existe", clienteId);
        } catch (HttpClientErrorException e) {
            log.error("Fallo la validación: Cliente ID {} no existe en el microservicio Clientes", clienteId);
            throw new RuntimeException("No se puede crear el pedido: El cliente especificado no existe.");
        }
    }
}