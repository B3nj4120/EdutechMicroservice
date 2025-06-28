package com.microservice.usuario.repository;

import com.microservice.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    //Buscar un usuario por su nombre de usuario
    Optional<Usuario> findByUsername(String username);

    //buscar usuario por correo
    Optional<Usuario> findByEmail(String email);

    //validar si ya existe un usuario con ese nombre
    boolean existsByUsername(String username);

    //validar si ya existe un correo registrado
    boolean existsByEmail(String email); 
}
