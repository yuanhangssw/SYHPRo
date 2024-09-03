package com.tianji.dam.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tianji.dam.domain.TAnalysisConfig;
import com.tianji.dam.mapper.TAnalysisConfigMapper;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
import com.tj.common.utils.StringUtils;

/**
 * 分析设置 服务层
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class TAnalysisConfigService {

    @Autowired
    private TAnalysisConfigMapper analysisConfigMapper;

    /**
     * 根据id查询
     * @param id 分析设置id
     * @return 分析设置详情
     */
    public TAnalysisConfig select(Long id){
        TAnalysisConfig param = new TAnalysisConfig();
        param.setId(id);
        List<TAnalysisConfig> list = select(param);
        return list.isEmpty()?null:list.get(0);
    }

    /**
     * 获取分析设置集合
     * @param param
     * @return 集合
     */
    public List<TAnalysisConfig> select(TAnalysisConfig param){
        return analysisConfigMapper.select(param);
    }

    /**
     * 添加或修改
     * @param param
     * @return 成功 true 失败 false
     */
    public int addOrUpdate(TAnalysisConfig param){
        return StringUtils.isNotNull(param.getId())?analysisConfigMapper.update(param):analysisConfigMapper.insert(param);
    }
    
}
