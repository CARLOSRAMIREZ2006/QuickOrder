package com.QuickOrder.pago.controller;

import com.QuickOrder.pago.model.Pago;
import com.QuickOrder.pago.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public List<Pago> listar() {
        return pagoService.obtenerTodos();
    }

    @PostMapping
    public ResponseEntity<Pago> crearPago(@Valid @RequestBody Pago pago) {
        Pago nuevoPago = pagoService.procesarPago(pago);
        return new ResponseEntity<>(nuevoPago, HttpStatus.CREATED);
    }
}