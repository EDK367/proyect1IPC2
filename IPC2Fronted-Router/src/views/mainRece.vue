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
            prepend-icon="mdi-camera-control"
            title="Points"
            value="points"
            @click="changeComponent('points')"
          ></v-list-item>
          <v-list-item
            prepend-icon="mdi-package"
            title="Paquetes"
            value="paquetes"
            @click = "changeComponent('paquetes')"
          ></v-list-item>
          <v-list-item
          prepend-icon="mdi-clipboard-account"
          title="Cliente"
          value="cliente"
          >
          </v-list-item>
        </v-list>
      </v-navigation-drawer>
      <v-main style="height: 250px"></v-main>
    </v-layout>
  </v-card>
</template>

<script>
import profiles from "../components/dates/profiles.vue"
import points from "../components/recepcionistas/points.vue"
import paquetes from "../components/recepcionistas/paquetes.vue"
export default {
  data() {
    return {
      use: paquetes,
      drawer: true,
      rail: true,
      
    };
  },

  methods: {
    // Método para cambiar el componente mostrado
    changeComponent(componentName) {
      if(componentName === "profiles"){
        this.use = profiles;
      }else if(componentName === "points"){
        this.use = points;
      }else if(componentName === "paquetes"){
        this.use = paquetes;
      }
    },
  },

  components: {
    profiles,
    points,
    paquetes,
  },
};
</script>

<style>
/* Estilos CSS si los necesitas */
</style>
