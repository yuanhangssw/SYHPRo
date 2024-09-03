package com.tj.web.controller.dam;

import com.github.pagehelper.PageHelper;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.*;
import com.tianji.dam.service.DistanceService;
import com.tianji.dam.service.SysconfigService;
import com.tianji.dam.service.TrackService;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/reservoir/distance")
@Api(tags = "工作量统计接口")
public class DistanceController extends BaseController {
    @Autowired
    DistanceService distanceService;
    @Autowired
    TrackService trackService;
    @Autowired
    SysconfigService sysconfigService;

    /**
     * 数据管理，分页查询轨迹和车辆信息
     * 查询条件有pageNum, pageSize, carId, startTime, endTime, startElevation, endElevation
     * 这个查询的时间是根据轨迹表t_track来查，类型与TTrack的时间一致，所以需要Long型的startTime,和endTime
     */
    @GetMapping("/findTrackCars")
    @ApiOperation(value = "数据管理-按条件查询轨迹和车辆信息", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = TrackCar.class, responseContainer = "List")
    })
    public TableDataInfo findTrackCars(QueryConditions qc) {
        PageHelper.startPage(qc.getPageNum(), qc.getPageSize(), "");
        List<TrackCar> trackCars = trackService.findTrackCar(qc);
        return new TableDataInfo(trackCars, trackCars.size());
    }

    /**
     * 单车-速度超限查询
     * 查询条件有sys_key, carId, startTime, endTime, startElevation, endElevation
     * 这个查询是根据轨迹表t_track来查，起止时间需要与TTrack的时间一致，所以查询条件需要Long型的startTime和endTime
     */
    @GetMapping("/findOverSpeed")
    @ApiOperation(value = "单车-查询超速数据", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = DateSpeed.class, responseContainer = "List")
    })
    public AjaxResult findCarOverSpeed(QueryConditions qc) {
        List<DateSpeed> tTrackList = trackService.findOverSpeed(qc);
        return AjaxResult.success("OK", tTrackList);
    }

    /**
     * 日-各车辆速度超限查询
     * 查询条件有sys_key, date, startElevation, endElevation
     */
    @GetMapping("/findDayOverSpeed")
    @ApiOperation(value = "日-查询各车辆超速数据", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = DateSpeed.class, responseContainer = "List")
    })
    public AjaxResult findDayOverSpeed(QueryConditions qc) {
        qc.setStartTimeStr(qc.getDate() + " 00:00:00");
        qc.setEndTimeStr(qc.getDate() + " 23:59:59");
        List<DateSpeed> tTrackList = trackService.findDayOverSpeed(qc);
        return AjaxResult.success("OK", tTrackList);
    }

    /**
     * 周-各车辆速度超限查询
     * 查询条件有sys_key, startTime, endTime, startElevation, endElevation
     * 这个查询是根据轨迹表t_track来查，起止时间需要与TTrack的时间一致，所以查询条件需要Long型的startTime和endTime
     */
    @GetMapping("/findWeekOverSpeed")
    @ApiOperation(value = "周-查询各车辆超速数据", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = DateSpeed.class, responseContainer = "List")
    })
    public AjaxResult findWeekOverSpeed(QueryConditions qc) {
        List<DateSpeed> tTrackList = trackService.findWeekOverSpeed(qc);
        return AjaxResult.success("OK", tTrackList);
    }


    /**
     * 单车工作量统计
     * 查询条件有carId, startTimeStr, endTimeStr
     * 这个查询是根据工作量表t_distance来查，起止时间需要与Distance的时间一致，所以查询条件需要String类型的startTimeStr和endTimeStr
     */
    @GetMapping("/findByDay")
    @ApiOperation(value = "单车-查询每天工作量", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = DistanceTime.class, responseContainer = "List")
    })
    public AjaxResult findCarDistance(QueryConditions qc) {
        List<DistanceTime> distanceTime = distanceService.findACar(qc);
        return AjaxResult.success("OK", distanceTime);
    }

    /**
     * 日-查询当天各车辆工作量
     * 查询条件有startTimeStr
     * 这个查询是根据工作量表t_distance来查，起止时间需要与Distance的时间一致，所以查询条件需要String类型的startTimeStr
     *
     * @return
     */
    @GetMapping("/findDayDistance")
    @ApiOperation(value = "日-查询每天各车辆工作量", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = DistanceTime.class, responseContainer = "List")
    })
    public AjaxResult findDayDistance(QueryConditions qc) {
        List<DistanceTime> dt = distanceService.findDaily(qc);
        return AjaxResult.success("OK", dt);
    }

    /**
     * 周-查询每天各车辆工作量
     * 查询条件有startTimeStr, endTimeStr
     * 这个查询是根据工作量表t_distance来查，起止时间需要与Distance的时间一致，所以查询条件需要String类型的startTimeStr和endTimeStr
     *
     * @return
     */
    @GetMapping("/findWeekDistance")
    @ApiOperation(value = "周-查询每天各车辆工作量", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = DistanceTime.class, responseContainer = "List")
    })
    public AjaxResult findWeekDistance(QueryConditions qc) {
        List<DistanceTime> dt = distanceService.findWeekly(qc);
        return AjaxResult.success("OK", dt);
    }

    /**
     * 单车-查询工作量统计的表格内容
     * 查询条件有pageNum, pageSize,carId, startTime, endTime, startElevation, endElevation
     */
    @GetMapping("/findStatistics")
    @ApiOperation(value = "单车-查询工作量统计的表格内容", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = TrackStatistic.class, responseContainer = "List")
    })
    public TableDataInfo findCarStatistics(QueryConditions qc) {

        String tablep = GlobCache.cartableprfix[qc.getCartype()];
        String tablename = tablep + "_t_1";
        qc.setTablename(tablename);
        PageHelper.startPage(qc.getPageNum(), qc.getPageSize());

        List<TrackStatistic> statisticsList = trackService.findStatistics(qc);
        return getDataTable(statisticsList);
    }

    /**
     * 日-查询工作量统计的表格内容
     * 查询条件有pageNum, pageSize,date, startElevation, endElevation
     */
    @GetMapping("/findDayStatistics")
    @ApiOperation(value = "日-查询工作量统计的表格内容", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = TrackStatistic.class, responseContainer = "List")
    })
    public TableDataInfo findDayStatistics(QueryConditions qc) {
        String tablep;
        if (qc.getCarId() != 0) {
            tablep = GlobCache.cartableprfix[qc.getCarId()];
        } else {
            tablep = "ylj";
        }
        String tablename = tablep + "_t_1";
        qc.setTablename(tablename);
        qc.setStartTimeStr(qc.getDate() + " 00:00:00");
        qc.setEndTimeStr(qc.getDate() + " 23:59:59");
        PageHelper.startPage(qc.getPageNum(), qc.getPageSize());
        List<TrackStatistic> statisticsList = trackService.findDayStatistics(qc);
        return getDataTable(statisticsList);
    }

    /**
     * 周-查询工作量统计的表格内容
     * 查询条件有pageNum, pageSize, startTime, endTime, startElevation, endElevation
     */
    @GetMapping("/findWeekStatistics")
    @ApiOperation(value = "周-查询工作量统计的表格内容", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = TrackStatistic.class, responseContainer = "List")
    })
    public TableDataInfo findWeekStatistics(QueryConditions qc) {
        PageHelper.startPage(qc.getPageNum(), qc.getPageSize());
        List<TrackStatistic> statisticsList = trackService.findWeekStatistics(qc);
        return getDataTable(statisticsList);
    }


}
