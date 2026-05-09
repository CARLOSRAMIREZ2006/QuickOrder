package com.QuickOrder.reclamo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
        if (this.estado == null) {
            this.estado = "INGRESADO";
        }
    }

    public Reclamo() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFechaReclamo() { return fechaReclamo; }
    public void setFechaReclamo(LocalDateTime fechaReclamo) { this.fechaReclamo = fechaReclamo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
}
