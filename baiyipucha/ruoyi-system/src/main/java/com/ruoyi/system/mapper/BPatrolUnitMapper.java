package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BPatrolUnit;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 巡查单元Mapper接口
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
public interface BPatrolUnitMapper 
{
    /**
     * 查询巡查单元
     * 
     * @param id 巡查单元主键
     * @return 巡查单元
     */
    public BPatrolUnit selectBPatrolUnitById(Long id);

    /**
     * 查询巡查单元列表
     * 
     * @param bPatrolUnit 巡查单元
     * @return 巡查单元集合
     */
    public List<BPatrolUnit> selectBPatrolUnitList(BPatrolUnit bPatrolUnit);

    /**
     * 新增巡查单元
     * 
     * @param bPatrolUnit 巡查单元
     * @return 结果
     */
    public int insertBPatrolUnit(BPatrolUnit bPatrolUnit);

    /**
     * 修改巡查单元
     * 
     * @param bPatrolUnit 巡查单元
     * @return 结果
     */
    public int updateBPatrolUnit(BPatrolUnit bPatrolUnit);

    /**
     * 删除巡查单元
     * 
     * @param id 巡查单元主键
     * @return 结果
     */
    public int deleteBPatrolUnitById(Long id);

    /**
     * 批量删除巡查单元
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBPatrolUnitByIds(Long[] ids);


    @Select(" delete from b_inspector_unit where project_id = #{projectId}")
    public void deleteBPatrolUnitByProject(Long projectId);

    @Select("SELECT\n" +
            "\t* \n" +
            "FROM\n" +
            "\tb_patrol_unit \n" +
            "WHERE\n" +
            "\tid IN (\n" +
            "\tSELECT\n" +
            "\t\tpatrol_unit_id \n" +
            "\tFROM\n" +
            "\t\t`b_inspector_unit` \n" +
            "\tWHERE\n" +
            "\t\tproject_id = #{projectId} \n" +
            "\tAND inspector = #{userId})\n")
    List<BPatrolUnit> selectBPatrolUnitByUserAndProject(@Param("userId") int userId,@Param("projectId") int projectId);


    //根据项目id查看该项目下有多少单元
    @Select("SELECT count(*) from \t b_patrol_unit where project_id= #{id}")
    int selectUnitNumbers(Long id);
}
