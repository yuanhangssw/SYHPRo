package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BDykePrevention;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 堤防防治情况Mapper接口
 * 
 * @author ruoyi
 * @date 2024-04-08
 */
public interface BDykePreventionMapper 
{
    /**
     * 查询堤防防治情况
     * 
     * @param id 堤防防治情况主键
     * @return 堤防防治情况
     */
    public BDykePrevention selectBDykePreventionById(Long id);

    /**
     * 查询堤防防治情况列表
     * 
     * @param bDykePrevention 堤防防治情况
     * @return 堤防防治情况集合
     */
    public List<BDykePrevention> selectBDykePreventionList(BDykePrevention bDykePrevention);

    /**
     * 新增堤防防治情况
     * 
     * @param bDykePrevention 堤防防治情况
     * @return 结果
     */
    public int insertBDykePrevention(BDykePrevention bDykePrevention);

    /**
     * 修改堤防防治情况
     * 
     * @param bDykePrevention 堤防防治情况
     * @return 结果
     */
    public int updateBDykePrevention(BDykePrevention bDykePrevention);

    /**
     * 删除堤防防治情况
     * 
     * @param id 堤防防治情况主键
     * @return 结果
     */
    public int deleteBDykePreventionById(Long id);

    /**
     * 批量删除堤防防治情况
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBDykePreventionByIds(Long[] ids);



    @Select("\tSELECT\n" +
            "\t*\n" +
            "FROM\n" +
            "\tb_dyke_prevention \n" +
            "WHERE\n" +
            "\tunit_id = #{unitId}  and  detriment_type=#{detrimentType}")
    public String selectBReservoirPreventionByUnitId(@Param("unitId") Long unitId, @Param("detrimentType") Long detrimentType);
}
