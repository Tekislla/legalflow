<template>
  <q-page class="flex flex-center">
    <div class="flex column flex-center main-text">
      <div style="font-size: 8vh">Welcome!</div>
      <div class="text-h4">
        Select a project from the menu or create a new project.
      </div>
      <div class="flex-row">
        <q-btn
          class="index-btn"
          color="teal"
          v-on:click="this.$emit('open-new-customer-modal')"
        >
          <div>
            <div>New Customer</div>
            <br />
            <q-icon name="person" size="xl" />
          </div>
        </q-btn>
        <q-btn
          class="index-btn"
          color="teal"
          v-on:click="this.$emit('open-new-project-modal')"
        >
          <div>
            <div>New Project</div>
            <q-icon name="topic" size="xl" />
          </div>
        </q-btn>
      </div>
    </div>
  </q-page>
</template>

<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "IndexPage",
});
</script>
