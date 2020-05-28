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
import com.ye.modules.cus.entity.CommentEntity;
import com.ye.modules.cus.service.CommentService;

import com.ye.common.utils.Query;
import com.ye.common.utils.R;
import com.ye.common.validator.ValidatorUtils;
import com.ye.common.validator.group.AddGroup;
import com.ye.common.validator.group.UpdateGroup;

/**
 * 评论
 * 
 */
@Controller
@RequestMapping("/cus/comment")
public class CommentController {
	@Autowired
	private CommentService commentService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<CommentEntity> commentList = commentService.queryList(query);
		int total = commentService.queryTotal(query);
		
		return R.ok().put("total", total).put("rows", commentList);
		//return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@ResponseBody
	public R info(@PathVariable("id") Integer id){
		CommentEntity comment = commentService.queryObject(id);
		
		return R.ok().put("comment", comment);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@ResponseBody
	public R save(@RequestBody CommentEntity comment){
			  		  		  		  		 comment.setCreateTime(new Date());
	 	ValidatorUtils.validateEntity(comment, AddGroup.class);
		commentService.save(comment);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@ResponseBody
	public R update(@RequestBody CommentEntity comment){
	ValidatorUtils.validateEntity(comment, UpdateGroup.class);
		commentService.update(comment);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public R delete(@RequestBody Integer[] ids){
		commentService.deleteBatch(ids);
		
		return R.ok();
	}
	@RequestMapping("/commentadd.html")
	public String commentadd(HttpServletRequest req) {

		return "modules/cus/commentadd";
	}

	@RequestMapping("/commentedit.html")
	public String commentedit(@RequestParam("id") Integer id,
			HttpServletRequest req) {
		CommentEntity comment = commentService.queryObject(id);
		
		req.setAttribute("comment", comment);
		return "modules/cus/commentedit";
	}
}
