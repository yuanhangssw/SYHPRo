package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ruoyi.system.domain.BPatrol;
import com.ruoyi.system.domain.BPatrolUnitPlace;
import org.apache.ibatis.annotations.Select;

/**
 * 巡查Mapper接口
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
public interface BPatrolMapper 
{
    /**
     * 查询巡查
     * 
     * @param id 巡查主键
     * @return 巡查
     */
    public BPatrol selectBPatrolById(Long id);

    /**
     * 查询巡查列表
     * 
     * @param bPatrol 巡查
     * @return 巡查集合
     */
    public List<BPatrol> selectBPatrolList(BPatrol bPatrol);

    /**
     * 新增巡查
     * 
     * @param bPatrol 巡查
     * @return 结果
     */
    public int insertBPatrol(BPatrol bPatrol);

    /**
     * 修改巡查
     * 
     * @param bPatrol 巡查
     * @return 结果
     */
    public int updateBPatrol(BPatrol bPatrol);

    /**
     * 删除巡查
     * 
     * @param id 巡查主键
     * @return 结果
     */
    public int deleteBPatrolById(Long id);

    /**
     * 批量删除巡查
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBPatrolByIds(Long[] ids);

    @Select("SELECT\n" +
            "    id,\n" +
            "    patrol_unit,\n" +
            "    patrol_id,\n" +
            "    detriment_type,\n" +
            "    adress,\n" +
            "    lat,\n" +
            "    lon,\n" +
            "    inspector,\n" +
            "    project_id,\n" +
            "    DATE_FORMAT(create_time, '%Y-%m-%d %H:%i:%s')  create_time\n" +
            "FROM \n" +
            "    `b_patrol_unit_place`\n" +
            "where patrol_unit=#{patrolUnit}  and inspector=#{inspectorId} and project_id=#{projectId}\t\t")
    List<Map<String,Objects>> selectBPatrolList2(BPatrol bPatrol);

    @Select("SELECT \n" +
            "id,\n" +
            "patrol_unit,\n" +
            "patrol_id as patrolId,\n" +
            "detriment_type detrimentType,\n" +
            "adress,\n" +
            "lat,\n" +
            "lon,\n" +
            "inspector,\n" +
            "project_id  projectId FROM   `b_patrol_unit_place`   where  id=#{id}")
    BPatrolUnitPlace listall(int id);

    @Select("\tSELECT type_name  from  b_patrol_type  WHERE id=#{freedom3}\n ")
    String selectfreedom3(String freedom3);


    Map<String, Integer> selectZhishiWuNumbers(Map mapsparam);
}
