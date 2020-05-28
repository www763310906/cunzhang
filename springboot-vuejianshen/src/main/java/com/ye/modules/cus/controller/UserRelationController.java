package com.ye.modules.cus.controller;

import java.util.HashMap;
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

import com.ye.modules.cus.entity.UserRelationEntity; 
import com.ye.modules.cus.service.UserRelationService;
import com.ye.modules.cus.service.UserService;
import com.ye.common.utils.Query;
import com.ye.common.utils.R;
import com.ye.common.validator.ValidatorUtils;
import com.ye.common.validator.group.AddGroup;
import com.ye.common.validator.group.UpdateGroup;

/**
 * 教练学员关系表
 * 
 */
@Controller
@RequestMapping("/cus/userrelation")
public class UserRelationController {
	@Autowired
	private UserRelationService userRelationService;
	@Autowired
	private UserService userService;
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);

		List<UserRelationEntity> userRelationList = userRelationService
				.queryList(query);
		int total = userRelationService.queryTotal(query);

		return R.ok().put("total", total).put("rows", userRelationList);
		// return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@ResponseBody
	public R info(@PathVariable("id") Integer id) {
		UserRelationEntity userRelation = userRelationService.queryObject(id);

		return R.ok().put("userRelation", userRelation);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@ResponseBody
	public R save(@RequestBody UserRelationEntity userRelation) {
		userRelation.setCreateTime(new Date());
		ValidatorUtils.validateEntity(userRelation, AddGroup.class);
		userRelationService.save(userRelation);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@ResponseBody
	public R update(@RequestBody UserRelationEntity userRelation) {
		ValidatorUtils.validateEntity(userRelation, UpdateGroup.class);
		userRelationService.update(userRelation);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public R delete(@RequestBody Integer[] ids) {
		userRelationService.deleteBatch(ids);

		return R.ok();
	}

	@RequestMapping("/userrelationadd.html")
	public String userRelationadd(@RequestParam("userId") Integer userId,HttpServletRequest req) {
		req.setAttribute("userId", userId);
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("userType", "1");
		req.setAttribute("list", userService.queryList(map));
		return "modules/cus/userrelationadd";
	}

	@RequestMapping("/userrelation.html")
	public String userrelation(@RequestParam("userId") Integer userId,
			HttpServletRequest req) {
		req.setAttribute("userId", userId);
		return "modules/cus/userrelation";
	}

	@RequestMapping("/userrelationedit.html")
	public String userRelationedit(@RequestParam("id") Integer id,
			HttpServletRequest req) {
		UserRelationEntity userRelation = userRelationService.queryObject(id);

		req.setAttribute("userRelation", userRelation);
		return "modules/cus/userrelationedit";
	}
}
