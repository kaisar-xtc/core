package com.dksys.core.mapper;

import com.dksys.core.entity.SysLogin;
import com.dksys.core.entity.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @version 1.0.0
 * @Author Wang
 * @createTime 2021-08-06 13:46:00
 */
@Repository
public interface UserMapper {
    @Select("select * from user where username = #{username}")
    User queryUserByusername(String username);
}
