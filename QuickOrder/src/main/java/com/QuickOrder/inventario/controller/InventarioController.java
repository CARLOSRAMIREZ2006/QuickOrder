package com.QuickOrder.inventario.controller;

import com.QuickOrder.inventario.model.Inventario;
import com.QuickOrder.inventario.service.InventarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")
public class InventarioController {

    private static final Logger log = LoggerFactory.getLogger(InventarioController.class);
    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public ResponseEntity<List<Inventario>> listarInventario() {
        log.info("Petición REST para obtener todo el inventario");
        return ResponseEntity.ok(inventarioService.obtenerTodos());
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Inventario> obtenerInventarioPorProducto(@PathVariable Long productoId) {
        log.info("Petición REST para obtener inventario del producto ID: {}", productoId);
        return ResponseEntity.ok(inventarioService.obtenerPorProductoId(productoId));
    }

    @PostMapping
    public ResponseEntity<Inventario> inicializarInventario(@Valid @RequestBody Inventario inventario) {
        log.info("Petición REST para crear registro de inventario");
        return new ResponseEntity<>(inventarioService.inicializarInventario(inventario), HttpStatus.CREATED);
    }

    @PutMapping("/producto/{productoId}")
    public ResponseEntity<Inventario> actualizarStock(
            @PathVariable Long productoId,
            @RequestParam Integer cantidad) {
        log.info("Petición REST para actualizar stock del producto ID: {} a la cantidad de: {}", productoId, cantidad);
        return ResponseEntity.ok(inventarioService.actualizarStock(productoId, cantidad));
    }

    @PutMapping("/producto/{productoId}/descontar")
    public ResponseEntity<Inventario> descontarStock(
            @PathVariable Long productoId,
            @RequestParam Integer cantidad) {
        log.info("Petición REST para descontar {} unidades del producto ID: {}", cantidad, productoId);
        return ResponseEntity.ok(inventarioService.descontarStock(productoId, cantidad));
    }
}