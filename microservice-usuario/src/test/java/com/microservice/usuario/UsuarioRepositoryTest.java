package com.microservice.usuario;


import com.microservice.usuario.model.Usuario;
import com.microservice.usuario.repository.UsuarioRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UsuarioRepositoryTest {

     @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testSaveAndFindByUsername() {
        Usuario u = ((Object) Usuario.builder())
                .username("benja")
                .password("123")
                .email("benja@mail.com")
                .rol("ADMIN")
                .build();

        usuarioRepository.save(u);

        boolean exists = usuarioRepository.existsByUsername("benja");
        assertThat(exists).isTrue();
    }

}
