package com.zkyt.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author lc
 * @since 8/2/22
 */
@Data
public class CheckDto {

   /**  判断文件名是否存在 **/
   @NotEmpty(message = "文件名为空")
   private String fileName;

   /**  文件字节数 判断文件字节数 **/
   @NotNull(message = "文件字节大小为空")
   private Long byteSize;

   /**  修改时间戳 判断文件修改时间  注意:js或其他语言可能不支持长数字 **/
   @NotNull(message = "文件修改时间戳为空")
   private Long fileUpdateTime;

   /** 路径 检查指定的路径上传文件，默认传空 "" 字符串 **/
   @NotNull(message = "文件路径为null,可以传''空字符串")
   private String path;
}
