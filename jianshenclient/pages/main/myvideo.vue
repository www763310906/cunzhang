<template>
	<view class="page">
		<view class="input-row border" >
			<m-input class="m-input border-box" placeholder="请输入视频标题" v-model="name"></m-input><button type="primary" class="primary" @tap="searchVideo">查询</button>
		</view>
		<view class="tip"><text class="uni-product-tip">共{{list.length}}条</text></view>
		<view class="uni-product-list">
			<view class="uni-product" v-for="(product,index) in list" :key="index" >
				<view class="image-view">
					<image lazy-load="true" class="uni-product-image" :src="product.pic" @click="goDetail(product)"></image>
				</view>
				<view  v-if="product.name" class="uni-product-title">标题：{{product.name}}</view>  
			    <view  v-if="product.name" class="uni-product-title">时间：{{product.time}}</view>
				 <view  v-if="product.name" class="uni-text-small" @click="del(product.id)">删除</view>
				<view class="uni-product-price">
					<text class="uni-product-price-favour"></text>
					<text class="uni-product-price-original"></text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import mInput from '../../components/m-input.vue'
	import {
		mapState,
		mapMutations
	} from 'vuex'
	export default {
			computed: mapState(['forcedLogin', 'hasLogin', 'muser']),
			components: {
				mInput
			},
		data() {
			return {
				refreshing: false,
				list: [],
				fetchPageNum: 1,
				loadall: false,
				user_id:0,
			}
		},
		onPullDownRefresh() {
			console.log('下拉刷新');

			this.refresh();
		},
		onReachBottom() {
			if (this.loadall) return;
			this.fetchPageNum++;
			this.search();
		},


		methods: {
			
			refresh: function() {
				console.log('下拉刷新');
				this.fetchPageNum = 1;
				this.loadall = false;
				this.refreshing = true;
				this.list = [];
				this.search();
			},
			search() {
				var that = this;
				var free='';
				if(!this.hasLogin)  free=1;
				uni.request({
					url: this.$serverUrl + '/cus/api/videolist',
					method: 'post',
					header: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
					data: {
						page: this.fetchPageNum,
						limit: 10,
						free:free,
						userId:this.muser.id,
					},
					success: res => {
						if (res.statusCode == 200) {
							if (res.data.code == 0) {
								let data = res.data.data;
								console.log("muser:",this.muser.id);
								console.log("myvideolist:",data);
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
										gsize:data.length
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
										gsize:data.length
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
			goDetail(e) {
				
				  {
					 uni.navigateTo({
					 	url: 'videodetail?id='+e.id
					 })
				}
				
			},
			del(id) {
								uni.request({
										url: this.$serverUrl + '/cus/api/videodel', //获取访问令牌
										data: {
											id:  id,
										},
										header: {
											'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
										},
									 
										method: 'POST',
										success: (res) => { //  检验成功返回状态码,访问令牌等参数,使用vuex保存状态,后面请求服务器接口都要带上
											 
											if (res.statusCode == 200) {
												if (res.data.code == 0) {
											uni.showModal({
											content: "删除成功",
											title: '消息提示',
											showCancel: false,
											success: (res) => {
												this.refresh()
											} 
										});
													
												}  
											}
										} 
									})  
			}
		},
		
		onLoad() {

			// 防止app里由于渲染导致转场动画卡顿
			setTimeout(() => {
				this.refresh();
			}, 300);
		}
	}
</script>

<style>
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
 .border-box{
 	border: 3upx solid #007AFF;
 	border-radius: 30upx;
 }
 .uni-text-small{
	 color: red;
	 font-size: 30upx;
	 
 }
</style>
