<template>
  <q-layout view="hHh lpR fFf">
    <header-component
      :processos-criados="getProcessosSize(processosCriados)"
      :processos-em-progresso="getProcessosSize(processosEmProgresso)"
      :processos-finalizados="getProcessosSize(processosFinalizados)"
      :processos-arquivados="getProcessosSize(processosArquivados)"
      :id-quadro-atual="idQuadroAtual"
      :user-role="userRole"
      @toggle-left-drawer="toggleLeftDrawer()"
      @update:tab="tab = $event"
      @abrir-modal-novo-usuario="abrirModalNovoUsuario()"
      @abrir-modal-novo-quadro="abrirModalNovoQuadro()"
    />

    <q-drawer v-model="leftDrawerOpen" show-if-above bordered>
      <q-list>
        <q-item class="list-label" clickable @click="redirectHome()">
          <q-item-section avatar> Home </q-item-section>
        </q-item>
        <q-expansion-item class="list-label" label="Quadros">
          <lista-quadros
            v-for="quadro in quadros"
            v-bind:quadro="quadro"
            :key="quadro.id"
            :id-quadro-atual="idQuadroAtual"
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
          <q-item
            clickable
            @click="redirectListagemUsuarios()"
            class="list-item"
          >
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
      <q-tab-panels v-model="tab" animated v-show="idQuadroAtual !== null">
        <q-tab-panel name="CRIADO">
          <lista-processos
            :user-role="userRole"
            v-show="idQuadroAtual !== null"
            :status-atual="this.tab"
            :quadro-id="this.idQuadroAtual"
            :processos="this.processosCriados"
            @salvar-processo="processoSalvo()"
            @deletar-processo="onProcessoDelete()"
            @deletar-quadro="onQuadroDelete()"
            @abrir-modal-novo-processo="abrirModalNovoProcesso()"
          />
        </q-tab-panel>
        <q-tab-panel name="EM_PROGRESSO">
          <lista-processos
            :user-role="userRole"
            v-show="idQuadroAtual !== null"
            :status-atual="this.tab"
            :quadro-id="this.idQuadroAtual"
            :processos="this.processosEmProgresso"
            @salvar-processo="processoSalvo()"
            @deletar-processo="onProcessoDelete()"
            @deletar-quadro="onQuadroDelete()"
            @abrir-modal-novo-processo="abrirModalNovoProcesso()"
          />
        </q-tab-panel>
        <q-tab-panel name="FINALIZADO">
          <lista-processos
            :user-role="userRole"
            v-show="idQuadroAtual !== null"
            :status-atual="this.tab"
            :quadro-id="this.idQuadroAtual"
            :processos="this.processosFinalizados"
            @salvar-processo="processoSalvo()"
            @deletar-processo="onProcessoDelete()"
            @deletar-quadro="onQuadroDelete()"
            @abrir-modal-novo-processo="abrirModalNovoProcesso()"
          />
        </q-tab-panel>
        <q-tab-panel name="ARQUIVADO">
          <lista-processos
            :user-role="userRole"
            v-show="idQuadroAtual !== null"
            :status-atual="this.tab"
            :quadro-id="this.idQuadroAtual"
            :processos="this.processosArquivados"
            @salvar-processo="processoSalvo()"
            @deletar-processo="onProcessoDelete()"
            @deletar-quadro="onQuadroDelete()"
            @abrir-modal-novo-processo="abrirModalNovoProcesso()"
          />
        </q-tab-panel>
      </q-tab-panels>

      <router-view
        v-show="this.idQuadroAtual === null"
        :user-role="userRole"
        :usuarios="this.usuarios"
        :email-usuario-logado="emailUsuarioLogado"
        @abrir-modal-novo-quadro="abrirModalNovoQuadro()"
        @abrir-modal-novo-usuario="abrirModalNovoUsuario()"
        @deletar-usuario="onUsuarioDelete()"
        @editar-usuario="onUsuarioEdit()"
      />

      <modal-novo-processo
        :id-quadro-atual="idQuadroAtual"
        v-model="modalNovoProcessoOpen"
        @processo-criado="processoSalvo()"
      />
      <modal-novo-quadro
        :lista-usuarios="this.listaUsuarios"
        v-model="modalNovoQuadroOpen"
        @salvar-quadro="salvarQuadro($event)"
      />
      <modal-novo-usuario
        v-model="modalNovoUsuarioOpen"
        @update:modalNovoUsuarioOpen="modalNovoUsuarioOpen = $event"
        @salvar-usuario="salvarUsuario()"
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
    const emailUsuarioLogado = store.state.usuario.email;

    return {
      store,
      userRole,
      emailUsuarioLogado,
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
      idQuadroAtual: null,
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
    redirectHome() {
      this.idQuadroAtual = null;
      this.$router.push({ path: "/" });
    },
    redirectListagemUsuarios() {
      this.$router.push({ path: "/usuarios" });
      this.idQuadroAtual = null;
    },
    setQuadro(quadro) {
      this.limparProcessos();
      this.$router.push({ path: "/" });
      this.idQuadroAtual = quadro.id;
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
      if (this.idQuadroAtual != null) {
        let actualQuadro = this.quadros.find(
          (quadro) => quadro.id === this.idQuadroAtual
        );
        this.setQuadro(actualQuadro);
      }
    },
    async salvarQuadro(quadro) {
      await QuadroService.salvarQuadro(quadro).then(() => {
        this.fetch();
        this.modalNovoQuadroOpen = false;
      });
      this.returnFeedbackMessage("Quadro criado com sucesso!");
    },
    async salvarUsuario() {
      await this.fetch();
      this.modalNovoUsuarioOpen = false;
      this.returnFeedbackMessage("Usuário criado com sucesso!");
    },
    async processoSalvo() {
      this.modalNovoProcessoOpen = false;
      await this.fetch();
      this.returnFeedbackMessage("Processo salvo com sucesso!");
    },
    onProcessoDelete() {
      this.returnFeedbackMessage("Processo deletado com sucesso");
      this.fetch();
    },
    onQuadroDelete() {
      this.returnFeedbackMessage("Quadro deletado com sucesso!");
      this.idQuadroAtual = null;
      this.fetch();
    },
    onUsuarioDelete() {
      this.returnFeedbackMessage("Usuário deletado com sucesso!");
      this.fetch();
    },
    onUsuarioEdit() {
      this.returnFeedbackMessage("Usuário editado com sucesso!");
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
