package com.QuickOrder.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventarios")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del producto es obligatorio")
    @Column(name = "producto_id", unique = true, nullable = false)
    private Long productoId;

    @NotNull(message = "La cantidad disponible es obligatoria")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible;

    @Column(name = "bodega")
    private String bodega;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @PrePersist
    @PreUpdate
    public void actualizarFecha() {
        this.ultimaActualizacion = LocalDateTime.now();
    }
}