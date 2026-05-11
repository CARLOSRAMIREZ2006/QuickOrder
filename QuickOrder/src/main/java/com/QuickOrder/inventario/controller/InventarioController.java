package com.QuickOrder.inventario.controller;

import com.QuickOrder.inventario.model.Inventario;
import com.QuickOrder.inventario.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventarios")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public ResponseEntity<List<Inventario>> listarTodo() {
        return ResponseEntity.ok(inventarioService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<Inventario> crearInventario(@Valid @RequestBody Inventario inventario) {
        Inventario nuevo = inventarioService.guardar(inventario);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PutMapping("/descontar")
    public ResponseEntity<Void> descontarStock(@RequestParam Long productoId, @RequestParam Integer cantidad) {
        inventarioService.descontarStock(productoId, cantidad);
        return ResponseEntity.ok().build();
    }
}