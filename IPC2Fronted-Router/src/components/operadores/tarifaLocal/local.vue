<template>
  <h1>Table of Local Rate</h1>
    
  <div class="table_Container">
    <div class="new">
      <local/>
    </div>
    <v-table
     height="400px" 
     fixed-header>
      <thead> 
        <tr>
          <th class="text-left">ID of Local Rate</th>
          <th class="text-left">Quota Local</th>
          <th class="text-left">ID point</th>
          <th class="text-left">Charge Operator</th>
          <th class="text-left">Option</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="tari in tarifaLocal" :key="tari.IdTarifa">
          <td>{{ tari.IdTarifa }}</td>
          <td>{{ tari.tarifaLocal }}</td>
          <td>{{ tari.IDControl }}</td>
          <td>{{tari.cuiOperador}}</td>
          <td>
            <v-icon
        size="small"
        @click="deleteItem(tari)"
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
import local from "./newLocal.vue"

export default {
  
  data() {
    return {
      deleteOption: null,
      dialogDelete: false,
      tarifaLocal: [],
    };
  },

  components: {
    local,
  },
computed: {
},
  mounted() {
    this.obtenerTarifaLocal();
    //actualizar la tabla constantemente
    setInterval(this.obtenerTarifaLocal, 5000);//2000 == 2 segundos
  },


  methods: {
    obtenerTarifaLocal() {
      axios //nombre de como lo declaraste
        .get("http://localhost:8080/backendIPC2/api/tarifaLocal")//el metodo que vas hacer
        
        .then((response) => {

          this.tarifaLocal = response.data;
          
           this.tarifaLocal = response.data.sort((a, b) => b.tarifaLocal - a.tarifaLocal);

           console.log("Esta es la tarifa local", this.tarifaLocal)
        })
        .catch((error) => {
          console.error("error al obtener datos");
        });


    },

     //metodo para editar y eliminar se obtiene el form 
     //este metodo no se usa por logica pero esta ahi por si alguin dia se quiere usar
    option(tari){
      localStorage.setItem('optionUser', JSON.stringify(tari));
       console.log('Contenido de localStorage:', localStorage.getItem('optionUser'));
    },

    deleteItem(tari){
      console.log("Delete", tari)
      this.deleteOption = tari;
      this.dialogDelete = true;
    },
    closeDelete(){
      this.dialogDelete = false;
    },
    deleteItemConfirm(tari){
      this.dialogDelete = false;
       const tariJson = JSON.stringify(this.deleteOption);
      console.log("Este es el JSON del administrador:", tariJson);

      axios
        .delete("http://localhost:8080/backendIPC2/api/tarifaLocal", {
          data: tariJson,
        })
        .then((response) => {
          console.log("Operador eliminado con éxito");
          this.obtenerTarifaGlobal();
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
