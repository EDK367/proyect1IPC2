<template>
<div class="principal">
  <nav class="navbar">
    <form class="container-fluid justify-content-start">
      <button class="btn btn-sm btn-outline-secondary" type="button" @click="table = 'tableAdmin'">Administradores</button>
      <button class="btn btn-sm btn-outline-secondary" type="button" @click="table = 'tableOperador'">Operadores</button>
      <button class="btn btn-sm btn-outline-secondary" type="button" @click="table = 'tableRecepcion'">Recepcionistas</button>
    </form>
  </nav>
  <component :is="table" :key="login"></component>
</div>
</template>

<script>
import tableAdmin from '../components/administrador/tableAdm.vue'
import tableOperador from '../components/administrador/tableOper.vue'
import tableRecepcion from '../components/administrador/tableRece.vue'

export default {
  data() {
    return {
      table: tableAdmin
    }
  },
  components: {
    tableAdmin,
    tableOperador,
    tableRecepcion
  }
}
</script>

<style>


</style>
