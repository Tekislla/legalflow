<template>
  <div>
    <div class="btn-card-processos q-gutter-md flex">
      <div class="flex-container">
        <div class="right-buttons">
          <q-btn
            v-show="userRole === 'ADMIN'"
            v-on:click="this.abrirModalNovoProcesso()"
            unelevated
            size="md"
            label="Novo Processo"
            no-caps
            color="teal"
            :disable="idQuadroAtual === null"
          />
          <q-btn
            v-show="userRole === 'ADMIN'"
            v-on:click="this.confirmaDeletarQuadro(idQuadroAtual)"
            unelevated
            size="md"
            label="Deletar Quadro"
            no-caps
            color="black"
            :disable="idQuadroAtual === null"
          />
        </div>
        <div class="right-buttons config-btn">
          <q-btn
            v-show="userRole === 'ADMIN'"
            v-on:click="this.abrirModalEditarQuadro()"
            unelevated
            size="md"
            :disable="idQuadroAtual === null"
          >
            <q-icon name="settings" />
          </q-btn>
        </div>
      </div>
    </div>
    <q-tab-panels
      class="paineis"
      v-model="tabProxy"
      animated
      v-show="idQuadroAtual !== null"
    >
      <q-tab-panel name="CRIADO">
        <lista-processos
          v-show="idQuadroAtual !== null"
          :user-role="userRole"
          :status-atual="tabProxy"
          :id-quadro-atual="idQuadroAtual"
          :processos="processosCriados"
          :lista-quadros="listaQuadros"
          @salvar-processo="processoSalvo()"
          @deletar-processo="onProcessoDelete()"
          @abrir-modal-novo-processo="abrirModalNovoProcesso()"
        />
      </q-tab-panel>
      <q-tab-panel name="EM_PROGRESSO">
        <lista-processos
          v-show="idQuadroAtual !== null"
          :user-role="userRole"
          :status-atual="tabProxy"
          :id-quadro-atual="idQuadroAtual"
          :processos="processosEmProgresso"
          :lista-quadros="listaQuadros"
          @salvar-processo="processoSalvo()"
          @deletar-processo="onProcessoDelete()"
          @abrir-modal-novo-processo="abrirModalNovoProcesso()"
        />
      </q-tab-panel>
      <q-tab-panel name="FINALIZADO">
        <lista-processos
          v-show="idQuadroAtual !== null"
          :user-role="userRole"
          :status-atual="tabProxy"
          :id-quadro-atual="idQuadroAtual"
          :processos="processosFinalizados"
          :lista-quadros="listaQuadros"
          @salvar-processo="processoSalvo()"
          @deletar-processo="onProcessoDelete()"
          @abrir-modal-novo-processo="abrirModalNovoProcesso()"
        />
      </q-tab-panel>
      <q-tab-panel name="ARQUIVADO">
        <lista-processos
          v-show="idQuadroAtual !== null"
          :user-role="userRole"
          :status-atual="tabProxy"
          :id-quadro-atual="idQuadroAtual"
          :processos="processosArquivados"
          :lista-quadros="listaQuadros"
          @salvar-processo="processoSalvo()"
          @deletar-processo="onProcessoDelete()"
          @abrir-modal-novo-processo="abrirModalNovoProcesso()"
        />
      </q-tab-panel>
    </q-tab-panels>
  </div>

  <modal-novo-processo
    :id-quadro-atual="idQuadroAtual"
    v-model="modalNovoProcessoOpen"
    @processo-criado="processoSalvo()"
  />

  <modal-editar-quadro
    :lista-usuarios="listaUsuarios"
    :nome-quadro="nomeQuadroAtual"
    :usuario-responsavel-quadro="usuarioResponsavelQuadro"
    :id-quadro-atual="idQuadroAtual"
    v-model="modalEditarQuadroOpen"
    @salvar-quadro="salvarQuadro"
  />
</template>

<script>
import { ref, defineComponent } from "vue";
import ListaProcessos from "@/components/ListaProcessos.vue";
import ModalNovoProcesso from "@/components/ModalNovoProcesso.vue";
import ModalEditarQuadro from "@/components/ModalEditarQuadro.vue";
import QuadroService from "@/services/QuadroService";

export default defineComponent({
  name: "ListagemProcessos",
  inheritAttrs: false,

  props: {
    tab: String,
    userRole: String,
    idQuadroAtual: Number,
    nomeQuadroAtual: String,
    listaQuadros: Array,
    listaUsuarios: Array,
    usuarioResponsavelQuadro: Object,
    processosCriados: Array,
    processosEmProgresso: Array,
    processosFinalizados: Array,
    processosArquivados: Array,
  },

  components: {
    ListaProcessos,
    ModalNovoProcesso,
    ModalEditarQuadro,
  },

  data() {
    return {
      tabProxy: "CRIADO",
      modalNovoProcessoOpen: ref(false),
      modalEditarQuadroOpen: ref(false),
    };
  },

  methods: {
    confirmaDeletarQuadro(quadroId) {
      const title = "Deletar quadro";
      const message =
        "Tem certeza que deseja excluir este quadro? TODOS os processos associados à ele serão excluídos.";

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
          this.deletarQuadro(quadroId);
        });
    },
    salvarQuadro() {
      this.$emit("salvar-quadro");
      this.modalEditarQuadroOpen = false;
    },
    abrirModalEditarQuadro() {
      this.modalEditarQuadroOpen = true;
    },
    deletarQuadro(quadroId) {
      QuadroService.deletarQuadro(quadroId).then(() => {
        this.$emit("deletar-quadro");
      });
    },
    abrirModalNovoProcesso() {
      this.modalNovoProcessoOpen = true;
    },
    processoSalvo() {
      this.$emit("salvar-processo");
      this.modalNovoProcessoOpen = false;
    },
    onProcessoDelete() {
      this.$emit("deletar-processo");
    },
  },

  watch: {
    tab(newValue) {
      this.tabProxy = newValue;
    },
  },
});
</script>
