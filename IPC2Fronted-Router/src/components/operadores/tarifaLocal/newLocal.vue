<template>
  <div class="pa-4 text-center">
    <v-dialog v-model="dialog" max-width="600">
      <template v-slot:activator="{ props: activatorProps }">
        <v-btn
          class="text-none font-weight-regular"
          prepend-icon="mdi-account"
          text="New Local Rate"
          variant="tonal"
          v-bind="activatorProps"
        ></v-btn>
      </template>

      <v-card prepend-icon="mdi-account" title="New Local Rate">
        <v-card-text>
          <v-row dense>
            <v-col cols="12" md="4" sm="6">
              <v-select
                v-model="bodega"
                :items="point"
                item-title="IdControl"
                label="Warehouse"
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
                v-model="quota"
                :rules="[rules.required]"
                label="Quota*"
                type="number"
                required
              ></v-text-field>
            </v-col>

<v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="cuiOperador"
                :rules="[rules.required]"
                label="ID*"
                type="number"
                disabled
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
              !bodega ||
              !quota ||
              !cuiOperador 
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
         Proyect manager
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
    bodega: "",
    cuiOperador: "",
    quota: "",
    rules: {
      required: (value) => !!value || "Field is required",
    },
    point: [],
    showBottomSheet: false,
  }),
  //esta opcion es la que trae el JSON
  computed: {
    ...mapState(["loginData"]),
  },
  //carga los datos
  mounted() {
    this.cargaDatos();
    this.cargarBodegas();
  },
  methods: {
    closeDialogAndClearFields() {
      (this.dialog = false), 
      this.clearTextFields();
    },

    clearTextFields() {
      this.bodega = "";
      this.cuiOperador = "";
      this.quota = "";
    },

    //aca esta la logica para guardar los datos y responder segun sea lo necesario
    saveDialogAndSubmitForm() {
      this.submitForm();
    },
    cargaDatos(){
        const loginData = JSON.parse(localStorage.getItem("loginData"));
        if(loginData){
            this.cuiOperador = loginData.cuiOperador;
        }else{
            this.cuiOperador = "Error Comuniquese con alguien"
        }
    },

    
    submitForm() {
      const create = {
        tarifaLocal: this.quota,
        IDControl: this.bodega,
        cuiOperador: this.cuiOperador,
      };
    const jsonTarifa = JSON.stringify(create);
      console.log(create);
      axios
      .post("http://localhost:8080/backendIPC2/api/tarifaLocal",
      create)
      .then((response)=> {
        if(Object.keys(response.data).length > 0){
            this.dialog = false;
            console.log("Tarifa Local creado exitosamente");
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
        .get("http://localhost:8080/backendIPC2/api/point")
        .then((respon) => {
          this.point = respon.data;
         this.point = this.point.filter(point => point.cuiOperador === this.cuiOperador);
         
        })
        .catch((error) => {
          console.error("Error loading warehouses:", error);
        });
    },

  },



};
</script>

<style></style>
