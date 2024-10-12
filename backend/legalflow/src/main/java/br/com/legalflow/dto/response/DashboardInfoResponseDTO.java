package br.com.legalflow.dto.response;

import br.com.legalflow.entity.Processo;
import lombok.Data;

import java.util.List;

@Data
public class DashboardInfoResponseDTO {

    private Long totalProcessosEmAberto;
    private List<Processo> processosAVencerPrazoSubsidio;
    private List<Processo> processosAVencerPrazoFatal;
    private List<Processo> processosAVencer;
}
