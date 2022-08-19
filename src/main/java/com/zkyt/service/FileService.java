package com.zkyt.service;

import com.zkyt.dto.CheckDto;
import com.zkyt.util.R;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ws
 * @since 7/11/22
 */
public interface FileService {
    /**
     * 下载文件 - 允许所有人下载
     * @since 2022/7/29
     */
    void download(String path, HttpServletResponse response) throws IOException;
    /**
     * 获取目录、文件的大小
     * @param path  路径，当不传递，默认访问data/shantou-upload-file/的
     * @param fileName
     * @return
     */
    Map checkSize(String path, String fileName);
    /**
     * 查询目录
     * @param path
     * @return
     */
    List getFileType(String path);
    /**
     * @param file 文件流上传
     * @param path 保存到本地的目录路径
     * @return
     */
    Map upload(MultipartFile file, String path);
    /**
     * 删除文件或者目录
     * @param json
     * @return
     */
    String deleteFile(String json);
    /**
     * @param blockNumber 上传编号,编号必须从0开始，且只能有序的数字例如：0，1，2，3
     * @param fileName    文件名称
     * @since 8/2/22
     */
    Map uploadBlock(MultipartFile file, String blockNumber, String fileName);
    /**
     * 文件检查 1已存在，2续传(返回已上传的块数)，3不存在
     * @since 7/11/22
     */
    R check(CheckDto checkDto);
    /**
     * @param fileName   文件名
     * @param targetPath 合并并过去的目录
     * @since 8/2/22
     */
    R merge(String fileName, String targetPath);
}
