package com.microservice.usuario.dto;

import lombok.Data;

@Data
public class UsuarioDTO {

    private Integer id;
    private String username;
    private String password;
    private String email;
    private String rol;

    public UsuarioDTO() {
        // Constructor vacío para frameworks
    }

    public UsuarioDTO(Integer id, String username, String password, String email, String rol) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.rol = rol;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

}
