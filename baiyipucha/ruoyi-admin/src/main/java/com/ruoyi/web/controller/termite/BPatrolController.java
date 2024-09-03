package com.ruoyi.web.controller.termite;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.BPatrolMapper;
import com.ruoyi.system.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 巡查Controller
 *
 * @author ruoyi
 * @date 2024-03-05
 */
@RestController
@RequestMapping("/system/patrol")
public class BPatrolController extends BaseController {
    @Autowired
    private IBPatrolService bPatrolService;

    @Autowired
    private IBAuditService ibAuditService;
    @Autowired
    private IBProjectService projectService;
    @Autowired
    private IBPatrolFileService fileService;

    @Autowired
    ISysDeptService deptService;

    @Autowired
    BPatrolMapper bPatrolMapper;

    /**
     * 查询巡查列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BPatrol bPatrol) {
        // List<BPatrol> list = bPatrolService.selectBPatrolList2(bPatrol);
        startPage();
        List<Map<String, Objects>> maps = bPatrolMapper.selectBPatrolList2(bPatrol);
        return getDataTable(maps);
    }

    /**
     * 查询巡查列表根据条件
     */
    @GetMapping("/listByCondition")
    public TableDataInfo listByCondition(BPatrol bPatrol) {
        startPage();
        List<BPatrol> bPatrols = bPatrolMapper.selectBPatrolList(bPatrol);
        return getDataTable(bPatrols);
    }


    /**
     * 导出巡查列表
     */
    @Log(title = "巡查", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BPatrol bPatrol) {
        List<BPatrol> list = bPatrolService.selectBPatrolList(bPatrol);
        ExcelUtil<BPatrol> util = new ExcelUtil<BPatrol>(BPatrol.class);
        util.exportExcel(response, list, "巡查数据");
    }

    /**
     * 获取巡查详细信息
     */

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        BPatrol bPatrol = bPatrolService.selectBPatrolById(id);
        return success(bPatrol);
    }

    /**
     * 新增巡查
     */
    @PostMapping
    public AjaxResult add(@RequestBody BPatrol bPatrol) {
        bPatrol.setAuditStatus("1");
        //提交数据的时候需要生成审核信息，根据当前提交人所在的部门生成。
        bPatrol.setFreedom2(DateUtils.getTime());
        int rss = bPatrolService.insertBPatrol(bPatrol);
        try {
            Long projectid = bPatrol.getProjectId();
            BProject projectinfo = projectService.selectBProjectById(projectid);
            SysDept dept = deptService.selectDeptById(projectinfo.getDeptId());
            BAudit audit = new BAudit();
            audit.setPatrolId(bPatrol.getId());
            audit.setCurrentDept(dept.getDeptId());
            audit.setSubordinateDept(dept.getParentId());
            audit.setDataState(1l);
            ibAuditService.insertBAudit(audit);
        } catch (Exception e) {
            System.out.println("生成审核信息出现错误。");
        }

        try {
            if (!"".equals(bPatrol.getFreedom1())) {

                String[] filelist = bPatrol.getFreedom1().split(",");
                for (String s : filelist) {
                    BPatrolFile file = new BPatrolFile();
                    file.setPatorlId(bPatrol.getId());
                    file.setFilePath(s);
                    file.setDelFlag("0");
                    fileService.insertBPatrolFile(file);
                }
            }
        } catch (Exception e) {
            System.out.printf("保存图片失败。");
        }

        return toAjax(rss);
    }



    /**
     * 修改巡查
     */
    @PutMapping
    public AjaxResult edit(@RequestBody BPatrol bPatrol) {
        //修改驳回的数据，需要重新发起审核请求
        if (bPatrol.getAuditStatus().equals("4")) {
            bPatrol.setAuditStatus("1");
            try {
                Long projectid = bPatrol.getProjectId();
                BProject projectinfo = projectService.selectBProjectById(projectid);
                SysDept dept = deptService.selectDeptById(projectinfo.getDeptId());
                BAudit audit = new BAudit();
                audit.setPatrolId(bPatrol.getId());
                audit.setCurrentDept(dept.getDeptId());
                audit.setSubordinateDept(dept.getParentId());
                audit.setDataState(1l);
                ibAuditService.insertBAudit(audit);
            } catch (Exception e) {
                System.out.println("生成审核信息出现错误。");
            }

        }

        return toAjax(bPatrolService.updateBPatrol(bPatrol));
    }

    /**
     * 删除巡查
     */
    @PreAuthorize("@ss.hasPermi('system:patrol:remove')")
    @Log(title = "巡查", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bPatrolService.deleteBPatrolByIds(ids));
    }

    /**
     * 查询
     */
    @GetMapping("/listall/{id}")
    public AjaxResult listall(@PathVariable int id) {
        BPatrolUnitPlaceAll bPatrolUnitPlaceAll = new BPatrolUnitPlaceAll();

        BPatrolUnitPlace bPatrolUnitPlace = bPatrolMapper.listall(id);
        List<BPatrol> bPatrolList = new ArrayList<>();
        String patrolId = bPatrolUnitPlace.getPatrolId();
        String[] patrolIdArray = patrolId.split(",");
        for (String s : patrolIdArray) {
            String replace = s.replace(",", "");
            long i = Long.parseLong((replace.toString()));
            BPatrol bPatrol = bPatrolMapper.selectBPatrolById(i);
            String freedom3 = bPatrol.getFreedom3();
            String typeName = bPatrolMapper.selectfreedom3(freedom3);
            bPatrol.setTypeName(typeName);


            bPatrolList.add(bPatrol);
        }
        bPatrolUnitPlaceAll.setAddress(bPatrolUnitPlace.getAdress());
        bPatrolUnitPlaceAll.setPatrolUnit(String.valueOf(bPatrolUnitPlace.getPatrolUnit()));
        bPatrolUnitPlaceAll.setPatrolType(bPatrolUnitPlace.getDetrimentType());
        bPatrolUnitPlaceAll.setProjectId(bPatrolUnitPlace.getProjectId());
        bPatrolUnitPlaceAll.setInspectorId(Long.valueOf(bPatrolUnitPlace.getInspector()));
        bPatrolUnitPlaceAll.setLat(Integer.parseInt(bPatrolUnitPlace.getLat()));
        bPatrolUnitPlaceAll.setLon(Integer.parseInt(bPatrolUnitPlace.getLon()));

        bPatrolUnitPlaceAll.setPatrols(bPatrolList);


        return AjaxResult.success(bPatrolUnitPlaceAll);
    }


    //导出zip图片  该目录
    @RequestMapping("/exportZipAllById")
    public AjaxResult exportZipAllById(Long id,HttpServletResponse response) throws  IOException  {
        String prefix = "https://119.3.215.237:8083";
        SSLUtils.disableCertificateValidation();
        BPatrol bPatrol = bPatrolService.selectBPatrolById(id);
        String freedom11 = bPatrol.getFreedom1();
        String[] split = freedom11.split(",");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int i=0;
        // 遍历 split 数组
        for (String element : split) {
            String url = prefix + element;
            String fileName = "image" + i + ".jpg";
            i++;
            Map<String, Object> map= new HashMap<String, Object>();
            map.put("url", url);
            map.put("fileName", fileName);
            list.add(map);
        }
        response.setHeader("Content-Disposition", "attachment; filename=images.zip");
        response.setContentType("text/html;charset=UTF-8");

        downloadAndZipImages(list, response);

        // 将 Zip 文件写入 HttpServletResponse 输出流

        return  AjaxResult.success();

    }



