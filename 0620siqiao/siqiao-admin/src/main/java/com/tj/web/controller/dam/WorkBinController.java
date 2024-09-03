package com.tj.web.controller.dam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.bean.JtsRTree;
import com.tianji.dam.domain.*;
import com.tianji.dam.domain.vo.DamsConstructionVo;
import com.tianji.dam.domain.vo.T1VO;
import com.tianji.dam.mapper.MaterialMapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.mapper.THistoryPicMapper;
import com.tianji.dam.mapper.TableMapper;
import com.tianji.dam.service.ITReportSaveService;
import com.tianji.dam.service.RollingDataService;
import com.tianji.dam.service.T1Service;
import com.tianji.dam.service.TDamsconstructionService;
import com.tianji.dam.thread.AutoHistoryThread;
import com.tianji.dam.thread.AutoReportThread;
import com.tianji.dam.utils.GenerateRedisKey;
import com.tianji.dam.utils.RedisUtil;
import com.tianji.dam.utils.TrackConstant;
import com.tj.common.annotation.RepeatSubmit;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.utils.DateUtils;
import com.tj.common.utils.SecurityUtils;
import com.tj.common.utils.StringUtils;
import com.vividsolutions.jts.algorithm.PointLocator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/workBin")
@Controller("WorkBinController")
public class WorkBinController {
    @Autowired
    private TDamsconstructionService damsConstructionService;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private TableMapper tableMapper;

    @Autowired
    private T1Service t1Service;

    @Autowired
    private RollingDataService rollingDataService;

    @Autowired
    private static StoreHouseMap storeHouseMap;
    @Autowired
    private THistoryPicMapper historyPicMapper;

    @Autowired
    private ITReportSaveService tReportSaveService;


    @Autowired
    private TDamsconstructionMapper damsconstructionMapper;

    /**
     * 跳转RTK范围
     *
     * @return
     */
    @RequestMapping("/storehouse")
    public String index(ModelMap model) {
        return "workBin/storehouse";
    }

    //树零级目录跳转
    @RequestMapping("/treeLevel0")
    public String treeLevel0(ModelMap model, HttpServletRequest request) {
        String id = request.getParameter("id");
        model.addAttribute("id", id);
        return "workBin/level0";
    }

    @RequestMapping(value = "/addLevel0", method = RequestMethod.GET)
    public String addLevel0(ModelMap model) {
        return "workBin/addLevel0";
    }

    /*新增分布工程 材料*/
    @RequestMapping(value = "/addLevel0", method = RequestMethod.POST)
    @ResponseBody
    @JsonIgnore
    @RepeatSubmit(interval = 1000, message = "请求过于频繁")
    public int addLevel0Post(HttpServletRequest request, @RequestBody Map<String, String> param) {
        DamsConstruction damsConstruction = new DamsConstruction();
        if (!param.get("xbegin").equals("")) {
            damsConstruction.setXbegin(Double.valueOf(param.get("xbegin")));
        }
        if (!param.get("xend").equals("")) {
            damsConstruction.setXend(Double.valueOf(param.get("xend")));
        }
        damsConstruction.setTitle(param.get("title"));
        damsConstruction.setEngcode(param.get("engcode"));
        damsConstruction.setPlanstarttime(param.get("planstarttime"));
        damsConstruction.setPlanendtime(param.get("planendtime"));
        damsConstruction.setActualstarttime(param.get("actualstarttime"));
        damsConstruction.setActualendtime(param.get("actualendtime"));
        damsConstruction.setRemarks(param.get("remarks"));
        damsConstruction.setDamsid(1);
        damsConstruction.setPid(0);
        damsConstruction.setMaterialname(param.get("materialname"));
        damsConstruction.setStatus(4);


        int result = damsConstructionService.addSelective(damsConstruction);

        return result;
        //	damsConstruction.setId(material.getId());
		
		
		/*向材料表中增加
		Material m = materialMapper.selectByDesc();
		int index = 0;
		if(m!=null){
			index = m.getId();
		}
		Material material = new Material();
		material.setId(index + 1);
		material.setMaterialname(param.get("title"));
		material.setMid(index + 1);
		int result2=materialMapper.insert(material);
		查询材料
		Material material2 =materialMapper.selectByMaterialname(param.get("title"));
		int result = damsConstructionService.addSelective(damsConstruction);
		if(result==1&&result2==1){
			return 1;
		}
		else {
			return 0;
		}*/
    }

