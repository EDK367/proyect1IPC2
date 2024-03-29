<template>
  <nav class="pages">
    | <router-link to="/" class="link">Home</router-link> |
    <router-link to="/about" class="link">Login</router-link> |  
  </nav>
  <transition name="fade" mode="out-in">
  <router-view/>
</transition>
</template>

<script>

</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin: 0;
  padding: 0;
  overflow: hidden;
}

.pages {
  top: 0;
  left: 0;
  width: 100%;
  background-color: rgb(92, 161, 245); 
  padding: 20px;
  border-bottom: 1px solid white;
  z-index: 999;
}

.pages a {
  font-weight: bold;
  color: #2c3e50;
}

.pages a.router-link-exact-active {
  color: red;
}

.link{
  display: inline-table;
  color: red;
  text-decoration: none;
  border-radius: 30px;
  transition: all 0.3s ease;
}

.link:hover{
  color:rgb(248, 120, 120);
  transform: scale(1.20);
  transition: transform 0.5s;
  
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active in <2.1.8 */ {
  opacity: 0;
}
</style>
