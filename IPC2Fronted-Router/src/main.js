import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import vuetify from '../vuetify-project/src/plugins/vuetify';
// Importación de Bootstrap
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';

// Crear la aplicación Vue y montarla
createApp(App)
  .use(store)
  .use(router)
  .use(vuetify) // Agregar Vuetify como un plugin
  .mount('#app');

