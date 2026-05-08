package com.QuickOrder.pedido.controller;

import com.QuickOrder.pedido.model.Pedido;
import com.QuickOrder.pedido.service.PedidoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private static final Logger log = LoggerFactory.getLogger(PedidoController.class);
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        log.info("Petición REST para listar todos los pedidos");
        return ResponseEntity.ok(pedidoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedido(@PathVariable Long id) {
        log.info("Petición REST para obtener pedido ID: {}", id);
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@Valid @RequestBody Pedido pedido) {
        log.info("Petición REST para crear un nuevo pedido");
        Pedido nuevoPedido = pedidoService.crearPedido(pedido);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Pedido> confirmarPedido(
            @PathVariable Long id,
            @RequestParam Long productoId,
            @RequestParam Integer cantidad) {

        log.info("Petición REST para confirmar pedido ID: {} y descontar {} unidades del producto {}", id, cantidad, productoId);
        Pedido pedidoConfirmado = pedidoService.confirmarPedidoYDescontarStock(id, productoId, cantidad);
        return ResponseEntity.ok(pedidoConfirmado);
    }
}