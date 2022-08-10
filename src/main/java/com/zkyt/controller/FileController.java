package com.zkyt.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zkyt.dto.CheckDto;
import com.zkyt.exception.ServiceException;
import com.zkyt.service.UserService;
import com.zkyt.util.R;
import com.zkyt.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author lc
 * @since 7/11/22
 */
@RestController
@RequestMapping("file")
@Slf4j
public class FileController {


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

    public static void main(String[] args) {
        System.out.println(SecureUtil.md5());
    }

    /**
     * 下载文件 - 允许所有人下载
     *
     * @since 2022/7/29
     */
    @GetMapping("download")
    public void download(String path, HttpServletResponse response) throws IOException {
        File file = new File(customFilePath + path);
        if (!file.isFile()) {
            throw new ServiceException("路径不是文件！");
        }
        response.setCharacterEncoding("utf-8");   //设置处理编码
        //设置接收的长度,单位字节
        ServletOutputStream outputStream = response.getOutputStream();
        //URLEncoder.encode("CentOS7_min.rar","utf-8")把指定的字符串 转换为指定编码的String并返回
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getName(), "utf-8"));

        int read;  //读取的字节数
        byte[] bytes = new byte[bytesSize];
        BufferedInputStream inputStream = FileUtil.getInputStream(file);
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
    @GetMapping("check")
    public Object check(@Valid CheckDto checkDto) {
        R<Object> r = R.okData(null);
        String fileName = checkDto.getFileName();

        //用户存储路径
        File file = FileUtil.file(getFilePath() + checkDto.getPath() + fileName);
        //分块路径
        File fileFolder = FileUtil.file(customFilePath + SecureUtil.md5(fileName));
        long time = checkDto.getFileUpdateTime();

        log.info("文件上传修改时间: {},传过来时间: {}", file.lastModified(), time);

        //默认文件不存在
        HashMap<String, Object> map = new HashMap<>();
        int status = 3;
        r.setMsg("文件不存在！");

        if (file.isFile() && file.length() == checkDto.getByteSize() && file.lastModified() == time) {
            status = 1;
            r.setMsg("文件已存在！");
        }

        if (fileFolder.isDirectory()) {
            status = 2;
            map.put("skipBlockTotal", com.zkyt.util.FileUtil.getSkipBlock(fileFolder));
            r.setMsg("文件已分块需继续上传！");
        }
        map.put("status", status);
        r.setData(map);
        return r;
    }

    /**
     * 获取所有文件
     * @param path 后面目录
     * @since 7/11/22
     */
    @GetMapping("listAll")
    public Object listFile(String path){
        String p = customFilePath; //这里需要获取所有用户的路径所以需要这样写
        if (StrUtil.isNotBlank(path)) {
            p += path;
        }
        return R.okData(com.zkyt.util.FileUtil.getAllFileName(p));
    }

    /**
     * 获取指定类型的文件
     * @param path value
     * @since 8/2/22
     */
    @GetMapping("list")
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
            return com.zkyt.util.FileUtil.getAllFileName(path);
        }
        File f = new File(path);
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

    /**
     * @param blockNumber 上传编号,编号必须从0开始，且只能有序的数字例如：0，1，2，3
     * @param fileName    文件名称
     * @since 8/2/22
     */
    @PostMapping("upload/block")
    public Object uploadBlock(MultipartFile file, String blockNumber, String fileName) {
        String filePath = customFilePath;
        String mkdirPath = filePath + SecureUtil.md5(fileName);
        //创建目录
        FileUtil.mkdir(mkdirPath);
        mkdirPath += File.separator + blockNumber;
        System.out.println(mkdirPath);
        File f = new File(mkdirPath);
        try {
            file.transferTo(f);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("上传文件失败！");
        }
        return R.okMsg(blockNumber + "块上传成功！");
    }

    /**
     * @param fileName   文件名
     * @param targetPath 合并并过去的目录
     * @param timestamp  时间戳用于校验文件时间
     * @since 8/2/22
     */
    @PostMapping("merge")
    public Object merge(String fileName, String targetPath, String timestamp) {
        String filePath = getFilePath() + targetPath + fileName;
        long time = Long.parseLong(timestamp);
        File f = new File(filePath);
        f.delete(); //追加模式则需要先删除文件然后在写入
        //异步合并
        ThreadUtil.EXECUTOR.execute(() -> {
            log.info("合并文件");
            com.zkyt.util.FileUtil.merge(f, customFilePath + SecureUtil.md5(fileName));
            f.setLastModified(time);
        });
        return R.okMsg("异步合并中！");
    }
}
