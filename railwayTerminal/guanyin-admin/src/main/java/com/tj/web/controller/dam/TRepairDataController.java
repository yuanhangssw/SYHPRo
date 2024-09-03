package com.tj.web.controller.dam;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.bean.JtsRTree;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.Material;
import com.tianji.dam.domain.T1;
import com.tianji.dam.domain.TRepairData;
import com.tianji.dam.domain.vo.DamsJtsTreeVo;
import com.tianji.dam.domain.vo.T1VO;
import com.tianji.dam.mapper.*;
import com.tianji.dam.service.RollingDataService;
import com.tianji.dam.service.T1Service;
import com.tianji.dam.thread.AreaRepairDataAutoProduct;
import com.tianji.dam.thread.AutoHistoryThread;
import com.tianji.dam.thread.AutoReportThread;
import com.tianji.dam.utils.DataTimeUtil;
import com.tj.common.annotation.RepeatSubmit;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.core.page.TableDataInfo;
import com.tj.common.utils.DateUtils;
import com.tj.common.utils.StringUtils;
import com.vividsolutions.jts.algorithm.PointLocator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin
@Api(value = "人工补录controller", tags = {"人工补录管理操作接口"})
@RestController
@RequestMapping("/reservoir/repairData")
@Slf4j
public class TRepairDataController extends BaseController {

    @Autowired
    private TRepairDataMapper repairDataMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private TableMapper tableMapper;
    @Autowired
    private TDamsconstructionMapper damsconstructionMapper;
    @Autowired
    private T1Service t1Service;
    @Autowired
    private RollingDataService rollingDataService;

