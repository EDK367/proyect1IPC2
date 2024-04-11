<template>
  <h1>Table of Users Admins</h1>
  <div class="table_Container">
    <v-table height="400px" fixed-header>
      <thead>
        <nuevoUser />

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
      administrador: [],
    };
  },

  components: {
    nuevoUser,
  },

  mounted() {
    this.obtenerAdmin();
  },
  methods: {
    obtenerAdmin() {
      axios
        .get("http://localhost:8080/backendIPC2/api/admin")
        .then((response) => {
          this.administrador = response.data;
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
