package com.lvf.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lvf.springboot.model.User;

public interface UserMapper {
	
    public List<User> findUserInfo();
    
    public User queryUserInfoById(@Param("id") String id);

    public List<User> queryUserInfoByIds(@Param("ids") String ids);

    public List<User> queryUserInfoByIdsForeach(@Param("list") List<String> list);

    public void insert(User user);

    public void insertList(@Param("list") List<User> userList);
    
    public int updateUser(User user);
    
}
