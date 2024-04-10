<template>
  <div>
    <v-img
      class="mx-auto my-6"
      max-width="100"
      :src= "require('../icons/3795330.png')"
    ></v-img>

    <v-card
      class="mx-auto pa-12 pb-8"
      elevation="8"
      max-width="448"
      rounded="lg"
    >
      <div class="text-subtitle-1 text-medium-emphasis">Administrador</div>

      <v-text-field
        v-model="adminID"
        :rules="[rules.required]"
        density="compact"
        type="number"
        placeholder="ID Admin"
        prepend-inner-icon="mdi-account-outline"
        variant="outlined"
      ></v-text-field>

      <div
        class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between"
      >
        Password
      </div>

      <v-text-field
        v-model="password"
        :rules="[rules.required]"
        :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
        :type="visible ? 'text' : 'password'"
        density="compact"
        placeholder="Enter your password"
        prepend-inner-icon="mdi-lock-outline"
        variant="outlined"
        @click:append-inner="visible = !visible"
      ></v-text-field>

      <v-btn
        class="buttonLogin"
        color="Black"
        size="large"
        variant="tonal"
        block
        @click="submitForm"
        :disabled="!adminID || !password"
      >
        Login
      </v-btn>
    </v-card>


        <v-bottom-sheet v-model="sheet">
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

          <div class="invalid">
            Los datos ingresado fueron incorrectos, por favor llenar de nuevo
            <br>
            O comunicarse con un administrador
          </div>
        </v-card-text>
      </v-card>
    </v-bottom-sheet>
  </div>
</template>

<script>
//se importa axios para el post
import axios from 'axios'
//importar el router
import router from '@/router'
//importar el vuex
import store from '@/store'

export default {
  data: () => ({
    sheet: false,
    visible: false,
    adminID: "",
    password: "",

    rules: {
      required: (value) => !!value || "Field is required",
    },
  }),
  
  methods: {
    submitForm() {
      // Formar el objeto JSON con los datos ingresados
      const dataLogin = {
        identificador: this.adminID,
        password: this.password,
      };
      // Mostrar el objeto JSON en la consola del navegador
      console.log("Datos del formulario:", dataLogin);
      const adminJson = JSON.stringify(dataLogin);
      //aca se envia el post
      axios
        .post("http://localhost:8080/backendIPC2/api/login", dataLogin)
        .then((response) => {
          
          if(Object.keys(response.data).length > 0){
            store.commit('toggleFixedComponent', false)            
            router.push('/admin')
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

<style scoped>
.buttonLogin {
  background-color: #437af9 !important;
  color: white;
  font-weight: bold;
  border-radius: 8px;
  transition: background-color 0.3s ease;
}

.buttonLogin::after {
  content: "" !important;
  width: 100%;
  height: 100%;
  background: radial-gradient(
    circle farthest-corner at 10% 20%,
    rgba(255, 94, 247, 1) 18%,
    rgba(2, 245, 255, 1) 100%
  );
  filter: blur(15px);
  position: absolute;
  left: 0;
  top: 0;
  z-index: 10;
  animation: move 2s linear infinite;
  background-size: 40% 200%;
}

.buttonLogin:hover {
  background-color: #134dfb !important;
  color: black;
}

@keyframes move {
  0% {
    background-position: 0 0;
  }
  100% {
    background-position: 200px 4400px;
  }
}
</style>
