<template>
  <div class="operador">
    <form @submit.prevent="submitForm">
      <h3>Operador</h3>
      <label for="user">Usuario/CUI: </label><br>
      <input id="user" v-model="form.user" type="number" required><br>

      <label for="password">Contraseña</label><br>
      <input id="password" v-model="form.password" :type="passwordFieldType" name="password" required>
      <br>
      <input type="checkbox" id="show" @click="togglePasswordVisibility">
      Mostrar Contraseña
      <br><br>
       <nav class="button" :class="{ 'disabled': !isFormValid }">
         <router-link v-if="isFormValid" :to="{ path: '/operador' }" class="link">
           Iniciar Sesion
         </router-link>
         <span v-else class="link disabled">
           Iniciar Sesion
         </span>
       </nav>
       <router-view/>
    </form>

  </div>
  

</template>

<script>

export default {

    
  data() {
    return {
      form: {
        user: '',
        password: ''
      },
      passwordFieldType: 'password' 
    }
  },
  methods: {
    submitForm() {
      console.log("Operador", this.form);
      
    },
    togglePasswordVisibility() {
      this.passwordFieldType = this.passwordFieldType === 'password' ? 'text' : 'password';
    }
  },
  computed:{
    isFormValid(){
      return this.form.user !== '' && this.form.password !== '';
    }
  }
}
</script>

<style scoped>

.operador {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: -40vh;
  margin: 0 auto;

}

form {
  display: flex;
  flex-direction: column;
  width: 275px;
  padding: 20px;
  border-radius: 30px;
  background-color: rgb(6, 141, 237);
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
}

label {
  margin-bottom: 10px;
}

input[type="number"],
input[type="password"] {
  margin-bottom: 15px;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
}


.button {
  display: flex;
  justify-content: center;
}

.button .link {
  text-decoration: none;
  padding: 10px 20px;
  background-color: #007BFF;
  color: white;
  border-radius: 20px;
  transition: all 0.3s ease 0s;
  cursor: pointer;
}

.button .link:hover {
  background-color: #cde8ff;
  transform: scale(1.1);
  transition: transform 0.5s;
  color: black;
}

/* Deshabilitar el enlace cuando el formulario no está lleno */
.button.disabled .link {
  background-color: #ccc;
  cursor: not-allowed;
}





</style>
