package com.QuickOrder.cliente.service;

import com.QuickOrder.cliente.model.Cliente;
import com.QuickOrder.cliente.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<Cliente> obtenerTodos() {
        log.info("Consultando la lista completa de clientes");
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente obtenerPorId(Long id) {
        log.info("Buscando cliente con ID: {}", id);
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
    }

    @Transactional
    public Cliente crearCliente(Cliente cliente) {
        log.info("Intentando registrar un nuevo cliente con email: {}", cliente.getEmail());

        Optional<Cliente> existente = clienteRepository.findByEmail(cliente.getEmail());
        if (existente.isPresent()) {
            log.error("Error al crear cliente: El email {} ya está en uso", cliente.getEmail());
            throw new RuntimeException("El email ingresado ya está registrado en el sistema");
        }

        Cliente nuevoCliente = clienteRepository.save(cliente);
        log.info("Cliente registrado exitosamente con ID: {}", nuevoCliente.getId());
        return nuevoCliente;
    }

    @Transactional
    public Cliente actualizarCliente(Long id, Cliente datosActualizados) {
        log.info("Actualizando información del cliente ID: {}", id);
        Cliente clienteDb = obtenerPorId(id);

        if (!clienteDb.getEmail().equals(datosActualizados.getEmail())) {
            Optional<Cliente> existente = clienteRepository.findByEmail(datosActualizados.getEmail());
            if (existente.isPresent()) {
                throw new RuntimeException("El nuevo email ya está en uso por otro cliente");
            }
        }

        clienteDb.setNombre(datosActualizados.getNombre());
        clienteDb.setApellido(datosActualizados.getApellido());
        clienteDb.setEmail(datosActualizados.getEmail());
        clienteDb.setTelefono(datosActualizados.getTelefono());
        clienteDb.setDireccion(datosActualizados.getDireccion());
        clienteDb.setActivo(datosActualizados.getActivo());

        return clienteRepository.save(clienteDb);
    }

    @Transactional
    public void eliminarCliente(Long id) {
        log.info("Eliminando (baja lógica) cliente con ID: {}", id);
        Cliente clienteDb = obtenerPorId(id);
        clienteDb.setActivo(false); // Baja lógica recomendada
        clienteRepository.save(clienteDb);
        log.info("Cliente ID: {} desactivado", id);
    }
}
