<template>
  <div class="pa-4 text-center">
    <v-dialog v-model="dialog" max-width="600">
      <template v-slot:activator="{ props: activatorProps }">
        <v-btn
          class="text-none font-weight-regular"
          prepend-icon="mdi-account"
          text="New Router"
          variant="tonal"
          v-bind="activatorProps"
        ></v-btn>
      </template>

      <v-card prepend-icon="mdi-account" title="New Router">
        <v-card-text>
          <v-row dense>
            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="IdRuta"
                :rules="[rules.required]"
                label="ID Router*"
                type="number"
                required
              ></v-text-field>
            </v-col>
             <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="packages"
                :rules="[rules.required]"
                label="Total packages*"
                type="number"
                required
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="Start"
                :rules="[rules.required]"
                label="Start*"
                required
              ></v-text-field>
            </v-col>
            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="End"
                :rules="[rules.required]"
                label="End*"
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
              !IdRuta ||
              !packages ||
              !Start||
              !End
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
           Hubo un error al crear la nuevo ruta por favor verificar los datos 
           <br>
           Posibles errores:ID de la ruta ya usada 
           <br>

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
    IdRuta: "",
    packages: "",
    Start: "",
    End: "",
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
     this.IdRuta = "";
     this.packages = "";
     this.Start = "";
     this.End = "";
      
    },

    //aca esta la logica para guardar los datos y responder segun sea lo necesario
    saveDialogAndSubmitForm() {
      this.submitForm();
    },

    submitForm() {
      const createRouter = {
        idRuta: this.IdRuta,
        cantidadPuntos: this.packages,
        start: this.Start,
        end: this.End,
      };
      
      const jsonRouter = JSON.stringify(createRouter);
      console.log(jsonRouter);
      axios
      .post("http://localhost:8080/backendIPC2/api/router", jsonRouter)
      .then((response) => {
        if(Object.keys(response.data).length > 0){
        this.dialog = false;
        this.clearTextFields
        console.log(response.data)
        }else{
          this.sheet = true;
        }
      })
      .catch((error) => {
            console.error("Error al enviar la solicitud:", error);

          });
    }
  },
};
</script>

<style></style>
