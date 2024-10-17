package br.com.legalflow.controller;

import br.com.legalflow.controller.base.BaseController;
import br.com.legalflow.dto.request.ProcessoRequestDTO;
import br.com.legalflow.entity.Processo;
import br.com.legalflow.exception.usuario.UsuarioNaoAutorizadoException;
import br.com.legalflow.service.OrganizacaoService;
import br.com.legalflow.service.ProcessoService;
import br.com.legalflow.service.QuadroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/processo")
public class ProcessoController extends BaseController {

    @Autowired
    private QuadroService quadroService;
    @Autowired
    private ProcessoService processoService;
    @Autowired
    private OrganizacaoService organizacaoService;

    @PostMapping("/")
    public ResponseEntity<?> criarProcesso(@RequestPart("processo") ProcessoRequestDTO dto,
                                           @RequestPart("arquivo") MultipartFile arquivo) {
        try {
            Processo processo = processoService.saveProcesso(dto, arquivo.getBytes());

            return ResponseEntity.ok(processo);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/")
    public ResponseEntity<?> editarProcesso(@RequestBody ProcessoRequestDTO dto) {
        try {
            Processo processo = processoService.saveProcesso(dto, null);

            return ResponseEntity.ok(processo);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadProcesso(@PathVariable Long id) {
        try {
            Processo processo = processoService.findById(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + processo.getNumero() + ".pdf\"")
                    .body(processo.getArquivo());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarProcesso(@PathVariable Long id) {
        try {
            if (!isUsuarioAdmin()) {
                throw new UsuarioNaoAutorizadoException();
            }

            processoService.deleteById(id);

            return ResponseEntity.ok("Processo deletado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/dashboard/{organizacaoId}")
    public ResponseEntity<?> getDashboardInfo(@PathVariable Long organizacaoId) {
        try {
            return ResponseEntity.ok(processoService.getDashboardInfo(organizacaoId));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
