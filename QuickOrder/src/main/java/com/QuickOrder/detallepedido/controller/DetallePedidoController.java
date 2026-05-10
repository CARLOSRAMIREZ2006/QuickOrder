package com.QuickOrder.detallepedido.controller;

import com.QuickOrder.detallepedido.model.DetallePedido;
import com.QuickOrder.detallepedido.service.DetallePedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/detalles-pedido")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping
    public List<DetallePedido> listar() {
        return detallePedidoService.obtenerTodos();
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<DetallePedido>> listarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(detallePedidoService.obtenerPorPedidoId(pedidoId));
    }

    @PostMapping
    public ResponseEntity<DetallePedido> crear(@Valid @RequestBody DetallePedido detallePedido) {
        return new ResponseEntity<>(detallePedidoService.agregarDetalle(detallePedido), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        detallePedidoService.eliminarDetalle(id);
        return ResponseEntity.noContent().build();
    }
}