    @GetMapping("/list")
    @ApiOperation(value = "分页查询", response = TRepairData.class, nickname = "list")
    public TableDataInfo list(TRepairData param) {
        try {


            PageHelper.startPage(param.getPageNum(), param.getPageSize(), "");
            List<TRepairData> list = repairDataMapper.selectTRepairDatasList(param);
            List<TRepairData> resList = new LinkedList<>();
            for (TRepairData data : list) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d_re = new Date(Long.parseLong(data.getRepairTime()));
                Date d_begin = new Date(Long.parseLong(data.getStartTime()));
                Date d_end = new Date(Long.parseLong(data.getEndTime()));
                Material material = materialMapper.selectByPrimaryKey(Integer.valueOf(data.getMaterialname()));
                data.setMaterialValue(material.getMaterialname());
                data.setRepairTime(sdf.format(d_re));
                data.setStartTime(sdf.format(d_begin));
                data.setEndTime(sdf.format(d_end));
                resList.add(data);
            }
            return getDataTable(list);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "根据id获取详细信息", response = TRepairData.class, nickname = "getInfo")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int")
    public AjaxResult getInfo(@PathVariable int id) {
        try {
            return AjaxResult.success(repairDataMapper.selectByPrimaryKey(id));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error();
    }

    @PostMapping
    @RepeatSubmit
    @ApiOperation(value = "新增", response = TRepairData.class, nickname = "add")
    public AjaxResult add(@Validated @RequestBody TRepairData vo) {
        if (StringUtils.isNotNull(vo.getRanges())) {
            List<Coordinate> list = JSONArray.parseArray(vo.getRanges(), Coordinate.class);
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
                vo.setXbegin(xMin);
                vo.setXend(xMax);
                vo.setYbegin(yMin);
                vo.setYend(yMax);
            }
            if (1 == vo.getRepairtype()) {
                //首先清除旧的模拟数据
                // repairDataMapper.deleteByType(vo.getDamsid());
                AreaRepairDataAutoProduct autoProduct = new AreaRepairDataAutoProduct();
                autoProduct.setRepairData(vo);
                try {
                    Thread t = new Thread(autoProduct);
                    t.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }

        return toAjax(repairDataMapper.insertSelective(vo));

    }

    @PutMapping
    @ApiOperation(value = "修改", response = TRepairData.class, nickname = "edit")
    public AjaxResult edit(@Validated @RequestBody TRepairData vo) {
        if (StringUtils.isNotNull(vo.getRanges())) {
            List<Coordinate> list = JSONArray.parseArray(vo.getRanges(), Coordinate.class);
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
                vo.setXbegin(xMin);
                vo.setXend(xMax);
                vo.setYbegin(yMin);
                vo.setYend(yMax);
            }
            if (1 == vo.getRepairtype()) {
                //首先清除旧的模拟数据

                // repairDataMapper.deleteByType(vo.getDamsid());

                AreaRepairDataAutoProduct autoProduct = new AreaRepairDataAutoProduct();
                autoProduct.setRepairData(vo);
                try {
                    Thread t = new Thread(autoProduct);
                    t.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
        return toAjax(repairDataMapper.updateByPrimaryKeySelective(vo));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", response = TRepairData.class, nickname = "remove")
    public AjaxResult remove(@PathVariable("id") int id) {

        TRepairData vo = repairDataMapper.selectByPrimaryKey(id);

        DamsConstruction com = damsconstructionMapper.selectByPrimaryKey(vo.getDamsid());
        //首先清空数据。
        if (vo.getRepairtype() == 1) {//删除模拟数据时，会清除仓位所有数据。
            String tablepre = GlobCache.cartableprfix[vo.getCartype()];
            tableMapper.truncateDateTable2(tablepre + "_" + com.getTablename(), vo.getCarid());
            //删除保存的报表数据
            tableMapper.deletesaveold(com.getId());
            repairDataMapper.deleteByPrimaryKey(id);

            TRepairData query = new TRepairData();
            query.setCartype(vo.getCartype());
            query.setDamsid(vo.getDamsid());
          List<TRepairData> allre =       repairDataMapper.selectTRepairDatas(query);

            //如果删除完所有补录，为了避免数据空白， 需要从新执行开仓逻辑将总表中的数据从新导入到仓位表。
                if(allre.isEmpty()){
                    try {
                        reloadt_1tocang(com,vo.getCartype());
                    } catch (Exception e) {

                        log.info("重新导入数据失败。。");
                    }
                }

        }


        return toAjax(1);
    }

    @GetMapping("/dam/{id}")
    @ApiOperation(value = "删除", response = TRepairData.class, nickname = "remove")
    public AjaxResult removedam(@PathVariable("id") int id) {

        DamsConstruction com = damsconstructionMapper.selectByPrimaryKey(id);
        int r = tableMapper.truncateDateTable(com.getTablename());

        return toAjax(1);
    }


    /**
     * 补录完成以后点击生成数据
     *
     * @param
     */
    @GetMapping("/makedata/{damid}")
    @RepeatSubmit(interval = 10000, message = "请求过于频繁")
    public void makedata(@PathVariable("damid") Integer damid) {
        // 补录以后从新生成仓位的报告、平面分析

        TReportSaveMapper reportSaveMapper = BeanContext.getApplicationContext().getBean(TReportSaveMapper.class);
        int[] types = {1};
        // int[] types = {1, 2};
        for (int type : types) {
            AutoReportThread autoReportThread = new AutoReportThread(damid, type, true);

            autoReportThread.call();
//            Thread reportthread = new Thread(autoReportThread);
//            reportthread.start();

            AutoHistoryThread autoHistoryThread = new AutoHistoryThread(damid, type, true);
            autoHistoryThread.call();
//            Thread thread = new Thread(autoHistoryThread);
//            thread.start();

        }

    }


    @GetMapping("/autorepair")
    public void autorepair(Integer damid, int begin, int type) {

        List<DamsConstruction> allchild = damsconstructionMapper.getdambypid(damid);

        for (DamsConstruction damsConstruction : allchild) {

            //自动补录时先清除旧的同类型补录数据

            TRepairData vo = new TRepairData();
            if (type == 1) {//正常全区域补录
                vo.setRanges(damsConstruction.getRanges());
                vo.setColorId(damsConstruction.getFrequency());
                vo.setSpeed(damsConstruction.getSpeed());
                vo.setVibration(70.0);
                damsconstructionMapper.deleterepairbydamadntype(damsConstruction.getId(), 0);
            } else if (type == 2) {//下游堆石料
                if (Integer.parseInt(damsConstruction.getEngcode().toString()) < begin) {
                    continue;
                }
//                if(index==1){
//                    vo.setRanges("[{\"x\":2773.743638116661,\"y\":1366.5768619933867},{\"x\":2784.5190818428696,\"y\":1345.8560163291545},{\"x\":2804.8711166247103,\"y\":1338.5332430583733},{\"x\":2814.6240095440917,\"y\":1346.9138274999648}]");
//                }else if(index==2){
//                    vo.setRanges("[{\"x\":2797.0675054152725,\"y\":1074.5156993366609},{\"x\":2793.92987761866,\"y\":1046.5742485641308},{\"x\":2821.95728939469,\"y\":1049.568534179069},{\"x\":2825.979098726801,\"y\":1069.5031604492342}]");
//                }else if(index==3){
//                    vo.setRanges("[{\"x\":2780.9230538089964,\"y\":1685.5315623713468},{\"x\":2798.9882003575203,\"y\":1677.5255916951196},{\"x\":2816.9537640445055,\"y\":1689.5103792761215},{\"x\":2801.9785884075895,\"y\":1692.500767326191}]");
//                }
                vo.setColorId(0);
                vo.setSpeed(0.0d);
                vo.setVibration(0.0d);
            }

            vo.setRepairtype(0);
            vo.setDamsid(damsConstruction.getId());
            vo.setMaterialname(damsConstruction.getMaterialname());
            vo.setStartTime(DataTimeUtil.timeToStamp(damsConstruction.getActualstarttime()) + "");
            vo.setEndTime(DataTimeUtil.timeToStamp(damsConstruction.getActualendtime()) + "");

            vo.setTitle(damsConstruction.getTitle() + DateUtils.parseDateToStr("yyyymmddmmss", new Date()));
            vo.setRepairTime(new Date().getTime() + "");
            if (StringUtils.isNotNull(vo.getRanges())) {
                List<Coordinate> list = JSONArray.parseArray(vo.getRanges(), Coordinate.class);
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
                    vo.setXbegin(xMin);
                    vo.setXend(xMax);
                    vo.setYbegin(yMin);
                    vo.setYend(yMax);
                }

            }
            repairDataMapper.insertSelective(vo);

        }

    }

    public  void  reloadt_1tocang(DamsConstruction damsConstruction,int cartype) throws  Exception{

        String regex = "\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}";
        Pattern pattern = Pattern.compile(regex);
        String dateFormate = "yyyy-MM-dd";
        List<Integer> ids = new LinkedList<>();

        Matcher matcher = pattern.matcher(damsConstruction.getActualstarttime());
        if (matcher.matches()) {
            dateFormate = "yyyy-MM-dd HH:mm:ss";
        } else {
            dateFormate = "yyyy-MM-dd";
        }
        DateFormat df = new SimpleDateFormat(dateFormate);
        Date start = df.parse(damsConstruction.getActualstarttime());
        Date end = df.parse(damsConstruction.getActualendtime());
        String tableName = GlobCache.cartableprfix[cartype]+"_t_1";

        log.info(String.format("归档时 查询条件tablename%s《《《start%s《《end%s", tableName, start.getTime(), end.getTime()));
        List<T1> t1List = t1Service.selectByTime(tableName, start.getTime(), end.getTime());
        log.info(String.format("归档时 查询到%s《《《《《条数据", t1List.size()));
        for (T1 t1 : t1List) {
            if (t1.getZhuangX() != null && t1.getZhuangY() != null) {
                List<DamsJtsTreeVo> treeVos = JtsRTree.query(t1.getZhuangX(), t1.getZhuangY());
                if (!treeVos.isEmpty()) {
                    for (DamsJtsTreeVo tree : treeVos) {
                        if (tree.getId() == damsConstruction.getId()) {
                            PointLocator a = new PointLocator();
                            Coordinate point = new Coordinate();
                            point.x = t1.getZhuangX();//桩x
                            point.y = t1.getZhuangY();//桩y
                            point.z = t1.getElevation();//高程
                            GeometryFactory geometryFactory = new GeometryFactory();
                            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
                            Coordinate[] array = new Coordinate[list.size() + 1];
                            list.toArray(array);
                            array[list.size()] = array[0];
                            Geometry bg = geometryFactory.createPolygon(array);
                            boolean p1 = a.intersects(point, bg);
                            if (p1) {
                                ids.add(t1.getId());
                                break;
                            }
                        }
                    }
                }
            }
        }
        log.info(String.format("归档时 匹配到%s《《《《《条数据", ids.size()));
        if (ids.size() > 0) {
            //分批插入 每次5千
            int size = 5000;
            List<List<Integer>> partition = ListUtils.partition(ids, size);
            partition.stream().forEach(sublist -> {
                //插入对应单元数据
                t1Service.addTableData(new T1VO(sublist, GlobCache.cartableprfix[cartype] + "_" + damsConstruction.getTablename(), tableName));
            });
        }
    }


}
