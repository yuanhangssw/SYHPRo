<template>
  <view class="content">
    <tj-add class="add-box"></tj-add>
    <view class="box">
      <view class="image-box">
        <image src="../../static/images/bg-img.png" alt="" class="bg" />
      </view>

      <view class="text-area">
        <!--项目选择名称-->
        <text class="project-name">{{ projectName }}</text>

        <!--信息统计-->
        <view class="count-box">
          <view class="baiyi-box" @click="addUnit(1)">
            <view class="left-box">
              <text class="text1">白蚁等</text>
              <view>
                <text class="text2">危害及防治</text>
              </view>
            </view>
            <image
              src="../../static/images/baiyi-logo.png"
              alt=""
              class="logo"
            />
          </view>
          <view class="ghs-box" @click="addUnit(2)">
            <view class="left-box">
              <text class="text1">獾狐鼠等</text>
              <view>
                <text class="text2">危害及防治</text>
              </view>
            </view>
            <image src="../../static/images/hhs-logo.png" alt="" class="logo" />
          </view>
        </view>
      </view>
      <scroll-view
        :scroll-y="true"
        class="scroll-view"
        :style="{ height: contentHeight + 'px' }"
      >
        <!-- <view > -->
        <view v-if="dataList.length > 0" class="list">
          <view
            v-for="(item, index) in dataList"
            :key="index"
            class="project-box"
            @click="toUnitDetail(item)"
          >
            <view class="left"></view>
            <view class="right">
              <view class="name-and-count">
                <view class="name">{{ item.unitname }}</view>
                <view class="count">
                  <image
                    src="../../static/images/count.png"
                    alt=""
                    class="count-img"
                  />
                  <view>总数：{{ getCount(item) }}</view>
                </view>
              </view>
              <view class="fb-statistics">
                <view class="by-count">
                  <image
                    src="../../static/images/baiyi-small.png"
                    alt=""
                    class="count-img"
                  />
                  <view>白蚁：{{ item.type1total }}例</view>
                </view>

                <view class="by-count">
                  <image
                    src="../../static/images/hhs-small.png"
                    alt=""
                    class="count-img"
                  />
                  <view>獾狐鼠：{{ item.type2total }}例</view>
                </view>
              </view>
            </view>
          </view>
        </view>
        <tj-empty v-else></tj-empty>
        <!-- </view> -->
      </scroll-view>
    </view>
  </view>
</template>

<script>
import { getUnits } from "@/api/unit/index";
import storage from "@/utils/storage";
import constant from "@/utils/constant";
export default {
  data() {
    return {
      contentHeight: 0,
      queryQarams: {
        userId: storage.get(constant.userId),
        projectId: this.$store.state.user.projectId,
      },
      projectName: this.$store.state.user.projectName,
      dataList: [],
    };
  },
  onLoad: function () {
    // this.getUnitList();
  },
  onShow: function () {
    this.queryQarams.projectId = this.$store.state.user.projectId;
    this.projectName = this.$store.state.user.projectName;
    this.getUnitList();
  },
  onReady() {
    let that = this;
    uni.getSystemInfo({
      //调用uni-app接口获取屏幕高度
      success(res) {
        //成功回调函数
        const pageHeiht = res.windowHeight; //windowHeight为窗口高度
        let imageBoxDom = uni.createSelectorQuery().select(".image-box"); //想要获取高度的元素名（class/id）
        imageBoxDom
          .boundingClientRect((data) => {
            if (data) {
              // that.contentHeight = pageHeiht - 250;
              that.contentHeight = pageHeiht - data.height;
            }
          })
          .exec();
      },
    });
  },
  created() {},
  methods: {
    getUnitList() {
      getUnits(this.queryQarams).then((res) => {
        if (res.code === 200) {
          this.dataList = res.data;
        }
      });
    },
    // 计算总数
    getCount(item) {
      return item.type1total + item.type2total;
    },
    toUnitDetail(item) {
      this.$tab.navigateTo(
        "/pages/project/unitdetail/index?id=" + item.patrol_unit
      );
    },
    // 添加单元

    addUnit(type) {
      this.$tab.navigateTo("/pages/project/addOrupdate/index?type=" + type);
    },
  },
};
</script>

