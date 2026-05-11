package com.QuickOrder.despacho.service;

import com.QuickOrder.despacho.model.Despacho;
import com.QuickOrder.despacho.repository.DespachoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class DespachoService {

    private final DespachoRepository despachoRepository;
    private final RestTemplate restTemplate;

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
                .orElseThrow(() -> new RuntimeException("No hay despacho para el pedido ID: " + pedidoId));
    }

    @Transactional
    public Despacho crearDespacho(Despacho despacho) {
        String urlPago = "http://localhost:8080/api/v1/pagos/pedido/" + despacho.getPedidoId();
        try {
            restTemplate.getForObject(urlPago, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("El pedido no ha sido pagado");
        }
        despacho.setEstado("PREPARANDO");
        return despachoRepository.save(despacho);
    }

    @Transactional
    public Despacho actualizarEstado(Long id, String estado) {
        Despacho despacho = despachoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Despacho no encontrado"));
        despacho.setEstado(estado);
        return despachoRepository.save(despacho);
    }
}