package com.tianji.dam.Test;

// Java code for polygon gridding
import java.awt.Polygon;
import java.awt.geom.Point2D;
public class PolygonGridding {
    public static void main(String[] args) {
        // Create a polygon
        Polygon polygon = new Polygon();
        polygon.addPoint(0, 0);
        polygon.addPoint(30, 10);
        polygon.addPoint(28, 50);
        polygon.addPoint(50, 100);
        // Create a grid of points
        int gridSize = 10;
        Point2D[][] grid = new Point2D[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                double x = i * 10.0 / (gridSize - 1);
                double y = j * 10.0 / (gridSize - 1);
                grid[i][j] = new Point2D.Double(x, y);
            }
        }
        // Filter out points outside the polygon
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (!polygon.contains(grid[i][j])) {
                    grid[i][j] = null;
                }
            }
        }
        // Print the grid
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] != null) {
                    System.out.print("X");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
