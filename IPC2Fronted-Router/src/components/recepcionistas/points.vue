<template>
  <h1>Table of Routes and Points</h1>
  <div class="table_Container">
    <div class="flex-container">
    <v-table
     height="300px" 
     fixed-header>
      <thead> 
        <tr>
          <th class="text-left">ID Router</th>
          <th class="text-left">Start</th>
          <th class="text-left">Full Stop</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="router in rutas" :key="router.idRuta">
          <td>{{ router.idRuta }}</td>
          <td>{{ router.start }}</td>
          <td>{{ router.end }}</td>
        </tr>
      </tbody>
    </v-table>
    <v-table
     height="300px" 
     fixed-header>
      <thead> 
        <tr>
          <th class="text-left">ID Controller</th>
          <th class="text-left">Name Controller</th>
          <th class="text-left">Charge Operator</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="puntos in puntos" :key="puntos.IdControl">
          <td>{{ puntos.IdControl }}</td>
          <td>{{ puntos.nombre }}</td>
          <td>{{ puntos.cuiOperador }}</td>
        </tr>
      </tbody>
    </v-table>
  </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  
  data() {
    return {
      deleteOption: null,
      dialogDelete: false,
      rutas: [],
      puntos:[],
    };
  },

  components: {

  },

  mounted() {
    this.obtenerRutas();
    //actualizar la tabla constantemente
    setInterval(this.obtenerRutas, 5000);//2000 == 20 segundos
  
    this.obtenerPoints();
    setInterval(this.obtenerPoints, 5000)
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

    obtenerPoints(){
      axios
      .get("http://localhost:8080/backendIPC2/api/point")//el metodo que vas hacer
        
        .then((respon) => {
          this.puntos = respon.data;
          
        })
        .catch((error) => {
          console.error("error al obtener datos");
        });
    },
  }
}
</script>

<style scoped>
.new{
  top: 90px;
  position: absolute;
  left: 10;
}

.flex-container {
  display: flex;
  flex-direction: row; /* Cambia la dirección a row para colocar las tablas una al lado de la otra */
  justify-content: center;
  width: 100%; 
}

.deletes{
  top: 90px;
  position: absolute;
  right: 0;
}

.table_Container {
  margin: 50px 20px;
  max-width: -1000px;
  margin-left: 50px;
  margin-right: 0px;
  width: 100%;
}

table {
  width: 45%;
  border-collapse: collapse;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.2);
  background-color: #676666; 
  border-radius: 30px; 
  font-size: 20px;
  overflow: hidden;
}

th,
td {
  border: 1px solid #ddd;
  padding: 12px 20px;
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