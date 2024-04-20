<template>
  <div class="pa-4 text-center">
    <v-dialog v-model="dialog" max-width="600">
      <template v-slot:activator="{ props: activatorProps }">
        <v-btn
          class="text-none font-weight-regular"
          prepend-icon="mdi-account"
          text="Edit Point Controller"
          variant="tonal"
          v-bind="activatorProps"
          @click="cargaDatos()"
        ></v-btn>
      </template>

      <v-card prepend-icon="mdi-account" title="New User">
        <v-card-text>
          <v-row dense>
            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="idPoint"
                :rules="[rules.required]"
                label="ID Point*"
                type="number"
                disabled
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
           No se pudo realizar la actualizacion, posibles errores:
           <br>
           El Operador no existe
           <br>
           Ya no exise el Punto seleccionado
           <br>
           Si los problemas persisten por favor comunicarse con un proyect Manajer
          </div>
        </v-card-text>
      </v-card>
    </v-bottom-sheet>
  </div>
</template>

<script>
import axios from "axios";
//libray para poder exportar
import { mapState, mapActions } from "vuex";

export default {
  data: () => ({
    sheet: false,
    dialog: false,
    idPoint: "",
    nombre: "",
    idOperator: "",
    rules: {
      required: (value) => !!value || "Field is required",
    },
    showBottomSheet: false,
  }),

  //esta opcion es la que trae el JSON
  computed: {
    ...mapState(["optionPoint"]),
  },
  //carga los datos
  mounted() {
    this.cargaDatos();
  },

  methods: {
    //transfiere los datos
    cargaDatos() {
      const optionPoint = JSON.parse(localStorage.getItem("optionPoint"));
      if (optionPoint) {
       
        this.idPoint = optionPoint.IdControl;
        this.nombre = optionPoint.nombre;
        this.idOperator = optionPoint.cuiOperador;

      } else {
        // Si no hay datos en el localStorage
        this.idAdmin = "";
        this.nombre = "";
        this.idOperator = "";

      }
    },
    //cierra
    closeDialogAndClearFields() {
      this.dialog = false;
      
    },
    //aca esta la logica para guardar los datos y responder segun sea lo necesario
    saveDialogAndSubmitForm() {
      this.submitForm();
    },

    submitForm() {
      const update = {
        IdControl: this.idPoint,
        nombre: this.nombre,
        cuiOperador: this.idOperator,
      };
      const jsonUser = JSON.stringify(update);
      console.log(update);

      axios
      .put("http://localhost:8080/backendIPC2/api/point", update)
      .then((response) => {
        console.log(response.data)
        if(Object.keys(response.data).length > 0){
          this.dialog = false;
        }else{
          console.log("Saber que error ocurrio")
          this.sheet = true;
        }
      })
      .catch((error) => {
            console.error("Error al enviar la solicitud:", error);
            
          });
    }
}
}
</script>

<style></style>
