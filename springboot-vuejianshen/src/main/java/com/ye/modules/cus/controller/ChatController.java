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
import com.ye.modules.cus.entity.ChatEntity;
import com.ye.modules.cus.service.ChatService;

import com.ye.common.utils.Query;
import com.ye.common.utils.R;
import com.ye.common.validator.ValidatorUtils;
import com.ye.common.validator.group.AddGroup;
import com.ye.common.validator.group.UpdateGroup;

/**
 * 聊天
 * 
 */
@Controller
@RequestMapping("/cus/chat")
public class ChatController {
	@Autowired
	private ChatService chatService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public R list(@RequestBody ChatEntity chat){		 
		List<ChatEntity> chatList = chatService.queryChatList(chat);			
		return R.ok().put("rows", chatList);	
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@ResponseBody
	public R info(@PathVariable("id") Integer id){
		ChatEntity chat = chatService.queryObject(id);
		
		return R.ok().put("chat", chat);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@ResponseBody
	public R save(@RequestBody ChatEntity chat){
		chat.setCreateTime(new Date());
	 	ValidatorUtils.validateEntity(chat, AddGroup.class);
		chatService.save(chat);		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@ResponseBody
	public R update(@RequestBody ChatEntity chat){
	ValidatorUtils.validateEntity(chat, UpdateGroup.class);
		chatService.update(chat);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public R delete(@RequestBody Integer[] ids){
		chatService.deleteBatch(ids);
		
		return R.ok();
	}
	@RequestMapping("/chatadd.html")
	public String chatadd(HttpServletRequest req) {

		return "modules/cus/chatadd";
	}

	@RequestMapping("/chatedit.html")
	public String chatedit(@RequestParam("id") Integer id,
			HttpServletRequest req) {
		ChatEntity chat = chatService.queryObject(id);
		
		req.setAttribute("chat", chat);
		return "modules/cus/chatedit";
	}
}
