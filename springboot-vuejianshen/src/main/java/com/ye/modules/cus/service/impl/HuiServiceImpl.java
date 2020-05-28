package com.ye.modules.cus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ye.modules.cus.dao.HuiDao;
import com.ye.modules.cus.entity.HuiEntity;
import com.ye.modules.cus.service.HuiService;



@Service("huiService")
public class HuiServiceImpl implements HuiService {
	@Autowired
	private HuiDao huiDao;
	
	@Override
	public HuiEntity queryObject(Integer id){
		return huiDao.queryObject(id);
	}
	
	@Override
	public List<HuiEntity> queryList(Map<String, Object> map){
		return huiDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return huiDao.queryTotal(map);
	}
	
	@Override
	public void save(HuiEntity hui){
		huiDao.save(hui);
	}
	
	@Override
	public void update(HuiEntity hui){
		huiDao.update(hui);
	}
	
	@Override
	public void delete(Integer id){
		huiDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		huiDao.deleteBatch(ids);
	}

	@Override
	public List<HuiEntity> queryListByUser(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return huiDao.queryListByUser(map);
	}
	
}
