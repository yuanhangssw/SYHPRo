package com.ruoyi.web.controller.termite;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.BPatrol;
import com.ruoyi.system.domain.BPatrolUnit;
import com.ruoyi.system.domain.BPatrolUnitPlace;
import com.ruoyi.system.domain.BProject;
import com.ruoyi.system.mapper.BPatrolMapper;
import com.ruoyi.system.mapper.BPatrolUnitMapper;
import com.ruoyi.system.mapper.BProjectMapper;
import com.ruoyi.system.mapper.CommonMapper;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzTransactionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.PageUtils.startPage;

@RestController
@RequestMapping("/rest")
public class RestApiController {

    @Autowired
    CommonMapper commonMapper;

    @Autowired
    private BPatrolUnitMapper bPatrolUnitMapper;

    @Autowired
    private BProjectMapper bProjectMapper;

    @Autowired
    private BPatrolMapper bPatrolMapper;

    /**
     * 根据用户获取分配给他的项目列表
     *
     * @param userid
     * @return
     */
    @GetMapping("/getprojectlistbyuser")
    List<BProject> getprojectbyuserid(Long userid) {

        return commonMapper.getprojectlistbyuserid(userid);

    }

    /**
     * 首页统计信息，依据项目和用户查询，
     *
     * @param userid    用户ID
     * @param projectid 项目id
     * @return
     */
    @GetMapping("/indexinfo")
    AjaxResult getindexinfo(Long userid, Long projectid) {

        //分类统计
        int type1 = commonMapper.gettotalbytype(userid, 1, projectid);
        int type2 = commonMapper.gettotalbytype(userid, 2, projectid);
        //单元总数
        int unittotal = commonMapper.gettotalunit(userid, projectid);

        //提交总数
        int datatotal = commonMapper.getdatatotal(userid, projectid);
        //审核通过、未通过总数
        int passtotal = commonMapper.getdatatotalbystatus(userid, "3", projectid);
        int backtotal = commonMapper.getdatatotalbystatus(userid, "4", projectid);

        Map<String, Object> result = new HashMap<>();

        result.put("type1", type1);
        result.put("type2", type2);
        result.put("unittotal", unittotal);
        result.put("datatotal", datatotal);
        result.put("passtotal", passtotal);
        result.put("backtotal", backtotal);


        return AjaxResult.success(result);
    }

    /**
     * 根据项目和用户，获取负责的每个工区的灾害信息量
     *
     * @param projectid
     * @param userid
     * @return
     */
    @GetMapping("/unitlist")
    AjaxResult getunitlist(Long projectid, Long userid) {

        List<Map<String, Object>> res = commonMapper.getprojecttotallist(projectid, userid);

        return AjaxResult.success(res);
    }

    @GetMapping("/unitlist2")
    AjaxResult getunitlist2(Long projectid, Long userid) {

        List<Map<String, Object>> res = commonMapper.getprojectunitllist(projectid, userid);

        return AjaxResult.success(res);
    }

    @GetMapping("/unitlist3")
    AjaxResult getunitlist3(Long projectid, Long userid) {

        List<Map<String, Object>> res = commonMapper.getprojectunitllist3(projectid, userid);

        return AjaxResult.success(res);
    }


    @GetMapping("/getdatatotalbystatus")
    AjaxResult getdatatotalbystatus(Long projectid, Long userid) {

        Map<String, Integer> res = commonMapper.getdatatotalbystatus2(projectid, userid);
        return AjaxResult.success(res);

    }

    //查询普查处数
    @GetMapping("/getPcdata")
    AjaxResult getPcdata(int projectid, int userid) {
        int number = commonMapper.getPcdata(projectid, userid);
        return AjaxResult.success(number);

    }

    //查询已完成单元数
    @GetMapping("/getCompletedata")
    AjaxResult getCompletedata(int projectid, int userid) {
        int number = commonMapper.getCompletedata(projectid, userid);
        return AjaxResult.success(number);

    }

    //查询全部单元数下的普查
    // 信息
    @GetMapping("/list")
    public AjaxResult getAllPuCha(int userId, int projectId) {
        startPage();
        List<Map<String, Object>> map = commonMapper.getprojecttotallistandUnitId(projectId, userId);

        //查找巡查表里没有的单元
        List<Map<String, Object>> map2 = commonMapper.getprojecttotallistandUnitIdSub(projectId, userId);
        map.addAll(map2);
        return AjaxResult.success(map);
    }


/*
    //查询单元下白蚁和鼠的数量
    @GetMapping("/getAllType")
    AjaxResult getAllType(int projectid,int userid){
  //      int number  = commonMapper.getAllType(projectid,userid);
  //      return  AjaxResult.success(number);
    }*/


    //统计两种害虫数量  项目用户
    @GetMapping("/getdataBaiyi")
    public AjaxResult getdataBaiyi(BPatrolUnitPlace bPatrolUnitPlace) {

        Map<String, Object> map = new HashMap<>();
        map = commonMapper.getdataBaiyi(bPatrolUnitPlace);
        return AjaxResult.success(map);
    }

