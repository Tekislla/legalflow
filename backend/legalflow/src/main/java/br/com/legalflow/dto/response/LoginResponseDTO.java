package br.com.legalflow.dto.response;

import br.com.legalflow.entity.Usuario;
import lombok.Data;

@Data
public class LoginResponseDTO {

    private String token;
    private Usuario usuario;

    public LoginResponseDTO(Usuario usuario, String token) {
        this.token = token;
        this.usuario = usuario;
    }

}
