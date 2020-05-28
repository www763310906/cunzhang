package com.ye.modules.cus.controller;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ye.common.utils.R;
import com.ye.common.validator.Assert;
import com.ye.common.validator.ValidatorUtils;
import com.ye.common.validator.group.AddGroup;
import com.ye.modules.cus.entity.UserEntity;
import com.ye.modules.cus.service.UserService;
import com.ye.modules.sys.entity.SysUserEntity;
import com.ye.modules.sys.shiro.ShiroUtils;

@RestController
@RequestMapping("/cus/api/user")
public class ApiUserController {
	public final String OBJ_TAG = "data";

	@Autowired
	private UserService userService;

	/**
	 * 登录
	 */
	@PostMapping("login")
	// @RequestMapping("login")
	public R login(String username, String password, String type) {
		Assert.isBlank(username, "用户名不能为空");
		Assert.isBlank(password, "密码不能为空");
		System.out.println(type);
		UserEntity userEntity = userService.login(username, password);
		if (userEntity == null)
			return R.error("帐号密码不对");
		try {
			Subject subject = ShiroUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken("admin",
					"admin");
			subject.login(token);
		} catch (UnknownAccountException e) {
			return R.error(e.getMessage());
		} catch (IncorrectCredentialsException e) {
			return R.error("账号或密码不正确");
		} catch (LockedAccountException e) {
			return R.error("账号已被锁定,请联系管理员");
		} catch (AuthenticationException e) {
			return R.error("账户验证失败");
		}
		return R.ok().put(OBJ_TAG, userEntity);
	}

	/**
	 * 注册
	 */
	@PostMapping("register")
	// @RequestMapping("login")
	public R register(String username, String password, String phone, String pic,
			String realname, String address, String birthday) {
		Assert.isBlank(username, "用户名不能为空");
		Assert.isBlank(password, "密码不能为空");

		UserEntity userEntity;
		try {
			userEntity = new UserEntity();
			userEntity.setUsername(username);
			userEntity.setPassword(password);
			userEntity.setPhone(phone);
			userEntity.setPic(pic);
			userEntity.setStatus(1);
			userEntity.setUserType("1");
			userEntity.setAddress(address);
			userEntity.setBirthday(birthday);
			userEntity.setNickname(realname);
			userEntity.setCreateTime(new Date());
			userService.save(userEntity);
			
			return R.ok().put(OBJ_TAG, userEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return R.error("用户名已存在");

	}

	@PostMapping("info")
	public R info(@RequestParam("id") Integer id) {

		return R.ok().put(OBJ_TAG, userService.queryObject(id));
	}

	/**
	 * 注册
	 */
	@PostMapping("update")
	// @RequestMapping("login")
	public R update(String username, String password, int userId, String phone,String pic,
			String realname, String address, String birthday) {

		UserEntity userEntity;
		try {

			userEntity = userService.queryObject(userId);
			userEntity.setUsername(username);
			if (!StringUtils.isEmpty(password))
				userEntity.setPassword(password);
			userEntity.setPhone(phone);
			userEntity.setPic(pic);
			userEntity.setNickname(realname);
			userEntity.setAddress(address);
			userEntity.setBirthday(birthday);
			userService.update(userEntity);
			return R.ok().put(OBJ_TAG, userEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return R.error("用户名已存在");

	}

	@PostMapping("pwd")
	public R pwd(String oldpassword, String newpassword, String id) {
		if (StringUtils.isEmpty(id))
			return R.error("用户不存在");
		UserEntity user = userService.queryObject(Integer.parseInt(id));
		if (!user.getPassword().equals(oldpassword))
			return R.error("原密码不对");
		user.setPassword(newpassword);
		userService.update(user);
		return R.ok().put(OBJ_TAG, user);
	}
}
