<template>
  <div class="pa-4 text-center">
    <v-dialog v-model="dialog" max-width="600">
      <template v-slot:activator="{ props: activatorProps }">
        <v-btn
          class="text-none font-weight-regular"
          prepend-icon="mdi-account"
          text="New Journey"
          variant="tonal"
          v-bind="activatorProps"
        ></v-btn>
      </template>

      <v-card prepend-icon="mdi-account" title="New Journey">
        <v-card-text>
          <v-row dense>
             <v-col cols="12" md="4" sm="6">
              <v-select
                v-model="idRuta"
                :items="rutas"
                item-title="idRuta"
                label="ID Router"
              >
                <template v-slot:item="{ props, item }">
                  <v-list-item
                    v-bind="props"
                    :subtitle="item.raw.department"
                  ></v-list-item>
                </template>
              </v-select>
            </v-col>
            <v-col cols="12" md="4" sm="6">
              <v-select
                v-model="idControl"
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

            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="position"
                :rules="[rules.required]"
                label="Position*"
                type="number"
                required
              ></v-text-field>
            </v-col>
            <v-col cols="12" sm="6"> </v-col>
          </v-row>

          <small class="text-caption text-medium-emphasis"
            >*indicates required field</small
          >
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
              !idRuta ||
              !idControl ||
              !position
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
           Hubo un error al crear una nueva ruta
           <br>
           Posibles errores:La Ruta no existe
           <br>
            El punto de control no existe
            <br>
            La posicion ya esta ocupada
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
    idRuta: "",
    idControl: "",
    position: "",

    rules: {
      required: (value) => !!value || "Field is required",
    },
     bodegasDisponibles: [],
     rutas: [],
    showBottomSheet: false,
  }),

  methods: {
    closeDialogAndClearFields() {
      (this.dialog = false), 
      this.clearTextFields();
    },

    clearTextFields() {
     this.idRuta = "";
     this.idControl = "";
     this.position = "";
    },

    //aca esta la logica para guardar los datos y responder segun sea lo necesario
    saveDialogAndSubmitForm() {
      this.submitForm();
    },

    submitForm() {
      const createTrayecto = {
        idRuta: this.idRuta,
        idControl: this.idControl,
        posicion: this.position,
      };
      
      const jsonTrayecto = JSON.stringify(createTrayecto);
      console.log(jsonTrayecto);
      axios
      .post("http://localhost:8080/backendIPC2/api/trayecto", createTrayecto)
      .then((response) => {
        if(Object.keys(response.data).length > 0){
        this.dialog = false;
        this.clearTextFields();
        console.log(response.data)
        }else{
          this.sheet = true;
        }
      })
      .catch((error) => {
            console.error("Error al enviar la solicitud:", error);

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
     cargarRutas() {
      axios
        .get("http://localhost:8080/backendIPC2/api/router")
        .then((respon) => {
          this.rutas = respon.data;
          console.log(this.rutas);
        })
        .catch((error) => {
          console.error("Error loading warehouses:", error);
        });
    },
  },
   mounted() {
    this.cargarBodegas();
    this.cargarRutas();
  },
  
};
</script>

<style></style>
