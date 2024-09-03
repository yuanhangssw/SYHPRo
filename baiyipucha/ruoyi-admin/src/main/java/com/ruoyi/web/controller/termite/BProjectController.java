package com.ruoyi.web.controller.termite;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.domain.vo.Fujian6;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.IBPatrolUnitService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.domain.vo.MainbReservoir;
import com.ruoyi.system.domain.vo.Fujian5;
import com.ruoyi.web.controller.termite.utils.MileageUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.IBProjectService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目信息Controller
 *
 * @author ruoyi
 * @date 2024-03-05
 */
@RestController
@RequestMapping("/system/project")
public class BProjectController extends BaseController {
    @Autowired
    private IBProjectService bProjectService;
    @Autowired
    private IBPatrolUnitService bPatrolUnitService;
    @Autowired
    private ISysDeptService deptService;


    @Autowired
    private BPatrolUnitMapper bPatrolUnitMapper;

    @Autowired
    private BReservoirPreventionMapper bReservoirPreventionma;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private BDykePreventionMapper bDykePreventionMapper;

    @Autowired
    private BPatrolUnitPlaceMapper bPatrolUnitPlaceMapper;

    @Autowired
    private BProjectMapper bProjectMapper;

    /**
     * 查询项目信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:project:list')")
    @GetMapping("/list")
    public TableDataInfo list(BProject bProject) {
        startPage();
        List<BProject> list = bProjectService.selectBProjectList(bProject);
        return getDataTable(list);
    }


    /**
     * 导出项目信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:project:export')")
    @Log(title = "项目信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BProject bProject) {
        List<BProject> list = bProjectService.selectBProjectList(bProject);
        ExcelUtil<BProject> util = new ExcelUtil<BProject>(BProject.class);
        util.exportExcel(response, list, "项目信息数据");
    }

    /**
     * 获取项目信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:project:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bProjectService.selectBProjectById(id));
    }

    /**
     * 新增项目信息
     */
    @PreAuthorize("@ss.hasPermi('system:project:add')")
    @Log(title = "项目信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BProject bProject) {
        int res = bProjectService.insertBProject(bProject);
        //项目类型为堤防  普查起始桩号、终止桩号不为空生成巡查单元
        if (bProject.getProjectType() == 2) {
            if (StringUtils.isNotEmpty(bProject.getDykePatrolPileEnd()) && StringUtils.isNotEmpty(bProject.getDykePatrolPile())) {
                List<String> list = MileageUtils.mtolist1(bProject.getDykePatrolPile(), bProject.getDykePatrolPileEnd());
                if (list.size() > 0) {
                    for (String patorl : list) {
                        BPatrolUnit bPatrolUnit = new BPatrolUnit();
                        bPatrolUnit.setDelFlag("0");
                        bPatrolUnit.setProjectId(bProject.getId());
                        bPatrolUnit.setUnitName(patorl);
                        bPatrolUnit.setCreateBy(SecurityUtils.getUsername());
                        bPatrolUnit.setCreateTime(new Date());
                        bPatrolUnitService.insertBPatrolUnit(bPatrolUnit);
                    }
                }
            }
        }
        return toAjax(res);
    }

    /**
     * 修改项目信息
     */
    @Log(title = "项目信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BProject bProject) {
        BProject bProjectold = new BProject();
        bProjectold.setId(bProject.getId());
        List<BProject> oldProject = bProjectService.selectBProjectList(bProjectold);
        if (oldProject.size() > 0) {
            if (oldProject.get(0).getDykePatrolPileEnd().equals(bProject.getDykePatrolPileEnd()) && oldProject.get(0).getDykePatrolPile().equals(bProject.getDykePatrolPile())) {

            } else {
                bPatrolUnitService.deleteBPatrolUnitByProject(bProject.getId());
                if (bProject.getProjectType() == 2) {
                    List<String> list = MileageUtils.mtolist1(bProject.getDykePatrolPile(), bProject.getDykePatrolPileEnd());
                    if (list.size() > 0) {
                        for (String patorl : list) {
                            BPatrolUnit bPatrolUnit = new BPatrolUnit();
                            bPatrolUnit.setDelFlag("0");
                            bPatrolUnit.setProjectId(bProject.getId());
                            bPatrolUnit.setUnitName(patorl);
                            bPatrolUnit.setCreateBy(SecurityUtils.getUsername());
                            bPatrolUnit.setCreateTime(new Date());
                            bPatrolUnitService.insertBPatrolUnit(bPatrolUnit);
                        }
                    }
                }
                BPatrolUnit bPatrolUnit = new BPatrolUnit();
                bPatrolUnit.setDelFlag("0");
                bPatrolUnit.setProjectId(bProject.getId());
                bPatrolUnit.setCreateBy(SecurityUtils.getUsername());
                bPatrolUnit.setCreateTime(new Date());
                bPatrolUnitService.insertBPatrolUnit(bPatrolUnit);

            }
        }
        return toAjax(bProjectService.updateBProject(bProject));
    }

    /**
     * 删除项目信息
     */
    @PreAuthorize("@ss.hasPermi('system:project:remove')")
    @Log(title = "项目信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bProjectService.deleteBProjectByIds(ids));
    }

    /**
     * 获取当前部门下的项目
     *
     * @return
     */
    @GetMapping("/listbyDeptId")
    public TableDataInfo listbyDeptId() {
        SysDept sysDept = new SysDept();
        List<SysDept> listd = deptService.selectDeptList(sysDept);
        List<Long> SysDeptList = listd.stream().map(SysDept::getDeptId).collect(Collectors.toList());
        List<BProject> list = bProjectService.getBProjectByDept(SysDeptList);
        return getDataTable(list);
    }


    /**
     * 获取当前部门下的项目
     *
     * @return
     */
    @GetMapping("/listbyDeptIdList")
    public AjaxResult listbyDeptIdList() {
        Long deptId = SecurityUtils.getDeptId();
        BProject bProject = new BProject();
        bProject.setDeptId(deptId);
        List<BProject> bProjects = bProjectService.selectBProjectList(bProject);
        List list = new ArrayList();
        for (BProject project : bProjects) {
            Long id = project.getId();
            String projectName = project.getProjectName();
            Map map = new HashMap();
            map.put("projectId", id);
            map.put("projectName", projectName);
            list.add(map);
        }
        return AjaxResult.success(list);
    }

    /**
     * 获取当前项目下的单元
     *
     * @return
     */
    @GetMapping("/listbyProjectUnit")
    public AjaxResult listbyProjectUnit(Long projectId) {
        BPatrolUnit bPatrolUnit = new BPatrolUnit();
        bPatrolUnit.setProjectId(projectId);
        List<BPatrolUnit> bPatrolUnits = bPatrolUnitService.selectBPatrolUnitList(bPatrolUnit);
        List list = new ArrayList();
        for (BPatrolUnit patrolUnit : bPatrolUnits) {
            Long id = patrolUnit.getId();
            String unitName = patrolUnit.getUnitName();
            Map map = new HashMap();
            map.put("unitId", id);
            map.put("unitName", unitName);
            list.add(map);
        }
        return AjaxResult.success(list);
    }


