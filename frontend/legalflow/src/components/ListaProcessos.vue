<template>
  <q-page class="main-page flex">
    <q-card class="container-card q-pa-lg">
      <q-card-section>
        <q-table
          :loading="loading"
          :rows="processos"
          :columns="columns"
          row-key="id"
          no-data-label="Não há processos disponíveis com este status."
          v-model:pagination="pagination"
          :rows-per-page-options="[10, 25, 50, 100, 0]"
        >
          <template v-slot:body-cell-details="props">
            <q-td :props="props" auto-width>
              <q-btn-group unelevated>
                <q-btn
                  size="md"
                  round
                  icon="open_in_new"
                  @click="abrirModalDetalhesProcesso(props.row)"
                >
                </q-btn>
              </q-btn-group>
            </q-td>
          </template>
          <template v-slot:body-cell-actions="props">
            <q-td :props="props" auto-width>
              <q-btn-group unelevated>
                <q-btn
                  size="md"
                  round
                  icon="file_open"
                  @click="baixarProcesso(props.row)"
                >
                  <q-tooltip>Abrir processo em nova guia</q-tooltip>
                </q-btn>
                <q-btn
                  size="md"
                  round
                  icon="block"
                  @click="arquivarProcesso(props.row)"
                  :disabled="this.statusAtual === 'ARQUIVADO'"
                >
                  <q-tooltip>Arquivar processo</q-tooltip>
                </q-btn>
              </q-btn-group>
            </q-td>
          </template>
        </q-table>
      </q-card-section>
    </q-card>

    <q-dialog persistent v-model="modalDetalhesProcessoOpen">
      <q-card class="q-pa-md main-card">
        <q-card-section class="row items-center q-pb-none">
          <div class="col-6">
            <div class="text-h6">Detalhes</div>
          </div>
          <div class="col-6 text-right">
            <q-select
              stack-label
              v-model="processoSelecionado.status"
              :options="statusList"
              label="Status"
              outlined
              required
              :disable="!editandoProcesso"
              :rules="[(val) => !!val || 'Status é obrigatório']"
            />
          </div>
        </q-card-section>

        <q-card-section>
          <div class="q-gutter-md">
            <q-select
              v-show="userRole === 'ADMIN'"
              stack-label
              v-model="processoSelecionado.quadroAtual"
              :options="listaQuadros"
              label="Quadro"
              outlined
              required
              :disable="!editandoProcesso"
              :rules="[(val) => !!val || 'Quadro é obrigatório']"
            />
            <q-input
              stack-label
              :disable="!editandoProcesso"
              v-model="processoSelecionado.numero"
              label="Número do processo"
              outlined
              required
              :rules="[
                (val) => !!val || 'Número do processo é obrigatório',
                (val) => val.length <= 100 || 'Máximo de 100 caracteres',
              ]"
            />
            <q-input
              stack-label
              :disable="!editandoProcesso"
              v-model="processoSelecionado.nome"
              label="Nome"
              outlined
              required
              :rules="[
                (val) => !!val || 'Nome é obrigatório',
                (val) => val.length <= 100 || 'Máximo de 100 caracteres',
              ]"
            />
            <q-input
              stack-label
              :disable="!editandoProcesso"
              v-model="processoSelecionado.autor"
              label="Autor"
              outlined
              required
              :rules="[
                (val) => !!val || 'Autor é obrigatório',
                (val) => val.length <= 100 || 'Máximo de 100 caracteres',
              ]"
            />
            <q-input
              stack-label
              :disable="!editandoProcesso"
              v-model="processoSelecionado.reu"
              label="Réu"
              outlined
              required
              :rules="[
                (val) => !!val || 'Réu é obrigatório',
                (val) => val.length <= 100 || 'Máximo de 100 caracteres',
              ]"
            />

            <div class="row">
              <div class="col date-input-left">
                <q-input
                  :disable="!editandoProcesso"
                  stack-label
                  label="Prazo de subsídio"
                  outlined
                  required
                  v-model="processoSelecionado.prazoSubsidio"
                  mask="##/##/####"
                  :rules="[
                    (val) => !!val || 'Prazo de subsídio é obrigatório',
                    (val) => {
                      const dateParts = val.split('/');
                      const day = parseInt(dateParts[0]);
                      const month = parseInt(dateParts[1]);
                      if (day > 31 || day < 1) {
                        return 'Dia inválido';
                      }
                      if (month > 12 || month < 1) {
                        return 'Mês inválido';
                      }
                      return true;
                    },
                  ]"
                >
                  <template v-slot:append>
                    <q-icon name="event" class="cursor-pointer">
                      <q-popup-proxy
                        cover
                        transition-show="scale"
                        transition-hide="scale"
                      >
                        <q-date
                          v-model="processoSelecionado.prazoSubsidio"
                          mask="DD/MM/YYYY"
                        >
                          <div class="row items-center justify-end">
                            <q-btn
                              v-close-popup
                              label="Close"
                              color="primary"
                              flat
                            />
                          </div>
                        </q-date>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
              </div>

              <div class="col date-input-right">
                <q-input
                  :disable="!editandoProcesso"
                  stack-label
                  label="Prazo fatal"
                  outlined
                  required
                  v-model="processoSelecionado.prazoFatal"
                  mask="##/##/####"
                  :rules="[
                    (val) => !!val || 'Prazo fatal é obrigatório',
                    (val) => {
                      const dateParts = val.split('/');
                      const day = parseInt(dateParts[0]);
                      const month = parseInt(dateParts[1]);
                      if (day > 31 || day < 1) {
                        return 'Dia inválido';
                      }
                      if (month > 12 || month < 1) {
                        return 'Mês inválido';
                      }
                      return true;
                    },
                  ]"
                >
                  <template v-slot:append>
                    <q-icon name="event" class="cursor-pointer">
                      <q-popup-proxy
                        cover
                        transition-show="scale"
                        transition-hide="scale"
                      >
                        <q-date
                          v-model="processoSelecionado.prazoFatal"
                          mask="DD/MM/YYYY"
                        >
                          <div class="row items-center justify-end">
                            <q-btn
                              v-close-popup
                              label="Close"
                              color="primary"
                              flat
                            />
                          </div>
                        </q-date>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
              </div>
            </div>

            <q-input
              stack-label
              v-model="processoSelecionado.descricao"
              label="Descrição"
              outlined
              required
              :disable="!editandoProcesso"
              type="textarea"
              :hint="editandoProcesso ? 'Max 1000 caracteres' : ''"
              :rules="[
                (val) => !!val || 'Descrição é obrigatória',
                (val) => val.length <= 1000 || 'Máximo de 1000 caracteres',
              ]"
            />

            <q-card-actions>
              <div class="flex-container">
                <div class="left-buttons">
                  <q-btn
                    v-show="userRole === 'ADMIN'"
                    unelevated
                    @click="confirmaDeletarProcesso(processoSelecionado)"
                    label="Deletar Processo"
                    color="black"
                    no-caps
                    :disable="loading"
                  />
                </div>
                <div class="right-buttons">
                  <q-btn
                    v-show="!editandoProcesso"
                    flat
                    @click="editandoProcesso = false"
                    label="Fechar"
                    color="black"
                    no-caps
                    v-close-popup
                  />
                  <q-btn
                    v-show="editandoProcesso"
                    flat
                    @click="editandoProcesso = false"
                    label="Cancelar"
                    color="black"
                    no-caps
                  />
                  <q-btn
                    v-show="!editandoProcesso"
                    unelevated
                    @click="editandoProcesso = true"
                    label="Editar Processo"
                    color="teal"
                    no-caps
                  />
                  <q-btn
                    :loading="loading"
                    v-show="editandoProcesso"
                    unelevated
                    @click="editarProcesso(processoSelecionado)"
                    label="Salvar Processo"
                    color="teal"
                    no-caps
                    :disable="
                      loading ||
                      processoSelecionado.status === '' ||
                      processoSelecionado.status === undefined ||
                      processoSelecionado.nome === '' ||
                      processoSelecionado.nome.length >= 100 ||
                      processoSelecionado.numero === '' ||
                      processoSelecionado.numero.length >= 100 ||
                      processoSelecionado.autor === '' ||
                      processoSelecionado.autor.length >= 100 ||
                      processoSelecionado.reu === '' ||
                      processoSelecionado.reu.length >= 100 ||
                      processoSelecionado.prazoSubsidio === '' ||
                      processoSelecionado.prazoFatal === '' ||
                      processoSelecionado.descricao === '' ||
                      processoSelecionado.descricao.length >= 1000
                    "
                  />
                </div>
              </div>
            </q-card-actions>
          </div>
        </q-card-section>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script>
