<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <!-- <el-form-item label="项目id" prop="projectId">
        <el-input
          v-model="queryParams.projectId"
          placeholder="请输入项目id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="单元名称" prop="unitName">
        <el-input
          v-model="queryParams.unitName"
          placeholder="请输入单元名称"
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
          v-hasPermi="['system:unit:add']"
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
          v-hasPermi="['system:unit:edit']"
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
          v-hasPermi="['system:unit:remove']"
          >删除</el-button
        >
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:unit:export']"
          >导出</el-button
        >
      </el-col> -->
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="unitList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="项目id" align="center" prop="projectId" /> -->
      <el-table-column label="项目名称" align="center" prop="projectName">
        <template slot-scope="scope"> {{ projectName }} </template>
      </el-table-column>
      <el-table-column label="单元名称" align="center" prop="unitName" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-flag"
            @click="handlePatrol(scope.row)"
            >巡查记录</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-claim"
            @click="handleFill(scope.row)"
            >填报记录</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:unit:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:unit:remove']"
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

    <!-- 添加或修改巡查单元对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <!-- <el-form-item label="项目id" prop="projectId">
          <el-input
            v-model="form.projectId"
            placeholder="请输入项目id"
            :disabled="true"
          />
        </el-form-item> -->
        <!-- 项目名称 -->
        <el-form-item label="项目名称" prop="projectId">
          <el-input v-model="projectName" :disabled="true" />
        </el-form-item>
        <el-form-item label="单元名称" prop="unitName">
          <el-input v-model="form.unitName" placeholder="请输入单元名称" />
        </el-form-item>
        <el-form-item label="是否启用" prop="delFlag">
          <el-switch
            v-model="form.delFlag"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
          >
          </el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listUnit,
  getUnit,
  delUnit,
  addUnit,
  updateUnit,
  listPatrol,
  listFill,
} from "@/api/system/unit";

export default {
  name: "Unit",
  data() {
    return {
      projectName: this.$route.query.projectName,
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
      // 巡查单元表格数据
      unitList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        delFlag: 0,
        projectId: this.$route.query.projectId,
        unitName: null,
        freedom1: null,
        freedom2: null,
        freedom3: null,
        freedom4: null,
        freedom5: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleFill(row) {
      this.$router.push({
        path: "/system/fill",
        query: {
          projectId: this.$route.query.projectId,
          projectName: this.$route.query.projectName,
          unitId: row.id,
          unitName: row.unitName,
        },
      });
    },
    handlePatrol(row) {
      this.$router.push({
        path: "/system/patrol",
        query: {
          projectId: this.$route.query.projectId,
          projectName: this.$route.query.projectName,
          unitId: row.id,
          unitName: row.unitName,
        },
      });
    },
    /** 查询巡查单元列表 */
    getList() {
      this.loading = true;
      listUnit(this.queryParams).then((response) => {
        this.unitList = response.rows;
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
        projectId: this.$route.query.projectId,
        unitName: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        freedom1: null,
        freedom2: null,
        freedom3: null,
        freedom4: null,
        freedom5: null,
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
      this.title = "添加巡查单元";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getUnit(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改巡查单元";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateUnit(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUnit(this.form).then((response) => {
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
        .confirm('是否确认删除巡查单元编号为"' + ids + '"的数据项？')
        .then(function () {
          return delUnit(ids);
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
        "system/unit/export",
        {
          ...this.queryParams,
        },
        `unit_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
