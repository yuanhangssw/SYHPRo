package com.ruoyi.web.controller.Test;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


//附件2   模板使用
public class Test2 {
    public static void main(String[] args) {
        String inputFilePath = "C:\\Users\\pingz.DESKTOP-4LNNVLR\\Desktop\\白蚁报表模板\\附件2水库大坝白蚁等害堤动物危害及防治情况普查登记表.xlsx"; // 模板文件路径
        String outputFilePath = System.getProperty("user.home") + "/Desktop/output.xlsx"; // 导出路径

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            // 插入5行到第18行上方，样式和第17行一致
            insertRowsOfBaiYi(sheet, 17, 5);

            // 插入5行到第30行上方，样式和第29行一致
            insertRowsOfMouse(sheet, 30, 5);

            // 修改第一行，第一列的值
            setCellValue(sheet, 0, 0, "三峡大坝水库工程");
            setCellValue(sheet, 1, 1, "煎锅");

            // 写出到文件
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                workbook.write(fos);
            }

            System.out.println("Excel 文件已成功导出到桌面");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 用于新增 白蚁的列
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
}
