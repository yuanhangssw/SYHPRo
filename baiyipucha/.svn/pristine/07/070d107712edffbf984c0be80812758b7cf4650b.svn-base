<template>
  <view class="tj-tabs">
    <view
      class="tj-tab-item"
      :class="{ current: index === currentIndex }"
      v-for="(item, index) in list"
      :key="index"
      @click="tabSelect(index)"
    >
      <span>{{ item }}</span>
    </view>
  </view>
</template>
<script>
export default {
  props: {
    list: {
      type: Array,
      default: [],
    },
  },
  data() {
    return {
      currentIndex: 0,
    };
  },
  methods: {
    tabSelect(index) {
      if (this.currentIndex === index) return;
      this.currentIndex = index;
      this.$emit("select", index);
    },
  },
};
</script>

<style lang="scss" scoped>
// @import "../../static/font/iconfont.css";
.tj-tabs {
  width: 100%;
  font-size: 14rpx;
  display: flex;
  padding-left: 30rpx;
  .tj-tab-item {
    height: 40rpx;
    display: flex;
    align-items: flex-end;
    span {
      padding-bottom: 10rpx;
      position: relative;
      display: inline-block;
      color: #737895;
      font-family: 苹方-简-常规体;
      font-size: 28rpx;
      transition: all 0.3s;
      mix-blend-mode: normal;
    }
    & + .tj-tab-item {
      margin-left: 30rpx;
    }

    &.current {
      position: relative;

      span {
        transform: scale(1.2);
        color: #3e8df6;
        font-family: 苹方-简-中粗体;
        font-size: 28rpx;
        letter-spacing: -0.6rpx;

        &::after {
          content: "";
          width: 100%;
          height: 4rpx;
          border-radius: 3rpx;
          position: absolute;
          bottom: 3rpx;
          left: 0;
          background-color: #3e8df6;
        }
      }
    }
  }
}
</style>
