package com.QuickOrder.inventario.service;

import com.QuickOrder.inventario.model.Inventario;
import com.QuickOrder.inventario.repository.InventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Inventario> obtenerTodos() {
        return inventarioRepository.findAll();
    }

    @Transactional
    public Inventario guardar(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }
}