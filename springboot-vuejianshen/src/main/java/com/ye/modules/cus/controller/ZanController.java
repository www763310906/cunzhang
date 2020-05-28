package com.ye.modules.cus.controller;

import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;

import com.ye.modules.cus.entity.ZanEntity;
import com.ye.modules.cus.service.ZanService;
import com.ye.common.utils.Query;
import com.ye.common.utils.R;
import com.ye.common.validator.ValidatorUtils;
import com.ye.common.validator.group.AddGroup;
import com.ye.common.validator.group.UpdateGroup;

/**
 * 点赞
 * 
 */
@Controller
@RequestMapping("/cus/zan")
public class ZanController {
	@Autowired
	private ZanService zanService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<ZanEntity> zanList = zanService.queryList(query);
		int total = zanService.queryTotal(query);
		
		return R.ok().put("total", total).put("rows", zanList);
		//return R.ok().put("page", pageUtil);
	}
	/**
	 * 查看是否点赞
	 */
	@RequestMapping("/queryIsExist")
	@ResponseBody
	public R queryIsExist(@RequestBody ZanEntity zan){
		
        int i = zanService.queryIsExist(zan);
        boolean check = true;
        if(i != 0){
        	check = true;
        }else{
        	check = false;
        }
        		
		return R.ok().put("check",check);
	}	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@ResponseBody
	public R info(@PathVariable("id") Integer id){
		ZanEntity zan = zanService.queryObject(id);
		
		return R.ok().put("zan", zan);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@ResponseBody
	public R save(@RequestBody ZanEntity zan){
			  		  		  		 zan.setCreateTime(new Date());
	 	  	ValidatorUtils.validateEntity(zan, AddGroup.class);
		zanService.save(zan);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@ResponseBody
	public R update(@RequestBody ZanEntity zan){
	ValidatorUtils.validateEntity(zan, UpdateGroup.class);
		zanService.update(zan);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public R delete(@RequestBody Integer[] ids){
		zanService.deleteBatch(ids);
		
		return R.ok();
	}
	@RequestMapping("/delete/userId")
	@ResponseBody
	public R deleteByChoose(@RequestBody ZanEntity zan){
		zanService.deleteByChoose(zan);		
		return R.ok();
	}
	@RequestMapping("/zanadd.html")
	public String zanadd(HttpServletRequest req) {

		return "modules/cus/zanadd";
	}

	@RequestMapping("/zanedit.html")
	public String zanedit(@RequestParam("id") Integer id,
			HttpServletRequest req) {
		ZanEntity zan = zanService.queryObject(id);
		
		req.setAttribute("zan", zan);
		return "modules/cus/zanedit";
	}
}
