package com.microservice.usuario.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private String benja;

    public UsuarioDTO(int par, Object benja, String string, String benjamailcom, String admin) {
        this.benja = (String) benja;
    }

    private Integer id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @Email(message = "El correo debe ser válido")
    private String email;

    private String rol;

}
