import { createStore } from 'vuex'

export default createStore({
  state: {
    showFixedComponent: true,
  },
  getters: {
  },
  mutations: {
    toggleFixedComponent(state, value) {
      state.showFixedComponent = value;
      localStorage.setItem('showFixedComponent', value);
    },
  },
  actions: {
  },
  modules: {
  }
})
