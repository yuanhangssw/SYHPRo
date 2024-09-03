<template>
  <view class="content content-box">
    <view class="back-box" @click="back">
      <text class="back">＜</text>
      <text class="label">巡查记录</text>
    </view>
    <scroll-view
      :scroll-y="true"
      class="scroll-view"
      :style="{ height: contentHeight + 'px' }"
      @scrolltolower="load"
    >
      <!-- <view > -->
      <view v-if="dataList.length > 0" class="list">
        <view
          v-for="(item, index) in dataList"
          :key="index"
          class="project-box"
        >
          <view v-if="item.status === 1" class="approval-status">已通过</view>
          <view
            v-if="item.status === 2"
            class="approval-status approval-status-fail"
            >未通过</view
          >
          <view class="detail">
            <view class="left"></view>
            <view class="info-box">
              <view class="name-and-status">
                <view class="type">
                  <image
                    :src="
                      item.patrolType === 1
                        ? '../../../static/images/baiyi-small.png'
                        : '../../../static/images/hhs-small.png'
                    "
                    class="logo"
                  ></image>
                  <view
                    :class="item.patrolType === 1 ? 'type-name' : 'type-name-1'"
                    >{{ item.patrolType === 1 ? "白蚁" : "獾狐鼠" }}</view
                  >
                </view>
                <view class="name">{{ item.unitName }}</view>
              </view>
              <view class="note">
                <view class="describe-box">
                  <text class="label">描述：</text>
                  <text class="describe">{{
                    item.description ? item.description : ""
                  }}</text>
                </view>
                <view class="describe-box">
                  <text class="label">类型：</text>
                  <text class="describe">{{
                    item.patrolType === 1 ? "白蚁" : "獾狐鼠"
                  }}</text>
                </view>
                <view class="describe-box">
                  <text class="label">位置：</text>
                  <text class="describe">{{
                    item.address ? item.address : ""
                  }}</text>
                </view>
              </view>
              <view class="bottom-box">
                <view class="time">{{
                  item.patrolTime ? item.patrolTime : ""
                }}</view>
                <button
                  class="mini-btn tj-btn"
                  type="primary"
                  size="mini"
                  @click="handleDetail(item)"
                >
                  查看
                </button>
              </view>
            </view>
          </view>
        </view>
        <div class="no-more flex-1 flex-h jc-c" v-if="dataList.length >= total">
          没有更多了
        </div>
      </view>
      <tj-empty v-else></tj-empty>
      <!-- </view> -->
    </scroll-view>
  </view>
</template>

