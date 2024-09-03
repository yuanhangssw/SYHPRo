<template>
  <view class="content content-box">
    <view class="back-box" @click="back">
      <text class="back">＜</text>
      <text class="label">录入巡查单元信息</text>
    </view>
    <scroll-view
      :scroll-y="true"
      class="scroll-view"
      :style="{ height: contentHeight + 'px' }"
    >
      <view class="info-box">
        <view class="label">单元</view>
        <view class="info-value">
          <picker
            @change="onPickerChange($event, 'unit')"
            :value="unitIndex"
            :range="unitList"
            range-key="unitname"
          >
            <view class="uni-input">{{
              unitList.length > 0 ? unitList[unitIndex].unitname : ""
            }}</view>
          </picker>
        </view>
      </view>
      <view class="info-box">
        <view class="label">渗漏处数</view>
        <view class="info-value">
          <input
            v-model="form.leaksNumber"
            type="number"
            class="uni-input"
            placeholder="请输入渗漏处数"
          />
        </view>
      </view>
      <view class="info-box">
        <view class="label">穿坝处数</view>
        <view class="info-value">
          <input
            v-model="form.throughNumber"
            type="number"
            class="uni-input"
            placeholder="请输入穿坝处数"
          />
        </view>
      </view>
      <view class="info-box">
        <view class="label">跌窝处数</view>
        <view class="info-value">
          <input
            v-model="form.dropSocketNumber"
            type="number"
            class="uni-input"
            placeholder="请输入跌窝处数"
          />
        </view>
      </view>
      <view class="info-box">
        <view class="label">挖巢数量(个)</view>
        <view class="info-value">
          <input
            v-model="form.nestDigging"
            type="number"
            class="uni-input"
            placeholder="请输入挖巢数量"
          />
        </view>
      </view>
      <view class="info-box">
        <view class="label">施药面积(m²)</view>
        <view class="info-value">
          <input
            v-model="form.chargeArea"
            type="number"
            class="uni-input"
            placeholder="请输入施药面积"
          />
        </view>
      </view>
      <view class="info-box">
        <view class="label">灌浆量(延米)</view>
        <view class="info-value">
          <input
            v-model="form.groutingQuantity"
            type="number"
            class="uni-input"
            placeholder="请输入灌浆量"
          />
        </view>
      </view>
      <view class="info-box">
        <view class="label">投入资金(万元)</view>
        <view class="info-value">
          <input
            v-model="form.investCapital"
            type="text"
            @input="handleInput"
            class="uni-input"
            placeholder="请输入投入资金"
          />
        </view>
      </view>
      <view class="info-box">
        <view class="label">治理数量</view>
        <view class="info-value">
          <input
            v-model="form.quantityGovernance"
            type="number"
            class="uni-input"
            placeholder="请输入治理数量"
          />
        </view>
      </view>
      <view class="btn-box">
        <button type="primary" class="tj-btn" @click="submit">提交</button>
      </view>
    </scroll-view>
  </view>
