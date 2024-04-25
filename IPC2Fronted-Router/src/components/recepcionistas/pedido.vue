<template>
  <div>
    <h1>Table of Order</h1>

    <div class="table_Container">
      <div class="new">
        <nuevoPedido />
      </div>
      <div class="new2">
        <nuevoPaquete />
      </div>
      <v-table height="400px" fixed-header>
        <thead>
          <tr>
            <th class="text-left">Order No.</th>
            <th class="text-left">Warehouse</th>
            <th class="text-left">Status</th>
            <th class="text-left">Destination Controller</th>
            <th class="text-left">Taken Route</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="pedido in pedidos" :key="pedido.noPedido">
            <td>{{ pedido.noPedido }}</td>
            <td>{{ pedido.bodegaActual }}</td>
            <td>
              <template v-if="pedido.estado === 'C'">
                <v-icon color="blue">mdi-store</v-icon>
              </template>
              <template v-else-if="pedido.estado === 'R'">
                <v-icon color="red">mdi-router</v-icon>
              </template>
              <template v-else-if="pedido.estado === 'E'">
                <v-icon color="green">mdi-package</v-icon>
              </template>
              <template v-else>
                <v-icon color="yellow">mdi-account-question</v-icon>
              </template>
            </td>
            <td>{{ pedido.destinoController }}</td>
            <td>
              <template v-if="pedido.rutaTomada === 0">
                <v-icon color="grey">mdi-adjust</v-icon>
              </template>
              <template v-else>
                {{ pedido.rutaTomada }}
              </template>
            </td>
          </tr>
        </tbody>
      </v-table>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import nuevoPedido from "./nuevoPedido.vue";
import nuevoPaquete from "./nuevoPaquete.vue";
import nuevoPaqueteVue from "./nuevoPaquete.vue";
export default {
  data() {
    return {
      pedidos: [],
    };
  },
  components: {
    nuevoPedido,
    nuevoPaquete,
  },
  mounted() {
    this.obtenerPedidos();
    // Actualizar la tabla de pedidos cada 5 segundos
    setInterval(this.obtenerPedidos, 5000);
  },
  methods: {
    obtenerPedidos() {
      axios
        .get("http://localhost:8080/backendIPC2/api/pedidos")
        .then((response) => {
          this.pedidos = response.data;
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
