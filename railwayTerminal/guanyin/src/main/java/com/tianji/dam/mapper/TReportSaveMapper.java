package com.tianji.dam.mapper;

import com.tianji.dam.domain.AreaReportInfo;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.TReportSave;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 报表保存Mapper接口
 *
 * @author liyan
 * @date 2022-10-14
 */
public interface TReportSaveMapper {
    /**
     * 查询报表保存
     *
     * @param id 报表保存主键
     * @return 报表保存
     */
    public TReportSave selectTReportSaveById(Long id);

    public TReportSave seletbyDamAndType(Long damgid, Long type);

    /**
     * 查询报表保存列表
     *
     * @param tReportSave 报表保存
     * @return 报表保存集合
     */
    public List<TReportSave> selectTReportSaveList(TReportSave tReportSave);

    /**
     * 新增报表保存
     *
     * @param tReportSave 报表保存
     * @return 结果
     */
    public int insertTReportSave(TReportSave tReportSave);

    /**
     * 修改报表保存
     *
     * @param tReportSave 报表保存
     * @return 结果
     */
    public int updateTReportSave(TReportSave tReportSave);

    /**
     * 删除报表保存
     *
     * @param id 报表保存主键
     * @return 结果
     */
    public int deleteTReportSaveById(Long id, int type);

    /**
     * 批量删除报表保存
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTReportSaveByIds(Long[] ids);

    @Select("select * from t_damsconstruction  where pid=#{id}")
    public List<DamsConstruction> selectChildById(@Param("id") Long id);

    /*
        @Select("select * from  t_report_save where damgid=#{id} and types=#{types}")
    */
    @Select(" select t.damgid, d.`status` ,d.title,m.materialname,t.id,t.types,t.unitname,t.areaname,t.partname,t.sgtime,t.zhuanghao,t.gaocheng,t.bianshu,t.mianji," +
            "                               t.base64, t.param1, t.param2,t.param3, t.param4, t.param5, t.param6, t.param7, t.param8, t.param9, t.param10, t.create_time, t.create_by, t.status  " +
            "                              from  t_damsconstruction d " +
            "                              LEFT JOIN t_report_save t on  t.damgid=d.id " +
            "                             LEFT JOIN t_material  m on t.types=m.id " +
            "                                where " +
            "                                 d.id=#{id} and t.types=#{types} "
    )
    public TReportSave seletbyDamById(@Param("id") Long id, @Param("types") long types);


    public List<TReportSave> seletbyDamList(AreaReportInfo areaReportInfo);

    public List<TReportSave> selectTReportSaveListByAreaReportInfo(AreaReportInfo areaReportInfo);
}
