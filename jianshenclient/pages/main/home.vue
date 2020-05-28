<template>
	<view class="content1">
		<view>
			<!--<image class="banner-img" :src="item.img" mode="aspectFit"></image> -->
			<uni-swiper-dot class="banner-img" :info="pics" :current="current" :mode="mode" :dots-styles="dotsStyles" field="content">
				<swiper class="swiper-box">
					<swiper-item v-for="(item ,index) in pics" :key="index">
						<view class="swiper-item">
							<image :src="item" mode="aspectFill" />
						</view>
					</swiper-item>
				</swiper>
			</uni-swiper-dot>
		</view>
		<view class="content" style="padding: 0;">
			<view class="example-title">
				<view class="tag-view">
					<uni-tag text="最新" type="primary" :inverted="inverted1" @click="setInverted(0)" />
				</view>
				<view class="tag-view">
					<uni-tag text="推荐" type="primary" :inverted="inverted2" @click="setInverted(1)" />
				</view>

			</view>
				<view class="input-row border" >
					<m-input class="m-input border-box" placeholder="请输入视频标题或作者" v-model="name"></m-input><button type="primary" class="primary" @tap="searchVideo">查询</button>
				</view>
			<view class="tip"><text class="uni-product-tip">共{{list.length}}条</text></view>
			<view class="uni-product-list">
				<view class="uni-product" v-for="(product,index) in list" :key="index" >
					<view class="image-vietimew">
						<image lazy-load="true" class="uni-product-image" :src="product.pic" @click="goDetail(product)"></image>
					</view>
				   <view  v-if="product.name" class="uni-product-title">标题：{{product.name}}</view>
				   <view  v-if="product.name" class="uni-product-title red" @click="goDetailUser(product.userId)">作者：{{product.userName}}</view>
				   <view  v-if="product.name" class="uni-product-title">时间：{{product.time}}</view>
					
				</view>
				
			</view>
		<view class="uni-product-price">
			
			<text class="uni-product-price-original">共{{list.length}}条</text>
		</view>
		</view>

	</view>
	</view>
</template>

