<template>
  <q-header elevated class="bg-black text-white flex" height-hint="98">
    <q-toolbar>
      <q-btn
        dense
        flat
        round
        icon="menu"
        @click="this.$emit('toggle-left-drawer')"
      />

      <q-toolbar-title> LegalFlow </q-toolbar-title>

      <q-btn
        v-show="idQuadroAtual != null && userRole === 'ADMIN'"
        v-on:click="this.$emit('abrir-modal-novo-usuario')"
        unelevated
        size="md"
        label="Novo Usuário"
        no-caps
        class="header-btn"
        color="teal"
      />
      <q-btn
        v-show="idQuadroAtual != null && userRole === 'ADMIN'"
        v-on:click="this.$emit('abrir-modal-novo-quadro')"
        unelevated
        size="md"
        label="Novo Quadro"
        no-caps
        class="header-btn"
        color="teal"
      />
    </q-toolbar>

    <q-tabs align="left" v-model="tabProxy">
      <q-tab name="CRIADO" label="Criado" v-show="idQuadroAtual != null">
        <q-badge color="teal">
          {{ processosCriados }}
        </q-badge>
      </q-tab>
      <q-tab
        name="EM_PROGRESSO"
        label="Em Progresso"
        v-show="idQuadroAtual != null"
      >
        <q-badge color="teal">
          {{ processosEmProgresso }}
        </q-badge>
      </q-tab>
      <q-tab
        name="FINALIZADO"
        label="Finalizado"
        v-show="idQuadroAtual != null"
      >
        <q-badge color="teal">
          {{ processosFinalizados }}
        </q-badge>
      </q-tab>
      <q-tab name="ARQUIVADO" label="Arquivado" v-show="idQuadroAtual != null">
        <q-badge color="teal">
          {{ processosArquivados }}
        </q-badge>
      </q-tab>
    </q-tabs>
  </q-header>
</template>
<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "HeaderComponent",

  props: {
    processosCriados: {
      type: Number,
      default: 0,
    },
    processosEmProgresso: {
      type: Number,
      default: 0,
    },
    processosFinalizados: {
      type: Number,
      default: 0,
    },
    processosArquivados: {
      type: Number,
      default: 0,
    },
    idQuadroAtual: Number,
    userRole: String,
  },

  data() {
    return {
      tabProxy: "CRIADO",
    };
  },

  watch: {
    tabProxy() {
      this.$emit("update:tab", this.tabProxy);
    },
  },
});
</script>
