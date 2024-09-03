package com.tianji.dam.service.impl;

import com.tianji.dam.domain.AreaReportInfo;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.TReportSave;
import com.tianji.dam.mapper.TReportSaveMapper;
import com.tianji.dam.service.ITReportSaveService;
import com.tj.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 报表保存Service业务层处理
 *
 * @author liyan
 * @date 2022-10-14
 */
@Service
public class TReportSaveServiceImpl implements ITReportSaveService {
    @Autowired
    private TReportSaveMapper tReportSaveMapper;

    /**
     * 查询报表保存
     *
     * @param id 报表保存主键
     * @return 报表保存
     */
    @Override
    public TReportSave selectTReportSaveById(Long id) {
        return tReportSaveMapper.selectTReportSaveById(id);
    }

    @Override
    public TReportSave seletbyDamAndType(Long damgid, Long type) {
        return tReportSaveMapper.seletbyDamAndType(damgid, type);
    }

    /**
     * 查询报表保存列表
     *
     * @param tReportSave 报表保存
     * @return 报表保存
     */
    @Override
    public List<TReportSave> selectTReportSaveList(TReportSave tReportSave) {
        return tReportSaveMapper.selectTReportSaveList(tReportSave);
    }

    /**
     * 新增报表保存
     *
     * @param tReportSave 报表保存
     * @return 结果
     */
    @Override
    public int insertTReportSave(TReportSave tReportSave) {
        tReportSave.setCreateTime(DateUtils.getNowDate());

        tReportSaveMapper.deleteTReportSaveById(tReportSave.getDamgid(), tReportSave.getTypes().intValue());
        return tReportSaveMapper.insertTReportSave(tReportSave);
    }

    /**
     * 修改报表保存
     *
     * @param tReportSave 报表保存
     * @return 结果
     */
    @Override
    public int updateTReportSave(TReportSave tReportSave) {
        return tReportSaveMapper.updateTReportSave(tReportSave);
    }

    /**
     * 批量删除报表保存
     *
     * @param ids 需要删除的报表保存主键
     * @return 结果
     */
    @Override
    public int deleteTReportSaveByIds(Long[] ids) {
        return tReportSaveMapper.deleteTReportSaveByIds(ids);
    }

    /**
     * 删除报表保存信息
     *
     * @param id 报表保存主键
     * @return 结果
     */
    @Override
    public int deleteTReportSaveById(Long id, int type) {
        return tReportSaveMapper.deleteTReportSaveById(id, type);
    }

    @Override
    public List<DamsConstruction> selectChildById(Long id) {
        return tReportSaveMapper.selectChildById(id);
    }

    @Override
    public TReportSave seletbyDamById(Long id, long types) {
        return tReportSaveMapper.seletbyDamById(id, types);
    }

    @Override
    public List<TReportSave> selectTReportSaveListByAreaReportInfo(AreaReportInfo areaReportInfo) {
        return tReportSaveMapper.selectTReportSaveListByAreaReportInfo(areaReportInfo);
    }
}