    /**
     * 附件2 水库大坝白蚁等害堤动物危害及防治情况普查登记表
     *
     * @return
     */
    @GetMapping("/reservoirRegister")
    public void reservoirRegister(HttpServletResponse response, @RequestParam("projectId") Long projectId) {
        //定义普查方式
        List list = Arrays.asList("", "地表指示物普查", "引诱普查", "仪器普查");
        //定义坝型
        List<String> listDam = Arrays.asList("", "土石坝", "混凝土坝", "混合坝");
        //定义护坡方式
        List<String> listSlopeProtection = Arrays.asList("", "草皮护坡", "干(浆)砌石护坡", "混凝土护坡");
        //动物治理方式后边应该需要


        // 结果输出为map类型
        Map<String, Object> map = new LinkedHashMap<>();

// 1.首先查出项目基本信息  类型应该为project_type=1 水库
        BProject bProject = bProjectService.selectBProjectById(projectId);
        map.put("reservoirName", Optional.ofNullable(bProject).map(BProject::getProjectName).orElse("")); // 水库名称

// 水库管理单元 从部门表里查出来对应名字
        SysDept sysDept = sysDeptMapper.selectDeptById(Optional.ofNullable(bProject).map(BProject::getDeptId).orElse(null));
        map.put("reservoirManger", Optional.ofNullable(sysDept).map(SysDept::getDeptName).orElse("")); // 水库管理单位

        map.put("reservoirAddress", Optional.ofNullable(bProject).map(BProject::getAddress).orElse("")); // 工程所在地
        map.put("storageCapacity", Optional.ofNullable(bProject).map(bp -> bp.getStorageCapacity() + "m³").orElse("")); // 库容
        map.put("reservoirgrade", Optional.ofNullable(bProject).map(BProject::getReservoirGrade).orElse(0l)); // 工程等级
        map.put("censusMethod", list.get(Optional.ofNullable(bProject)
                .map(bp -> Optional.ofNullable(bp.getCensusMethod())
                        .map(String::valueOf)
                        .map(Integer::valueOf)
                        .orElse(0))
                .orElse(0))); // 普查方式

// 2.查找主坝与副坝 大坝就是一个单元就是一个坝。 //前端这点需要加个输入 是主坝还是副坝。
        BPatrolUnit bPatrolUnit = new BPatrolUnit();
        bPatrolUnit.setProjectId(projectId);
        List<BPatrolUnit> bPatrolUnits = bPatrolUnitMapper.selectBPatrolUnitList(bPatrolUnit);

        List<MainbReservoir> mainbReservoirs = new ArrayList<>(); //存贮主库的集合
        List<MainbReservoir> minorReservoirs = new ArrayList<>(); //存贮从库的集合
        List<Long> mainbReservoirUnits = new ArrayList<>();//存贮主库的单元id
        List<Long> minorReservoirUnits = new ArrayList<>();//存贮从库的单元id
        if (bPatrolUnits != null && !bPatrolUnits.isEmpty()) {
            for (BPatrolUnit patrolUnit : bPatrolUnits) {
                MainbReservoir mainbReservoir = new MainbReservoir();
                if (patrolUnit.getDamType() != null) {   //判断 大坝类型 避免空
                    mainbReservoir.setDamType(listDam.get(Integer.valueOf(String.valueOf(patrolUnit.getDamType()))));  //坝型
                } else {
                    mainbReservoir.setDamType(listDam.get(0));
                }
                mainbReservoir.setDamLength(patrolUnit.getDamLength());//坝长
                mainbReservoir.setDamHigth(patrolUnit.getDamHigth());//坝高
                if (patrolUnit.getSlopeProtection() != null) {
                    mainbReservoir.setSlopeProtection(listSlopeProtection.get(Integer.valueOf(String.valueOf(patrolUnit.getSlopeProtection()))));//护坡方式
                } else {
                    mainbReservoir.setSlopeProtection(listSlopeProtection.get(0));
                }

                if (patrolUnit.getFreedom1().equals("1")) { //最后这点判断是主库还是从库
                    mainbReservoirs.add(mainbReservoir);
                    mainbReservoirUnits.add(patrolUnit.getId());  //存贮主库的单元id
                } else {
                    minorReservoirs.add(mainbReservoir);
                    minorReservoirUnits.add(patrolUnit.getId());  //存贮从库的单元id
                }
            }
        }
        map.put("mainbReservoirs", mainbReservoirs);//主库集合
        map.put("minorReservoirs", minorReservoirs);//从库集合

        List<BReservoirPrevention> mainReservoirPreventionList = new ArrayList<>(); //存储主坝的白蚁危害防治情况
        List<BReservoirPrevention> minorReservoirPreventionList = new ArrayList<>(); //存储主坝的白蚁危害防治情况


        //白蚁危害及防治情况 主坝和副坝
        //白蚁危害及防治情况  遍历主坝
        if (!mainbReservoirUnits.isEmpty()) {
            for (Long mainbReservoirUnit : mainbReservoirUnits) {
                BReservoirPrevention bReservoirPrevention = new BReservoirPrevention();
                bReservoirPrevention.setProjectId(projectId);
                bReservoirPrevention.setDetrimentType("1");//危害类型 1 白蚁危害及防治情况
                bReservoirPrevention.setUnitId(mainbReservoirUnit);
                List<BReservoirPrevention> bReservoirPreventions = bReservoirPreventionma.selectBReservoirPreventionList(bReservoirPrevention);
                if (bReservoirPreventions != null && !bReservoirPreventions.isEmpty()) {
                    BReservoirPrevention mainbReservoirPrevention = bReservoirPreventions.get(0);
                    mainReservoirPreventionList.add(mainbReservoirPrevention);
                    map.put("mainReservoirPreventionList", mainReservoirPreventionList); //主坝的白蚁危害及防治情况
                }
            }
        }


        //白蚁危害及防治情况  遍历从坝
        if (!minorReservoirUnits.isEmpty()) {
            for (Long minorReservoirUnit : minorReservoirUnits) {
                BReservoirPrevention bReservoirPrevention = new BReservoirPrevention();
                bReservoirPrevention.setProjectId(projectId);
                bReservoirPrevention.setDetrimentType("1");//危害类型 1 白蚁危害及防治情况
                bReservoirPrevention.setUnitId(minorReservoirUnit);
                List<BReservoirPrevention> bReservoirPreventions = bReservoirPreventionma.selectBReservoirPreventionList(bReservoirPrevention);
                if (bReservoirPreventions != null && !bReservoirPreventions.isEmpty()) {
                    BReservoirPrevention minorbReservoirPrevention = bReservoirPreventions.get(0);
                    minorReservoirPreventionList.add(minorbReservoirPrevention);
                    map.put("minorReservoirPreventionList", minorReservoirPreventionList); //从坝 的 白蚁危害及防治情况
                }
            }
        }


        List<BReservoirPrevention> mainReservoirPreventionListOfMouse = new ArrayList<>(); //存储主坝的 獾、狐、鼠  危害防治情况
        List<BReservoirPrevention> minorReservoirPreventionListOfMouse = new ArrayList<>(); //存储主坝的 獾、狐、鼠 危害防治情况
        //獾、狐、鼠等其他动物危害及防治情况 主坝和副坝
        //獾、狐、鼠等其他动物危害及防治情况  遍历主坝
        if (!mainbReservoirUnits.isEmpty()) {
            for (Long mainbReservoirUnit : mainbReservoirUnits) {
                BReservoirPrevention bReservoirPrevention = new BReservoirPrevention();
                bReservoirPrevention.setProjectId(projectId);
                bReservoirPrevention.setDetrimentType("2");//危害类型 2 獾、狐、鼠
                bReservoirPrevention.setUnitId(mainbReservoirUnit);
                List<BReservoirPrevention> bReservoirPreventions = bReservoirPreventionma.selectBReservoirPreventionList(bReservoirPrevention);
                if (bReservoirPreventions != null && !bReservoirPreventions.isEmpty()) {
                    BReservoirPrevention mainbReservoirPrevention = bReservoirPreventions.get(0);
                    mainReservoirPreventionListOfMouse.add(mainbReservoirPrevention);
                    map.put("mainReservoirPreventionListOfMouse", mainReservoirPreventionListOfMouse); //主坝的白蚁危害及防治情况
                }
            }
        }
        //獾、狐、鼠等其他动物危害及防治情况  遍历从坝
        if (!minorReservoirUnits.isEmpty()) {
            for (Long minorReservoirUnit : minorReservoirUnits) {
                BReservoirPrevention bReservoirPrevention = new BReservoirPrevention();
                bReservoirPrevention.setProjectId(projectId);
                bReservoirPrevention.setDetrimentType("2");//危害类型 2 獾、狐、鼠
                bReservoirPrevention.setUnitId(minorReservoirUnit);
                List<BReservoirPrevention> bReservoirPreventions = bReservoirPreventionma.selectBReservoirPreventionList(bReservoirPrevention);
                if (bReservoirPreventions != null && !bReservoirPreventions.isEmpty()) {
                    BReservoirPrevention minorbReservoirPrevention = bReservoirPreventions.get(0);
                    minorReservoirPreventionListOfMouse.add(minorbReservoirPrevention);
                    map.put("minorReservoirPreventionListOfMouse", minorReservoirPreventionListOfMouse); //从坝 的 白蚁危害及防治情况
                }
            }
        }
        // 调用生成 Excel 方法
        ByteArrayInputStream byteArrayInputStream = bProjectService.setExcelValue(map);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode((String) map.get("reservoirName"), "UTF-8") + ".xlsx\"");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


