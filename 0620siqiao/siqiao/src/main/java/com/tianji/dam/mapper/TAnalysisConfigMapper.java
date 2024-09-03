package com.tianji.dam.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.tianji.dam.domain.TAnalysisConfig;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;

/**
 * 分析设置接口
 */
@Mapper
@Component
@DataSource(value = DataSourceType.SLAVE)
public interface TAnalysisConfigMapper {

    /**
     * 查询
     * @param vo
     * @return
     */
    List<TAnalysisConfig> select(TAnalysisConfig vo);

    /**
     * 添加
     */
    Integer insert(TAnalysisConfig vo);

    /**
     * 修改
     */
    Integer update(TAnalysisConfig vo);

    /**
     * 删除
     */
    int delete(Long id);

    /**
     * 批量删除
     */
    int deletes(Long[] ids);

    /**
     *查询底图位置映射
     * @return t_analysis_config
     */
    @Select("SELECT * FROM t_analysis_config WHERE id = 2")
    TAnalysisConfig getPicPath();

    /**
     * 查询id最大的一条记录
     * @return
     */
    @Select("select id,num,speed,rate,path,x,y,z," +
            "four_param_x as fourParamX," +
            "four_param_y as fourParamY," +
            "four_param_z as fourParamZ," +
            "four_param_angle as fourParamAngle," +
            "four_param_factor as fourParamFactor from t_analysis_config ORDER BY id desc limit 1")
    TAnalysisConfig selectMaxIdOne();
}
