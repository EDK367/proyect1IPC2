<template>
  <div class="pa-4 text-center">
    <v-dialog v-model="dialog" max-width="600">
      <template v-slot:activator="{ props: activatorProps }">
        <v-btn
          class="text-none font-weight-regular"
          prepend-icon="mdi-account"
          text="Activete"
          variant="tonal"
          v-bind="activatorProps"
          @click="cargaDatos()"
        ></v-btn>
      </template>

      <v-card prepend-icon="mdi-account" title="Activete">
        <v-card-text>
          <v-row dense>
            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="idRuta"
                :rules="[rules.required]"
                label="ID Router*"
                type="number"
                disabled
                required
              ></v-text-field>
            </v-col>
            <v-col cols="12" md="4" sm="6">
              <v-text-field
                v-model="idControl"
                :rules="[rules.required]"
                label="ID Controller*"
                type="number"
                disabled
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
                      {{activete ? 'mdi-flag' : 'mdi-flag-outline'}}
                    </v-icon>
                  </template>
                  </v-checkbox>
                </v-col>
              </v-row>
            </v-col>
          </v-row>

          <small class="text-caption text-medium-emphasis">*indicates required field</small>
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
              !idControl 
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
          <v-btn variant="text" @click="sheet = !sheet">close</v-btn>
          <br><br>
          <div>
            Hubo un error al editar el trayecto
            <br>
            Posibles errores: Posicion de la ruta ya usada
          </div>
        </v-card-text>
      </v-card>
    </v-bottom-sheet>
  </div>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";

export default {
  data: () => ({
    sheet: false,
    dialog: false,
    idRuta: "",
    idControl: "",
    activete: false,
    rules: {
      required: (value) => !!value || "Field is required",
    },
  }),

  computed: {
    ...mapState(["optionPoint"]),
  },

  mounted() {
    this.cargaDatos();
  },

  methods: {
    cargaDatos() {
      const optionRouter = JSON.parse(localStorage.getItem("optionPoint"));
      if (optionRouter) {
        this.idRuta = optionRouter.idRuta;
        this.idControl = optionRouter.idControl;
        this.activete = optionRouter.activete;
      } else {
        this.idRuta = "";
        this.idControl = "";
        this.activete = false;
      }
    },

    toggleActivete() {
      this.activete = !this.activete;
      localStorage.setItem("optionPoint", JSON.stringify({ idRuta: this.idRuta, idControl: this.idControl, posicion: this.posicion, activete: this.activete }));
    },

    closeDialogAndClearFields() {
      this.dialog = false;
    },

    saveDialogAndSubmitForm() {
      this.submitForm();
    },

    submitForm() {
      const update = {
        idRuta: this.idRuta,
        idControl: this.idControl,
        activete: this.activete,
      };

      axios
        .put("http://localhost:8080/backendIPC2/api/trayecto", update)
        .then((response) => {
          if (Object.keys(response.data).length > 0) {
            this.dialog = false;
          } else {
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
