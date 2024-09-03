import config from "@/config";
import storage from "@/utils/storage";
import constant from "@/utils/constant";
import { login, getInfo } from "@/api/login";
import { getToken, setToken, removeToken } from "@/utils/auth";
import { stat } from "fs";

const baseUrl = config.baseUrl;

const user = {
  state: {
    token: getToken(),
    name: storage.get(constant.name),
    avatar: storage.get(constant.avatar),
    roles: storage.get(constant.roles),
    permissions: storage.get(constant.permissions),
    projectList: [],
    projectId: storage.get("projectId"),
    projectName: "",
    userId: storage.get(constant.userId),
    phone: storage.get(constant.phone),
    isAddNewPatrol: false,
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token;
    },
    SET_NAME: (state, name) => {
      state.name = name;
      storage.set(constant.name, name);
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar;
      storage.set(constant.avatar, avatar);
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles;
      storage.set(constant.roles, roles);
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions;
      storage.set(constant.permissions, permissions);
    },
    SET_USERID: (state, userId) => {
      state.userId = userId;
      storage.set(constant.userId, userId);
    },
    SET_PHONE: (state, phone) => {
      state.phone = phone;
      storage.set(constant.phone, phone);
    },
    SET_PROJECTNAME: (state, projectName) => {
      state.projectName = projectName;
    },
    SET_ISNEWADDPATROL: (state, isAddNewPatrol) => {
      state.isAddNewPatrol = isAddNewPatrol;
    },
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username;
      const password = userInfo.password;
      return new Promise((resolve, reject) => {
        login(username, password)
          .then((res) => {
            commit("SET_USERID", res.data.id);
            commit("SET_NAME", res.data.inspectorName);
            commit("SET_PHONE", res.data.phone);
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo()
          .then((res) => {
            const user = res.user;
            const avatar =
              user == null || user.avatar == "" || user.avatar == null
                ? require("@/static/images/mine/profile.png")
                : baseUrl + user.avatar;
            const username =
              user == null || user.userName == "" || user.userName == null
                ? ""
                : user.userName;
            if (res.roles && res.roles.length > 0) {
              commit("SET_ROLES", res.roles);
              commit("SET_PERMISSIONS", res.permissions);
            } else {
              commit("SET_ROLES", ["ROLE_DEFAULT"]);
            }
            commit("SET_NAME", username);
            commit("SET_AVATAR", avatar);
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
  },
};

export default user;
