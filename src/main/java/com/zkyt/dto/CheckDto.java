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

   /**  判断分块文件名是否存在 **/
   @NotEmpty(message = "分块文件名为空")
   private String blockName;

 /*  *//** 路径 检查指定的路径上传文件，默认传空 "" 字符串 **//*
   @NotNull(message = "文件路径为null,可以传''空字符串")
   private String blockName;*/
}
