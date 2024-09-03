package com.tianji.dam.mapper;

import com.tianji.dam.domain.CangAllitems;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * shujuMapper接口
 * 
 * @author ruoyi
 * @date 2023-12-05
 */
public interface CangAllitemsMapper 
{
    /**
     * 查询shuju
     * 
     * @param id shuju主键
     * @return shuju
     */
    public CangAllitems selectCangAllitemsById(Long id);

    /**
     * 查询shuju列表
     * 
     * @param cangAllitems shuju
     * @return shuju集合
     */
    public List<CangAllitems> selectCangAllitemsList(CangAllitems cangAllitems);

    /**
     * 新增shuju
     * 
     * @param cangAllitems shuju
     * @return 结果
     */
    public int insertCangAllitems(CangAllitems cangAllitems);

    /**
     * 修改shuju
     * 
     * @param cangAllitems shuju
     * @return 结果
     */
    public int updateCangAllitems(CangAllitems cangAllitems);

    /**
     * 删除shuju
     * 
     * @param id shuju主键
     * @return 结果
     */
    public int deleteCangAllitemsById(Long id);

    /**
     * 批量删除shuju
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCangAllitemsByIds(Long[] ids);
     void  inserbatch(@Param("tablename") String tablename, @Param("datalist") List<CangAllitems> allitemsList);

    @Select(" select * from ${param1} where px=#{param2} and py =#{param3} order by times asc")
     List<CangAllitems> getcanglasttems(String tablename,int px,int py);

     @Select("SELECT COUNT(*) FROM information_schema.tables WHERE table_name = #{param1}")
     int checktable(String tablename);

     @Update("CREATE TABLE ${param1}  (" +
             "  `id` bigint(0) NOT NULL AUTO_INCREMENT," +
             "  `px` int(0) NULL DEFAULT NULL COMMENT '平面坐标x'," +
             "  `py` int(0) NULL DEFAULT NULL COMMENT '平面坐标y'," +
             "  `pz` int(0) NULL DEFAULT NULL COMMENT '高程坐标z'," +
             "  `carid` int(0) NULL DEFAULT NULL COMMENT '车辆id'," +
             "  `speed` int(0) NULL DEFAULT NULL COMMENT '速度（扩大100倍至整数）'," +
             "  `vcv` int(0) NULL DEFAULT NULL COMMENT 'vcv（扩大100倍至整数）'," +
             "  `times` bigint(0) NULL DEFAULT NULL COMMENT '时间戳'," +
             "  PRIMARY KEY (`id`) USING BTREE," +
             "  UNIQUE KEY `idx_unique_px_py` (`px`, `py`) USING BTREE" +  // 添加联合唯一索引
             ") ENGINE = MyISAM AUTO_INCREMENT = 400629 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;")
     int createtable(String tablename);

    @Update("CREATE TABLE ${param1}  (" +
            "  `id` bigint(0) NOT NULL AUTO_INCREMENT," +
            "  `px` int(0) NULL DEFAULT NULL COMMENT '平面坐标x'," +
            "  `py` int(0) NULL DEFAULT NULL COMMENT '平面坐标y'," +
            "  `pz` int(0) NULL DEFAULT NULL COMMENT '高程坐标z'," +
            "  `carid` int(0) NULL DEFAULT NULL COMMENT '车辆id'," +
            "  `speed` int(0) NULL DEFAULT NULL COMMENT '速度（扩大100倍至整数）'," +
            "  `vcv` int(0) NULL DEFAULT NULL COMMENT 'vcv（扩大100倍至整数）'," +
            "  `times` bigint(0) NULL DEFAULT NULL COMMENT '时间戳'," +
            "  PRIMARY KEY (`id`) USING BTREE," +
            "  UNIQUE KEY `idx_unique_px_py` (`px`, `py`) USING BTREE" +  // 添加联合唯一索引
            ") ENGINE = MyISAM AUTO_INCREMENT = 400629 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;")
    int createtableceng(String tablename);

     @Update("TRUNCATE TABLE ${param1}")
    void truncatetable(String tablename);

     @Update("CREATE INDEX idx_pxy ON ${param1} (px,py)")
    void createindexs(String tablename);
     @Select("select count(id) from ${param1} where px=#{param2} and py =#{param3}")
     int getcount(String tablename, Integer x,Integer y );
     @Delete("delete from ${param1} where px=#{param2} and py =#{param3}")
     void deletecengpxy(String tablename,Integer x,Integer y);

     @Select("SELECT \n" +
             "    layer56.px, \n" +
             "    layer56.py, \n" +
             "    layer56.pz - layer55.pz AS pz\n" +
             "FROM \n" +
             "    cang_ceng_${param1}_${param2} AS layer55\n" +
             "JOIN \n" +
             "    cang_ceng_${param1}_${param3} AS layer56 ON layer55.px = layer56.px AND layer55.py = layer56.py")
     List<CangAllitems> selecengpxyzbyceng(Integer pid, Integer beginceng,Integer endceng);


    @Select("SELECT \n" +
            "    layer56.px, \n" +
            "    layer56.py, \n" +
            "    layer56.pz - layer55.pz AS pz\n" +
            "FROM \n" +
            "    cang_ceng_${param1}_${param2} AS layer55\n" +
            "JOIN \n" +
            "    cang_allitems_${param3} AS layer56 ON layer55.px = layer56.px AND layer55.py = layer56.py")
    List<CangAllitems> selecengpxyzbycangceng(Integer pid, Integer beginceng,String cangname);


     @Select("SELECT\n" +
             " AVG( PZ ) pz \n" +
             "FROM\n" +
             " (\n" +
             " SELECT\n" +
             " layer56.pz - layer55.pz AS pz \n" +
             " FROM\n" +
             " ${param2} AS layer55\n" +
             " JOIN ${param1} AS layer56 ON layer55.px = layer56.px \n" +
             " AND layer55.py = layer56.py" +
             " where layer56.pz - layer55.pz>0 \n" +
             " ) T")
     CangAllitems  selectmaxminavg(String  cangtable,String cengtable);


    @Select(" select avg(pz) pz from ${param1} ")
    CangAllitems selectCangavgev(String tablename);

    @Select("SELECT\n" +
            " AVG( PZ ) pz \n" +
            "FROM\n" +
            " (\n" +
            " SELECT\n" +
            "  layer55.pz AS pz \n" +
            " FROM\n" +
            " ${param1} AS layer55\n" +
            " JOIN ${param2} AS layer56 ON layer55.px = layer56.px \n" +
            " AND layer55.py = layer56.py" +
            " where layer56.pz - layer55.pz>0 \n" +
            " ) T")
    CangAllitems selectCangavgev_before(String beforetablename,String currenttablename);
}
