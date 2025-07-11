package com.microservice.usuario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "rol", nullable = false)
    private String rol;

    public static Object builder() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'builder'");
    }

    public void setId(int id_usuario) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setUsername(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPassword(String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEmail(String email) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setRol(String rol) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
    