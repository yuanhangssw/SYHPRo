package com.tj.web.controller.dam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.*;
import com.tianji.dam.domain.vo.T1RestAPI;
import com.tianji.dam.domain.vo.T1VO;
import com.tianji.dam.mapper.CarMapper;
import com.tianji.dam.mapper.RollingDataMapper;
import com.tianji.dam.mapper.T1Mapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.service.RollingDataService;
import com.tianji.dam.service.TDamsconstructionService;
import com.tianji.dam.thread.AreaRepairDataAutoProduct;
import com.tianji.dam.thread.AreaRepairDataAutoProduct_SimpleData;
import com.tj.common.annotation.RepeatSubmit;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.utils.DateUtils;
import com.tj.framework.web.service.SysLoginService;
import com.vividsolutions.jts.geom.Coordinate;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
@RequestMapping("/restapi")
public class RestApiController {

    @Autowired
    T1Mapper mapper;
    @Autowired
    private SysLoginService loginService;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private RollingDataMapper rollingDataMapper;
    @Autowired
    private TDamsconstructionService damsconstructionService;
    @Autowired
    private TDamsconstructionMapper tDamsconstructionMapper;
    @Autowired
    private RollingDataService rollingDataService;

    @GetMapping("/gettoken")
    @RepeatSubmit(interval = 10000, message = "请求过于频繁")
    public AjaxResult gettokenbyUser(String username, String password) {


        // 生成令牌
        String token = loginService.loginonlyname(username, password);

        return AjaxResult.success(token);

    }

    @GetMapping("/Vehicle/getVehiclehistory")
    @ResponseBody
    @RepeatSubmit(interval = 10000, message = "请求过于频繁")
    public AjaxResult getVehicledata_history(HttpServletRequest request) {
        AjaxResult result = new AjaxResult();
        try {
            String VehicleID = request.getParameter("VehicleID");
            Long begintime = Long.valueOf(request.getParameter("begintime"));
            Long endtime = Long.valueOf(request.getParameter("endtime"));
            int pagesize = Integer.valueOf(request.getParameter("pagesize"));
            int pageno = Integer.valueOf(request.getParameter("pageno"));
            PageHelper.startPage(pageno, pagesize, true);
            T1VO param = new T1VO();
            param.setPageNum(pageno);
            param.setPageSize(pagesize);
            param.setVehicleID(VehicleID);

            param.setBeginTime(begintime);
            param.setEndTime(endtime);


            List<T1RestAPI> list = mapper.selectrest(param);

            for (T1RestAPI t1RestAPI : list) {
                double a = t1RestAPI.getLatitude();
                double b = t1RestAPI.getLongitude();
                t1RestAPI.setLatitude(b);
                t1RestAPI.setLongitude(a);
            }


            result = AjaxResult.success("", list);
        } catch (Exception e) {
            e.printStackTrace();
            result = AjaxResult.error(e.getLocalizedMessage(), "");
        }
        return result;

    }

    @GetMapping("/Vehicle/getVehicle")
    @ResponseBody
    @RepeatSubmit(interval = 10000, message = "请求过于频繁")
    public Map<String, Object> getVehicledata(HttpServletRequest request) {

        Map<String, Object> result = new HashMap<>();
        try {
            int VehicleID = Integer.parseInt(request.getParameter("VehicleID"));
            T1RestAPI data = mapper.selecttop1(VehicleID);
            if (null != data) {
                double a = data.getLatitude();
                double b = data.getLongitude();
                data.setLatitude(b);
                data.setLongitude(a);
            }
            result.put("code", 200);
            result.put("data", data);

        } catch (Exception e) {
            System.out.println("api请求失败" + e.getLocalizedMessage());
            result = AjaxResult.error(e.getLocalizedMessage(), "");
        }
        return result;
    }

