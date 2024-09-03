package com.tianji.dam.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.tianji.dam.domain.Sysconfig;
import com.tianji.dam.domain.SysconfigExample;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;

@Mapper
@Component
@DataSource(value = DataSourceType.SLAVE)
public interface SysconfigMapper {
    long countByExample(SysconfigExample example);

    int deleteByExample(SysconfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Sysconfig record);

    int insertSelective(Sysconfig record);

    List<Sysconfig> selectByExample(SysconfigExample example);

    Sysconfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Sysconfig record, @Param("example") SysconfigExample example);

    int updateByExample(@Param("record") Sysconfig record, @Param("example") SysconfigExample example);

    int updateByPrimaryKeySelective(Sysconfig record);

    int updateByPrimaryKey(Sysconfig record);

    Sysconfig selectBySyskey(String key);
}