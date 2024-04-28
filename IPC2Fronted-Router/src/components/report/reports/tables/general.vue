<template>
  <div>
    <h1>General Reports</h1>

    <div class="table_Container">
       <div class="busqueda">
      <input type="text" v-model="searchTerm" placeholder="Search Order.">
      </div>
      <v-table height="300px" fixed-header>
        <thead>
          <tr>
            <th class="text-left">Voice</th>
            <th class="text-left">Router</th>
            <th class="text-left">Order</th>
            <th class="text-left">Total</th>
            <th class="text-left">Name</th>
            <th class="text-left">NIT</th>
            <th class="text-left">Number Phone</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="reporte in filteredTrayecto" :key="reporte.noFactura">
            <td>{{ reporte.noFactura }}</td>
            <td>{{ reporte.ruta }}</td>
            <td>{{ reporte.noPedido }}</td>
            <td>{{ reporte.total }}</td>
            <td>{{ reporte.nombre }}</td>
            <td>{{ reporte.NIT }}</td>
            <td>{{ reporte.telefono }}</td>
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
      reporte: [],
    };
  },
  components: {

  },
  computed: {

    filteredTrayecto() {
      if (!this.searchTerm) return this.reporte;
      return this.reporte.filter((reporte) => {
        return reporte.noFactura.toString() === this.searchTerm;
      });
      
    },
  },
  mounted() {
    this.obtenerReporte();
    // Actualizar la tabla de pedidos cada 5 segundos
    setInterval(this.obtenerReporte, 5000);
  },
  methods: {
    obtenerReporte() {
      axios
        .get("http://localhost:8080/backendIPC2/api/reportes?op=list")
        .then((response) => {
          this.reporte = response.data;
        })
        .catch((error) => {
          console.error("Error al obtener datos de pedidos", error);
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
.busqueda{
  top: 85px;
  position: absolute;
  left: 80px;
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
