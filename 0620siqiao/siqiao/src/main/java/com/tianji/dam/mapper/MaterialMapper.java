package com.tianji.dam.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.tianji.dam.domain.Material;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
@Mapper
@Component
@DataSource(value = DataSourceType.SLAVE)
public interface MaterialMapper {
    List<Material> findAllMaterial(Material param);
    List<Material> findAllMaterial();
    int deleteByPrimaryKey(Integer id);
    int insert(Material record);
    int insertSelective(Material record);
    Material selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(Material record);
    int updateByPrimaryKey(Material record);
    int deleteByMaterialname(String materialname);
    Material selectByMaterialname(String materialname);

    Material selectByDesc();
}