<template>
  <div>
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
          :quadro-id="idQuadroAtual"
          :processos="processosCriados"
          @salvar-processo="processoSalvo()"
          @deletar-processo="onProcessoDelete()"
          @deletar-quadro="onQuadroDelete()"
          @abrir-modal-novo-processo="abrirModalNovoProcesso()"
        />
      </q-tab-panel>
      <q-tab-panel name="EM_PROGRESSO">
        <lista-processos
          v-show="idQuadroAtual !== null"
          :user-role="userRole"
          :status-atual="tabProxy"
          :quadro-id="idQuadroAtual"
          :processos="processosEmProgresso"
          @salvar-processo="processoSalvo()"
          @deletar-processo="onProcessoDelete()"
          @deletar-quadro="onQuadroDelete()"
          @abrir-modal-novo-processo="abrirModalNovoProcesso()"
        />
      </q-tab-panel>
      <q-tab-panel name="FINALIZADO">
        <lista-processos
          v-show="idQuadroAtual !== null"
          :user-role="userRole"
          :status-atual="tabProxy"
          :quadro-id="idQuadroAtual"
          :processos="processosFinalizados"
          @salvar-processo="processoSalvo()"
          @deletar-processo="onProcessoDelete()"
          @deletar-quadro="onQuadroDelete()"
          @abrir-modal-novo-processo="abrirModalNovoProcesso()"
        />
      </q-tab-panel>
      <q-tab-panel name="ARQUIVADO">
        <lista-processos
          v-show="idQuadroAtual !== null"
          :user-role="userRole"
          :status-atual="tabProxy"
          :quadro-id="idQuadroAtual"
          :processos="processosArquivados"
          @salvar-processo="processoSalvo()"
          @deletar-processo="onProcessoDelete()"
          @deletar-quadro="onQuadroDelete()"
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
</template>

<script>
import { ref, defineComponent } from "vue";
import ListaProcessos from "@/components/ListaProcessos.vue";
import ModalNovoProcesso from "@/components/ModalNovoProcesso.vue";

export default defineComponent({
  name: "ListagemProcessos",
  inheritAttrs: false,

  props: {
    tab: String,
    userRole: String,
    idQuadroAtual: Number,
    processosCriados: Array,
    processosEmProgresso: Array,
    processosFinalizados: Array,
    processosArquivados: Array,
  },

  components: {
    ListaProcessos,
    ModalNovoProcesso,
  },

  data() {
    return {
      tabProxy: "CRIADO",
      modalNovoProcessoOpen: ref(false),
    };
  },

  methods: {
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
    onQuadroDelete() {
      this.$emit("deletar-quadro");
    },
  },

  watch: {
    tab(newValue) {
      this.tabProxy = newValue;
    },
  },
});
</script>
