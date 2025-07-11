package com.microservice.usuario;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.usuario.controller.UsuarioController;
import com.microservice.usuario.dto.UsuarioDTO;
import com.microservice.usuario.service.UsuarioService;
import com.microservice.usuario.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

     @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    private static final ObjectMapper mapper = new ObjectMapper();

    private Usuario getDemoUsuario() {
    return Usuario.builder()
            .username("benja")
            .password("123")
            .email("benja@mail.com")
            .rol("ADMIN")
            .build();
}

    @Test
    void testGetAllUsers() throws Exception {
        when(usuarioService.findAll()).thenReturn(List.of(getDemoUsuario()));

        mockMvc.perform(get("/api/v1/usuarios/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("benja"));
    }

    @Test
    void testGetUserByIdFound() throws Exception {
        when(usuarioService.findById(1)).thenReturn(Optional.of(getDemoUsuario()));

        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("benja"));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        when(usuarioService.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveUsuario() throws Exception {
        UsuarioDTO dto = new UsuarioDTO(0, "benja", "123", "benja@mail.com", "ADMIN");
        Usuario saved = getDemoUsuario();
        when(usuarioService.save(any())).thenReturn(saved);

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("benja"));
    }

}
