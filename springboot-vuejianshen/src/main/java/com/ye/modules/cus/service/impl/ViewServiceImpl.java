package com.ye.modules.cus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ye.modules.cus.dao.ViewDao;
import com.ye.modules.cus.entity.ViewEntity;
import com.ye.modules.cus.service.ViewService;



@Service("viewService")
public class ViewServiceImpl implements ViewService {
	@Autowired
	private ViewDao viewDao;
	
	@Override
	public ViewEntity queryObject(Integer id){
		return viewDao.queryObject(id);
	}
	
	@Override
	public List<ViewEntity> queryList(Map<String, Object> map){
		return viewDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return viewDao.queryTotal(map);
	}
	
	@Override
	public void save(ViewEntity view){
		viewDao.save(view);
	}
	
	@Override
	public void update(ViewEntity view){
		viewDao.update(view);
	}
	
	@Override
	public void delete(Integer id){
		viewDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		viewDao.deleteBatch(ids);
	}
	
}
