import Vue from "vue";
import App from "./App";
import store from "./store"; // store
import plugins from "./plugins"; // plugins
import "./permission"; // permission
Vue.use(plugins);

Vue.config.productionTip = false;
Vue.prototype.$store = store;

App.mpType = "app";

// const url = "https://192.168.1.129:8083";
const url = "https://hdfz.bdsrtk.cn:8083";
Vue.prototype.$url = url;

const app = new Vue({
  ...App,
});

app.$mount();
