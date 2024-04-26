<template>
  <h1 class="title">Datos del User</h1>
<div class="container">
  <table>
    <tr v-for="(value, key) in loginData" :key="key">
        <td>{{ key }}</td>
        <td>{{value}}</td>
    </tr>
  </table>
</div>

</template>

<script>
export default {
data(){
  return {
  }
},

  computed: {
    loginData() {
      return this.$store.state.loginData;
      
    },
    formato(){
      const info = JSON.stringify(this.loginData, null, 2);
      return info;
    }
  },

  mounted() {
    // Llama al método para imprimir loginData cuando el componente se inicia
    this.printLoginData();
  },

  methods: {
    printLoginData() {
      // Imprime loginData en la consola
      console.log('Datos de inicio de sesión:', this.loginData);
    },
  },
};
</script>



<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50vh;
}

.title {
  font-size: 36px;
  margin-bottom: 20px;
  color:rgb(53, 53, 231);
}

.table-container {
  border: 2px solid #ddd;
  border-radius: 10px;
  overflow: auto;
  max-width: 200%;
}

.table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  border: 1px solid #ddd;
  padding: 15px;
  text-align: left;
}

th {
  background-color: #f2f2f2;
  font-weight: bold;
}

</style>