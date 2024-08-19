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
        v-show="actualProjectId != null"
        v-on:click="this.$emit('open-new-customer-modal')"
        unelevated
        size="md"
        label="New Customer"
        no-caps
        class="header-btn"
        color="teal"
      />
      <q-btn
        v-show="actualProjectId != null"
        v-on:click="this.$emit('open-new-project-modal')"
        unelevated
        size="md"
        label="New Project"
        no-caps
        class="header-btn"
        color="teal"
      />
    </q-toolbar>

    <q-tabs align="left" v-model="tabProxy">
      <q-tab name="CREATED" label="Created" v-show="actualProjectId != null">
        <q-badge color="teal">
          {{ createdTasks }}
        </q-badge>
      </q-tab>
      <q-tab
        name="IN_PROGRESS"
        label="In Progress"
        v-show="actualProjectId != null"
      >
        <q-badge color="teal">
          {{ inProgressTasks }}
        </q-badge>
      </q-tab>
      <q-tab name="DONE" label="Done" v-show="actualProjectId != null">
        <q-badge color="teal">
          {{ doneTasks }}
        </q-badge>
      </q-tab>
      <q-tab name="CANCELED" label="Canceled" v-show="actualProjectId != null">
        <q-badge color="teal">
          {{ canceledTasks }}
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
    createdTasks: Number,
    inProgressTasks: Number,
    doneTasks: Number,
    canceledTasks: Number,
    actualProjectId: Number,
  },

  data() {
    return {
      tabProxy: "CREATED",
    };
  },

  watch: {
    tabProxy() {
      this.$emit("update:tab", this.tabProxy);
    },
  },
});
</script>
