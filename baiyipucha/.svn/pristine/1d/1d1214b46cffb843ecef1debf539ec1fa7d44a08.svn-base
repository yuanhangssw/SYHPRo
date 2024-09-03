<template>
  <view class="content">
    <tj-add class="add-box"></tj-add>
    <scroll-view
      :scroll-y="true"
      class="scroll-view"
      :style="{ height: contentHeight + 'px' }"
    >
      <view
        style="
          display: flex;
          flex-direction: column;
          position: relative;
          align-items: center;
          justify-content: center;
        "
      >
        <image src="../static/images/bg-img.png" alt="" class="bg" />
        <view class="text-area">
          <!--项目选择-->
          <picker
            @change="change"
            :value="projectIndex"
            :range="projectList"
            class="paroect-picker"
          >
            <view class="uni-input">{{ projectList[projectIndex] }}</view>
          </picker>
          <!--轮播-->
          <view class="uni-margin-wrap">
            <swiper
              class="swiper"
              circular
              :indicator-dots="indicatorDots"
              :autoplay="autoplay"
              :interval="interval"
              :duration="duration"
            >
              <swiper-item v-for="(item, index) in infoList" :key="index">
                <view class="swiper-item swiper-bg">
                  <view class="info">{{ item.info }}</view>
                </view>
              </swiper-item>
            </swiper>
          </view>
          <!--信息统计-->
          <view class="count-box">
            <view class="baiyi-box">
              <view class="left-box">
                <text class="text1">白蚁</text>
                <view>
                  <text class="text2">{{ baiyiCount }}</text>
                  <text class="text3">例</text>
                </view>
              </view>
              <image
                src="../static/images/baiyi-logo.png"
                alt=""
                class="logo"
              />
            </view>
            <view class="ghs-box">
              <view class="left-box">
                <text class="text1">獾狐鼠等</text>
                <view>
                  <text class="text2">{{ hhsCount }}</text>
                  <text class="text3">例</text>
                </view>
              </view>
              <image src="../static/images/hhs-logo.png" alt="" class="logo" />
            </view>
          </view>
          <!--单元-->
          <view class="unit-box">
            <view class="unit">
              <view class="unit-bg">
                <view class="unit-label">全部单元数</view>
                <view class="unit-count">
                  <view
                    v-for="(item, index) in unittotal"
                    :key="index"
                    class="count-label"
                    >{{ item }}</view
                  >
                </view>
              </view>
              <view class="unit-bg unit-bg-1">
                <view class="unit-label">巡查数量</view>
                <view class="unit-count">
                  <view
                    v-for="(item, index) in datatotal"
                    :key="index"
                    class="count-label"
                    >{{ item }}</view
                  >
                </view>
              </view>
            </view>
            <view class="unit">
              <view class="unit-bg">
                <view class="unit-label">审批通过数</view>
                <view class="unit-count">
                  <view
                    v-for="(item, index) in passtotal"
                    :key="index"
                    class="count-label"
                    >{{ item }}</view
                  >
                </view>
              </view>
              <view class="unit-bg unit-bg-1">
                <view class="unit-label">审批未通过数</view>
                <view class="unit-count">
                  <view
                    v-for="(item, index) in backtotal"
                    :key="index"
                    class="count-label"
                    >{{ item }}</view
                  >
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import { getProjects, getHomeInfo } from "@/api/home/index";
import { mapGetters } from "vuex";
import storage from "@/utils/storage";
import constant from "@/utils/constant";
export default {
  data() {
    return {
      contentHeight: 0,
      queryParams: {
        // userId: this.$store.state.user.userId,
        userId: storage.get(constant.userId),
        projectId: null,
      },
      projectList: [],
      projectIdList: [],
      projectIndex: 0,

      infoList: [
        {
          info: "",
        },
        {
          info: "",
        },
        {
          info: "",
        },
        {
          info: "",
        },
      ],
      indicatorDots: true,
      autoplay: true,
      interval: 2000,
      duration: 500,
      unittotal: [],
      datatotal: [],
      passtotal: [],
      backtotal: [],
      baiyiCount: 0,
      hhsCount: 0,
      dataStr: [0, 0, 0],
      // userId: this.$store.state.user.userId,
      userId: storage.get(constant.userId),
    };
  },
  computed: {
    ...mapGetters(["isAddNewPatrol"]),
  },
  watch: {
    isAddNewPatrol: {
      deep: true,
      handler() {
        this.getInfo();
      },
    },
  },
  // onLoad: function () {
  //   this.getProjectList();
  // },
  onShow: function () {
    this.getProjectList();
  },
  onReady() {
    let that = this;
    uni.getSystemInfo({
      //调用uni-app接口获取屏幕高度
      success(res) {
        //成功回调函数
        const pageHeiht = res.windowHeight; //windowHeight为窗口高度
        let taskListDom = uni.createSelectorQuery().select(".scroll-view"); //想要获取高度的元素名（class/id）
        taskListDom
          .boundingClientRect((data) => {
            if (data) {
              that.contentHeight = pageHeiht;
            }
          })
          .exec();
      },
    });
  },
  // 分享给朋友
  onShareAppMessage() {
    return {
      title: "害堤动物防治",
      // imageUrl: '分享图片链接',
      path: "pages/index1",
      success() {
        console.log("分享成功");
      },
      fail(err) {
        console.error("分享失败", err);
      },
    };
  },
  methods: {
    // 获取项目列表
    getProjectList() {
      let that = this;
      getProjects(this.userId).then((res) => {
        that.projectList = [];
        that.projectIdList = [];
        if (res.length > 0) {
          for (let i = 0; i < res.length; i++) {
            that.projectList.push(res[i].projectName);
            that.projectIdList.push(res[i].id);
          }
        } else {
          that.projectList.push("");
          that.projectIdList.push(-1);
        }

        let pId = uni.getStorageSync("projectId");
        if (pId) {
          that.projectIdList.forEach((id, index) => {
            if (id == pId) {
              that.projectIndex = index;
              that.queryParams.projectId = that.projectIdList[index];
              that.$store.state.user.projectId = that.projectIdList[index];
              that.$store.state.user.projectName = that.projectList[index];
            }
          });
        } else {
          that.queryParams.projectId = that.projectIdList[0];
          that.$store.state.user.projectId = that.projectIdList[0];
          uni.setStorageSync("projectId", that.projectIdList[0]);
          that.$store.state.user.projectName = that.projectList[0];
        }
        this.getInfo();
      });
    },
    // 选择项目
    change(e) {
      this.projectIndex = e.detail.value;
      let id = this.projectIdList[e.detail.value];
      this.$store.state.user.projectId = id;
      uni.setStorageSync("projectId", id);
      this.queryParams.projectId = id;
      this.$store.state.user.projectName = this.projectList[e.detail.value];

      this.getInfo();
    },

    // 获取统计信息
    getInfo() {
      let that = this;
      getHomeInfo(this.queryParams).then((res) => {
        if (res.code === 200) {
          if (res.data) {
            that.baiyiCount = res.data.type1;
            that.hhsCount = res.data.type2;
            that.unittotal = that.formateData(res.data.unittotal);
            that.datatotal = that.formateData(res.data.datatotal);
            that.passtotal = that.formateData(res.data.passtotal);
            that.backtotal = that.formateData(res.data.backtotal);
          }
        }
        this.$store.commit("SET_ISNEWADDPATROL", false);
      });
    },
    formateData(count) {
      let list = [];
      if (count > 0) {
        let countStr = count.toString();
        list = Array.from(countStr);
      } else {
        list = this.dataStr;
      }
      if (list.length === 1) {
        list.unshift(0);
        list.unshift(0);
      } else if (list.length === 2) {
        list.unshift(0);
      }
      return list;
    },
  },
};
</script>

