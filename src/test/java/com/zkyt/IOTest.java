package com.zkyt;

import java.io.File;

/**
 * 递归实现遍历文件夹，获得子文件夹的路径
 */

public class IOTest {
    public static void main(String[] ssd) {
        //这里的文件路径是绝对路径（即必须写明具体在哪儿）
        isDirectory(new File("d://data"));
    }

    public static void isDirectory(File file) {
        if(file.exists()){
            if (file.isFile()) {
                System.out.println("file is ==>>" + file.getAbsolutePath());
            }else{
                File[] list = file.listFiles();
                if (list.length == 0) {
                    System.out.println(file.getAbsolutePath() + " is null");
                } else {
                    for (int i = 0; i < list.length; i++) {
                        isDirectory(list[i]);//递归调用
                    }
                }
            }
        }else{
            System.out.println("文件不存在！");
        }
    }
}
