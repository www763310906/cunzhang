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
import com.ye.modules.cus.entity.GonggaoEntity;
import com.ye.modules.cus.service.GonggaoService;

import com.ye.common.utils.Query;
import com.ye.common.utils.R;
import com.ye.common.validator.ValidatorUtils;
import com.ye.common.validator.group.AddGroup;
import com.ye.common.validator.group.UpdateGroup;

/**
 * 公告
 * 
 */
@Controller
@RequestMapping("/cus/gonggao")
public class GonggaoController {
	@Autowired
	private GonggaoService gonggaoService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<GonggaoEntity> gonggaoList = gonggaoService.queryList(query);
		int total = gonggaoService.queryTotal(query);
		
		return R.ok().put("total", total).put("rows", gonggaoList);
		//return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@ResponseBody
	public R info(@PathVariable("id") Integer id){
		GonggaoEntity gonggao = gonggaoService.queryObject(id);
		
		return R.ok().put("gonggao", gonggao);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@ResponseBody
	public R save(@RequestBody GonggaoEntity gonggao){
			  		  		  		  		 gonggao.setCreateTime(new Date());
	 	  		  	ValidatorUtils.validateEntity(gonggao, AddGroup.class);
		gonggaoService.save(gonggao);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@ResponseBody
	public R update(@RequestBody GonggaoEntity gonggao){
		
	ValidatorUtils.validateEntity(gonggao, UpdateGroup.class);
		gonggaoService.update(gonggao);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public R delete(@RequestBody Integer[] ids){
		gonggaoService.deleteBatch(ids);
		
		return R.ok();
	}
	@RequestMapping("/gonggaoadd.html")
	public String gonggaoadd(HttpServletRequest req) {

		return "modules/cus/gonggaoadd";
	}

	@RequestMapping("/gonggaoedit.html")
	public String gonggaoedit(@RequestParam("id") Integer id,
			HttpServletRequest req) {
		GonggaoEntity gonggao = gonggaoService.queryObject(id);
		
		req.setAttribute("gonggao", gonggao);
		return "modules/cus/gonggaoedit";
	}
}
