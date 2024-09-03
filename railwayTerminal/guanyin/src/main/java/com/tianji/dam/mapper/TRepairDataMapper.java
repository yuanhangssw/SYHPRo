package com.tianji.dam.mapper;


import com.tianji.dam.domain.TRepairData;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
@DataSource(value = DataSourceType.SLAVE)
public interface TRepairDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TRepairData record);

    int insertSelective(TRepairData record);

    TRepairData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TRepairData record);

    int updateByPrimaryKey(TRepairData record);

    List<TRepairData> selectTRepairDatasList(TRepairData record);

    List<TRepairData> selectTRepairDatas(TRepairData record);


    void deleteByType(TRepairData vo);
}