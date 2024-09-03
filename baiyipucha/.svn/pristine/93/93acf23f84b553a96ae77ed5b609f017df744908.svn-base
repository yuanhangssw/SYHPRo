<template>
  <div class="tj-empty-wrap flex-h ai-c">
    <div class="tj-empty-body flex-v ai-c">
      <image src="../../static/images/empty.png" />
      <text class="desc">{{ desc }}</text>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    desc: {
      type: String,
      default: "暂无数据",
    },
  },
};
</script>

<style scoped lang="scss">
.tj-empty-wrap {
  width: 100%;
  height: 100%;
  // padding-top: 50rpx;
  .tj-empty-body {
    width: 100%;
    display: flex;
    flex-flow: column;
    justify-content: center;
    align-items: center;
    position: relative;
    image {
      width: 302rpx;
      height: 282rpx;
    }

    .desc {
      position: absolute;
      top: 200rpx;
      left: auto;
      // margin-top: 54rpx;
      color: #737b80;
      font-family: 苹方-简-常规体;
      font-size: 18rpx;
    }
  }
}
</style>
