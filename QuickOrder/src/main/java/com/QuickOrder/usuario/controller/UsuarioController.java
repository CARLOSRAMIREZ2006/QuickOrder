package com.QuickOrder.usuario.controller;

import com.QuickOrder.usuario.model.Usuario;
import com.QuickOrder.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        log.info("Petición REST recibida para listar usuarios");
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        log.info("Petición REST recibida para obtener usuario ID: {}", id);
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario usuario) {
        log.info("Petición REST recibida para crear usuario");
        Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuario) {
        log.info("Petición REST recibida para actualizar usuario ID: {}", id);
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        log.info("Petición REST recibida para eliminar (desactivar) usuario ID: {}", id);
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}