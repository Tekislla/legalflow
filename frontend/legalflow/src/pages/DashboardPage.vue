<template>
  <q-page class="main-page flex">
    <div class="container text-center q-pa-md flex flex-center">
      <q-card class="container-card q-pa-lg">
        <h4>Olá, {{ userName }}!</h4>
        <q-card-section class="main-section q-gutter-md">
          <q-card bordered class="dashboard-card q-pa-lg">
            <q-card-section>
              <q-item-label>
                <strong>Total de processos em aberto</strong>
              </q-item-label>
              <q-separator class="separator" inset />
              <q-item-label>
                <q-spinner
                  color="teal"
                  size="2em"
                  :thickness="2"
                  v-show="loading === true"
                />
                {{ totalProcessos }}
              </q-item-label>
            </q-card-section>
          </q-card>

          <q-card bordered class="dashboard-card q-pa-lg">
            <q-card-section>
              <q-item-label>
                <strong>Processos a vencer/vencidos (prazo de subsídio)</strong>
              </q-item-label>
              <q-separator class="separator" inset />
              <q-item-label>
                <q-spinner
                  color="teal"
                  size="2em"
                  :thickness="2"
                  v-show="loading === true"
                />
                {{ processosAVencerPrazoSubsidio }}
              </q-item-label>
            </q-card-section>
          </q-card>

          <q-card bordered class="dashboard-card q-pa-lg">
            <q-card-section>
              <q-item-label>
                <strong>Processos a vencer/vencidos (prazo fatal)</strong>
              </q-item-label>
              <q-separator class="separator" inset />
              <q-item-label>
                <q-spinner
                  color="teal"
                  size="2em"
                  :thickness="2"
                  v-show="loading === true"
                />
                {{ processosAVencerPrazoFatal }}
              </q-item-label>
            </q-card-section>
          </q-card>
        </q-card-section>

        <q-card-section>
          <q-table
            :rows="processosAVencer"
            :columns="columns"
            :loading="loading"
            row-key="id"
            no-data-label="Não há processos vencidos/próximos ao vencimento."
            v-model:pagination="pagination"
            :rows-per-page-options="[10, 25, 50, 100, 0]"
          >
          </q-table>
        </q-card-section>
      </q-card>
    </div>
  </q-page>
</template>

<script>
import { defineComponent } from "vue";
import { formatDate, formatTextSize } from "../utils/formatters";
import ProcessoService from "../services/ProcessoService";
import NotificationUtil from "@/utils/NotificationUtil";

export default defineComponent({
  name: "DashboardPage",

  props: {
    userName: String,
    organizacaoId: Number,
  },

  data() {
    return {
      loading: false,
      totalProcessos: null,
      processosAVencerPrazoSubsidio: null,
      processosAVencerPrazoFatal: null,
      processosAVencer: [],
      pagination: {
        rowsPerPage: 10,
      },
      columns: [
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
      ],
    };
  },

  methods: {
    async fetch() {
      await this.getDashboardInfo();
    },
    async getDashboardInfo() {
      this.loading = true;

      await ProcessoService.getDashboardInfo(this.organizacaoId)
        .then((response) => {
          this.totalProcessos = response.data.totalProcessosEmAberto;
          this.processosAVencerPrazoSubsidio =
            response.data.processosAVencerPrazoSubsidio.length;
          this.processosAVencerPrazoFatal =
            response.data.processosAVencerPrazoFatal.length;
          this.processosAVencer = response.data.processosAVencer;
          this.loading = false;
        })
        .catch((err) => {
          this.loading = false;
          NotificationUtil.returnFeedbackMessage(
            this.$q,
            err.response?.data || "Falha ao carregar informações do dashboard",
            "negative",
            "red"
          );
        });
    },
  },

  mounted() {
    this.fetch();
  },
});
</script>
