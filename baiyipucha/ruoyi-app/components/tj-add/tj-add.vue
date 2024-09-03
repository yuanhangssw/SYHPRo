<template>
  <view class="add" @click="addPatrol">
    <image src="../../static/images/plus.png" class="plus-img" alt="" />
  </view>
</template>

<script>
export default {
  // props: {
  //   list: {
  //     type: Array,
  //     default: [],
  //   },
  // },
  // data() {
  //   return {
  //     currentIndex: 0,
  //   };
  // },
  methods: {
    addPatrol() {
      // this.$tab.navigateTo("/pages/common/addpatrol/index");
      this.$tab.navigateTo("/pages/common/addpatrol/AddORDetail?type=add");
    },
  },
};
</script>

<style lang="scss" scoped>
.add {
  width: 72rpx;
  height: 72rpx;
  background: linear-gradient(160.05deg, #0385ff 8.68%, #80c1ff 91.71%), #ffffff;
  box-shadow: 3.6px 5.4px 7.2px #00000040;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  .plus-img {
    width: 40rpx;
    height: 40rpx;
  }
}
</style>
