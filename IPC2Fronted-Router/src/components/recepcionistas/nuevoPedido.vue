<template>
  <div class="pa-4 text-center">
    <v-dialog v-model="dialog" max-width="600">
      <template v-slot:activator="{ props: activatorProps }">
        <v-btn
          class="text-none font-weight-regular"
          prepend-icon="mdi-account"
          text="New Order"
          variant="tonal"
          v-bind="activatorProps"
        ></v-btn>
      </template>

      <v-card prepend-icon="mdi-account" title="New Order">
        <v-card-text>
          <v-row dense>
            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="bodega"
                :rules="[rules.required]"
                label="ID Warehouse start*"
                type="number"
                required
              ></v-text-field>
            </v-col>
            <v-col cols="12" md="4" sm="6">
              <v-select
                v-model="destino"
                :items="bodegasDisponibles"
                item-title="idBodega"
                label="Destination Warehouse"
              >
                <template v-slot:item="{ props, item }">
                  <v-list-item
                    v-bind="props"
                    :subtitle="item.raw.department"
                  ></v-list-item>
                </template>
              </v-select>
            </v-col>
            <v-col cols="12" sm="6"> </v-col>
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
            :disabled="!bodega || !destino"
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

export default {
  data() {
    return {
      sheet: false,
      dialog: false,
      bodega: "",
      destino: "",
      rules: {
        required: (value) => !!value || "Field is required",
      },
      bodegasDisponibles: [],
       showBottomSheet: false,
    };
  },
  methods: {
    closeDialogAndClearFields() {
      this.dialog = false;
      this.clearTextFields();
    },
    clearTextFields() {
      this.bodega = "";
      this.destino = "";
    },
    saveDialogAndSubmitForm() {
      this.submitForm();
    },
    submitForm() {
      const createRouter = {
        bodegaActual: this.bodega,
        destinoController: this.destino,
      };

      axios
        .post("http://localhost:8080/backendIPC2/api/pedidos", createRouter)
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
    cargarBodegas() {
      axios
        .get("http://localhost:8080/backendIPC2/api/bodegaOption")
        .then((responses) => {
          this.bodegasDisponibles = responses.data;
          console.log(this.bodegasDisponibles);
        })
        .catch((error) => {
          console.error("Error loading warehouses:", error);
        });
    },
  },
  mounted() {
    this.cargarBodegas();
  },
};
</script>

<style></style>
