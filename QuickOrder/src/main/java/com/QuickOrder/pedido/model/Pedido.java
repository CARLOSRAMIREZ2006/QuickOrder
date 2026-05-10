package com.QuickOrder.pedido.model;

import com.QuickOrder.detallepedido.model.DetallePedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
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
    @Column(name = "precio_total", nullable = false)
    private BigDecimal precioTotal = BigDecimal.ZERO;

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
            BigDecimal totalAcumulado = BigDecimal.ZERO;
            for (DetallePedido det : this.detalles) {
                if (det.getPrecioUnitario() != null && det.getCantidad() != null) {
                    BigDecimal sub = det.getPrecioUnitario().multiply(new BigDecimal(det.getCantidad()));
                    det.setSubtotal(sub);
                    totalAcumulado = totalAcumulado.add(sub);
                }
            }
            this.precioTotal = totalAcumulado;
        }
    }
}