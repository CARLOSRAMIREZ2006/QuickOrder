package com.QuickOrder.notificacion.controller;

import com.QuickOrder.notificacion.model.Notificacion;
import com.QuickOrder.notificacion.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public List<Notificacion> listar() {
        return notificacionService.obtenerTodas();
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<Notificacion>> listarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(notificacionService.obtenerPorPedidoId(pedidoId));
    }

    @PostMapping
    public ResponseEntity<Notificacion> crearNotificacion(@Valid @RequestBody Notificacion notificacion) {
        Notificacion nuevaNotificacion = notificacionService.enviarNotificacion(notificacion);
        return new ResponseEntity<>(nuevaNotificacion, HttpStatus.CREATED);
    }
}
