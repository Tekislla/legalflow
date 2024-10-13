<template>
  <q-page class="flex">
    <div class="container text-center q-pa-md flex flex-center">
      <q-card class="container-card q-pa-lg">
        <q-card-section class="btn-card q-gutter-md">
          <q-btn
            v-on:click="this.$emit('abrir-modal-novo-usuario')"
            unelevated
            size="md"
            label="Novo Usuário"
            no-caps
            color="teal"
          />
        </q-card-section>

        <q-card-section>
          <q-table
            :rows="usuarios"
            :columns="columns"
            :loading="usuarios.length === 0"
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
                    @click="abrirmodalDetalhesUsuario(props.row)"
                  >
                  </q-btn>
                </q-btn-group>
              </q-td>
            </template>
            <template v-slot:body-cell-actions="props">
              <q-td :props="props" auto-width>
                <q-btn-group unelevated>
                  <q-btn
                    v-show="
                      props.row.ativo && props.row.email !== emailUsuarioLogado
                    "
                    size="md"
                    round
                    icon="person_remove"
                    @click="confirmaAcao(props.row, 'desativar')"
                  >
                    <q-tooltip>Desativar usuário</q-tooltip>
                  </q-btn>
                  <q-btn
                    v-show="
                      !props.row.ativo && props.row.email !== emailUsuarioLogado
                    "
                    size="md"
                    round
                    icon="person_add"
                    @click="confirmaAcao(props.row, 'ativar')"
                  >
                    <q-tooltip>Ativar usuário</q-tooltip>
                  </q-btn>
                  <q-btn
                    v-show="props.row.email !== emailUsuarioLogado"
                    size="md"
                    round
                    icon="person_off"
                    @click="confirmaAcao(props.row, 'deletar')"
                  >
                    <q-tooltip>Excluir usuário</q-tooltip>
                  </q-btn>
                </q-btn-group>
              </q-td>
            </template>
          </q-table>
        </q-card-section>
      </q-card>

      <q-dialog persistent v-model="modalDetalhesUsuarioOpen">
        <q-card class="q-pa-md main-card">
          <q-card-section class="row items-center q-pb-none">
            <div class="col-6">
              <div class="text-h6">Detalhes</div>
            </div>
          </q-card-section>

          <q-card-section>
            <div class="q-gutter-md">
              <q-input
                stack-label
                disable
                v-model="usuarioSelecionado.nome"
                label="Nome"
                outlined
                required
              />
              <q-input
                stack-label
                disable
                v-model="usuarioSelecionado.email"
                label="E-mail"
                outlined
                required
              />
              <q-input
                stack-label
                v-model="usuarioSelecionado.senha"
                label="Senha"
                outlined
                required
                :type="showPwd ? 'text' : 'password'"
                :rules="[
                  (val) => val.length <= 20 || 'Máximo de 20 caracteres',
                  (val) => val.length >= 5 || 'Mínimo de 5 caracteres',
                ]"
              >
                <template v-slot:append>
                  <q-icon
                    :name="showPwd ? 'visibility' : 'visibility'"
                    class="cursor-pointer"
                    @click="showPwd = !showPwd"
                  />
                </template>
              </q-input>
              <q-input
                stack-label
                v-model="usuarioSelecionado.confirmaSenha"
                label="Confirme a senha"
                outlined
                required
                :type="showPwd ? 'text' : 'password'"
                :rules="[
                  (val) =>
                    val === usuarioSelecionado.senha ||
                    'As senhas não conferem',
                  (val) => val.length <= 20 || 'Máximo de 20 caracteres',
                  (val) => val.length >= 5 || 'Mínimo de 5 caracteres',
                ]"
              >
                <template v-slot:append>
                  <q-icon
                    :name="showPwd ? 'visibility' : 'visibility'"
                    class="cursor-pointer"
                    @click="showPwd = !showPwd"
                  />
                </template>
              </q-input>

              <q-select
                v-model="usuarioSelecionado.role"
                :options="roles"
                label="Role"
                outlined
                required
                :disable="usuarioSelecionado.email === emailUsuarioLogado"
              />

              <q-card-actions align="right">
                <q-btn
                  v-show="!editandoUsuario"
                  flat
                  @click="editandoUsuario = false"
                  label="Fechar"
                  color="black"
                  no-caps
                  v-close-popup
                />
                <q-btn
                  v-show="!editandoUsuario"
                  unelevated
                  @click="editarUsuario(usuarioSelecionado)"
                  label="Salvar"
                  color="teal"
                  no-caps
                  :disable="
                    (usuarioSelecionado.senha != '' &&
                      usuarioSelecionado.senha.length < 5) ||
                    (usuarioSelecionado.senha != '' &&
                      usuarioSelecionado.senha.length > 20) ||
                    (usuarioSelecionado.confirmaSenha != '' &&
                      usuarioSelecionado.confirmaSenha.length < 5) ||
                    (usuarioSelecionado.confirmaSenha != '' &&
                      usuarioSelecionado.confirmaSenha.length > 20) ||
                    usuarioSelecionado.senha !==
                      usuarioSelecionado.confirmaSenha
                  "
                />
              </q-card-actions>
            </div>
          </q-card-section>
        </q-card>
      </q-dialog>
    </div>
  </q-page>
