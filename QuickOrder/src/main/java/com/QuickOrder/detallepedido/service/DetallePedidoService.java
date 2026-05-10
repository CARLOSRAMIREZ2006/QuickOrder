package com.QuickOrder.detallepedido.service;

import com.QuickOrder.detallepedido.model.DetallePedido;
import com.QuickOrder.detallepedido.repository.DetallePedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DetallePedidoService {

    private static final Logger log = LoggerFactory.getLogger(DetallePedidoService.class);
    private final DetallePedidoRepository detallePedidoRepository;

    public DetallePedidoService(DetallePedidoRepository detallePedidoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
    }

    @Transactional(readOnly = true)
    public List<DetallePedido> obtenerTodos() {
        return detallePedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<DetallePedido> obtenerPorPedidoId(Long pedidoId) {
        log.info("Consultando detalles para el pedido: {}", pedidoId);
        return detallePedidoRepository.findByPedidoId(pedidoId);
    }

    @Transactional
    public DetallePedido agregarDetalle(DetallePedido detalle) {
        // Corregido: getPedidoId() con I mayúscula, no L minúscula
        log.info("Guardando detalle para pedido ID: {}", detalle.getPedidoId());
        return detallePedidoRepository.save(detalle);
    }

    @Transactional
    public void eliminarDetalle(Long id) {
        detallePedidoRepository.deleteById(id);
    }
}