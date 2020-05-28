package com.ye.modules.cus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ye.modules.cus.dao.GonggaoDao;
import com.ye.modules.cus.entity.GonggaoEntity;
import com.ye.modules.cus.service.GonggaoService;



@Service("gonggaoService")
public class GonggaoServiceImpl implements GonggaoService {
	@Autowired
	private GonggaoDao gonggaoDao;
	
	@Override
	public GonggaoEntity queryObject(Integer id){
		return gonggaoDao.queryObject(id);
	}
	
	@Override
	public List<GonggaoEntity> queryList(Map<String, Object> map){
		return gonggaoDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return gonggaoDao.queryTotal(map);
	}
	
	@Override
	public void save(GonggaoEntity gonggao){
		gonggaoDao.save(gonggao);
	}
	
	@Override
	public void update(GonggaoEntity gonggao){
		gonggaoDao.update(gonggao);
	}
	
	@Override
	public void delete(Integer id){
		gonggaoDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		gonggaoDao.deleteBatch(ids);
	}
	
}
