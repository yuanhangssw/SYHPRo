package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BInspectorUnit;
import org.apache.ibatis.annotations.Param;

/**
 * 巡查用户和巡查单元关联Mapper接口
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
public interface BInspectorUnitMapper 
{
    /**
     * 查询巡查用户和巡查单元关联
     * 
     * @param id 巡查用户和巡查单元关联主键
     * @return 巡查用户和巡查单元关联
     */
    public BInspectorUnit selectBInspectorUnitById(Long id);

    /**
     * 查询巡查用户和巡查单元关联列表
     * 
     * @param bInspectorUnit 巡查用户和巡查单元关联
     * @return 巡查用户和巡查单元关联集合
     */
    public List<BInspectorUnit> selectBInspectorUnitList(BInspectorUnit bInspectorUnit);

    /**
     * 新增巡查用户和巡查单元关联
     * 
     * @param bInspectorUnit 巡查用户和巡查单元关联
     * @return 结果
     */
    public int insertBInspectorUnit(BInspectorUnit bInspectorUnit);

    /**
     * 修改巡查用户和巡查单元关联
     * 
     * @param bInspectorUnit 巡查用户和巡查单元关联
     * @return 结果
     */
    public int updateBInspectorUnit(BInspectorUnit bInspectorUnit);

    /**
     * 删除巡查用户和巡查单元关联
     * 
     * @param id 巡查用户和巡查单元关联主键
     * @return 结果
     */
    public int deleteBInspectorUnitById(Long id);

    /**
     * 批量删除巡查用户和巡查单元关联
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBInspectorUnitByIds(Long[] ids);

    public int deleteBInspectorUnit(@Param("inspector")Long inspector,@Param("projectId") Long projectId);
}
