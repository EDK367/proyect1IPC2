<template>
  <div>
    <h1>Table of Invoice</h1>

    <div class="table_Container">
      <div class="busqueda">
        <input type="text" v-model="searchTerm" placeholder="Search Order." />
      </div>

      <v-table height="400px" fixed-header>
        <thead>
          <tr>
            <th class="text-left">Invoice</th>
            <th class="text-left">Operator</th>
            <th class="text-left">Receptionist</th>
            <th class="text-left">Order</th>
            <th class="text-left">Warehouse</th>
            <th class="text-left">Router</th>
            <th class="text-left">Total</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="fac in filteredTrayecto" :key="fac.noFactura">
            <td>{{ fac.noFactura }}</td>
            <td>{{ fac.cuiOperador }}</td>
            <td>{{ fac.cuiRecepcionista }}</td>
            <td>{{ fac.noPedido }}</td>
            <td>{{ fac.noBodega }}</td>
            <td>{{ fac.ruta }}</td>
            <td>{{ fac.total }}</td>
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
      facturas: [],
    };
  },
  components: {},
  mounted() {
    this.obtenerFacturas();
    // Actualizar la tabla constantemente
    setInterval(this.obtenerFacturas, 5000); // 5000 == 5 segundos
  },
  computed: {
    filteredTrayecto() {
      if (!this.searchTerm) return this.facturas;
      return this.facturas.filter((fac) => {
        return fac.noFactura.toString() === this.searchTerm;
      });
    },
  },
  methods: {
    obtenerFacturas() {
      axios
        .get("http://localhost:8080/backendIPC2/api/facturas")
        .then((response) => {
          this.facturas = response.data;
        })
        .catch((error) => {
          console.error("Error al obtener datos", error);
        });
    },
  },
};
</script>

<style scoped>
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
.busqueda {
  top: 110px;
  position: absolute;
  left: 70px;
}

.search-input {
  padding: 10px;
  border-radius: 20px;
  border: 1px solid #ccc;
  font-size: 16px;
  transition: all 0.3s ease;
  width: 250px;
}

.search-input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
}
</style>
