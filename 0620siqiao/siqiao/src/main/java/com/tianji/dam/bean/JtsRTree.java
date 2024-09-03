package com.tianji.dam.bean;

import com.alibaba.fastjson.JSONArray;
import com.tianji.dam.domain.vo.DamsJtsTreeVo;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tj.common.utils.StringUtils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.strtree.STRtree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.LinkedList;import java.util.ArrayList;
import java.util.List;

@Component
public class JtsRTree implements Runnable{
	
	 @Autowired
     private BeanContext beancontext;

    /*@Value("${enableJts}")
    private boolean enableJts;*/

   /* @Autowired
    private TDamsconstructionMapper damsMapper;*/

    //定义全局R树
    public static STRtree tree=new STRtree();
    public static GeometryFactory factory=new GeometryFactory();

    @PostConstruct
    public void init(){
        //启动线程实例
        new Thread(this).start();
        /*if(enableJts){
            new Thread(this).start();
        }else{
            System.out.println("不初始化R树线程");
        }*/
    }    

    
    @Override
    public void run() {
        synchronized(this){
            try {
                executeTask();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化R树
     */
    public void executeTask(){
        tree = new STRtree();
        //获取所有单元
        TDamsconstructionMapper damsMapper = beancontext.getApplicationContext().getBean(TDamsconstructionMapper.class);
        List<DamsJtsTreeVo> vos = damsMapper.selectDams();
        for (DamsJtsTreeVo vo : vos) {
            if(StringUtils.isNotNull(vo.getRanges())){
                List<Coordinate> list = JSONArray.parseArray(vo.getRanges(), Coordinate.class);
                Coordinate[] array= new Coordinate[list.size()+1];  //最重要，不能遗漏，预先申请好数组空间
                list.toArray(array);
                array[list.size()]=array[0];
                Polygon polygon=factory.createPolygon(array);
                tree.insert(polygon.getEnvelopeInternal(), vo);
            }
        }
        tree.build();
        System.out.println("success:R树索引创建成功！"+vos.size());
    }

    /**
     * R树查询
     * @return
     */
    public static List<DamsJtsTreeVo> query(double x, double y){
        Coordinate coordinate = new Coordinate(x, y);
        Point point = factory.createPoint(coordinate);
        List <DamsJtsTreeVo> result=new LinkedList<DamsJtsTreeVo>();
        List list=tree.query(point.getEnvelopeInternal());
        for (Object object : list) {
            DamsJtsTreeVo p = (DamsJtsTreeVo)object;
            result.add(p);
        }
        return result;
    }

    /**
     * R树新增
     * @param dams 单元信息
     * @param type 1新增 2删除 3修改
     * @return
     */
    public static boolean add(DamsJtsTreeVo dams,int type){
        try{
            if(StringUtils.isNotNull(dams.getRanges())){
                List<Coordinate> list = JSONArray.parseArray(dams.getRanges(), Coordinate.class);
                Coordinate[] array= new Coordinate[list.size()+1];  //最重要，不能遗漏，预先申请好数组空间
                list.toArray(array);
                array[list.size()]=array[0];
                Polygon polygon=factory.createPolygon(array);
                if(type==1) {
                    tree.insert(polygon.getEnvelopeInternal(), dams);
                }else if(type==2) {
                    tree.remove(polygon.getEnvelopeInternal(),dams);
                }
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

}
