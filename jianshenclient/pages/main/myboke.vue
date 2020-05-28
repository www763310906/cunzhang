<template>
	<view id="moments">

		<view class="home-pic">
			<view class="home-pic-base">
				 
			</view>
		</view>
		<view id="block">
		<view class="moments__post" v-for="(post,index) in posts" :key="index" :id="'post-'+index">
			<view class="post-left">
				<image class="post_header" :src="post.header_image" @tap="gochat(index)"></image>
			</view>

			<view class="post_right">
				<text class="post-username">{{post.username}}</text>				
				<view id="paragraph" class="paragraph">{{post.content}}</view>
				<!-- 相册 -->
				<view class="thumbnails">
					<view :class="post.pics.length === 1?'my-gallery':'thumbnail'" v-for="(image, index_images) in post.pics"
					 :key="index_images"  v-if="post.pics != $serverPicUrl+'null' ">
						<image class="gallery_img" lazy-load mode="aspectFill" :src="image" :data-src="image" @tap="previewImage(post.pics,index_images)"></image>
					</view>
				</view>
				<!-- 资料条 -->
				<view class="toolbar">
					<view class="timestamp">{{post.timestamp}}</view>
					<view class="like" @tap="like(index)">
						<image :src="post.islike===0?'../../static/index/islike.png':'../../static/index/like.png'"></image>
					</view>
					<view class="comment" @tap="comment(index)">
						<image src="../../static/index/comment.png"></image>
					</view>
				</view>
				<!-- 赞／评论区 -->
				<view class="post-footer">
					<view class="footer_content">
						<image class="liked" src="../../static/index/liked.png"></image>
					 	<text class="nickname" v-for="(user,index_like) in post.like" :key="index_like">{{user.username}}
							<text v-if="index_like < post.like.length-1">,</text>
						</text>
					</view>
					<view class="footer_content" v-for="(comment,comment_index) in post.comments" :key="comment_index" @tap="reply(index,comment_index)">
						<text class="comment-nickname">{{comment.username}} :<text class="comment-content"> &nbsp;{{comment.content}}</text></text>
					</view>
				</view>
			</view>
			<!-- 结束 post -->
		</view>
		</view>
		<view class="foot" v-show="showInput">
			<chat-input @send-message="send_comment" :focus="focus" :placeholder="input_placeholder"></chat-input>
			
		</view>
		<view class="uni-loadmore" v-if="showLoadMore">{{loadMoreText}}</view>
	</view>

</template>

