package com.tianji.dam.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.tianji.dam.domain.TColorConfig;
import com.tianji.dam.domain.vo.TColorConfigVO;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;

/**
 * 配色设置接口
 */
@Mapper
@Component
@DataSource(value = DataSourceType.SLAVE)
public interface TColorConfigMapper {

    /**
     * 查询
     * @param vo
     * @return
     */
    List<TColorConfig> select(TColorConfig vo);

    /**
     * 查询
     * @param vo
     * @return
     */
    List<TColorConfig> selectByVo(TColorConfigVO vo);

    /**
     * 添加
     */
    Integer insert(TColorConfig vo);

    /**
     * 修改
     */
    Integer update(TColorConfig vo);

    /**
     * 删除
     */
    public int delete(Long id);

    /**
     * 批量删除
     */
    public int deletes(Long[] ids);
    
    TColorConfig getById(Long id);

}
