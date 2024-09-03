<template>
  <q-item
    class="list-item"
    clickable
    @click="this.$emit('set-quadro-id', quadro.id)"
    :focused="quadro.id === this.actualQuadroId"
  >
    <q-item-section>
      <q-item-label>{{ quadro.nome }}</q-item-label>
      <q-item-label caption>Responsável: {{ quadro.responsavel }}</q-item-label>
      <q-item-label class="text-teal" caption>
        {{ quadro.processos.length }} processos em aberto
      </q-item-label>
    </q-item-section>
  </q-item>
</template>

<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "QuadrosList",

  props: {
    quadro: Object,
    actualQuadroId: Number,
  },

  emits: ["set-quadro-id"],
});
</script>
