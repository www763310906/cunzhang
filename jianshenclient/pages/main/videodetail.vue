<template>


	<view class="content1">

 

		<view  class="banner-img" >
		  <video class="video" :src="item.video" :controls="true" :loop="true"></video>
		</view>
		<view class="article-meta">
			<text class="article-author">视频标题：</text>
			<rich-text :nodes="item.name"></rich-text>
		</view>
		<view class="article-meta">
			<text class="article-author">视频描述：</text>
			<rich-text :nodes="item.content"></rich-text>
		</view>
		
		<view class="article-meta">
			<text class="article-author">发布者：</text>
			<rich-text :nodes="item.user"></rich-text>
		</view>
		<view class="article-meta">
			<text class="article-author">发布时间：</text>
			<rich-text :nodes="item.time"></rich-text>
		</view>		
	</view>
	</view>


</template>

<script>
	import service from '../../service.js';
	import htmlParser from '@/components/html-parser.js'
	import uniSwiperDot from '@/components/uni-swiper-dot/uni-swiper-dot.vue'
	import {
		mapState,
		mapMutations
	} from 'vuex'

	export default {
		computed: mapState(['forcedLogin', 'hasLogin', 'muser']),
		
		components: {
			uniSwiperDot

		},
		data() {
			return {
				nickname:'',
				userId:0,
				name:'',
				content:'',
				current_time:0,
				item: [],
				htmlNodes: [],
				modeIndex: -1,
				styleIndex: -1,
				current: 0,
				mode: 'default',
				dotsStyles: {},
				pics:[]
			}
		},

		methods: {
		
			change(e) {
				this.current = e.detail.current
			},
			showpic: function(pics) {
				uni.previewImage({
					count: 1,
					urls: pics
				})


			},
		},
		onLoad(option) {
			var that = this;
			 that.goods=option.id;
			uni.request({
				url: this.$serverUrl + '/cus/api/videoinfo', //获取访问令牌
				method: 'post',
				header: {
				 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
				},
				data: {
					id: that.goods,
					userId:that.muser.id,
				},
				success: res => {

					if (res.statusCode == 200) {
						if (res.data.code == 0) {
							console.log("res:",res);							
							console.log("video:",res.data);
							let data = res.data.data;
							that.item={
								name: data.name,
								content:data.content,
								img: that.$serverPicUrl + data.pic,
								pics:  data.pic.split(','),
								video: that.$serverPicUrl+data.video,
								time: data.createTime,
								user: data.nickname
							};
							console.log("item:",that.item);
							 for (let i in that.item.pics) {						  
							 	that.pics.push(that.$serverPicUrl+that.item.pics[i]);
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

				}
			});

		}
	};
</script>

<style>
	 .video {
	        flex: 1;
	        /* #ifndef APP-PLUS */
	        width: 100%;
	        /* #endif */
	    }
	.content1 {
		display: flex;
		flex: 1;
		flex-direction: column;
		background-color: #efeff4;

	}

	.status_bar {
		/*     height: var(--status-bar-height); */
		min-height: 44px;

		width: 100%;

	}

	.status_bar .bar_text {
		font-size: 30upx;
		font-weight: 500;
		padding: 20upx 0;
		line-height: 1.5;
	}

	.banner {
		/* padding: 0 10%; */
		overflow: hidden;
		position: relative;

	}

	.banner-img {
		width: 100%;

	}

	.cls {
		text-indent: 2em;
	}

	.banner-title {
		max-height: 84upx;
		overflow: hidden;
		position: absolute;
		left: 30upx;
		bottom: 30upx;
		width: 90%;
		font-size: 32upx;
		font-weight: 400;
		line-height: 42upx;
		color: white;
		z-index: 11;
	}

	.article-meta {
		padding: 5upx 40upx;
		display: flex;
		flex-direction: row;
		justify-content: flex-start;
		color: black;
	}

	.article-text {
		font-size: 26upx;
		line-height: 50upx;
		margin: 0 20upx;
	}

	.article-author,
	.article-time {
		font-size: 30upx;
	}

	.article-content {
		padding: 0 30upx;
		overflow: hidden;
		font-size: 30upx;
		margin-bottom: 30upx;
	}
	
	
	
	
 
	
	.example {
		padding: 0 30upx 30upx
	}
	
	.example-title {
		font-size: 32upx;
		line-height: 32upx;
		color: #777;
		margin: 40upx 25upx;
		position: relative
	}
	
	.example .example-title {
		margin: 40upx 0
	}
	
	.example-body {
		padding: 0 40upx
	}
	
	.swiper-box {
		height: 400upx;
	}
	
	.swiper-item {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 100%;
		background: #eee;
		color: #fff;
	}
	
	.swiper-item image {
		width: 100%;
		height: 100%;
	}
	
	.uni-bg-red {
		background: #ff5a5f;
	}
	
	.uni-bg-green {
		background: #09bb07;
	}
	
	.uni-bg-blue {
		background: #007aff;
	}
	
	.uni-swiper__box {
		margin-top: 30upx;
	}
	
	.uni-swiper__header {
		padding: 0 30upx;
		font-size: 32upx;
		color: #333;
	}
	
	.uni-swiper__info {
		display: flex;
		padding: 0 15upx;
	}
	
	.uni-swiper__info-item {
		display: flex;
		justify-content: center;
		align-items: center;
		margin: 15upx;
		height: 60upx;
		width: 100%;
		font-size: 28upx;
		color: #333;
		border: 1px #eee solid;
		border-radius: 10upx;
	}
	
	.uni-swiper__info-dots {
		width: 16upx;
		height: 16upx;
		border-radius: 50%;
		background: #333333;
		margin-left: 10upx;
		box-sizing: border-box;
	}
	
	.uni-swiper__info-dots:first-child {
		margin: 0;
	}
	
	.active {
		border: 1px #000 solid;
	}
	
	slider {
		width: 100%;
	}
</style>
