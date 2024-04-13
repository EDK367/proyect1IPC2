<template>
  <v-card>
    <component :is="use" :key="use"></component>
    <v-layout>
      <v-navigation-drawer expand-on-hover rail>
        <v-divider></v-divider>
        <v-list-item
          prepend-icon="mdi-account-outline"
          title="Perfil"
          value="profiles"
          @click="changeComponent('profiles')"
        ></v-list-item>
        <v-divider></v-divider>

        <v-list density="compact" nav>
          <v-list-item
            prepend-icon="mdi-account-tie"
            title="Admin"
            value="tables"
            @click="changeComponent('tables')"
          ></v-list-item>
          <v-list-item
            prepend-icon="mdi-account-supervisor"
            title="Operator"
            value="tablesOp"
            @click="changeComponent('tablesOp')"
          ></v-list-item>
<v-list-item
            prepend-icon="mdi-account-voice"
            title="Recepcion"
            value="tablesRe"
            @click="changeComponent('tablesRe')"
          ></v-list-item>


          <v-list-item
            prepend-icon="mdi-camera-control"
            title="Points"
            value="points"
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
import profiles from "../components/dates/profiles.vue"
import tables from "../components/administrador/adminUser.vue";
import tablesOp from "../components/administrador/operator.vue";
import tablesRe from "../components/administrador/recepcion.vue"

export default {
  data() {
    return {
      use: tables,
      drawer: true,
      rail: true,
      
    };
  },

  methods: {
    // Método para cambiar el componente mostrado
    changeComponent(componentName) {
      if (componentName === "tables") {
        this.use = tables;
      } else if (componentName === "tablesOp") {
        this.use = tablesOp;
      } else if(componentName === "tablesRe"){
        this.use = tablesRe;
      }else if(componentName === "profiles"){
        this.use = profiles;
      }
    },
  },

  components: {
    tables,
    tablesOp,
    tablesRe,
    profiles,
  },
};
</script>

<style>
/* Estilos CSS si los necesitas */
</style>
