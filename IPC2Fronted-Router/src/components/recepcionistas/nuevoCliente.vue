<template>
  <div class="pa-4 text-center">
    <v-dialog v-model="dialog" max-width="600">
      <template v-slot:activator="{ props: activatorProps }">
        <v-btn
          class="text-none font-weight-regular"
          prepend-icon="mdi-account"
          text="New Customer"
          variant="tonal"
          v-bind="activatorProps"
        ></v-btn>
      </template>

      <v-card prepend-icon="mdi-account" title="New Order">
        <v-card-text>
          <v-row dense>
            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="nombre"
                :rules="[rules.required]"
                label="Customer Name*"
                required
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="apellido"
                :rules="[rules.required]"
                label="Customer Last Name*"
                required
              ></v-text-field>
            </v-col>
            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="nit"
                :rules="[rules.required]"
                label="NIT*"
                type="number"
                required
              ></v-text-field>
            </v-col>
            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="cuiOperador"
                :rules="[rules.required]"
                label="Operator*"
                type="number"
                disabled
                required
              ></v-text-field>
            </v-col>
            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="cuiRecepcionista"
                :rules="[rules.required]"
                label="Receptionist*"
                type="number"
                disabled
                required
              ></v-text-field>
            </v-col>
            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="telefono"
                :rules="[rules.required]"
                label="Number Phone*"
                type="number"
                required
              ></v-text-field>
            </v-col>
          </v-row>

          <small class="text-caption text-medium-emphasis">
            *indicates required field
          </small>
        </v-card-text>

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
            !nombre || 
            !apellido ||
            !nit ||
            !cuiOperador ||
            !cuiRecepcionista ||
            !telefono
            "
            @click="saveDialogAndSubmitForm"
          ></v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
  <div class="text-center">
    <v-bottom-sheet v-model="sheet" inset>
      <v-card class="text-center" height="200">
        <v-card-text>
          <v-btn variant="text" @click="sheet = !sheet"> close </v-btn>

          <br />
          <br />

          <div>
            There was an error creating the new route. Please check the data.
            <br />
            Possible errors: ID of the route already used
            <br />
          </div>
        </v-card-text>
      </v-card>
    </v-bottom-sheet>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";

export default {
  data() {
    return {
      sheet: false,
      dialog: false,
      nombre: "",
      apellido: "",
      nit: "",
      cuiOperador: "",
      cuiRecepcionista: "",
      telefono: "",
      rules: {
        required: (value) => !!value || "Field is required",
      },
    };
  },
  computed: {
    ...mapState(["loginData"]),
  },
  mounted() {
    this.cargaDatos();
  },
  methods: {
    cargaDatos() {
      const loginData = JSON.parse(localStorage.getItem("loginData"));
      if (loginData) {
        this.cuiOperador = loginData.cuiOperador;
        this.cuiRecepcionista = loginData.cuiRecepcionista;
      } else {
        this.cuiOperador = "Error Comuniquese con alguien";
      }
    },
    closeDialogAndClearFields() {
      this.dialog = false;
      this.clearTextFields();
    },
    clearTextFields() {
      this.nombre = "";
      this.apellido = "";
      this.nit = "";
      this.cuiOperador = "";
      this.cuiRecepcionista = "";
      this.telefono = "";
    },
    saveDialogAndSubmitForm() {
      this.submitForm();
    },
    submitForm() {
      const createCustomer = {
        nombre: this.nombre,
        apellido: this.apellido,
        NIT: this.nit,
        cuiOperador: this.cuiOperador,
        cuiRecepcionista: this.cuiRecepcionista,
        telefono: this.telefono
      };

      axios
        .post("http://localhost:8080/backendIPC2/api/cliente", createCustomer)
        .then((response) => {
          if (Object.keys(response.data).length > 0) {
            this.dialog = false;
            this.clearTextFields();
            console.log(response.data);
          } else {
            this.sheet = true;
          }
        })
        .catch((error) => {
          console.error("Error submitting form:", error);
          this.sheet = true;
        });
    },
  },
};
</script>

<style></style>
