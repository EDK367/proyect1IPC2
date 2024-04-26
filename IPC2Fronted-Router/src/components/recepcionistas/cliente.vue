<template>
  <div>
    <h1>Table of Customer</h1>

    <div class="table_Container">
       <div class="new">
        <nuevoCliente />
      </div>
      <v-table height="400px" fixed-header>
        <thead>
          <tr>
            <th class="text-left">Name</th>
            <th class="text-left">Last Name</th>
            <th class="text-left">NIT</th>
            <th class="text-left">Operator</th>
            <th class="text-left">Receptionist</th>
            <th class="text-left">Number Phone</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="cliente in clientes" :key="cliente.NIT">
            <td>{{ cliente.nombre }}</td>
            <td>{{ cliente.apellido }}</td>
            <td>{{ cliente.NIT }}</td>
            <td>{{ cliente.cuiOperador }}</td>
            <td>{{ cliente.cuiRecepcionista }}</td>
            <td>{{ cliente.telefono }}</td>
          </tr>
        </tbody>
      </v-table>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import nuevoCliente from "./nuevoCliente.vue"
export default {
  data() {
    return {
      clientes: [],
    };
  },
  components: {
    nuevoCliente,
  },
  mounted() {
    this.obtenerClientes();
    // Actualizar la tabla de pedidos cada 5 segundos
    setInterval(this.obtenerClientes, 5000);
  },
  methods: {
    obtenerClientes() {
      axios
        .get("http://localhost:8080/backendIPC2/api/cliente")
        .then((response) => {
          this.clientes = response.data;
        })
        .catch((error) => {
          console.error("Error al obtener datos de pedidos", error);
        });
    },
  },
};
</script>

<style scoped>
.new {
  top: 90px;
  position: absolute;
  left: 10;
}
.new2 {
  top: 90px;
  position: absolute;
  left: 200px;
}
.table_Container {
  margin: 100px 10px;
  max-width: 1500px;
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
