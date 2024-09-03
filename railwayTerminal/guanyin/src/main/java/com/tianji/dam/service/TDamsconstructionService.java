package com.tianji.dam.service;


import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.vo.DamsConstructionVo;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 分部工程信息 服务层
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public interface TDamsconstructionService {

    /**
     * 修改分部工程范围
     *
     * @return
     */
    public int updateRanges(Long id, String ranges);

    public List getAll(String pid);

    //遍历法获得目录树 二级标题
    public List<DamsConstruction> getAtreeMenuByFor2();

    //遍历法获得目录树 三级标题 所有的工作仓 包括未开仓 正在施工 已闭仓
    public List<DamsConstruction> getAtreeMenuByFor3();

    //遍历法获得目录树 三级标题 只获得正在施工的工作仓
    public List<DamsConstruction> getAtreeMenuByFor3Working();

    //遍历法获得目录树 三级标题 只获得已闭仓的工作仓
    public List<DamsConstruction> getAtreeMenuByFor3Closed();

    int add(DamsConstruction damsConstruction);

    DamsConstruction findById(Integer id);

    int updateDC(DamsConstruction damsConstruction);

    int deleteDC(Integer id);

    List<DamsConstruction> findAtreeMenuThirdHasData();

    DamsConstruction selectByPid(String pid);

    DamsConstruction findByTablename(String str);

    List<DamsConstruction> findRange();

    List<DamsConstruction> findByStatus(int i);

    List<DamsConstruction> findCengByMaterialname(String materialname);

    List<DamsConstruction> findAtreeMenuThirdHasDataByMaterialnameAndHeightIndex(String materialname, Integer heightIndex);

    List<String> findCangToday(String t_1, long yest, long tod);

    DamsConstruction selectByTablename(String toString);

    Set findWorkingCengOfTwoDay(String tod, String yesterday, String daybeforeyesterday);

    /*
     * 系统“结果分析”页面中的查询所有已闭仓树菜单接口
     * */
    List findAtreeMenuClosed();

    List findAtreeMenuClosedTod();

    List findAtreeMenuOpened();

    List findAtreeMenu();

    List<DamsConstruction> getAllByPage(String id, int begin, int limit, int status);

    List findAtreeMenuOpenedForAndriod();

    /**
     * 描述 获取历史记录左侧材料树
     * 新增人 lct
     * 新增日期 2021-07-27
     *
     * @return
     */
    List historyMaterialTree();

    List historyMaterialTreeRepair();

    /**
     * 描述 获取获取最大区域的一条数据
     * 新增人 lct
     * 新增日期 2021-07-27
     *
     * @return
     */
    DamsConstruction getDamsMax();

    int addSelective(DamsConstruction damsConstruction);


    public List getAllVo(DamsConstructionVo damsConstructionVo);

    List<DamsConstruction> getAllByPageVo(DamsConstructionVo damsConstructionVo);

    public List<DamsConstruction> selectChildById(Long id, int start, int end);

    public String selectStore(Long aLong);

    public int selectCountNumber(Long id);

    List historyMaterialTreeLevel();
}
