package com.zkyt;



import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileTest {
    private static ArrayList<String> filelist = new ArrayList<String>();

    public static void main(String[] args) throws Exception {

        String filePath = "D://data";
        Map files = getFiles(filePath);
        System.out.println(files);
    }
    /*
     * 通过递归得到某一路径下所有的目录及其文件
     */
    static Map getFiles(String filePath){
        File root = new File(filePath);
        File[] files = root.listFiles();
        Map map = new HashMap();
        for(File file:files){
            String fileName = "";
            List list = new ArrayList();
            if(file.isDirectory()){
                /*
                 * 递归调用
                 */
                fileName = file.getName();
                getFiles(file.getAbsolutePath());
                filelist.add(file.getAbsolutePath());
                //System.out.println("显示"+filePath+"下所有子目录及其文件"+file.getAbsolutePath());
            }else{
                //System.out.println("显示"+filePath+"下所有子目录"+file.getAbsolutePath());
            }
        }
        return map;
    }
}