<template>
  <div class="pa-4 text-center">
    <v-dialog v-model="dialog" max-width="600">
      <template v-slot:activator="{ props: activatorProps }">
        <v-btn
          class="text-none font-weight-regular"
          prepend-icon="mdi-account"
          text="New Package"
          variant="tonal"
          v-bind="activatorProps"
          @click="cargaDatos()"
        ></v-btn>
      </template>

      <v-card prepend-icon="mdi-account" title="New Package">
        <v-card-text>
          <v-row dense>
            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="cuiOperador"
                :rules="[rules.required]"
                label="Charge Operator*"
                type="number"
                disabled
                required
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="cuiRecepcionista"
                :rules="[rules.required]"
                label="Charge Receptionist*"
                type="number"
                disabled
                required
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="4" sm="6">
              <v-select
                v-model="pedido"
                :items="pedidos"
                item-title="noPedido"
                label="Number Order*"
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
                v-model="bodegaInicial"
                :rules="[rules.required]"
                label="Warehouse*"
                type="number"
                required
              ></v-text-field>
            </v-col>
            

         <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="cliente"
                :rules="[rules.required]"
                label="Customer/Cliente*"
                type="number"
                required
              ></v-text-field>
            </v-col>

             <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="peso"
                :rules="[rules.required]"
                label="Weight*"
                type="number"
                required
              ></v-text-field>
            </v-col>
             <v-col cols="12" sm="6">
              <v-row>
                <v-col cols="12">
                  <v-checkbox
                    v-model="activete"
                    :color="activete ? 'green' : 'red'"
                    label="Activete"
                    @click="toggleActivete"
                    hide-details
                  >
                  <template v-slot:label>
                    <v-icon :color="activete ? 'green' : 'red'">
                      {{activete ? 'mdi-cash' : 'mdi-cash-multiple'}}
                    </v-icon>
                  </template>
                  </v-checkbox>
                </v-col>
              </v-row>
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
            !cuiOperador ||
            !cuiRecepcionista ||
            !pedido ||
            !bodegaInicial ||
            !cliente || 
            !peso
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
             No se pudo crear un nuevo paquete Posibles Errores
            <br />
            El cliente no Existe 
            <br />
            No existe la tarifa Local Para esta Bodega
            <br />
            La bodega Inicial no Coincide con la Bodega del No.Pedido
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
      cuiOperador: "",
      cuiRecepcionista: "",
      pedido: "",
      bodegaInicial: "",
      cliente: "",
      peso: "",
      activete: true,
      rules: {
        required: (value) => !!value || "Field is required",
      },
      pedidos: [],
      customer: [],
    };
  },
   computed: {
    ...mapState(["loginData"]),
  },
   mounted() {
    this.cargaDatos();
    this.cargarBodegas()
  },

  methods: {
    cargaDatos(){
        const loginData = JSON.parse(localStorage.getItem("loginData"));
        if(loginData){
            this.cuiOperador = loginData.cuiOperador;
            this.cuiRecepcionista = loginData.cuiRecepcionista;
        }else{
            this.cuiOperador = "Error Comuniquese con alguien"
        }
    },
    closeDialogAndClearFields() {
      this.dialog = false;
      this.clearTextFields();
    },
    clearTextFields() {
      this.cuiOperador = "";
      this.cuiRecepcionista = "";
      this.pedido = "";
      this.bodegaInicial = "";
      this.cliente = "";
      this.peso = "";
      this.activete = true;
    },
    saveDialogAndSubmitForm() {
      this.submitForm();
    },
    submitForm() {
      const createRouter = {
        //en el Json        //en el submit
        cuiOperador: this.cuiOperador,
        cuiRecepcionista: this.cuiRecepcionista,
        noPedido: this.pedido,
        bodegaInicial: this.bodegaInicial,
        cliente: this.cliente,
        peso: this.peso,
        tarifaGlobal: this.activete,
      };
          console.log("este es el form ", createRouter)

      axios
        .post("http://localhost:8080/backendIPC2/api/paquetes", createRouter)
        .then((response) => {
          console.log(response.data)
          if (Object.keys(response.data).length > 0) {
            this.dialog = false;
            this.clearTextFields();
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
        .get("http://localhost:8080/backendIPC2/api/pedidos")
        .then((responses) => {
          this.pedidos = responses.data;
          this.pedidos = this.pedidos.filter(pedidos => pedidos.estado === 'C');

          console.log(this.pedidos);
        })
        .catch((error) => {
          console.error("Error loading warehouses:", error);
        });
    },
    cargarClientes(){
        axios
        .get("http://localhost:8080/backendIPC2/api/cliente")
        .then((respon) => {
          this.customer = respon.data;
          console.log(this.customer);
        })
        .catch((error) => {
          console.error("Error loading customer:", error);
        });
    },
  },
  mounted() {
    this.cargaDatos();
    this.cargarBodegas();
  },
};
</script>

<style></style>
