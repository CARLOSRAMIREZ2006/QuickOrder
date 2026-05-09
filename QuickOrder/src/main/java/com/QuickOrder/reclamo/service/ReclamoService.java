package com.QuickOrder.reclamo.service;

import com.QuickOrder.reclamo.model.Reclamo;
import com.QuickOrder.reclamo.repository.ReclamoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReclamoService {

    private static final Logger log = LoggerFactory.getLogger(ReclamoService.class);
    private final ReclamoRepository reclamoRepository;

    public ReclamoService(ReclamoRepository reclamoRepository) {
        this.reclamoRepository = reclamoRepository;
    }

    @Transactional(readOnly = true)
    public List<Reclamo> obtenerTodos() {
        return reclamoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reclamo> obtenerPorPedido(Long pedidoId) {
        return reclamoRepository.findByPedidoId(pedidoId);
    }

    @Transactional
    public Reclamo crearReclamo(Reclamo reclamo) {
        log.info("Registrando nuevo reclamo para el pedido ID: {}", reclamo.getPedidoId());
        return reclamoRepository.save(reclamo);
    }

    @Transactional
    public Reclamo resolverReclamo(Long id, String respuesta, String nuevoEstado) {
        log.info("Resolviendo reclamo ID: {} con estado: {}", id, nuevoEstado);
        Reclamo reclamo = reclamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reclamo no encontrado"));

        reclamo.setRespuesta(respuesta);
        reclamo.setEstado(nuevoEstado);

        return reclamoRepository.save(reclamo);
    }
}