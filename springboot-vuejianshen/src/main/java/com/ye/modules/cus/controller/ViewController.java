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
import com.ye.modules.cus.entity.ViewEntity;
import com.ye.modules.cus.service.ViewService;

import com.ye.common.utils.Query;
import com.ye.common.utils.R;
import com.ye.common.validator.ValidatorUtils;
import com.ye.common.validator.group.AddGroup;
import com.ye.common.validator.group.UpdateGroup;

@Controller
@RequestMapping("/cus/view")
public class ViewController {
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

		List<ViewEntity> viewList = viewService.queryList(query);
		int total = viewService.queryTotal(query);
		
		return R.ok().put("total", total).put("rows", viewList);
		//return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@ResponseBody
	public R info(@PathVariable("id") Integer id){
		ViewEntity view = viewService.queryObject(id);
		
		return R.ok().put("view", view);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@ResponseBody
	public R save(@RequestBody ViewEntity view){
			  		  		  		 view.setCreateTime(new Date());
	 	  	ValidatorUtils.validateEntity(view, AddGroup.class);
		viewService.save(view);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@ResponseBody
	public R update(@RequestBody ViewEntity view){
	ValidatorUtils.validateEntity(view, UpdateGroup.class);
		viewService.update(view);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public R delete(@RequestBody Integer[] ids){
		viewService.deleteBatch(ids);
		
		return R.ok();
	}
	@RequestMapping("/viewadd.html")
	public String viewadd(HttpServletRequest req) {

		return "modules/cus/viewadd";
	}

	@RequestMapping("/viewedit.html")
	public String viewedit(@RequestParam("id") Integer id,
			HttpServletRequest req) {
		ViewEntity view = viewService.queryObject(id);
		
		req.setAttribute("view", view);
		return "modules/cus/viewedit";
	}
}
