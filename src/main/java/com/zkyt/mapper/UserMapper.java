package com.zkyt.mapper;

import com.zkyt.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author licheng
*/
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

}




