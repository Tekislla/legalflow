<template>
  <q-item
    clickable
    @click="this.$emit('get-tasks-by-project-id', project.id)"
    :focused="project.id === this.actualProjectId"
  >
    <q-item-section>
      <q-item-label>{{ project.projectName }}</q-item-label>
      <q-item-label caption>Customer: {{ project.customerName }}</q-item-label>
      <q-item-label class="text-teal" caption>
        {{ taskListSize }} task(s) open
      </q-item-label>
    </q-item-section>
  </q-item>
</template>

<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "EssentialLink",

  props: {
    project: Object,
    actualProjectId: Number,
    taskListSize: Number,
  },
});
</script>
