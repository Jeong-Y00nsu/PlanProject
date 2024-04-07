package com.jRyun.demo.planProject.user.mapper;

import com.jRyun.demo.planProject.user.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

    User selectUserById(@Param("id")String id);

    void insertUser(@Param("user")User user);

    void deleteUser(@Param("id")String id);

    void updateUser(@Param("user")User user);

    int countDuplicateId(@Param("id")String id);

}
