package com.ye.modules.cus.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ye.common.utils.Query;
import com.ye.common.utils.R;
import com.ye.common.validator.ValidatorUtils;
import com.ye.common.validator.group.AddGroup;
import com.ye.common.validator.group.UpdateGroup;
import com.ye.modules.cus.entity.UserEntity;
import com.ye.modules.cus.service.UserService;

@Controller
@RequestMapping("/cus/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<UserEntity> userList = userService.queryList(query);
		int total = userService.queryTotal(query);
		
	 	return R.ok().put("total", total).put("rows", userList);
		//return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@ResponseBody
	public R info(@PathVariable("id") Integer id){
		UserEntity user = userService.queryObject(id);
		return R.ok().put("user", user);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@ResponseBody
	public R save(@RequestBody UserEntity user){
		user.setCreateTime(new Date());
	 	ValidatorUtils.validateEntity(user, AddGroup.class);
		userService.save(user);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@ResponseBody
	public R update(@RequestBody UserEntity user){
	ValidatorUtils.validateEntity(user, UpdateGroup.class);
		userService.update(user);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public R delete(@RequestBody Integer[] ids){
		userService.deleteBatch(ids);
		
		return R.ok();
	}
	@RequestMapping("/useradd.html")
	public String useradd(HttpServletRequest req) {

		return "modules/cus/useradd";
	}
	@RequestMapping("/useradd1.html")
	public String useradd1(HttpServletRequest req) {

		return "modules/cus/useradd1";
	}
	@RequestMapping("/useredit.html")
	public String useredit(@RequestParam("id") Integer id,
			HttpServletRequest req) {
		UserEntity user = userService.queryObject(id);
		
		req.setAttribute("user", user);
		return "modules/cus/useredit";
	}
	@RequestMapping("/useredit1.html")
	public String useredit1(@RequestParam("id") Integer id,
			HttpServletRequest req) {
		UserEntity user = userService.queryObject(id);
		
		req.setAttribute("user", user);
		return "modules/cus/useredit1";
	}
}
