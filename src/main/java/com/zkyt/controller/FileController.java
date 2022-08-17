package com.zkyt.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zkyt.dto.CheckDto;
import com.zkyt.exception.ServiceException;
import com.zkyt.service.UserService;
import com.zkyt.util.R;
import com.zkyt.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author lc
 * @since 7/11/22
 */
@RestController
@RequestMapping(path = "file")
@Slf4j
public class FileController {

    public static final String utf8 = "utf-8";

    @Value("${file.path}")
    private String customFilePath;

    @Autowired
    private UserService userService;

    private static final int bytesSize = 1024 * 1024 * 10;

    /**
     * 根据用户的属性设置，例如A则 = customFilePath + "a/"
     *
     * @since 7/12/22
     */
    private String getFilePath() {
        return customFilePath + userService.getPath(); //多用户环境下
    }

    /**
     * 下载文件 - 允许所有人下载
     *
     * @since 2022/7/29
     */
    @PostMapping(path = "download")
    public void download(@RequestBody String path, HttpServletResponse response) throws IOException {
        Map map = JSON.parseObject(path, Map.class);
        path = (String) map.get("path");
        String s = customFilePath;
        if (null != path && "" != path) {
            s = customFilePath + path;
        }
        String fileName = (String) map.get("fileName");
        File file = new File(s, fileName);

        //判断是否为文件
        if (!file.isFile()) {
            throw new ServiceException("路径不是文件！");
        }
        response.setCharacterEncoding(utf8);   //设置处理编码
        //设置接收的长度,单位字节
        ServletOutputStream outputStream = response.getOutputStream();
        //URLEncoder.encode("CentOS7_min.rar",utf)把指定的字符串 转换为指定编码的String并返回
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getName(), "utf-8"));

        int read;  //读取的字节数
        byte[] bytes = new byte[bytesSize];
        BufferedInputStream inputStream = FileUtil.getInputStream(file);
        //写入数据
        while ((read = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }
        outputStream.flush();
        outputStream.close();
    }


    /**
     * 文件检查 1已存在，2续传(返回已上传的块数)，3不存在
     *
     * @since 7/11/22
     */
    @GetMapping(path = "check")
    public Object check(CheckDto checkDto) {
        R<Object> r = R.okData(null);
        String fileName = checkDto.getFileName();

        //用户存储路径
        //String userFilePath = getFilePath() + checkDto.getPath() + fileName;
        String userFilePath = customFilePath + checkDto.getPath() + fileName;
        File file = FileUtil.file(userFilePath);

        //分块路径
        String blockFilePath = customFilePath + SecureUtil.md5(fileName);
        File fileFolder = FileUtil.file(blockFilePath);
        long time = checkDto.getFileUpdateTime();

        log.info("文件上传修改时间: {},传过来时间: {}", file.lastModified(), time);

        //默认文件不存在
        HashMap<String, Object> map = new HashMap<>();
        int status = 3;
        r.setMsg("文件不存在！");

        //if (file.isFile() && file.length() == checkDto.getByteSize() && file.lastModified() == time) {
        if (file.isFile()) {
            status = 1;
            r.setMsg("文件已存在！");
            map.put("size", file.length());
        }

        if (fileFolder.isDirectory()) {
            status = 2;
            map.put("skipBlockTotal", com.zkyt.util.FileUtil.getSkipBlock(fileFolder));
            r.setMsg("文件已分块需继续上传！");
        }
        map.put("status", status);
        //用户存储路径
        map.put("userFilePath", customFilePath + checkDto.getPath());
    /*    //分块路径
        map.put("blockFilePath", blockFilePath);*/
        r.setData(map);
        return r;
    }

    /**
     * 获取目录、文件的大小
     *
     * @param path
     * @return
     */
    @GetMapping(path = "/checkSize")
    public Object checkSize(@RequestParam("path") String path, @RequestParam("fileName") String fileName) {
        Long size = null;
        Map map = new HashMap();
        File file = new File(customFilePath + path);
        //判断传递的文件名是否存在存在，则是进行文件的判断
        if (!"".equals(fileName)) {
            file = new File(customFilePath + path, fileName);
        }
        size = getSize(file);
        map.put("size", size);
        return R.okData(map);
    }

    public long getSize(File file) {
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (final File child : children)
                total += getSize(child);
        return total;
    }


    /**
     * 获取所有文件
     * // * @param path 后面目录
     *
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

    private static List arrayList = new ArrayList();

    public static List isDirectory(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                System.out.println("file is ==>>" + file.getAbsolutePath());
                arrayList.add(file.getAbsolutePath());
            } else {
                File[] list = file.listFiles();
                if (list.length == 0) {
                    System.out.println(file.getAbsolutePath() + " is null");
                    arrayList.add(file.getAbsolutePath());
                } else {
                    for (int i = 0; i < list.length; i++) {
                        isDirectory(list[i]);//递归调用
                    }
                }
            }
        } else {
            System.out.println("文件不存在！");
        }
        return arrayList;
    }

    @GetMapping(path = "list")
    public Object getFileType(String path) {
        File file = new File(customFilePath + path); //以某路径实例化一个File对象
        if (!StringUtils.isBlank(path)) {
            if (!file.exists()) { //如果不存在
                file.mkdirs(); //创建目录
            }
        }
        List list = isDirectory(file);
        arrayList = new ArrayList();
        return R.okData(list);
    }
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

    /**
     * @param blockNumber 上传编号,编号必须从0开始，且只能有序的数字例如：0，1，2，3
     * @param fileName    文件名称
     * @since 8/2/22
     */
    @PostMapping(path = "upload/block")
    public Object uploadBlock(@RequestParam("file") MultipartFile file,
                              @RequestParam("blockNumber") String blockNumber,
                              @RequestParam("fileName") String fileName) {
        //获取到文件的配置路径
        String filePath = getFilePath();
        //为文件进行随机取名
        String mkdirPath = filePath + SecureUtil.md5(fileName);
        //创建目录
        FileUtil.mkdir(mkdirPath);
        //拼接文件路径
        mkdirPath += File.separator + blockNumber;
        log.info(mkdirPath);
        //文件进行上传到仓库
        File f = new File(mkdirPath);
        try {
            file.transferTo(f);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("上传文件失败！");
        }
        return R.okMsg(blockNumber + "块上传成功");
    }

    /**
     * @param fileName   文件名
     * @param targetPath 合并并过去的目录
     * @param timestamp  时间戳用于校验文件时间
     * @since 8/2/22
     */
    @PostMapping(path = "merge")
    public Object merge(String fileName, String targetPath, String timestamp) {
        String filePath = getFilePath() + targetPath + fileName;
        long time = Long.parseLong(timestamp);
        File file = new File(filePath);
        file.delete(); //追加模式则需要先删除文件然后在写入
        //异步合并
        ThreadUtil.EXECUTOR.execute(() -> {
            log.info("合并文件");
            //合并代码
            com.zkyt.util.FileUtil.merge(file, customFilePath + SecureUtil.md5(fileName));
            file.setLastModified(time);
        });
        return R.okMsg("异步合并中！");
    }

    /**
     * @param file 文件流上传
     * @param path 保存到本地的目录路径
     * @return
     */
    @GetMapping(path = "singleFile")
    public R upload(MultipartFile file, String path) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        Map map = new HashMap();
        try {
            String fileName = file.getOriginalFilename();
            inputStream = file.getInputStream();
            // 数据缓冲
            byte[] buffer = new byte[bytesSize];
            //读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
            String filePath = getFilePath();
            System.out.println(filePath);
            path = filePath + path;
            File tempFile = new File(path);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            fileOutputStream = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
            // 开始读取
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            map.put("absolutePath", path + "/" + fileName);
            long size = file.getSize();
            map.put("size", size);
            return R.okData(map);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            try {
                fileOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return R.okData(map);
    }

    @DeleteMapping(path = "delete")
    public R DeleteFile(@RequestBody String json) {
        Map map = JSON.parseObject(json, Map.class);
        String fileName = (String) map.get("fileName");
        String path = (String) map.get("path");
        String s = customFilePath;
        if (!StringUtils.isEmpty(path)) {
            s = customFilePath + path + "/";
        }
        if (!StringUtils.isEmpty(fileName)) {
            s += fileName;
        }
        File file = new File(s);
        if (file.isDirectory()) {
            deleteFile(file);
            return R.okMsg("删除文件夹" + file.getAbsolutePath() + "成功");
        }
        if (file.exists()) {
            file.delete();//存在则删除
            return R.okMsg("删除文件" + file.getName() + "成功");
        }
        return R.okMsg("删除失败");
    }

    public static void deleteFile(File file) {
        if (file.exists()) {//判断路径是否存在
            if (file.isFile()) {//boolean isFile():测试此抽象路径名表示的文件是否是一个标准文件。
                file.delete();
            } else {//不是文件，对于文件夹的操作
                //保存 路径D:/1/新建文件夹2  下的所有的文件和文件夹到listFiles数组中
                File[] listFiles = file.listFiles();//listFiles方法：返回file路径下所有文件和文件夹的绝对路径
                for (File file2 : listFiles) {
                    /*
                     * 递归作用：由外到内先一层一层删除里面的文件 再从最内层 反过来删除文件夹
                     *    注意：此时的文件夹在上一步的操作之后，里面的文件内容已全部删除
                     *         所以每一层的文件夹都是空的  ==》最后就可以直接删除了
                     */
                    deleteFile(file2);
                }
            }
            file.delete();
        } else {
            System.out.println("该file路径不存在！！");
        }
    }
}