    //统计两种害虫数量  项目用户 加 单元
    @GetMapping("/getdataBaiyiAndUnit")
    public AjaxResult getdataBaiyiAndUnit(BPatrolUnitPlace bPatrolUnitPlace) {

        Map<String, Object> map = new HashMap<>();
        map = commonMapper.getdataBaiyiAndUnit(bPatrolUnitPlace);
        return AjaxResult.success(map);
    }


    //后台管理  统计首页 数据
    @GetMapping("/getDataHomePage")
    public AjaxResult getDataHomePage() {

        Long inspector = SecurityUtils.getUserId();
        Long deptId = SecurityUtils.getDeptId();
        //1.统计工程数量
        int projectNum = commonMapper.getProjectNum(deptId);
        //2.统计巡查员数量
        int inspectorNum = commonMapper.getInspectorNum(deptId);
        //3.单元数量
        int unitNum = commonMapper.getUnitNum(deptId);
        Map map = new HashMap();
        map.put("projectNum", projectNum);
        map.put("inspectorNum", inspectorNum);
        map.put("unitNum", unitNum);
        return AjaxResult.success(map);
    }


    //5.巡查类型统计
    @GetMapping("/getInspectType")
    public AjaxResult getInspectType() {

        Long deptId = SecurityUtils.getDeptId();
        //根据部门Id去查
        BProject bProject = new BProject();
        bProject.setDeptId(deptId);
        List<BProject> bProjects = bProjectMapper.selectBProjectList(bProject);
        //遍历每一个项目
        List list = new ArrayList();
        for (BProject project : bProjects) {
            Long id = project.getId();
            Map map = new HashMap();
            //获取每个项目下的  每种灾害类型的数量
            map = commonMapper.getdataProjectZhNumber(id);
            if (null == map) {
                map = new HashMap<>();
                map.put("project_id", id);
                map.put("project_name", project.getProjectName());
                map.put("type1total", "0");
                map.put("type2total", "0");
            }
            list.add(map);
        }
        return AjaxResult.success(list);
    }


    //6.进度统计
    @GetMapping("/getSchedule")
    public AjaxResult getSchedule() {
        Long deptId = SecurityUtils.getDeptId();
        //根据部门Id去查项目结合
        BProject bProject = new BProject();
        bProject.setDeptId(deptId);
        List<BProject> bProjects = bProjectMapper.selectBProjectList(bProject);
        //遍历每一个项目
        List list = new ArrayList();
        for (BProject project : bProjects) {
            Long id = project.getId();
            Map map = new HashMap();
            //每个项目  已完成单元和所有单元
            BProject bProject1 = bProjectMapper.selectBProjectById(id);
            String projectName = bProject1.getProjectName();
            Long id1 = bProject1.getId();
            map.put("projectName", projectName);
            map.put("projectId", id1);
            int CompleteNum = commonMapper.getScheduleCompleteUnit(id);
            map.put("CompleteNum", CompleteNum);
            int allCompleteNum = commonMapper.getScheduleAllCompleteUnit(id);
            map.put("allCompleteNum", allCompleteNum);
            list.add(map);
        }
        return AjaxResult.success(list);
    }

    @GetMapping("/getCompletedataAdmin")
    AjaxResult getCompletedataAdmin() {
        Long deptId = SecurityUtils.getDeptId();
        BProject bProject = new BProject();
        bProject.setDeptId(deptId);
        List<BProject> bProjects = bProjectMapper.selectBProjectList(bProject);
        List<Map<String, Object>> list = new ArrayList<>();
        for (BProject project : bProjects) {
            Long id = project.getId();
            String projectName = project.getProjectName();
            int number = commonMapper.getCompletedataAdmin(id);
            Map map = new HashMap();
            map.put("projectName", projectName);
            map.put("number", number);
            list.add(map);
        }
        return AjaxResult.success(list);
    }


    //指示物类型统计
    @GetMapping("/getZhiWuNumbers")
    public AjaxResult getZhiWuNumbers() {
        List<String> customOrder = Arrays.asList("鸡枞菌", "碳棒菌", "泥被","泥线", "分飞孔", "啃食木材", "白蚁", "巢穴", "取食点(白蚁)", "活动迹象", "取食点（鼠）", "洞穴","捕获动物");

        Long deptId = SecurityUtils.getDeptId();
        //根据部门Id去查
        BProject bProject = new BProject();
        bProject.setDeptId(deptId);
        List<BProject> bProjects = bProjectMapper.selectBProjectList(bProject);
        //遍历每一个项目
        List<Long> ids=new ArrayList<>();
        for (BProject project : bProjects) {
            Long id = project.getId();
            ids.add(id);
            //根据项目ID 去查b_patrol表
        }
        Map<String,List<Long>> mapsparam=new HashMap<>();
        mapsparam.put("ids",ids);
        Map<String, Integer> map = new LinkedHashMap<>();
        map = bPatrolMapper.selectZhishiWuNumbers(mapsparam);

        Map<String, Integer> sortedMap = customOrder.stream()
                .filter(map::containsKey)
                .collect(Collectors.toMap(
                        key -> key,
                        map::get,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
        List  list=new ArrayList();


        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            Map mapResult = new LinkedHashMap<>();
            mapResult.put("Number",entry.getValue());
            mapResult.put("typeName",entry.getKey());
            list.add(mapResult);
        }

        return AjaxResult.success(list);
    }



}
