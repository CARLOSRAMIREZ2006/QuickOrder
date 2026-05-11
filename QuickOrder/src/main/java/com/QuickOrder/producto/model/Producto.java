package com.QuickOrder.producto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El SKU no puede estar vacío")
    @Column(unique = true, nullable = false)
    private String sku;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    @Column(nullable = false)
    private BigDecimal precio;

    @Column(nullable = false)
    private Boolean activo = true;

    @PrePersist
    public void prePersist() {
        if (this.activo == null) {
            this.activo = true;
        }
    }
}