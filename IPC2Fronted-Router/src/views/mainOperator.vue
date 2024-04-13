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
import profiles from "../components/dates/profilesOperador.vue"
import tableRece from "../components/operadores/recepcionistasTable.vue"
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
      }
    },
  },

  components: {
    tableRece,
  },
};
</script>

<style>
/* Estilos CSS si los necesitas */
</style>
