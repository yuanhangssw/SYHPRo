package com.tianji.dam.mapper;


import com.tianji.dam.domain.Dam;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.RollingData;
import com.tianji.dam.domain.Row;
import com.tianji.dam.domain.vo.DamsConstructionVo;
import com.tianji.dam.domain.vo.DamsJtsTreeVo;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 分部工程信息接口
 */
@Mapper
@Component
@DataSource(value = DataSourceType.SLAVE)
public interface TDamsconstructionMapper {


    int deleteByPrimaryKey(Integer id);

    int insert(DamsConstruction record);

    int insertSelective(DamsConstruction record);

    DamsConstruction selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DamsConstruction record);

    int updateByPrimaryKey(DamsConstruction record);

    List<DamsConstruction> getAll(@Param(value = "s") String s);

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
    List<DamsConstruction> findCengByMaterialname(@Param("materialname") String materialname);

    List<DamsConstruction> findAtreeMenuThirdHasDataByMaterialnameAndHeightIndex(@Param("materialname") String materialname, @Param("heightIndex") Integer heightIndex);

    @Select("select DISTINCT rangeStr from ${tableName} where Timestamp  between ${yest} and ${tod}  ")
    List<String> findCangToday(@Param("tableName") String tableName, @Param("yest") long yest, @Param("tod") long tod);

    @Select("select DISTINCT rangeStr from ${t_1} where Timestamp  between ${yest} and ${tod} union elect DISTINCT rangeStr from ${t_2} where Timestamp  between ${yest} and ${tod} union elect DISTINCT rangeStr from ${t_3} where Timestamp  between ${yest} and ${tod}  ")
    List<String> findCangTodayUnion(@Param("t_1") String t_1, @Param("t_2") String t_2, @Param("materialname") String t_3, @Param("materialname") long yest, @Param("materialname") long tod);

    @Select("(SELECT DISTINCT LayerID,materialname from ${tablename1}) UNION (SELECT DISTINCT LayerID,materialname from ${tablename2}) UNION (SELECT DISTINCT LayerID,materialname from ${tablename3}) ORDER BY LayerID ")
    List<RollingData> findWorkingCeng(@Param("tablename1") String tablename1, @Param("tablename2") String tablename2, @Param("tablename3") String tablename3);

    @Select("SELECT * from t_damsconstruction WHERE heightIndex=${layerId}  AND materialname=${mat}  AND hasdata=1 ")
    List<DamsConstruction> getAllHasDataByLayerMat(@Param("layerId") int layerId, @Param("mat") int mat);

    @Select("select DISTINCT materialname,heightIndex from t_damsconstruction WHERE hasdata=1 AND heightIndex is not null ORDER BY heightIndex")
    List<DamsConstruction> getAllCeng();


    List<DamsConstruction> getAllClosedByMaterial(String s);

    List<DamsConstruction> getAllOpenedByMaterial(String s);

    List<DamsConstruction> getAllByMaterial(String s);

    DamsConstruction findMaterialByMaterialId(int id);

    List<DamsConstruction> getAllByPage(String id, int begin, int limit, Integer status);

    List findAtreeMenuOpenedForAndriod();

    List<DamsConstruction> findAllRange();

    List<DamsConstruction> getAllByTod();

    /**
     * 描述 根据tablename筛选下级信息
     * 新增人 lct
     *
     * @param tablename
     * @return
     */
    List<DamsConstruction> getDamsByTableName(@Param("tablename") String tablename);

    /**
     * 描述 获取获取最大区域的一条数据
     * 新增人 lct
     *
     * @return
     */
    DamsConstruction getDamsMax();


    /**
     * 修改范围
     *
     * @param map
     * @return
     */
    Integer updateRanges(Map<String, Object> map);

    /**
     * 查询所有的单元工程
     *
     * @return
     */
    List<DamsJtsTreeVo> selectDams();

    @Select("select * from t_damsconstruction where status =8 and  actualstarttime<#{time} and  actualendtime > #{time}")
    List<DamsConstruction> getallopenbytimes(@Param("time") String time);

    @Select("SELECT MAX(`Timestamp`) as maxTime,MIN(`Timestamp`) as minTime FROM ${tablename}")
    Map<String, Long> getTimestampByTablename(@Param("tablename") String tablename);

    @Select("select * from t_damsconstruction where pid = #{pid}")
    List<DamsConstruction> getdambypid(@Param("pid") Integer pid);

    @Select("delete from t_repair_data where damsid=#{damid} and repairtype =#{type}")
    void deleterepairbydamadntype(@Param("damid") int damid, @Param("type") int type);


    List<Row> selectTDamsconstructionByStatus4();

    List<Dam> selectByid(int id);


    public List getAllVo(DamsConstructionVo damsConstructionVo);

    List<DamsConstruction> getAllByPageVo(DamsConstructionVo damsConstructionVo);


    @Select("select * from t_damsconstruction  where pid=#{id} limit #{start},#{end}")
    public List<DamsConstruction> selectChildById(@Param("id") Long id, @Param("start") int start, @Param("end") int end);

    @Select("select title from  t_damsconstruction where id=#{id}")
    public String selectStore(@Param("id") Long id);

    @Select(" select count(*) from t_damsconstruction  where pid=#{id} ")
    public int selectCountNumber(Long id);

    @Select("SELECT AVG(Elevation) FROM ${param1}")
    Double getdamavgevolution(String tablename);

    @Select("SELECT DISTINCT engcode FROM `t_damsconstruction` where pid =#{pid} ORDER BY engcode+''")
    List<Integer> selectarealevel(Integer id);


    @Select("select id,damsid,pid,gaocheng,cenggao,ranges,tablename tablename from t_damsconstruction where pid =#{param1} and engcode =#{param2}")
    List<DamsConstruction> getalltabbypidandencode(Integer pid, String encode);

    @Select("SELECT MAX(CoordX) maxx,MAX(CoordY) maxy,MIN(CoordX) minx,MIN(CoordY) miny  FROM ${tablename}")
    Map<String, Double> getdammaxandmincoordinate(@Param("tablename") String tablename);


    @Select("select title from t_damsconstruction where id=#{cangid}")
    String selectDamTitleById(@Param("cangid") Integer cangid);

    @Select("SELECT title  FROM `t_damsconstruction` where id in (SELECT pid  FROM `t_damsconstruction` where id=#{cangid}\n" +
            ")")
    String selectDameNameByPid(@Param("cangid") Integer cangid);

    @Select("SELECT gaocheng  FROM `t_damsconstruction` where id=#{cangid}")
    String selectdesignGaochengById(Integer cangid);


    @Select("SELECT * FROM `t_damsconstruction`  where id =#{damid}")
    DamsConstruction getDamconstructionById(@Param("damid") Integer damid);
}
