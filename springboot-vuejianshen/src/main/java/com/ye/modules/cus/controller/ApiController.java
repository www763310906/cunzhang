package com.ye.modules.cus.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ye.common.exception.RRException;
import com.ye.common.utils.DateUtils;
import com.ye.common.utils.Query;
import com.ye.common.utils.R;
import com.ye.common.utils.ResourceUtil;
import com.ye.modules.cus.entity.ChatEntity;
import com.ye.modules.cus.entity.CommentEntity;
import com.ye.modules.cus.entity.GonggaoEntity;
import com.ye.modules.cus.entity.HuiEntity;
import com.ye.modules.cus.entity.UserEntity;
import com.ye.modules.cus.entity.VideoEntity;
import com.ye.modules.cus.entity.ViewEntity;
import com.ye.modules.cus.entity.ZanEntity;
import com.ye.modules.cus.service.ChatService;
import com.ye.modules.cus.service.CommentService;
import com.ye.modules.cus.service.GonggaoService;
import com.ye.modules.cus.service.HuiService;
import com.ye.modules.cus.service.UserRelationService;
import com.ye.modules.cus.service.UserService;
import com.ye.modules.cus.service.VideoService;
import com.ye.modules.cus.service.ViewService;
import com.ye.modules.cus.service.ZanService;
				
@RestController
@RequestMapping("/cus/api")
public class ApiController {
	public final String OBJ_TAG = "data";

	@Autowired
	public UserService userService;
	@Autowired
	public GonggaoService gonggaoService;
	@Autowired
	public HuiService huiService;
	@Autowired
	public VideoService videoService;
	@Autowired
	public ViewService viewService;
	@Autowired
	public ChatService chatService;
	@Autowired
	public UserRelationService relationService;
		@Autowired
		public CommentService commentService;
		@Autowired
		public ZanService zanService;
	private void initPage(Map<String, Object> params) {
		params.put("page", "1");
		params.put("limit", "100");
		params.put("sidx", "");
		params.put("order", "");
	}

	/**
	 * 上传文件	
	 */
	@RequestMapping("/upload")
	@ResponseBody
	public R upload(@RequestParam("file") MultipartFile file,
			HttpServletRequest request) throws Exception {
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}

		// 上传文件
		String suffix = file.getOriginalFilename().substring(
				file.getOriginalFilename().lastIndexOf("."));
		System.out.println(suffix);
		// String url = OSSFactory.build().uploadSuffix(file.getBytes(),
		// suffix);

