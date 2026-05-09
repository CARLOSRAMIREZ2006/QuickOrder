package com.QuickOrder.despacho.controller;

import com.QuickOrder.despacho.model.Despacho;
import com.QuickOrder.despacho.service.DespachoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/despachos")
public class DespachoController {

    private final DespachoService despachoService;

    public DespachoController(DespachoService despachoService) {
        this.despachoService = despachoService;
    }

    @GetMapping
    public List<Despacho> listar() {
        return despachoService.obtenerTodos();
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Despacho> obtenerPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(despachoService.obtenerPorPedidoId(pedidoId));
    }

    @PostMapping
    public ResponseEntity<Despacho> registrarDespacho(@Valid @RequestBody Despacho despacho) {
        Despacho nuevoDespacho = despachoService.crearDespacho(despacho);
        return new ResponseEntity<>(nuevoDespacho, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Despacho> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        Despacho despachoActualizado = despachoService.actualizarEstado(id, estado);
        return ResponseEntity.ok(despachoActualizado);
    }
}