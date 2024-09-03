<template>
  <view
    class="mine-container content-box"
    :style="{ height: `${windowHeight}px` }"
  >
    <!--顶部个人信息栏-->
    <view class="header-section">
      <view class="flex padding justify-between">
        <view class="flex align-center">
          <!-- <view v-if="!avatar" class="cu-avatar xl round bg-white"> -->
          <!-- <view class="iconfont icon-people text-gray icon"></view> -->

          <!-- <view class="user-avatar"></view> -->
          <!-- </view> -->
          <image
            v-if="!avatar"
            src="../../static/images/mine/profile.png"
            class="user-avatar"
          ></image>
          <image
            v-if="avatar"
            @click="handleToAvatar"
            :src="avatar"
            class="cu-avatar xl round"
            mode="widthFix"
          >
          </image>
          <view v-if="!name" @click="handleToLogin" class="login-tip">
            点击登录
          </view>
          <!-- @click="handleToInfo" -->
          <view v-if="name" class="user-info">
            <view class="info">
              <view class="u_title"> {{ name }} </view>
              <!-- <view class="role">角色</view> -->
            </view>
            <view class="tel">{{ phone }}</view>
          </view>
        </view>
      </view>
    </view>
    <view class="btn-box">
      <button type="primary" class="tj-btn" @click="handleLogout">
        退出登录
      </button>
    </view>
    <!-- <view class="cu-list menu">
      <view class="cu-item item-box">
        <view class="content text-center" @click="handleLogout">
          <text class="text-black">退出登录</text>
        </view>
        
      </view>
    </view> -->

    <!-- <view class="menu-list">
      <view class="list-cell list-cell-arrow" @click="handleToEditInfo">
        <view class="menu-item-box">
          <view class="iconfont icon-user menu-icon"></view>
          <view>编辑资料</view>
        </view>
      </view>
      <view class="list-cell list-cell-arrow" @click="handleHelp">
        <view class="menu-item-box">
          <view class="iconfont icon-help menu-icon"></view>
          <view>常见问题</view>
        </view>
      </view>
      <view class="list-cell list-cell-arrow" @click="handleAbout">
        <view class="menu-item-box">
          <view class="iconfont icon-aixin menu-icon"></view>
          <view>关于我们</view>
        </view>
      </view>
      <view class="list-cell list-cell-arrow" @click="handleToSetting">
        <view class="menu-item-box">
          <view class="iconfont icon-setting menu-icon"></view>
          <view>应用设置</view>
        </view>
      </view>
    </view> -->
  </view>
</template>

<script>
import storage from "@/utils/storage";
import constant from "@/utils/constant";

export default {
  data() {
    return {
      name: this.$store.state.user.name,
      phone: storage.get(constant.phone),
      version: getApp().globalData.config.appInfo.version,
    };
  },
  computed: {
    avatar() {
      return this.$store.state.user.avatar;
    },
    windowHeight() {
      return uni.getSystemInfoSync().windowHeight - 50;
    },
  },
  methods: {
    handleToInfo() {
      this.$tab.navigateTo("/pages/mine/info/index");
    },
    handleToEditInfo() {
      this.$tab.navigateTo("/pages/mine/info/edit");
    },
    handleToSetting() {
      this.$tab.navigateTo("/pages/mine/setting/index");
    },
    handleToLogin() {
      this.$tab.reLaunch("/pages/login");
    },
    handleToAvatar() {
      this.$tab.navigateTo("/pages/mine/avatar/index");
    },
    handleLogout() {
      storage.remove(constant.userId);
      storage.remove(constant.phone);
      uni.removeStorageSync("projectId");
      this.$tab.reLaunch("/pages/login");
    },
    handleHelp() {
      this.$tab.navigateTo("/pages/mine/help/index");
    },
    handleAbout() {
      this.$tab.navigateTo("/pages/mine/about/index");
    },
  },
};
</script>

<style lang="scss">
.mine-container {
  width: 100%;
  height: 100%;
  color: #333333;

  .header-section {
    padding-top: 24%;
    padding-left: 30rpx;
    padding-bottom: 80rpx;
    color: white;

    .user-avatar {
      width: 110rpx;
      height: 110rpx;
    }
    .login-tip {
      font-size: 18px;
      margin-left: 10px;
    }

    .cu-avatar {
      border: 2px solid #eaeaea;

      .icon {
        font-size: 55px;
      }
    }

    .user-info {
      margin-left: 15px;
      display: flex;
      flex-flow: column;
      .info {
        display: flex;
        .u_title {
          color: #333333;
          font-family: 苹方-简-常规体;
          font-size: 32rpx;
          height: 45rpx;
          line-height: 45rpx;
        }
        .role {
          height: 43px;
          mix-blend-mode: normal;
          border-radius: 23rpx;
          background: #575be3;
        }
      }
      .tel {
        height: 45rpx;
        mix-blend-mode: normal;
        color: #555555;
        font-family: 苹方-简-常规体;
        font-size: 28rpx;
      }
    }
  }

  .content-section {
    position: relative;
    top: -50px;

    .mine-actions {
      margin: 15px 15px;
      padding: 20px 0px;
      border-radius: 8px;
      background-color: white;

      .action-item {
        .icon {
          font-size: 28px;
        }

        .text {
          display: block;
          font-size: 13px;
          margin: 8px 0px;
        }
      }
    }
  }
}
.btn-box {
  margin-top: 50rpx;
  padding: 0 56rpx;
  .tj-btn {
    height: 80rpx;
    mix-blend-mode: normal;
    border-radius: 50rpx;
    background: #3e8df6;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #ffffff;
    font-family: 苹方-简-中粗体;
    font-size: 28rpx;
  }
}
</style>
