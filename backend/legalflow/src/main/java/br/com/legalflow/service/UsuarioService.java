package br.com.legalflow.service;

import br.com.legalflow.entity.Organizacao;
import br.com.legalflow.enums.RoleEnum;
import br.com.legalflow.dto.request.CadastroRequestDTO;
import br.com.legalflow.dto.request.EditarUsuarioRequestDTO;
import br.com.legalflow.entity.Usuario;
import br.com.legalflow.exception.usuario.CredenciaisInvalidasException;
import br.com.legalflow.exception.usuario.UsuarioInativoException;
import br.com.legalflow.exception.usuario.UsuarioJaCadastradoException;
import br.com.legalflow.exception.usuario.UsuarioNaoEncontradoException;
import br.com.legalflow.repository.UsuarioRepository;
import br.com.legalflow.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private QuadroService quadroService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public Usuario cadastrarUsuario(CadastroRequestDTO dto, Organizacao organizacao) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(dto.getEmail());

        if (usuarioOpt.isPresent()) {
            throw new UsuarioJaCadastradoException(dto.getEmail());
        }

        Usuario usuario = new Usuario();

        usuario.setOrganizacao(organizacao);
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenhaCrypto(passwordEncoder.encode(dto.getSenha()));
        usuario.setDataCadastro(new Date());
        usuario.setDataAtualizacao(new Date());
        usuario.setAtivo(true);
        usuario.setRole(RoleEnum.getRole(dto.isAdministrador()).toString());

        return usuarioRepository.save(usuario);
    }

    public Usuario editarUsuario(EditarUsuarioRequestDTO dto) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.getId());

        if (usuarioOpt.isEmpty()) {
            throw new UsuarioNaoEncontradoException(dto.getId());
        }

        Usuario usuario = usuarioOpt.get();

        if (dto.getSenha() != null) {
            usuario.setSenhaCrypto(passwordEncoder.encode(dto.getSenha()));
        }

        usuario.setDataAtualizacao(new Date());
        usuario.setAtivo(dto.isAtivo());
        usuario.setRole(RoleEnum.getRole(dto.isAdministrador()).toString());

        return usuarioRepository.save(usuario);
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new UsuarioNaoEncontradoException(email));
    }

    public List<Usuario> findByOrganizacaoId(Long id) {
        return usuarioRepository.findByOrganizacaoId(id);
    }

    public void excluirUsuario(Usuario usuario) {
        for (var quadro : usuario.getQuadros()) {
            quadroService.deletarQuadro(quadro);
        }

        usuarioRepository.deleteById(usuario.getId());
    }

    public void ativaDesativaUsuario(Long id, boolean ativo) {
        Usuario usuario = findById(id);
        usuario.setAtivo(ativo);
        usuarioRepository.save(usuario);
    }

    public Usuario autenticarUsuario(String email, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            throw new UsuarioNaoEncontradoException(email);
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.isAtivo()) {
            throw new UsuarioInativoException(email);
        }

        if (!passwordEncoder.matches(senha, usuario.getSenhaCrypto())) {
            throw new CredenciaisInvalidasException();
        }

        return usuario;
    }

    public String gerarToken(Usuario usuario) {
        return jwtTokenProvider.gerarToken(usuario);
    }
}
