<template>
  <h1>Table of Routes</h1>
  <div class="table_Container">
     <div class = "new">
<nuevoRouter/>
</div>
    <v-table
     height="400px" 
     fixed-header>
      <div class="deletes">
      <putDelete/>
    </div>
      <thead> 
        <tr>
          <th class="text-left">ID Router</th>
          <th class="text-left">Packages</th>
          <th class="text-left">Starting Points</th>
          <th class="text-left">Full Stop</th>
          <th class="text-left">Options</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="router in rutas" :key="router.idRuta">
          <td>{{ router.idRuta }}</td>
          <td>{{ router.cantidadPuntos }}</td>
          <td>{{ router.start }}</td>
          <td>{{ router.end }}</td>
          <td>
            <v-btn
            @click="option(router)"
             prepend-icon="$vuetify" 
             variant="text"
             > SELECT </v-btn>
              <v-icon
        size="small"
        @click="deleteItem(router)"
      >
        mdi-delete
      </v-icon>
          </td>
        </tr>
      </tbody>
    </v-table>
      <v-dialog v-model="dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="text-h5">Are you sure you want to delete this USER?</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue-darken-1" variant="text" @click="closeDelete">Cancel</v-btn>
              <v-btn color="blue-darken-1" variant="text" @click="deleteItemConfirm">OK</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
  </div>
</template>

<script>
import axios from "axios";
import nuevoRouter from "./newRouter.vue";
import putDelete from "./deleteAndPuteRouter";
export default {
  
  data() {
    return {
      deleteOption: null,
      dialogDelete: false,
      rutas: [],
    };
  },

  components: {
    nuevoRouter,
    putDelete,
  },

  mounted() {
    this.obtenerRutas();
    //actualizar la tabla constantemente
    setInterval(this.obtenerRutas, 5000);//2000 == 20 segundos
  },


  methods: {
    obtenerRutas() {
      axios //nombre de como lo declaraste
        .get("http://localhost:8080/backendIPC2/api/router")//el metodo que vas hacer
        
        .then((response) => {
          this.rutas = response.data;
          
        })
        .catch((error) => {
          console.error("error al obtener datos");
        });


    },
     //metodo para editar y eliminar se obtiene el form 
    option(router){
      localStorage.setItem('optionPoint', JSON.stringify(router));
       console.log('Contenido de localStorage:', localStorage.getItem('optionPoint'));
    },

    deleteItem(router){
      console.log("Delete", router)
      this.deleteOption = router;
      this.dialogDelete = true;
    },
    closeDelete(){
      this.dialogDelete = false;
    },
    deleteItemConfirm(router){
      this.dialogDelete = false;
       const routerJson = JSON.stringify(this.deleteOption);
      console.log("Este es el JSON del administrador:", routerJson);

      axios
        .delete("http://localhost:8080/backendIPC2/api/router", {
          data: routerJson,
        })
        .then((response) => {
          console.log("Administrador eliminado con éxito");
          this.obtenerRutas();
        })
        .catch((error) => {
          console.error("Error al eliminar router", error);
        });
  }
  },
};
</script>

<style scoped>
.new{
  top: 90px;
  position: absolute;
  left: 10;
}
.deletes{
  top: 90px;
  position: absolute;
  right: 0;
  
}
.table_Container {
  margin: 100px 10px;
  max-width: -800px;
  margin-left: 50px;
  margin-right: 0px;
}

table {
  width: auto;
  border-collapse: collapse;
  box-shadow: 0 2px 15px rgba(red, green, blue, alpha);
  background-color: white;
}

th,
td {
  border: 1px solid #ddd;
  padding: 8px 15px;
  text-align: center;
}

th {
  background-color: #f2f2f2;
  color: rgb(0, 0, 0);
  font-weight: normal;
  text-transform: uppercase;
}

.btn {
  margin: 0px 5px;
}
</style>
