<template>
  <h1>Table of Points</h1>
    
  <div class="table_Container">

    <v-table
     height="400px" 
     fixed-header>
      
    
      <thead> 
        <tr>
          <th class="text-left">ID the Point</th>
          <th class="text-left">Name</th>
          <th class="text-left">Operator in Charge</th>
          <th class="text-left">Options</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="point in point" :key="point.IdControl">
          <td>{{ point.IdControl }}</td>
          <td>{{ point.nombre }}</td>
          <td>{{ point.cuiOperador }}</td>
          <td>
            <v-btn
            @click="option(point)"
             prepend-icon="$vuetify" 
             variant="text"
             > SELECT </v-btn>
              <v-icon
        size="small"
        @click="deleteItem(point)"
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


export default {
  
  data() {
    return {
      deleteOption: null,
      dialogDelete: false,
      point: [],
    };
  },

  components: {

  },

  mounted() {
    this.obtenerPoint();
    //actualizar la tabla constantemente
    setInterval(this.obtenerPoint, 5000);//2000 == 20 segundos
  },


  methods: {
    obtenerPoint() {
      axios //nombre de como lo declaraste
        .get("http://localhost:8080/backendIPC2/api/point")//el metodo que vas hacer
        
        .then((response) => {
          this.point = response.data;
          
        })
        .catch((error) => {
          console.error("error al obtener datos");
        });


    },
     //metodo para editar y eliminar se obtiene el form 
    option(point){
      localStorage.setItem('optionUser', JSON.stringify(point));
       console.log('Contenido de localStorage:', localStorage.getItem('optionUser'));
    },

    deleteItem(point){
      console.log("Delete", point)
      this.deleteOption = point;
      this.dialogDelete = true;
    },
    closeDelete(){
      this.dialogDelete = false;
    },
    deleteItemConfirm(operador){
      this.dialogDelete = false;
       const pointJson = JSON.stringify(this.deleteOption);
      console.log("Este es el JSON del administrador:", pointJson);

      axios
        .delete("http://localhost:8080/backendIPC2/api/point", {
          data: pointJson,
        })
        .then((response) => {
          console.log("Operador eliminado con éxito");
          this.obtenerPoint();
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