    @GetMapping("/ssCarInfo")
    public AjaxResult getCarinfoByType() {

        //查询所有车辆类型为洒水车

        Car query = new Car();
        query.setType(3);
        Map<String, Object> Carstatus = new HashMap<>();

        List<Map<String, Object>> onlinecarinfo = new ArrayList<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");
        Format dft = new SimpleDateFormat("MM-dd HH:mm:ss");
        List<Car> allcar = carMapper.getCarbyType(3);
        int online = 0;
        int offline = 0;
        for (Car car : allcar) {
            List<Date> timelist = carMapper.getcaronlinetime(car.getCarID());
            if (null == timelist || timelist.size() == 0) {
                List<Map<String, LocalDateTime>> offtimelist = carMapper.getcarofflinetime(car.getCarID());
                Map<String, Object> tempcar = new HashMap<>();
                tempcar.put("carid", car.getCarID());
                tempcar.put("carname", car.getRemark());


                if (offtimelist.size() > 0) {

                    for (Map<String, LocalDateTime> stringLocalDateTimeMap : offtimelist) {
                        Map<String, Object> tempcar2 = new HashMap<>();
                        tempcar2.put("carid", car.getCarID());
                        tempcar2.put("carname", car.getRemark());

                        tempcar2.put("onlinetime", stringLocalDateTimeMap.get("onlinetime").format(df));
                        tempcar2.put("offlinetime", stringLocalDateTimeMap.get("offlinetime").format(df));
                        onlinecarinfo.add(tempcar2);
                    }


                } else {
                    tempcar.put("onlinetime", "");
                    tempcar.put("offlinetime", "");
                    onlinecarinfo.add(tempcar);
                }

                offline++;
            } else {
                online++;
                Map<String, Object> tempcar = new HashMap<>();
                tempcar.put("carid", car.getCarID());
                tempcar.put("carname", car.getRemark());
                tempcar.put("onlinetime", dft.format(timelist.get(0)));
                tempcar.put("offlinetime", "");
                onlinecarinfo.add(tempcar);
            }


        }
        Carstatus.put("online", online);
        Carstatus.put("offline", offline);
        Carstatus.put("onlinecarinfo", onlinecarinfo);
        return AjaxResult.success(Carstatus);
    }


    @GetMapping("/getsscarhistory")
    public AjaxResult getsscarhistory(String carid, String begintime, String endtime) {
        begintime = "06-20 04:00:00";
        endtime = "06-20 05:00:00";
        String year = DateUtils.dateTimeNow("yyyy");
        String fullbegin = year + "-" + begintime;
        String fullend = year + "-" + endtime;
        String tablename = "ssc_t_1";

        Long beginl = DateUtils.parseDate(fullbegin).getTime();
        Long endl = DateUtils.parseDate(fullend).getTime();

        List<RollingData> alldata = rollingDataMapper.getRollingLatLngDataByDateRange(tablename, carid, beginl, endl);

        return AjaxResult.success(alldata);

    }

    @GetMapping("/checkalive")
    public int checkalive() {

        return 200;

    }


    @GetMapping("/getdambypid")
    public AjaxResult getdamarea(String pid){
        List all =  damsconstructionService.getAll(pid);
        return  AjaxResult.success(all);
    }

    /**
     * 通过状态查询仓位信息
     * @param status
     * @return
     */
    @GetMapping("/getdaminfo")
    public AjaxResult getdaminfo(Integer status){
        List<DamsConstruction> alls =  damsconstructionService.findByStatus(status);
        return  AjaxResult.success(alls);

    }

    /**
     * 查询仓位信息表的所有数据
     * @param tablename
     * @return
     */
    @GetMapping("/getdamdata")
    public AjaxResult getdamdata(String tablename,String type){
        Long begintime =0l;
        Long endtime =9679890324000l;
        if("d".equals(type)){
            begintime =DateUtils.dateTime(DateUtils.YYYY_MM_DD,DateUtils.getTime()).getTime();
            endtime = new Date().getTime();
        }
        List<T1> alldata = mapper.selectallbasedata(tablename,begintime,endtime);
        return  AjaxResult.success(alldata);
    }


