package com.tianji.dam.service.serviceimpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tianji.dam.domain.TSpeedWarming;
import com.tianji.dam.mapper.TSpeedWarmingMapper;
import com.tianji.dam.service.ITSpeedWarmingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 预警信息Service业务层处理
 * 
 * @author tianji_liyan
 * @date 2021-12-28
 */
@Service
public class TSpeedWarmingServiceImpl implements ITSpeedWarmingService 
{
    @Autowired
    private TSpeedWarmingMapper tSpeedWarmingMapper;

    /**
     * 查询预警信息
     * 
     * @param gid 预警信息主键
     * @return 预警信息
     */
    @Override
    public TSpeedWarming selectTSpeedWarmingByGid(String gid)
    {
        return tSpeedWarmingMapper.selectTSpeedWarmingByGid(gid);
    }

    /**
     * 查询预警信息列表
     * 
     * @param tSpeedWarming 预警信息
     * @return 预警信息
     */
    @Override
    public List<TSpeedWarming> selectTSpeedWarmingList(TSpeedWarming tSpeedWarming)
    {
        return tSpeedWarmingMapper.selectTSpeedWarmingList(tSpeedWarming);
    }

    /**
     * 新增预警信息
     * 
     * @param tSpeedWarming 预警信息
     * @return 结果
     */
    @Override
    public int insertTSpeedWarming(TSpeedWarming tSpeedWarming)
    {
        return tSpeedWarmingMapper.insertTSpeedWarming(tSpeedWarming);
    }

    /**
     * 修改预警信息
     * 
     * @param tSpeedWarming 预警信息
     * @return 结果
     */
    @Override
    public int updateTSpeedWarming(TSpeedWarming tSpeedWarming)
    {
        return tSpeedWarmingMapper.updateTSpeedWarming(tSpeedWarming);
    }

    /**
     * 批量删除预警信息
     * 
     * @param gids 需要删除的预警信息主键
     * @return 结果
     */
    @Override
    public int deleteTSpeedWarmingByGids(String[] gids)
    {
        return tSpeedWarmingMapper.deleteTSpeedWarmingByGids(gids);
    }

    /**
     * 删除预警信息信息
     * 
     * @param gid 预警信息主键
     * @return 结果
     */
    @Override
    public int deleteTSpeedWarmingByGid(String gid)
    {
        return tSpeedWarmingMapper.deleteTSpeedWarmingByGid(gid);
    }

	@Override
	public List<TSpeedWarming> selectTSpeedWarmingList2(TSpeedWarming tsw) {
		   return tSpeedWarmingMapper.selectTSpeedWarmingList2(tsw);
	}

    @Override
    public int updateStatus(String ids) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
         JSONObject JSONS =    JSONObject.parseObject(ids);

            JSONArray jsonNode = JSONS.getJSONArray("ids");

            String [] s =new String[jsonNode.size()];
            jsonNode.toArray(s);
            return tSpeedWarmingMapper.updateStatus(s);
        } catch (Exception e) {
            System.out.println("数据处理失败");
        }
        return 0;
    }
}
