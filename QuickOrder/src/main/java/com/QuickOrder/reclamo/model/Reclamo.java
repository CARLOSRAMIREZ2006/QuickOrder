package com.QuickOrder.reclamo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reclamos")
public class Reclamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del pedido es obligatorio")
    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @NotNull(message = "El ID del cliente es obligatorio")
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @NotBlank(message = "El motivo del reclamo es obligatorio")
    @Column(nullable = false)
    private String motivo;

    @NotBlank(message = "La descripción detallada es obligatoria")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(name = "fecha_reclamo")
    private LocalDateTime fechaReclamo;

    @Column(nullable = false)
    private String estado;

    @Column(columnDefinition = "TEXT")
    private String respuesta;

    @PrePersist
    public void prePersist() {
        this.fechaReclamo = LocalDateTime.now();
        if (this.estado == null) this.estado = "INGRESADO";
    }
}