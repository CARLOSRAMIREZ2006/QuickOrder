package com.QuickOrder.pedido.model;

import com.QuickOrder.detallepedido.model.DetallePedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del cliente es obligatorio")
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @PositiveOrZero(message = "El total no puede ser negativo")
    @Column(nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private String estado;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pedido_id")
    private List<DetallePedido> detalles;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = "PENDIENTE";
        }

        if (this.detalles != null && !this.detalles.isEmpty()) {
            BigDecimal suma = BigDecimal.ZERO;
            for (DetallePedido det : this.detalles) {
                if (det.getPrecioUnitario() != null && det.getCantidad() != null) {
                    BigDecimal sub = det.getPrecioUnitario().multiply(new BigDecimal(det.getCantidad()));
                    suma = suma.add(sub);
                }
            }
            this.total = suma;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}