<script>
import { getUnitInfo } from "@/api/unit/index";
import storage from "@/utils/storage";
import constant from "@/utils/constant";
export default {
  data() {
    return {
      contentHeight: 0,
      queryQarams: {
        // userId: storage.get(constant.userId),
        // projectId: this.$store.state.user.projectId,
        // unitId: "",
        inspectorId: storage.get(constant.userId),
        projectId: this.$store.state.user.projectId,
        patrolUnit: "",
        pageSize: 10,
        pageNum: 1,
      },
      dataList: [],
      total: 0,
    };
  },
  onLoad: function (option) {
    //option为object类型，会序列化上个页面传递的参数
    // this.queryQarams.unitId = option.id;
    this.queryQarams.patrolUnit = option.id;
    this.queryQarams.projectId = this.$store.state.user.projectId;
    this.queryQarams.pageNum = 1;
    this.getUnitDetail();
  },
  onShow: function () {
    // this.queryQarams.projectId = this.$store.state.user.projectId;
    // this.queryQarams.pageNum = 1;
    // this.getUnitDetail();
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
    getUnitDetail(page) {
      getUnitInfo(this.queryQarams).then((res) => {
        // this.dataList = res.rows;
        if (res.code === 200) {
          if (page == "page") {
            this.dataList = this.dataList.concat(res.rows);
          } else {
            this.dataList = res.rows;
          }
          this.total = res.total;
        }
      });
    },
    // 下拉加载更多
    load() {
      if (this.dataList.length >= this.total) {
        return;
      } else {
        this.queryQarams.pageNum++;
        this.getUnitDetail("page");
      }
    },
    // 获取巡查详情
    handleDetail(item) {
      this.$tab.navigateTo(
        "/pages/common/addpatrol/AddORDetail?patrold=" +
          item.id +
          "&type=detail"
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
// @import "../../../static/font/iconfont.css";
.content {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  // background: rgba(240, 240, 240, 1);

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
  width: 100%;
  display: flex;
  justify-content: center;
  // margin-top: 30rpx;
  .list {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    display: flex;
    flex-flow: column;
    padding-top: 50rpx;
    .project-box {
      width: 92%;
      mix-blend-mode: normal;
      border-radius: 15rpx;
      background: #ffffff;
      margin-bottom: 30rpx;
      display: flex;
      position: relative;
      .approval-status {
        position: absolute;
        top: -25rpx;
        right: 20rpx;
        width: 122rpx;
        height: 50rpx;
        background-image: url("../../../static/images/message/pass.png");
        background-size: 100% 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #ffffff;
        font-family: 苹方-简-中粗体;
        font-size: 26rpx;
      }
      .approval-status-fail {
        background-image: url("../../../static/images/message/fail.png");
      }
      .detail {
        display: flex;
        width: 100%;
        .left {
          width: 15rpx;
          height: 100%;
          border-radius: 15rpx 0 0 15rpx;
          mix-blend-mode: normal;
          background: linear-gradient(160.05deg, #0385ff 8.68%, #80c1ff 91.71%);
        }
      }
      .info-box {
        flex: 1;
        // height: 100%;
        padding: 28rpx 30rpx 24rpx 30rpx;
        display: flex;
        flex-flow: column;
        .name-and-status {
          display: flex;
          .type {
            display: flex;
            margin-right: 20rpx;
            .logo {
              width: 36rpx;
              height: 36rpx;
              margin-right: 2rpx;
            }
            .type-name {
              color: #3e8df6;
              font-family: 苹方-简-中粗体;
              font-size: 28rpx;
            }
            .type-name-1 {
              color: #02cd96;
            }
          }
          .name {
            mix-blend-mode: normal;
            color: #333333;
            font-family: 苹方-简-中粗体;
            font-size: 30rpx;
          }
        }
        .note {
          width: 100%;
          margin-top: 20rpx;
          // height: 80rpx;
          mix-blend-mode: normal;
          background: #f8f8f8;
          color: #333333;
          font-family: 苹方-简-常规体;
          font-size: 30rpx;
          display: flex;
          flex-flow: column;
          // align-items: center;
          padding-left: 30rpx;
          padding: 18rpx 0 18rpx 15rpx;
          // justify-content: center;
          .describe-box {
            display: flex;
            color: #333333;
            .label {
              font-family: 苹方-简-中粗体;
              font-size: 24rpx;
            }
            .describe {
              font-family: 苹方-简-常规体;
              font-size: 24rpx;
              word-break: break-all;
              flex: 1;
            }
          }
        }
        .bottom-box {
          margin-top: 30rpx;
          display: flex;
          justify-content: space-between;
          .time {
            flex: 1;
            color: #666666;
            font-family: 苹方-简-常规体;
            font-size: 24rpx;
          }
          .tj-btn {
            border-radius: 10rpx;
            // width: 110rpx;
            height: 44rpx;
            display: flex;
            align-items: center;
            font-family: 苹方-简-常规体;
            font-size: 24rpx;
            background: #3e8df6;
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
    .no-more {
      padding: 0 0 10rpx;
      font-size: 28rpx;
      color: #89879c;
      .loading-fin {
        margin-top: 20rpx;
        font-size: 24rpx;
        color: #919296;
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
  margin-bottom: 40rpx;
  display: flex;
  justify-content: center;
  flex-flow: column;
  .project-name {
    font-size: 50rpx;
    color: #ffffff;
    font-family: 苹方-简-中黑体;
    letter-spacing: -0.7px;
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
  background: url("../../../static/images/baiyi-bg.png");
  background-size: 100% 100%;
  display: flex;
  padding: 36rpx 16rpx 0;
  justify-content: space-between;
}
.ghs-box {
  margin-left: 2%;
  background: url("../../../static/images/hhs-bg.png");
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
