package com.ye.modules.cus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ye.modules.cus.dao.UserRelationDao;
import com.ye.modules.cus.entity.UserEntity;
import com.ye.modules.cus.entity.UserRelationEntity;
import com.ye.modules.cus.service.UserRelationService;



@Service("userRelationService")
public class UserRelationServiceImpl implements UserRelationService {
	@Autowired
	private UserRelationDao userRelationDao;
	
	@Override
	public UserRelationEntity queryObject(Integer id){
		return userRelationDao.queryObject(id);
	}
	
	@Override
	public List<UserRelationEntity> queryList(Map<String, Object> map){
		return userRelationDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return userRelationDao.queryTotal(map);
	}
	
	@Override
	public void save(UserRelationEntity userRelation){
		userRelationDao.save(userRelation);
	}
	
	@Override
	public void update(UserRelationEntity userRelation){
		userRelationDao.update(userRelation);
	}
	
	@Override
	public void delete(Integer id){
		userRelationDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		userRelationDao.deleteBatch(ids);
	}

	@Override
	public List<UserEntity> queryXueList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
