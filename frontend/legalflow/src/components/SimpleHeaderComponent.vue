<template>
  <q-header elevated class="bg-black text-white" height-hint="98">
    <q-toolbar>
      <q-toolbar-title>
        <q-img src="@/assets/logo.png" class="header-logo" />
      </q-toolbar-title>
    </q-toolbar>
  </q-header>
</template>
<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "SimpleHeaderComponent",
});
</script>
