<template>
  <v-card class="scroll" max-height="500">
    <v-banner class="justify-center text-h5 font-weight-light" sticky>
      <span class="font-weight-bold" v-text="scrollInvoked"></span>
    </v-banner>


    </v-card>
</template>

<script>
export default {
  data: () => ({
    scrollInvoked: null,
  }),
};
</script>



<style scoped>
.scroll {
  overflow-y: 0;
}
</style>
