<template>
<div class="principal">
 
  <div class="contenedor_Boton">
    <button @click="login = 'recepcionista'">Recepcionista</button>
    <button @click="login = 'operador'">Operador</button>
    <button @click="login = 'admin'">Administrador</button>
    <component :is="login"></component>
 </div>

</div>
</template>

<script>

//importaciones para el login  
import admin from '../components/login/admin.vue'
import operador from '../components/login/operador.vue'
import recepcionista from '../components/login/recepcionista.vue'


export default {

data(){
  return{
    login : recepcionista
  }
},
components: {
  admin,
  operador,
  recepcionista
  }
}
</script>

<style scoped>

.principal {
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
  padding: 30px;
  box-sizing: border-box;
}

.title {
  text-align: center;
}

.trabajador{
  margin: 10px;
}
.cliente{
  margin: 10px;
}


.contenedor_Boton{
  text-align: center;
  margin: 0 auto;
  
}
button {
  margin: 100px;
  display: ruby-base-container;
  width: 25%;
  max-width: 100px;
  padding: 10px;
  background-color: #007BFF;
  color: white;
  border: none;
  border-radius: 20px;
  transition: all 0.3 ease 0s;
  cursor: pointer;
  margin: 10px 20px;
}

button:hover {
  background-color: #cde8ff;
  transform: scale(1.1);
  transition: transform 0.5s;
  color: black;
}
</style>
