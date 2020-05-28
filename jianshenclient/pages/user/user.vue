<template>
	<view class="content1">
		<view class="logo">

			<image class="logo-img" :src="avatarUrl"  ></image>
			
			<view class="logo-title">
				<text class="uer-name">
					Hi， {{muser.nickname}}
				</text>


			</view>


			<text class="uer-name" @tap="bindLogout">
				&nbsp;	退出 
			</text>

		</view>
		<view class="example-body">
			<view class="button-sp-area">
				 <button class="mini-btn" type="primary" @click="touser" >我的信息</button>
				 <button class="mini-btn" type="primary" @click="fabu1" >发布视频</button>
				<button class="mini-btn" type="primary" @click="chakan1" >查看视频</button>
				<button class="mini-btn" type="primary" @click="chakan2" >查看博客</button>
			</view>
		</view>
		 
	</view>
</template>

<script>
	import uniSection from '@/components/uni-section/uni-section.vue'
	import mIcon from '../../components/m-icon/m-icon.vue';
	import uniGrid from '@/components/uni-grid/uni-grid.vue'
	import uniGridItem from '@/components/uni-grid-item/uni-grid-item.vue'
	import uniTag from '@/components/uni-tag/uni-tag.vue'

 
	import htmlParser from '@/components/html-parser.js'
	import {
		mapState,
		mapMutations
	} from 'vuex'

	export default {
		computed: mapState(['forcedLogin', 'hasLogin', 'muser']),
		components: {
			mIcon,
			uniSection,
			uniTag,
			uniGrid,
			uniGridItem 
		},
		data() {
			return {
				avatarUrl: '../../static/logo.png',
				openId: '',
				appusername: '',		 
			};
		},
		onLoad() {

			if (!this.hasLogin)
			//	if(uni.getStorageInfoSync("ukey")==null)
			{
				uni.showModal({
					title: '未登录',
					content: '您未登录，需要登录后才能继续',
					/**
					 * 如果需要强制登录，不显示取消按钮
					 */
					showCancel: !this.forcedLogin,
					success: (res) => {
						if (res.confirm) {

							// #ifdef MP-WEIXIN
							uni.reLaunch({
								url: '../main/wxlogin'
							});
							return;
							// #endif
							/**
							 * 如果需要强制登录，使用reLaunch方式
							 */
							if (this.forcedLogin) {
								uni.reLaunch({
									url: '../login/login'
								});
							} else {
								uni.navigateTo({
									url: '../login/login'
								});
							}
						}
					}
				});
			} else {
				if (this.muser.pic != null) {
					this.avatarUrl =this.$serverPicUrl+ this.muser.pic;
					
				}
			}

			 

		},
		onShow() {

		},
		methods: {
			 
		   ...mapMutations(['logout']),
			bindLogout: function() {
				this.logout();
				console.log(this.forcedLogin);
				/**
				 * 如果需要强制登录跳转回登录页面
				 */
				if (this.forcedLogin) {
				    uni.reLaunch({
				        url: '../login/login',
				    });
				}

			},
			 
			touser(e) {
				uni.navigateTo({
					url: 'userdetail?id='+this.muser.id
				})  
			},
			 fabu1(e) {
			 	uni.navigateTo({
			 		url: '../main/videoadd' 
			 	})  
			 },
			chakan1(e) {
				uni.navigateTo({
					url: '../main/myvideo' 
				})  
			},chakan2(e) {
				uni.navigateTo({
					url: '../main/myboke' 
				})  
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
	};
</script>

<style>
	.mclear {

		font-size: 36upx;
		width: 15%;
		line-height: 1;
		display: inline-block;
		text-decoration: none;

	}

	.example-title {
		font-size: 32upx;
		line-height: 32upx;
		color: #777;
		margin: 40upx 25upx;
		display: flex;
		align-self: center;
	}

	.tag-view {
		margin: 1upx 1upx;
		display: inline-block;
	}

	.content1 {
		display: flex;
		flex: 1;
		flex-direction: column;
		background-color: #efeff4;

	}

	.example-body {
		flex-direction: row;
		flex-wrap: wrap;
		align-content: center;
		padding: 0;
		font-size: 14rpx;
		background-color: #ffffff;
		align-self: center;
		width: 100%;
	}

	.hello {
		display: flex;
		flex: 1;
		flex-direction: column;
	}

	.title {
		color: #8f8f94;
		margin-top: 50upx;
	}

	.ul {
		font-size: 30upx;
		color: #8f8f94;
		margin-top: 50upx;
	}

	.ul>view {
		line-height: 50upx;
	}

	button {
		margin-top: 30upx;
		margin-bottom: 30upx;
	}

	.button-sp-area {
		margin: 0 auto;
		width: 60%;
	}

	.mini-btn {
		margin-right: 10upx;
	}


	.logo {
		
		padding: 20px;
		box-sizing: border-box;
		background-color: white;
		flex-direction: row;
		align-items: center;
	}

	.logo-hover {
		opacity: 0.8;
	}

	.logo-img {
		width: 200upx;
		height: 200upx;
		
	}

	.logo-title {
		height: 100upx;
		flex: 1;
		align-items: center;
		justify-content: flex-start;
		flex-direction: row;
		margin-left: 20upx;
	}

	.uer-name {
		height: 60upx;
		line-height: 60upx;
		font-size: 38upx;
		color: black;
	}

	.uer-name1 {
		height: 20upx;
		line-height: 20upx;
		font-size: 18upx;
		color: black;
	}
</style>