    @GetMapping("/getcengdatabycang/{cangid}")
  public AjaxResult getcengdatabycang(@PathVariable("cangid") Integer cangid){

        DamsConstruction damsConstruction = tDamsconstructionMapper.selectByPrimaryKey(Integer.valueOf(cangid));
        List<Car> carList = carMapper.findCar();
        //2024年3月26日 14:44:31 拿到这一层所有的仓位数据进行数据生成，

        String encode = damsConstruction.getEngcode();
        Integer pid=   damsConstruction.getPid();
        List<DamsConstruction> allcangs =        tDamsconstructionMapper.getalltabbypidandencode(pid,encode);
              List<Map<String,Object>> cardata = new ArrayList<>();
        for (DamsConstruction allcang : allcangs) {
            for (Car car : carList) {
                String  tableName =   "ylj_" + allcang.getTablename();
                List<Map<String,Object>> temp    = rollingDataMapper.getsimpledataVehicleID(tableName, car.getCarID().toString());

                cardata.addAll(temp);
            }
        }
        return AjaxResult.success(cardata);
    }

    @GetMapping("/getHistoryPicByceng/{pid}/{ceng}/{cartype}")
    @ResponseBody
    public JSONObject getHistoryPicByceng(HttpServletRequest request, @PathVariable("pid") Integer pid, @PathVariable("ceng") Integer ceng, @PathVariable("cartype") Integer cartype)  {
        /*取得当前的用户，后面根据用户名放置网格内存*/

        //首先查询是否存在已经保存的 存在这直接使用已经保存的
//        THistoryPic select = new THistoryPic();
//        select.setDamid(Long.valueOf(tableName));
//        select.setHtype(cartype.longValue());
//        List<THistoryPic> selects = historyPicMapper.selectTHistoryPicList(select);
//        if (!selects.isEmpty()) {
//            String res = selects.get(0).getContent();
//
//            return JSONObject.parseObject(res);
//        }

        return rollingDataService.getHistoryPicbyalltabledata(pid,ceng, cartype);
    }



    @GetMapping("/tmakedata")
    public AjaxResult makeData(String ranges){
        TRepairData vo =new TRepairData();
        ranges =    "[[[4833,5731.615],[4833,5772.9],[4781.112,5772.9],[4781.895,5768.605],[4782.646,5764.155],[4783.383,5759.392],[4784.176,5753.674],[4784.955,5747.195],[4785.525,5741.607],[4786.121,5734.414],[4786.558,5727.335],[4786.896,5718.428],[4787,5686.078],[4788,5686.078],[4788,5693],[4821.5,5693],[4821.5,5731],[4832,5731.615],[4833,5731.615]]]";
        String  voranges="";
        voranges =  convertRangesToLists2(ranges);
        vo.setRanges(voranges);
        vo.setStartTime(new Date().getTime()+"");
        vo.setSpeed(5.0d);
        vo.setColorId(5);

        List<Coordinate> list = JSONArray.parseArray(voranges, Coordinate.class);
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
        AreaRepairDataAutoProduct_SimpleData  autoProduct =new AreaRepairDataAutoProduct_SimpleData();

        autoProduct.setRepairData(vo);
        try {
            Thread t = new Thread(autoProduct);
            t.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return AjaxResult.success();
    }

    public static void convertRangesToLists(String ranges, List<Coordinate> result) {
        // 移除最外层的两个方括号
        ranges = ranges.substring(2, ranges.length() - 2);

        // 使用正则表达式匹配每一对坐标
        Pattern pattern = Pattern.compile("\\[(\\d+\\.\\d+),\\s*(\\d+\\.\\d+)\\]");
        Matcher matcher = pattern.matcher(ranges);

        while (matcher.find()) {
            Coordinate temp = new Coordinate(Double.parseDouble(matcher.group(1)),Double.parseDouble(matcher.group(2)));
            result.add(temp);
        }
    }
    public static String convertRangesToLists2(String ranges) {
        // 移除最外层的两个方括号
        ranges = ranges.substring(2, ranges.length() - 2);

        // 使用正则表达式匹配每一对坐标
        Pattern pattern = Pattern.compile("\\[(\\d+\\.\\d+),\\s*(\\d+\\.\\d+)\\]");
        Matcher matcher = pattern.matcher(ranges);
            String ranges2 ="[";
        while (matcher.find()) {
            ranges2+="{'x':"+Double.parseDouble(matcher.group(1))*10.0d+",'y':"+Double.parseDouble(matcher.group(2))*10.0d+"}";
        }
      String  result= ranges2+"]";
        return  result;
    }

}
