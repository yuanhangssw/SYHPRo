package com.tianji.dam.mapper;

import com.tianji.dam.domain.DayConstructionInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 日施工数据Mapper接口
 * 
 * @author liyan
 * @date 2023-11-30
 */
public interface DayConstructionInfoMapper 
{
    /**
     * 查询日施工数据
     * 
     * @param gid 日施工数据主键
     * @return 日施工数据
     */
    public DayConstructionInfo selectDayConstructionInfoByGid(Long gid);

    /**
     * 查询日施工数据列表
     * 
     * @param dayConstructionInfo 日施工数据
     * @return 日施工数据集合
     */
    public List<DayConstructionInfo> selectDayConstructionInfoList(DayConstructionInfo dayConstructionInfo);

    /**
     * 新增日施工数据
     * 
     * @param dayConstructionInfo 日施工数据
     * @return 结果
     */
    public int insertDayConstructionInfo(DayConstructionInfo dayConstructionInfo);

    /**
     * 修改日施工数据
     * 
     * @param dayConstructionInfo 日施工数据
     * @return 结果
     */
    public int updateDayConstructionInfo(DayConstructionInfo dayConstructionInfo);

    /**
     * 删除日施工数据
     * 
     * @param gid 日施工数据主键
     * @return 结果
     */
    public int deleteDayConstructionInfoByGid(Long gid);

    /**
     * 批量删除日施工数据
     * 
     * @param gids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDayConstructionInfoByGids(Long[] gids);

    @Select("select day,todayevolution from t_day_construction_info order by day desc limit 30 ")
    List<DayConstructionInfo> selectdayevolution();
    @Select("SELECT  ROUND(sum( base64->>'$.finalarea'),2)   FROM `t_report_save`")
    Double gettotalfl();

}
