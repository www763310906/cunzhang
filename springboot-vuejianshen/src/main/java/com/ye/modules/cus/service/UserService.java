package com.ye.modules.cus.service;

import com.ye.common.utils.R;
import com.ye.modules.cus.entity.UserEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
	
	UserEntity queryObject(Integer id);
	
	List<UserEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(UserEntity user);
	
	void update(UserEntity user);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	R saveUser(UserEntity u);
	UserEntity login(String username,String password);
	UserEntity login(String username,String password,String type);
}
