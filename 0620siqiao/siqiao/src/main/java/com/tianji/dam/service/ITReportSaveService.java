package com.tianji.dam.service;

import com.tianji.dam.domain.AreaReportInfo;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.TReportSave;

import java.util.List;

/**
 * 报表保存Service接口
 *
 * @author liyan
 * @date 2022-10-14
 */
public interface ITReportSaveService {
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
     * 批量删除报表保存
     *
     * @param ids 需要删除的报表保存主键集合
     * @return 结果
     */
    public int deleteTReportSaveByIds(Long[] ids);

    /**
     * 删除报表保存信息
     *
     * @param id 报表保存主键
     * @return 结果
     */
    public int deleteTReportSaveById(Long id, int type);

    /**
     * //根据父id查询child
     *
     * @param id
     * @return
     */
    public List<DamsConstruction> selectChildById(Long id);

    public TReportSave seletbyDamById(Long id, long types);


    public List<TReportSave> selectTReportSaveListByAreaReportInfo(AreaReportInfo areaReportInfo);
}
