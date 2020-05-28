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
import com.ye.modules.cus.entity.HuiEntity;
import com.ye.modules.cus.service.HuiService;

import com.ye.common.utils.Query;
import com.ye.common.utils.R;
import com.ye.common.validator.ValidatorUtils;
import com.ye.common.validator.group.AddGroup;
import com.ye.common.validator.group.UpdateGroup;


@Controller
@RequestMapping("/cus/hui")
public class HuiController {
	@Autowired
	private HuiService huiService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<HuiEntity> huiList = huiService.queryList(query);
		int total = huiService.queryTotal(query);
		return R.ok().put("total", total).put("rows", huiList);
		
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@ResponseBody
	public R info(@PathVariable("id") Integer id){
		HuiEntity hui = huiService.queryObject(id);
		
		return R.ok().put("hui", hui);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@ResponseBody
	public R save(@RequestBody HuiEntity hui){
			  		  		  		  		 hui.setCreateTime(new Date());
	 	  	ValidatorUtils.validateEntity(hui, AddGroup.class);
		huiService.save(hui);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@ResponseBody
	public R update(@RequestBody HuiEntity hui){
	ValidatorUtils.validateEntity(hui, UpdateGroup.class);
		huiService.update(hui);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public R delete(@RequestBody Integer[] ids){
		huiService.deleteBatch(ids);
		
		return R.ok();
	}
	@RequestMapping("/huiadd.html")
	public String huiadd(HttpServletRequest req) {

		return "modules/cus/huiadd";
	}

	@RequestMapping("/huiedit.html")
	public String huiedit(@RequestParam("id") Integer id,
			HttpServletRequest req) {
		HuiEntity hui = huiService.queryObject(id);
		
		req.setAttribute("hui", hui);
		return "modules/cus/huiedit";
	}
}
