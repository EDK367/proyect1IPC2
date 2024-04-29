<template>
  <div class="pa-4 text-center">
    <v-dialog v-model="dialog" max-width="600">
      <template v-slot:activator="{ props: activatorProps }">
        <v-btn
          class="text-none font-weight-regular"
          prepend-icon="mdi-account"
          text="New User"
          variant="tonal"
          v-bind="activatorProps"
        ></v-btn>
      </template>

      <v-card prepend-icon="mdi-account" title="New User">
        <v-card-text>
          <v-row dense>
            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="idAdmin"
                :rules="[rules.required]"
                label="ID*"
                type="number"
                required
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="nombre"
                :rules="[rules.required]"
                label="Names*"
                required
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="apellido"
                :rules="[rules.required]"
                label="Last name*"
                required
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="correo"
                :rules="[rules.required]"
                label="Email*"
                required
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="contraseña"
                :rules="[rules.required]"
                label="Password*"
                required
              ></v-text-field>
            </v-col>

            <v-col cols="12" sm="6"> </v-col>
          </v-row>

          <small class="text-caption text-medium-emphasis"
            >*indicates required field</small
          >
        </v-card-text>

        <v-col cols="12" sm="6">
          <v-select
            v-model="rank"
            :rules="[rules.required]"
            :items="['Admin', 'Operator']"
            label="Rank*"
            required
          ></v-select>
        </v-col>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn
            text="Close"
            variant="plain"
            @click="closeDialogAndClearFields"
          ></v-btn>

          <v-btn
            color="primary"
            text="Save"
            variant="tonal"
            :disabled="
              !idAdmin ||
              !nombre ||
              !apellido ||
              !correo ||
              !contraseña ||
              !rank
            "
            @click="saveDialogAndSubmitForm"
          ></v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
    <div class="text-center">
    <v-bottom-sheet v-model="sheet" inset>
      <v-card
        class="text-center"
        height="200"
      >
        <v-card-text>
          <v-btn
            variant="text"
            @click="sheet = !sheet"
          >
            close
          </v-btn>

          <br>
          <br>

          <div>
           El ID o el correo Electronico ya existen en la base de datos
           Revisar los datos
          </div>
        </v-card-text>
      </v-card>
    </v-bottom-sheet>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data: () => ({
    sheet: false,
    dialog: false,
    idAdmin: "",
    nombre: "",
    apellido: "",
    correo: "",
    contraseña: "",
    rank: "",
    rules: {
      required: (value) => !!value || "Field is required",
    },
    showBottomSheet: false,
  }),

  methods: {
    closeDialogAndClearFields() {
      (this.dialog = false), 
      this.clearTextFields();
    },

    clearTextFields() {
      this.idAdmin = "";
      this.nombre = "";
      this.apellido = "";
      this.correo = "";
      this.contraseña = "";
      this.rank = "";
    },

    //aca esta la logica para guardar los datos y responder segun sea lo necesario
    saveDialogAndSubmitForm() {
      this.submitForm();
    },

    submitForm() {
      const create = {
        cuiAdmin: this.idAdmin,
        nombre: this.nombre,
        apellido: this.apellido,
        correo: this.correo,
        contraseña: this.contraseña,
      };
      const createOperator = {
        cuiOperador: this.idAdmin,
        nombre: this.nombre,
        apellido: this.apellido,
        correo: this.correo,
        contraseña: this.contraseña,
      };
      
      const jsonUser = JSON.stringify(create);
      const jsonUserOp = JSON.stringify(createOperator);
      console.log(create);

      //detecta para donde ira el post
      if (this.rank === "Admin") {
        console.log("Admin");
        axios
          .post("http://localhost:8080/backendIPC2/api/admin", create)
          .then((response) => {
            console.log(response.data);
            if (Object.keys(response.data).length > 0) {
              console.log("Usuario creado exitosamente");
              this.dialog = false;
              this.clearTextFields();
            } else {
              console.log("No se pudo crear");
              this.sheet = true;
            }
          })
          .catch((error) => {
            console.error("Error al enviar la solicitud:", error);
            
          });
      } else if (this.rank === "Operator") {
        console.log("Operator");
        axios
        .post("http://localhost:8080/backendIPC2/api/operador", createOperator)
         .then((response) => {
            console.log(response.data);
            if (Object.keys(response.data).length > 0) {
              console.log("Usuario creado exitosamente");
              this.dialog = false;
              this.clearTextFields();
            } else {
              console.log("No se pudo crear");
              this.sheet = true;
            }
          })
          .catch((error) => {
            console.error("Error al enviar la solicitud:", error);
          });
      } else {
        console.log("Hay un error de codigo");
      }
    },
  },
};
</script>

<style></style>
