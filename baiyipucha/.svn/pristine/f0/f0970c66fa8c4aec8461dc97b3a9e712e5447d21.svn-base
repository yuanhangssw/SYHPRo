<template>
  <div class="app-container">
    <el-table
      v-loading="loading"
      :data="auditList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" />
      <el-table-column label="工程" align="center" prop="project_name" />
      <el-table-column label="巡查单元" align="center" prop="unit_name" />
      <el-table-column label="巡查类型" align="center" prop="patrol_type">
        <template slot-scope="scope">
          {{ scope.row.patrol_type == 1 ? "白蚁" : "獾狐鼠等" }}
        </template>
      </el-table-column>
      <el-table-column label="巡查人" align="center" prop="inspector_name" />
      <el-table-column label="联系电话" align="center" prop="phone" />
      <el-table-column label="巡查时间" align="center" prop="uptime" />
      <el-table-column label="数据状态" align="center" prop="dataState">
        <template slot-scope="scope">
          {{ scope.row.dataState | dataStateFilter }}
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            >处理</el-button
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

    <!-- 添加或修改审核记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="巡查详情">
          <!-- <el-input v-model="patrol" :disabled="true" /> -->
          <el-button type="primary" @click="handleopenpatrol()"
            >查看详情</el-button
          >
        </el-form-item>
        <el-form-item label="审核意见" prop="auditOpinion">
          <el-input
            v-model="form.auditOpinion"
            placeholder="请输入审核意见"
            type="textarea"
          />
        </el-form-item>
        <el-form-item label="审核状态" prop="auditStatus">
          <el-radio-group v-model="form.auditStatus">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">未通过</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 巡查详情对话框 -->
    <el-dialog
      title="巡查详情"
      :visible.sync="showPatrol"
      width="800px"
      append-to-body
    >
      <el-form :model="patrol" label-width="80px">
        <el-form-item label="巡查类型">
          <el-input v-model="patrol.patrolType" :disabled="true" />
        </el-form-item>
        <el-form-item label="巡查单元">
          <el-input v-model="patrol.patrolUnit" :disabled="true" />
        </el-form-item>
        <el-form-item label="工程名称">
          <el-input v-model="patrol.projectName" :disabled="true" />
        </el-form-item>
        <el-form-item label="断面位置">
          <el-input v-model="patrol.sectionPosition" :disabled="true" />
        </el-form-item>
        <el-form-item label="桩号">
          <el-input v-model="patrol.pileNumber" :disabled="true" />
        </el-form-item>
        <el-form-item label="轴线距离">
          <el-input v-model="patrol.axisDistance" :disabled="true" />
        </el-form-item>
        <el-form-item label="文字描述">
          <el-input v-model="patrol.description" :disabled="true" />
        </el-form-item>
        <el-form-item label="图片">
          <div v-if="patrol.list">
            <el-image
              v-for="(item, index) in patrol.list"
              :key="item.id"
              :src="url + item"
              :preview-src-list="getPreviewImageList()"
              style="
                max-width: 100px;
                max-height: 100px;
                margin-right: 10px;
                cursor: pointer;
              "
              :initial-index="index"
            >
              <div slot="placeholder" class="image-slot">
                加载中<span class="dot">...</span>
              </div>
            </el-image>
          </div>
          <div v-else>暂无图片</div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showPatrol = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listAudit,
  getAudit,
  delAudit,
  addAudit,
  updateAudit,
  getPatrolDetail,
} from "@/api/system/audit";

export default {
  name: "Audit",
  data() {
    return {
      patrol: {},
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
      // 审核记录表格数据
      auditList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        delFlag: 0,
        patrolId: null,
        auditUser: null,
        auditOpinion: null,
        auditStatus: null,
        currentDept: null,
        subordinateDept: null,
        dataState: null,
        freedom1: null,
        freedom2: null,
        freedom3: null,
        freedom4: null,
        freedom5: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        //   auditOpinion: [
        //     { required: true, message: "请输入审核意见", trigger: "blur" },
        //   ],
      },
      showPatrol: false,
      url: process.env.VUE_APP_BASE_IMG,
    };
  },
  filters: {
    dataStateFilter(dataState) {
      const statusMap = {
        1: "进行中",
        2: "流程中止",
        3: "完成",
      };
      return statusMap[dataState];
    },
  },
  created() {
    this.getList();
  },
  methods: {
    getPreviewImageList() {
      return this.patrol.list.map(
        (item) => process.env.VUE_APP_BASE_API + item
      );
    },
    handleImagePreview(index) {
      // 不使用$previewImage
    },
    handleopenpatrol() {
      this.showPatrol = true;
      const id = this.form.patrolId;
      getPatrolDetail(id).then((response) => {
        this.patrol = response.data;
        this.patrol.patrolType =
          this.patrol.patrolType === 1 ? "白蚁" : "獾狐鼠等";
        this.patrol.list = this.patrol.freedom1.split(",");
        if (this.patrol.freedom1 == "") {
          this.patrol.list = null;
        }
        // /profile/upload/2024/03/07/baiyi-small_20240307145137A007.png,/profile/upload/2024/03/07/bg-img_20240307145145A008.png,/profile/upload/2024/03/07/hhs-logo_20240307145147A009.png
      });
    },
    /** 查询审核记录列表 */
    getList() {
      this.loading = true;
      listAudit(this.queryParams).then((response) => {
        this.auditList = response.rows;
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
        patrolId: null,
        auditUser: null,
        auditOpinion: null,
        auditStatus: "1",
        currentDept: null,
        subordinateDept: null,
        dataState: null,
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
      this.title = "添加审核记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getAudit(id).then((response) => {
        this.form = response.data;
        this.form.auditStatus = "1";
        this.open = true;
        this.title = "处理审核";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateAudit(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAudit(this.form).then((response) => {
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
        .confirm('是否确认删除审核记录编号为"' + ids + '"的数据项？')
        .then(function () {
          return delAudit(ids);
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
        "system/audit/export",
        {
          ...this.queryParams,
        },
        `audit_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
