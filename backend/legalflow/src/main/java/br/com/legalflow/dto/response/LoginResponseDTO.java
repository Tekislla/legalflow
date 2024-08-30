package br.com.legalflow.dto.response;

import br.com.legalflow.entity.Usuario;
import lombok.Data;

@Data
public class LoginResponseDTO {

    private Long usuarioId;
    private Long organizacaoId;
    private String nome;
    private String email;
    private String token;

    public LoginResponseDTO(Usuario usuario, String token) {
        this.token = token;
        this.usuarioId = usuario.getId();
        this.organizacaoId = usuario.getOrganizacao().getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
    }
}