</template>
<script>
import { getUnits, getUnitFill, addUnit, editUnit } from "@/api/unit/index";
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
      queryUnitFillParams: {
        inspector: storage.get(constant.userId),
        projectId: this.$store.state.user.projectId,
        unitId: "",
        delFlag: 0,
        fillType: "", // 类型1白蚁2动物
      },
      form: {
        projectId: this.$store.state.user.projectId,
        unitId: null, // 巡查单元
        inspector: storage.get(constant.userId), // 巡查用户
        fillType: 1, // 填报类型
        leaksNumber: 0, // 渗漏处数
        throughNumber: 0, // 穿坝处数
        dropSocketNumber: 0, // 跌窝处数
        nestDigging: 0, // 挖巢数量
        chargeArea: 0, // 施药面积
        groutingQuantity: 0, // 灌浆量
        investCapital: 0, // 投入资金
        quantityGovernance: 0, // 治理数量
      },
      fillType: "",
      unitNmameList: [],
      unitIdList: [],
      unitList: [],
      unitIndex: 0,
      typeIndex: 0,
      typeList: [
        {
          value: 1,
          text: "白蚁",
        },
        {
          value: 2,
          text: "獾狐鼠",
        },
      ],
      msgType: "success",
      messageText: "这是一条成功提示",
    };
  },
  onLoad: function (option) {
    //option为object类型，会序列化上个页面传递的参数
    this.queryUnitFillParams.fillType = parseInt(option.type);
    this.form.fillType = parseInt(option.type);
  },
  onReady() {
    let that = this;
    uni.getSystemInfo({
      //调用uni-app接口获取屏幕高度
      success(res) {
        //成功回调函数
        const pageHeiht = res.windowHeight; //windowHeight为窗口高度
        let infoDom = uni.createSelectorQuery().select(".scroll-view"); //想要获取高度的元素名（class/id）
        infoDom
          .boundingClientRect((data) => {
            if (data) {
              that.contentHeight = pageHeiht - 102;
            }
          })
          .exec();
      },
    });
  },
  onShow: function () {
    this.queryQarams.projectId = this.$store.state.user.projectId;
    this.getUnitList();
  },
  methods: {
    getUnitList() {
      getUnits(this.queryQarams).then((res) => {
        if (res.code === 200) {
          this.unitList = res.data;
          if (this.unitList.length > 0) {
            this.queryUnitFillParams.unitId = res.data[0].patrol_unit;
            this.form.unitId = res.data[0].patrol_unit;
          }

          this.getUnitFillInfo();
        }
      });
    },
    getUnitFillInfo() {
      getUnitFill(this.queryUnitFillParams).then((res) => {
        if (res.code === 200) {
          if (res.rows.length > 0) {
            this.form = res.rows[0];
          } else {
            this.form.fillType = this.queryUnitFillParams.fillType;
            this.form.id = null;
            this.form.leaksNumber = 0;
            this.form.throughNumber = 0;
            this.form.dropSocketNumber = 0;
            this.form.nestDigging = 0;
            this.form.chargeArea = 0;
            this.form.groutingQuantity = 0;
            this.form.investCapital = 0;
            this.form.quantityGovernance = 0;
          }
        }
      });
    },
    onPickerChange(event, prop) {
      switch (prop) {
        case "unit":
          if (this.unitList.length > 0) {
            this.unitIndex = event.detail.value;
            this.form.unitId = this.unitList[this.unitIndex].patrol_unit;
            this.queryUnitFillParams.unitId =
              this.unitList[this.unitIndex].patrol_unit;
            this.getUnitFillInfo();
          }

        // case "fillType":
        //   this.typeIndex = event.detail.value;
        //   this.form.fillType = this.typeList[this.typeIndex].text;
        default:
          break;
      }
    },

    // 投入资金
    handleInput(event) {
      // 获取输入值
      let value = event.target.value;
      // 使用正则表达式去除非数字和点的字符
      value = value.replace(/[^\d.]/g, "");
      // 限制只能有一个小数点
      const index = value.indexOf(".");
      if (index !== -1) {
        value =
          value.substr(0, index) + "." + value.substr(index).replace(/\./g, "");
      }
      // 更新输入框的值
      this.$nextTick(() => {
        this.form.investCapital = value;
      });
    },
    // 提交
    submit() {
      if (!this.form.unitId) {
        this.$modal.msg("请选择单元");
        return;
      }
      if (!this.form.leaksNumber.toString()) {
        this.$modal.msg("请输入渗漏处数");
        return;
      }
      if (!this.form.throughNumber.toString()) {
        this.$modal.msg("请输入穿坝处数");
        return;
      }
      if (!this.form.dropSocketNumber.toString()) {
        this.$modal.msg("请输入跌窝处数");
        return;
      }
      if (!this.form.nestDigging.toString()) {
        this.$modal.msg("请输入挖巢数量");
        return;
      }
      if (!this.form.chargeArea.toString()) {
        this.$modal.msg("请输入施药面积");
        return;
      }
      if (!this.form.groutingQuantity.toString()) {
        this.$modal.msg("请输入灌浆量");
        return;
      }
      if (!this.form.investCapital.toString()) {
        this.$modal.msg("请输入投入资金");
        return;
      }
      if (!this.form.quantityGovernance.toString()) {
        this.$modal.msg("请输入治理数量");
        return;
      }
      if (this.form.id) {
        // 修改
        editUnit(this.form).then((res) => {
          if (res.code === 200) {
            this.$modal.msgSuccess("修改成功");
            // uni.navigateBack({
            //   delta: 1, // 返回的页面数，这里设置为1表示返回上一页
            // });
          }
        });
      } else {
        // 添加
        addUnit(this.form).then((res) => {
          if (res.code === 200) {
            this.$modal.msgSuccess("添加成功");
            // uni.navigateBack({
            //   delta: 1, // 返回的页面数，这里设置为1表示返回上一页
            // });
          }
        });
      }
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
  .scroll-view {
    // position: relative;
    // margin-top: 50rpx;
    width: 100%;
    display: flex;
    justify-content: center;
    padding: 0 30rpx;
    .info-box {
      display: flex;
      align-items: center;
      // height: 92rpx;
      margin-bottom: 30rpx;
      .label {
        width: 220rpx;
        font-family: 苹方-简-常规体;
        font-size: 30rpx;
        color: #333333;
      }
      .info-value {
        flex: 1;
        .info-view {
          min-height: 92rpx;
          display: flex;
          align-items: center;
        }
      }
    }
    .btn-box {
      margin-top: 50rpx;
      padding: 0 100rpx;
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
  }
}
::v-deep .uni-stat__select {
  .uni-select {
    border: none;
    height: auto;
    .uni-select__input-box {
      height: 92rpx;
      .uni-select__input-placeholder,
      .uni-select__input-text {
        color: #9095b2;
        font-family: 苹方-简-常规体;
        font-size: 28rpx;
      }
    }
  }
}
::v-deep .uni-input {
  height: 92rpx;
  line-height: 92rpx;
  color: #9095b2;
  background: #ffffff;
  border-radius: 10rpx;
  padding-left: 20rpx;
  .uni-input-placeholder,
  .uni-input-input {
    color: #9095b2;
    font-family: 苹方-简-常规体;
    font-size: 28rpx;
  }
}
</style>
