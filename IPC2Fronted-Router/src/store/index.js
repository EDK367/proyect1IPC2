import { createStore } from "vuex";

export default createStore({
  state: {
    showFixedComponent: true,
    loginData: JSON.parse(localStorage.getItem("loginData")) || null,
    optionUser: JSON.parse(localStorage.getItem("optionUser")) || null,
    optionPoint: JSON.parse(localStorage.getItem("optionPoint")) || null,
  },
  getters: {},
  mutations: {
    setLoginData(state, data) {
      state.loginData = data;
      localStorage.setItem("loginData", JSON.stringify(data));
    },
    setoptionUser(state, data) {
      state.optionUser = data;
      localStorage.setItem("optionUser", JSON.stringify(data));
    },
    setoptionPoint(state, data) {
      state.optionPoint = data;
      localStorage.setItem("optionPoint", JSON.stringify(data));
    },
    toggleFixedComponent(state, value) {
      state.showFixedComponent = value;
      localStorage.setItem("showFixedComponent", value);
    },
  },
  actions: {
    saveLoginData({ commit }, data) {
      commit("setLoginData", data);
    },

    saveOptionUser({ commit }, data) {
      commit("optionUser", data);
    },
    saveOptionPoint({ commit }, data) {
      commit("optionPoint", data);
    },
  },
  modules: {},
});
