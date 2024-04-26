<template>
  <v-card>
    <component :is="use" :key="use"></component>
    <v-layout>
      <v-navigation-drawer expand-on-hover rail>
        <v-divider></v-divider>
        <v-list-item
          prepend-icon="mdi-account-outline"
          title="Profile"
          value="profiles"
          @click="changeComponent('profiles')"
        ></v-list-item>
        <v-divider></v-divider>

        <v-list density="compact" nav>
          <v-list-item
            prepend-icon="mdi-account-voice"
            title="Recepcion"
            value="tablesRe"
            @click="changeComponent('tableRece')"
          ></v-list-item>

          <v-list-item
            prepend-icon="mdi-camera-control"
            title="Points"
            value="points"
            @click="changeComponent('point')"
          ></v-list-item>
          <v-list-item
            prepend-icon=" mdi-boom-gate"
            title="Router"
            value="ruta"
            @click="changeComponent('router')"
          ></v-list-item>

          <v-list-item
            prepend-icon=" mdi-boom-gate-alert"
            title="Router Info"
            value="info"
            @click="changeComponent('routerInfo')"
          ></v-list-item>
          <v-list-item
            prepend-icon=" mdi-cart-arrow-down"
            title="Order"
            value="pedido"
          ></v-list-item>

          <v-list-item
            prepend-icon="mdi-package"
            title="Packages"
            value="paquete"
          ></v-list-item>

          <v-list-item
            prepend-icon="mdi-ballot-outline"
            title="Reports"
            value="reports"
          ></v-list-item>
        </v-list>
      </v-navigation-drawer>
      <v-main style="height: 250px"></v-main>
    </v-layout>
  </v-card>
</template>

<script>
import profiles from "../components/dates/profiles.vue";
import tableRece from "../components/operadores/recepcionistasTable.vue";
import point from "../components/operadores/pointsAndRouter/points.vue";
import router from "../components/operadores/pointsAndRouter/router.vue";
import routerInfo from "../components/operadores/pointsAndRouter/routerInfo.vue"
export default {
  data() {
    return {
      use: tableRece,
      drawer: true,
      rail: true,
    };
  },

  methods: {
    // Método para cambiar el componente mostrado
    changeComponent(componentName) {
      if (componentName === "tableRece") {
        this.use = tableRece;
      } else if (componentName === "profiles") {
        this.use = profiles;
      } else if (componentName === "point") {
        this.use = point;
      } else if (componentName === "router") {
        this.use = router;
      }else if(componentName === "routerInfo"){
        this.use = routerInfo;
      }
    },
  },

  components: {
    tableRece,
    profiles,
    point,
    router,
    routerInfo
  },
};
</script>

<style>
/* Estilos CSS si los necesitas */
</style>
