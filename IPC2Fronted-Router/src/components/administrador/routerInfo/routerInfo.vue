<template>
  <div>
    
    <h1>Table of Journey</h1>
    <div class="table_Container">
      <div class="new">
        <nuevoTrayecto/>
        
      </div>
      <div class="busqueda">
      <input type="text" v-model="searchTerm" placeholder="Search Router.">
      </div>
      <v-table height="400px" fixed-header>
        <div class="deletes">
          <putDelete/>
        </div>
        
        <thead> 
          <tr>
            <th class="text-left">ID Router</th>
            <th class="text-left">ID Controller</th>
            <th class="text-left">Position</th>
            <th class="text-left">Activete</th>
            <th class="text-left">Options</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="trayecto in filteredTrayecto" :key="trayecto.IDRuta">
            <td>{{ trayecto.idRuta }}</td>
            <td>{{ trayecto.idControl }}</td>
            <td>{{ trayecto.posicion }}</td>
            <td><v-icon :color="trayecto.activete ? 'green' : 'red'">
                {{ trayecto.activete ? 'mdi-flag-checkered' : 'mdi-flag-remove' }}
              </v-icon></td>
            <td>
              <v-btn @click="option(trayecto)" prepend-icon="$vuetify" variant="text"> SELECT </v-btn>
              <v-icon size="small" @click="deleteItem(trayecto)">mdi-delete</v-icon>
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
  </div>
</template>

<script>
import axios from "axios";
import nuevoTrayecto from "./newTrayecto.vue";
import putDelete from "./deleteAndPuteRouter";

export default {
  data() {
    return {
      deleteOption: null,
      dialogDelete: false,
      trayecto: [],
      searchTerm: "",
    };
  },
  components: {
    nuevoTrayecto,
    putDelete,
  },
  mounted() {
    this.obtenerTrayecto();
    setInterval(this.obtenerTrayecto, 5000);
  },
  computed: {

    filteredTrayecto() {
      if (!this.searchTerm) return this.trayecto;
      return this.trayecto.filter((trayecto) => {
        return trayecto.idRuta.toString().toLowerCase().includes(this.searchTerm.toLowerCase());
      });
      
    },
  },
  methods: {
    obtenerTrayecto() {
      axios
        .get("http://localhost:8080/backendIPC2/api/trayecto")
        .then((response) => {
          this.trayecto = response.data;
        })
        .catch((error) => {
          console.error("error al obtener datos");
        });
    },
    option(trayecto) {
      localStorage.setItem('optionPoint', JSON.stringify(trayecto));
      console.log('Contenido de localStorage:', localStorage.getItem('optionPoint'));
    },
    deleteItem(trayecto) {
      console.log("Delete", trayecto)
      this.deleteOption = trayecto;
      this.dialogDelete = true;
    },
    closeDelete() {
      this.dialogDelete = false;
    },
    deleteItemConfirm(trayecto) {
      this.dialogDelete = false;
      const trayectoJson = JSON.stringify(this.deleteOption);
      console.log("Este es el JSON del administrador:", trayectoJson);
      axios
        .delete("http://localhost:8080/backendIPC2/api/trayecto", {
          data: trayectoJson,
        })
        .then((response) => {
          console.log("Administrador eliminado con éxito");
          this.obtenerTrayecto();
        })
        .catch((error) => {
          console.error("Error al eliminar router", error);
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
th, td {
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
  top: 110px;
  position: absolute;
  right: 1000px;
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
