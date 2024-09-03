package com.ruoyi.web.controller.termite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        int total = 10;  // 进度条总长度

        for (int i = 1; i <= total; i++) {
            System.out.print("\r进度：[");
            for (int j = 1; j <= i; j++) {
                System.out.print('=');
            }
            for (int k = i; k < total; k++) {
                System.out.print(' ');
            }
            System.out.print("] " + i + "%");
            Thread.sleep(100);
        }
    }
}
