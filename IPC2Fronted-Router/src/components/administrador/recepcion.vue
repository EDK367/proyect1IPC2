<template>
  <h1>Table of Users Receptionist</h1>
  <div class="table_Container">
    <v-table height="400px" fixed-header>
      <thead>
        <tr>
          <th class="text-left">Identification</th>
          <th class="text-left">Name</th>
          <th class="text-left">Last Name</th>
          <th class="text-left">Gmail</th>
          <th class="text-left">Password</th>
          <th class="text-left">Operator</th>
          <th class="text-left">Options</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="recepcion in receptionist" :key="recepcion.cuiRecepcionista">
          <td>{{ recepcion.cuiRecepcionista }}</td>
          <td>{{ recepcion.nombre }}</td>
          <td>{{ recepcion.apellido }}</td>
          <td>{{ recepcion.correo }}</td>
          <td>{{ recepcion.contraseña }}</td>
          <td>{{ recepcion.cuiOperador }}</td>

          <td></td>
        </tr>
      </tbody>
    </v-table>
  </div>
</template>

<script>
import axios from "axios";
import nuevoUser from "./new.vue";
export default {
  data() {
    return {
      pruebas: nuevoUser,
      receptionist: [],
    };
  },

  components: {
    nuevoUser,
  },

  mounted() {
    this.obtenerReceptionist();
  },
  methods: {
    obtenerReceptionist() {
      axios
        .get("http://localhost:8080/backendIPC2/api/recepcion")
        .then((response) => {
            
          this.receptionist = response.data;
        })
        .catch((error) => {
          console.error("error al obtener datos");
        });
    },
  },
};
</script>

<style scoped>
.table_Container {
  margin: 30px 10px;
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