        // 写入响应
        try {
            org.apache.commons.io.IOUtils.copy(byteArrayInputStream, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * 附件3 堤防工程白蚁等害堤动物危害及防治情况普登记表
     *
     * @return
     */
    @GetMapping("/dykeRegister")
    public void dykeRegister(HttpServletResponse response, @RequestParam("projectId") Long projectId) {
        // 定义普查方式
        List<String> list = Arrays.asList("", "地表指示物普查", "引诱普查", "仪器普查");

        // 1.工程的基本信息
        BProject bProject = bProjectService.selectBProjectById(projectId);
        if (bProject == null) {
            return;
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("projectName", Optional.ofNullable(bProject.getProjectName()).orElse(""));  // 提防名称
        map.put("dykeGrade", Optional.ofNullable(bProject.getDykeGrade()).orElse(null));  // 提防等级

        // 提防管理单元 从部门表里查出来对应名字
        SysDept sysDept = sysDeptMapper.selectDeptById(bProject.getDeptId());
        map.put("dykeManger", Optional.ofNullable(sysDept).map(SysDept::getDeptName).orElse(""));  // 提防管理单位
        map.put("address", Optional.ofNullable(bProject.getAddress()).orElse(""));  // 工程所在地

        Long censusMethodIndex = Optional.ofNullable(bProject.getCensusMethod()).orElse(0l);
        String censusMethod = censusMethodIndex < list.size() ? list.get(Integer.valueOf(String.valueOf(censusMethodIndex))) : "";
        map.put("censusMethod", censusMethod);  // 普查方式

        map.put("dykeLength", Optional.ofNullable(bProject.getDykeLength()).orElse(null));  // 提防长度
        map.put("dykePileStartAndEnd", Optional.ofNullable(bProject.getDykePile()).orElse("") + "-" + Optional.ofNullable(bProject.getDykePileEnd()).orElse(""));  // 提防起止桩号-终止桩号

        // 获取当前项目下的单元数
        BPatrolUnit bPatrolUnit = new BPatrolUnit();
        bPatrolUnit.setProjectId(projectId);
        List<BPatrolUnit> bPatrolUnits = bPatrolUnitService.selectBPatrolUnitList(bPatrolUnit);
        map.put("unitNumbers", bPatrolUnits.size());  // 单元数
        map.put("dykePatrolLength", Optional.ofNullable(bProject.getDykePatrolLength()).orElse(null));  // 普查长度
        map.put("dykePatrolPileStartAndEnd", Optional.ofNullable(bProject.getDykePatrolPile()).orElse("") + "-" + Optional.ofNullable(bProject.getDykePatrolPileEnd()).orElse(""));  // 普查桩号  起止桩号-终止桩号

        // 白蚁危害防治情况 type为1
        BDykePrevention bDykePrevention = new BDykePrevention();
        bDykePrevention.setProjectId(projectId);
        bDykePrevention.setDetrimentType(1L);
        List<BDykePrevention> bDykePreventions = bDykePreventionMapper.selectBDykePreventionList(bDykePrevention);

        if (!bDykePreventions.isEmpty() && bDykePreventions != null) {
            double totalDetrimentLength = bDykePreventions.stream()
                    .mapToDouble(bp -> Optional.ofNullable(bp.getDetrimentLength()).orElse(0l))
                    .sum();
            map.put("totalDetrimentLengthOfBaiYi", totalDetrimentLength);  // 危害长度总计

            double totalDetrimentLengthOne = bDykePreventions.stream()
                    .mapToDouble(bp -> Optional.ofNullable(bp.getOneDetrimentLength()).orElse(0l))
                    .sum();
            map.put("totalDetrimentLengthOneOfBaiYi", totalDetrimentLengthOne);  // I级危害长度

            double totalDetrimentLengthTwo = bDykePreventions.stream()
                    .mapToDouble(bp -> Optional.ofNullable(bp.getTwoDetrimentLength()).orElse(0l))
                    .sum();
            map.put("totalDetrimentLengthTwoOfBaiYi", totalDetrimentLengthTwo);  // II级危害长度

            double totalDetrimentLengthThree = bDykePreventions.stream()
                    .mapToDouble(bp -> Optional.ofNullable(bp.getThreeDetrimentLength()).orElse(0l))
                    .sum();
            map.put("totalDetrimentLengthThreeOfBaiYi", totalDetrimentLengthThree);  // III级危害长度

          /*
            BPatrolUnitPlace bPatrolUnitPlace = new BPatrolUnitPlace();
            bPatrolUnitPlace.setProjectId(projectId);
            List<BPatrolUnitPlace> bPatrolUnitPlaces = bPatrolUnitPlaceMapper.selectBPatrolUnitPlaceList(bPatrolUnitPlace);
            map.put("detrimentOfNumbersOfBaiYi", bPatrolUnitPlaces.size());  // 危害处数
            */

            double leaksNumbers = bDykePreventions.stream()
                    .mapToDouble(bp -> Optional.ofNullable(bp.getLeaksNumber()).orElse(0l))
                    .sum();
            map.put("leaksNumbersOfBaiYi", leaksNumbers);  // 渗漏处数

            double throughNumbers = bDykePreventions.stream()
                    .mapToDouble(bp -> Optional.ofNullable(bp.getThroughNumber()).orElse(0l))
                    .sum();
            map.put("throughNumbersOfBaiYi", throughNumbers);  // 贯穿处数

            double dropSocketNumbers = bDykePreventions.stream()
                    .mapToDouble(bp -> Optional.ofNullable(bp.getDropSocketNumber()).orElse(0l))
                    .sum();
            map.put("dropSocketNumbersOfBaiYi", dropSocketNumbers);  // 跌窝处数

            Double sumChu = leaksNumbers + throughNumbers + dropSocketNumbers;
            map.put("detrimentOfNumbersOfBaiYi", sumChu);  // 危害处数


            double nestDigging = bDykePreventions.stream()
                    .mapToDouble(bp -> Optional.ofNullable(bp.getNestDigging()).orElse(0l))
                    .sum();
            map.put("nestDiggingOfBaiYi", nestDigging);  // 挖巢数量

            double chargeAreas = bDykePreventions.stream()
                    .mapToDouble(bp -> Optional.ofNullable(bp.getChargeArea()).orElse(0l))
                    .sum();
            map.put("chargeAreasOfBaiYi", chargeAreas);  // 施药面积

            double groutingQuantitys = bDykePreventions.stream()
                    .mapToDouble(bp -> Optional.ofNullable(bp.getGroutingQuantity()).orElse(0l))
                    .sum();
            map.put("groutingQuantitysOfBaiYi", groutingQuantitys);  // 灌浆量

            // 投入资金统计
            double totalInvestCapital2023 = bDykePreventions.stream()
                    .filter(bp -> Optional.ofNullable(bp.getCreateTime()).map(time -> time.getYear() == 2023).orElse(null))
                    .mapToDouble(bp -> Optional.ofNullable(bp.getInvestCapital()).orElse(0l))
                    .sum();
            map.put("totalInvestCapital2023fBaiYi", totalInvestCapital2023);  // 投入资金2022


        }

        // 老鼠危害防治情况 type为2
        BDykePrevention bDykePreventionOfMouse = new BDykePrevention();
        bDykePreventionOfMouse.setProjectId(projectId);
        bDykePreventionOfMouse.setDetrimentType(2L);
        List<BDykePrevention> bDykePreventionsOfMouse = bDykePreventionMapper.selectBDykePreventionList(bDykePreventionOfMouse);

        double totalDetrimentLengthOfMouse = bDykePreventionsOfMouse.stream()
                .mapToDouble(bp -> Optional.ofNullable(bp.getDetrimentLength()).orElse(0l))
                .sum();
        map.put("totalDetrimentLengthOfMouse", totalDetrimentLengthOfMouse);  // 危害长度总计

        double totalDetrimentLengthOneOfMouse = bDykePreventionsOfMouse.stream()
                .mapToDouble(bp -> Optional.ofNullable(bp.getOneDetrimentLength()).orElse(0l))
                .sum();
        map.put("totalDetrimentLengthOneOfMouse", totalDetrimentLengthOneOfMouse);  // I级危害长度

        double totalDetrimentLengthTwoOfMouse = bDykePreventionsOfMouse.stream()
                .mapToDouble(bp -> Optional.ofNullable(bp.getTwoDetrimentLength()).orElse(0l))
                .sum();
        map.put("totalDetrimentLengthTwoOfMouse", totalDetrimentLengthTwoOfMouse);  // II级危害长度

        double totalDetrimentLengthThreeOfMouse = bDykePreventionsOfMouse.stream()
                .mapToDouble(bp -> Optional.ofNullable(bp.getThreeDetrimentLength()).orElse(0l))
                .sum();
        map.put("totalDetrimentLengthThreeOfMouse", totalDetrimentLengthThreeOfMouse);  // III级危害长度


        double leaksNumbersOfMouse = bDykePreventionsOfMouse.stream()
                .mapToDouble(bp -> Optional.ofNullable(bp.getLeaksNumber()).orElse(0l))
                .sum();
        map.put("leaksNumbersOfMouse", leaksNumbersOfMouse);  // 渗漏处数

        double throughNumbersOfMouse = bDykePreventionsOfMouse.stream()
                .mapToDouble(bp -> Optional.ofNullable(bp.getThroughNumber()).orElse(0l))
                .sum();
        map.put("throughNumbersOfMouse", throughNumbersOfMouse);  // 渗漏处数

        double dropSocketNumbersOfMouse = bDykePreventionsOfMouse.stream()
                .mapToDouble(bp -> Optional.ofNullable(bp.getDropSocketNumber()).orElse(0l))
                .sum();
        map.put("dropSocketNumbersOfMouse", dropSocketNumbersOfMouse);  // 跌窝处数

        double sum = leaksNumbersOfMouse + throughNumbersOfMouse + dropSocketNumbersOfMouse;
        map.put("detrimentOfNumbersOfMouse", sum);  // 危害处数


        double nestDiggingOfMouse = bDykePreventionsOfMouse.stream()
                .mapToDouble(bp -> Optional.ofNullable(bp.getNestDigging()).orElse(0l))
                .sum();
        map.put("nestDiggingOfMouse", nestDiggingOfMouse);  // 挖巢数量

        double chargeAreasOfMouse = bDykePreventionsOfMouse.stream()
                .mapToDouble(bp -> Optional.ofNullable(bp.getChargeArea()).orElse(0l))
                .sum();
        map.put("chargeAreasOfMouse", chargeAreasOfMouse);  // 施药面积


        if (bDykePreventionsOfMouse != null && !bDykePreventionsOfMouse.isEmpty()) {
            map.put("zoneTypeOfMouse", bDykePreventionsOfMouse.get(0).getZoonType());  // 动物种类
        } else {
            map.put("zoneTypeOfMouse", "");  // 动物种类

        }

        //查询普查单元
        BPatrolUnit bPatrolUnit1 = new BPatrolUnit();
        bPatrolUnit1.setProjectId(projectId);
        List<BPatrolUnit> bPatrolUnits1 = bPatrolUnitService.selectBPatrolUnitList(bPatrolUnit1);
        String unitNames = bPatrolUnits1.stream()
                .filter(patrolUnit -> patrolUnit != null && patrolUnit.getUnitName() != null)
                .map(BPatrolUnit::getUnitName)
                .collect(Collectors.joining(", "));

        map.put("puChaUnit", unitNames);  // 动物种类


        // 投入资金统计
        double totalInvestCapital2023OfMouse = bDykePreventionsOfMouse.stream()
                .filter(bp -> Optional.ofNullable(bp.getCreateTime()).map(time -> time.getYear() == 2023).orElse(false))
                .mapToDouble(bp -> Optional.ofNullable(bp.getInvestCapital()).orElse(0l))
                .sum();
        map.put("totalInvestCapital2023OfMouse", totalInvestCapital2023OfMouse);  // 投入资金2022


        ByteArrayInputStream byteArrayInputStream = bProjectService.setExcelValue3(map);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode((String) map.get("projectName"), "UTF-8") + ".xlsx\"");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


        // 写入响应
        try {
            org.apache.commons.io.IOUtils.copy(byteArrayInputStream, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

  /*  //附件5
    //水库大坝白蚁等害堤动物危害及防治情况普查明细表
    @GetMapping("/ReservoirCensusDetails")
    public AjaxResult ReservoirCensusDetails(@RequestParam("deptId") Long deptId) {
        //定义坝型
        List listDam = Arrays.asList("", "土石坝", "混凝土坝", "混合坝");

        //1.处理结果集
        Map map = new LinkedHashMap();

        //2.定位工程 这个应该是按照部门下的多个工程  比如 江苏省  江苏下边到县的几个项目
        BProject bProject = new BProject();
        bProject.setDeptId(deptId);
        List<BProject> bProjects = bProjectService.selectBProjectList(bProject);

        if (bProjects != null && !bProjects.isEmpty()) {//防空指针
            for (BProject project : bProjects) { //project肯定存在的
                map.put("projectName", project.getProjectName());  //某县  工程层面
                map.put("", project.getReservoirGrade());//工程等级

                //寻找主坝  单元层面
                BPatrolUnit bPatrolUnit = new BPatrolUnit();
                bPatrolUnit.setFreedom1("1");//1代表主坝  主坝就一个
                List<BPatrolUnit> bPatrolUnits = bPatrolUnitService.selectBPatrolUnitList(bPatrolUnit);
                BPatrolUnit mainBPatrolUnit = bPatrolUnits.get(0);//主坝单元

                if (mainBPatrolUnit != null) {
                    map.put("mainDamType", listDam.get(Optional.ofNullable(mainBPatrolUnit.getDamType())
                            .map(Long::intValue)
                            .orElse(0))); // 工程等级
                    BPatrolUnitPlace bPatrolUnitPlace = new BPatrolUnitPlace();
                    bPatrolUnitPlace.setPatrolUnit(mainBPatrolUnit.getId());
                    List<BPatrolUnitPlace> bPatrolUnitPlaces = bPatrolUnitPlaceMapper.selectBPatrolUnitPlaceList(bPatrolUnitPlace);
                    map.put("MainDetrimentOfNumbers", bPatrolUnitPlaces.size());  //主坝危害处数
                }
                map.put("detrimentOfGrade", mainBPatrolUnit.getFreedom2());
                //寻找从坝
                bPatrolUnit.setFreedom1("2");//2代表从坝   单元层面
                List<BPatrolUnit> minorPatrolUnits = bPatrolUnitService.selectBPatrolUnitList(bPatrolUnit);
                map.put("minorTotalNumbers", minorPatrolUnits.size());  //副坝总数

                //水库防治表里无 一级 二级 三级 危害  暂时省略
                if (minorPatrolUnits != null && !minorPatrolUnits.isEmpty()) {//遍历每一个副坝
                    int sum = 0;
                    for (BPatrolUnit minorPatrolUnit : minorPatrolUnits) {
                        BPatrolUnitPlace bPatrolUnitPlace = new BPatrolUnitPlace();
                        bPatrolUnitPlace.setPatrolUnit(minorPatrolUnit.getId());
                        List<BPatrolUnitPlace> bPatrolUnitPlaces = bPatrolUnitPlaceMapper.selectBPatrolUnitPlaceList(bPatrolUnitPlace);
                        sum += bPatrolUnitPlaces.size();
                    }
                    map.put("minorDetrimentOfNumbers", sum);
                }


            }

        }
        return AjaxResult.success(map);
    }*/

    /*
     *
     * 附件5 水库大坝白蚁等害堤动物危害及防治情况普查明细表（reservoirDetails）
     *
     * @return*/

    @GetMapping("/reservoirDetails")
    public void reservoirDetails(HttpServletResponse response, @RequestParam("sysDeptId") Long sysDeptId) {
        //定义普查方式
        List list = Arrays.asList("", "地表指示物普查", "引诱普查", "仪器普查");
        //定义坝型
        List<String> listDam = Arrays.asList("", "土石坝", "混凝土坝", "混合坝");
        //定义护坡方式
        List<String> listSlopeProtection = Arrays.asList("", "草皮护坡", "干(浆)砌石护坡", "混凝土护坡");
        //动物治理方式后边应该需要

        //拿到部门Id直接去查对应的项目ID
        BProject bProject = new BProject();
        bProject.setDeptId(sysDeptId);
        List<BProject> bProjects = bProjectService.selectBProjectList(bProject);
        Map projecMaps = new LinkedHashMap();
        //寻找该级的某某局
        SysDept sysDept = sysDeptMapper.selectDeptById(sysDeptId);
        projecMaps.put("deptName", sysDept.getDeptName());
        int i = 1;
        if (bProjects != null && !bProjects.isEmpty()) {  ///遍历该部门下所有的项目
            for (BProject project : bProjects) {
                String projectName = project.getProjectName();
                if (projectName != null) {   //只保留水库
                    if (projectName.contains("水库") || projectName.contains("堤坝")) {
                        Map<String, Object> map = new LinkedHashMap<>();
                        //存一下  行政单位  某某县，某某局

                        map.put("reservoirName", Optional.ofNullable(project).map(BProject::getProjectName).orElse("")); // 水库名称
                        map.put("reservoirgrade", Optional.ofNullable(project).map(BProject::getReservoirGrade).orElse(0l)); // 工程等级
                        BPatrolUnit bPatrolUnit = new BPatrolUnit();
                        bPatrolUnit.setFreedom1("1");   //1代表主坝  主坝就一个单元
                        bPatrolUnit.setProjectId(project.getId());
                        List<BPatrolUnit> bPatrolUnits = bPatrolUnitMapper.selectBPatrolUnitList(bPatrolUnit);
                        if (bPatrolUnits != null && !bPatrolUnits.isEmpty()) {
                            BPatrolUnit bPatrolUnit1 = bPatrolUnits.get(0);
                            //主坝
                            map.put("mainReservoirDamType", listDam.get(Integer.valueOf(String.valueOf(Optional.ofNullable(bPatrolUnit1.getDamType()).orElse(0l)))));//主坝坝型
                            map.put("", "");//危害处数无法统计
                            map.put("mainReservoirBadGrade", bPatrolUnit1.getFreedom2());//危害等级
                        }

                        BPatrolUnit minorBPatrolUnit = new BPatrolUnit();
                        minorBPatrolUnit.setFreedom1("2");
                        minorBPatrolUnit.setProjectId(project.getId());
                        List<BPatrolUnit> minorBPatrolUnits = bPatrolUnitMapper.selectBPatrolUnitList(minorBPatrolUnit);
                        long oneDetrimentLength = 0l;
                        long twoDetrimentLength = 0l;
                        long threeDetrimentLength = 0l;
                        long oneDetrimentLengthOfMouse = 0l;
                        long twoDetrimentLengthOfMouse = 0l;
                        long threeDetrimentLengthOfMouse = 0l;

                        if (minorBPatrolUnits != null && !minorBPatrolUnits.isEmpty()) {
                            map.put("minorTotal", minorBPatrolUnits.size());//总数
                            for (BPatrolUnit patrolUnit : minorBPatrolUnits) {
                                BReservoirPrevention bReservoirPrevention = new BReservoirPrevention();
                                bReservoirPrevention.setUnitId(patrolUnit.getId());
                                List<BReservoirPrevention> reservoirPreventionList = bReservoirPreventionma.selectBReservoirPreventionList(bReservoirPrevention);
                                if (reservoirPreventionList != null && !reservoirPreventionList.isEmpty()) {
                                    for (BReservoirPrevention bReservoirPrevention1 : reservoirPreventionList) {
                                        oneDetrimentLength += bReservoirPrevention1.getOneDetrimentLength();
                                        twoDetrimentLength += bReservoirPrevention1.getTwoDetrimentLength();
                                        threeDetrimentLength += bReservoirPrevention1.getThreeDetrimentLength();
                                    }
                                }
                                BReservoirPrevention bReservoirPreventions2 = new BReservoirPrevention();
                                bReservoirPreventions2.setDetrimentType("2");
                                bReservoirPreventions2.setUnitId(patrolUnit.getId());
                                List<BReservoirPrevention> bDykePreventions = bReservoirPreventionma.selectBReservoirPreventionList(bReservoirPreventions2);
                                if (bDykePreventions != null && !bDykePreventions.isEmpty()) {
                                    for (BReservoirPrevention bDykePrevention : bDykePreventions) {
                                        oneDetrimentLengthOfMouse += bDykePrevention.getOneDetrimentLength();
                                        twoDetrimentLengthOfMouse += bDykePrevention.getTwoDetrimentLength();
                                        threeDetrimentLengthOfMouse += bDykePrevention.getThreeDetrimentLength();
                                    }
                                }


                            }
                        }
                        map.put("oneDetrimentLength", oneDetrimentLength);//副坝一级危害总数
                        map.put("twoDetrimentLength", twoDetrimentLength);//副坝二级危害总数
                        map.put("threeDetrimentLength", threeDetrimentLength);//副坝三级危害总数
                        map.put("totalDetrimentLength", oneDetrimentLength + twoDetrimentLength + threeDetrimentLength);//总计危害处数

                        map.put("oneDetrimentLengthOfMouse", oneDetrimentLengthOfMouse);//副坝mouse一级危害总数
                        map.put("twoDetrimentLengthOfMouse", twoDetrimentLengthOfMouse);//副坝mouse二级危害总数
                        map.put("threeDetrimentLengthOfMouse", threeDetrimentLengthOfMouse);//副坝mouse三级危害总数
                        map.put("totalDetrimentLengthOfMouse", oneDetrimentLengthOfMouse + twoDetrimentLengthOfMouse + threeDetrimentLengthOfMouse);//总计危害处数


                        //再查一次，不区分主坝副坝
                        BReservoirPrevention bReservoirPrevention = new BReservoirPrevention();
                        bReservoirPrevention.setProjectId(project.getId());
                        bReservoirPrevention.setDetrimentType("1");//危害类型 1 白蚁危害及防治情况
                        List<BReservoirPrevention> mainAndMinorbReservoirPreventions = bReservoirPreventionma.selectBReservoirPreventionList(bReservoirPrevention);
                        if (mainAndMinorbReservoirPreventions != null && !mainAndMinorbReservoirPreventions.isEmpty()) {
                            long leaksNumberOfBaiyi = mainAndMinorbReservoirPreventions.stream().mapToLong(BReservoirPrevention::getLeaksNumber).sum();
                            map.put("leaksNumberOfBaiyi", leaksNumberOfBaiyi);//渗透处数
                            long throughNumberOfBaiyi = mainAndMinorbReservoirPreventions.stream().mapToLong(BReservoirPrevention::getThroughNumber).sum();
                            map.put("throughNumberOfBaiyi", throughNumberOfBaiyi);//穿坝处数
                            long dropSocketNumberOfBaiyi = mainAndMinorbReservoirPreventions.stream().mapToLong(BReservoirPrevention::getDropSocketNumber).sum();
                            map.put("dropSocketNumberOfBaiyi", dropSocketNumberOfBaiyi);//跌窝处数
                            long nestDiggingOfBaiyi = mainAndMinorbReservoirPreventions.stream().mapToLong(BReservoirPrevention::getNestDigging).sum();
                            map.put("nestDiggingOfBaiyi", nestDiggingOfBaiyi);//挖巢数量
                            long chargeAreaOfBaiyi = mainAndMinorbReservoirPreventions.stream().mapToLong(BReservoirPrevention::getChargeArea).sum();
                            map.put("chargeAreaOfBaiyi", chargeAreaOfBaiyi);//施药面积
                            long groutingQuantity = mainAndMinorbReservoirPreventions.stream().mapToLong(BReservoirPrevention::getGroutingQuantity).sum();
                            map.put("groutingQuantity", groutingQuantity);//灌浆量
                            long investCapital = mainAndMinorbReservoirPreventions.stream().mapToLong(BReservoirPrevention::getInvestCapital).sum();
                            map.put("investCapital", investCapital);//投入资金
                        }


                        BReservoirPrevention bReservoirPreventionOfMouse = new BReservoirPrevention();
                        bReservoirPreventionOfMouse.setProjectId(project.getId());
                        bReservoirPreventionOfMouse.setDetrimentType("2");//危害类型 2   危害及防治情况  獾、狐、鼠等其他动物危害及防治情况
                        List<BReservoirPrevention> mainAndMinorbReservoirPreventionsOfMouse = bReservoirPreventionma.selectBReservoirPreventionList(bReservoirPreventionOfMouse);
                        if (mainAndMinorbReservoirPreventionsOfMouse != null && !mainAndMinorbReservoirPreventionsOfMouse.isEmpty()) {
                            long leaksNumberOfMouse = mainAndMinorbReservoirPreventionsOfMouse.stream().mapToLong(BReservoirPrevention::getLeaksNumber).sum();
                            map.put("leaksNumberOfMouse", leaksNumberOfMouse);//渗透处数
                            long throughNumberOfMouse = mainAndMinorbReservoirPreventionsOfMouse.stream().mapToLong(BReservoirPrevention::getThroughNumber).sum();
                            map.put("throughNumberOfMouse", throughNumberOfMouse);//穿坝处数
                            long dropSocketNumberOfMouse = mainAndMinorbReservoirPreventionsOfMouse.stream().mapToLong(BReservoirPrevention::getDropSocketNumber).sum();
                            map.put("dropSocketNumberOfMouse", dropSocketNumberOfMouse);//跌窝处数
                            long nestDiggingOfMouse = mainAndMinorbReservoirPreventionsOfMouse.stream().mapToLong(BReservoirPrevention::getNestDigging).sum();
                            map.put("nestDiggingOfMouse", nestDiggingOfMouse);//挖巢数量
                            long chargeAreaOfMouse = mainAndMinorbReservoirPreventionsOfMouse.stream().mapToLong(BReservoirPrevention::getChargeArea).sum();
                            map.put("chargeAreaOfMouse", chargeAreaOfMouse);//施药面积
                            long groutingQuantityOfMouse = mainAndMinorbReservoirPreventionsOfMouse.stream().mapToLong(BReservoirPrevention::getGroutingQuantity).sum();
                            map.put("groutingQuantityOfMouse", groutingQuantityOfMouse);//灌浆量

                            long zoonGovernNumber = mainAndMinorbReservoirPreventionsOfMouse.stream().mapToLong(BReservoirPrevention::getZoonGovernNumber).sum();
                            map.put("zoonGovernNumber", zoonGovernNumber);//动物治理数量

                            long investCapitalOfMouse = mainAndMinorbReservoirPreventionsOfMouse.stream().mapToLong(BReservoirPrevention::getInvestCapital).sum();
                            map.put("investCapitalOfMouse", investCapitalOfMouse);//投入资金
                        }

                        projecMaps.put(i, map);
                        i++;
                    }
                }
            }
        }

        // 调用生成 Excel 方法
        ByteArrayInputStream byteArrayInputStream = bProjectService.setExcelValueFive(projecMaps);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode((String) projecMaps.get("deptName"), "UTF-8") + ".xlsx\"");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


        // 写入响应
        try {
            org.apache.commons.io.IOUtils.copy(byteArrayInputStream, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    /*
     *
     * 附件6 水库大坝白蚁等害堤动物危害及防治情况普查汇总表  (传入的sysDeptId为  广西省，适用于某省的Id，这样才能展示其下属部门，比如说南宁市水利局、桂林市水利局、梧州市水利局......)
     *   大体数据返回框架内容及数据参考:  C:\Users\pingz.DESKTOP-4LNNVLR\Desktop\附件6数据返回类型.txt
     * @return*/
    @GetMapping("/reservoirPuChaDetails")
    public void reservoirPuChaDetails(HttpServletResponse response, @RequestParam("sysDeptId") Long sysDeptId) {
        // 定义普查方式
        List<String> list = Arrays.asList("", "地表指示物普查", "引诱普查", "仪器普查");
        // 定义坝型
        List<String> listDam = Arrays.asList("", "土石坝", "混凝土坝", "混合坝");
        // 定义护坡方式
        List<String> listSlopeProtection = Arrays.asList("", "草皮护坡", "干(浆)砌石护坡", "混凝土护坡");

        // 附件6 分类 白蚁 鼠
        // 白蚁 分为主坝 副坝 ，狐鼠分为 主坝 副坝
        // 1.查到leader
        SysDept sysDept = new SysDept();
        sysDept.setParentId(sysDeptId);
        List<SysDept> sysDepts = sysDeptMapper.selectDeptList(sysDept);

        // 检查 sysDepts 是否为空或为空列表
        if (sysDepts == null || sysDepts.isEmpty()) {
            return;
        }

        // 1.包含 南宁市水利局、桂林市水利局、梧州市水利局、北海市水利局、防城港市水利局、钦州市水利局、贵港市水利局、玉林市水利局、百色市水利局、贺州市水利局、河池市水利局、来宾市水利局、崇左市水利局、柳州市水利局、梧州市水利局、玉林市水利局、百色市水利局、贺州市水利局、河池市水利局
        List<Map<String, Object>> mapList = new LinkedList();

        for (SysDept dept : sysDepts) {
            Map<String, Object> map = new LinkedHashMap<>();
            String leader = dept.getLeader();
            map.put("leader", leader);  // 加入leader 、广西省水利厅

            int i = 1;
            Long deptId = dept.getDeptId();

            // 检查 deptId 是否为空
            if (deptId == null) {
                continue;
            }

            SysDept sysDeptBy = sysDeptMapper.selectDeptById(deptId);

            // 检查 sysDeptBy 是否为空
            if (sysDeptBy == null) {
                continue;
            }

            String deptName = sysDeptBy.getDeptName();
            Fujian5 fujian5 = new Fujian5();
            fujian5.setDeptName(dept.getDeptName());  // 南宁市水利局名字

            // 白蚁
            // 工程总数
            int count = bProjectMapper.selectNumberBydeprtId(deptId);
            fujian5.setTotalProjects(count);

            // 普查完成数
            int countPubcha = bProjectMapper.selectProjectOfCompleteNumbers(deptId);
            fujian5.setCompletedSurveys(countPubcha);

            // 投入资金
            double investment2023 = 0;

            // 主坝
            BProject bProject = new BProject();
            bProject.setDeptId(deptId);
            bProject.setFreedom1("1");
            List<BProject> bProjects = bProjectMapper.selectBProjectList(bProject);

            if (bProjects != null) {
                Double level1Hazards = 0.0;
                Double level2Hazards = 0.0;
                Double level3Hazards = 0.0;

                for (BProject project : bProjects) {
                    BDykePrevention bDykePrevention = new BDykePrevention();
                    bDykePrevention.setProjectId(project.getId());
                    bDykePrevention.setDetrimentType(1L);
                    List<BDykePrevention> bDykePreventions = bDykePreventionMapper.selectBDykePreventionList(bDykePrevention);

                    if (bDykePreventions != null) {
                        level1Hazards += bDykePreventions.stream()
                                .filter(Objects::nonNull)
                                .mapToDouble(dp -> dp.getOneDetrimentLength() != null ? dp.getOneDetrimentLength() : 0.0)
                                .sum();


                        level2Hazards += bDykePreventions.stream()
                                .filter(Objects::nonNull)
                                .mapToDouble(dp -> dp.getTwoDetrimentLength() != null ? dp.getTwoDetrimentLength() : 0.0)
                                .sum();
                        level3Hazards += bDykePreventions.stream()
                                .filter(Objects::nonNull)
                                .mapToDouble(dp -> dp.getThreeDetrimentLength() != null ? dp.getThreeDetrimentLength() : 0.0)
                                .sum();
                        investment2023 += bDykePreventions.stream()
                                .filter(Objects::nonNull)
                                .mapToDouble(dp -> dp.getInvestCapital() != null ? dp.getInvestCapital() : 0.0)
                                .sum();
                    }
                }
                fujian5.setMainDamLevel1Hazards(level1Hazards.intValue());
                fujian5.setMainDamLevel2Hazards(level2Hazards.intValue());
                fujian5.setMainDamLevel3Hazards(level3Hazards.intValue());
            }

            // 副坝
            BProject minorbProject = new BProject();
            minorbProject.setDeptId(deptId);
            minorbProject.setFreedom1("2");
            List<BProject> minorbProjects = bProjectMapper.selectBProjectList(minorbProject);

            if (minorbProjects != null) {
                Double minorlevel1Hazards = 0.0;
                Double minorlevel2Hazards = 0.0;
                Double minorlevel3Hazards = 0.0;

                for (BProject project : minorbProjects) {
                    BDykePrevention bDykePrevention = new BDykePrevention();
                    bDykePrevention.setProjectId(project.getId());
                    bDykePrevention.setDetrimentType(1L);
                    List<BDykePrevention> bDykePreventions = bDykePreventionMapper.selectBDykePreventionList(bDykePrevention);

                    if (bDykePreventions != null) {
                        minorlevel1Hazards += bDykePreventions.stream()
                                .filter(Objects::nonNull)
                                .mapToDouble(dp -> dp.getOneDetrimentLength() != null ? dp.getOneDetrimentLength() : 0.0)
                                .sum();
                        minorlevel2Hazards += bDykePreventions.stream()
                                .filter(Objects::nonNull)
                                .mapToDouble(dp -> dp.getTwoDetrimentLength() != null ? dp.getTwoDetrimentLength() : 0.0)
                                .sum();
                        minorlevel3Hazards += bDykePreventions.stream()
                                .filter(Objects::nonNull)
                                .mapToDouble(dp -> dp.getThreeDetrimentLength() != null ? dp.getThreeDetrimentLength() : 0.0)
                                .sum();
                        investment2023 += bDykePreventions.stream()
                                .filter(Objects::nonNull)
                                .mapToDouble(dp -> dp.getInvestCapital() != null ? dp.getInvestCapital() : 0.0)
                                .sum();
                    }
                }
                fujian5.setAuxiliaryDamTotal(minorlevel1Hazards.intValue() + minorlevel2Hazards.intValue() + minorlevel3Hazards.intValue());
                fujian5.setAuxiliaryDamLevel1Hazards(minorlevel1Hazards.intValue());
                fujian5.setAuxiliaryDamLevel2Hazards(minorlevel2Hazards.intValue());
                fujian5.setAuxiliaryDamLevel3Hazards(minorlevel3Hazards.intValue());
            }
            fujian5.setInvestment2023(investment2023);
            map.put("Baiyi", fujian5);

            // 獾狐鼠

            // 投入资金
            double investment2023OfMouse = 0;

            // 主坝
            BProject bProjectOfMouse = new BProject();
            bProjectOfMouse.setDeptId(deptId);
            bProjectOfMouse.setFreedom1("1");
            List<BProject> bProjectsOfMouse = bProjectMapper.selectBProjectList(bProjectOfMouse);

            if (bProjectsOfMouse != null) {
                Double level1HazardsOfMouse = 0.0;
                Double level2HazardsOfMouse = 0.0;
                Double level3HazardsOfMouse = 0.0;

                for (BProject project : bProjectsOfMouse) {
                    BDykePrevention bDykePrevention = new BDykePrevention();
                    bDykePrevention.setProjectId(project.getId());
                    bDykePrevention.setDetrimentType(2L);
                    List<BDykePrevention> bDykePreventions = bDykePreventionMapper.selectBDykePreventionList(bDykePrevention);

                    if (bDykePreventions != null) {
                        level1HazardsOfMouse += bDykePreventions.stream()
                                .filter(Objects::nonNull)
                                .mapToDouble(dp -> dp.getOneDetrimentLength() != null ? dp.getOneDetrimentLength() : 0.0)
                                .sum();
                        level2HazardsOfMouse += bDykePreventions.stream()
                                .filter(Objects::nonNull)
                                .mapToDouble(dp -> dp.getTwoDetrimentLength() != null ? dp.getTwoDetrimentLength() : 0.0)
                                .sum();
                        level3HazardsOfMouse += bDykePreventions.stream()
                                .filter(Objects::nonNull)
                                .mapToDouble(dp -> dp.getThreeDetrimentLength() != null ? dp.getThreeDetrimentLength() : 0.0)
                                .sum();
                        investment2023OfMouse += bDykePreventions.stream()
                                .filter(Objects::nonNull)
                                .mapToDouble(dp -> dp.getInvestCapital() != null ? dp.getInvestCapital() : 0.0)
                                .sum();
                    }
                }
                fujian5.setMainDamLevel1Hazards(level1HazardsOfMouse.intValue());
                fujian5.setMainDamLevel2Hazards(level2HazardsOfMouse.intValue());
                fujian5.setMainDamLevel3Hazards(level3HazardsOfMouse.intValue());
                fujian5.setInvestment2023(investment2023OfMouse);
            }

            // 副坝
            BProject minorbProjectOfMouse = new BProject();
            minorbProjectOfMouse.setDeptId(deptId);
            minorbProjectOfMouse.setFreedom1("2");
            List<BProject> minorbProjectsOfMouse = bProjectMapper.selectBProjectList(minorbProjectOfMouse);

            if (minorbProjectsOfMouse != null) {
                Double minorlevel1HazardsOfMouse = 0.0;
                Double minorlevel2HazardsOfMouse = 0.0;
                Double minorlevel3HazardsOfMouse = 0.0;

                for (BProject project : minorbProjectsOfMouse) {
                    BDykePrevention bDykePrevention = new BDykePrevention();
                    bDykePrevention.setProjectId(project.getId());
                    bDykePrevention.setDetrimentType(2L);
                    List<BDykePrevention> bDykePreventions = bDykePreventionMapper.selectBDykePreventionList(bDykePrevention);

                    if (bDykePreventions != null) {
                        minorlevel1HazardsOfMouse += bDykePreventions.stream()
                                .filter(Objects::nonNull)
                                .mapToDouble(dp -> dp.getOneDetrimentLength() != null ? dp.getOneDetrimentLength() : 0.0)
                                .sum();
                        minorlevel2HazardsOfMouse += bDykePreventions.stream()
                                .filter(Objects::nonNull)
                                .mapToDouble(dp -> dp.getTwoDetrimentLength() != null ? dp.getTwoDetrimentLength() : 0.0)
                                .sum();
                        minorlevel3HazardsOfMouse += bDykePreventions.stream()
                                .filter(Objects::nonNull)
                                .mapToDouble(dp -> dp.getThreeDetrimentLength() != null ? dp.getThreeDetrimentLength() : 0.0)
                                .sum();
                        investment2023OfMouse += bDykePreventions.stream()
                                .filter(Objects::nonNull)
                                .mapToDouble(dp -> dp.getInvestCapital() != null ? dp.getInvestCapital() : 0.0)
                                .sum();
                    }
                }
                fujian5.setAuxiliaryDamTotal(minorlevel1HazardsOfMouse.intValue() + minorlevel2HazardsOfMouse.intValue() + minorlevel3HazardsOfMouse.intValue());
                fujian5.setAuxiliaryDamLevel1Hazards(minorlevel1HazardsOfMouse.intValue());
                fujian5.setAuxiliaryDamLevel2Hazards(minorlevel2HazardsOfMouse.intValue());
                fujian5.setAuxiliaryDamLevel3Hazards(minorlevel3HazardsOfMouse.intValue());
            }
            fujian5.setInvestment2023(investment2023OfMouse);
            map.put("HuanHuShu", fujian5);
            mapList.add(map);
        }


        ByteArrayInputStream byteArrayInputStream = bProjectService.setExcelValueSix(mapList);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode((String) mapList.get(0).get("leader"), "UTF-8") + ".xlsx\"");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


        // 写入响应
        try {
            org.apache.commons.io.IOUtils.copy(byteArrayInputStream, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }




    /*
     *
     * 附件7 堤防工程白蚁等害堤动物危害及防治情况普查明细表
     *两大类：1.白蚁危害 2.獾狐鼠
     *这个直接查  堤防表
     根据determinType的不同区分为白蚁和獾狐鼠          遍历的是每个部门，给我的是部门ID
     */

    @GetMapping("/reservoirDetailsOfSeven")
    public void reservoirDetailsOfSeven(HttpServletResponse response, @RequestParam("sysDeptId") Long sysDeptId) {
        //定义普查方式
        List list = Arrays.asList("", "地表指示物普查", "引诱普查", "仪器普查");
        //定义坝型
        List<String> listDam = Arrays.asList("", "土石坝", "混凝土坝", "混合坝");
        //定义护坡方式
        List<String> listSlopeProtection = Arrays.asList("", "草皮护坡", "干(浆)砌石护坡", "混凝土护坡");



        SysDept sysDeptGuangxi = sysDeptMapper.selectDeptById(sysDeptId);
        String deptName = sysDeptGuangxi.getDeptName();//1.获取 广西省水利厅

        //广西省水利厅 部门ID101 下属部门
        SysDept sysDept = new SysDept();
        sysDept.setParentId(sysDeptId);
        List<SysDept> sysDepts = sysDeptMapper.selectDeptList(sysDept);

        // 检查 sysDepts 是否为空或为空列表
        if (sysDepts == null || sysDepts.isEmpty()) {
            return  ;
        }
        List<Map> listResult = new LinkedList();
        for (SysDept dept : sysDepts) {
            Map map1 = new LinkedHashMap();

            //广西省水利厅加进来
            map1.put("deptName",deptName);

            Long deptId = dept.getDeptId();   //部门南宁市水利局Id

            //获取当前部门的名字
            SysDept sysDeptNanning = sysDeptMapper.selectDeptById(deptId);
            String deptNameOfNanning = sysDeptNanning.getDeptName();
            map1.put("deptNameOfMinor",deptNameOfNanning);   //南宁市水利局
            List projectList=new LinkedList();


            //根据南宁部门ID去查下属项目
            BProject bProject=new BProject();
            bProject.setDeptId(deptId);
            bProject.setProjectType(2l);   //这里的2代表堤坝
            List<BProject> bProjects = bProjectMapper.selectBProjectList(bProject);
            if (bProjects != null&&bProjects.size()>0) {
                for (BProject bProject1 : bProjects) {
                    Map projetMap=new LinkedHashMap();
                    Fujian6 fujian6=new Fujian6();
                    String projectName = bProject1.getProjectName();
                    fujian6.setDykeName(projectName);  //南宁提防  --项目名   动作：根据项目ID去危害防治表中查对应的数据
                    fujian6.setDykeLevel(String.valueOf(bProject1.getDykeGrade()));
                    fujian6.setDykeLength(bProject1.getDykeLength());

                    //该项目有多少单元
                    int count=bPatrolUnitMapper.selectUnitNumbers(bProject1.getId());
                    fujian6.setUnitCount(count);

                    //危害长度总计
                    int totalHazardLength=0;
                    int seepageCount=0;
                    int penetrationCount=0;
                    int sinkholeCount=0;
                    int nestCount=0;
                    int medicationArea=0;
                    int groutingLength=0;
                    int invest_capital=0;
                    BDykePrevention bDykePrevention=new BDykePrevention();
                    bDykePrevention.setProjectId(bProject1.getId());
                    bDykePrevention.setDetrimentType(1l);   // 这里代表的是 危害类型1，  白蚁危害
                    List<BDykePrevention> bDykePreventions = bDykePreventionMapper.selectBDykePreventionList(bDykePrevention);
                    if (bDykePreventions != null&&bDykePreventions.size()>0){
                        double sum = bDykePreventions.stream().mapToDouble(bp -> bp.getDetrimentLength() != null ? bp.getDetrimentLength() : 0.0).sum();
                        totalHazardLength+=sum;
                        double sum1 = bDykePreventions.stream().mapToDouble(bp -> bp.getLeaksNumber() != null ? bp.getLeaksNumber() : 0.0).sum();
                        seepageCount+=sum1;
                        double sum2 = bDykePreventions.stream().mapToDouble(bp -> bp.getThroughNumber() != null ? bp.getThroughNumber() : 0.0).sum();
                        penetrationCount+=sum2;
                        double sum3 = bDykePreventions.stream().mapToDouble(bp -> bp.getDropSocketNumber() != null ? bp.getDropSocketNumber() : 0.0).sum();
                        sinkholeCount+=sum3;

                        double sum4 = bDykePreventions.stream().mapToDouble(bp -> bp.getNestDigging() != null ? bp.getNestDigging() : 0.0).sum();
                        nestCount+=sum4;
                        double sum5 = bDykePreventions.stream().mapToDouble(bp -> bp.getChargeArea() != null ? bp.getChargeArea() : 0.0).sum();
                        medicationArea+=sum5;
                        double sum6 = bDykePreventions.stream().mapToDouble(bp -> bp.getGroutingQuantity() != null ? bp.getGroutingQuantity() : 0.0).sum();
                        groutingLength+=sum6;
                        double sum7 = bDykePreventions.stream().mapToDouble(bp -> bp.getInvestCapital() != null ? bp.getInvestCapital() : 0.0).sum();
                        invest_capital+=sum7;


                    }
                    fujian6.setTotalHazardLength(totalHazardLength);
                    fujian6.setLevel1UnitCount(1);
                    fujian6.setLevel2UnitCount(2);
                    fujian6.setLevel3UnitCount(4);
                    fujian6.setSeepageCount(seepageCount);
                    fujian6.setPenetrationCount(penetrationCount);
                    fujian6.setSinkholeCount(sinkholeCount);
                    fujian6.setNestCount(nestCount);
                    fujian6.setMedicationArea(medicationArea);
                    fujian6.setGroutingLength(groutingLength);
                    fujian6.setInvestment2023(invest_capital);
                    fujian6.setHazardCount(seepageCount+penetrationCount+sinkholeCount);

                    int totalHazardLengthOfMouse=0;
                    int seepageCountOfMouse=0;
                    int penetrationCountOfMouse=0;
                    int sinkholeCountOfMouse=0;
                    int nestCountOfMouse=0;
                    int medicationAreaOfMouse=0;
                    int groutingLengthOfMouse=0;
                    int invest_capitalOfMouse=0;
                    BDykePrevention bDykePreventionOfMouse=new BDykePrevention();
                    bDykePreventionOfMouse.setProjectId(bProject1.getId());
                    bDykePreventionOfMouse.setDetrimentType(2l);   // 这里代表的是 危害类型2， 獾狐鼠
                    List<BDykePrevention> bDykePreventionsOfMouse = bDykePreventionMapper.selectBDykePreventionList(bDykePreventionOfMouse);
                    if (bDykePreventionsOfMouse != null&&bDykePreventionsOfMouse.size()>0){
                        double sumOfMouse = bDykePreventionsOfMouse.stream().mapToDouble(bp -> bp.getDetrimentLength() != null ? bp.getDetrimentLength() : 0.0).sum();
                        totalHazardLengthOfMouse+=sumOfMouse;
                        double sum1OfMouse = bDykePreventionsOfMouse.stream().mapToDouble(bp -> bp.getLeaksNumber() != null ? bp.getLeaksNumber() : 0.0).sum();
                        seepageCountOfMouse+=sum1OfMouse;
                        double sum2OfMouse = bDykePreventionsOfMouse.stream().mapToDouble(bp -> bp.getThroughNumber() != null ? bp.getThroughNumber() : 0.0).sum();
                        penetrationCountOfMouse+=sum2OfMouse;
                        double sum3OfMouse = bDykePreventionsOfMouse.stream().mapToDouble(bp -> bp.getDropSocketNumber() != null ? bp.getDropSocketNumber() : 0.0).sum();
                        sinkholeCountOfMouse+=sum3OfMouse;

                        double sum4OfMouse = bDykePreventionsOfMouse.stream().mapToDouble(bp -> bp.getNestDigging() != null ? bp.getNestDigging() : 0.0).sum();
                        nestCountOfMouse+=sum4OfMouse;
                        double sum5OfMouse = bDykePreventionsOfMouse.stream().mapToDouble(bp -> bp.getChargeArea() != null ? bp.getChargeArea() : 0.0).sum();
                        medicationAreaOfMouse+=sum5OfMouse;
                        double sum6OfMouse = bDykePreventionsOfMouse.stream().mapToDouble(bp -> bp.getGroutingQuantity() != null ? bp.getGroutingQuantity() : 0.0).sum();
                        groutingLengthOfMouse+=sum6OfMouse;
                        double sum7OfMouse = bDykePreventionsOfMouse.stream().mapToDouble(bp -> bp.getInvestCapital() != null ? bp.getInvestCapital() : 0.0).sum();
                        invest_capitalOfMouse+=sum7OfMouse;
                    }
                    fujian6.setTotalHazardLengthOfMouse(totalHazardLengthOfMouse);
                    fujian6.setLevel1UnitCountOfMouse(1);
                    fujian6.setLevel2UnitCountOfMouse(2);
                    fujian6.setLevel3UnitCountOfMouse(4);
                    fujian6.setSeepageCountOfMouse(seepageCountOfMouse);
                    fujian6.setPenetrationCountOfMouse(penetrationCountOfMouse);
                    fujian6.setSinkholeCountOfMouse(sinkholeCountOfMouse);
                    fujian6.setNestCountOfMouse(nestCountOfMouse);
                    fujian6.setMedicationAreaOfMouse(medicationAreaOfMouse);
                    fujian6.setGroutingLengthOfMouse(groutingLengthOfMouse);
                    fujian6.setInvestment2023OfMouse(invest_capitalOfMouse);
                    fujian6.setHazardCountOfMouse(seepageCountOfMouse+penetrationCountOfMouse+sinkholeCountOfMouse);
                    projetMap.put(projectName,fujian6);
                    projectList.add(projetMap);
                }
            }

            map1.put("project",projectList);
            listResult.add(map1);

        }
        ByteArrayInputStream byteArrayInputStream = bProjectService.setExcelValueSeven(listResult);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode((String) listResult.get(0).get("deptName"), "UTF-8") + ".xlsx\"");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // 写入响应
        try {
            org.apache.commons.io.IOUtils.copy(byteArrayInputStream, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    /*
     *
     * 附件8 堤防工程白蚁等害堤动物危害及防治情况普查汇总表
     *两大类：1.白蚁危害 2.獾狐鼠
     *
     */

    @GetMapping("/reservoirDetailsOfEight")
    public void reservoirDetailsOfEight(HttpServletResponse response, @RequestParam("sysDeptId") Long sysDeptId) {
        //定义普查方式
        List list = Arrays.asList("", "地表指示物普查", "引诱普查", "仪器普查");
        //定义坝型
        List<String> listDam = Arrays.asList("", "土石坝", "混凝土坝", "混合坝");
        //定义护坡方式
        List<String> listSlopeProtection = Arrays.asList("", "草皮护坡", "干(浆)砌石护坡", "混凝土护坡");



        SysDept sysDeptGuangxi = sysDeptMapper.selectDeptById(sysDeptId);
        String deptName = sysDeptGuangxi.getDeptName();//1.获取 广西省水利厅

        //广西省水利厅 部门ID101 下属部门
        SysDept sysDept = new SysDept();
        sysDept.setParentId(sysDeptId);
        List<SysDept> sysDepts = sysDeptMapper.selectDeptList(sysDept);

        // 检查 sysDepts 是否为空或为空列表
        if (sysDepts == null || sysDepts.isEmpty()) {
            return ;
        }
        List<Map> listResult = new LinkedList();
        for (SysDept dept : sysDepts) {
            Map map1 = new LinkedHashMap();

            //广西省水利厅加进来
            map1.put("deptName",deptName);

            Long deptId = dept.getDeptId();   //部门南宁市水利局Id

            //获取当前部门的名字
            SysDept sysDeptNanning = sysDeptMapper.selectDeptById(deptId);
            String deptNameOfNanning = sysDeptNanning.getDeptName();
            map1.put("deptNameOfMinor",deptNameOfNanning);   //南宁市水利局
            List projectList=new LinkedList();


            //根据南宁部门ID去查下属项目
            BProject bProject=new BProject();
            bProject.setDeptId(deptId);
            bProject.setProjectType(2l);   //这里的2代表堤坝
            List<BProject> bProjects = bProjectMapper.selectBProjectList(bProject);
            if (bProjects != null&&bProjects.size()>0) {
                for (BProject bProject1 : bProjects) {
                    Map projetMap=new LinkedHashMap();
                    Fujian6 fujian6=new Fujian6();
                    String projectName = bProject1.getProjectName();
                    fujian6.setDykeName(projectName);  //南宁提防  --项目名   动作：根据项目ID去危害防治表中查对应的数据
                    fujian6.setDykeLevel(String.valueOf(bProject1.getDykeGrade()));
                    fujian6.setDykeLength(bProject1.getDykeLength());

                    //该项目有多少单元
                    int count=bPatrolUnitMapper.selectUnitNumbers(bProject1.getId());
                    fujian6.setUnitCount(count);

                    //危害长度总计
                    int totalHazardLength=0;
                    int seepageCount=0;
                    int penetrationCount=0;
                    int sinkholeCount=0;
                    int nestCount=0;
                    int medicationArea=0;
                    int groutingLength=0;
                    int invest_capital=0;
                    BDykePrevention bDykePrevention=new BDykePrevention();
                    bDykePrevention.setProjectId(bProject1.getId());
                    bDykePrevention.setDetrimentType(1l);   // 这里代表的是 危害类型1，  白蚁危害
                    List<BDykePrevention> bDykePreventions = bDykePreventionMapper.selectBDykePreventionList(bDykePrevention);
                    if (bDykePreventions != null&&bDykePreventions.size()>0){
                        double sum = bDykePreventions.stream().mapToDouble(bp -> bp.getDetrimentLength() != null ? bp.getDetrimentLength() : 0.0).sum();
                        totalHazardLength+=sum;
                        double sum1 = bDykePreventions.stream().mapToDouble(bp -> bp.getLeaksNumber() != null ? bp.getLeaksNumber() : 0.0).sum();
                        seepageCount+=sum1;
                        double sum2 = bDykePreventions.stream().mapToDouble(bp -> bp.getThroughNumber() != null ? bp.getThroughNumber() : 0.0).sum();
                        penetrationCount+=sum2;
                        double sum3 = bDykePreventions.stream().mapToDouble(bp -> bp.getDropSocketNumber() != null ? bp.getDropSocketNumber() : 0.0).sum();
                        sinkholeCount+=sum3;

                        double sum4 = bDykePreventions.stream().mapToDouble(bp -> bp.getNestDigging() != null ? bp.getNestDigging() : 0.0).sum();
                        nestCount+=sum4;
                        double sum5 = bDykePreventions.stream().mapToDouble(bp -> bp.getChargeArea() != null ? bp.getChargeArea() : 0.0).sum();
                        medicationArea+=sum5;
                        double sum6 = bDykePreventions.stream().mapToDouble(bp -> bp.getGroutingQuantity() != null ? bp.getGroutingQuantity() : 0.0).sum();
                        groutingLength+=sum6;
                        double sum7 = bDykePreventions.stream().mapToDouble(bp -> bp.getInvestCapital() != null ? bp.getInvestCapital() : 0.0).sum();
                        invest_capital+=sum7;


                    }
                    fujian6.setTotalHazardLength(totalHazardLength);
                    fujian6.setLevel1UnitCount(1);
                    fujian6.setLevel2UnitCount(2);
                    fujian6.setLevel3UnitCount(4);
                    fujian6.setSeepageCount(seepageCount);
                    fujian6.setPenetrationCount(penetrationCount);
                    fujian6.setSinkholeCount(sinkholeCount);
                    fujian6.setNestCount(nestCount);
                    fujian6.setMedicationArea(medicationArea);
                    fujian6.setGroutingLength(groutingLength);
                    fujian6.setInvestment2023(invest_capital);
                    fujian6.setHazardCount(seepageCount+penetrationCount+sinkholeCount);

                    int totalHazardLengthOfMouse=0;
                    int seepageCountOfMouse=0;
                    int penetrationCountOfMouse=0;
                    int sinkholeCountOfMouse=0;
                    int nestCountOfMouse=0;
                    int medicationAreaOfMouse=0;
                    int groutingLengthOfMouse=0;
                    int invest_capitalOfMouse=0;
                    BDykePrevention bDykePreventionOfMouse=new BDykePrevention();
                    bDykePreventionOfMouse.setProjectId(bProject1.getId());
                    bDykePreventionOfMouse.setDetrimentType(2l);   // 这里代表的是 危害类型2， 獾狐鼠
                    List<BDykePrevention> bDykePreventionsOfMouse = bDykePreventionMapper.selectBDykePreventionList(bDykePreventionOfMouse);
                    if (bDykePreventionsOfMouse != null&&bDykePreventionsOfMouse.size()>0){
                        double sumOfMouse = bDykePreventionsOfMouse.stream().mapToDouble(bp -> bp.getDetrimentLength() != null ? bp.getDetrimentLength() : 0.0).sum();
                        totalHazardLengthOfMouse+=sumOfMouse;
                        double sum1OfMouse = bDykePreventionsOfMouse.stream().mapToDouble(bp -> bp.getLeaksNumber() != null ? bp.getLeaksNumber() : 0.0).sum();
                        seepageCountOfMouse+=sum1OfMouse;
                        double sum2OfMouse = bDykePreventionsOfMouse.stream().mapToDouble(bp -> bp.getThroughNumber() != null ? bp.getThroughNumber() : 0.0).sum();
                        penetrationCountOfMouse+=sum2OfMouse;
                        double sum3OfMouse = bDykePreventionsOfMouse.stream().mapToDouble(bp -> bp.getDropSocketNumber() != null ? bp.getDropSocketNumber() : 0.0).sum();
                        sinkholeCountOfMouse+=sum3OfMouse;

                        double sum4OfMouse = bDykePreventionsOfMouse.stream().mapToDouble(bp -> bp.getNestDigging() != null ? bp.getNestDigging() : 0.0).sum();
                        nestCountOfMouse+=sum4OfMouse;
                        double sum5OfMouse = bDykePreventionsOfMouse.stream().mapToDouble(bp -> bp.getChargeArea() != null ? bp.getChargeArea() : 0.0).sum();
                        medicationAreaOfMouse+=sum5OfMouse;
                        double sum6OfMouse = bDykePreventionsOfMouse.stream().mapToDouble(bp -> bp.getGroutingQuantity() != null ? bp.getGroutingQuantity() : 0.0).sum();
                        groutingLengthOfMouse+=sum6OfMouse;
                        double sum7OfMouse = bDykePreventionsOfMouse.stream().mapToDouble(bp -> bp.getInvestCapital() != null ? bp.getInvestCapital() : 0.0).sum();
                        invest_capitalOfMouse+=sum7OfMouse;
                    }
                    fujian6.setTotalHazardLengthOfMouse(totalHazardLengthOfMouse);
                    fujian6.setLevel1UnitCountOfMouse(1);
                    fujian6.setLevel2UnitCountOfMouse(2);
                    fujian6.setLevel3UnitCountOfMouse(4);
                    fujian6.setSeepageCountOfMouse(seepageCountOfMouse);
                    fujian6.setPenetrationCountOfMouse(penetrationCountOfMouse);
                    fujian6.setSinkholeCountOfMouse(sinkholeCountOfMouse);
                    fujian6.setNestCountOfMouse(nestCountOfMouse);
                    fujian6.setMedicationAreaOfMouse(medicationAreaOfMouse);
                    fujian6.setGroutingLengthOfMouse(groutingLengthOfMouse);
                    fujian6.setInvestment2023OfMouse(invest_capitalOfMouse);
                    fujian6.setHazardCountOfMouse(seepageCountOfMouse+penetrationCountOfMouse+sinkholeCountOfMouse);
                    projetMap.put(projectName,fujian6);
                    projectList.add(projetMap);
                }
            }

            map1.put("project",projectList);
            listResult.add(map1);

        }
        ByteArrayInputStream byteArrayInputStream = bProjectService.setExcelValueEight(listResult);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode((String) listResult.get(0).get("deptName"), "UTF-8") + ".xlsx\"");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // 写入响应
        try {
            org.apache.commons.io.IOUtils.copy(byteArrayInputStream, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
 }



    
    


