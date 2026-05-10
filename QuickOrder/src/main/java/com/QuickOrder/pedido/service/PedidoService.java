package com.QuickOrder.pedido.service;

import com.QuickOrder.pedido.model.Pedido;
import com.QuickOrder.pedido.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);
    private final PedidoRepository pedidoRepository;
    private final RestTemplate restTemplate;

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
        var detalles = pedido.getDetalles();
        pedido.setDetalles(null);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        if (detalles != null) {
            for (var detalle : detalles) {
                detalle.setPedidoId(pedidoGuardado.getId());
            }
            pedidoGuardado.setDetalles(detalles);
            pedidoGuardado = pedidoRepository.save(pedidoGuardado);
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
    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }
}