package com.tianji.dam.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.tianji.dam.domain.TAnalysisConfig;
import com.tianji.dam.domain.TDesign;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;

@Component
@Mapper
@DataSource(value = DataSourceType.SLAVE)
public interface CommonMapper {
	
	
	
	@Select("select * from t_design where type =${type}")
	public List<TDesign> getallpointbytype(@Param("type")String type);
	
	@Select("SELECT DISTINCT type  from  t_design")
	public List<String> getalltype();

	@Select("select four_param_x fourParamX,four_param_y fourParamY,four_param_angle fourParamAngle,four_param_factor fourParamFactor from t_analysis_config limit 0,1")
	public TAnalysisConfig getconfig();
	
	
}
