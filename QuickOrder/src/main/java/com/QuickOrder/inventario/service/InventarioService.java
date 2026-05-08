package com.QuickOrder.inventario.service;

import com.QuickOrder.inventario.model.Inventario;
import com.QuickOrder.inventario.repository.InventarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    private static final Logger log = LoggerFactory.getLogger(InventarioService.class);
    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Inventario> obtenerTodos() {
        log.info("Consultando todo el inventario");
        return inventarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Inventario obtenerPorProductoId(Long productoId) {
        log.info("Consultando inventario para el producto ID: {}", productoId);
        return inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException("No se encontró inventario para el producto con ID: " + productoId));
    }

    @Transactional
    public Inventario inicializarInventario(Inventario inventario) {
        log.info("Inicializando inventario para el producto ID: {}", inventario.getProductoId());
        Optional<Inventario> existente = inventarioRepository.findByProductoId(inventario.getProductoId());

        if (existente.isPresent()) {
            throw new RuntimeException("El producto ya tiene un registro de inventario. Utilice actualizar.");
        }
        return inventarioRepository.save(inventario);
    }

    @Transactional
    public Inventario actualizarStock(Long productoId, Integer nuevaCantidad) {
        log.info("Actualizando stock del producto ID: {} a {}", productoId, nuevaCantidad);
        Inventario inventario = obtenerPorProductoId(productoId);
        inventario.setCantidadDisponible(nuevaCantidad);
        return inventarioRepository.save(inventario);
    }

    @Transactional
    public Inventario descontarStock(Long productoId, Integer cantidadADescontar) {
        log.info("Intentando descontar {} unidades del producto ID: {}", cantidadADescontar, productoId);
        Inventario inventario = obtenerPorProductoId(productoId);

        if (inventario.getCantidadDisponible() < cantidadADescontar) {
            log.error("Stock insuficiente para el producto ID: {}. Disponible: {}, Solicitado: {}",
                    productoId, inventario.getCantidadDisponible(), cantidadADescontar);
            throw new RuntimeException("Stock insuficiente para procesar el pedido.");
        }

        inventario.setCantidadDisponible(inventario.getCantidadDisponible() - cantidadADescontar);
        Inventario inventarioActualizado = inventarioRepository.save(inventario);
        log.info("Stock descontado exitosamente. Nuevo stock: {}", inventarioActualizado.getCantidadDisponible());
        return inventarioActualizado;
    }
}