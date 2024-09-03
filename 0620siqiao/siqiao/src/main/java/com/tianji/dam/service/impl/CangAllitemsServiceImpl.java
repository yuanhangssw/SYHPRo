package com.tianji.dam.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tianji.dam.mapper.CangAllitemsMapper;
import com.tianji.dam.domain.CangAllitems;
import com.tianji.dam.service.ICangAllitemsService;

/**
 * shujuService业务层处理
 * 
 * @author ruoyi
 * @date 2023-12-05
 */
@Service
public class CangAllitemsServiceImpl implements ICangAllitemsService 
{
    @Autowired
    private CangAllitemsMapper cangAllitemsMapper;

    /**
     * 查询shuju
     * 
     * @param id shuju主键
     * @return shuju
     */
    @Override
    public CangAllitems selectCangAllitemsById(Long id)
    {
        return cangAllitemsMapper.selectCangAllitemsById(id);
    }

    /**
     * 查询shuju列表
     * 
     * @param cangAllitems shuju
     * @return shuju
     */
    @Override
    public List<CangAllitems> selectCangAllitemsList(CangAllitems cangAllitems)
    {
        return cangAllitemsMapper.selectCangAllitemsList(cangAllitems);
    }

    /**
     * 新增shuju
     * 
     * @param cangAllitems shuju
     * @return 结果
     */
    @Override
    public int insertCangAllitems(CangAllitems cangAllitems)
    {
        return cangAllitemsMapper.insertCangAllitems(cangAllitems);
    }

    /**
     * 修改shuju
     * 
     * @param cangAllitems shuju
     * @return 结果
     */
    @Override
    public int updateCangAllitems(CangAllitems cangAllitems)
    {
        return cangAllitemsMapper.updateCangAllitems(cangAllitems);
    }

    /**
     * 批量删除shuju
     * 
     * @param ids 需要删除的shuju主键
     * @return 结果
     */
    @Override
    public int deleteCangAllitemsByIds(Long[] ids)
    {
        return cangAllitemsMapper.deleteCangAllitemsByIds(ids);
    }

    /**
     * 删除shuju信息
     * 
     * @param id shuju主键
     * @return 结果
     */
    @Override
    public int deleteCangAllitemsById(Long id)
    {
        return cangAllitemsMapper.deleteCangAllitemsById(id);
    }
}
