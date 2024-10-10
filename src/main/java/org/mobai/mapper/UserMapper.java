package org.mobai.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mobai.entity.User;

import java.util.List;

public interface UserMapper {
  @Select("select * from users")
  List<User> getAllUsers();

  @Select("select * from users where username = #{username} and password = #{password}")
  User userCheck(@Param("username") String username,@Param("password") String password);
}