<script>
	import service from '../../service.js';
	import uniTag from '@/components/uni-tag/uni-tag.vue'
	import uniSwiperDot from '@/components/uni-swiper-dot/uni-swiper-dot.vue'
	
	import {
		mapState,		
	} from 'vuex'
	import mInput from '../../components/m-input.vue'

	export default {
		computed: mapState(['forcedLogin', 'hasLogin', 'muser']),
		components: {
			uniTag,uniSwiperDot,mInput
		},
		data() {
			return {
				name:'',
				refreshing: false,
				list: [],
				fetchPageNum: 1,
				loadall: false,
				current: 1,
				inverted: [true, false],
				inverted1: true,
				inverted2: false,
				pics: [],
				modeIndex: -1,
				styleIndex: -1,
				current: 0,
				mode: 'default',
				dotsStyles: {},
				userId:0,

			}
		},
		onPullDownRefresh() {
			console.log('下拉刷新');
			list=[];
			this.refresh();
		},
		onReachBottom() {
			if (this.loadall) return;
			this.fetchPageNum++;
			this.search();
		},

		onShow() {
			this.list=[];
			this.fetchPageNum=1;
			this.search();
		},
		methods: {
			setInverted(index) {
				if (index == 0) {
					this.inverted1 = true;
					this.inverted2 = false;
					this.choseindex = 0;
					this.list = [];
					this.refresh();
				} else if (index == 1) {
					this.inverted1 = false;
					this.inverted2 = true;
					this.choseindex = 1;
					this.list = [];
					this.search1();
				}
				
			},
			refresh: function() {
					this.fetchPageNum = 1;
					this.loadall = false;
					this.refreshing = true;
					this.list = [];
					this.search();
				
			},
			search() {
				var that = this;
				var free='';
				
				if(this.choseindex==1)  {
					this.search1();
					return;
				}
				uni.request({
					url: this.$serverUrl + '/cus/api/videolist',
					method: 'post',
					header: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
					data: {
						page: this.fetchPageNum,
						limit: 10,
						sidx:free,
					},
					success: res => {
				
						if (res.statusCode == 200) {
							if (res.data.code == 0) {
								let data = res.data.data;
								console.log("videolist:",res.data);
								if (data.length == 0) {
									uni.showToast({
				
										title: "没有更多数据了",
										icon: "none"
				
									});
									return;
								}
								for (let i = 0, length = data.length; i < length; i++) {
									that.list.push({
										id: data[i].id,
										content: data[i].content,
										name: data[i].name,
										free: data[i].free,
										pic: that.$serverPicUrl + data[i].pic,
										time:data[i].createTime,
										video:that.$serverPicUrl + [i].video,
										userName:data[i].nickname,
										userId:data[i].userId
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
						that.refreshing = false;
						uni.stopPullDownRefresh();
				
					}
				});
			},
			search1() {
				var that = this;
			 
				uni.request({
					url: this.$serverUrl + '/cus/api/recommend',
					method: 'post',
					header: {
				
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
					data: {
						userId: this.muser.id,
					},
					success: res => {
				
						if (res.statusCode == 200) {
							if (res.data.code == 0) {
								let data = res.data.rows;
								console.log("recommend:",res.data);
								if (data.length == 0) {
									uni.showToast({
										title: "没有更多数据了",
										icon: "none"
									});
									return;
								}
								this.list=[];
								for (let i = 0; i <6; i++) {
									that.list.push({
										id: data[i].id,										 
										content: data[i].content,
										name: data[i].name,
										free: data[i].free,
										pic: that.$serverPicUrl + data[i].pic,
										time:data[i].createTime,
										userName:data[i].nickname,
										userId:data[i].userId
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
						that.refreshing = false;
						uni.stopPullDownRefresh();
					}
				});
			},
			goDetail(e) {
				uni.navigateTo({
					url: 'videodetail?id='+e.id
				})
			},
			searchVideo(){
				this.list = [];
				this.fetchPageNum = 1;
				var that = this;
				var free=1;
				var videoName = this.name;
				console.log("videoName:",videoName);
				uni.request({
					url: this.$serverUrl + '/cus/api/videoSearch',
					method: 'post',
					header: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
					data: {
						page: this.fetchPageNum,
						limit: 10,
						sidx:free,
						name:videoName					
					},
					success: res => {
						if (res.statusCode == 200) {
							if (res.data.code == 0) {
								console.log("fetchPageNum:",this.fetchPageNum);
								console.log("videoSearch:",res.data);
								let data = res.data.data;
								if (data.length == 0) {
									uni.showToast({
										title: "没有更多数据了",
										icon: "none"
									});
									return;
								}
								for (let i = 0, length = data.length; i < length; i++) {
									that.list.push({
										id: data[i].id,
										content: data[i].content,
										name: data[i].name,
										free: data[i].free,
										pic: that.$serverPicUrl + data[i].pic,
										time:data[i].createTime,
										userName:data[i].nickname,
										gsize:data.length,
										userId:data[i].userId
									});
								}
								console.log("searchVideo:",res);
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
						that.refreshing = false;
						uni.stopPullDownRefresh();
					}
				});
				},
				goDetailUser(index) {
					console.log("index",index);
					uni.navigateTo({
						url: './frenddetail?id=' + index
					});
				
				}
		},
		
			
		onLoad() {
			this.list=[];
			this.fetchPageNum=1;
			this.pics.push('../../static/img/run1.jpg');
			this.pics.push('../../static/img/run2.jpg');
			this.pics.push('../../static/img/run3.jpg');
			// 防止app里由于渲染导致转场动画卡顿
			setTimeout(() => {
				this.refresh();
			}, 300);
			
		}
	}
</script>

<style>
	.content1 {
		display: flex;
		flex: 1;
		flex-direction: column;
		background-color: #ffffff;

	}

	page {
		display: flex;
		flex-direction: column;
		box-sizing: border-box;
		background-color: #fff
	}

	view {
		font-size: 28upx;
		line-height: inherit
	}

	.example {
		padding: 0 30upx 30upx
	}

	.example-title {
		font-size: 32upx;
		line-height: 32upx;
		color: #777;
		margin: 40upx 25upx;
		position: relative;
		display: flex;
		flex-direction: row;
		align-self: center;
	}

	.example .example-title {
		margin: 40upx 0
	}

	.example-body {
		padding: 0 40upx
	}

	.uni-product-list {
		display: flex;
		width: 100%;
		flex-wrap: wrap;
		flex-direction: row;
		justify-content: center;
	}

	.textmy {

		width: 300upx;
		height: auto;
		word-wrap: break-word;
		word-break: break-all;
		overflow: hidden;

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
	.border-box{
		border: 3upx solid #007AFF;
		border-radius: 30upx;
	}
	.uni-product-tip {
		text-align: center;
		height:40upx;
		background-color: #ff3333;
		color: #ffffff;
		padding: 0 10upx;
		border-radius: 5upx;
		
	}
	.tip{
		height:40upx;
		text-align: center;
	}
	.red{
		color: red;
	}
</style>