<style lang="scss" scoped>
// @import "../static/font/iconfont.css";
.content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: rgba(240, 240, 240, 1);
  position: relative;
  .add-box {
    position: absolute;
    bottom: 10rpx;
    right: 30rpx;
    z-index: 10;
  }
}
.scroll-view {
  position: relative;
}

.bg {
  width: 100%;
  margin-left: auto;
  margin-right: auto;
}

.text-area {
  position: absolute;
  width: 92%;
  top: 35%;
  margin-bottom: 40rpx;
  display: flex;
  justify-content: center;
  flex-flow: column;
  .uni-margin-wrap {
    width: 100%;
  }
  .paroect-picker {
    color: #ffffff;
    font-size: 50rpx;
    letter-spacing: -0.7px;
    height: 66rpx;
  }
}
.swiper {
  margin-top: 31rpx;
  height: 280rpx;
}
.swiper-item {
  height: 100%;
  padding: 25rpx 20rpx 0 206rpx;
}
.info {
  font-size: 28rpx;
  letter-spacing: 1rpx;
  color: #ffffff;
  text-align: end;
}
.count-box {
  display: flex;
  margin-top: 40rpx;
}
.baiyi-box,
.ghs-box {
  width: 49%;
  height: 160rpx;
  background: url("../static/images/baiyi-bg.png");
  background-size: 100% 100%;
  display: flex;
  padding: 36rpx 16rpx 0;
  justify-content: space-between;
}
.ghs-box {
  margin-left: 2%;
  background: url("../static/images/hhs-bg.png");
  background-size: 100% 100%;
}
.left-box {
  display: flex;
  flex-flow: column;
}
.text1,
.text2,
.text3 {
  color: rgba(255, 255, 255, 1);
  font-size: 32rpx;
  height: 45rpx;
  line-height: 45rpx;
}
.text3 {
  font-size: 20rpx;
}
.logo {
  width: 98rpx;
  height: 98rpx;
}
.unit-box {
  display: flex;
  flex-flow: column;
  margin-top: 50rpx;
}
.unit {
  display: flex;
  margin-bottom: 40rpx;
}
.unit-bg {
  width: 49%;
  height: 260rpx;
  background-image: url("../static/images/unit-bg.png");
  background-size: 100% 100%;
  padding-top: 36rpx;
  display: flex;
  flex-flow: column;
  text-align: center;
}
.unit-bg-1 {
  margin-left: 2%;
}
.unit-label {
  mix-blend-mode: normal;
  color: rgba(255, 255, 255, 1);
  font-size: 36rpx;
}
.unit-count {
  display: flex;
  justify-content: center;
  margin-top: 42rpx;
  height: 98rpx;
}
.count-label {
  height: 100%;
  width: 72rpx;
  border-radius: 10px;
  background-color: #ffffff;
  color: rgba(62, 141, 246, 1);
  font-size: 72rpx;
  margin-left: 10rpx;
  margin-right: 10rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}
::v-deep .uni-select {
  width: 50%;
  height: 84rpx;
  color: rgba(255, 255, 255, 1);
  border: none;
  .uni-select__selector-item {
    height: 100%;
    font-size: 60rpx;
  }
}
::v-deep .uni-select__input-text {
  color: #fff;
  font-size: 50rpx;
}

::v-deep .uni-select__selector {
  background: transparent;
  border: none;
  box-shadow: none;
  .uni-select__selector-item {
    font-size: 40rpx;
    height: 80rpx;
  }
}

::v-deep .uniui-bottom,
::v-deep .uniui-top {
  color: #ffffff !important;
}

::v-deep .uni-select__selector-item {
  font-size: 50rpx;
}

::v-deep .uni-popper__arrow {
  display: none;
}

.title {
  font-size: 36rpx;
  color: #8f8f94;
}
</style>
