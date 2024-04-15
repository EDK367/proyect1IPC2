<template>
  <h1>Table of Users Admins</h1>
    
  <div class="table_Container">
    <div class = "new">
<nuevoUser />
</div>
    <v-table
     height="400px" 
     fixed-header>
      <div class="deletes">
      <deletes />
    </div>
    
      <thead> 
        <tr>
          <th class="text-left">Identification</th>
          <th class="text-left">Name</th>
          <th class="text-left">Last Name</th>
          <th class="text-left">Gmail</th>
          <th class="text-left">Password</th>
          <th class="text-left">Options</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="admin in administrador" :key="admin.cuiAdmin">
          <td>{{ admin.cuiAdmin }}</td>
          <td>{{ admin.nombre }}</td>
          <td>{{ admin.apellido }}</td>
          <td>{{ admin.correo }}</td>
          <td>{{ admin.contraseña }}</td>
          <td>
            <v-btn
            @click="option(admin)"
             prepend-icon="$vuetify" 
             variant="text"
             > SELECT </v-btn>
              <v-icon
        size="small"
        @click="deleteItem(admin)"
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
import nuevoUser from "./new.vue";
import deletes from "../option/deleteAndPut.vue"

export default {
  
  data() {
    return {
      deleteOption: null,
      pruebas: nuevoUser,
      dialogDelete: false,
      administrador: [],
    };
  },

  components: {
    nuevoUser,
    deletes,
  },

  mounted() {
    this.obtenerAdmin();
    //actualizar la tabla constantemente
    setInterval(this.obtenerAdmin, 5000);//2000 == 20 segundos
  },


  methods: {
    obtenerAdmin() {
      axios //nombre de como lo declaraste
        .get("http://localhost:8080/backendIPC2/api/admin")//el metodo que vas hacer
        
        .then((response) => {
          this.administrador = response.data;
          
        })
        .catch((error) => {
          console.error("error al obtener datos");
        });


    },
     //metodo para editar y eliminar se obtiene el form 
    option(admin){
      localStorage.setItem('optionUser', JSON.stringify(admin));
       console.log('Contenido de localStorage:', localStorage.getItem('optionUser'));
    },

    deleteItem(admin){
      console.log("Delete", admin)
      this.deleteOption = admin;
      this.dialogDelete = true;
    },
    closeDelete(){
      this.dialogDelete = false;
    },
    deleteItemConfirm(admin){
      this.dialogDelete = false;
       const adminJson = JSON.stringify(this.deleteOption);
      console.log("Este es el JSON del administrador:", adminJson);

      axios
        .delete("http://localhost:8080/backendIPC2/api/admin", {
          data: adminJson,
        })
        .then((response) => {
          console.log("Administrador eliminado con éxito");
          this.obtenerAdmin();
        })
        .catch((error) => {
          console.error("Error al eliminar administrador", error);
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
