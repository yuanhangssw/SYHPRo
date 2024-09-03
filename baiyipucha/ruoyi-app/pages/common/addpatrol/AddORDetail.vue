<template>
  <view class="content content-box">
    <view class="header-back">
      <view class="back-box" @click="back">
        <text class="back">＜</text>
        <text class="label">{{
          pageType === "add" ? "添加巡查信息" : "巡查详情"
        }}</text>
      </view>
    </view>
    <scroll-view
      :scroll-y="true"
      class="scroll-view"
      :style="{ height: contentHeight + 'px' }"
    >
      <view class="info-content">
        <view class="info-box">
          <view class="label">所在位置</view>
          <view class="info-value">
            <input
              v-if="pageType === 'add' || pageType === 'edit'"
              v-model="form.address"
              class="uni-input"
              placeholder="位置"
              @click="location"
            />
            <view v-else class="info-view">
              <text class="tj-text">{{
                form.address ? form.address : ""
              }}</text>
            </view>
          </view>
        </view>
        <view v-if="pageType === 'add'" class="info-box">
          <view class="label">巡查单元</view>
          <view class="info-value">
            <picker
              @change="changeUnitType"
              :value="unitIndex"
              :range="unitList"
              range-key="unit_name"
            >
              <view class="uni-input">{{
                unitList.length > 0 ? unitList[unitIndex].unit_name : ""
              }}</view>
            </picker>
          </view>
        </view>
        <view class="info-box">
          <view class="label">巡查类别</view>
          <view class="info-value">
            <picker
              v-if="pageType === 'add' || pageType === 'edit'"
              @change="changeType"
              :value="typeIndex"
              :range="typeList"
            >
              <view class="uni-input">{{ typeList[typeIndex] }}</view>
            </picker>
            <view v-else class="info-view">
              <text class="tj-text">{{
                form.patrolType === 1 ? "白蚁" : "獾狐鼠"
              }}</text>
            </view>
          </view>
        </view>
        <view class="info-box">
          <view class="label">巡查类型</view>
          <view class="info-value">
            <picker
              v-if="pageType === 'add' || pageType === 'edit'"
              @change="changePatrolType"
              :value="patrolTypeIndex"
              :range="patrolTypeList"
              range-key="typeName"
            >
              <view class="uni-input">{{
                patrolTypeList.length > 0
                  ? patrolTypeList[patrolTypeIndex].typeName
                  : ""
              }}</view>
            </picker>
            <view v-else class="info-view">
              <text class="tj-text">{{
                patrolTypeList.length > 0
                  ? patrolTypeList[patrolTypeIndex].typeName
                  : ""
              }}</text>
            </view>
          </view>
        </view>

        <view class="info-box">
          <view class="label">巡查时间</view>
          <view class="info-value">
            <uni-datetime-picker
              v-if="pageType === 'add' || pageType === 'edit'"
              type="datetime"
              :dateNow="dateNow"
              :end="startDate"
              @change="onChange"
              v-model="form.patrolTime"
            />
            <!-- :disabledDate="disabledDate" -->
            <view v-else class="info-view">
              <text class="tj-text">{{
                form.patrolTime ? form.patrolTime : ""
              }}</text>
            </view>
          </view>
        </view>
        <view class="info-box">
          <view class="label">桩号</view>
          <view class="info-value">
            <input
              v-if="pageType === 'add' || pageType === 'edit'"
              v-model="form.pileNumber"
              class="uni-input"
              placeholder="请输入桩号"
            />
            <view v-else class="info-view">
              <text class="tj-text">{{
                form.pileNumber ? form.pileNumber : ""
              }}</text>
            </view>
          </view>
        </view>
        <view class="info-box">
          <view class="label">轴线距离(m)</view>
          <view class="info-value">
            <input
              v-if="pageType === 'add' || pageType === 'edit'"
              type="number"
              v-model="form.axisDistance"
              class="uni-input"
              placeholder="请输入轴线距离"
            />
            <view v-else class="info-view">
              <text class="tj-text">{{
                form.axisDistance ? form.axisDistance : ""
              }}</text>
            </view>
          </view>
        </view>
        <view class="info-box">
          <view class="label">断面位置</view>
          <view class="info-value">
            <picker
              v-if="pageType === 'add' || pageType === 'edit'"
              @change="changeSectionPosition"
              :value="positionIndex"
              :range="sectionPositionList"
            >
              <view class="uni-input">{{
                sectionPositionList[positionIndex]
              }}</view>
            </picker>
            <view v-else class="info-view">
              <text class="tj-text">{{
                form.sectionPosition ? form.sectionPosition : ""
              }}</text>
            </view>
          </view>
        </view>
        <view class="info-box">
          <view class="label">描述</view>
          <view class="info-value">
            <input
              v-if="pageType === 'add' || pageType === 'edit'"
              v-model="form.description"
              class="uni-input"
              placeholder="请输入描述信息"
            />
            <view v-else class="info-view">
              <text class="tj-text">{{
                form.description ? form.description : ""
              }}</text>
            </view>
          </view>
        </view>
        <view class="info-box">
          <view class="label">{{ pageType === "add" ? "拍照" : "照片" }}</view>
          <view class="info-value">
            <uni-file-picker
              v-if="pageType === 'add' || pageType === 'edit'"
              :value="imageLists"
              limit="9"
              @select="onSelectFile"
              @success="onUploadSuccess"
              @delete="removeFile"
            >
              <view class="upload-box">
                <image
                  src="../../../static/images/picture.png"
                  class="upload-img"
                ></image>
              </view>
            </uni-file-picker>
            <view v-else class="image-list">
              <view
                v-for="(item, index) in imageLists"
                :key="index"
                class="upload-box image-box"
              >
                <image :src="item.url" class="picture"></image>
              </view>
            </view>
          </view>
        </view>
        <view v-if="pageType !== 'add'" class="info-box">
          <view class="label">状态</view>
          <view class="info-value">
            <view v-if="pageType !== 'add'" class="info-view">
              <text class="tj-text">{{
                form.auditStatus === "1"
                  ? "已提交"
                  : form.auditStatus === "2"
                  ? "审核中"
                  : form.auditStatus === "3"
                  ? "审核通过"
                  : "审核未通过"
              }}</text>
            </view>
          </view>
        </view>
        <view v-if="pageType === 'add' || pageType === 'edit'" class="btn-box">
          <button type="primary" class="tj-btn" @click="submit">提交</button>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import {
  addPatrol,
  editPatrol,
  getPatrol,
  getPatrolTypeList,
  getUnits2,
} from "@/api/unit/index";
import storage from "@/utils/storage";
import constant from "@/utils/constant";
export default {
  data() {
    return {
      contentHeight: 0,
      typeList: ["白蚁", "獾狐鼠"],
      typeIndex: 0,
      sectionPositionList: ["顶", "上游", "下游"],
      positionIndex: 0,
      id: null,
      queryQarams: {
        userid: storage.get(constant.userId),
        projectid: this.$store.state.user.projectId,
      },
      form: {
        projectId: this.$store.state.user.projectId,
        patrolUnit: null, // 巡查单元
        inspectorId: storage.get(constant.userId), // 巡查用户
        patrolType: null,
        address: "",
        lat: null,
        lon: null,
        pileNumber: null, //桩号
        description: null, // 描述
        sectionPosition: null, // 断面位置
        axisDistance: null, // 轴线距离
        patrolTime: null, // 巡查时间
        freedom1: "", // 文件地址
        freedom3: "", // 巡查类型
      },
      patrolTime: null,

      fileLists: [],
      imageLists: [],
      filePathList: [],
      src: "",
      pageType: "add",
      patrolTypeList: [],
      patrolTypeIndex: 0,
      unitList: [],
      unitIndex: 0,
      startDate: null,
      // disabledDate: null,
      dateNow: null,
      isChooseLocation: false,
    };
  },
  onLoad: function (option) {
    //option为object类型，会序列化上个页面传递的参数
    if (option.id) {
      this.form.patrolUnit = option.id;
    }

    this.pageType = option.type;
    if (option.patrold) {
      this.id = option.patrold;
    }
  },
  onShow: function () {
    if (this.isChooseLocation === false) {
      this.dateNow = Date.now();
      if (this.pageType === "detail") {
        this.getPartrolDetail();
      } else {
        this.getUnitList();
        this.getPatrolTypes();
        this.getCurrentDate();
      }
    } else {
      this.isChooseLocation = false;
    }
  },
  onReady() {
    let that = this;
    uni.getSystemInfo({
      //调用uni-app接口获取屏幕高度
      success(res) {
        //成功回调函数
        const pageHeiht = res.windowHeight; //windowHeight为窗口高度
        let headerBackDom = uni.createSelectorQuery().select(".header-back"); //想要获取高度的元素名（class/id）
        headerBackDom
          .boundingClientRect((data) => {
            if (data) {
              // that.contentHeight = pageHeiht - 105;
              that.contentHeight = pageHeiht - data.height;
            }
          })
          .exec();
      },
    });
  },
  created() {},
  methods: {
    getCurrentDate() {
      const date = new Date();
      this.startDate = `${date.getFullYear()}-${
        date.getMonth() + 1
      }-${date.getDate()} ${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`;
    },
    onChange(event) {
      // 时间选择器值改变时的处理逻辑
    },
    getUnitList() {
      getUnits2(this.queryQarams).then((res) => {
        if (res.code === 200) {
          this.unitList = res.data;
          if (this.unitList.length > 0) {
            this.form.patrolUnit = this.unitList[0].id;
          }
        }
      });
    },
    change(value) {},
    // 获取详情
    getPartrolDetail() {
      let that = this;
      this.imageLists = [];
      getPatrol(this.id).then((res) => {
        if (res.code === 200) {
          that.form = res.data;
          that.typeIndex = that.form.patrolType - 1;

          that.positionIndex =
            that.form.sectionPosition === "顶"
              ? 0
              : that.form.sectionPosition === "上游"
              ? 1
              : 2;
          if (that.form.auditStatus == "4") {
            that.pageType = "edit";
          }
          if (that.form.freedom1) {
            let filePathList = that.form.freedom1.split(",");
            if (filePathList.length > 0) {
              for (let i = 0; i < filePathList.length; i++) {
                let obj = {
                  url: that.$url + filePathList[i],
                  uuid: i,
                };
                let obj1 = {
                  uuId: i,
                  fileName: filePathList[i],
                };
                that.imageLists.push(obj);
                that.fileLists.push(obj1);
              }
            }
          }
          that.getPatrolTypes(that.form.patrolType);
        }
      });
    },
    // 获取当前位置
    location() {
      this.isChooseLocation = true;
      uni.chooseLocation({
        success: (res) => {
          this.form.address = res.address;
          // console.log("位置名称：" + res.name);
          // console.log("详细地址：" + res.address);
          // console.log("纬度：" + res.latitude);
          // console.log("经度：" + res.longitude);
          this.form.lat = res.latitude;
          this.form.lon = res.longitude;
        },
      });
    },
    changeUnitType(e) {
      this.unitIndex = e.detail.value;
      this.form.patrolUnit = this.unitList[this.unitIndex].id;
    },
    // 断面位置选择
    changeSectionPosition(e) {
      this.positionIndex = e.detail.value;
      this.form.sectionPosition = this.sectionPositionList[this.positionIndex];
    },
    // 类型选择
    changeType(e) {
      this.typeIndex = e.detail.value;
      this.patrolTypeIndex = 0;
      this.form.patrolType = this.typeIndex == 0 ? 1 : 2;
      this.getPatrolTypes();
    },

    // 获取类型列表
    getPatrolTypes() {
      let type = this.typeIndex == 0 ? 1 : 2;
      getPatrolTypeList(type).then((res) => {
        if (res.code === 200) {
          if (res.rows) {
            this.patrolTypeList = res.rows;
            if (this.pageType === "edit" || this.pageType === "detail") {
              for (let i = 0; i < this.patrolTypeList.length; i++) {
                if (this.form.freedom3 == this.patrolTypeList[i].id) {
                  this.patrolTypeIndex = i;

                  break;
                }
              }
            } else {
              this.form.freedom3 = this.patrolTypeList[0].id;
            }
          }
        }
      });
    },

    // 巡查类型选择
    changePatrolType(e) {
      this.patrolTypeIndex = e.detail.value;
      this.form.freedom3 = this.patrolTypeList[this.patrolTypeIndex].id;
    },
    // 选择时间
    bindDateChange(e) {
      this.form.patrolTime = e.detail.value;
    },
    onSelectFile(e) {
      let that = this;
      let tempFile = e.tempFiles[0];
      let uuId = tempFile.uuid;
      uni.uploadFile({
        url: this.$url + "/common/upload",
        filePath: tempFile.url,
        name: "file",
        success(uploadFileRes) {
          that.$modal.msgSuccess("上传成功");
          let obj = {
            uuId: uuId,
            fileName: JSON.parse(uploadFileRes.data).fileName,
          };
          that.fileLists.push(obj);
        },
      });
    },

    // 删除上传文件
    removeFile(e) {
      let uuId = e.tempFile.uuid;
      for (let i = 0; i < this.fileLists.length; i++) {
        if (uuId === this.fileLists[i].uuId) {
          this.fileLists.splice(i, 1);
          break;
        }
      }
    },
    // 上传成功
    onUploadSuccess(e) {
      console.log("上传成功");
    },
    submit() {
      this.form.patrolType = this.typeIndex == 0 ? 1 : 2;
      this.form.freedom1 = "";
      if (!this.form.address) {
        this.$modal.msg("请选择所在位置");
        return;
      }
      if (!this.form.patrolUnit) {
        this.$modal.msg("请选择巡查单元");
        return;
      }
      if (!this.form.patrolTime) {
        this.$modal.msg("请选择巡查时间");
        return;
      }
      if (this.fileLists.length > 0) {
        // this.form.freedom1 = this.fileLists.join(",");
        for (let i = 0; i < this.fileLists.length; i++) {
          this.form.freedom1 =
            this.form.freedom1 + this.fileLists[i].fileName + ",";
        }
        this.form.freedom1 = this.form.freedom1.substring(
          0,
          this.form.freedom1.length - 1
        );
      }
      if (this.form.id && this.form.auditStatus == "4") {
        editPatrol(this.form).then((res) => {
          if (res.code === 200) {
            this.$modal.msgSuccess("修改成功");
            uni.navigateBack({
              delta: 1, // 返回的页面数，这里设置为1表示返回上一页
            });
          }
        });
      } else {
        addPatrol(this.form).then((res) => {
          if (res.code === 200) {
            this.$modal.msgSuccess("添加成功");
            this.$store.commit("SET_ISNEWADDPATROL", true);
            uni.navigateBack({
              delta: 1, // 返回的页面数，这里设置为1表示返回上一页
            });
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
  .header-back {
    width: 100%;
    .back-box {
      width: 100%;
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
}
.scroll-view {
  // position: relative;
  // margin-top: 50rpx;
  width: 100%;
  display: flex;
  justify-content: center;
  padding: 0 30rpx;
  .info-content {
    width: 100%;
    display: flex;
    flex-flow: column;
    position: relative;
    align-items: center;
    justify-content: center;
  }
  .info-box {
    width: 100%;
    display: flex;
    align-items: center;
    // height: 92rpx;
    margin-bottom: 30rpx;
    .label {
      width: 180rpx;
      font-family: 苹方-简-常规体;
      font-size: 30rpx;
      color: #333333;
    }
    .info-value {
      flex: 1;
      .upload-box,
      .image-box {
        width: 200rpx;
        height: 140rpx;
        mix-blend-mode: normal;
        border-radius: 10rpx;
        background: #ffffff;
        display: flex;
        align-items: center;
        justify-content: center;
        .upload-img {
          width: 48rpx;
          height: 48rpx;
        }

        .picture {
          width: 100%;
          height: 100%;
        }
      }
      .image-box {
        margin-right: 30rpx;
        margin-bottom: 30rpx;
      }
      .info-view {
        min-height: 92rpx;
        display: flex;
        align-items: center;
      }
      .tj-text {
        // height: 92rpx;
        // line-height: 92rpx;
        color: #9095b2;
        font-family: 苹方-简-常规体;
        font-size: 28rpx;
      }
      .image-list {
        display: flex;
        flex-wrap: wrap;
      }
    }
  }
  .btn-box {
    width: 100%;
    margin-bottom: 30rpx;
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

::v-deep .uni-date-editor--x {
  border: none;
  background-color: #ffffff;
  .uni-date-x {
    // background: transparent;
    padding-left: 15rpx;
    .icon-calendar {
      padding-left: 0;
    }
    .uni-date__x-input {
      height: 92rpx;
      line-height: 92rpx;
      color: #9095b2;
    }
  }
}
</style>
