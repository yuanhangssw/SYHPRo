package com.tianji.dam.service;

import com.tianji.dam.bean.JtsRTree;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.RollingData;
import com.tianji.dam.domain.T1;
import com.tianji.dam.domain.vo.DamsJtsTreeVo;
import com.tianji.dam.domain.vo.T1VO;
import com.tianji.dam.mapper.T1Mapper;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
import com.tj.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * 原始轨迹数据信息 服务层
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class T1Service {

    @Autowired
    private T1Mapper t1Mapper;

    /**
     * 获取原始轨迹数据信息集合
     * @param param
     * @return 原始轨迹数据信息集合
     */
    public List<T1> select(T1VO param){
        return t1Mapper.select(param);
    }

    /**
     * 数据归档查询
     * @param vo
     * @return
     */
    public List<T1> selectByRanges(T1VO vo){
        return t1Mapper.selectByRanges(vo);
    }
 
    public List<T1> selectByTime(String tablename,long actualstarttime,long actualendtime){
        return t1Mapper.selectByTime(tablename,actualstarttime,actualendtime);
    }

    public List<T1> selectByEvolution(String tablename,double begin,double end){
        return t1Mapper.selectByEvolution(tablename,begin,end);
    }
    /**
     * 复制原始表数据到对应的单元数据表
     * @param vo
     * @return
     */
    public Integer addTableData(T1VO vo){
        return t1Mapper.addTableData(vo);
    }

    Double division = 0.1;
    BigDecimal gridSize = new BigDecimal(0.1);

    /**
     * 根据自定义车辆查询历史回放数据
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    public void getHistoryByCustomCar(){
        // 开始结束日期 高程
        // 车辆id  开始结束日期 集合

        //1.查询t1原始表数据
        List<RollingData> originalList = t1Mapper.getRollingData();
        List<DamsConstruction> damsList = new LinkedList<>();
        //2.数据分区域 0,-750 0,0 1050,0 1050,-750
        //分成若干区域
        long start = System.currentTimeMillis();
        Map<DamsJtsTreeVo,List<RollingData>> maps = new HashMap<>();
        for (RollingData original:originalList) {//加入查询百万条数据
            int j=-1;
            List<DamsJtsTreeVo> treeVos = JtsRTree.query(original.getZhuangX(), original.getZhuangY());
            if(!treeVos.isEmpty()){
                for (int i = 0; i < treeVos.size(); i++) {
                    DamsJtsTreeVo vo = treeVos.get(i);
                    //高程700-单元工程的高程>层高200 肯定不在这个区域
                    BigDecimal sub = new BigDecimal(original.getElevation());
                    //轨迹高程减去单元高程
                    if(StringUtils.isNotNull(vo.getGaocheng())){
                        sub = new BigDecimal(original.getElevation()).subtract(new BigDecimal(vo.getGaocheng()));
                    }
                    if(StringUtils.isNotNull(vo.getCenggao())){
                        //>层高 也就是铺料厚度 表示不在此区域
                        if(sub.compareTo(new BigDecimal(vo.getCenggao())) == 1){
                            System.out.println("no quyu........");
                        }else{
                            j=i;
                            break;
                        }
                    }
                }
            }else{
                System.out.println("-----------------no data........");
            }
            if(j>=0) {
                DamsJtsTreeVo vo = treeVos.get(j);
                boolean isEmpty=maps.containsKey(vo);
                if(isEmpty){
                    List<RollingData> data = maps.get(vo);
                    data.add(original);
                    maps.put(vo,data);
                }else{
                    maps.put(vo,new LinkedList<RollingData>(Arrays.asList(original)));
                }
            }
        }
        System.out.println((System.currentTimeMillis() - start) + "毫秒");

        /*//1050 0   0 750
        //3.跟据区域画图
        BigDecimal xend  = new BigDecimal(1000);
        BigDecimal xbegin =new BigDecimal(0);
        BigDecimal yend  =new BigDecimal(0);
        BigDecimal ybegin =new BigDecimal(-750);
        int xSize=(xend.subtract(xbegin).divide(gridSize,0,BigDecimal.ROUND_UP)).intValue();//行数
        int ySize=(yend.subtract(ybegin).divide(gridSize,0,BigDecimal.ROUND_UP)).intValue();//列数
        //绘制图片
        BufferedImage bi = new BufferedImage(xSize,ySize,BufferedImage.TYPE_INT_ARGB);//TYPE_INT_ARGB 为透明的 INT精确度达到一定,RGB三原色，高度70,宽度150
        //得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        for(int i=0;i<xSize-1;i++) {
            for(int j=0;j<ySize-1;j++) {
                int rollingTimes=i/9==0?3:0;//碾压边次
                g2.setColor(getColorByCount2(rollingTimes));
                g2.fillRect(i, j, 1, 1);
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi,"PNG",baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();
        String bsae64Str="data:image/png;base64,"+ Base64.encodeBase64String(bytes);
        System.out.println(bsae64Str);*/
    }

    public static Color getColorByCount2(Integer count) {
        if(count.equals(0)) {
            return new Color(255,255,255,0);
        }
        if(count.equals(1)) {
            return new Color(225,148, 207);
        }
        if(count.equals(2)) {
            return new Color(245,102, 102);
        }
        if(count.equals(3)) {
            return new Color(255,0,0);
        }
        if(count.equals(4)) {
            return new Color(185,40,71);
        }
        if(count.equals(5)) {
            return new Color(255,0,243);
        }
        if(count.equals(6)) {
            return new Color(72,238,217);
        }
        if(count.equals(7)) {
            return new Color(71,195,238);
        }
        if(count.equals(8)) {
            return new Color(133,244,133);
        }
        if(count.equals(9)) {
            return new Color(133,244,133);
        }
        if(count.equals(10)) {
            return new Color(133,244,133);
        }
        if(count.equals(11)) {
            return new Color(133,244,133);
        }
        if(count>11) {
            return new Color(133,244,133);
        }
        return null;
    }
}
