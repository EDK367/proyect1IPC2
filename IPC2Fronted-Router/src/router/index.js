import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import inicioAdmin from '../views/inicioAdmin.vue'
import inicioOperador from '../views/inicioOperador.vue'
import inicioRecepcion from '../views/inicioRecepcion.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/admin',
    name: 'admin',

    component: () => import('../views/inicioAdmin.vue')
  },
  {
  path: '/operador',
  name: 'operador',

  component: () => import('../views/inicioOperador.vue')
  },
  {
    path: '/recepcion',
    name: 'recepcion',

    component: () => import('../views/inicioRecepcion.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
