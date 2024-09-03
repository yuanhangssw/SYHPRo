package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BReservoirPrevention;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 水库防治情况Mapper接口
 * 
 * @author ruoyi
 * @date 2024-04-08
 */
public interface BReservoirPreventionMapper 
{
    /**
     * 查询水库防治情况
     * 
     * @param id 水库防治情况主键
     * @return 水库防治情况
     */
    public BReservoirPrevention selectBReservoirPreventionById(Long id);

    /**
     * 查询水库防治情况列表
     * 
     * @param bReservoirPrevention 水库防治情况
     * @return 水库防治情况集合
     */
    public List<BReservoirPrevention> selectBReservoirPreventionList(BReservoirPrevention bReservoirPrevention);

    /**
     * 新增水库防治情况
     * 
     * @param bReservoirPrevention 水库防治情况
     * @return 结果
     */
    public int insertBReservoirPrevention(BReservoirPrevention bReservoirPrevention);

    /**
     * 修改水库防治情况
     * 
     * @param bReservoirPrevention 水库防治情况
     * @return 结果
     */
    public int updateBReservoirPrevention(BReservoirPrevention bReservoirPrevention);

    /**
     * 删除水库防治情况
     * 
     * @param id 水库防治情况主键
     * @return 结果
     */
    public int deleteBReservoirPreventionById(Long id);

    /**
     * 批量删除水库防治情况
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBReservoirPreventionByIds(Long[] ids);

    @Select("   SELECT\n" +
            "        id,\n" +
            "        detriment_type,\n" +
            "        project_id,\n" +
            "        unit_id,\n" +
            "        detriment_level,\n" +
            "        leaks_number,\n" +
            "        through_number,\n" +
            "        drop_socket_number,\n" +
            "        nest_digging,\n" +
            "        charge_area,\n" +
            "        grouting_quantity,\n" +
            "        invest_capital,\n" +
            "        zoon_type,\n" +
            "        zoon_govern_number,\n" +
            "        zoon_govern_type,\n" +
            "        create_time\n" +
            "        FROM\n" +
            "        b_reservoir_prevention\n" +
            "        WHERE\n" +
            "        unit_id = #{unitId}   and  detriment_type=#{detrimentType}")
    public String selectBReservoirPreventionByUnitId(@Param("unitId") Long unitId, @Param("detrimentType")String detrimentType);
}
