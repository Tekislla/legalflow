<template>
  <q-item
    class="list-item"
    clickable
    @click="this.$emit('set-quadro', quadro.id)"
    :focused="quadro.id === this.idQuadroAtual"
  >
    <q-item-section>
      <q-item-label>{{ quadro.nome }}</q-item-label>
      <q-item-label caption>Responsável: {{ quadro.responsavel }}</q-item-label>
      <q-item-label class="text-teal" caption>
        {{ processosEmAberto }} processos em aberto
      </q-item-label>
    </q-item-section>
  </q-item>
</template>

<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "ListaQuadros",

  props: {
    quadro: Object,
    idQuadroAtual: Number,
    processosEmAberto: Number,
  },
});
</script>
