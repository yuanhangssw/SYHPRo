// 应用全局配置
module.exports = {
  // baseUrl: "https://vue.ruoyi.vip/prod-api",
  // baseUrl: "https://192.168.1.129:8083",
  baseUrl: "https://hdfz.bdsrtk.cn:8083",
  devServer: {
    https: true, //开启https访问
  },

  // 应用信息
  appInfo: {
    // 应用名称
    name: "baiyi-app",
    // 应用版本
    version: "1.1.0",
    // 应用logo
    logo: "/static/logo.png",
    // 官方网站
    site_url: "http://ruoyi.vip",
    // 政策协议
    agreements: [
      {
        title: "隐私政策",
        url: "https://ruoyi.vip/protocol.html",
      },
      {
        title: "用户服务协议",
        url: "https://ruoyi.vip/protocol.html",
      },
    ],
  },
};
