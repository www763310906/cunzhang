package com.ye.modules.cus.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericItemPreferenceArray;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ye.modules.cus.dao.VideoDao;
import com.ye.modules.cus.dao.ViewDao;
import com.ye.modules.cus.entity.VideoEntity;
import com.ye.modules.cus.entity.ViewEntity;
import com.ye.modules.cus.service.VideoService;



@Service("videoService")
public class VideoServiceImpl implements VideoService {
	@Autowired
	private VideoDao videoDao;
	@Autowired
	private ViewDao viewDao;
	
	@Override
	public VideoEntity queryObject(Integer id){
		return videoDao.queryObject(id);
	}
	
	@Override
	public List<VideoEntity> queryList(Map<String, Object> map){
		return videoDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return videoDao.queryTotal(map);
	}
	
	@Override
	public void save(VideoEntity video){
		videoDao.save(video);
	}
	
	@Override
	public void update(VideoEntity video){
		videoDao.update(video);
	}
	
	@Override
	public void delete(Integer id){
		videoDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		videoDao.deleteBatch(ids);
	}
	@Override
	public List<VideoEntity> recommendList(Integer userId){
		
		Map<String, Object> map=new HashMap<String, Object>();
		  List<VideoEntity> goods=new ArrayList<VideoEntity>();
		 
		try{
		 	map.clear();
		      map.put("sidx", "rand()");
		    
		      goods=	 videoDao  .queryList(map);
			map.put("userId", userId);
		List<ViewEntity> list=viewDao.queryList(map); 
		int length=list.size();
		FastByIDMap<PreferenceArray> itemData=new FastByIDMap<PreferenceArray>();
		for (int i = 0; i < length; i++) {
			GenericItemPreferenceArray array=new GenericItemPreferenceArray(length);
			array.setItemID(i, list.get(i).getGoodsId());
//			array.setUserID(i, user);
			array.setValue(i,   RandomUtils.nextFloat());
			
			itemData.put(i, array);
		}
		 
	 
		DataModel dataItemModel=new GenericDataModel(itemData);
		
		 
      ItemSimilarity itemsimilarity = new PearsonCorrelationSimilarity(dataItemModel);//计算内容相似度  
      GenericItemBasedRecommender recommender1 = new GenericItemBasedRecommender(dataItemModel, itemsimilarity);//构造推荐引擎 
       
    
      List<RecommendedItem> recommendItems = recommender1.recommendedBecause(userId, list.get(RandomUtils.nextInt(0, length)).getGoodsId().intValue(),  10);
 
     
      for (RecommendedItem recommendedItem : recommendItems) {
          System.out.println(recommendedItem);
          goods.add(videoDao.queryObject(recommendedItem.getItemID()));
      }
		}catch(Exception e){
			 
		
		}
		return goods;
	}

	@Override
	public List<VideoEntity> queryListSearch(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return videoDao.queryListSearch(map);
	}
}
