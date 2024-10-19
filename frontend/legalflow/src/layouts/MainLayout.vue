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
        <q-item
          class="list-label"
          clickable
          @click="redirectHome()"
          :focused="isHomeRoute"
        >
          <q-item-section avatar> Home </q-item-section>
        </q-item>
        <q-item
          class="list-label"
          clickable
          @click="redirectDashboard()"
          :focused="isDashboardRoute"
        >
          <q-item-section avatar> Dashboard </q-item-section>
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
          <q-item v-show="this.loading === true">
            <q-spinner color="teal" size="3em" :thickness="2" />
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
            :focused="isUsuariosRoute"
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
      <router-view
        :id-quadro-atual="this.idQuadroAtual"
        :nome-quadro-atual="this.nomeQuadroAtual"
        :lista-quadros="listaQuadros"
        :usuario-responsavel-quadro="this.usuarioResponsavelQuadro"
        :user-role="userRole"
        :user-name="userName"
        :organizacao-id="organizacaoId"
        :lista-usuarios="this.listaUsuarios"
        :usuarios="this.usuarios"
        :tab="this.tab"
        :email-usuario-logado="emailUsuarioLogado"
        :processos-criados="this.processosCriados"
        :processos-em-progresso="this.processosEmProgresso"
        :processos-finalizados="this.processosFinalizados"
        :processos-arquivados="this.processosArquivados"
        @abrir-modal-novo-quadro="abrirModalNovoQuadro()"
        @abrir-modal-novo-usuario="abrirModalNovoUsuario()"
        @deletar-usuario="onUsuarioDelete()"
        @editar-usuario="onUsuarioEdit()"
        @salvar-processo="processoSalvo()"
        @deletar-processo="onProcessoDelete()"
        @deletar-quadro="onQuadroDelete()"
        @salvar-quadro="salvarQuadro()"
      />

      <modal-novo-quadro
        :lista-usuarios="this.listaUsuarios"
        v-model="modalNovoQuadroOpen"
        @salvar-quadro="salvarQuadro()"
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
import ModalNovoUsuario from "@/components/ModalNovoUsuario.vue";
import ModalNovoQuadro from "@/components/ModalNovoQuadro.vue";
import ListaQuadros from "@/components/ListaQuadros.vue";
import UsuarioService from "@/services/UsuarioService";
import NotificationUtil from "@/utils/NotificationUtil";

export default defineComponent({
  name: "MainLayout",

  setup() {
    const store = useStore();
    const userRole = store.state.usuario.role;
    const emailUsuarioLogado = store.state.usuario.email;
    const userName = store.state.usuario.nome;
    const organizacaoId = store.state.organizacaoId;

    return {
      store,
      userRole,
      emailUsuarioLogado,
      userName,
      organizacaoId,
    };
  },

  components: {
    HeaderComponent,
    ModalNovoUsuario,
    ModalNovoQuadro,
    ListaQuadros,
  },

  inheritAttrs: false,

  data() {
    return {
      tab: "CRIADO",
      idQuadroAtual: null,
      nomeQuadroAtual: "",
      usuarioResponsavelQuadro: null,
      processos: [],
      usuarios: [],
      listaUsuarios: [],
      quadros: [],
      listaQuadros: [],
      processosCriados: [],
      processosEmProgresso: [],
      processosFinalizados: [],
      processosArquivados: [],
      loading: ref(false),
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
    fetchQuadro() {
      if (this.idQuadroAtual != null) {
        let actualQuadro = this.quadros.find(
          (quadro) => quadro.id === this.idQuadroAtual
        );
        this.setQuadro(actualQuadro);
      }
      this.loading = false;
    },
    setQuadro(quadro) {
      this.limparProcessos();

      this.processos = quadro.processos;
      this.idQuadroAtual = quadro.id;
      this.nomeQuadroAtual = quadro.nome;
      this.usuarioResponsavelQuadro = {
        label: quadro.responsavel,
        value: quadro.idResponsavel,
      };

      quadro.processos.forEach((processo) => {
        processo.quadroAtual = {
          label: quadro.nome,
          value: quadro.id,
        };

        processo.status === "CRIADO"
          ? this.processosCriados.push(processo)
          : processo.status === "EM_PROGRESSO"
          ? this.processosEmProgresso.push(processo)
          : processo.status === "FINALIZADO"
          ? this.processosFinalizados.push(processo)
          : this.processosArquivados.push(processo);
      });
      this.$router.push({ path: "/processos" });
    },
    async getUsuarioInfo() {
      this.loading = true;
      this.listaUsuarios = [];
      this.quadros = [];
      this.listaQuadros = [];

      this.usuarios = (await UsuarioService.getUsuarioInfo()).data;

      this.usuarios.forEach((usuario) => {
        usuario.quadros.forEach((quadro) => {
          quadro.responsavel = usuario.nome;
          quadro.idResponsavel = usuario.id;
          this.quadros.push(quadro);
          this.listaQuadros.push({
            label: quadro.nome,
            value: quadro.id,
          });
        });

        this.listaUsuarios.push({
          label: usuario.nome,
          value: usuario.id,
        });
      });

      this.fetchQuadro();
    },
    async salvarQuadro() {
      await this.fetch();
      this.modalNovoQuadroOpen = false;
      this.returnFeedbackMessage("Quadro salvo com sucesso!");
    },
    async salvarUsuario() {
      await this.fetch();
      this.modalNovoUsuarioOpen = false;
      this.returnFeedbackMessage("Usuário salvo com sucesso!");
    },
    async processoSalvo() {
      await this.fetch();
      this.modalNovoProcessoOpen = false;
      this.returnFeedbackMessage("Processo salvo com sucesso!");
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
    onProcessoDelete() {
      this.returnFeedbackMessage("Processo deletado com sucesso");
      this.fetch();
    },
    onQuadroDelete() {
      this.returnFeedbackMessage("Quadro deletado com sucesso!");
      this.redirectDashboard();
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
    redirectHome() {
      this.idQuadroAtual = null;
      this.$router.push({ path: "/home" });
    },
    redirectDashboard() {
      this.idQuadroAtual = null;
      this.$router.push({ path: "/dashboard" });
    },
    redirectListagemUsuarios() {
      this.idQuadroAtual = null;
      this.$router.push({ path: "/usuarios" });
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
      this.$store.dispatch("logout");
      this.$router.push({ path: "/auth/login" });
    },
  },

  computed: {
    isHomeRoute() {
      return this.$route.path === "/home";
    },
    isUsuariosRoute() {
      return this.$route.path === "/usuarios";
    },
    isProcessosRoute() {
      return this.$route.path === "/processos";
    },
    isDashboardRoute() {
      return this.$route.path === "/dashboard";
    },
  },

  mounted() {
    if (this.$route.path !== "/") {
      this.$router.push("/");
    }
    this.fetch();
  },
});
</script>
