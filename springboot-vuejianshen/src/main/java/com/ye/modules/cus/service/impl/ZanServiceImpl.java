package com.ye.modules.cus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ye.modules.cus.dao.ZanDao;
import com.ye.modules.cus.entity.ZanEntity;
import com.ye.modules.cus.service.ZanService;



@Service("zanService")
public class ZanServiceImpl implements ZanService {
	@Autowired
	private ZanDao zanDao;
	
	@Override
	public ZanEntity queryObject(Integer id){
		return zanDao.queryObject(id);
	}
	
	@Override
	public List<ZanEntity> queryList(Map<String, Object> map){
		return zanDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return zanDao.queryTotal(map);
	}
	
	@Override
	public void save(ZanEntity zan){
		zanDao.save(zan);
	}
	
	@Override
	public void update(ZanEntity zan){
		zanDao.update(zan);
	}
	
	@Override
	public void delete(Integer id){
		zanDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		zanDao.deleteBatch(ids);
	}

	@Override
	public List<ZanEntity> queryListByBokeId(Integer bokeId) {
		// TODO Auto-generated method stub
		return zanDao.queryListByBokeId(bokeId);
	}

	@Override
	public void deleteByChoose(ZanEntity zan) {
		// TODO Auto-generated method stub
		zanDao.deleteByChoose(zan);
	}

	@Override
	public int queryIsExist(ZanEntity zan) {
		// TODO Auto-generated method stub
		return zanDao.queryIsExist(zan);
	}
	
}