<style lang="scss" scoped>
// @import "../../static/font/iconfont.css";
page {
  height: 100%;
}

.content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: rgba(240, 240, 240, 1);
  position: relative;
  height: 100%;
  .add-box {
    position: absolute;
    bottom: 10rpx;
    right: 30rpx;
    z-index: 10;
  }
  .box {
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background: rgba(240, 240, 240, 1);
    .image-box {
      width: 100%;
    }
  }
}
.scroll-view {
  width: 100%;
  display: flex;
  justify-content: center;
  .list {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    display: flex;
    flex-flow: column;
    .project-box {
      width: 92%;
      height: 160rpx;
      mix-blend-mode: normal;
      border-radius: 15rpx;
      background: #ffffff;
      margin-bottom: 30rpx;
      display: flex;
      .left {
        width: 15rpx;
        height: 100%;
        border-radius: 15rpx 0 0 15rpx;
        mix-blend-mode: normal;
        background: linear-gradient(160.05deg, #0385ff 8.68%, #80c1ff 91.71%);
      }
      .right {
        flex: 1;
        height: 100%;
        padding: 28rpx 30rpx 0 30rpx;
        display: flex;
        flex-flow: column;
        .name-and-count {
          display: flex;
          justify-content: space-between;
          .name {
            mix-blend-mode: normal;
            color: #333333;
            font-family: 苹方-简-中粗体;
            font-size: 30rpx;
          }
          .count {
            mix-blend-mode: normal;
            color: #555555;
            font-family: 苹方-简-常规体;
            font-size: 28rpx;
            display: flex;
            line-height: 40rpx;
            .count-img {
              width: 36rpx;
              height: 36rpx;
              margin-right: 4rpx;
            }
          }
        }
        .fb-statistics {
          margin-top: 30rpx;
          display: flex;
          justify-content: space-between;
          padding: 0 100rpx;
          .by-count {
            display: flex;
            mix-blend-mode: normal;
            color: #555555;
            font-family: 苹方-简-常规体;
            font-size: 28rpx;
            .count-img {
              width: 36rpx;
              height: 36rpx;
              margin-right: 4rpx;
            }
          }
        }
      }
    }
  }
}

.bg {
  height: 400rpx;
  width: 100%;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 100rpx;
}

.text-area {
  position: absolute;
  width: 92%;
  // top: 11%;
  top: 190rpx;
  // margin-bottom: 40rpx;
  display: flex;
  justify-content: center;
  flex-flow: column;
  .project-name {
    font-size: 50rpx;
    color: #ffffff;
    font-family: 苹方-简-中黑体;
    letter-spacing: -0.7px;
    height: 66rpx;
  }
}

.count-box {
  display: flex;
  margin-top: 40rpx;
  /* flex-flow: column; */
}
.baiyi-box,
.ghs-box {
  width: 49%;
  height: 160rpx;
  background: url("../../static/images/baiyi-bg.png");
  background-size: 100% 100%;
  display: flex;
  padding: 36rpx 16rpx 0;
  justify-content: space-between;
}
.ghs-box {
  margin-left: 2%;
  background: url("../../static/images/hhs-bg.png");
  background-size: 100% 100%;
}
.left-box {
  display: flex;
  flex-flow: column;
}
.text1,
.text2,
.text3 {
  font-size: 32rpx;
  height: 45rpx;
  line-height: 45rpx;
  mix-blend-mode: normal;
  color: #ffffff;
  font-family: 苹方-简-中粗体;
  letter-spacing: 1px;
}
.text3 {
  font-size: 20rpx;
}
.logo {
  width: 98rpx;
  height: 98rpx;
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
    font-size: 50rpx;
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
