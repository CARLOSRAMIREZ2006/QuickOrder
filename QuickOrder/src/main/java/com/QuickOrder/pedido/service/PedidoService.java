package com.QuickOrder.pedido.service;

import com.QuickOrder.detallepedido.model.DetallePedido;
import com.QuickOrder.pedido.model.Pedido;
import com.QuickOrder.pedido.repository.PedidoRepository;
import com.QuickOrder.producto.model.Producto;
import com.QuickOrder.producto.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final RestTemplate restTemplate;

    public PedidoService(PedidoRepository pedidoRepository, ProductoRepository productoRepository, RestTemplate restTemplate) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
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
        // Regla: No se puede crear un pedido si el cliente no existe
        String urlCliente = "http://localhost:8080/api/v1/clientes/" + pedido.getClienteId();
        try {
            restTemplate.getForObject(urlCliente, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Error: El cliente con ID " + pedido.getClienteId() + " no existe.");
        }

        pedido.setFechaCreacion(LocalDateTime.now());
        if (pedido.getEstado() == null) {
            pedido.setEstado("PENDIENTE");
        }

        BigDecimal totalAcumulado = BigDecimal.ZERO;

        if (pedido.getDetalles() != null) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                Producto producto = productoRepository.findById(detalle.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + detalle.getProductoId()));

                BigDecimal precioUnitario = producto.getPrecio();
                BigDecimal subtotal = precioUnitario.multiply(new BigDecimal(detalle.getCantidad()));

                detalle.setPrecioUnitario(precioUnitario);
                detalle.setSubtotal(subtotal);
                totalAcumulado = totalAcumulado.add(subtotal);
            }
        }

        pedido.setPrecioTotal(totalAcumulado);
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

        // Regla: El inventario debe descontarse automáticamente al confirmar un pedido
        String urlInventario = "http://localhost:8080/api/v1/inventarios/descontar?productoId=" + productoId + "&cantidad=" + cantidad;
        try {
            restTemplate.put(urlInventario, null);
        } catch (Exception e) {
            throw new RuntimeException("Error: No se pudo descontar el stock del producto " + productoId);
        }

        pedido.setEstado("CONFIRMADO");
        log.info("Pedido {} confirmado e inventario actualizado", id);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }
}