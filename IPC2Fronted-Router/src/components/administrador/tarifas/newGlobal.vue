<template>
  <div class="pa-4 text-center">
    <v-dialog v-model="dialog" max-width="600">
      <template v-slot:activator="{ props: activatorProps }">
        <v-btn
          class="text-none font-weight-regular"
          prepend-icon="mdi-account"
          text="New Global Rate"
          variant="tonal"
          v-bind="activatorProps"
        ></v-btn>
      </template>

      <v-card prepend-icon="mdi-account" title="New Global Rate">
        <v-card-text>
          <v-row dense>
            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="idAdmin"
                :rules="[rules.required]"
                label="ID*"
                type="number"
                disabled
                required
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="idGlobal"
                label="Global Rate ID*"
                disabled
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="quota"
                :rules="[rules.required]"
                label="Quota*"
                type="number"
                required
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="date"
                :rules="[rules.required]"
                label="date*"
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
              !idAdmin ||
              !quota ||
              !date 
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
         ERROR al Generar una nueva Tarifa, comuniquese con el 
         <br>
         Proyect manager o revise la Fecha
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
  data: () => ({
    sheet: false,
    dialog: false,
    idAdmin: "",
    idGlobal: "",
    quota: "",
    date: "",
    rules: {
      required: (value) => !!value || "Field is required",
    },
    showBottomSheet: false,
  }),
  //esta opcion es la que trae el JSON
  computed: {
    ...mapState(["loginData"]),
  },
  //carga los datos
  mounted() {
    this.cargaDatos();
  },
  methods: {
    closeDialogAndClearFields() {
      (this.dialog = false), 
      this.clearTextFields();
    },

    clearTextFields() {
      this.idAdmin = "";
      this.idGlobal = "";
      this.quota = "";
      this.date = "";
    },

    //aca esta la logica para guardar los datos y responder segun sea lo necesario
    saveDialogAndSubmitForm() {
      this.submitForm();
    },
    cargaDatos(){
        const loginData = JSON.parse(localStorage.getItem("loginData"));
        if(loginData){
            this.idAdmin = loginData.cuiAdmin;
        }else{
            this.idAdmin = "Error Comuniquese con alguien"
        }
    },

    
    submitForm() {
      const create = {
        tarifaG: this.quota,
        fechaInicio: this.date,
        cuiAdmin: this.idAdmin,
      };
    const jsonTarifa = JSON.stringify(create);
      console.log(create);
      axios
      .post("http://localhost:8080/backendIPC2/api/tarifaGlobal",
      create)
      .then((response)=> {
        if(Object.keys(response.data).length > 0){
            this.dialog = false;
            console.log("Tarifa global creado exitosamente");
        }else{
            this.sheet = true;
        }
      }) 
      .catch((error) => {
            console.error("Error al enviar la solicitud:", error);
            
          });
    },
  },



};
</script>

<style></style>
