package com.zkyt.controller;

import com.zkyt.dto.CheckDto;
import com.zkyt.service.FileService;
import com.zkyt.service.UserService;
import com.zkyt.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author ws
 * @since 7/11/22
 */
@RestController
@RequestMapping(path = "file")
@Slf4j
public class FileController {

    @Value("${file.path}")
    private String customFilePath;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    /**
     * @param file 文件流上传
     * @param path 保存到本地的目录路径
     * @return
     */
    @GetMapping(path = "singleFile")
    public R<Map> upload(MultipartFile file, String path) {
        Map map = fileService.upload(file,path);
        return R.okData(map);
    }

    /**
     * 删除文件或者目录
     * @param json
     * @return
     */
    @DeleteMapping(path = "delete")
    public R<String> deleteFile(@RequestBody String json) {
        String msg = fileService.deleteFile(json);
        return R.okMsg(msg);
    }

    /**
     * 查询目录
     * @param path
     * @return
     */
    @GetMapping(path = "list")
    public R<List> getFileType(String path) {
        List list =  fileService.getFileType(path);
        return R.okData(list);
    }

    /**
     * 获取目录、文件的大小
     * @param path  路径，当不传递，默认访问data/shantou-upload-file/的
     * @param fileName
     * @return
     */
    @GetMapping(path = "/checkSize")
    public R<Map> checkSize(@RequestParam("path") String path, @RequestParam("fileName") String fileName) {
        Map map = fileService.checkSize(path,fileName);
        return R.okData(map);
    }

    /**
     * 下载文件 - 允许所有人下载
     * @since 2022/7/29
     */
    @PostMapping(path = "download")
    public void download(@RequestBody String path, HttpServletResponse response) throws IOException {
        fileService.download(path,response);
    }

    /**
     * @param blockNumber 上传编号,编号必须从0开始，且只能有序的数字例如：0，1，2，3
     * @param fileName    文件名称
     * @since 8/2/22
     */
    @PostMapping(path = "upload/block")
    public R<Map> uploadBlock(@RequestParam MultipartFile file,
                              @RequestParam String blockNumber,
                              @RequestParam String fileName) {
        Map map = fileService.uploadBlock(file,blockNumber,fileName);
        return R.okData(map);
    }

    /**
     * 文件检查 status 1已存在，2续传(返回已上传的块数)，3不存在
     * @since 7/11/22
     */
    @GetMapping(path = "check")
    public R check(CheckDto checkDto) {
        R r = fileService.check(checkDto);
        return r;
    }

    /**
     * @param fileName   文件名
     * @param targetPath 合并并过去的目录
     * @since 8/2/22
     */
    @PostMapping(path = "merge")
//    public Object merge(String fileName, String targetPath, String timestamp) {
    public Object merge(@RequestParam("fileName") String fileName,
                        @RequestParam("targetPath") String targetPath) {
        R r = fileService.merge(fileName,targetPath);
        return r;
    }

    /**
     * 根据用户的属性设置，例如A则 = customFilePath + 用户属性
     * @since 7/12/22
     */
    private String getFilePath() {
        return customFilePath + userService.getPath(); //多用户环境下
    }

    /**
     * 查询目录
     * @param path 后面目录
     * @since 7/11/22
     */
    /*
    @GetMapping(path = "listAll")
    public Object listFile(String path){
        String p = customFilePath; //这里需要获取所有用户的路径所以需要这样写
        if (StrUtil.isNotBlank(path)) {
            p += path;
        }
        return R.okData(com.zkyt.util.FileUtil.getAllFileName(p));
    }
*/

    /**
     * 获取指定类型的文件
     * @param path value
     * @since 8/2/22
     */
/*
@GetMapping(path = "list")
    public Object getFileType(String path, String value) {
        String p = customFilePath;
        if (StrUtil.isNotBlank(path)) {
            p += path;
        }
        List data = new ArrayList();
        data = getData(p, data, value);
        for (int i = 0; i < data.size(); i++) {
            System.out.println(data.get(i));
        }
        return R.okData(data);
    }

    private static List getData(String path, List data, String value) {
        if (StringUtils.isBlank(value)) {
            //获取目录所有文件名-不包含子目录,目录也会显示
            return com.zkyt.util.FileUtil.getAllFileName(path);
        }
        File f = new File(path);
        //路径名的文件是否是一个目录
        if (f.isDirectory()) {
            File[] fs = f.listFiles();
            for (int i = 0; i < fs.length; i++) {
                data = getData(fs[i].getPath(), data, value);
            }
        } else if (f.getName().endsWith(value)) {
            data.add(f.getName());
        }
        return data;
    }
*/

}
