package com.zkyt.util;

import cn.hutool.core.io.IoUtil;
import com.zkyt.exception.ServiceException;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * linux or windows 特殊目录下使用会出现权限问题，不会出现错误，但无法获取信息！
 * @author lc
 * @since 7/11/22
 */
public final class FileUtil {
    private FileUtil(){}
    /**
     * 获取目录所有文件的字节数-不包含子目录
     * @since 7/11/22
     */
    public static long getFolderByteSize(File file){
        long byteSize = 0;
        File[] files = file.listFiles();
        if (!file.isDirectory() || files == null) {
            return byteSize;
        }
        for (File f : files) {
            if (f.isFile()) {
                byteSize += f.length();
                System.out.println(byteSize);
            }
        }
        return byteSize;
    }


    /**
     * 获取已有的块数量
     * @since 8/2/22
     */
    public static int getSkipBlock(File file){
        File[] files = file.listFiles();
        if (!file.isDirectory() || files == null) {
            return 0;
        }
        return files.length;
    }

    @Data
    static class FileInfo implements Serializable {

        private String fileName;
        /**
         *  1 是目录，0 默认文件
         */
        private int isDirectory;
    }

    /**
     * 获取目录所有文件名-不包含子目录,目录也会显示
     * @since 7/11/22
     */
    public static ArrayList<FileInfo> getAllFileName(String path){
        return getAllFileName(new File(path));
    }

    /**
     * 获取目录所有文件名-不包含子目录,目录也会显示
     * @since 7/11/22
     */
    public static ArrayList<FileInfo> getAllFileName(File file){
        ArrayList<FileInfo> list = new ArrayList<>();
        File[] files = file.listFiles();
        if ( files == null) {
            return list;
        }
        for (File f : files) {
            FileInfo info = new FileInfo();
            info.setFileName(f.getName());
            if (f.isDirectory()) {
                info.setIsDirectory(1);
            }
            list.add(info);
        }
        return list;
    }

    /**
     *
     * 获取文件夹下的所有文件-包含子目录
     * @since 7/11/22
     */
    public static ArrayList<Object> getFolderAllFolderFileName(File file){
        ArrayList<Object> list = new ArrayList<>();
        File[] files = file.listFiles();
        if (!file.isDirectory() || files == null) {
            return list;
        }
        for (File f : files) {
            if (f.isFile()) {
                list.add(f.getName());
            }else {
                HashMap<String, Object> map = new HashMap<>();
                map.put(f.getName(),getFolderAllFolderFileName(f));
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 合并的目录里面只能是分块文件，不允许有其他文件！
     * @since 7/13/22
     */
    public static void merge(File file,String path){
        File f = new File(path);
        File[] files = f.listFiles();
        if (files == null) {
            throw  new ServiceException("合并错误！");
        }
        //合并
        for (int j = 0; j < files.length; j++) {
            File t = new File(path + File.separator + j);
            byte[] b = IoUtil.readBytes(cn.hutool.core.io.FileUtil.getInputStream(t));
            cn.hutool.core.io.FileUtil.writeBytes(b, file, 0, b.length, true);
            t.delete();
        }
        File t = new File(path);
        t.delete();
    }


    public static void main(String[] args) {
        String path = "/home/lco/Downloads";
        File file = new File(path);
        System.out.println(getAllFileName(path));
        System.out.println(getFolderByteSize(file));
        File f = new File("/home/lco/Downloads");
        System.out.println(getFolderAllFolderFileName(f));
    }
}
