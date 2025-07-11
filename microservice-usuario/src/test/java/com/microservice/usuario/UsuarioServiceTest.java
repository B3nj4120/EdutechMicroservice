package com.microservice.usuario;


import com.microservice.usuario.model.Usuario;
import com.microservice.usuario.repository.UsuarioRepository;
import com.microservice.usuario.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

      @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    private Usuario crearUsuarioDemo() {
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
        when(usuarioRepository.findAll()).thenReturn(List.of(crearUsuarioDemo()));
        
        List<Usuario> usuarios = usuarioService.findAll();
        
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals("benja", usuarios.get(0).getUsername());
    }

    @Test
    void testFindById() {
        Usuario usuario = crearUsuarioDemo();
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        Optional<Usuario> encontrado = usuarioService.findById(1);

        assertTrue(encontrado.isPresent());
        assertEquals("benja", encontrado.get().getUsername());
    }

    @Test
    void testSave() {
        Usuario usuario = crearUsuarioDemo();
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario guardado = usuarioService.save(usuario);

        assertNotNull(guardado);
        assertEquals("benja", guardado.getUsername());
    }

    @Test
    void testDelete() {
        doNothing().when(usuarioRepository).deleteById(1);

        usuarioService.delete(1);

        verify(usuarioRepository, times(1)).deleteById(1);
    }


}
