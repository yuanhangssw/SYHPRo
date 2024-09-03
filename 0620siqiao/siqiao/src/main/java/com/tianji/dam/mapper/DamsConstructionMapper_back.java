package com.tianji.dam.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.RollingData;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;

@Component
@Mapper
@DataSource(value = DataSourceType.SLAVE)
public interface DamsConstructionMapper_back {
    int deleteByPrimaryKey(Integer id);
    int insert(DamsConstruction record);
    int insertSelective(DamsConstruction record);
    DamsConstruction selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(DamsConstruction record);
    int updateByPrimaryKey(DamsConstruction record);
    List<DamsConstruction> getAll(String s);
    List<DamsConstruction> getAllWorking(String s);
    List<DamsConstruction> getAllClosed(String s);
    List<DamsConstruction> findAll();
    int add(DamsConstruction damsConstruction);
    List<DamsConstruction> getAllHasData();
    List<DamsConstruction> findAllWorkingPlane();
    DamsConstruction findByTablename(String str);
    List<DamsConstruction> findRange();
    DamsConstruction selectByTablename(String id);
    List<DamsConstruction> findByStatus(int i);
    @Select("select * from (select * from `t_damsconstruction` where hasdata=1 and materialname=${materialname} ORDER BY `heightIndex` ) as aaa GROUP BY aaa.heightIndex ")
    List<DamsConstruction> findCengByMaterialname(@Param("materialname")String materialname);
    List<DamsConstruction> findAtreeMenuThirdHasDataByMaterialnameAndHeightIndex(@Param("materialname")String materialname, @Param("heightIndex")Integer heightIndex);
    @Select("select DISTINCT rangeStr from ${tableName} where Timestamp  between ${yest} and ${tod}  ")
    List<String> findCangToday(@Param("tableName")String tableName, @Param("yest")long yest, @Param("tod")long tod);
    @Select("select DISTINCT rangeStr from ${t_1} where Timestamp  between ${yest} and ${tod} union elect DISTINCT rangeStr from ${t_2} where Timestamp  between ${yest} and ${tod} union elect DISTINCT rangeStr from ${t_3} where Timestamp  between ${yest} and ${tod}  ")
    List<String> findCangTodayUnion(@Param("t_1")String t_1, @Param("t_2")String t_2, @Param("materialname")String t_3, @Param("materialname")long yest, @Param("materialname")long tod);
    @Select("(SELECT DISTINCT LayerID,materialname from ${tablename1}) UNION (SELECT DISTINCT LayerID,materialname from ${tablename2}) UNION (SELECT DISTINCT LayerID,materialname from ${tablename3}) ORDER BY LayerID ")
    List<RollingData> findWorkingCeng(@Param("tablename1")String tablename1, @Param("tablename2")String tablename2, @Param("tablename3") String tablename3);
    @Select("SELECT * from t_damsconstruction WHERE heightIndex=${layerId}  AND materialname=${mat}  AND hasdata=1 " )
    List<DamsConstruction> getAllHasDataByLayerMat(@Param("layerId")int layerId, @Param("mat")int mat);
    @Select("select DISTINCT materialname,heightIndex from t_damsconstruction WHERE hasdata=1 AND heightIndex is not null ORDER BY heightIndex" )
    List<DamsConstruction> getAllCeng();
    List<DamsConstruction> getAllClosedByMaterial(String s);
    List<DamsConstruction> getAllOpenedByMaterial(String s);
    List<DamsConstruction> getAllByMaterial(String s);
    DamsConstruction findMaterialByMaterialId(int id);
    List<DamsConstruction> getAllByPage(String id, int begin, int limit);
    List findAtreeMenuOpenedForAndriod();
    List<DamsConstruction> findAllRange();
    List<DamsConstruction> getAllByTod();
}