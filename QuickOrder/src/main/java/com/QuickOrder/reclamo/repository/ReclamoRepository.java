package com.QuickOrder.reclamo.repository;

import com.QuickOrder.reclamo.model.Reclamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReclamoRepository extends JpaRepository<Reclamo, Long> {
    List<Reclamo> findByPedidoId(Long pedidoId);
    List<Reclamo> findByClienteId(Long clienteId);
}