    /*修改分布工程 材料*/
    @RequestMapping(value = "/updateLevel0", method = RequestMethod.POST)
    @ResponseBody
    @RepeatSubmit(interval = 1000, message = "请求过于频繁")
    public int updateLevel0Post(ModelMap model, HttpServletRequest request, @RequestBody Map<String, String> param) {
        /*根据id 查询原来的工作仓*/
        DamsConstruction dam = damsConstructionService.findById(Integer.valueOf(param.get("id")));
        /*如果材料名修改了 需要修改材料表*/
		/*if(!dam.getTitle().equals(param.get("title"))){
			Material material2 =materialMapper.selectByMaterialname(dam.getTitle());
			material2.setMaterialname(param.get("title"));
			int re = materialMapper.updateByPrimaryKey(material2);
		}*/
        if (StringUtils.isNotNull(param.get("xbegin"))) {
            dam.setXbegin(Double.valueOf(param.get("xbegin")));
        }
        if (StringUtils.isNotNull(param.get("xend"))) {
            dam.setXend(Double.valueOf(param.get("xend")));
        }
        dam.setTitle(param.get("title"));
        dam.setPlanstarttime(param.get("planstarttime"));
        dam.setPlanendtime(param.get("planendtime"));
        dam.setRemarks(param.get("remarks"));
        dam.setActualstarttime(param.get("actualstarttime"));
        dam.setActualendtime(param.get("actualendtime"));
        dam.setEngcode(param.get("engcode"));
        int result = damsConstructionService.updateDC(dam);
        return result;
    }

