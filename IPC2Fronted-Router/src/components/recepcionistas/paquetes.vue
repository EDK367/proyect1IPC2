<template>
  <div>
    <h1>Table of Packages</h1>

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
            <th class="text-left">Package</th>
            <th class="text-left">Operator</th>
            <th class="text-left">Receptionist</th>
            <th class="text-left">Order No.</th>
            <th class="text-left">Initial Warehouse</th>
            <th class="text-left">NIT</th>
            <th class="text-left">Weight</th>
            <th class="text-left">Global Rate</th>
            <th class="text-left">Total</th>
            <th class="text-left">Options</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="paquete in paquetes" :key="paquete.paquete">
            <td>{{ paquete.paquete }}</td>
            <td>{{ paquete.cuiOperador }}</td>
            <td>{{ paquete.cuiRecepcionista }}</td>
            <td>{{ paquete.noPedido }}</td>
            <td>{{ paquete.bodegaInicial }}</td>
            <td>{{ paquete.cliente }}</td>
            <td>{{ paquete.peso }}</td>
            <td>
              <v-icon :color="paquete.tarifaGlobal ? 'green' : 'red'">
                {{
                  paquete.tarifaGlobal
                    ? "mdi-flag-checkered"
                    : "mdi-flag-remove"
                }}
              </v-icon>
            </td>
            <td>{{ paquete.total }}</td>
            <td>
              <v-icon size="small" @click="deleteItem(paquete)">
                mdi-delete
              </v-icon>
            </td>
          </tr>
        </tbody>
      </v-table>

      <v-dialog v-model="dialogDelete" max-width="500px">
        <v-card>
          <v-card-title class="text-h5"
            >Are you sure you want to delete this ITEM?</v-card-title
          >
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue-darken-1" variant="text" @click="closeDelete"
              >Cancel</v-btn
            >
            <v-btn
              color="blue-darken-1"
              variant="text"
              @click="deleteItemConfirm"
              >OK</v-btn
            >
            <v-spacer></v-spacer>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import nuevoPedido from "./nuevoPedido.vue";
import nuevoPaquete from "./nuevoPaquete.vue";
export default {
  data() {
    return {
      deleteOption: null,
      dialogDelete: false,
      paquetes: [],
    };
  },
  components: {
    nuevoPedido,
    nuevoPaquete,
  },
  mounted() {
    this.obtenerPaquetes();
    // Actualizar la tabla constantemente
    setInterval(this.obtenerPaquetes, 5000); // 5000 == 5 segundos
  },
  methods: {
    obtenerPaquetes() {
      axios
        .get("http://localhost:8080/backendIPC2/api/paquetes")
        .then((response) => {
          this.paquetes = response.data;
        })
        .catch((error) => {
          console.error("Error al obtener datos", error);
        });
    },
    option(paquete) {
      localStorage.setItem("optionPaquete", JSON.stringify(paquete));
      console.log(
        "Contenido de localStorage:",
        localStorage.getItem("optionPaquete")
      );
    },
    deleteItem(paquete) {
      console.log("Delete", paquete);
      this.deleteOption = paquete;
      this.dialogDelete = true;
    },
    closeDelete() {
      this.dialogDelete = false;
    },
    deleteItemConfirm(paquete) {
      this.dialogDelete = false;
      const pack = this.deleteOption.paquete;
      axios
        .delete(
          `http://localhost:8080/backendIPC2/api/paquetes?paquete=${pack}`
        )
        .then((response) => {
          console.log("Paquete eliminado con éxito");
          this.obtenerPaquetes();
        })
        .catch((error) => {
          console.error("Error al eliminar paquete", error);
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
