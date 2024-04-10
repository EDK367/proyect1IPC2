<template>
<div class="a">
</div>
  <v-card>
     <component :is="use" :key="use"></component>
    <v-layout>
      <v-navigation-drawer
        v-model="drawer"
        :rail="rail"
        permanent
        @click="rail = false"
      >
        <v-list-item
          prepend-icon="mdi-view-dashboard"
          nav
        >
          <template v-slot:append>
            <v-btn
              icon="mdi-chevron-left"
              variant="text"
              @click.stop="rail = !rail"
            ></v-btn>
          </template>
        </v-list-item>

        <v-divider></v-divider>

        <v-list density="compact" nav>
          <v-list-item prepend-icon="mdi-account-group-outline" title="Users" value="users" @click="use"></v-list-item>
          <v-list-item prepend-icon="mdi-camera-control" title="Points" value="points" ></v-list-item>
          <v-list-item prepend-icon="mdi-ballot-outline" title="Reports" value="reports"></v-list-item>
        </v-list>
      </v-navigation-drawer>
      <v-main style="height: 100px"></v-main>
    </v-layout>
  </v-card>
    
    

</template>

<script>
import tables from '../components/administrador/adminUser.vue'
import nuevoUser from '../components/administrador/new.vue'
  export default {
    data () {
      return {
        use: tables,
        drawer: true,
        rail: true,
       
      }
    },

    components:{
        tables,
    }
  }
</script>

<style>




</style>
