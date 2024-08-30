package br.com.legalflow.dto.request;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String senha;

    public LoginRequestDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }
}
