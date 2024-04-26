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
          prepend-icon=" mdi-cart-arrow-down"
          title="Pedidos"
          value="pedidos"
          @click="changeComponent('pedidos')"
          >
          </v-list-item>
          
          <v-list-item
            prepend-icon="mdi-package"
            title="Paquetes"
            value="paquetes"
            @click = "changeComponent('paquetes')"
          ></v-list-item>
          <v-list-item
            prepend-icon="mdi-account"
            title="Cliente"
            value="cliente"
            @click = "changeComponent('cliente')"
          ></v-list-item>
          

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
import pedidos from "../components/recepcionistas/pedido.vue"
import cliente from "../components/recepcionistas/cliente.vue"
export default {
  data() {
    return {
      use: pedidos,
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
      }else if(componentName === "pedidos"){
        this.use = pedidos;
      }else if(componentName === "cliente"){
        this.use = cliente;
      }
    },
  },

  components: {
    profiles,
    points,
    paquetes,
    pedidos,
    cliente,
  },
};
</script>

<style>
/* Estilos CSS si los necesitas */
</style>
