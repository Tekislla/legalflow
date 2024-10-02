<template>
  <q-layout view="hHh lpR fFf">
    <header-component
      :processos-criados="getProcessosSize(processosCriados)"
      :processos-em-progresso="getProcessosSize(processosEmProgresso)"
      :processos-finalizados="getProcessosSize(processosFinalizados)"
      :processos-arquivados="getProcessosSize(processosArquivados)"
      :actual-quadro-id="actualQuadroId"
      :user-role="userRole"
      @toggle-left-drawer="toggleLeftDrawer()"
      @update:tab="tab = $event"
      @abrir-modal-novo-usuario="abrirModalNovoUsuario()"
      @abrir-modal-novo-quadro="abrirModalNovoQuadro()"
    />

    <q-drawer v-model="leftDrawerOpen" show-if-above bordered>
      <q-list>
        <q-item class="list-label" clickable @click="actualQuadroId = null">
          <q-item-section avatar> Home </q-item-section>
        </q-item>
        <q-expansion-item class="list-label" label="Quadros">
          <lista-quadros
            v-for="quadro in quadros"
            v-bind:quadro="quadro"
            :key="quadro.id"
            :actual-quadro-id="actualQuadroId"
            :processos-em-aberto="getProcessosAbertos(quadro.processos)"
            @set-quadro="setQuadro(quadro)"
          />
          <q-item
            clickable
            @click="abrirModalNovoQuadro()"
            v-show="this.quadros.length === 0 && this.userRole === 'ADMIN'"
          >
            <q-item-section>
              <q-item-label>Não há nenhum quadro criado</q-item-label>
              <q-item-label caption>Clique aqui para criar um</q-item-label>
            </q-item-section>
          </q-item>
          <q-item
            v-show="this.quadros.length === 0 && this.userRole === 'USER'"
          >
            <q-item-section>
              <q-item-label>Não há nenhum quadro criado</q-item-label>
              <q-item-label caption
                >Solicite ao administrador para que ele crie um</q-item-label
              >
            </q-item-section>
          </q-item>
        </q-expansion-item>
        <q-expansion-item
          label="Configurações"
          v-show="this.userRole === 'ADMIN'"
          class="list-label"
        >
          <q-item clickable @click="abrirModalNovoUsuario()" class="list-item">
            <q-item-section>
              <q-item-label>Gestão de usuários</q-item-label>
              <q-item-label caption>Criar ou excluir usuários</q-item-label>
            </q-item-section>
          </q-item>
        </q-expansion-item>
        <q-item class="list-label" clickable @click="logout()">
          <q-item-section>
            <q-item-label>Sair</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

    <q-page-container>
      <q-tab-panels v-model="tab" animated v-show="actualQuadroId !== null">
        <q-tab-panel name="CRIADO">
          <lista-processos
            v-show="actualQuadroId !== null"
            :actual-status="this.tab"
            :quadro-id="this.actualQuadroId"
            :tasks="this.processosCriados"
            @update-task="submitTaskForm($event, 'updated')"
            @delete-task="onTaskDelete()"
            @deletar-quadro="onQuadroDelete()"
            @abrir-modal-novo-processo="abrirModalNovoProcesso()"
          />
        </q-tab-panel>
        <q-tab-panel name="EM_PROGRESSO">
          <lista-processos
            v-show="actualQuadroId !== null"
            :actual-status="this.tab"
            :quadro-id="this.actualQuadroId"
            :tasks="this.processosEmProgresso"
            @update-task="submitTaskForm($event, 'updated')"
            @delete-task="onTaskDelete()"
            @deletar-quadro="onQuadroDelete()"
            @abrir-modal-novo-processo="abrirModalNovoProcesso()"
          />
        </q-tab-panel>
        <q-tab-panel name="FINALIZADO">
          <lista-processos
            v-show="actualQuadroId !== null"
            :actual-status="this.tab"
            :quadro-id="this.actualQuadroId"
            :tasks="this.processosFinalizados"
            @update-task="submitTaskForm($event, 'updated')"
            @delete-task="onTaskDelete()"
            @deletar-quadro="onQuadroDelete()"
            @abrir-modal-novo-processo="abrirModalNovoProcesso()"
          />
        </q-tab-panel>
        <q-tab-panel name="ARQUIVADO">
          <lista-processos
            v-show="actualQuadroId !== null"
            :actual-status="this.tab"
            :quadro-id="this.actualQuadroId"
            :tasks="this.processosArquivados"
            @update-task="submitTaskForm($event, 'updated')"
            @delete-task="onTaskDelete()"
            @deletar-quadro="onQuadroDelete()"
            @abrir-modal-novo-processo="abrirModalNovoProcesso()"
          />
        </q-tab-panel>
      </q-tab-panels>

      <router-view
        v-show="this.actualQuadroId === null"
        @abrir-modal-novo-quadro="abrirModalNovoQuadro()"
        @abrir-modal-novo-usuario="abrirModalNovoUsuario()"
      />

      <modal-novo-processo
        :actual-quadro-id="actualQuadroId"
        v-model="modalNovoProcessoOpen"
        @processo-criado="processoCriado()"
      />
      <modal-novo-usuario
        v-model="modalNovoUsuarioOpen"
        @update:modalNovoUsuarioOpen="modalNovoUsuarioOpen = $event"
        @hide-modal="modalNovoUsuarioOpen = false"
        @submit-form-novo-usuario="submitFormNovoUsuario()"
      />
      <modal-novo-quadro
        :lista-usuarios="this.listaUsuarios"
        v-model="modalNovoQuadroOpen"
        @submit-form-novo-quadro="submitFormNovoQuadro($event)"
      />
    </q-page-container>
  </q-layout>
