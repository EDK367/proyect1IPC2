<template>
  <v-container class="principal">
    <v-row>
      <v-col cols="12">
        <h2>Administradores</h2>
        <v-btn color="primary" @click="newAdmin">Nuevo Administrador</v-btn>
        <component :is="newAdmin" :key="newAdmin"></component>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-simple-table>
          <template v-slot:default>
            <thead>
              <tr>
                <th>CUI</th>
                <th>Nombre</th>
                <th>Apellido</th>
                <th>Correo</th>
                <th>Contraseña</th>
                <th>Opciones</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="admin in administrador" :key="admin.cuiAdmin">
                <td>{{ admin.cuiAdmin }}</td>
                <td>{{ admin.nombre }}</td>
                <td>{{ admin.apellido }}</td>
                <td>{{ admin.correo }}</td>
                <td>{{ admin.contraseña }}</td>
                <td>
                  <v-btn color="warning" @click="editar(admin)">Editar</v-btn>
                  <v-btn color="error" @click="eliminar(admin)">Eliminar</v-btn>
                </td>
              </tr>
            </tbody>
          </template>
        </v-simple-table>
      </v-col>
    </v-row>
  </v-container>
</template>


<script>
import axios from "axios";

export default {
  data() {
    return {
      administrador: [],
    };
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
          console.error("Error al obtener datos de administrador");
        });
    },
    eliminar(admin) {
      // Convertir el objeto admin a JSON
      const adminJson = JSON.stringify(admin);
      console.log("Este es el JSON del administrador:", adminJson);

      // Enviar la solicitud DELETE con el JSON como parte del cuerpo de la solicitud
      axios
        .delete("http://localhost:8080/backendIPC2/api/admin", {
          data: adminJson,
        })
        .then((response) => {
          console.log("Administrador eliminado con éxito");
          // Realizar alguna acción adicional si es necesario, como actualizar la lista de administradores
          // Por ejemplo, puedes volver a cargar la lista de administradores después de eliminar uno
          this.obtenerAdmin();
        })
        .catch((error) => {
          console.error("Error al eliminar administrador", error);
        });
    },
  },
};
</script>

<style scoped>
table {
  width: 100%;
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

.nuevo_admin {
  float: right;
}

.btn {
  margin: 0px 5px;
}
</style>
