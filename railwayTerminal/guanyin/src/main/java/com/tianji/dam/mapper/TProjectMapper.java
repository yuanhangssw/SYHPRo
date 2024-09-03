package com.tianji.dam.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.tianji.dam.domain.TDesign;
import com.tianji.dam.domain.TProject;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;

/**
 * 工程信息接口
 */
@Mapper
@Component
@DataSource(value = DataSourceType.SLAVE)
public interface TProjectMapper {

    /**
     * 查询
     * @param vo
     * @return
     */
    List<TProject> select(TProject vo);

    /**
     * 添加
     */
    Integer insert(TProject vo);

    /**
     * 修改
     */
    Integer update(TProject vo);

    /**
     * 删除
     */
    public int delete(Long id);

    /**
     * 批量删除
     */
    public int deletes(Long[] ids);
    
    @Select("select * from t_design order by id asc")
    public List<TDesign> getallpoint();

}
