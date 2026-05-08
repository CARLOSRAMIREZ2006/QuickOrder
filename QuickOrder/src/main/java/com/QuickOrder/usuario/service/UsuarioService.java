package com.QuickOrder.usuario.service;

import com.QuickOrder.usuario.model.Usuario;
import com.QuickOrder.usuario.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        log.info("Consultando todos los usuarios del sistema");
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Transactional
    public Usuario crearUsuario(Usuario usuario) {
        log.info("Intentando registrar un nuevo usuario con email: {}", usuario.getEmail());

        Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail());
        if (existente.isPresent()) {
            log.error("Error: El email {} ya está registrado para otro usuario", usuario.getEmail());
            throw new RuntimeException("El email ingresado ya pertenece a un usuario existente");
        }

        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        log.info("Usuario creado exitosamente con ID: {} y Rol: {}", nuevoUsuario.getId(), nuevoUsuario.getRol());
        return nuevoUsuario;
    }

    @Transactional
    public Usuario actualizarUsuario(Long id, Usuario datosActualizados) {
        log.info("Actualizando datos del usuario ID: {}", id);
        Usuario usuarioDb = obtenerPorId(id);

        if (!usuarioDb.getEmail().equals(datosActualizados.getEmail())) {
            Optional<Usuario> existente = usuarioRepository.findByEmail(datosActualizados.getEmail());
            if (existente.isPresent()) {
                throw new RuntimeException("El nuevo email ya está en uso por otro usuario");
            }
        }

        usuarioDb.setNombre(datosActualizados.getNombre());
        usuarioDb.setEmail(datosActualizados.getEmail());
        usuarioDb.setRol(datosActualizados.getRol());
        usuarioDb.setActivo(datosActualizados.getActivo());

        if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().isBlank()) {
            usuarioDb.setPassword(datosActualizados.getPassword());
        }

        return usuarioRepository.save(usuarioDb);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        log.info("Desactivando (baja lógica) usuario con ID: {}", id);
        Usuario usuarioDb = obtenerPorId(id);
        usuarioDb.setActivo(false);
        usuarioRepository.save(usuarioDb);
        log.info("Usuario ID: {} marcado como inactivo", id);
    }
}