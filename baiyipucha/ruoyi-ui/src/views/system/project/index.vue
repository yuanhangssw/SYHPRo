<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane
        label="水库"
        name="first"
        style="height: 100%"
        v-loading="loading"
      >
        <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
        >
          <el-form-item label="项目名称" prop="projectName">
            <el-input
              v-model="queryParams.projectName"
              placeholder="请输入项目名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="所属机构" prop="deptId">
            <treeselect
              v-model="queryParams.deptId"
              :options="deptOptions"
              :show-count="true"
              placeholder="请输入所属机构"
              @input="handleQuery"
              style="width: 200px"
            />
          </el-form-item>

          <el-form-item label="负责人" prop="person">
            <el-input
              v-model="queryParams.person"
              placeholder="请输入负责人"
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
              v-hasPermi="['system:project:add']"
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
              v-hasPermi="['system:project:edit']"
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
              v-hasPermi="['system:project:remove']"
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
              v-hasPermi="['system:project:export']"
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
          :data="projectList"
          @selection-change="handleSelectionChange"
          height="calc(100vh - 350px)"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="项目名称" align="center" prop="projectName" />
          <el-table-column label="所属机构" align="center" prop="deptId">
            <template slot-scope="scope">
              <span>{{ scope.row.deptId | deptName(deptOptions) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="登记号"
            align="center"
            prop="registrationNumber"
          />
          <el-table-column label="工程所在地" align="center" prop="address" />
          <el-table-column
            label="水库等级"
            align="center"
            prop="reservoirGrade"
          />
          <el-table-column label="负责人" align="center" prop="person" />
          <el-table-column label="手机号" align="center" prop="personPhone" />
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <!-- 巡查单元 -->
              <el-button
                size="mini"
                type="text"
                icon="el-icon-search"
                @click="handleUnit(scope.row)"
                >巡查单元</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:project:edit']"
                >修改</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:project:remove']"
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
          style="margin-bottom: 35px"
        />
      </el-tab-pane>
      <el-tab-pane label="堤防" name="second">
        <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
        >
          <el-form-item label="项目名称" prop="projectName">
            <el-input
              v-model="queryParams.projectName"
              placeholder="请输入项目名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="所属机构" prop="deptId">
            <treeselect
              v-model="queryParams.deptId"
              :options="deptOptions"
              :show-count="true"
              placeholder="请输入所属机构"
              @input="handleQuery"
              style="width: 200px"
            />
          </el-form-item>

          <el-form-item label="负责人" prop="person">
            <el-input
              v-model="queryParams.person"
              placeholder="请输入负责人"
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
              v-hasPermi="['system:project:add']"
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
              v-hasPermi="['system:project:edit']"
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
              v-hasPermi="['system:project:remove']"
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
          :data="projectList"
          @selection-change="handleSelectionChange"
          height="calc(100vh - 350px)"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="项目名称" align="center" prop="projectName" />
          <el-table-column label="所属机构" align="center" prop="deptId">
            <template slot-scope="scope">
              <span>{{ scope.row.deptId | deptName(deptOptions) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="登记号"
            align="center"
            prop="registrationNumber"
          />
          <el-table-column label="工程所在地" align="center" prop="address" />
          <el-table-column label="堤防等级" align="center" prop="dykeGrade" />
          <el-table-column label="堤防长度" align="center" prop="dykeLength" />
          <el-table-column
            label="堤防普查长度"
            align="center"
            prop="dykePatrolLength"
          />
          <el-table-column
            label="堤防起始桩号"
            align="center"
            prop="dykePile"
          />
          <el-table-column
            label="堤防普查起始桩号"
            align="center"
            prop="dykePatrolPile"
          />
          <el-table-column label="负责人" align="center" prop="person" />
          <el-table-column label="手机号" align="center" prop="personPhone" />
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
            width="240"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-search"
                @click="handleUnit(scope.row)"
                >巡查单元</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:project:edit']"
                >修改</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:project:remove']"
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
          style="margin-bottom: 35px"
        />
      </el-tab-pane>
    </el-tabs>

    <!-- 添加或修改项目信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="140px">
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="所属机构" prop="deptId">
          <!-- <el-input v-model="form.deptId" placeholder="请输入所属机构" /> -->
          <treeselect
            v-model="form.deptId"
            :options="deptOptions"
            :show-count="true"
            placeholder="请选择归属机构"
          />
        </el-form-item>
        <el-form-item label="登记号" prop="registrationNumber">
          <el-input
            v-model="form.registrationNumber"
            placeholder="请输入登记号"
          />
        </el-form-item>
        <el-form-item label="工程所在地" prop="address">
          <el-input v-model="form.address" placeholder="请输入工程所在地" />
        </el-form-item>
        <el-form-item
          label="水库等级"
          prop="reservoirGrade"
          v-if="activeName === 'first'"
        >
          <el-input
            v-model="form.reservoirGrade"
            placeholder="请输入水库等级"
          />
        </el-form-item>
        <el-form-item
          label="堤防等级"
          prop="dykeGrade"
          v-if="activeName === 'second'"
        >
          <el-input v-model="form.dykeGrade" placeholder="请输入堤防等级" />
        </el-form-item>
        <el-form-item
          label="堤防长度"
          prop="dykeLength"
          v-if="activeName === 'second'"
        >
          <el-input v-model="form.dykeLength" placeholder="请输入堤防长度" />
        </el-form-item>
        <el-form-item
          label="堤防普查长度"
          prop="dykePatrolLength"
          v-if="activeName === 'second'"
        >
          <el-input
            v-model="form.dykePatrolLength"
            placeholder="请输入堤防普查长度"
          />
        </el-form-item>
        <el-form-item
          label="起始桩号"
          prop="dykePile"
          v-if="activeName === 'second'"
        >
          <el-input v-model="form.dykePile" placeholder="请输入堤防起始桩号" />
        </el-form-item>
        <!-- 堤防终止桩号 -->
        <el-form-item
          label="终止桩号"
          prop="dykePileEnd"
          v-if="activeName === 'second'"
        >
          <el-input v-model="form.dykePileEnd" placeholder="堤防终止桩号" />
        </el-form-item>
        <el-form-item prop="dykePatrolPile" v-if="activeName === 'second'">
          <span slot="label">
            <el-tooltip
              effect="dark"
              content="堤防普查起始桩号、终止桩号，用于生成巡查单元"
              placement="top"
            >
              <i class="el-icon-info"></i>
            </el-tooltip>
            普查起始桩号
          </span>

          <el-input
            v-model="form.dykePatrolPile"
            placeholder="请输入堤防普查起始桩号"
          />
        </el-form-item>
        <!-- 堤防普查终止桩号 -->
        <el-form-item prop="dykePatrolPileEnd" v-if="activeName === 'second'">
          <span slot="label">
            <el-tooltip
              effect="dark"
              content="堤防普查起始桩号、终止桩号，用于生成巡查单元"
              placement="top"
            >
              <i class="el-icon-info"></i>
            </el-tooltip>
            普查终止桩号
          </span>

          <el-input
            v-model="form.dykePatrolPileEnd"
            placeholder="请输入堤防普查终止桩号"
          />
        </el-form-item>
        <el-form-item label="负责人" prop="person">
          <el-input v-model="form.person" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="手机号" prop="personPhone">
          <el-input v-model="form.personPhone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-radio v-model="form.delFlag" label="0">正常</el-radio>
          <el-radio v-model="form.delFlag" label="1">删除</el-radio>
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
  listProject,
  getProject,
  delProject,
  addProject,
  updateProject,
} from "@/api/system/project";
import { deptTreeSelect } from "@/api/system/user";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "Project",
  data() {
    return {
      activeName: "first",
      deptOptions: undefined,
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
      // 项目信息表格数据
      projectList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        delFlag: 0,
        projectName: null,
        projectType: 1,
        deptId: null,
        registrationNumber: null,
        address: null,
        reservoirGrade: null,
        dykeGrade: null,
        dykeLength: null,
        dykePatrolLength: null,
        dykePile: null,
        dykePatrolPile: null,
        person: null,
        personPhone: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        projectName: [
          { required: true, message: "请输入项目名称", trigger: "blur" },
        ],
        deptId: [
          { required: true, message: "请选择所属机构", trigger: "blur" },
        ],
        registrationNumber: [
          { required: true, message: "请输入登记号", trigger: "blur" },
        ],
        address: [
          { required: true, message: "请输入工程所在地", trigger: "blur" },
        ],
        reservoirGrade: [
          { required: true, message: "请输入水库等级", trigger: "blur" },
        ],
        dykeGrade: [
          { required: true, message: "请输入堤防等级", trigger: "blur" },
        ],
        dykeLength: [
          { required: true, message: "请输入堤防长度", trigger: "blur" },
        ],
        dykePatrolLength: [
          { required: true, message: "请输入堤防普查长度", trigger: "blur" },
        ],
        dykePile: [
          { required: true, message: "请输入堤防起始桩号", trigger: "blur" },
        ],
        dykePatrolPile: [
          {
            required: true,
            message: "请输入堤防普查起始桩号",
            trigger: "blur",
          },
        ],
        person: [{ required: true, message: "请输入负责人", trigger: "blur" }],
        personPhone: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          {
            pattern: /^1[3456789]\d{9}$/,
            message: "手机号格式不正确",
            trigger: "blur",
          },
        ],
        dykePileEnd: [
          { required: true, message: "请输入堤防终止桩号", trigger: "blur" },
        ],
        dykePatrolPileEnd: [
          {
            required: true,
            message: "请输入堤防普查终止桩号",
            trigger: "blur",
          },
        ],
      },
    };
  },
  components: { Treeselect },
  created() {
    this.getList();
    this.getDeptTree();
  },
  filters: {
    deptName(deptId, deptOptions) {
      if (!deptOptions || !Array.isArray(deptOptions)) {
        return "";
      }
      function findName(deptId, deptOptions) {
        for (let i = 0; i < deptOptions.length; i++) {
          const dept = deptOptions[i];
          if (dept.id === deptId) {
            return dept.label;
          } else if (dept.children) {
            const result = findName(deptId, dept.children);
            if (result) {
              return result;
            }
          }
        }
        return "";
      }
      return findName(deptId, deptOptions);
    },
  },
  methods: {
    handleUnit(row) {
      this.$router.push({
        path: "/system/unit",
        query: { projectId: row.id, projectName: row.projectName },
      });
    },
    loadOptions(node, resolve) {
      if (node.level === 0) {
        return resolve(this.deptOptions);
      }
      if (node.level > 0) {
        return resolve([]);
      }
    },
    /** 查询机构下拉树结构 */
    getDeptTree() {
      deptTreeSelect().then((response) => {
        this.deptOptions = response.data;
      });
    },
    handleClick(tab) {
      this.activeName = tab.name;
      this.queryParams.projectType = tab.name === "first" ? 1 : 2;
      this.form.projectType = tab.name === "first" ? 1 : 2;
      this.getList();
    },
    /** 查询项目信息列表 */
    getList() {
      this.loading = true;
      if (this.$store.getters.deptId != "") {
        this.queryParams.deptId = this.$store.getters.deptId;
      }
      listProject(this.queryParams).then((response) => {
        this.projectList = response.rows;
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
        projectName: null,
        projectType: null,
        deptId: null,
        registrationNumber: null,
        address: null,
        reservoirGrade: null,
        dykeGrade: null,
        dykeLength: null,
        dykePatrolLength: null,
        dykePile: null,
        dykePatrolPile: null,
        person: null,
        personPhone: null,
        delFlag: "0",
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
      };
      if (this.activeName === "second") {
        this.form.projectType = 2;
      } else {
        this.form.projectType = 1;
      }
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
      this.title = "添加项目信息";
      if (this.activeName === "second") {
        this.form.projectType = 2;
      } else {
        this.form.projectType = 1;
      }
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getProject(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改项目信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateProject(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProject(this.form).then((response) => {
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
        .confirm('是否确认删除项目信息编号为"' + ids + '"的数据项？')
        .then(function () {
          return delProject(ids);
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
        "system/project/export",
        {
          ...this.queryParams,
        },
        `project_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