<script>
	import {
		mapState,
		mapMutations
	} from 'vuex'
	
	import chatInput from '../../components/im-chat/chatinput.vue'; //input框
	// import postData from '../../common/index/index.post.data.js'; //朋友圈数据


	export default {
			computed: mapState(['forcedLogin', 'hasLogin', 'muser']),
		components: {
			chatInput
		},
		data() {
			return {
				posts: [], //模拟数据
				user_id: 0,
				username: '',
				avatarUrl: '../../static/logo.png',
				index: '',
				comment_index: '',
				check:null,
				input_placeholder: '评论', //占位内容
				focus: false, //是否自动聚焦输入框
				is_reply: false, //回复还是评论
				showInput: false, //评论输入框

				screenHeight: '', //屏幕高度(系统)
				platform: '',
				windowHeight: '', //可用窗口高度(不计入软键盘)

				loadMoreText: "加载中...",
				showLoadMore: false,
				fetchPageNum:1,
				num:0,
			}
		},
	 
		onLoad(option) {
			  console.log(this.muser);
			// if (this.muser.pic != null) {
			// 	this.avatarUrl = this.$serverPicUrl + this.muser.pic;
			// }
			 this.username=this.muser.nickname;
			 this.user_id=this.muser.id;
			
			uni.getSystemInfo({ //获取设备信息
				success: (res) => {
					this.screenHeight = res.screenHeight;
					this.platform = res.platform;
				}
			});
			uni.startPullDownRefresh();

			
		},
		onShow() {
		 
			uni.onWindowResize((res) => { //监听窗口尺寸变化,窗口尺寸不包括底部导航栏
				if (this.platform === 'ios') {
					this.windowHeight = res.size.windowHeight;
					this.adjust();
				} else {
					if (this.screenHeight - res.size.windowHeight > 60 && this.windowHeight <= res.size.windowHeight) {
						this.windowHeight = res.size.windowHeight;
						this.adjust();
					}
				}
			});
		},
		onHide() {
			uni.offWindowResize(); //取消监听窗口尺寸变化
		},
		onUnload() {
			this.max = 0,
				this.data = [],
				this.loadMoreText = "加载更多",
				this.showLoadMore = false;
		},
		
		onReachBottom() { //监听上拉触底事件
			console.log('onReachBottom');
			this.showLoadMore = true;
			// setTimeout(() => {
			// 	//获取数据
			// 	if (this.posts.length < 20) { //测试数据
			// 		this.posts = this.posts.concat(this.posts);
			// 	} else {
			// 		this.loadMoreText = "暂无更多";
			// 	}
			// }, 1000);
			this.search();
		},
		onPullDownRefresh() { //监听下拉刷新动作
			console.log('onPullDownRefresh');
			document.getElementById('block').innerHTML="";
			this.fetchPageNum=1;
			this.search();
			
			// 这里获取数据
			// setTimeout(function() {
			// 	//初始化数据
			// 	uni.stopPullDownRefresh(); //停止下拉刷新
			// }, 1000);
		},
		onNavigationBarButtonTap(e) { //监听标题栏点击事件
			if (e.index == 0) {
				this.navigateTo('../publish/publish')
			}
		},
		 
		methods: {
			search() {
			let that = this;
				console.log("我的博客",this.username);
				uni.request({
					url: this.$serverUrl + '/cus/api/myhuilist',
					method: 'post',
					header: {			
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
					data: {	
						user_id:this.user_id,
						page: this.fetchPageNum,
						limit: 10 
					},
					success: res => {
			
						if (res.statusCode == 200) {
							if (res.data.code == 0) {
								let data = res.data.data;
								if (data.length == 0) {
			
									uni.showToast({
			
										title: "没有更多数据了",
										icon: "none"
			
									});
									this.loadMoreText = "暂无更多";
									return;
								}
								this.fetchPageNum++;
								for (let i = 0, length = data.length; i < length; i++) {
									let pic=data[i].pic;									
									pic = (pic.substring(pic.length - 1) == ',') ? pic.substring(0, pic.length-1) : pic;
									
									let pics=pic;
									pics= pic.split(',');
									console.log("pic.length:",pics.length);
								for (let j = 0, length1 = pics.length; j < length1; j++){
									pics[j]=that.$serverPicUrl+ pics[j];
								}
								let comments = data[i].comments;
								let whocomment = data[i].comments.username;
								let num = 0;
								let like=data[i].zan;
								console.log("data",data[i]);
								console.log("comments：",comments);
								console.log("like",like);
								
								//显示是否点过赞
								uni.request({
									url: that.$serverUrl + '/cus/zan/queryIsExist', //查询是否已经点赞
									method: 'post',
									data: {
										bokeId: data[i].id,							 
										userId: that.muser.id,													 
									},
									success: res => {
										console.log("res:",res);
										if (res.statusCode == 200) {
											if (res.data.code == 0) {
												console.log("check");
												if(res.data.check){
													console.log("true:",res.data.check);													
													num = 1;
												}else{
													console.log("false:",res.data.check);
													
													num = 0;
												}
												that.posts.push({
													post_id: data[i].id,
													uid:data[i].userId,
													username :data[i].nickname, 
													content: data[i].content,
													pics:pics,
													"islike": num,
													like:like,
													timestamp:data[i].createTime,
													comments:comments,
													whocomment:whocomment,										
													header_image:that.$serverPicUrl+ data[i].head   
												});
											} 
										}
									}
								});
								
							}
			
							} else {
								uni.showModal({
									content: res.data.msg,
									title: '消息提示',
									showCancel: false,
									success: (res) => {
			
									}
								})
							}
						}
			
						
			
					},
					complete: function() {
						 
						uni.stopPullDownRefresh();
			
					}
				});
			},
			gochat(index) {
				console.log("go");
				uni.navigateTo({
					url: "../main/frenddetail?id="+this.posts[index].uid
				});
			},
			test() {
				this.navigateTo('../test/test');
			},
			navigateTo(url) {
				uni.navigateTo({
					url: url
				});
			},
			like(index) {
				if (this.posts[index].islike === 0) { //还未点赞
					this.posts[index].islike = 1;
					this.posts[index].like.push({
						"uid": this.muser.id,
						"username" :  this.muser.nickname
					});					
					var that = this;														
					uni.request({
						url: that.$serverUrl + '/cus/zan/save', //获取访问令牌
						method: 'post',
						data: {
							bokeId:  that.posts[index].post_id,							 
							userId: that.muser.id,							 
						},
						success: res => {
							console.log(res);
							if (res.statusCode == 200) {
								if (res.data.code == 0) {
									uni.showToast({
										title: "点赞成功",
										icon: "none"
									});
								} 
							}
						}
					});					
					
					//如果已经点赞了，然后再点击,取消点赞
				} else {
					var that = this;
					this.posts[index].islike = 0;
					this.posts[index].like.splice(this.posts[index].like.indexOf({						
						"userId": this.muser.userId
					}), 1);
					console.log("delete bokeId:",that.posts[index].like[0].bokeId);
					console.log("delete userId:",that.muser.id);
					let bokeId = that.posts[index].like[0].bokeId;
					let userId = that.muser.id;
					uni.request({
						url: that.$serverUrl + '/cus/zan/delete/userId', //获取访问令牌
						method: 'post',					
						data: {
							bokeId: that.posts[index].like[0].bokeId,							 
							userId: that.muser.id,							
						 
						},
						success: res => {
							console.log("zan delete");							
							if (res.statusCode == 200) {								
								if (res.data.code == 0) {	
									uni.showToast({
										title: "点赞取消",
										icon: "none"
									});													
								}
							}
							}
						});
					
					
					
					
				}
			},
			comment(index) {
				this.showInput = true; //调起input框
				this.focus = true;
				this.index = index;
			},
			adjust() { //当弹出软键盘发生评论动作时,调整页面位置pageScrollTo
				return;
				uni.createSelectorQuery().selectViewport().scrollOffset(res => {
					var scrollTop = res.scrollTop;
					let view = uni.createSelectorQuery().select("#post-" + this.index);
					view.boundingClientRect(data => {
						console.log("data:" + JSON.stringify(data));
						console.log("手机屏幕高度:" + this.screenHeight);
						console.log("竖直滚动位置" + scrollTop);
						console.log("节点离页面顶部的距离为" + data.top);
						console.log("节点高度为" + data.height);
						console.log("窗口高度为" + this.windowHeight);

						uni.pageScrollTo({
							scrollTop: scrollTop - (this.windowHeight - (data.height + data.top + 45)), //一顿乱算
							// scrollTop: 50, 
							duration: 300
						});
					}).exec();
				}).exec();
			},
			reply(index, comment_index) {
				console.log("comment2 ");
				this.is_reply = true; //回复中
				this.showInput = true; //调起input框
				let replyTo = this.posts[index].comments.comment[comment_index].username;
				this.input_placeholder = '回复' + replyTo;
				this.index = index; //post索引
				this.comment_index = comment_index; //评论索引
				this.focus = true;

			},
			blur: function() {
				console.log("comment1 ");
				this.init_input();
				
			},
			send_comment: function(message) {
				
			console.log("comment sdfsd");
				if (this.is_reply) {
					console.log("comment 894195621561");
					var reply_username = this.posts[this.index].comments.comment[this.comment_index].username;
					var comment_content = '回复' + reply_username + ' : ' + message.content;
				} else {
					var comment_content = message.content;
				}
				this.posts[this.index].comments.total += 1;
				this.posts[this.index].comments.push({
					"id": this.muser.id,
					"username": this.muser.nickname,
					"content": comment_content //直接获取input中的值
				});
				this.init_input();
				
				var that = this;
				uni.request({
					url: that.$serverUrl + '/cus/comment/save', //获取访问令牌
					method: 'post',
					header: {
				
						//'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
				
					data: {
						bokeId: that.posts[that.index].post_id,
						content:comment_content,
						userId: that.muser.id,					 
					},
					success: res => {
						console.log(res);
						if (res.statusCode == 200) {
							if (res.data.code == 0) {

							} else {
								uni.showToast({
									title: "已评论",
									icon: "none"
								});
							}
						}
					}
				
				});
				
			},
			init_input() {
				this.showInput = false;
				this.focus = false;
				this.input_placeholder = '评论';
				this.is_reply = false;
			},
			previewImage(imageList, image_index) {
				var current = imageList[image_index];
				uni.previewImage({
					current: current,
					urls: imageList
				});
			},
			goPublish() {
				uni.navigateTo({
					url: './publish/publish',
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			}
		}
	}
</script>

<style scoped>
	@import url("../../common/index/index.css");
</style>
