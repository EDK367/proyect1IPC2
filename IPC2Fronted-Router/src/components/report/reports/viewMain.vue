<template>
  <v-card>
    <v-tabs v-model="tab" align-tabs="center" bg-color="blue">
      <v-tab value="one">General</v-tab>
      <v-tab value="two">Popular Route</v-tab>
      <v-tab value="three">Earnings</v-tab>
      <v-tab value="four">Best Customers</v-tab>
      <v-tab value="five">Routes</v-tab>
    </v-tabs>

    <v-card-text>
      <v-window v-model="tab">
        <v-window-item value="one">
          <general />
        </v-window-item>
        <v-window-item value="two">
          <popular />
        </v-window-item>
        <v-window-item value="three">
          <pagado />
        </v-window-item>
        <v-window-item value="four">
          <cliente />
        </v-window-item>
        <v-window-item value="five">
          <ruta />
        </v-window-item>
      </v-window>
    </v-card-text>
  </v-card>
</template>

<script>
import general from "./tables/general.vue";
import popular from "./tables/popular.vue";
import pagado from "./tables/pagado.vue";
import cliente from "./tables/cliente.vue";
import ruta from "./tables/rutas.vue";
export default {
  data: () => ({
    tab: null,
  }),

  components: {
    general,
    popular,
    pagado,
    cliente,
    ruta,
  },
};
</script>

<style></style>