    //删除车辆
    @RequestMapping(value = "/deleteLevel0", method = RequestMethod.GET)
    @ResponseBody
    public int deleteLevel0(HttpServletRequest request) {
        Integer id = Integer.valueOf(request.getParameter("id"));
        DamsConstruction damsConstruction = damsConstructionService.findById(id);
        String materialname = damsConstruction.getTitle();
        int result = damsConstructionService.deleteDC(id);
        int result2 = materialMapper.deleteByMaterialname(materialname);
        if (result == 1 && result2 == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    //树一级目录跳转
    @RequestMapping("/treeLevel1")
    public String treeLevel1(ModelMap model, HttpServletRequest request) {
        String id = request.getParameter("id");
        model.addAttribute("id", id);
        return "workBin/level1";
    }

    @RequestMapping(value = "/addLevel1", method = RequestMethod.POST)
    @ResponseBody
    @RepeatSubmit(interval = 1000, message = "请求过于频繁")
    public int addLevel1Post(HttpServletRequest request, @RequestBody Map<String, String> param) {
        String timestamp = String.valueOf(new Date().getTime());
        timestamp = "t_" + timestamp;
        DamsConstruction damsConstruction = new DamsConstruction();

        damsConstruction.setTitle(param.get("title"));
        damsConstruction.setEngcode(param.get("engcode"));
        damsConstruction.setPlanstarttime(param.get("planstarttime"));
        damsConstruction.setCenggao(Double.valueOf(param.get("cenggao")));
        damsConstruction.setGaocheng(Double.valueOf(param.get("gaocheng")));
        damsConstruction.setGaochengact(Double.valueOf(param.get("gaochengact")));
        damsConstruction.setPlanendtime(param.get("planendtime"));
        damsConstruction.setActualendtime(param.get("actualendtime").equals("") ? param.get("planstarttime") : param.get("actualendtime"));
        damsConstruction.setActualstarttime(param.get("actualstarttime").equals("") ? param.get("planendtime") : param.get("actualstarttime"));
        damsConstruction.setEdge(param.get("edge"));
        damsConstruction.setRemarks(param.get("remarks"));
        //新增字段
        damsConstruction.setRanges(param.get("ranges"));
        damsConstruction.setDamsid(1);

        damsConstruction.setMaterialname(param.get("materialname"));
        damsConstruction.setSpeed(Double.parseDouble(param.get("speed")));
        damsConstruction.setFrequency(Integer.parseInt(param.get("frequency")));
        damsConstruction.setPid(Integer.parseInt(param.get("pid")));
        damsConstruction.setFreedom1(param.get("freedom1"));
        damsConstruction.setFreedom2(param.get("freedom2"));
        damsConstruction.setFreedom3(param.get("freedom3"));
        damsConstruction.setFreedom4(param.get("freedom4"));
        damsConstruction.setHoudu(Double.valueOf(param.get("houdu").toString()));
        damsConstruction.setTypes(Integer.valueOf(param.get("types").toString()));
        damsConstruction.setAddtime(DateUtils.getTime());
        damsConstruction.setHeightIndex(Integer.valueOf(param.get("heightIndex")));
//		damsConstruction.setStatus(0);
        //修改
        damsConstruction.setStatus(88);
        damsConstruction.setIscang(1);
        damsConstruction.setHasdata(0);

        StringBuffer sb = new StringBuffer(timestamp);
        sb.append("_").append(damsConstruction.getEngcode()).append("_").append(damsConstruction.getPid());
        damsConstruction.setTablename(sb.toString());

        String[] tableprefix = GlobCache.cartableprfix;
        for (String s : tableprefix) {
            if (!"".equals(s)) {
                tableMapper.createVehicleDateTable(s + "_" + damsConstruction.getTablename());
            }

        }


        if (damsConstruction.getRanges().equals("[]") || damsConstruction.getRanges().equals("null")) {
            damsConstruction.setRanges(null);
        } else {
            if (StringUtils.isNotNull(damsConstruction.getRanges())) {
                List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
                if (!list.isEmpty()) {
                    double xs[] = new double[list.size()];
                    double ys[] = new double[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        xs[i] = list.get(i).x;
                        ys[i] = list.get(i).y;
                    }
                    double xMax = Arrays.stream(xs).max().getAsDouble();
                    double xMin = Arrays.stream(xs).min().getAsDouble();
                    double yMax = Arrays.stream(ys).max().getAsDouble();
                    double yMin = Arrays.stream(ys).min().getAsDouble();
                    damsConstruction.setXbegin(xMin);
                    damsConstruction.setXend(xMax);
                    damsConstruction.setYbegin(yMin);
                    damsConstruction.setYend(yMax);
                }
            }
        }
        /*tableMapper.createVehicleDateTable(timestamp);*/
        int result = damsConstructionService.add(damsConstruction);
        if (result > 0) {
            //插入R树
            new JtsRTree().init();
//			JtsRTree.add(new DamsJtsTreeVo(result, damsConstruction.getTitle(), damsConstruction.getTablename(), damsConstruction.getGaocheng(), damsConstruction.getCenggao(), damsConstruction.getRanges()),1);
        }
        return result;
    }

    @RequestMapping(value = "/updateLevel1", method = RequestMethod.POST)
    @ResponseBody
    @RepeatSubmit(interval = 1000, message = "请求过于频繁")
    public int updateLevel1Post(ModelMap model, HttpServletRequest request, @RequestBody Map<String, String> param) {
        int id = Integer.valueOf(param.get("id"));
        DamsConstruction damsConstruction = damsConstructionService.findById(id);
		/*damsConstruction.setXbegin(Double.valueOf(param.get("xbegin")));
		damsConstruction.setXend(Double.valueOf(param.get("xend")));
		damsConstruction.setYbegin(Double.valueOf(param.get("ybegin")));
		damsConstruction.setYend(Double.valueOf(param.get("yend")));*/
        damsConstruction.setTitle(param.get("title"));
        damsConstruction.setEngcode(param.get("engcode"));
        damsConstruction.setPlanstarttime(param.get("planstarttime"));
        damsConstruction.setCenggao(Double.valueOf(param.get("cenggao")));
        damsConstruction.setGaocheng(Double.valueOf(param.get("gaocheng")));
        damsConstruction.setGaochengact(Double.valueOf(param.get("gaochengact")));
        damsConstruction.setPlanendtime(param.get("planendtime"));
        damsConstruction.setActualendtime(param.get("actualendtime").equals("") ? param.get("planstarttime") : param.get("actualendtime"));
        damsConstruction.setActualstarttime(param.get("actualstarttime").equals("") ? param.get("planendtime") : param.get("actualstarttime"));
        damsConstruction.setEdge(param.get("edge"));
        damsConstruction.setRemarks(param.get("remarks"));
        damsConstruction.setMaterialname(param.get("materialname"));
        damsConstruction.setSpeed(Double.parseDouble(param.get("speed")));
        damsConstruction.setFrequency(Integer.parseInt(param.get("frequency")));
        damsConstruction.setFreedom1(param.get("freedom1"));
        damsConstruction.setFreedom2(param.get("freedom2"));
        damsConstruction.setFreedom3(param.get("freedom3"));
        damsConstruction.setFreedom4(param.get("freedom4"));
        damsConstruction.setHoudu(Double.valueOf(param.get("houdu").toString()));
        damsConstruction.setTypes(Integer.valueOf(param.get("types").toString()));
        damsConstruction.setUpdatetime(DateUtils.getTime());
        damsConstruction.setHeightIndex(Integer.valueOf(param.get("heightIndex")));
        damsConstruction.setPzmap(new HashMap<>());
        //新增字段
        if (param.get("ranges").equals("[]") || param.get("ranges").equals("null")) {
            if (StringUtils.isNotNull(damsConstruction.getRanges())) {
                List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
                if (!list.isEmpty()) {
                    double xs[] = new double[list.size()];
                    double ys[] = new double[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        xs[i] = list.get(i).x;
                        ys[i] = list.get(i).y;
                    }
                    double xMax = Arrays.stream(xs).max().getAsDouble();
                    double xMin = Arrays.stream(xs).min().getAsDouble();
                    double yMax = Arrays.stream(ys).max().getAsDouble();
                    double yMin = Arrays.stream(ys).min().getAsDouble();
                    damsConstruction.setXbegin(xMin);
                    damsConstruction.setXend(xMax);
                    damsConstruction.setYbegin(yMin);
                    damsConstruction.setYend(yMax);
                }
            }
        } else {
            if (StringUtils.isNotNull(param.get("ranges"))) {
                damsConstruction.setRanges(param.get("ranges"));
                List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
                if (!list.isEmpty()) {
                    double xs[] = new double[list.size()];
                    double ys[] = new double[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        xs[i] = list.get(i).x;
                        ys[i] = list.get(i).y;
                    }
                    double xMax = Arrays.stream(xs).max().getAsDouble();
                    double xMin = Arrays.stream(xs).min().getAsDouble();
                    double yMax = Arrays.stream(ys).max().getAsDouble();
                    double yMin = Arrays.stream(ys).min().getAsDouble();
                    damsConstruction.setXbegin(xMin);
                    damsConstruction.setXend(xMax);
                    damsConstruction.setYbegin(yMin);
                    damsConstruction.setYend(yMax);
                }
            }
        }
        int result = damsConstructionService.updateDC(damsConstruction);
        if (result > 0) {
            new JtsRTree().init();
            //修改R树
//			JtsRTree.add(new DamsJtsTreeVo(damsConstruction.getId(), damsConstruction.getTitle(), damsConstruction.getTablename(), damsConstruction.getGaocheng(), damsConstruction.getCenggao(), damsConstruction.getRanges()),1);
        }
        return result;
    }

    //删除
    @RequestMapping(value = "/deleteLevel1", method = RequestMethod.GET)
    @ResponseBody
    public int deleteLevel1(HttpServletRequest request) {
        Integer id = Integer.valueOf(request.getParameter("id"));
        /*需要删除对应的数据表*/
        DamsConstruction damsConstruction = damsConstructionService.findById(id);
        String tablename = damsConstruction.getTablename();
        int dropresult = tableMapper.dropByTablename(tablename);
        int result = damsConstructionService.deleteDC(id);
        if (result > 0) {
            //删除R树
            new JtsRTree().init();
//			JtsRTree.add(new DamsJtsTreeVo(damsConstruction.getId(), damsConstruction.getTitle(), damsConstruction.getTablename(), damsConstruction.getGaocheng(), damsConstruction.getCenggao(), damsConstruction.getRanges()),2);
        }
        return result;
    }

    @RequestMapping("/treeLevel2")
    public String treeLevel2(ModelMap model, HttpServletRequest request) {
        String id = request.getParameter("id");
        model.addAttribute("id", id);
        return "workBin/levelSec";
    }

    @RequestMapping(value = "/addLevel2", method = RequestMethod.GET)
    public String addLevel2(ModelMap model, HttpServletRequest request) {
        String id = request.getParameter("id");
        model.addAttribute("id", id);
        return "workBin/addLevel2";
    }

    @RequestMapping(value = "/addLevel2", method = RequestMethod.POST)
    @ResponseBody
    @RepeatSubmit(interval = 1000, message = "请求过于频繁")
    public int addLevel2Post(HttpServletRequest request) {
        DamsConstruction damsConstruction = new DamsConstruction();
        damsConstruction.setXbegin(Double.valueOf(request.getParameter("xbegin")));
        damsConstruction.setXend(Double.valueOf(request.getParameter("xend")));
        damsConstruction.setYbegin(Double.valueOf(request.getParameter("ybegin")));
        damsConstruction.setYend(Double.valueOf(request.getParameter("yend")));
        damsConstruction.setTitle(request.getParameter("title"));
        damsConstruction.setEngcode(request.getParameter("engcode"));
        damsConstruction.setPlanstarttime(request.getParameter("planstarttime"));
        damsConstruction.setPlanendtime(request.getParameter("planendtime"));
        damsConstruction.setEdge(request.getParameter("edge"));
        damsConstruction.setRemarks(request.getParameter("remarks"));
        damsConstruction.setDamsid(1);
        damsConstruction.setPid(Integer.valueOf(request.getParameter("pid")));


        int result = damsConstructionService.add(damsConstruction);
        return result;
    }

    @RequestMapping(value = "/updateLevel2", method = RequestMethod.GET)
    public String updateLevel2(ModelMap model, HttpServletRequest request) {
        Integer id = Integer.valueOf(request.getParameter("id"));
        DamsConstruction damsConstruction = damsConstructionService.findById(id);
        model.addAttribute("DC", damsConstruction);
        return "workBin/updateLevel2";
    }

    @RequestMapping(value = "/updateLevel2", method = RequestMethod.POST)
    @ResponseBody
    @RepeatSubmit(interval = 1000, message = "请求过于频繁")
    public int updateLevel2Post(ModelMap model, HttpServletRequest request) {
        DamsConstruction damsConstruction = new DamsConstruction();
        damsConstruction.setXbegin(Double.valueOf(request.getParameter("xbegin")));
        damsConstruction.setXend(Double.valueOf(request.getParameter("xend")));
        damsConstruction.setYbegin(Double.valueOf(request.getParameter("ybegin")));
        damsConstruction.setYend(Double.valueOf(request.getParameter("yend")));
        damsConstruction.setId(Integer.valueOf(request.getParameter("id")));
        damsConstruction.setTitle(request.getParameter("title"));
        damsConstruction.setEngcode(request.getParameter("engcode"));
        damsConstruction.setPlanstarttime(request.getParameter("planstarttime"));
        damsConstruction.setPlanendtime(request.getParameter("planendtime"));
        damsConstruction.setEdge(request.getParameter("edge"));
        damsConstruction.setRemarks(request.getParameter("remarks"));
        damsConstruction.setMaterialname(request.getParameter("materialname"));
        damsConstruction.setDamsid(1);
        damsConstruction.setPid(Integer.valueOf(request.getParameter("pid")));


        //新增字段
        damsConstruction.setRanges(request.getParameter("ranges"));
        int result = damsConstructionService.updateDC(damsConstruction);
        return result;
    }

    //删除车辆
    @RequestMapping(value = "/deleteLevel2", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject deleteLevel2(HttpServletRequest request) {
        Integer id = Integer.valueOf(request.getParameter("id"));
        int result = damsConstructionService.deleteDC(id);
        return new JSONObject();
    }


    public Map<String, Object> findAll(@PathVariable String id, HttpServletRequest request) {

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        List<DamsConstruction> damsConstructionList = damsConstructionService.getAll(id);
        Integer count = damsConstructionList.size();
        resultMap.put("count", count);
        resultMap.put("data", damsConstructionList);
        return resultMap;
    }

    @RequestMapping("/findWorkbinByPid/{id}")
    @ResponseBody
    public Map<String, Object> findAllByPage(@PathVariable String id, HttpServletRequest request) {
        int page = Integer.valueOf(request.getParameter("page"));
        int limit = Integer.valueOf(request.getParameter("limit"));
        Integer status = Integer.valueOf(request.getParameter("status") == null ? "8" : request.getParameter("status"));
        int begin = (page - 1) * limit;
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        List<DamsConstruction> damsConstructionList = damsConstructionService.getAllByPage(id, begin, limit, status);
        List<DamsConstruction> dams = damsConstructionService.getAll(id);
        Integer count = dams.size();
        resultMap.put("count", count);
        resultMap.put("data", damsConstructionList);
        return resultMap;
    }

    @RequestMapping("/findWorkbinByPid/")
    @ResponseBody
    public Map<String, Object> findWorkbinByPid(DamsConstructionVo damsConstructionVo) {
        int page = damsConstructionVo.getPage();
        int limit = damsConstructionVo.getLimit();
        int begin = (page - 1) * limit;
        damsConstructionVo.setBegin(begin);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        List<DamsConstruction> damsConstructionList = damsConstructionService.getAllByPageVo(damsConstructionVo);
        List<Coordinate> repairRange = new ArrayList<>();
        for (DamsConstruction damsConstruction : damsConstructionList) {
            repairRange = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
            if (null != repairRange && repairRange.size() > 0) {
                double aras = calculateArea(repairRange);

                double ar = TrackConstant.kk * TrackConstant.kk;
                double finalarea = new BigDecimal(aras * ar).setScale(4, RoundingMode.HALF_DOWN).doubleValue();
                Double fl = new BigDecimal(finalarea * damsConstruction.getCenggao() / 100.0).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
                damsConstruction.setFreedom1(fl + "");
                damsConstruction.setFreedom3(finalarea + "");
            }
            //查询仓位的统计分析结果
            //首先查询是否存在已经保存的 存在这直接使用已经保存的
            THistoryPic select = new THistoryPic();
            select.setDamid(Long.valueOf(damsConstruction.getId()));
            select.setHtype(1l);
            List<THistoryPic> selects = historyPicMapper.selectTHistoryPicList(select);
            if (selects.size() > 0) {
                String res = selects.get(0).getContent();
                JSONObject saveobj = JSONObject.parseObject(res);

                if (saveobj.containsKey("rollingResult")) {
                    JSONObject rollingResult = saveobj.getJSONObject("rollingResult");
                    Integer int0 = 0;
                    Integer int1 = 0;
                    Integer int2 = 0;
                    Integer int3 = 0;
                    Integer int4 = 0;
                    Integer int5 = 0;
                    Integer int6 = 0;
                    Integer int7 = 0;
                    Integer int8 = 0;
                    Integer int9 = 0;
                    Integer int10 = 0;
                    Integer int11 = 0;
                    Integer int12 = 0;
                    int0 = rollingResult.getInteger("time0");
                    int1 = rollingResult.getInteger("time1");
                    int2 = rollingResult.getInteger("time2");
                    int3 = rollingResult.getInteger("time3");
                    int4 = rollingResult.getInteger("time4");
                    int5 = rollingResult.getInteger("time5");
                    int6 = rollingResult.getInteger("time6");
                    int7 = rollingResult.getInteger("time7");
                    int8 = rollingResult.getInteger("time8");
                    int9 = rollingResult.getInteger("time9");
                    int10 = rollingResult.getInteger("time10");
                    int11 = rollingResult.getInteger("time11");
                    int11 = rollingResult.getInteger("time11Up");
                    int passcount = int8 + int9 + int10 + int11 + int12;
                    int total = int1 + int2 + int3 + int4 + int5 + int6 + int7 + int8 + int9 + int10 + int11 + int12;
                    if (total > 0) {
                        Double pass = new BigDecimal((passcount * 1.0 / total) * 100.0).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
                        damsConstruction.setTimepass(pass + "%");
                    }

                }
                if (saveobj.containsKey("rollingResultEvolution2")) {
                    JSONObject rollingResult = saveobj.getJSONObject("rollingResultEvolution2");
                    String houdu = "";
                    Integer int0 = 0;
                    Integer int1 = 0;
                    Integer int2 = 0;
                    Integer int3 = 0;
                    Integer int4 = 0;
                    Integer int5 = 0;
                    Integer int6 = 0;

                    int0 = rollingResult.getInteger("time0");
                    int1 = rollingResult.getInteger("time1");
                    int2 = rollingResult.getInteger("time2");
                    int3 = rollingResult.getInteger("time3");
                    int4 = rollingResult.getInteger("time4");
                    int5 = rollingResult.getInteger("time5");
                    int6 = rollingResult.getInteger("time6");

                    /*if (int0 > 0) {
                        houdu = "<65";
                    } else if (int1 > 0) {
                        houdu = "65-75";
                    } else if (int2 > 0) {
                        houdu = "75-85";
                    } else if (int3 > 0) {
                        houdu = "85-95";
                    } else if (int4 > 0) {
                        houdu = "95-105";
                    } else if (int5 > 0) {
                        houdu = "105-115";
                    } else if (int6 > 0) {
                        houdu = ">115";
                    }
*/
                    //从监控成果表中获取base64 拿到 houduQuality直接用就行
                    TReportSave tReportSave = tReportSaveService.seletbyDamAndType(Long.valueOf(String.valueOf(damsConstruction.getId())), 1l);
                    String base64 = tReportSave.getBase64();
                    JSONObject jsonObject = JSONObject.parseObject(base64);
                    String houduss = "";
                    if (null != jsonObject && null != jsonObject.getString("houduQuality")) {
                        houduss = jsonObject.getString("houduQuality");
                    }

                    damsConstruction.setHoudus(houduss);
                }
                if (saveobj.containsKey("rollingResultEvolution")) {
                    JSONObject rollingResult = saveobj.getJSONObject("rollingResultEvolution");
                    Integer int0 = 0;
                    Integer int1 = 0;
                    Integer int2 = 0;
                    Integer total = 0;
                    int0 = rollingResult.getInteger("time0");
                    int1 = rollingResult.getInteger("time1");
                    int2 = rollingResult.getInteger("time2");
                    total = int0 + int1 + int2;
                    if (total > 0) {
                        Double pass0 = new BigDecimal((int0 * 1.0 / total) * 100.0).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
                        Double pass1 = new BigDecimal((int1 * 1.0 / total) * 100.0).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
                        Double pass2 = new BigDecimal((int2 * 1.0 / total) * 100.0).setScale(2, RoundingMode.HALF_DOWN).doubleValue();

                        Map<String, String> pzmap = new HashMap<>();
                        pzmap.put("<-10", pass0 + "%");
                        pzmap.put("-10~10", pass1 + "%");
                        pzmap.put(">10", pass2 + "%");
                        damsConstruction.setPzmap(pzmap);
                    }
                }

            }

        }


        List<DamsConstruction> dams = damsConstructionService.getAllVo(damsConstructionVo);
        Integer count = dams.size();
        resultMap.put("count", count);
        resultMap.put("data", damsConstructionList);
        return resultMap;
    }


    static double calculateArea(List<Coordinate> vertices) {
        double area = 0.0;
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            area += vertices.get(i).x * vertices.get(j).y;
            area -= vertices.get(i).y * vertices.get(j).x;
        }
        area = Math.abs(area) / 2.0;
        return area;
    }

    /*开仓 更改仓的status 从0变为8*/
    @RequestMapping(value = "/openCang", method = RequestMethod.POST)
    @ResponseBody
    public int openCang(ModelMap model, HttpServletRequest request) {
        try {
            Integer id = Integer.valueOf(request.getParameter("id"));
            DamsConstruction damsConstruction = damsConstructionService.findById(id);
            if (damsConstruction == null) {
                return 0;
            }
            if (damsConstruction.getStatus().intValue() == 2) {
                damsConstruction.setStatus(2);
            } else
                damsConstruction.setStatus(8);

            String[] tableprefix = GlobCache.cartableprfix;
            for (String s : tableprefix) {
                if (!"".equals(s)) {
                    Map<String, String> map = tableMapper.checkTableExistsWithSchema(s + "_" + damsConstruction.getTablename());
                    if (map == null) {//检查仓位对应数据表是否存在 不存在则创
                        tableMapper.createVehicleDateTable(s + "_" + damsConstruction.getTablename());
                    } else {//存在则清空表数据
                        tableMapper.truncateDateTable(s + "_" + damsConstruction.getTablename());
                    }
                }
            }


            //清空缓存
            Map<String, MatrixItem[][]> storeHouseMaps2RollingData = storeHouseMap.getStoreHouses2RollingData();
            Map<String, StorehouseRange> shorehouseRange = storeHouseMap.getShorehouseRange();
            if (shorehouseRange.containsKey(damsConstruction.getId())) {
                shorehouseRange.remove(damsConstruction.getTablename());
            }
            if (storeHouseMaps2RollingData.containsKey(damsConstruction.getTablename())) {
                storeHouseMaps2RollingData.remove(damsConstruction.getTablename());
            }
            for (String s : tableprefix) {
                if ("".equals(s)) {
                    continue;
                }
                String tableName = s + "_t_1";
                //材料 层  查询原始表
                List<Integer> ids = new LinkedList<>();
                //List<T1> t1List = t1Service.selectByRanges(new T1VO(DataTimeUtil.timeToStamp(damsConstruction.getActualstarttime()), DataTimeUtil.timeToStamp(damsConstruction.getActualendtime()), damsConstruction.getXbegin(), damsConstruction.getXend(), damsConstruction.getYbegin(), damsConstruction.getYend(),tableName));

                Date start = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", damsConstruction.getActualstarttime());
                Date end = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", damsConstruction.getActualendtime());
                log.info(String.format("归档时 查询条件tablename%s《《《start%s《《end%s", tableName, start.getTime(), end.getTime()));
                List<T1> t1List = t1Service.selectByTime(tableName, start.getTime(), end.getTime());
                //2023年3月1日 14:57:48  修改为以高层+层高 作为高程范围来筛选数据，不使用时间范围。
//                BigDecimal endv = new BigDecimal(damsConstruction.getGaocheng()).add(new BigDecimal(damsConstruction.getHoudu()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));
//                log.info(String.format("归档时 查询条件tablename%s《《《begin_evolution%s>>>endevolution%s", tableName, damsConstruction.getGaocheng(),endv.doubleValue()));
//                List<T1> t1List = t1Service.selectByEvolution(tableName, damsConstruction.getGaocheng(), endv.doubleValue());

                log.info(String.format("归档时 查询到%s《《《《《条数据", t1List.size()));
                GeometryFactory geometryFactory = new GeometryFactory();
                List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
                Coordinate[] array = new Coordinate[list.size() + 1];
                list.toArray(array);
                array[list.size()] = array[0];
                Geometry bg = geometryFactory.createPolygon(array);
                for (T1 t1 : t1List) {
                    PointLocator a = new PointLocator();
                    Coordinate point = new Coordinate();
                    point.x = t1.getZhuangX();//桩x
                    point.y = t1.getZhuangY();//桩y
                    point.z = t1.getElevation();//高程

                    boolean p1 = a.intersects(point, bg);
                    if (p1) {
                        ids.add(t1.getId());

                    }

                }
                log.info(String.format("归档时 匹配到%s《《《《《条数据", ids.size()));
                if (ids.size() > 0) {
                    //分批插入 每次5千
                    int size = 5000;
                    List<List<Integer>> partition = ListUtils.partition(ids, size);
                    partition.stream().forEach(sublist -> {
                        //插入对应单元数据
                        t1Service.addTableData(new T1VO(sublist, s + "_" + damsConstruction.getTablename(), tableName));
                    });
                }
            }
            int r = damsConstructionService.updateDC(damsConstruction);
            new JtsRTree().init();


            return r;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @RequestMapping(value = "/openCang2", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult openCang2(HttpServletRequest request) {
        Integer id = Integer.valueOf(request.getParameter("id"));
        DamsConstruction damsConstruction = damsConstructionService.findById(id);
        if (damsConstruction == null) {
            return AjaxResult.error();
        }
        Map<String, Integer> result = new HashMap<>();
        String[] tableprefix = GlobCache.cartableprfix;

        for (String s : tableprefix) {
            if (!"".equals(s)) {
                Map<String, String> map = tableMapper.checkTableExistsWithSchema(s + "_" + damsConstruction.getTablename());
                if (map == null) {//检查仓位对应数据表是否存在 不存在则创
                    tableMapper.createVehicleDateTable(s + "_" + damsConstruction.getTablename());
                } else {//存在则清空表数据
                    tableMapper.truncateDateTable(s + "_" + damsConstruction.getTablename());
                }
            }
        }


        for (String s : tableprefix) {
            if ("".equals(s)) {
                continue;
            }
            String tableName = s + "_t_1";
            List<Integer> ids = new LinkedList<>();
            T1VO query = new T1VO();
            query.setTablename(tableName);
            query.setLayerid(id);
            List<T1> t1List = t1Service.select(query);
            ids = t1List.stream().map(t1 -> t1.getId().intValue()).collect(Collectors.toList());
            int size = 5000;
            List<List<Integer>> partition = ListUtils.partition(ids, size);
            partition.stream().forEach(sublist -> {
                //插入对应单元数据
                t1Service.addTableData(new T1VO(sublist, s + "_" + damsConstruction.getTablename(), tableName));
            });
            result.put(s, t1List.size());
        }

        //为该仓位生成报告
        try {
            AutoReportThread autoReportThread = new AutoReportThread(damsConstruction.getId(), 1, true);

            autoReportThread.call();
//            Thread reportthread = new Thread(autoReportThread);
//            reportthread.start();
            AutoHistoryThread autoHistoryThread = new AutoHistoryThread(damsConstruction.getId(), 1, true);
            autoHistoryThread.call();
        } catch (Exception e) {
            System.out.println("补录生成报告异常");
        }


        return AjaxResult.success(result);
    }


    /*闭 更改仓的status 从8变为2*/
    @RequestMapping(value = "/closeCang", method = RequestMethod.POST)
    @ResponseBody
    public int closeCang(ModelMap model, HttpServletRequest request) {
        Integer id = Integer.valueOf(request.getParameter("id"));
        DamsConstruction damsConstruction = damsConstructionService.findById(id);
        if (damsConstruction == null) {
            return 0;
        }
        DamsConstruction task = damsConstructionService.findById(id.intValue());
        RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        String key = GenerateRedisKey.taskrediskeysbycartypeCang(task.getTablename(), "ylj");
        Set<String> alldeletekey = redis.keys(key);
        for (String allbefore7key : alldeletekey) {
            redis.del(allbefore7key);
            System.out.println("闭仓清除redis任务数据");
        }
        damsConstruction.setStatus(2);


        //查询改仓位的实际施工时间
//      Map<String,Object> r =  tableMapper.seleminmaxdate(GlobCache.cartableprfix[1]+"_"+ damsConstruction.getTablename());
//       String begin =  DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,new Date(Long.valueOf(r.get("begin").toString())));
//       String end  =  DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,new Date(Long.valueOf(r.get("end").toString())));
//        damsConstruction.setActualstarttime(begin);
//        damsConstruction.setActualendtime(end);
        return damsConstructionService.updateDC(damsConstruction);
    }

    /**
     * 用于获取仓位的、施工时间、用料类型
     *
     * @return
     */
    @RequestMapping("/getdambyid")
    @ResponseBody
    public DamsConstruction getdmcbyid(HttpServletRequest request) {

        Integer id = Integer.valueOf(request.getParameter("id"));
        DamsConstruction damsConstruction = damsConstructionService.findById(id);
        return damsConstruction;
    }


    /*.计算合格率，返回给前端*/
    @GetMapping(value = "getInfoById")
    @ApiOperation(value = "根据仓位id获取合格率信息")
    @ResponseBody
    public Map<String, Object> getInfo(@RequestParam("id") Integer id) throws InterruptedException, ExecutionException, IOException {
        Map<String, Object> resultMap = new HashMap<>();
        DamsConstruction damsConstruction = damsConstructionService.findById(id);
        if (damsConstruction == null) {
            resultMap.put("code", 402);
            resultMap.put("msg", "未找到此仓位");
            return resultMap;
        }


        String userName = SecurityUtils.getUsername();
        JSONObject jsonObject2 = rollingDataService.getdam_rotate(userName, String.valueOf(id), 1);   //绘制历史碾压轨迹图
        QualifiedRate rate = rollingDataService.getQualifiedRate((RollingResult) jsonObject2.get("rollingResult"), damsConstruction.getFrequency());
        DecimalFormat df = new DecimalFormat("0.00");
        resultMap.put("code", 200);
        resultMap.put("msg", "获取合格率成功");
        resultMap.put("data", df.format(rate.getQualifiedtime() * 100.0f / rate.getAlltime()) + "%");


        return resultMap;
    }
}
