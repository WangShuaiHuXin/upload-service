package com.zkyt;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zkyt.dto.CheckDto;
import com.zkyt.util.TokenUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author lc
 * @since 7/13/22
 */
public class FileUploadTest {
    public static void main(String[] args) throws IOException {
        checkFileTest(); //检查

        //步骤1
//        step1();//模拟第一个块上传

        //步骤2
        step2();//模拟断点重传上传
        mergeTest(); //step1 执行后才能执行合并接口
    }
    private static final int bytesSize = 1024 * 1024 * 10;
    public static long timestamp = 1657704152000L;
    public static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHBpcmVUaW1lIjoxNjU5NjgzOTMxNDYwLCJ1c2VySWQiOjF9.dzFdSzcyJxOmmaVhiaVzWGTwWJ5scw2MvMSFRBeiiaI";
    public static String fileName = "CLion2020.zip";
    public static String filePath = "F:\\data\\"+fileName;
    public static String checkFilePath = "F:\\data\\"+fileName;


    /**
     * 测试检查文件
     * @since 8/2/22
     */
    public static void  checkFileTest(){
        File file = new File(checkFilePath);
        HttpRequest request = HttpUtil.createGet("http://localhost:8999/file/check");
        request.header(TokenUtil.HeaderKey, token);
        CheckDto checkDto = new CheckDto();
        checkDto.setFileName(fileName);
  /*      checkDto.setByteSize(file.length());
        checkDto.setFileUpdateTime(timestamp);
        checkDto.setPath("");*/

        ObjectMapper mapper = new ObjectMapper();
        HashMap hashMap = mapper.convertValue(checkDto, HashMap.class);
        request.form(hashMap);

        HttpResponse execute = request.execute();
        System.out.println(execute.body());
    }
    /**
     * 分一个10m块模拟上传
     * @since 8/1/22
     */
    public static void mergeTest(){
        HttpRequest request = HttpUtil.createPost("http://localhost:8999/file/merge");
        request.header(TokenUtil.HeaderKey, token);

        request.form("fileName", fileName);
        request.form("targetPath", "");
        request.form("timestamp", timestamp);

        HttpResponse response = request.execute();
        System.out.println(response.body());
    }

    /**
     * 分一个10m块模拟上传
     * @since 8/1/22
     */
    public static void step1(){
        File file = new File(filePath);

        byte[] bytes = IoUtil.readBytes(FileUtil.getInputStream(file));

        byte[] copy = Arrays.copyOf(bytes, bytesSize);

        HttpRequest request = HttpUtil.createPost("http://localhost:8999/file/upload/block");
        request.header(TokenUtil.HeaderKey, token);

        request.form("file",copy,file.getName());
        request.form("blockNumber", 0);
        request.form("fileName", file.getName());

        HttpResponse response = request.execute();
        System.out.println(response.body());
    }

    /**
     * 测试断点续传步骤1
     * @since 8/1/22
     */
    public static void step2() throws IOException {
        File file = new File(filePath);
        byte[] bytes = IoUtil.readBytes(FileUtil.getInputStream(file));
        //小于 10m 直接上传即可
        ArrayList<byte[]> list = new ArrayList<>();
        if (bytes.length <= bytesSize) {
            list.add(bytes);
        }else {
           int byteLength = 0;
           while (byteLength < bytes.length){
               //总长度-以提取的长度 < 分块大小
               int i = bytes.length - byteLength;
               byte[] copy;
               if (i < bytesSize) {
                   copy = Arrays.copyOfRange(bytes, byteLength, byteLength + i);
               } else{
                   copy = Arrays.copyOfRange(bytes, byteLength, byteLength + bytesSize);
               }
               list.add(copy);
               byteLength+=bytesSize;
           }
        }
        //从1开始，因为0块已经上传了
        for (int i = 1; i < list.size(); i++) {
            HttpRequest request = HttpUtil.createPost("http://localhost:8999/file/upload/block");
            request.header(TokenUtil.HeaderKey, token);
            request.form("file",list.get(i),file.getName());
            request.form("blockNumber", i);
            request.form("fileName", file.getName());
            HttpResponse response = request.execute();
            System.out.println(response.body());
        }
    }
}
