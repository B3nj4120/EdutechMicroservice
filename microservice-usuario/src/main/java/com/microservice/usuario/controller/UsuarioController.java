package com.microservice.usuario.controller;

import com.microservice.usuario.model.Usuario;
import com.microservice.usuario.service.UsuarioService;
import com.microservice.usuario.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

   /**
 * @return
 */
    @GetMapping("/listar")
    public CollectionModel<EntityModel<UsuarioDTO>> getAllUsers() {
        List<Object> usuarios = usuarioService.findAll().stream()
            .map(u -> extracted(u))
            .toList();

         return CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioController.class).getAllUsers()).withSelfRel());
    }

   private Object extracted(Usuario u) {
    UsuarioDTO dto = new UsuarioDTO(u.getId(), u.getUsername(), u.getPassword(), u.getEmail(), u.getRol());
        return EntityModel.of(dto,
         linkTo(methodOn(UsuarioController.class).getUserById(u.getId())).withSelfRel()
    );
   }

    @GetMapping("/{id_usuario}")
        public ResponseEntity<?> getUserById(@PathVariable Integer id_usuario) {
            Optional<Usuario> usuario = usuarioService.findById(id_usuario);

            if (usuario.isPresent()) {
                Usuario u = usuario.get();
                UsuarioDTO dto = new UsuarioDTO(u.getId(), u.getUsername(), u.getPassword(), u.getEmail(), u.getRol());

                EntityModel<UsuarioDTO> resource = EntityModel.of(dto);
                 resource.add(((Object) linkTo(methodOn(UsuarioController.class).getUserById(id_usuario))).withSelfRel());
                 resource.add(((Object) linkTo(methodOn(UsuarioController.class).getAllUsers())).withRel("usuarios"));

             return ResponseEntity.ok(resource);
            } else {
                     Map<String, String> errorBody = new HashMap<>();
                    errorBody.put("message", "No se encontró el usuario con ese ID: " + id_usuario);
                    errorBody.put("status", "404");
                     errorBody.put("timestamp", LocalDateTime.now().toString());
                 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
             }
}   

    private Object linkTo(com.microservice.usuario.controller.CollectionModel<com.microservice.usuario.controller.EntityModel<UsuarioDTO>> userById) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'linkTo'");
    }

    private UsuarioController methodOn(Class<UsuarioController> class1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'methodOn'");
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

            UsuarioDTO responseDTO = new UsuarioDTO(0, "benja", "123", "benja@mail.com", "ADMIN");
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

            UsuarioDTO responseDTO = new UsuarioDTO(0, "benja", "123", "benja@mail.com", "ADMIN");
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