import { defineComponent } from "vue";
import ProcessoService from "@/services/ProcessoService";
import NotificationUtil from "@/utils/NotificationUtil";
import { formatDate, formatStatus, formatTextSize } from "@/utils/formatters";

export default defineComponent({
  name: "ListaProcessos",

  props: {
    statusAtual: String,
    processos: Array,
    idQuadroAtual: Number,
    quadroAtual: Object,
    userRole: String,
    listaQuadros: Array,
  },

  data() {
    return {
      loading: false,
      modalDetalhesProcessoOpen: false,
      editandoProcesso: false,
      quadroSelecionado: this.quadroAtual,
      processoSelecionado: {},
      pagination: {
        rowsPerPage: 10,
      },
      statusList: [
        {
          label: "Criado",
          value: "CRIADO",
        },
        {
          label: "Em Progresso",
          value: "EM_PROGRESSO",
        },
        {
          label: "Finalizado",
          value: "FINALIZADO",
        },
        {
          label: "Arquivado",
          value: "ARQUIVADO",
        },
      ],
      columns: [
        {
          name: "details",
          label: "Detalhes",
          align: "left",
        },
        {
          name: "numero",
          required: true,
          label: "Número",
          align: "left",
          field: (row) => row.numero,
          format: (val) => `${formatTextSize(val, 25)}`,
          sortable: true,
        },
        {
          name: "nome",
          required: true,
          label: "Nome",
          align: "left",
          field: (row) => row.nome,
          format: (val) => `${formatTextSize(val, 30)}`,
          sortable: true,
        },
        {
          name: "autor",
          required: true,
          label: "Autor",
          align: "left",
          field: (row) => row.autor,
          format: (val) => `${formatTextSize(val, 20)}`,
          sortable: true,
        },
        {
          name: "reu",
          required: true,
          label: "Réu",
          align: "left",
          field: (row) => row.reu,
          format: (val) => `${formatTextSize(val, 20)}`,
          sortable: true,
        },
        {
          name: "prazoSubsidio",
          required: true,
          label: "Prazo Subsídio",
          align: "left",
          field: (row) => row.prazoSubsidio,
          format: (val) => `${formatDate(val)}`,
          sortable: true,
        },
        {
          name: "prazoFatal",
          required: true,
          label: "Prazo Fatal",
          align: "left",
          field: (row) => row.prazoFatal,
          format: (val) => `${formatDate(val)}`,
          sortable: true,
        },
        {
          name: "actions",
          label: "Ações",
          align: "center",
        },
      ],
    };
  },

  methods: {
    async baixarProcesso(processo) {
      this.loading = true;
      await ProcessoService.baixarProcesso(processo.id)
        .then((response) => {
          this.loading = false;
          const blob = new Blob([response.data], { type: "application/pdf" });
          const fileURL = URL.createObjectURL(blob);

          window.open(fileURL, "_blank");
        })
        .catch((err) => {
          this.loading = false;
          NotificationUtil.returnFeedbackMessage(
            this.$q,
            err.response?.data || "Erro ao baixar processo",
            "negative",
            "red"
          );
        });
    },
    arquivarProcesso(processo) {
      let processoSelecionado = {
        ...processo,
        status: "Arquivado",
        prazoSubsidio: formatDate(processo.prazoSubsidio),
        prazoFatal: formatDate(processo.prazoFatal),
      };
      this.salvarProcesso(processoSelecionado);
    },
    async salvarProcesso(processo) {
      this.loading = true;
      processo.quadroId = this.idQuadroAtual;

      if (typeof processo.status !== "string") {
        processo.status = processo.status.label;
      }

      const formData = new FormData();

      formData.append(
        "processo",
        new Blob([JSON.stringify(processo)], {
          type: "application/json",
        })
      );

      formData.append(
        "arquivo",
        new Blob([processo.arquivo], { type: "application/pdf" })
      );

      await ProcessoService.salvarProcesso(formData)
        .then(() => {
          this.loading = false;
          this.$emit("salvar-processo", processo);
          this.modalDetalhesProcessoOpen = false;
          this.editandoProcesso = false;
        })
        .catch((err) => {
          this.loading = false;
          NotificationUtil.returnFeedbackMessage(
            this.$q,
            err.response?.data || "Erro ao salvar processo",
            "negative",
            "red"
          );
        });
    },
    async editarProcesso(processo) {
      this.loading = true;

      processo.quadroId = processo.quadroAtual.value;

      if (typeof processo.status !== "string") {
        processo.status = processo.status.label;
      }

      await ProcessoService.editarProcesso(processo)
        .then(() => {
          this.loading = false;
          this.$emit("salvar-processo", processo);
          this.modalDetalhesProcessoOpen = false;
          this.editandoProcesso = false;
        })
        .catch((err) => {
          this.loading = false;
          NotificationUtil.returnFeedbackMessage(
            this.$q,
            err.response?.data || "Erro ao editar processo",
            "negative",
            "red"
          );
        });
    },
    confirmaDeletarProcesso(processo) {
      const title = "Deletar processo";
      const message = "Tem certeza que deseja excluir este processo?";

      this.$q
        .dialog({
          title: title,
          message: message,
          persistent: true,
          ok: {
            label: "Sim",
            color: "black",
            noCaps: true,
          },
          cancel: {
            label: "Não",
            color: "black",
            noCaps: true,
            flat: true,
          },
        })
        .onOk(() => {
          this.deletarProcesso(processo);
        });
    },
    deletarProcesso(processo) {
      this.loading = true;
      ProcessoService.deletarProcesso(processo.id)
        .then(() => {
          this.loading = false;
          this.$emit("deletar-processo");
          this.modalDetalhesProcessoOpen = false;
        })
        .catch((err) => {
          this.loading = false;
          NotificationUtil.returnFeedbackMessage(
            this.$q,
            err.response?.data || "Erro ao deletar processo",
            "negative",
            "red"
          );
        });
    },
    abrirModalDetalhesProcesso(processo) {
      this.processoSelecionado = {
        ...processo,
        status: formatStatus(this.statusList, processo.status),
        prazoSubsidio: formatDate(processo.prazoSubsidio),
        prazoFatal: formatDate(processo.prazoFatal),
      };
      this.modalDetalhesProcessoOpen = true;
    },
  },
});
</script>
