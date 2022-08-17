package com.zkyt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("t_user")
public class User implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 路径前缀
     */
    private String path;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 逻辑删除0正常1删除
     */
    private Integer deleted;

}