package com.tj.web.controller.dam;

import com.alibaba.fastjson.JSONArray;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.vo.RangesVO;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.mileageutil.Mileage;
import com.tianji.dam.service.TDamsconstructionService;
import com.tianji.dam.utils.productareapoints.scan.Point;
import com.tj.common.annotation.Log;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@Api(value = "分部工程controller", tags = {"分部工程操作接口"})
@RestController
@RequestMapping("/reservoir/td")
public class TDamsconstructionController extends BaseController {

    @Autowired
    private TDamsconstructionService tdService;
    @Autowired
    private TDamsconstructionMapper mapper;

    @Log(title = "分部工程管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改单元工程边界", nickname = "editRanges")
    public AjaxResult editRanges(@Validated @RequestBody RangesVO vo) {
        return toAjax(tdService.updateRanges(vo.getId(), vo.getRanges()));
    }


    @GetMapping("/getdamcoord")
    public AjaxResult getdamcoord(Integer pid) {

        List<DamsConstruction> alldam = mapper.getdambypid(pid);
        List<Map> resultlist = new ArrayList<>();
        Mileage mileage = Mileage.getmileage();
        for (DamsConstruction damsConstruction : alldam) {
            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
            List<Map<String, Object>> listcoord = new ArrayList<>();

            for (Coordinate coordinate : list) {
                Map<String, Object> temp = new HashMap<>();
                double x = coordinate.x;
                double y = coordinate.y;
                double[] coords = mileage.pixels2Coord(1, x, y);
                Coordinate newcoord = new Coordinate(coords[0], coords[1]);
                temp.put("coordx", newcoord.y);
                temp.put("coordy", newcoord.x);
                listcoord.add(temp);
            }
            System.out.println(damsConstruction.getId() + ">" + damsConstruction.getTitle() + ">" + damsConstruction.getGaocheng() + ">" + listcoord);


            Map<String, Object> dm = new HashMap<>();

            dm.put("ranges", JSONArray.toJSONString(listcoord));
            dm.put("title", damsConstruction.getTitle());
            dm.put("gaocheng", damsConstruction.getGaocheng());
            dm.put("gaochengact", damsConstruction.getGaochengact());
            dm.put("id", damsConstruction.getId());
            resultlist.add(dm);
        }

        return AjaxResult.success(resultlist);
    }


    /*//算法测试
    @GetMapping("/alphaShapeExample")
    public AjaxResult alphaShapeExample() {
        //查找数据获取 cordX、cordY
        List<Coordinate> points = mapper.selectTestPointCordXAndCordY();
        GeometryFactory geomFactory = new GeometryFactory();
        // 使用Delaunay三角剖分生成Alpha Shape
        DelaunayTriangulationBuilder triangulationBuilder = new DelaunayTriangulationBuilder();
        triangulationBuilder.setSites(points);
        Geometry triangulation = triangulationBuilder.getTriangles(geomFactory);
        double alpha = 0.5;  // Alpha值，控制形状的紧密度
        Geometry alphaShape = triangulation.buffer(alpha);


        for (int i = 0; i < alphaShape.getNumGeometries(); i++) {
            Polygon polygon = (Polygon) alphaShape.getGeometryN(i);
            Coordinate[] edgePoints = polygon.getCoordinates();
            for (Coordinate edgePoint : edgePoints) {
                System.out.println("Edge Point: " + edgePoint);
            }
        }
        return AjaxResult.success();
    }
*/

    //测试1w个点  索性后才能呢个
    @GetMapping("/TestConvexHull")
    public void TestConvexHull() {
        //1.查询某个表的x和y
        List<Point> pointList = mapper.selectTestPointCordXAndCordY();

        List<Point> convexHull = getConvexHull(pointList);
        System.out.println("Convex Hull points:");
        for (Point p : convexHull) {
            System.out.println("(" + p.x + ", " + p.y + ")");
        }
    }

    public static List<Point> getConvexHull(List<Point> points) {
        // 按 x 坐标排序，如果 x 坐标相同，按 y 坐标排序
        Collections.sort(points, new Comparator<Point>() {
            public int compare(Point p1, Point p2) {
                if (p1.x != p2.x) return Double.compare(p1.x, p2.x);
                return Double.compare(p1.y, p2.y);
            }
        });

        // 构建下边界
        List<Point> lower = new ArrayList<>();
        for (Point p : points) {
            while (lower.size() >= 2 && cross(lower.get(lower.size() - 2), lower.get(lower.size() - 1), p) <= 0) {
                lower.remove(lower.size() - 1);
            }
            lower.add(p);
        }

        // 构建上边界
        List<Point> upper = new ArrayList<>();
        for (int i = points.size() - 1; i >= 0; i--) {
            Point p = points.get(i);
            while (upper.size() >= 2 && cross(upper.get(upper.size() - 2), upper.get(upper.size() - 1), p) <= 0) {
                upper.remove(upper.size() - 1);
            }
            upper.add(p);
        }

        // 移除最后一个点，因为它重复出现
        lower.remove(lower.size() - 1);
        upper.remove(upper.size() - 1);

        // 合并下边界和上边界，形成凸包
        lower.addAll(upper);
        return lower;
    }


    private static double cross(Point o, Point a, Point b) {
        return (a.x - o.x) * (b.y - o.y) - (a.y - o.y) * (b.x - o.x);
    }

}
