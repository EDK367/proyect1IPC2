<template>
  <h1>Table of Users Admins</h1>

  <div class="table_Container">
    <div class="new">
      <nuevoCliente />
    </div>
    <v-table height="400px" fixed-header>
      <div class="deletes">
       <edit/>
      </div>

      <thead>
        <tr>
          <th class="text-left">Name</th>
          <th class="text-left">Last Name</th>
          <th class="text-left">NIT</th>
          <th class="text-left">Operator</th>
          <th class="text-left">Receptionist</th>
          <th class="text-left">Number Phone</th>
          <th class="text-left">Options</th>
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
          <td>
            <v-btn
              @click="option(cliente)"
              prepend-icon="$vuetify"
              variant="text"
            >
              SELECT
            </v-btn>
            <v-icon size="small" @click="deleteItem(cliente)">
              mdi-delete
            </v-icon>
          </td>
        </tr>
      </tbody>
    </v-table>
    <v-dialog v-model="dialogDelete" max-width="500px">
      <v-card>
        <v-card-title class="text-h5"
          >Are you sure you want to delete this USER?</v-card-title
        >
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue-darken-1" variant="text" @click="closeDelete"
            >Cancel</v-btn
          >
          <v-btn color="blue-darken-1" variant="text" @click="deleteItemConfirm"
            >OK</v-btn
          >
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import axios from "axios";
import nuevoCliente from "./nuevoCliente.vue"
import edit from "../option/deleteAndPutCliente.vue"
export default {
  data() {
    return {
      deleteOption: null,
      dialogDelete: false,
      clientes: [],
    };
  },

  components: {
    nuevoCliente,
    edit,
  },

  mounted() {
    this.obtenerCliente();
    //actualizar la tabla constantemente
    setInterval(this.obtenerCliente, 5000); //2000 == 20 segundos
  },

  methods: {
    obtenerCliente() {
      axios //nombre de como lo declaraste
        .get("http://localhost:8080/backendIPC2/api/cliente") //el metodo que vas hacer

        .then((response) => {
          this.clientes = response.data;
        })
        .catch((error) => {
          console.error("error al obtener datos");
        });
    },
    //metodo para editar y eliminar se obtiene el form
    option(cliente) {
      localStorage.setItem("optionUser", JSON.stringify(cliente));
      console.log(
        "Contenido de localStorage:",
        localStorage.getItem("optionUser")
      );
    },

    deleteItem(cliente) {
      console.log("Delete", cliente);
      this.deleteOption = cliente;
      this.dialogDelete = true;
    },
    closeDelete() {
      this.dialogDelete = false;
    },
    deleteItemConfirm(cliente) {
      this.dialogDelete = false;
      const clienteJson = JSON.stringify(this.deleteOption);
      console.log("Este es el JSON del administrador:", clienteJson);

      axios
        .delete("http://localhost:8080/backendIPC2/api/cliente", {
          data: clienteJson,
        })
        .then((response) => {
          console.log("cliente eliminado con éxito");
          this.obtenerCliente();
        })
        .catch((error) => {
          console.error("Error al eliminar cliente", error);
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
.deletes {
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
