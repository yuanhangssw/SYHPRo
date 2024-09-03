<script>
import config from "./config";
import store from "@/store";
import { getToken } from "@/utils/auth";
import storage from "@/utils/storage";
import constant from "@/utils/constant";

export default {
  onLaunch: function () {
    this.initApp();
  },
  methods: {
    // 初始化应用
    initApp() {
      // 初始化应用配置
      this.initConfig();
      // 检查用户登录状态
      //#ifdef H5
      this.checkLogin();
      //#endif
    },
    initConfig() {
      this.globalData.config = config;
    },
    checkLogin() {
      let phone = storage.get(constant.phone);

      if (!phone) {
        this.$tab.reLaunch("/pages/index1");
      } else {
        this.$tab.reLaunch("/pages/index");
      }
    },
  },
};
</script>

<style lang="scss">
@import "@/static/scss/index.scss";
</style>
