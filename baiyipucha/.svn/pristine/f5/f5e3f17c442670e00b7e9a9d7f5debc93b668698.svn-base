<template>
  <view class="content content-box">
    <tj-add class="add-box"></tj-add>
    <view class="box">
      <view class="header-box">
        <view class="header">
          <tj-tabs :list="tabList" @select="tabChange" class="tab-style" />
        </view>
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
            <view v-if="item.auditStatus === '1'" class="approval-status"
              >已提交</view
            >
            <view v-if="item.auditStatus === '2'" class="approval-status"
              >审核中</view
            >
            <view v-if="item.auditStatus === '3'" class="approval-status"
              >已通过</view
            >
            <view
              v-if="item.auditStatus === '4'"
              class="approval-status approval-status-fail"
              >未通过</view
            >
            <view class="info-box">
              <view class="name-and-status">
                <view
                  :class="item.auditStatus != '4' ? 'status' : 'status fail'"
                  >{{
                    item.auditStatus === "3"
                      ? "审核通过"
                      : item.auditStatus === "4"
                      ? "审核驳回"
                      : item.auditStatus === "1"
                      ? "已提交"
                      : "审核中"
                  }}</view
                >
                <view class="name">{{
                  item.unitName ? item.unitName : ""
                }}</view>
              </view>
              <view class="note">{{
                item.description ? item.description : ""
              }}</view>
              <view class="bottom-box">
                <view class="time">{{
                  item.patrolTime ? item.patrolTime : ""
                }}</view>
                <button
                  class="mini-btn tj-btn"
                  type="primary"
                  size="mini"
                  @click="toUnitDetail(item)"
                >
                  查看
                </button>
              </view>
            </view>
          </view>
          <div
            class="no-more flex-1 flex-h jc-c"
            v-if="dataList.length >= total"
          >
            没有更多了
          </div>
        </view>
        <tj-empty v-else></tj-empty>
        <!-- </view> -->
      </scroll-view>
    </view>
  </view>
</template>

<script>
import { getUnitInfo, getPatrolTotalByStatus } from "@/api/unit/index";
import storage from "@/utils/storage";
import constant from "@/utils/constant";
import tjiEmpty from "../../components/tj-empty/tj-empty.vue";
export default {
  components: { tjiEmpty },
  data() {
    return {
      contentHeight: 0,
      queryQarams: {
        inspectorId: storage.get(constant.userId),
        projectId: this.$store.state.user.projectId,
        auditStatus: "",
        pageSize: 10,
        pageNum: 1,
      },
      queryTotalParams: {
        userid: storage.get(constant.userId),
        projectid: this.$store.state.user.projectId,
      },
      // tabList: ["全部", "未通过", "已通过"],
      tabList: [],
      dataList: [],
      total: 0,
    };
  },
  onLoad: function () {},
  onShow: function () {
    this.queryQarams.projectId = this.$store.state.user.projectId;
    this.queryTotalParams.projectid = this.$store.state.user.projectId;
    this.queryQarams.pageNum = 1;
    this.getUnitDetail();
    this.getTotal();
  },
  onReady() {
    let that = this;
    uni.getSystemInfo({
      //调用uni-app接口获取屏幕高度
      success(res) {
        //成功回调函数
        const pageHeiht = res.windowHeight; //windowHeight为窗口高度
        let headerBoxDom = uni.createSelectorQuery().select(".header-box"); //想要获取高度的元素名（class/id）
        headerBoxDom
          .boundingClientRect((data) => {
            if (data) {
              that.contentHeight = pageHeiht - 92;
              // that.contentHeight = pageHeiht - data.height;
            }
          })
          .exec();
      },
    });
  },
  created() {},
  methods: {
    // 获取巡查数量
    getTotal() {
      this.tabList = [];
      getPatrolTotalByStatus(this.queryTotalParams).then((res) => {
        if (res.code === 200) {
          if (res.data) {
            this.tabList.push(
              "全部(" + (res.data.total ? res.data.total : 0) + ")"
            );
            this.tabList.push(
              "未通过(" + (res.data.backtotal ? res.data.backtotal : 0) + ")"
            );
            this.tabList.push(
              "已通过(" + (res.data.passtotal ? res.data.passtotal : 0) + ")"
            );
          }
        } else {
          this.tabList = ["全部", "未通过", "已通过"];
        }
      });
    },
    getUnitDetail(page) {
      getUnitInfo(this.queryQarams).then((res) => {
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
    tabChange(index) {
      this.queryQarams.auditStatus = index == 0 ? "" : index == 1 ? 4 : 3;
      this.queryQarams.pageNum = 1;
      this.getUnitDetail();
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
    toUnitDetail(item) {
      this.$tab.navigateTo(
        "/pages/common/addpatrol/AddORDetail?patrold=" +
          item.id +
          "&type=detail"
      );
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
  height: 100%;
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
  .header {
    width: 100%;
    margin-top: 100rpx;
    margin-bottom: 30rpx;
    display: flex;
    .tab-style {
      flex: 1;
      justify-content: left;
    }
  }
  .box {
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
  .header-box {
    width: 100%;
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
    padding-top: 25rpx;
    .project-box {
      width: 92%;
      height: 260rpx;
      mix-blend-mode: normal;
      border-radius: 15rpx;
      background: #ffffff;
      margin-bottom: 50rpx;
      display: flex;
      position: relative;
      &:last-child {
        margin-bottom: 30rpx;
      }
      .approval-status {
        position: absolute;
        top: -25rpx;
        right: 20rpx;
        width: 122rpx;
        height: 50rpx;
        background-image: url("../../static/images/message/pass.png");
        background-size: 100% 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #ffffff;
        font-family: 苹方-简-中粗体;
        font-size: 26rpx;
      }
      .approval-status-fail {
        background-image: url("../../static/images/message/fail.png");
      }
      .info-box {
        flex: 1;
        height: 100%;
        padding: 28rpx 30rpx 0 30rpx;
        display: flex;
        flex-flow: column;
        .name-and-status {
          display: flex;
          // justify-content: space-between;
          .status {
            width: 110rpx;
            height: 40rpx;
            line-height: 40rpx;
            mix-blend-mode: normal;
            border-radius: 10rpx;
            border: 1rpx solid #02cd96;
            box-sizing: border-box;
            background: #e3fff8;
            color: #02cd96;
            font-family: 苹方-简-常规体;
            font-size: 22rpx;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 10rpx;
          }
          .fail {
            border: 1px solid #ffc8c0;
            background: #fff1ee;
            color: #fa624b;
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
          height: 80rpx;
          mix-blend-mode: normal;
          background: #f8f8f8;
          color: #333333;
          font-family: 苹方-简-常规体;
          font-size: 30rpx;
          display: flex;
          align-items: center;
          padding-left: 30rpx;
          // justify-content: center;
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
</style>
