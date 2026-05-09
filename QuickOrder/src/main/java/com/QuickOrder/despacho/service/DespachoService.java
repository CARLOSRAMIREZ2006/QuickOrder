package com.QuickOrder.despacho.service;

import com.QuickOrder.despacho.model.Despacho;
import com.QuickOrder.despacho.repository.DespachoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class DespachoService {

    private static final Logger log = LoggerFactory.getLogger(DespachoService.class);
    private final DespachoRepository despachoRepository;
    private final RestTemplate restTemplate;
    private final String PEDIDOS_API_URL = "http://localhost:8080/api/v1/pedidos/";

    public DespachoService(DespachoRepository despachoRepository, RestTemplate restTemplate) {
        this.despachoRepository = despachoRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional(readOnly = true)
    public List<Despacho> obtenerTodos() {
        return despachoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Despacho obtenerPorPedidoId(Long pedidoId) {
        return despachoRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RuntimeException("No hay despacho registrado para el pedido ID: " + pedidoId));
    }

    @Transactional
    public Despacho crearDespacho(Despacho despacho) {
        log.info("Creando orden de despacho para el pedido ID: {}", despacho.getPedidoId());

        despacho.setNumeroSeguimiento("TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        return despachoRepository.save(despacho);
    }

    @Transactional
    public Despacho actualizarEstado(Long id, String nuevoEstado) {
        log.info("Actualizando estado del despacho ID: {} a {}", id, nuevoEstado);
        Despacho despacho = despachoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Despacho no encontrado"));

        despacho.setEstado(nuevoEstado);
        Despacho despachoActualizado = despachoRepository.save(despacho);

        try {
            String url = PEDIDOS_API_URL + despacho.getPedidoId() + "/estado?nuevoEstado=" + nuevoEstado;
            restTemplate.put(url, null);
            log.info("Pedido ID: {} sincronizado con el estado: {}", despacho.getPedidoId(), nuevoEstado);
        } catch (Exception e) {
            log.error("Error al sincronizar estado con el Pedido: {}", e.getMessage());
        }

        return despachoActualizado;
    }
}
