<template>
  <div></div>
  <!--
  <q-item
    clickable
    @click="this.$emit('set-quadro-id', quadro.id)"
    :focused="quadro.id === this.actualQuadroId"
  >
    <q-item-section>
      <q-item-label>{{ quadro.nome }}</q-item-label>
      <q-item-label caption
        >Responsável: {{ quadro.usuario.nome }}</q-item-label
      >
      <q-item-label class="text-teal" caption>
        {{ taskListSize }} task(s) open
      </q-item-label>
    </q-item-section>
  </q-item>
  -->
</template>

<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "EssentialLink",

  props: {
    quadro: Object,
    actualQuadroId: Number,
    taskListSize: Number,
  },
});
</script>
