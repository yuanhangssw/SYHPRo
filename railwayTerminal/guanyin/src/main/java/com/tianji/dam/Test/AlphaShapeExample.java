import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;

import java.util.ArrayList;
import java.util.List;

public class AlphaShapeExample {
    public static void main(String[] args) {
        // 创建点集
        List<Coordinate> points = new ArrayList<>();
        points.add(new Coordinate(0, 0));
        points.add(new Coordinate(1, 0));
        points.add(new Coordinate(0, 1));
        points.add(new Coordinate(1, 1));
        points.add(new Coordinate(0.5, 0.5));


        GeometryFactory geomFactory = new GeometryFactory();

        // 使用Delaunay三角剖分生成Alpha Shape
        DelaunayTriangulationBuilder triangulationBuilder = new DelaunayTriangulationBuilder();
        triangulationBuilder.setSites(points);

        Geometry triangulation = triangulationBuilder.getTriangles(geomFactory);

        double alpha = 0.5;  // Alpha值，控制形状的紧密度

        // 提取Alpha Shape边缘
        Geometry alphaShape = triangulation.buffer(alpha);

        // 获取边缘点
        for (int i = 0; i < alphaShape.getNumGeometries(); i++) {
            Polygon polygon = (Polygon) alphaShape.getGeometryN(i);
            Coordinate[] edgePoints = polygon.getCoordinates();
            for (Coordinate edgePoint : edgePoints) {
                System.out.println("Edge Point: " + edgePoint);
            }
        }
    }
}
