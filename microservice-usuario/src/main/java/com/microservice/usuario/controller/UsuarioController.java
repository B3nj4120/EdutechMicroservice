package com.microservice.usuario.controller;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.microservice.usuario.dto.UsuarioDTO;
import com.microservice.usuario.model.Usuario;
import com.microservice.usuario.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.Getter;

@Getter
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

     @Autowired
    private UsuarioService usuarioService;

    // GET ALL with HATEOAS
    @GetMapping("/listar")
    public CollectionModel<EntityModel<UsuarioDTO>> getAllUsers() {
        List<EntityModel<UsuarioDTO>> usuarios = usuarioService.findAll().stream()
            .map(usuario -> {
                UsuarioDTO dto = new UsuarioDTO(usuario.getId(), usuario.getUsername(), usuario.getPassword(), usuario.getEmail(), usuario.getRol());
                return EntityModel.of(dto,
                        linkTo(methodOn(UsuarioController.class).getUserById(usuario.getId())).withSelfRel(),
                        linkTo(methodOn(UsuarioController.class).getAllUsers()).withRel("usuarios"));
            })
            .collect(Collectors.toList());

        return CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioController.class).getAllUsers()).withSelfRel());
    }

    // GET BY ID with HATEOAS
    @GetMapping("/{id_usuario}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id_usuario) {
        Optional<Usuario> usuario = usuarioService.findById(id_usuario);

        if (usuario.isPresent()) {
            Usuario u = usuario.get();
            UsuarioDTO dto = new UsuarioDTO(u.getId(), u.getUsername(), u.getPassword(), u.getEmail(), u.getRol());

            EntityModel<UsuarioDTO> resource = EntityModel.of(dto,
                    linkTo(methodOn(UsuarioController.class).getUserById(id_usuario)).withSelfRel(),
                    linkTo(methodOn(UsuarioController.class).getAllUsers()).withRel("usuarios"));

            return ResponseEntity.ok(resource);
        } else {
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("message", "No se encontró el usuario con ese ID: " + id_usuario);
            errorBody.put("status", "404");
            errorBody.put("timestamp", LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
        }
    }

    // POST
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario usuario = new Usuario();
            usuario.setUsername(usuarioDTO.getUsername());
            usuario.setPassword(usuarioDTO.getPassword());
            usuario.setEmail(usuarioDTO.getEmail());
            usuario.setRol(usuarioDTO.getRol());

            Usuario guardado = usuarioService.save(usuario);

            UsuarioDTO responseDTO = new UsuarioDTO(guardado.getId(), guardado.getUsername(), guardado.getPassword(), guardado.getEmail(), guardado.getRol());

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

    // PUT
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

            UsuarioDTO responseDTO = new UsuarioDTO(actualizado.getId(), actualizado.getUsername(), actualizado.getPassword(), actualizado.getEmail(), actualizado.getRol());

            return ResponseEntity.ok(responseDTO);

        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE
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
