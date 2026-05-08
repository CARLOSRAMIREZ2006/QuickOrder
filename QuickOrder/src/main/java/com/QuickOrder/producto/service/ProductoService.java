package com.QuickOrder.producto.service;

import com.QuickOrder.producto.model.Producto;
import com.QuickOrder.producto.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerTodos() {
        log.info("Consultando todos los productos activos e inactivos");
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id) {
        log.info("Buscando producto con ID: {}", id);
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    @Transactional
    public Producto crearProducto(Producto producto) {
        log.info("Intentando crear un nuevo producto con SKU: {}", producto.getSku());
        Optional<Producto> existente = productoRepository.findBySku(producto.getSku());
        if (existente.isPresent()) {
            log.error("Error al crear: Ya existe un producto con el SKU {}", producto.getSku());
            throw new RuntimeException("El SKU ingresado ya pertenece a otro producto");
        }
        Producto nuevoProducto = productoRepository.save(producto);
        log.info("Producto creado exitosamente con ID: {}", nuevoProducto.getId());
        return nuevoProducto;
    }

    @Transactional
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        log.info("Actualizando producto con ID: {}", id);
        Producto productoDb = obtenerPorId(id);

        productoDb.setNombre(productoActualizado.getNombre());
        productoDb.setDescripcion(productoActualizado.getDescripcion());
        productoDb.setPrecio(productoActualizado.getPrecio());
        productoDb.setActivo(productoActualizado.getActivo());

        return productoRepository.save(productoDb);
    }

    @Transactional
    public void eliminarProducto(Long id) {
        log.info("Eliminando (baja lógica/física) producto con ID: {}", id);
        Producto producto = obtenerPorId(id);
        producto.setActivo(false);
        productoRepository.save(producto);
        log.info("Producto con ID: {} marcado como inactivo", id);
    }
}
