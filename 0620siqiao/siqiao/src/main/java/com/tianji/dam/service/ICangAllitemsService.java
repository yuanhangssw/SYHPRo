package com.tianji.dam.service;

import java.util.List;
import com.tianji.dam.domain.CangAllitems;

/**
 * shujuService接口
 * 
 * @author ruoyi
 * @date 2023-12-05
 */
public interface ICangAllitemsService 
{
    /**
     * 查询shuju
     * 
     * @param id shuju主键
     * @return shuju
     */
    public CangAllitems selectCangAllitemsById(Long id);

    /**
     * 查询shuju列表
     * 
     * @param cangAllitems shuju
     * @return shuju集合
     */
    public List<CangAllitems> selectCangAllitemsList(CangAllitems cangAllitems);

    /**
     * 新增shuju
     * 
     * @param cangAllitems shuju
     * @return 结果
     */
    public int insertCangAllitems(CangAllitems cangAllitems);

    /**
     * 修改shuju
     * 
     * @param cangAllitems shuju
     * @return 结果
     */
    public int updateCangAllitems(CangAllitems cangAllitems);

    /**
     * 批量删除shuju
     * 
     * @param ids 需要删除的shuju主键集合
     * @return 结果
     */
    public int deleteCangAllitemsByIds(Long[] ids);

    /**
     * 删除shuju信息
     * 
     * @param id shuju主键
     * @return 结果
     */
    public int deleteCangAllitemsById(Long id);
}
