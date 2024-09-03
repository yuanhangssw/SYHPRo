package com.ruoyi.web.controller.termite;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;



public class ImageDownloader {

    public static void main(String[] args) {
        SSLUtils.disableCertificateValidation();
        String url1 = "https://119.3.215.237:8083/profile/upload/2024/04/10/tmp_764f7ee06c2fd197d0dab60b86ad9b7ecf84488b5b7db492_20240410184058A027.jpg";
        String url2 = "https://119.3.215.237:8083/profile/upload/2024/04/10/tmp_4cd700cffdecda53dfb5a482074d0855a20c5285b8de4257_20240410184232A028.jpg";

        String fileName1 = "image1.jpg";
        String fileName2 = "image2.jpg";

        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
        String zipFilePath = desktopPath + File.separator + "images.zip";

        downloadAndZipImages(url1, url2, fileName1, fileName2, zipFilePath);
    }

    public static void downloadAndZipImages(String url1, String url2, String fileName1, String fileName2, String zipFilePath) {
        try {
            // 创建 ZIP 文件输出流
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath));

            // 下载第一张图片并添加到 ZIP 文件
            downloadAndAddToZip(url1, fileName1, zipOutputStream);

            // 下载第二张图片并添加到 ZIP 文件
            downloadAndAddToZip(url2, fileName2, zipOutputStream);

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
