<template>
  <h1>Table of Users Operator</h1>
    
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
        <tr v-for="operador in operator" :key="operador.cuiOperador">
          <td>{{ operador.cuiOperador }}</td>
          <td>{{ operador.nombre }}</td>
          <td>{{ operador.apellido }}</td>
          <td>{{ operador.correo }}</td>
          <td>{{ operador.contraseña }}</td>
          <td>
            <v-btn
            @click="option(operador)"
             prepend-icon="$vuetify" 
             variant="text"
             > SELECT </v-btn>
              <v-icon
        size="small"
        @click="deleteItem(operador)"
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
import deletes from "../option/deleteAndPutOperador.vue"

export default {
  
  data() {
    return {
      deleteOption: null,
      pruebas: nuevoUser,
      dialogDelete: false,
      operator: [],
    };
  },

  components: {
    nuevoUser,
    deletes,
  },

  mounted() {
    this.obteneroperator();
    //actualizar la tabla constantemente
    setInterval(this.obteneroperator, 5000);//2000 == 20 segundos
  },


  methods: {
    obteneroperator() {
      axios //nombre de como lo declaraste
        .get("http://localhost:8080/backendIPC2/api/operador")//el metodo que vas hacer
        
        .then((response) => {
          this.operator = response.data;
          
        })
        .catch((error) => {
          console.error("error al obtener datos");
        });


    },
     //metodo para editar y eliminar se obtiene el form 
    option(operador){
      localStorage.setItem('optionUser', JSON.stringify(operador));
       console.log('Contenido de localStorage:', localStorage.getItem('optionUser'));
    },

    deleteItem(operador){
      console.log("Delete", operador)
      this.deleteOption = operador;
      this.dialogDelete = true;
    },
    closeDelete(){
      this.dialogDelete = false;
    },
    deleteItemConfirm(operador){
      this.dialogDelete = false;
       const operadorJson = JSON.stringify(this.deleteOption);
      console.log("Este es el JSON del administrador:", operadorJson);

      axios
        .delete("http://localhost:8080/backendIPC2/api/operador", {
          data: operadorJson,
        })
        .then((response) => {
          console.log("Operador eliminado con éxito");
          this.obtenerAdmin();
        })
        .catch((error) => {
          console.error("Error al eliminar operador", error);
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