/*    *//**
     * 导出所有图片的压缩包
     *//*
    @RequestMapping("/exportZipAll")
    public void exportZipAll(Long id) {
        String prefix = "https://119.3.215.237:8083";
        SSLUtils.disableCertificateValidation();
        BPatrol bPatrol = new BPatrol();
        bPatrol.setId(id);
        List<BPatrol> bPatrolList = bPatrolService.selectBPatrolList(bPatrol);
        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
        String zipFilePath = desktopPath + File.separator + "images.zip";


        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (BPatrol patrol : bPatrolList) {
            String freedom1 = patrol.getFreedom1();
            String url = prefix + freedom1;
            String fileName = "image" + patrol.getId() + ".jpg";
            Map<String, Object> map= new HashMap<String, Object>();
            map.put("url", url);
            map.put("fileName", fileName);
            list.add(map);
        }
        downloadAndZipImages(list, zipFilePath);
    }*/


    public static void downloadAndZipImages(  List<Map<String, Object>> list, HttpServletResponse response) {
        try {
            // 创建 ZIP 文件输出流
            ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
            list.stream().forEach(map -> {
                String url = (String) map.get("url");
                String fileName = (String) map.get("fileName");
                // 下载第 该 张图片并添加到 ZIP 文件
                downloadAndAddToZip(url, (String) fileName, zipOutputStream);
            });

            // 关闭 ZIP 文件输出流
            zipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void downloadAndAddToZip(String url, String fileName, ZipOutputStream zipOutputStream) {
        try {
            // 创建 URL 对象
            URL imageUrl = new URL(url);

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();

            // 获取输入流
            InputStream inputStream = connection.getInputStream();

            // 添加 ZIP 文件条目
            zipOutputStream.putNextEntry(new ZipEntry(fileName));

            // 写入文件数据
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, length);
            }

            // 关闭输入流
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


