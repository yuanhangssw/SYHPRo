package com.ruoyi.system.service.impl;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.BReservoirPrevention;
import com.ruoyi.system.domain.vo.Fujian5;
import com.ruoyi.system.domain.vo.Fujian6;
import com.ruoyi.system.domain.vo.MainbReservoir;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BProjectMapper;
import com.ruoyi.system.domain.BProject;
import com.ruoyi.system.service.IBProjectService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;


/**
 * 项目信息Service业务层处理
 *
 * @author ruoyi
 * @date 2024-03-05
 */
@Service
public class BProjectServiceImpl implements IBProjectService {
    @Autowired
    private BProjectMapper bProjectMapper;

    /**
     * 查询项目信息
     *
     * @param id 项目信息主键
     * @return 项目信息
     */
    @Override
    public BProject selectBProjectById(Long id) {
        return bProjectMapper.selectBProjectById(id);
    }

    /**
     * 查询项目信息列表
     *
     * @param bProject 项目信息
     * @return 项目信息
     */
    @Override
    public List<BProject> selectBProjectList(BProject bProject) {
        return bProjectMapper.selectBProjectList(bProject);
    }

    /**
     * 新增项目信息
     *
     * @param bProject 项目信息
     * @return 结果
     */
    @Override
    public int insertBProject(BProject bProject) {
        bProject.setCreateTime(DateUtils.getNowDate());
        return bProjectMapper.insertBProject(bProject);
    }

    /**
     * 修改项目信息
     *
     * @param bProject 项目信息
     * @return 结果
     */
    @Override
    public int updateBProject(BProject bProject) {
        bProject.setUpdateTime(DateUtils.getNowDate());
        return bProjectMapper.updateBProject(bProject);
    }

    /**
     * 批量删除项目信息
     *
     * @param ids 需要删除的项目信息主键
     * @return 结果
     */
    @Override
    public int deleteBProjectByIds(Long[] ids) {
        return bProjectMapper.deleteBProjectByIds(ids);
    }

    /**
     * 删除项目信息信息
     *
     * @param id 项目信息主键
     * @return 结果
     */
    @Override
    public int deleteBProjectById(Long id) {
        return bProjectMapper.deleteBProjectById(id);
    }

    @Override
    public List<BProject> getBProjectByDept(List<Long> deptId) {
        return bProjectMapper.getBProjectByDept(deptId);
    }

    @Override
    public List<Map<String, Object>> selectBProjectListJoinPrevent(Long id) {
        return bProjectMapper.selectBProjectListJoinPrevent(id);
    }


