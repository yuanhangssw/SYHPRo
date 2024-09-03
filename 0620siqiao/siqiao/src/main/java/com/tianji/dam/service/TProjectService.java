package com.tianji.dam.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tianji.dam.domain.TDesign;
import com.tianji.dam.domain.TProject;
import com.tianji.dam.mapper.TProjectMapper;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
import com.tj.common.utils.StringUtils;

/**
 * 工程信息 服务层
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class TProjectService {

    @Autowired
    private TProjectMapper projectMapper;

    /**
     * 根据id查询
     * @param id 工程id
     * @return 工程详情
     */
    public TProject select(Long id){
        TProject param = new TProject();
        param.setId(id);
        List<TProject> list = select(param);
        return list.isEmpty()?null:list.get(0);
    }

    /**
     * 获取工程信息集合
     * @param param
     * @return 工程集合
     */
    public List<TProject> select(TProject param){
        return projectMapper.select(param);
    }
    
    public List<TDesign> getallpoint(){
    	return projectMapper.getallpoint();
    }
    

    /**
     * 添加或修改
     * @param param
     * @return 成功 true 失败 false
     */
    public int addOrUpdate(TProject param){
        return StringUtils.isNotNull(param.getId())?projectMapper.update(param):projectMapper.insert(param);
    }
}
