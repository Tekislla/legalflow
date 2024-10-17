package br.com.legalflow.enums;

public enum ProcessoStatusEnum {
    CRIADO("CRIADO"),
    EM_PROGRESSO("EM PROGRESSO"),
    FINALIZADO("FINALIZADO"),
    ARQUIVADO("ARQUIVADO");

    final String descricao;

    ProcessoStatusEnum(String descricao) {
        this.descricao = descricao;
    }

    public static ProcessoStatusEnum getProcessoStatusEnum(String descricao) {
        descricao = descricao.toUpperCase();
        for (ProcessoStatusEnum processoStatusEnum : ProcessoStatusEnum.values()) {
            if (processoStatusEnum.descricao.equals(descricao)) {
                return processoStatusEnum;
            }
        }
        return null;
    }
}
