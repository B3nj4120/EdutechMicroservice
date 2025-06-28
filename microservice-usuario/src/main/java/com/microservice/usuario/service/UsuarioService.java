package com.microservice.usuario.service;


import com.microservice.usuario.model.Usuario;
import com.microservice.usuario.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

     @Autowired
    private UsuarioRepository usuarioRepository;

      // Obtener todos los usuarios
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

      // Buscar usuario por ID
    public Optional<Usuario> findById(int id) {
        return usuarioRepository.findById(id);
    }

      // Guardar un nuevo usuario o actualizar uno existente
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

     // Eliminar un usuario por ID
    public void delete(int id) {
        usuarioRepository.deleteById(id);
    }

    // Verificar login (sin encriptación)
    public Optional<Usuario> login(String username, String password) {
        return usuarioRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password));
    }

      // Validar si ya existe el username
      public boolean existsByUsername(String username){
        return usuarioRepository.existsByUsername(username);
      }

      // Validar si ya existe el email
      public boolean existByEmail(String email){
        return usuarioRepository.existsByEmail(email);
      }
}
