package com.microservice.usuario;


import com.microservice.usuario.model.Usuario;
import com.microservice.usuario.repository.UsuarioRepository;
import com.microservice.usuario.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    
  @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    private Usuario getDemoUsuario() {
        return Usuario.builder()
                .id(1)
                .username("benja")
                .password("123")
                .email("benja@mail.com")
                .rol("ADMIN")
                .build();
    }

    @Test
    void testFindAll() {
        when(usuarioRepository.findAll()).thenReturn(List.of(getDemoUsuario()));

        List<Usuario> usuarios = usuarioService.findAll();

        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals("benja", usuarios.get(0).getUsername());
    }

    @Test
    void testFindByIdFound() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(getDemoUsuario()));

        Optional<Usuario> usuario = usuarioService.findById(1);

        assertTrue(usuario.isPresent());
        assertEquals("benja", usuario.get().getUsername());
    }

    @Test
    void testSave() {
        Usuario u = getDemoUsuario();
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(u);

        Usuario saved = usuarioService.save(u);

        assertNotNull(saved);
        assertEquals("benja", saved.getUsername());
    }

    @Test
    void testDelete() {
        doNothing().when(usuarioRepository).deleteById(1);

        usuarioService.delete(1);

        verify(usuarioRepository, times(1)).deleteById(1);
    }

    @Test
    void testExistsByUsername() {
        when(usuarioRepository.existsByUsername("benja")).thenReturn(true);

        assertTrue(usuarioService.existsByUsername("benja"));
    }

    @Test
    void testExistByEmail() {
        when(usuarioRepository.existsByEmail("benja@mail.com")).thenReturn(true);

        assertTrue(usuarioService.existByEmail("benja@mail.com"));
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }

    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


}
