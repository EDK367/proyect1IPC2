<template>
  <div class="pa-4 text-center">
    <v-dialog v-model="dialog" max-width="600">
      <template v-slot:activator="{ props: activatorProps }">
        <v-btn
          class="text-none font-weight-regular"
          prepend-icon="mdi-account"
          text="New Point Controller"
          variant="tonal"
          v-bind="activatorProps"
        ></v-btn>
      </template>

      <v-card prepend-icon="mdi-account" title="New Point Controller">
        <v-card-text>
          <v-row dense>
            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="idPoint"
                :rules="[rules.required]"
                label="ID Point*"
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
                v-model="idOperator"
                :rules="[rules.required]"
                label="Charge Operator*"
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
              !idPoint ||
              !nombre ||
              !idOperator
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
           Hubo un error al crear el nuevo punto de control por favor verificar los datos 
           <br>
           Y verifique si el operador existe
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
    idPoint: "",
    nombre: "",
    idOperator:"",
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
      this.idPoint = "";
      this.nombre = "";
      this.idOperator = "";
      
    },

    //aca esta la logica para guardar los datos y responder segun sea lo necesario
    saveDialogAndSubmitForm() {
      this.submitForm();
    },

    submitForm() {
      const createPoint = {
        IdControl: this.idPoint,
        nombre: this.nombre,
        cuiOperador: this.idOperator,
      };
      
      const jsonPoint = JSON.stringify(createPoint);
      console.log(createPoint);

      axios
      .post("http://localhost:8080/backendIPC2/api/point", jsonPoint)
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
