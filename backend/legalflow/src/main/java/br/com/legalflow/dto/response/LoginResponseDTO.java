package br.com.legalflow.dto.response;

import br.com.legalflow.entity.Usuario;
import lombok.Data;

@Data
public class LoginResponseDTO {

    private String token;
    private Usuario usuario;
    private Long organizacaoId;

    public LoginResponseDTO(Usuario usuario, String token) {
        this.token = token;
        this.usuario = usuario;
        this.organizacaoId = usuario.getOrganizacao().getId();
    }

}
