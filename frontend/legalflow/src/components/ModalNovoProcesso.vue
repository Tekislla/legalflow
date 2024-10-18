<template>
  <q-dialog persistent>
    <q-card class="q-pa-md main-card">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">Novo Processo</div>
        <q-space />
      </q-card-section>

      <q-card-section>
        <div class="q-gutter-md">
          <q-input
            stack-label
            v-model="processo.numero"
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
            v-model="processo.nome"
            label="Nome"
            outlined
            required
            :rules="[
              (val) => !!val || 'Nome do processo é obrigatório',
              (val) => val.length <= 100 || 'Máximo de 100 caracteres',
            ]"
          />

          <q-input
            stack-label
            v-model="processo.autor"
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
            v-model="processo.reu"
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
                stack-label
                label="Prazo de subsídio"
                outlined
                required
                v-model="processo.prazoSubsidio"
                mask="##/##/####"
                :rules="[
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
                        v-model="processo.prazoSubsidio"
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
                stack-label
                label="Prazo fatal"
                outlined
                required
                v-model="processo.prazoFatal"
                mask="##/##/####"
                :rules="[
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
                      <q-date v-model="processo.prazoFatal" mask="DD/MM/YYYY">
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
            v-model="processo.descricao"
            label="Descrição"
            outlined
            required
            type="textarea"
            hint="Máximo 1000 caracteres"
            :rules="[
              (val) => !!val || 'Descrição é obrigatória',
              (val) => val.length <= 1000 || 'Máximo de 1000 caracteres',
            ]"
          />

          <q-file
            stack-label
            clearable
            v-model="arquivo"
            label="Arquivo"
            outlined
            required
            accept="application/pdf"
            :rules="[
              (val) => !!val || 'Arquivo é obrigatório',
              (val) => val.size < 10000000 || 'Arquivo muito grande',
            ]"
          >
            <template v-slot:append>
              <q-icon name="attach_file" />
            </template>
          </q-file>

          <q-card-actions align="right">
            <q-btn
              flat
              label="Cancelar"
              color="black"
              no-caps
              v-close-popup
              @click="clearForm()"
            />
            <q-btn
              :loading="loading"
              @click="submitFormNovoProcesso()"
              unelevated
              size="md"
              label="Criar processo"
              no-caps
              color="teal"
              :disable="
                loading ||
                !processo.nome ||
                processo.nome.length > 100 ||
                !processo.numero ||
                processo.numero.length > 100 ||
                !processo.autor ||
                processo.autor.length > 100 ||
                !processo.reu ||
                processo.reu.length > 100 ||
                !processo.descricao ||
                !processo.prazoSubsidio ||
                !processo.prazoFatal ||
                !arquivo ||
                processo.descricao.length > 1000
              "
            />
          </q-card-actions>
        </div>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>
<script>
import { defineComponent } from "vue";
import ProcessoService from "@/services/ProcessoService";
import NotificationUtil from "@/utils/NotificationUtil";

export default defineComponent({
  name: "ModalNovoProcesso",

  props: {
    idQuadroAtual: {
      type: Number,
    },
  },

  data() {
    return {
      loading: false,
      processo: {
        nome: "",
        numero: "",
        autor: "",
        reu: "",
        descricao: "",
        prazoSubsidio: "",
        prazoFatal: "",
        quadroId: "",
        status: "Criado",
      },
      arquivo: null,
      persistent: true,
    };
  },

  methods: {
    async submitFormNovoProcesso() {
      this.loading = true;

      this.processo.quadroId = this.idQuadroAtual;
      const formData = new FormData();

      formData.append(
        "processo",
        new Blob([JSON.stringify(this.processo)], {
          type: "application/json",
        })
      );

      formData.append("arquivo", this.arquivo);

      await ProcessoService.salvarProcesso(formData)
        .then(() => {
          this.loading = false;
          this.$emit("processo-criado");
        })
        .catch((err) => {
          this.loading = false;
          NotificationUtil.returnFeedbackMessage(
            this.$q,
            err.response?.data || "Erro ao criar processo",
            "negative",
            "red"
          );
        });

      this.clearForm();
    },
    clearForm() {
      this.processo = {
        nome: "",
        numero: "",
        autor: "",
        reu: "",
        descricao: "",
        prazoSubsidio: "",
        prazoFatal: "",
        quadroId: this.idQuadroAtual,
        status: "Criado",
      };
      this.arquivo = null;
    },
  },
});
</script>
