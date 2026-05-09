package com.QuickOrder.reclamo.controller;

import com.QuickOrder.reclamo.model.Reclamo;
import com.QuickOrder.reclamo.service.ReclamoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reclamos")
public class ReclamoController {

    private final ReclamoService reclamoService;

    public ReclamoController(ReclamoService reclamoService) {
        this.reclamoService = reclamoService;
    }

    @GetMapping
    public List<Reclamo> listar() {
        return reclamoService.obtenerTodos();
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<Reclamo>> listarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(reclamoService.obtenerPorPedido(pedidoId));
    }

    @PostMapping
    public ResponseEntity<Reclamo> crearReclamo(@Valid @RequestBody Reclamo reclamo) {
        Reclamo nuevoReclamo = reclamoService.crearReclamo(reclamo);
        return new ResponseEntity<>(nuevoReclamo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/resolver")
    public ResponseEntity<Reclamo> resolverReclamo(
            @PathVariable Long id,
            @RequestParam String respuesta,
            @RequestParam String estado) {
        Reclamo reclamoActualizado = reclamoService.resolverReclamo(id, respuesta, estado);
        return ResponseEntity.ok(reclamoActualizado);
    }
}