</template>

<script>
import { defineComponent, ref } from "vue";
import { useStore } from "vuex";
import HeaderComponent from "@/components/HeaderComponent.vue";
import ModalNovoProcesso from "@/components/ModalNovoProcesso.vue";
import ModalNovoUsuario from "@/components/ModalNovoUsuario.vue";
import ModalNovoQuadro from "@/components/ModalNovoQuadro.vue";
import ListaQuadros from "@/components/ListaQuadros.vue";
import ListaProcessos from "@/pages/ListaProcessos.vue";
import UsuarioService from "@/services/UsuarioService";
import QuadroService from "@/services/QuadroService";
import NotificationUtil from "@/utils/NotificationUtil";

export default defineComponent({
  name: "MainLayout",

  setup() {
    const store = useStore();
    const userRole = store.state.usuario.role;

    return {
      store,
      userRole,
    };
  },

  components: {
    HeaderComponent,
    ModalNovoProcesso,
    ModalNovoUsuario,
    ModalNovoQuadro,
    ListaQuadros,
    ListaProcessos,
  },

  data() {
    return {
      tab: "CRIADO",
      processos: [],
      usuarios: [],
      listaUsuarios: [],
      quadros: [],
      listaQuadros: [],
      processosCriados: [],
      processosEmProgresso: [],
      processosFinalizados: [],
      processosArquivados: [],
      actualQuadroId: null,
      leftDrawerOpen: ref(false),
      modalNovoProcessoOpen: ref(false),
      modalNovoUsuarioOpen: ref(false),
      modalNovoQuadroOpen: ref(false),
    };
  },

  methods: {
    fetch() {
      this.getUsuarioInfo();
    },
    setQuadro(quadro) {
      this.limparProcessos();
      this.actualQuadroId = quadro.id;
      this.processos = quadro.processos;
      quadro.processos.forEach((processo) => {
        processo.status === "CRIADO"
          ? this.processosCriados.push(processo)
          : processo.status === "EM_PROGRESSO"
          ? this.processosEmProgresso.push(processo)
          : processo.status === "FINALIZADO"
          ? this.processosFinalizados.push(processo)
          : this.processosArquivados.push(processo);
      });
    },
    abrirModalNovoProcesso() {
      this.modalNovoProcessoOpen = true;
    },
    abrirModalNovoUsuario() {
      this.modalNovoUsuarioOpen = true;
    },
    abrirModalNovoQuadro() {
      this.modalNovoQuadroOpen = true;
    },
    async getUsuarioInfo() {
      this.listaUsuarios = [];
      this.quadros = [];

      this.usuarios = (await UsuarioService.getUsuarioInfo()).data;
      console.log(this.usuarios);

      this.usuarios.forEach((usuario) => {
        usuario.quadros.forEach((quadro) => {
          quadro.responsavel = usuario.nome;
          this.quadros.push(quadro);
        });

        this.listaUsuarios.push({
          label: usuario.nome,
          value: usuario.id,
        });
      });

      this.fetchQuadro();
    },
    fetchQuadro() {
      if (this.actualQuadroId != null) {
        let actualQuadro = this.quadros.find(
          (quadro) => quadro.id === this.actualQuadroId
        );
        this.setQuadro(actualQuadro);
      }
    },
    async submitFormNovoQuadro(quadro) {
      await QuadroService.salvarQuadro(quadro).then(() => {
        this.fetch();
        this.modalNovoQuadroOpen = false;
      });
      this.returnFeedbackMessage("Quadro criado com sucesso!");
    },
    async submitFormNovoUsuario() {
      await this.fetch();
      this.modalNovoUsuarioOpen = false;
      this.returnFeedbackMessage("Usuário criado com sucesso!");
    },
    async processoCriado() {
      await this.fetch();
      this.returnFeedbackMessage("Processo criado com sucesso!");
    },
    onTaskDelete() {
      this.returnFeedbackMessage("Task deleted successfully!");
      this.fetch();
    },
    onQuadroDelete() {
      this.returnFeedbackMessage("Quadro deletado com sucesso!");
      this.actualQuadroId = null;
      this.fetch();
    },
    toggleLeftDrawer() {
      this.leftDrawerOpen = !this.leftDrawerOpen;
    },
    returnFeedbackMessage(notifyMessage) {
      NotificationUtil.returnFeedbackMessage(
        this.$q,
        notifyMessage,
        "positive",
        "teal"
      );
    },
    limparProcessos() {
      this.processos = [];
      this.processosCriados = [];
      this.processosEmProgresso = [];
      this.processosFinalizados = [];
      this.processosArquivados = [];
    },
    getProcessosSize(processos) {
      return processos.length;
    },
    getProcessosAbertos(processos) {
      return processos.filter(
        (processo) =>
          processo.status === "CRIADO" || processo.status === "EM_PROGRESSO"
      ).length;
    },
    logout() {
      console.log("Logout");
      this.$store.dispatch("logout");
      this.$router.push({ path: "/auth/login" });
    },
  },

  mounted() {
    this.fetch();
  },
});
</script>
