<template>
  <q-page class="main-page flex">
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
                    @click="showUsuarios()"
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
                    icon="person_remove"
                    @click="baixarProcesso(props.row)"
                  >
                    <q-tooltip>Desativar usuário</q-tooltip>
                  </q-btn>
                  <q-btn
                    size="md"
                    round
                    icon="person_off"
                    @click="confirmaDeletar(props.row.id)"
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
            <div class="col-6 text-right">
              <q-select
                stack-label
                v-model="usuarioSelecionado.status"
                :options="statusList"
                label="Status"
                outlined
                required
                :disable="!editandoUsuario"
                :rules="[(val) => !!val || 'Status é obrigatório']"
              />
            </div>
          </q-card-section>

          <q-card-section>
            <div class="q-gutter-md">
              <q-input
                stack-label
                :disable="!editandoUsuario"
                v-model="usuarioSelecionado.nome"
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
                :disable="!editandoUsuario"
                v-model="usuarioSelecionado.nome"
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
                :disable="!editandoUsuario"
                v-model="usuarioSelecionado.nome"
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
                :disable="!editandoUsuario"
                v-model="usuarioSelecionado.email"
                label="Réu"
                outlined
                required
                :rules="[
                  (val) => !!val || 'Réu é obrigatório',
                  (val) => val.length <= 100 || 'Máximo de 100 caracteres',
                ]"
              />

              <q-input
                stack-label
                v-model="usuarioSelecionado.nome"
                label="Descrição"
                outlined
                required
                :disable="!editandoUsuario"
                type="textarea"
                :hint="editandoUsuario ? 'Max 1000 caracteres' : ''"
                :rules="[
                  (val) => !!val || 'Descrição é obrigatória',
                  (val) => val.length <= 1000 || 'Máximo de 1000 caracteres',
                ]"
              />

              <q-card-actions>
                <div class="flex-container">
                  <div class="left-buttons">
                    <q-btn
                      unelevated
                      @click="confirmaDeletar(usuarioSelecionado, 'processo')"
                      label="Deletar Processo"
                      color="black"
                      no-caps
                    />
                  </div>
                  <div class="right-buttons">
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
                      v-show="editandoUsuario"
                      flat
                      @click="editandoUsuario = false"
                      label="Cancelar"
                      color="black"
                      no-caps
                    />
                    <q-btn
                      v-show="!editandoUsuario"
                      unelevated
                      @click="editandoUsuario = true"
                      label="Editar Processo"
                      color="teal"
                      no-caps
                    />
                    <q-btn
                      v-show="editandoUsuario"
                      unelevated
                      label="Salvar Processo"
                      color="teal"
                      no-caps
                    />
                  </div>
                </div>
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
import { formatDate } from "@/utils/formatters";

export default defineComponent({
  name: "ListagemUsuarios",

  props: {
    userRole: String,
    usuarios: Array,
  },

  data() {
    return {
      loading: false,
      modalDetalhesUsuarioOpen: false,
      editandoUsuario: false,
      usuarioSelecionado: {},
      pagination: {
        rowsPerPage: 10,
      },
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
          format: (val) => `${val}`,
          sortable: true,
        },
        {
          name: "ativo",
          required: true,
          label: "Ativo",
          align: "left",
          field: (row) => row.ativo,
          format: (val) => `${val}`,
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
    confirmaDeletar(objeto) {
      const title = "Deletar usuário";
      const message =
        "Tem certeza que deseja excluir este usuário? TODOS os quadros e processos associados a ele serão PERMANENTEMENTE EXCLUÍDOS!";

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
          this.deletarUsuario(objeto);
        });
    },
    deletarUsuario(usuario) {
      UsuarioService.excluirUsuario(usuario.id).then(() => {
        this.$emit("deletar-usuario");
      });
    },
    abrirmodalDetalhesUsuario(processo) {
      this.usuarioSelecionado = {
        ...processo,
        status: formatStatus(this.statusList, processo.status),
        prazoSubsidio: formatDate(processo.prazoSubsidio),
        prazoFatal: formatDate(processo.prazoFatal),
      };
      this.modalDetalhesUsuarioOpen = true;
    },
    showUsuarios() {
      console.log(this.usuarios);
    },
  },
});
</script>
