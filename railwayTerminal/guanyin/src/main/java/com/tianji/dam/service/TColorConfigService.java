package com.tianji.dam.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tianji.dam.domain.TColorConfig;
import com.tianji.dam.domain.vo.TColorConfigVO;
import com.tianji.dam.mapper.TColorConfigMapper;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
import com.tj.common.utils.StringUtils;

/**
 * 配色设置 服务层
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class TColorConfigService {

    @Autowired
    private TColorConfigMapper colorConfigMapper;

    /**
     * 根据id查询
     * @param id 配色设置id
     * @return 配色设置详情
     */
    public TColorConfig select(Long id){
        TColorConfig param = new TColorConfig();
        param.setId(id);
        List<TColorConfig> list = select(param);
        return list.isEmpty()?null:list.get(0);
    }

    /**
     * 获取配色设置集合
     * @param param
     * @return 集合
     */
    public List<TColorConfig> select(TColorConfig param){
        return colorConfigMapper.select(param);
    }

    /**
     * 获取配色设置集合
     * @return 集合
     */
    public List<TColorConfig> select(){
        return colorConfigMapper.select(new TColorConfig());
    }

    /**
     * 获取配色设置集合
     * @param param
     * @return 集合
     */
    public List<TColorConfig> select(TColorConfigVO param){
        return colorConfigMapper.selectByVo(param);
    }

    /**
     * 添加或修改
     * @param param
     * @return 成功 true 失败 false
     */
    public int addOrUpdate(TColorConfig param){
        return StringUtils.isNotNull(param.getId())?colorConfigMapper.update(param):colorConfigMapper.insert(param);
    }

    /**
     * 删除
     * @param id 配色id
     * @return
     */
    public int delete(Long id){
        return colorConfigMapper.delete(id);
    }
}
