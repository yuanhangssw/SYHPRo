<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="108px"
    >
      <el-form-item label="巡查用户姓名" prop="inspectorName">
        <el-input
          v-model="queryParams.inspectorName"
          placeholder="请输入巡查用户姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:inspector:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:inspector:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:inspector:remove']"
          >删除</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="inspectorList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="70" align="center" />
      <el-table-column
        label="巡查用户姓名"
        align="center"
        prop="inspectorName"
      />
      <el-table-column label="手机号" align="center" prop="phone" />
      <!-- <el-table-column label="密码" align="center" prop="password" /> -->
      <el-table-column label="管理机构" align="center" prop="deptName" />
      <!-- <el-table-column label="部门用户创建者" align="center" prop="userId" />
      <el-table-column label="部门创建的用户" align="center" prop="deptId" /> -->

      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="handleDistribution(scope.row)"
            >分配巡查单元</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:inspector:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:inspector:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改巡查员用户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="巡查用户姓名" prop="inspectorName">
          <el-input
            v-model="form.inspectorName"
            placeholder="请输入巡查用户姓名"
          />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" />
        </el-form-item>
        <!-- <el-form-item label="部门用户创建者" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入部门用户创建者" />
        </el-form-item>
        <el-form-item label="部门创建的用户" prop="deptId">
          <el-input v-model="form.deptId" placeholder="请输入部门创建的用户" />
        </el-form-item> -->
        <el-form-item label="删除标志" prop="delFlag">
          <!-- <el-input v-model="form.delFlag" placeholder="请输入删除标志" /> -->
          <el-radio v-model="form.delFlag" label="0">正常</el-radio>
          <el-radio v-model="form.delFlag" label="1">删除</el-radio>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="分配巡查单元"
      :visible.sync="distributionVisible"
      width="500px"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form label-width="80px">
        <!-- 左右两侧布局，一侧是项目，一侧是巡查单元 -->
        <el-row>
          <el-col
            :span="12"
            style="
              max-height: 500px;
              overflow: auto;
              border-right: 1px solid #ebeef5;
              height: 500px;
            "
          >
            <el-form-item label="项目名称">
              <div v-for="item in DeptIdList" :key="item.id">
                <el-radio
                  v-model="checked1"
                  :label="item.id"
                  @change="handlelistUnit"
                  border
                >
                  {{ item.projectName }}
                </el-radio>
              </div>
            </el-form-item>
          </el-col>
          <el-col
            :span="12"
            style="max-height: 500px; overflow: auto; height: 500px"
          >
            <el-form-item label="巡查单元" prop="unitName">
              <el-checkbox
                :indeterminate="isIndeterminate"
                v-model="checkAll"
                @change="handleCheckAllChange"
                >全选</el-checkbox
              >
              <div style="margin: 15px 0"></div>
              <el-checkbox-group
                v-model="checkedCities"
                @change="handleCheckedCitiesChange"
              >
                <el-checkbox
                  v-for="city in cities"
                  :label="city.id"
                  :key="city.id"
                  >{{ city.unitName }}</el-checkbox
                >
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="distributionsubmitForm"
          >确 定</el-button
        >
        <el-button @click="distributionVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listInspector,
  getInspector,
  delInspector,
  addInspector,
  updateInspector,
  blistUnit,
} from "@/api/system/inspector";
import { listbyDeptId, listUnit, addInspectorUnit } from "@/api/system/project";

export default {
  name: "Inspector",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 巡查员用户表格数据
      inspectorList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        delFlag: 0,
        inspectorName: null,
        phone: null,
        password: null,
        userId: null,
        deptId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        inspectorName: [
          { required: true, message: "请输入巡查用户姓名", trigger: "blur" },
        ],
        phone: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          {
            pattern: /^1[3456789]\d{9}$/,
            message: "手机号格式不正确",
            trigger: "blur",
          },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },
      distributionVisible: false,
      checked1: false,
      cities: [],
      checkAll: false,
      isIndeterminate: true,
      checkedCities: [],
      DeptIdList: [],
      selectedItems: [],
      users: null,
    };
  },
  created() {
    this.getList();
    this.getlistbyDeptId();
  },
  methods: {
    async distributionsubmitForm() {
      this.selectedItems.forEach((item) => {
        item.inspectorId = this.users;
      });
      const response = await addInspectorUnit(this.selectedItems);
      if (response.code == 200) {
        this.$modal.msgSuccess("分配成功");
        this.distributionVisible = false;
      }
    },
    updateSelectedItems() {
      let item = this.selectedItems.find(
        (item) => item.projectId == this.checked1
      );
      if (item) {
        item.unit = this.checkedCities;
      } else {
        this.selectedItems.push({
          projectId: this.checked1,
          unit: this.checkedCities,
        });
      }
    },
    async handlelistUnit() {
      const response = await listUnit({
        projectId: this.checked1,
        delFlag: 0,
      });
      try {
        this.cities = response.rows;
        let item = this.selectedItems.find(
          (item) => item.projectId == this.checked1
        );
        this.checkedCities = item ? item.unit : [];
        this.checkAll =
          this.cities.length > 0 &&
          this.cities.length == this.checkedCities.length;
      } catch (error) {
        console.log(error);
        this.cities = [];
      }
    },
    async getlistbyDeptId() {
      const response = await listbyDeptId();
      try {
        this.DeptIdList = response.rows.sort(
          (a, b) => a.projectType - b.projectType
        );
        this.checked1 = this.DeptIdList[0].id;
        this.handlelistUnit();
      } catch (error) {
        console.log(error);
        this.DeptIdList = [];
      }
    },
    // 全选
    handleCheckAllChange(val) {
      this.checkedCities = val ? this.cities.map((city) => city.id) : [];
      this.updateSelectedItems();
    },
    // 单选
    handleCheckedCitiesChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.cities.length;
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.cities.length;
      this.updateSelectedItems();
    },
    handleDistribution(row) {
      blistUnit({ inspector: row.id }).then((response) => {
        let data = response.rows;

        let result = data.reduce((acc, val) => {
          let found = acc.find((o) => o.projectId === val.projectId);
          if (found) {
            found.unit.push(val.patrolUnitId);
          } else {
            acc.push({ projectId: val.projectId, unit: [val.patrolUnitId] });
          }
          return acc;
        }, []);

        this.selectedItems = result;
        this.users = row.id;
        this.distributionVisible = true;
        let item = this.selectedItems.find(
          (item) => item.projectId == this.checked1
        );
        this.checkedCities = item ? item.unit : [];
        this.checkAll =
          this.cities.length > 0 &&
          this.cities.length == this.checkedCities.length;
      });
    },
    /** 查询巡查员用户列表 */
    getList() {
      this.loading = true;
      if (this.$store.getters.deptId != "") {
        this.queryParams.deptId = this.$store.getters.deptId;
      }
      listInspector(this.queryParams).then((response) => {
        this.inspectorList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        inspectorName: null,
        phone: null,
        password: null,
        userId: null,
        deptId: null,
        delFlag: "0",
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加巡查员用户";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getInspector(id).then((response) => {
        // password base64解密
        this.form = response.data;
        this.form.password = atob(this.form.password);
        this.open = true;
        this.title = "修改巡查员用户";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateInspector(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInspector(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除巡查员用户编号为"' + ids + '"的数据项？')
        .then(function () {
          return delInspector(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/inspector/export",
        {
          ...this.queryParams,
        },
        `inspector_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-checkbox:last-of-type {
  margin-right: 15px;
}
.el-radio.is-bordered {
  margin: 0 0 10px 15px;
  width: 105px;
}
</style>
