<template>
  <view class="content content-box">
    <!-- <tj-add class="add-box"></tj-add> -->
    <view class="back-box" @click="back">
      <text class="back">＜</text>
      <text class="label">选择巡查单元</text>
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
          @click="toAddOrDetailPage(item)"
        >
          <view class="left"></view>
          <view class="right">
            <view class="name-and-count">
              <view class="name">{{ item.unitname }}</view>
              <view class="count">
                <image
                  src="../../../static/images/count.png"
                  alt=""
                  class="count-img"
                />
                <view>总数：{{ getCount(item) }}</view>
              </view>
            </view>
            <view class="fb-statistics">
              <view class="by-count">
                <image
                  src="../../../static/images/baiyi-small.png"
                  alt=""
                  class="count-img"
                />
                <view>白蚁：{{ item.type1total }}例</view>
              </view>

              <view class="by-count">
                <image
                  src="../../../static/images/hhs-small.png"
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
</template>

<script>
import { getUnits, getUnits2 } from "@/api/unit/index";
import storage from "@/utils/storage";
import constant from "@/utils/constant";
export default {
  data() {
    return {
      contentHeight: 0,
      queryQarams: {
        userid: storage.get(constant.userId),
        projectid: this.$store.state.user.projectId,
      },
      dataList: [],
    };
  },
  onLoad: function () {
    // this.getUnitList();
  },
  onShow: function () {
    this.queryQarams.projectid = this.$store.state.user.projectId;
    this.getUnitList();
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
              that.contentHeight = pageHeiht - 102;
            }
          })
          .exec();
      },
    });
  },
  created() {},
  methods: {
    getUnitList() {
      getUnits2(this.queryQarams).then((res) => {
        if (res.code === 200) {
          this.dataList = res.data;
        }
      });
    },
    // 计算总数
    getCount(item) {
      return item.type1total + item.type2total;
    },
    toAddOrDetailPage(item) {
      this.$tab.navigateTo(
        "/pages/common/addpatrol/AddORDetail?id=" +
          item.patrol_unit +
          "&type=add"
      );
    },
    // 返回
    back() {
      uni.navigateBack({
        delta: 1, // 返回的页面数，这里设置为1表示返回上一页
      });
    },
  },
};
</script>

<style lang="scss" scoped>
@import "../../../static/font/iconfont.css";
.content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  .add-box {
    position: absolute;
    bottom: 10rpx;
    right: 30rpx;
    z-index: 10;
  }
  .back-box {
    width: 100%;
    // margin-top: 88rpx;
    margin-top: 70rpx;
    margin-bottom: 30rpx;
    height: 88rpx;
    display: flex;
    // justify-content: left;
    align-items: center;
    padding-left: 30rpx;
    color: #333333;
    font-family: 苹方-简-常规体;
    font-size: 36rpx;
    letter-spacing: -0.7rpx;
    .back {
      font-size: 40rpx;
      margin-right: 6rpx;
    }
  }
}
.scroll-view {
  // position: relative;
  // margin-top: 50rpx;
  width: 100%;
  display: flex;
  justify-content: center;
  .list {
    display: flex;
    // flex-direction: column;
    // position: relative;
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

.title {
  font-size: 36rpx;
  color: #8f8f94;
}
</style>
