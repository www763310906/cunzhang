package com.ye.modules.cus.controller;

import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;

import com.ye.modules.cus.dao.ViewDao;
import com.ye.modules.cus.entity.VideoEntity;
import com.ye.modules.cus.entity.ViewEntity;
import com.ye.modules.cus.service.VideoService;
import com.ye.modules.cus.service.ViewService;
import com.ye.common.utils.Query;
import com.ye.common.utils.R;
import com.ye.common.validator.ValidatorUtils;
import com.ye.common.validator.group.AddGroup;
import com.ye.common.validator.group.UpdateGroup;

/**
 * 视频
 * 
 */
@Controller
@RequestMapping("/cus/video")
public class VideoController {
	@Autowired
	private VideoService videoService;
	@Autowired
	private ViewService viewService;
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<VideoEntity> videoList = videoService.queryList(query);
		int total = videoService.queryTotal(query);		
		return R.ok().put("total", total).put("rows", videoList);
		//return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@ResponseBody
	public R info(@PathVariable("id") Integer id){
		VideoEntity video = videoService.queryObject(id);
		
		return R.ok().put("video", video);
	}
	/**
	 * 信息
	 */
	@RequestMapping("/videoinfo")
	@ResponseBody
	public R videoInfo(@RequestParam("id") Integer id,@RequestParam("userId") Integer userId){
		VideoEntity video = videoService.queryObject(id);
		ViewEntity view=new ViewEntity();
		view.setCreateTime(new Date());
		view.setGoodsId(id);
		view.setUserId(userId);
		viewService.save(view);
		return R.ok().put("data", video);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@ResponseBody
	public R save(@RequestBody VideoEntity video){
			  		  		  		  		  		  		  		 video.setCreateTime(new Date());
	 	  	ValidatorUtils.validateEntity(video, AddGroup.class);
		videoService.save(video);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@ResponseBody
	public R update(@RequestBody VideoEntity video){
	ValidatorUtils.validateEntity(video, UpdateGroup.class);
		videoService.update(video);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public R delete(@RequestBody Integer[] ids){
		videoService.deleteBatch(ids);
		
		return R.ok();
	}
	@RequestMapping("/videoadd.html")
	public String videoadd(HttpServletRequest req) {

		return "modules/cus/videoadd";
	}

	@RequestMapping("/videoedit.html")
	public String videoedit(@RequestParam("id") Integer id,
			HttpServletRequest req) {
		VideoEntity video = videoService.queryObject(id);
		
		req.setAttribute("video", video);
		return "modules/cus/videoedit";
	}
}
