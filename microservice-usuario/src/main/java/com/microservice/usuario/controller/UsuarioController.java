package com.microservice.usuario.controller;

import com.microservice.usuario.model.Usuario;
import com.microservice.usuario.service.UsuarioService;
import com.microservice.usuario.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.*;
import jakarta.validation.Valid;

import java.util.*;
import java.time.LocalDateTime;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios") 
public class UsuarioController {

   @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listar")
    public List<Usuario> getAllUsers() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id_usuario}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id_usuario) {
        Optional<Usuario> usuario = usuarioService.findById(id_usuario);

        if (usuario.isPresent()) {
            Usuario u = usuario.get();
            UsuarioDTO dto = new UsuarioDTO();
            dto.setId(u.getId());
            dto.setUsername(u.getUsername());
            dto.setPassword(u.getPassword());
            dto.setEmail(u.getEmail());
            dto.setRol(u.getRol());

            return ResponseEntity.ok()
                    .header("mi-encabezado", "valor")
                    .body(dto);
        } else {
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("message", "No se encontró el usuario con ese ID: " + id_usuario);
            errorBody.put("status", "404");
            errorBody.put("timestamp", LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario usuario = new Usuario();
            usuario.setUsername(usuarioDTO.getUsername());
            usuario.setPassword(usuarioDTO.getPassword());
            usuario.setEmail(usuarioDTO.getEmail());
            usuario.setRol(usuarioDTO.getRol());

            Usuario guardado = usuarioService.save(usuario);

            UsuarioDTO responseDTO = new UsuarioDTO();
            responseDTO.setId(guardado.getId());
            responseDTO.setUsername(guardado.getUsername());
            responseDTO.setPassword(guardado.getPassword());
            responseDTO.setEmail(guardado.getEmail());
            responseDTO.setRol(guardado.getRol());

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(guardado.getId())
                    .toUri();

            return ResponseEntity.created(location).body(responseDTO);

        } catch (DataIntegrityViolationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "El correo o username ya está registrado");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    @PutMapping("/{id_usuario}")
    public ResponseEntity<UsuarioDTO> update(@PathVariable int id_usuario, @RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario usuario = new Usuario();
            usuario.setId(id_usuario);
            usuario.setUsername(usuarioDTO.getUsername());
            usuario.setPassword(usuarioDTO.getPassword());
            usuario.setEmail(usuarioDTO.getEmail());
            usuario.setRol(usuarioDTO.getRol());

            Usuario actualizado = usuarioService.save(usuario);

            UsuarioDTO responseDTO = new UsuarioDTO();
            responseDTO.setId(actualizado.getId());
            responseDTO.setUsername(actualizado.getUsername());
            responseDTO.setPassword(actualizado.getPassword());
            responseDTO.setEmail(actualizado.getEmail());
            responseDTO.setRol(actualizado.getRol());

            return ResponseEntity.ok(responseDTO);

        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id_usuario}")
    public ResponseEntity<?> eliminar(@PathVariable int id_usuario) {
        try {
            usuarioService.delete(id_usuario);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
