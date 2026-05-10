package com.QuickOrder.pedido.service;

import com.QuickOrder.detallepedido.model.DetallePedido;
import com.QuickOrder.pedido.model.Pedido;
import com.QuickOrder.pedido.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);
    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
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
        List<DetallePedido> detallesTemporales = pedido.getDetalles();
        pedido.setDetalles(null);
        pedido.setTotal(BigDecimal.ZERO);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        if (detallesTemporales != null) {
            BigDecimal sumaTotal = BigDecimal.ZERO;
            for (DetallePedido det : detallesTemporales) {
                det.setPedidoId(pedidoGuardado.getId());
                BigDecimal cantidad = new BigDecimal(det.getCantidad());
                BigDecimal subtotal = det.getPrecioUnitario().multiply(cantidad);
                sumaTotal = sumaTotal.add(subtotal);
            }
            pedidoGuardado.setTotal(sumaTotal);
            pedidoGuardado.setDetalles(detallesTemporales);
            return pedidoRepository.save(pedidoGuardado);
        }

        return pedidoGuardado;
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
        pedido.setEstado("CONFIRMADO");
        log.info("Pedido {} confirmado. Descontando {} unidades del producto {}", id, cantidad, productoId);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }
}