    //给Excel设置值
    // 生成 Excel 并返回 ByteArrayInputStream
    public ByteArrayInputStream setExcelValue(Map<String, Object> map) {
        String inputFilePath = "C:\\Users\\pingz.DESKTOP-4LNNVLR\\Desktop\\白蚁报表模板\\附件2水库大坝白蚁等害堤动物危害及防治情况普查登记表.xlsx"; // 模板文件路径

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheetAt(0);

            // 插入5行到第18行上方，样式和第17行一致
            // 插入5行到第30行上方，样式和第29行一致

            // 第一部分  表头值
            setCellValueIfNotEmpty(sheet, 1, 1, map.get("reservoirName") + "大坝白蚁等害堤动物危害及防治情况普查登记表");
            setCellValueIfNotEmpty(sheet, 3, 2, map.get("reservoirName"));
            setCellValueIfNotEmpty(sheet, 3, 8, map.get("reservoirManger"));
            setCellValueIfNotEmpty(sheet, 4, 2, map.get("reservoirAddress"));

            //库容
            setCellValueIfNotEmpty(sheet, 5, 2, map.get("storageCapacity"));
            setCellValueIfNotEmpty(sheet, 5, 9, map.get("reservoirgrade"));
            setCellValueIfNotEmpty(sheet, 5, 11, map.get("censusMethod"));

            //主坝情况
            setCellValueIfNotEmpty(sheet, 5, 11, map.get("censusMethod"));
            List<MainbReservoir> mainbReservoirs = (List<MainbReservoir>) map.get("mainbReservoirs");

            if (!mainbReservoirs.isEmpty() && mainbReservoirs != null) {
                //遍历主坝
                for (MainbReservoir mainbReservoir : mainbReservoirs) {
                    setCellValueIfNotEmpty(sheet, 6, 4, mainbReservoir.getDamType());
                    setCellValueIfNotEmpty(sheet, 6, 7, mainbReservoir.getDamLength());
                    setCellValueIfNotEmpty(sheet, 6, 9, mainbReservoir.getDamHigth());
                    setCellValueIfNotEmpty(sheet, 6, 11, mainbReservoir.getSlopeProtection());
                    break;
                }
            }

            //遍历从坝
            List<MainbReservoir> minorReservoirs = (List<MainbReservoir>) map.get("minorReservoirs");
            int Offset = 0; //偏移
            if (!minorReservoirs.isEmpty() && minorReservoirs != null) {
                //从坝数量大于3，创建行。
                if (minorReservoirs.size() > 3) {
                    Offset = minorReservoirs.size() - 3;
                    insertRowsOfBaiYi(sheet, 9, Offset);
                }
                int rowIndex = 7;
                for (MainbReservoir minorReservoir : minorReservoirs) {
                    //副坝1
                    setCellValueIfNotEmpty(sheet, rowIndex, 4, minorReservoir.getDamType());
                    setCellValueIfNotEmpty(sheet, rowIndex, 7, minorReservoir.getDamLength());
                    setCellValueIfNotEmpty(sheet, rowIndex, 9, minorReservoir.getDamHigth());
                    setCellValueIfNotEmpty(sheet, rowIndex, 11, minorReservoir.getSlopeProtection());
                    rowIndex++;
                }
            }


            //白蚁危害及防治情况  主坝
            List<BReservoirPrevention> mainReservoirPreventionList = (List<BReservoirPrevention>) map.get("mainReservoirPreventionList");
            if (!mainReservoirPreventionList.isEmpty() && mainReservoirPreventionList != null) {
                BReservoirPrevention bReservoirPrevention = mainReservoirPreventionList.get(0);
                if (bReservoirPrevention != null) {
                    setCellValueIfNotEmpty(sheet, 14 + Offset, 2, bReservoirPrevention.getDetrimentLevel());
                    //危害处数暂时没有 sum
                    Long sum = bReservoirPrevention.getLeaksNumber() + bReservoirPrevention.getThroughNumber() + bReservoirPrevention.getDropSocketNumber();
                    setCellValueIfNotEmpty(sheet, 14 + Offset, 3, sum);
                    setCellValueIfNotEmpty(sheet, 14 + Offset, 4, bReservoirPrevention.getLeaksNumber());
                    setCellValueIfNotEmpty(sheet, 14 + Offset, 6, bReservoirPrevention.getThroughNumber());
                    setCellValueIfNotEmpty(sheet, 14 + Offset, 7, bReservoirPrevention.getDropSocketNumber());
                    setCellValueIfNotEmpty(sheet, 14 + Offset, 8, bReservoirPrevention.getNestDigging());
                    setCellValueIfNotEmpty(sheet, 14 + Offset, 9, bReservoirPrevention.getChargeArea());
                    setCellValueIfNotEmpty(sheet, 14 + Offset, 10, bReservoirPrevention.getGroutingQuantity());
                    setCellValueIfNotEmpty(sheet, 14 + Offset, 11, bReservoirPrevention.getInvestCapital());
                }
            }


            //白蚁危害及防治情况  副坝
            List<BReservoirPrevention> minorReservoirPreventionList = (List<BReservoirPrevention>) map.get("minorReservoirPreventionList");
            int offset2 = 0;
            if (!minorReservoirPreventionList.isEmpty() && minorReservoirPreventionList != null) {
                if (minorReservoirPreventionList.size() > 3) {
                    offset2 = minorReservoirPreventionList.size() - 3;
                    insertRowsOfBaiYi(sheet, 17 + Offset, offset2);
                }
                int i = 0;
                for (BReservoirPrevention minorBReservoirPrevention : minorReservoirPreventionList) {
                    setCellValueIfNotEmpty(sheet, 15 + Offset + i, 2, minorBReservoirPrevention.getDetrimentLevel());
                    //危害处数暂时没有 sum
                    Long sum2 = minorBReservoirPrevention.getLeaksNumber() + minorBReservoirPrevention.getThroughNumber() + minorBReservoirPrevention.getDropSocketNumber();
                    setCellValueIfNotEmpty(sheet, 15 + Offset + i, 3, sum2);
                    setCellValueIfNotEmpty(sheet, 15 + Offset + i, 4, minorBReservoirPrevention.getLeaksNumber());
                    setCellValueIfNotEmpty(sheet, 15 + Offset + i, 6, minorBReservoirPrevention.getThroughNumber());
                    setCellValueIfNotEmpty(sheet, 15 + Offset + i, 6, minorBReservoirPrevention.getDropSocketNumber());
                    setCellValueIfNotEmpty(sheet, 15 + Offset + i, 7, minorBReservoirPrevention.getDropSocketNumber());
                    setCellValueIfNotEmpty(sheet, 15 + Offset + i, 8, minorBReservoirPrevention.getNestDigging());
                    setCellValueIfNotEmpty(sheet, 15 + Offset + i, 9, minorBReservoirPrevention.getChargeArea());
                    setCellValueIfNotEmpty(sheet, 15 + Offset + i, 10, minorBReservoirPrevention.getGroutingQuantity());
                    setCellValueIfNotEmpty(sheet, 15 + Offset + i, 11, minorBReservoirPrevention.getInvestCapital());
                    i++;
                }
            }


            //獾、狐、鼠等其他动物危害及防治情况  主坝
            List<BReservoirPrevention> mainReservoirPreventionListOfMouse = (List<BReservoirPrevention>) map.get("mainReservoirPreventionListOfMouse");
            if (!mainReservoirPreventionListOfMouse.isEmpty() && mainReservoirPreventionListOfMouse != null) {
                BReservoirPrevention mainReservoirPreventionOfMouse = mainReservoirPreventionListOfMouse.get(0);
                setCellValueIfNotEmpty(sheet, 22 + Offset + offset2, 2, mainReservoirPreventionOfMouse.getDetrimentType());
                setCellValueIfNotEmpty(sheet, 22 + Offset + offset2, 3, mainReservoirPreventionOfMouse.getDetrimentLevel());
                //危害处数 sum3
                Long sum3 = mainReservoirPreventionOfMouse.getLeaksNumber() + mainReservoirPreventionOfMouse.getThroughNumber() + mainReservoirPreventionOfMouse.getDropSocketNumber();
                setCellValueIfNotEmpty(sheet, 22 + Offset + offset2, 4, sum3);
                setCellValueIfNotEmpty(sheet, 22 + Offset + offset2, 5, mainReservoirPreventionOfMouse.getLeaksNumber());
                setCellValueIfNotEmpty(sheet, 22 + Offset + offset2, 6, mainReservoirPreventionOfMouse.getThroughNumber());
                setCellValueIfNotEmpty(sheet, 22 + Offset + offset2, 7, mainReservoirPreventionOfMouse.getDropSocketNumber());
                setCellValueIfNotEmpty(sheet, 22 + Offset + offset2, 8, mainReservoirPreventionOfMouse.getZoonGovernNumber());
                setCellValueIfNotEmpty(sheet, 22 + Offset + offset2, 9, mainReservoirPreventionOfMouse.getZoonGovernType());
                setCellValueIfNotEmpty(sheet, 22 + Offset + offset2, 9, mainReservoirPreventionOfMouse.getInvestCapital());
            }


            //獾、狐、鼠等其他动物危害及防治情况  副坝
            List<BReservoirPrevention> minorReservoirPreventionListOfMouse = (List<BReservoirPrevention>) map.get("minorReservoirPreventionListOfMouse");
            int offset3 = 0;
            if (!minorReservoirPreventionListOfMouse.isEmpty() && minorReservoirPreventionListOfMouse != null) {
                if (minorReservoirPreventionListOfMouse.size() > 3) {
                    offset3 = minorReservoirPreventionListOfMouse.size() - 3;
                    insertRowsOfMouse(sheet, 25 + Offset + offset2, offset3);
                }

                int j = 0;
                for (BReservoirPrevention minorReservoirPrevention : minorReservoirPreventionListOfMouse) {
                    setCellValueIfNotEmpty(sheet, 23 + Offset + offset2 + j, 2, minorReservoirPrevention.getDetrimentType());
                    setCellValueIfNotEmpty(sheet, 23 + Offset + offset2 + j, 3, minorReservoirPrevention.getDetrimentLevel());
                    //危害处数sum4
                    Long sum4 = minorReservoirPrevention.getLeaksNumber() + minorReservoirPrevention.getThroughNumber() + minorReservoirPrevention.getDropSocketNumber();
                    setCellValueIfNotEmpty(sheet, 23 + Offset + offset2 + j, 4, sum4);
                    setCellValueIfNotEmpty(sheet, 23 + Offset + offset2 + j, 5, minorReservoirPrevention.getLeaksNumber());
                    setCellValueIfNotEmpty(sheet, 23 + Offset + offset2 + j, 6, minorReservoirPrevention.getThroughNumber());
                    setCellValueIfNotEmpty(sheet, 23 + Offset + offset2 + j, 7, minorReservoirPrevention.getDropSocketNumber());
                    setCellValueIfNotEmpty(sheet, 23 + Offset + offset2 + j, 8, minorReservoirPrevention.getZoonGovernNumber());
                    setCellValueIfNotEmpty(sheet, 23 + Offset + offset2 + j, 9, minorReservoirPrevention.getZoonGovernType());
                    setCellValueIfNotEmpty(sheet, 23 + Offset + offset2 + j, 11, minorReservoirPrevention.getInvestCapital());
                    j++;
                }
            }

            // 写出到 ByteArrayOutputStream
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ByteArrayInputStream setExcelValue3(Map<String, Object> map) {
        String inputFilePath = "C:\\Users\\pingz.DESKTOP-4LNNVLR\\Desktop\\白蚁报表模板\\附件3堤防工程白蚁等害堤动物危害及防治情况普查登记表.xlsx"; // 模板文件路径

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheetAt(0);

            // 插入5行到第18行上方，样式和第17行一致
            // 插入5行到第30行上方，样式和第29行一致
            if (!map.isEmpty() && map != null) {

                // 第一部分  表头值  (已做修改，单元格行列和此处对应)
                setCellValueIfNotEmpty(sheet, 1, 1, map.get("projectName") + "工程白蚁等害堤动物危害及防治情况普杳登记表");
                setCellValueIfNotEmpty(sheet, 3, 2, map.get("projectName"));
                setCellValueIfNotEmpty(sheet, 3, 7, map.get("dykeGrade"));
                setCellValueIfNotEmpty(sheet, 3, 10, map.get("dykeManger"));
                setCellValueIfNotEmpty(sheet, 4, 2, map.get("address"));
                setCellValueIfNotEmpty(sheet, 4, 10, map.get("censusMethod"));
                setCellValueIfNotEmpty(sheet, 5, 2, map.get("dykeLength"));
                setCellValueIfNotEmpty(sheet, 5, 6, map.get("dykePileStartAndEnd"));
                setCellValueIfNotEmpty(sheet, 5, 11, map.get("unitNumbers"));
                setCellValueIfNotEmpty(sheet, 6, 2, map.get("dykePatrolLength"));
                setCellValueIfNotEmpty(sheet, 6, 6, map.get("dykePatrolPileStartAndEnd"));
                setCellValueIfNotEmpty(sheet, 6, 11, map.get("unitNumbers"));

                //第二部分  白蚁危害防治情况
                setCellValueIfNotEmpty(sheet, 11, 1, map.get("totalDetrimentLengthOfBaiYi"));
                setCellValueIfNotEmpty(sheet, 11, 2, map.get("totalDetrimentLengthOneOfBaiYi"));
                setCellValueIfNotEmpty(sheet, 11, 3, map.get("totalDetrimentLengthTwoOfBaiYi"));
                setCellValueIfNotEmpty(sheet, 11, 4, map.get("totalDetrimentLengthThreeOfBaiYi"));
                setCellValueIfNotEmpty(sheet, 11, 5, map.get("detrimentOfNumbersOfBaiYi"));
                setCellValueIfNotEmpty(sheet, 11, 6, map.get("leaksNumbersOfBaiYi"));
                setCellValueIfNotEmpty(sheet, 11, 7, map.get("throughNumbersOfBaiYi"));
                setCellValueIfNotEmpty(sheet, 11, 8, map.get("dropSocketNumbersOfBaiYi"));
                setCellValueIfNotEmpty(sheet, 11, 9, map.get("nestDiggingOfBaiYi"));
                setCellValueIfNotEmpty(sheet, 11, 10, map.get("chargeAreasOfBaiYi"));
                setCellValueIfNotEmpty(sheet, 11, 11, map.get("groutingQuantitysOfBaiYi"));
                setCellValueIfNotEmpty(sheet, 11, 12, map.get("totalInvestCapital2023fBaiYi"));


                //第三部分 獾、狐、鼠等其他害堤动物危害防治情况
                setCellValueIfNotEmpty(sheet, 16, 1, map.get("totalDetrimentLengthOfMouse"));
                setCellValueIfNotEmpty(sheet, 16, 2, map.get("totalDetrimentLengthOneOfMouse"));
                setCellValueIfNotEmpty(sheet, 16, 3, map.get("totalDetrimentLengthTwoOfMouse"));
                setCellValueIfNotEmpty(sheet, 16, 4, map.get("totalDetrimentLengthThreeOfMouse"));
                setCellValueIfNotEmpty(sheet, 16, 5, map.get("zoneTypeOfMouse"));
                setCellValueIfNotEmpty(sheet, 16, 6, map.get("detrimentOfNumbersOfMouse"));
                setCellValueIfNotEmpty(sheet, 16, 7, map.get("leaksNumbersOfMouse"));
                setCellValueIfNotEmpty(sheet, 16, 8, map.get("throughNumbersOfMouse"));
                setCellValueIfNotEmpty(sheet, 16, 9, map.get("dropSocketNumbersOfMouse"));
                setCellValueIfNotEmpty(sheet, 16, 10, map.get("chargeAreasOfMouse"));
                setCellValueIfNotEmpty(sheet, 16, 12, map.get("totalInvestCapital2023OfMouse"));
                setCellValueIfNotEmpty(sheet, 17, 2, map.get("puChaUnit"));
            }

            // 写出到 ByteArrayOutputStream
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    //附件5
    @Override
    public ByteArrayInputStream setExcelValueFive(Map projecMaps) {
        String inputFilePath = "C:\\Users\\pingz.DESKTOP-4LNNVLR\\Desktop\\白蚁报表模板\\附件5水库大坝白蚁等害堤动物危害及防治情况普香明细表.xlsx"; // 模板文件路径

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheetAt(0);


            ; //**县  给县设置值
            setCellValueIfNotEmpty(sheet, 9, 2, projecMaps.get("deptName"));


            int size = projecMaps.size() - 1;
            int k = 0; //偏移量
            if (!projecMaps.isEmpty() && projecMaps != null) {

                for (int i = 1; i <= size; i++) {

                    Map<String, Object> stringObjectMap = (Map<String, Object>) projecMaps.get(i);

                    setCellValueIfNotEmpty(sheet, 11 + k, 1, k + 1);

                    setCellValueIfNotEmpty(sheet, 11 + k, 2, stringObjectMap.get("reservoirName"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 3, stringObjectMap.get("reservoirgrade"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 4, stringObjectMap.get("mainReservoirDamType"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 5, stringObjectMap.get(""));//危害处数
                    setCellValueIfNotEmpty(sheet, 11 + k, 6, stringObjectMap.get("mainReservoirBadGrade"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 7, stringObjectMap.get("minorTotal"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 8, stringObjectMap.get("oneDetrimentLength"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 9, stringObjectMap.get("twoDetrimentLength"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 10, stringObjectMap.get("threeDetrimentLength"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 11, stringObjectMap.get("totalDetrimentLength"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 12, stringObjectMap.get("leaksNumberOfBaiyi"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 13, stringObjectMap.get("throughNumberOfBaiyi"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 14, stringObjectMap.get("dropSocketNumberOfBaiyi"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 15, stringObjectMap.get("nestDiggingOfBaiyi"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 16, stringObjectMap.get("chargeAreaOfBaiyi"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 17, stringObjectMap.get("groutingQuantity"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 18, stringObjectMap.get("investCapital"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 19, stringObjectMap.get("mainReservoirDamType"));//19 坝型
                    setCellValueIfNotEmpty(sheet, 11 + k, 20, stringObjectMap.get(""));//20 危害处数
                    setCellValueIfNotEmpty(sheet, 11 + k, 21, stringObjectMap.get("mainReservoirBadGrade"));//21 危害等级
                    setCellValueIfNotEmpty(sheet, 11 + k, 22, stringObjectMap.get("minorTotal"));//22 总数
                    setCellValueIfNotEmpty(sheet, 11 + k, 23, stringObjectMap.get("oneDetrimentLengthOfMouse"));//23 一级危害
                    setCellValueIfNotEmpty(sheet, 11 + k, 24, stringObjectMap.get("twoDetrimentLengthOfMouse"));//24 二级危害
                    setCellValueIfNotEmpty(sheet, 11 + k, 25, stringObjectMap.get("threeDetrimentLengthOfMouse"));//25 三级危害
                    setCellValueIfNotEmpty(sheet, 11 + k, 26, stringObjectMap.get("totalDetrimentLengthOfMouse"));//26 危害处数总数
                    setCellValueIfNotEmpty(sheet, 11 + k, 27, stringObjectMap.get("leaksNumberOfMouse"));//27 渗透处数
                    setCellValueIfNotEmpty(sheet, 11 + k, 28, stringObjectMap.get("throughNumberOfMouse"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 29, stringObjectMap.get("dropSocketNumberOfMouse"));
                    setCellValueIfNotEmpty(sheet, 11 + k, 30, stringObjectMap.get("zoonGovernNumber"));//治理数量
                    setCellValueIfNotEmpty(sheet, 11 + k, 31, stringObjectMap.get("investCapitalOfMouse"));//投入资金
                    k++;


                }

            }

            // 写出到 ByteArrayOutputStream
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }
    //附件6
    @Override
    public ByteArrayInputStream setExcelValueSix(List<Map<String, Object>> mapList) {
        String inputFilePath = "C:\\Users\\pingz.DESKTOP-4LNNVLR\\Desktop\\白蚁报表模板\\附件6水库大坝白蚁等害堤动物危害及防治情况普查汇总表.xlsx"; // 模板文件路径
        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheetAt(0);

            String leader="";
            if(mapList!=null&&mapList.size()>0){
                 leader = String.valueOf(mapList.get(0).get("leader"));
                 setCellValueIfNotEmpty(sheet, 1, 1,leader+"水库大坝白蚁等害堤动物危害及防治情况普查汇总表" );
            }else {
                return  null;
            }

            //广西省水利厅
            setCellValueIfNotEmpty(sheet, 7, 2, leader);
            int order=1;
            int offset=0;
            for (Map<String, Object> stringObjectMap : mapList) {
                if(stringObjectMap!=null&&stringObjectMap.size()>0){
                    Fujian5 baiYi = (Fujian5)stringObjectMap.get("Baiyi");
                    if(baiYi!=null){
                        setCellValueIfNotEmpty(sheet, 8+offset, 1, order);
                        setCellValueIfNotEmpty(sheet, 8+offset, 2, baiYi.getDeptName());
                        setCellValueIfNotEmpty(sheet, 8+offset, 3, baiYi.getTotalProjects());
                        setCellValueIfNotEmpty(sheet, 8+offset, 4, baiYi.getCompletedSurveys());
                        setCellValueIfNotEmpty(sheet, 8+offset, 5, baiYi.getMainDamLevel1Hazards());
                        setCellValueIfNotEmpty(sheet, 8+offset, 6, baiYi.getMainDamLevel2Hazards());
                        setCellValueIfNotEmpty(sheet, 8+offset, 7, baiYi.getMainDamLevel3Hazards());
                        setCellValueIfNotEmpty(sheet, 8+offset, 8, baiYi.getAuxiliaryDamTotal());
                        setCellValueIfNotEmpty(sheet, 8+offset, 9, baiYi.getAuxiliaryDamLevel1Hazards());//副坝等级危害处数Ⅰ级危害数
                        setCellValueIfNotEmpty(sheet, 8+offset, 10, baiYi.getAuxiliaryDamLevel2Hazards());
                        setCellValueIfNotEmpty(sheet, 8+offset, 11, baiYi.getAuxiliaryDamLevel3Hazards());
                        setCellValueIfNotEmpty(sheet, 8+offset, 12, baiYi.getInvestment2023());
                    }
                    Fujian5 mouse = (Fujian5)stringObjectMap.get("HuanHuShu");
                    if(mouse!=null){
                        setCellValueIfNotEmpty(sheet, 8+offset, 15, mouse.getMainDamLevel1Hazards());
                        setCellValueIfNotEmpty(sheet, 8+offset, 16, mouse.getMainDamLevel2Hazards());
                        setCellValueIfNotEmpty(sheet, 8+offset, 17, mouse.getMainDamLevel3Hazards());
                        setCellValueIfNotEmpty(sheet, 8+offset, 18, mouse.getAuxiliaryDamTotal());
                        setCellValueIfNotEmpty(sheet, 8+offset, 19, mouse.getAuxiliaryDamLevel1Hazards());
                        setCellValueIfNotEmpty(sheet, 8+offset, 20, mouse.getAuxiliaryDamLevel2Hazards());
                        setCellValueIfNotEmpty(sheet, 8+offset, 21, mouse.getAuxiliaryDamLevel3Hazards());
                        setCellValueIfNotEmpty(sheet, 8+offset, 22, mouse.getInvestment2023());
                    }

                }
                order++;
                offset++;
            }


            // 写出到 ByteArrayOutputStream
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }




    //附件7
    @Override
    public ByteArrayInputStream setExcelValueSeven(List listResult) {
        List list = Arrays.asList("一", "二", "三", "四","五","六","七","八","九","十","十一","十二","十三","十四","十五","十六");

        String inputFilePath = "C:\\Users\\pingz.DESKTOP-4LNNVLR\\Desktop\\白蚁报表模板\\附件7堤防工程白蚁等害堤动物危害及防治情况普查明细表.xlsx"; // 模板文件路径
        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheetAt(0);

            int offsetDept = 0;
            int offsetProject = 0;
            int offsetXiaoji=0;

            if (listResult!=null&&listResult.size()>0){

              Map mapBiaoti=(Map) listResult.get(0);
              String nameOfBiaoTi= (String)mapBiaoti.get("deptName");
              setCellValueIfNotEmpty(sheet, 1, 1,nameOfBiaoTi+"-堤防工程白蚁等害堤动物危害及防治情况普查明细表");

                for (int i = 0; i < listResult.size(); i++) {
                    Map<String, Object> map = (Map<String, Object>) listResult.get(i);
                    if(map!=null&&map.size()>0){
                        setCellValueIfNotEmpty(sheet, 9+offsetDept+offsetProject+offsetXiaoji, 1,list.get(offsetDept));
                        setCellValueIfNotEmpty(sheet, 9+offsetDept+offsetProject+offsetXiaoji, 2, map.get("deptNameOfMinor"));
                        List projectList =  (List) map.get("project");
                        if (projectList!=null&&projectList.size()>0){
                            for (Object o : projectList) {
                                 Map projetMap = (Map) o;
                                Object singleKey = projetMap.keySet().iterator().next();
                                Fujian6 fujian6 =(Fujian6) projetMap.get(singleKey);
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 1,offsetProject+1);
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 2,fujian6.getDykeName());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 3,fujian6.getDykeLevel());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 4,fujian6.getDykeLength());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 5,fujian6.getUnitCount());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 6,fujian6.getTotalHazardLength());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 7,fujian6.getLevel1UnitCount());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 8,fujian6.getLevel2UnitCount());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 9,fujian6.getLevel3UnitCount());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 10,fujian6.getHazardCount());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 11,fujian6.getSeepageCount());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 12,fujian6.getPenetrationCount());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 13,fujian6.getSinkholeCount());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 14,fujian6.getNestCount());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 15,fujian6.getMedicationArea());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 16,fujian6.getGroutingLength());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 17,fujian6.getInvestment2023());


                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 20,fujian6.getTotalHazardLengthOfMouse());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 21,fujian6.getLevel1UnitCountOfMouse());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 22,fujian6.getLevel2UnitCountOfMouse());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 23,fujian6.getLevel3UnitCountOfMouse());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 24,fujian6.getHazardCountOfMouse());

                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 26,fujian6.getSeepageCountOfMouse());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 27,fujian6.getPenetrationCountOfMouse());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 28,fujian6.getSinkholeCountOfMouse());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 30,fujian6.getInvestment2023OfMouse());

                                //少一个治理处数，动物种类。

                                offsetProject++;


                            }

                        }
                        offsetDept++;
                        offsetXiaoji++;


                    }

                }
            }
            // 写出到 ByteArrayOutputStream
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    //附件8
    @Override
    public ByteArrayInputStream setExcelValueEight(List<Map> listResult) {
        List list = Arrays.asList("一", "二", "三", "四","五","六","七","八","九","十","十一","十二","十三","十四","十五","十六");

        String inputFilePath = "C:\\Users\\pingz.DESKTOP-4LNNVLR\\Desktop\\白蚁报表模板\\附件8堤防工程白蚁等害堤动物危害及防治情况普查汇总表.xlsx"; // 模板文件路径
        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheetAt(0);

            int offsetDept = 0;
            int offsetProject = 0;
            int offsetXiaoji=0;

            if (listResult!=null&&listResult.size()>0){

                Map mapBiaoti=(Map) listResult.get(0);
                String nameOfBiaoTi= (String)mapBiaoti.get("deptName");
                setCellValueIfNotEmpty(sheet, 1, 1,nameOfBiaoTi+"-堤防工程白蚁等害堤动物危害及防治情况普查明细表");

                for (int i = 0; i < listResult.size(); i++) {
                    Map<String, Object> map = (Map<String, Object>) listResult.get(i);
                    if(map!=null&&map.size()>0){
                        setCellValueIfNotEmpty(sheet, 9+offsetDept+offsetProject+offsetXiaoji, 1,list.get(offsetDept));
                        setCellValueIfNotEmpty(sheet, 9+offsetDept+offsetProject+offsetXiaoji, 2, map.get("deptNameOfMinor"));
                        List projectList =  (List) map.get("project");
                        if (projectList!=null&&projectList.size()>0){
                            for (Object o : projectList) {
                                Map projetMap = (Map) o;
                                Object singleKey = projetMap.keySet().iterator().next();
                                Fujian6 fujian6 =(Fujian6) projetMap.get(singleKey);
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 1,offsetProject+1);
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 2,fujian6.getDykeName());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 3,fujian6.getDykeLength());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 4,fujian6.getDykeLength());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 5,fujian6.getUnitCount());

                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 6,fujian6.getTotalHazardLength());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 7,fujian6.getLevel1UnitCount());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 8,fujian6.getLevel2UnitCount());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 9,fujian6.getLevel3UnitCount());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 10,fujian6.getHazardCount());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 11,fujian6.getSeepageCount());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 12,fujian6.getPenetrationCount());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 13,fujian6.getSinkholeCount());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 14,fujian6.getInvestment2023());


                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 17,fujian6.getTotalHazardLengthOfMouse());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 18,fujian6.getLevel1UnitCountOfMouse());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 19,fujian6.getLevel2UnitCountOfMouse());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 20,fujian6.getLevel3UnitCountOfMouse());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 21,fujian6.getHazardCountOfMouse());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 22,fujian6.getSeepageCountOfMouse());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 23,fujian6.getPenetrationCountOfMouse());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 24,fujian6.getSinkholeCountOfMouse());
                                setCellValueIfNotEmpty(sheet, 11+offsetProject+offsetDept+offsetXiaoji, 25,fujian6.getInvestment2023OfMouse());


                                //少一个治理处数，动物种类。
                                offsetProject++;
                            }

                        }
                        offsetDept++;
                        offsetXiaoji++;
                    }
                }
            }
            // 写出到 ByteArrayOutputStream
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static void insertRowsOfBaiYi(Sheet sheet, int rowIndex, int numberOfRows) {
        // 复制样式的行
        Row sourceRow = sheet.getRow(rowIndex - 1);
        int lastRowNum = sheet.getLastRowNum();

        // 向下移动行以腾出空间
        sheet.shiftRows(rowIndex, lastRowNum, numberOfRows);

        // 创建新行并复制样式
        for (int i = 0; i < numberOfRows; i++) {
            Row newRow = sheet.createRow(rowIndex + i);
            copyRowStyle(sourceRow, newRow);

            // 合并新行的第五列和第六列
            sheet.addMergedRegion(new CellRangeAddress(rowIndex + i, rowIndex + i, 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex + i, rowIndex + i, 10, 11));
        }
    }

    // 用于新增 獾、狐、鼠等其他动物危害及防治情况
    private static void insertRowsOfMouse(Sheet sheet, int rowIndex, int numberOfRows) {
        // 复制样式的行
        Row sourceRow = sheet.getRow(rowIndex - 1);
        int lastRowNum = sheet.getLastRowNum();

        // 向下移动行以腾出空间
        sheet.shiftRows(rowIndex, lastRowNum, numberOfRows);

        // 创建新行并复制样式
        for (int i = 0; i < numberOfRows; i++) {
            Row newRow = sheet.createRow(rowIndex + i);
            copyRowStyle(sourceRow, newRow);

            // 合并新行的第五列和第六列
            sheet.addMergedRegion(new CellRangeAddress(rowIndex + i, rowIndex + i, 8, 9));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex + i, rowIndex + i, 10, 11));
        }
    }

    private static void copyRowStyle(Row sourceRow, Row targetRow) {
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            Cell oldCell = sourceRow.getCell(i);
            Cell newCell = targetRow.createCell(i);

            if (oldCell != null) {
                newCell.setCellStyle(oldCell.getCellStyle());

                // 如果需要复制单元格内容，可以在这里添加代码
                switch (oldCell.getCellType()) {
                    case STRING:
                        newCell.setCellValue(oldCell.getStringCellValue());
                        break;
                    case NUMERIC:
                        newCell.setCellValue(oldCell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        newCell.setCellValue(oldCell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        newCell.setCellFormula(oldCell.getCellFormula());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private static void setCellValue(Sheet sheet, int rowIndex, int colIndex, String value) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            cell = row.createCell(colIndex);
        }
        cell.setCellValue(value);
    }


    /**
     * 是哪个单元格，就写哪个单元格，方法里都都减1了。
     * @param sheet
     * @param rowIndex
     * @param colIndex
     * @param value
     */
    private static void setCellValueIfNotEmpty(Sheet sheet, int rowIndex, int colIndex, Object value) {
        if (value != null && !value.toString().isEmpty()) {
            setCellValue(sheet, rowIndex - 1, colIndex - 1, value.toString());
        }
    }
}