		String realPath = request.getSession().getServletContext()
				.getRealPath("/")
				+ "/upload";// 文件的硬盘真实路径
		realPath = ResourceUtil.getConfigByName("basePath");
		File file0 = new File(realPath);
		if (!file0.exists()) {
			file0.mkdirs();// 创建根目录
		}
		File savefile = new File(realPath + "/" + System.currentTimeMillis()
				+ suffix);
		System.out.println(savefile.getPath());
		// 文件拷贝到指定硬盘目录
		FileCopyUtils.copy(file.getBytes(), savefile);
		String contextPath = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + contextPath;
		return R.ok().put("url", "/upload/" + savefile.getName());
	}

	/**
	 * 上传头像	
	 */
	@RequestMapping("/userPic")
	@ResponseBody
	public R userPic(@RequestParam("file") MultipartFile file,
			HttpServletRequest request) throws Exception {
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}
		// 上传文件
		String suffix = file.getOriginalFilename().substring(
				file.getOriginalFilename().lastIndexOf("."));
		System.out.println(suffix);
		String realPath = request.getSession().getServletContext()
				.getRealPath("/")
				+ "/userPic";// 文件的硬盘真实路径
		realPath = ResourceUtil.getConfigByName("userPicPath");
		File file0 = new File(realPath);
		if (!file0.exists()) {
			file0.mkdirs();// 创建根目录
		}
		File savefile = new File(realPath + "/" + System.currentTimeMillis()
				+ suffix);
		System.out.println(savefile.getPath());
		// 文件拷贝到指定硬盘目录
		FileCopyUtils.copy(file.getBytes(), savefile);

		String contextPath = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + contextPath;
		return R.ok().put("url", "/userPic/" + savefile.getName());
	}
	
	/**
	 * 公告列表
	 */
	@RequestMapping("/gonggaolist")
	@ResponseBody
	public R gonggaolist(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);

		List<GonggaoEntity> list = gonggaoService.queryList(query);

		return R.ok().put(OBJ_TAG, list);

	}

	/**
	 * 公告信息
	 */
	@RequestMapping("/gonggaoinfo")
	@ResponseBody
	public R gonggaoinfo(@RequestParam Integer id) {

		GonggaoEntity good = gonggaoService.queryObject(id);

		return R.ok().put(OBJ_TAG, good);

	}

 
	/**
	 * 博客列表
	 */
	@RequestMapping("/huilist") 
	@ResponseBody
	public R huilist(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);

		List<HuiEntity> list = huiService.queryList(query);
		for (HuiEntity huiEntity : list) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("bokeId", huiEntity.getId());
			UserEntity user = userService.queryObject(huiEntity.getUserId());
			String nickname = user.getNickname();
			String userPic = user.getPic();
			huiEntity.setNickname(nickname);
			huiEntity.setHead(userPic);
			List<CommentEntity> comments = commentService.queryListByBokeId(huiEntity.getId());
			for(CommentEntity commentEntiy :comments){
				UserEntity userEntity =userService.queryObject(commentEntiy.getUserId());
				commentEntiy.setUsername(userEntity.getNickname());
			}
			List<ZanEntity>  zans = zanService.queryListByBokeId(huiEntity.getId());
			for(ZanEntity zanEntity :zans){				
				UserEntity userEntity =userService.queryObject(zanEntity.getUserId());
				 zanEntity.setUsername(userEntity.getNickname());
			}
			huiEntity.setComments(comments);
			huiEntity.setZan(zans);		
		}
		return R.ok().put(OBJ_TAG, list);

	}
	/**
	 * 博客列表
	 */
	@RequestMapping("/myhuilist") 
	@ResponseBody
	public R myhuilist(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);

		List<HuiEntity> list = huiService.queryListByUser(query);
		for (HuiEntity huiEntity : list) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("bokeId", huiEntity.getId());
			UserEntity user = userService.queryObject(huiEntity.getUserId());
			String nickname = user.getNickname();
			String userPic = user.getPic();
			huiEntity.setNickname(nickname);
			huiEntity.setHead(userPic);
			List<CommentEntity> comments = commentService.queryListByBokeId(huiEntity.getId());
			for(CommentEntity commentEntiy :comments){
				UserEntity userEntity =userService.queryObject(commentEntiy.getUserId());
				commentEntiy.setUsername(userEntity.getNickname());
			}
			List<ZanEntity>  zans = zanService.queryListByBokeId(huiEntity.getId());
			for(ZanEntity zanEntity :zans){				
				UserEntity userEntity =userService.queryObject(zanEntity.getUserId());
				 zanEntity.setUsername(userEntity.getNickname());
			}
			huiEntity.setComments(comments);
			huiEntity.setZan(zans);		
		}
		return R.ok().put(OBJ_TAG, list);

	}
	@RequestMapping("/huiinfo") 
	@ResponseBody
	public R huiinfo( int id) {
		return R.ok().put(OBJ_TAG,huiService.queryObject(id));
	}
	/**
	 * 列表
	 */
	@RequestMapping("/chatlist")
	@ResponseBody
	public R list(@RequestBody ChatEntity chat){		 
		List<ChatEntity> chatList = chatService.queryChatList(chat);
		if(!chatList.isEmpty()){
			ChatEntity chatEntity = chatList.get(0);				
			int touser = chatEntity.getTouserId();
			int user = chatEntity.getUserId();
			UserEntity userEntity1 =userService.queryObject(touser);
			UserEntity userEntity2 =userService.queryObject(user);
			String tousername = userEntity1.getNickname();
			String username = userEntity2.getNickname();
			String touserPic = userEntity1.getPic();
			String userPic = userEntity2.getPic();							
			for(ChatEntity c:chatList){
				chat.setTousername(tousername);
				chat.setUsername(username);
			}
			return R.ok().put("rows", chatList).put("tousername", tousername).put("username", username).put("touserPic", touserPic).put("userPic", userPic);
		}		
		return R.ok();
	}
	
	@RequestMapping("/videolist") 
	@ResponseBody
	public R videolist(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);

		List<VideoEntity> list = videoService.queryList(query);
		for(VideoEntity v:list){
			UserEntity userEntity =userService.queryObject(v.getUserId());
			String nickname = userEntity.getNickname();
			v.setNickname(nickname);
		}
		return R.ok().put(OBJ_TAG, list);

	}
	/**
	 * 列表
	 */
	@RequestMapping("/recommend")
	@ResponseBody
	public R recommend(@RequestParam Map<String, Object> params){
	 

		List<VideoEntity> videoList = videoService.recommendList(Integer.parseInt(params.get("userId").toString()));
		for(VideoEntity v:videoList){
			UserEntity userEntity = userService.queryObject(v.getUserId());
			String nickname = userEntity.getNickname();
			v.setNickname(nickname);
		}
		return R.ok().put("total", videoList.size()).put("rows", videoList);
		//return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/videoinfo")
	@ResponseBody
	public R videoInfo(@RequestParam("id") Integer id,@RequestParam("userId") Integer userId){
		VideoEntity video = videoService.queryObject(id);
		UserEntity userEntity =userService.queryObject(userId);
		String nickname = userEntity.getNickname();
		video.setNickname(nickname);
		ViewEntity view=new ViewEntity();
		view.setCreateTime(new Date());
		view.setGoodsId(id);
		view.setUserId(userId);
		viewService.save(view);
		return R.ok().put(OBJ_TAG, video);
	}
	@RequestMapping("/videodel") 
	@ResponseBody
	public R videodel( int id) {		
		 videoService.delete(id);		 
		return R.ok() ;
	}

	@RequestMapping("/videoSearch")
	@ResponseBody
	public R videoSearch(@RequestParam Map<String, Object> params){
		Query query = new Query(params);
		List<VideoEntity> videoList = videoService.queryListSearch(query);
		for(VideoEntity v:videoList){
			UserEntity userEntity =userService.queryObject(v.getUserId());
			String nickname = userEntity.getNickname();
			v.setNickname(nickname);
		}
		return R.ok().put("data", videoList);
	}  
	/**
	 * video添加
	 */
	@RequestMapping("/videoadd") 
	@ResponseBody
	public R videoadd(String content,String pic,String video,String name,int userId,int free) {
		 

		 VideoEntity zuo=new VideoEntity();
		 zuo.setUserId(userId);
		 zuo.setName(name);
		 zuo.setContent(content);
		 zuo.setCreateTime(new Date());
		 zuo.setPic(pic);
		 zuo.setVideo(video);
		 zuo.setFree(free);	
		 videoService.save(zuo);
		 
		return R.ok().put(OBJ_TAG, zuo);

	}
}