</template>

<script>
import { defineComponent } from "vue";
import UsuarioService from "@/services/UsuarioService";
import {
  formatDate,
  formatUserRole,
  formatUserStatus,
} from "@/utils/formatters";

export default defineComponent({
  name: "ListagemUsuarios",

  props: {
    userRole: String,
    usuarios: Array,
    emailUsuarioLogado: String,
  },

  data() {
    return {
      loading: false,
      showPwd: false,
      modalDetalhesUsuarioOpen: false,
      editandoUsuario: false,
      usuarioSelecionado: {},
      pagination: {
        rowsPerPage: 10,
      },
      roles: ["Administrador", "Usuário"],
      columns: [
        {
          name: "details",
          label: "Detalhes",
          align: "left",
        },
        {
          name: "nome",
          required: true,
          label: "Nome",
          align: "left",
          field: (row) => row.nome,
          format: (val) => `${val}`,
          sortable: true,
        },
        {
          name: "email",
          required: true,
          label: "E-mail",
          align: "left",
          field: (row) => row.email,
          format: (val) => `${val}`,
          sortable: true,
        },
        {
          name: "role",
          required: true,
          label: "Role",
          align: "left",
          field: (row) => row.role,
          format: (val) => `${formatUserRole(val)}`,
          sortable: true,
        },
        {
          name: "ativo",
          required: true,
          label: "Ativo",
          align: "left",
          field: (row) => row.ativo,
          format: (val) => `${formatUserStatus(val)}`,
          sortable: true,
        },
        {
          name: "dataCadastro",
          required: true,
          label: "Data de Cadastro",
          align: "left",
          field: (row) => row.dataCadastro,
          format: (val) => `${formatDate(val)}`,
          sortable: true,
        },
        {
          name: "dataAtualizacao",
          required: true,
          label: "Data de atualização",
          align: "left",
          field: (row) => row.dataAtualizacao,
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
    confirmaAcao(usuario, acao) {
      let title = "";
      let message = "";

      switch (acao) {
        case "desativar":
          title = "Desativar usuário";
          message = "Tem certeza que deseja desativar este usuário?";
          break;
        case "ativar":
          title = "Ativar usuário";
          message = "Tem certeza que deseja ativar este usuário?";
          break;
        default:
          title = "Deletar usuário";
          message =
            "Tem certeza que deseja excluir este usuário? TODOS os quadros e processos associados a ele serão PERMANENTEMENTE EXCLUÍDOS!";
          break;
      }

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
          switch (acao) {
            case "desativar":
              this.ativarDesativarUsuario(usuario, false);
              break;
            case "ativar":
              this.ativarDesativarUsuario(usuario, true);
              break;
            default:
              this.deletarUsuario(usuario);
              break;
          }
        });
    },
    deletarUsuario(usuario) {
      UsuarioService.excluirUsuario(usuario.id).then(() => {
        this.$emit("deletar-usuario");
      });
    },
    ativarDesativarUsuario(usuario, condicao) {
      UsuarioService.editarUsuario({
        id: usuario.id,
        ativo: condicao,
        administrador: usuario.role === "ADMIN",
      }).then(() => {
        this.$emit("editar-usuario");
      });
    },
    editarUsuario(usuarioSelecionado) {
      const usuario = {
        id: usuarioSelecionado.id,
        senha: usuarioSelecionado.senha || null,
        ativo: usuarioSelecionado.ativo,
        administrador: usuarioSelecionado.role === "Administrador",
      };
      UsuarioService.editarUsuario(usuario).then(() => {
        this.$emit("editar-usuario");
        this.modalDetalhesUsuarioOpen = false;
      });
    },
    abrirmodalDetalhesUsuario(usuario) {
      this.usuarioSelecionado = {
        ...usuario,
        senha: "",
        confirmaSenha: "",
        role: formatUserRole(usuario.role),
      };
      this.modalDetalhesUsuarioOpen = true;
    },
  },
});
</script>
