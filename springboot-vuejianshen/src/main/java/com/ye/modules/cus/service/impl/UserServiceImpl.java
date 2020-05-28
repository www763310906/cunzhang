package com.ye.modules.cus.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ye.common.utils.R;
import com.ye.modules.cus.dao.UserDao;
import com.ye.modules.cus.entity.UserEntity;
import com.ye.modules.cus.service.UserService;



@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	
	@Override
	public UserEntity queryObject(Integer id){
		return userDao.queryObject(id);
	}
	
	@Override
	public List<UserEntity> queryList(Map<String, Object> map){
		return userDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return userDao.queryTotal(map);
	}
	
	@Override
	public void save(UserEntity user){
		userDao.save(user);
	}
	
	@Override
	public void update(UserEntity user){
		userDao.update(user);
	}
	
	@Override
	public void delete(Integer id){
		userDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		userDao.deleteBatch(ids);
	}

	@Override
	public UserEntity login(String username, String password) {
	 
		return userDao.login(username, password);
	}
	@Override
	public UserEntity login(String username, String password,String type) {
	 
		return userDao.loginByType(username, password,type);
	}

	@Override
	public R saveUser(UserEntity u) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("userType", u.getUserType());
		map.put("username", u.getUsername());
		List<UserEntity> list= userDao.queryList(map);
		if(list.size()>0) return R.error("已经注册过了");
		 userDao.save(u);
		 return R.ok();
		
	}
}
