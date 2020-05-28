<template>
	<view class="content">
		<view class="input-row ">
			<text class="logoTitle">Welcome to SNGU</text>			
		</view>
		<view class="login-input-group">
		<view class="input-group">
			<view class="input-row border">
				<text class="title">账号：</text>
				<m-input class="m-input" type="text" clearable focus v-model="account" placeholder="请输入账号"></m-input>
			</view>
			<view class="input-row">
				<text class="title">密码：</text>
				<m-input type="password" displayable v-model="password" placeholder="请输入密码"></m-input>
			</view>
		</view>
		<view class="btn-row">
			<button type="primary" class="primary" @tap="bindLogin">登录</button>
		</view>
		<view class="action-row">
			   <navigator url="../reg/reg">注册账号</navigator>
          <!--  <text>|</text>
            <navigator url="../pwd/pwd">忘记密码</navigator> -->
		</view>
		</view>
	</view>
</template>

<script>
	import service from '../../service.js';
	import {
		mapState,
		mapMutations
	} from 'vuex'
	import mInput from '../../components/m-input.vue'

	export default {
		components: {
			mInput
		},
		data() {
			return {
				providerList: [],
				hasProvider: false,
				account: '',
				password: '',
				positionTop: 0,
				openId: '',
			}
		},
		computed: mapState(['forcedLogin']),
		methods: {
			...mapMutations(['login']),

			initPosition() {
				/**
				 * 使用 absolute 定位，并且设置 bottom 值进行定位。软键盘弹出时，底部会因为窗口变化而被顶上来。
				 * 反向使用 top 进行定位，可以避免此问题。
				 */
				this.positionTop = uni.getSystemInfoSync().windowHeight - 100;
			},
			bindLogin() {

				/**
				 * 客户端对账号信息进行一些必要的校验。
				 * 实际开发中，根据业务需要进行处理，这里仅做示例。
				 */
				if (this.account.length < 4) {
					uni.showToast({
						icon: 'none',
						title: '账号最短为 4 个字符'
					});
					return;
				}
				if (this.password.length < 4) {
					uni.showToast({
						icon: 'none',
						title: '密码最短为 4 个字符'
					});
					return;
				}

				/**
				 * 下面简单模拟下服务端的处理
				 * 检测用户账号密码是否在已注册的用户列表中
				 * 实际开发中，使用 uni.request 将账号信息发送至服务端，客户端在回调函数中获取结果信息。
				 */
				const data = {
					grant_type: 'password',
					client_id: 1,
					client_secret: '数据库中client_secret', //同上
					username: this.account,
					password: this.password,
				};
				//                 const validUser = service.getUsers().some(function (user) {
				//                     return data.account === user.account && data.password === user.password;
				//                 });

				uni.request({
					url: this.$serverUrl + '/cus/api/user/login', //获取访问令牌

					data: {
						username: this.account,
						password: this.password,
						openId: this.openId,
					},
					header: {

						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
					// dataType:"json",
					method: 'POST',
					success: (res) => { //  检验成功返回状态码,访问令牌等参数,使用vuex保存状态,后面请求服务器接口都要带上
						console.log(res);
						if (res.statusCode == 200) {
							if (res.data.code == 0) {

								this.login(res.data.data);
								uni.reLaunch({
									url: '../main/home',
								});
							} else {
								uni.showToast({
									icon: 'none',
									title: '用户账号或密码不正确',
								});
							}
						}
					},
					fail() {
						uni.showToast({
							icon: 'none',
							title: '无法连接服务器',
						});
					}
				})


			},

			toMain(userName) {
				this.login(userName);
				/**
				 * 强制登录时使用reLaunch方式跳转过来
				 * 返回首页也使用reLaunch方式
				 */
				if (this.forcedLogin) {
					uni.reLaunch({
						url: '../main/hui',
					});
				} else {
					uni.navigateBack();
				}

			}
		},
		onReady() {
			this.initPosition();
		},
		onLoad: function(option) { //option为object类型，会序列化上个页面传递的参数
			// #ifdef MP-WEIXIN
			 
			this.openId = option.openId;
			// #endif


		}
	}
</script>

<style>
	.action-row {
		display: flex;
		flex-direction: row;
		justify-content: center;
	}

	.action-row navigator {
		color: #007aff;
		padding: 0 20upx;
	}
	.logoTitle{
		font-size: 75upx;
		text-align: center;
		width: 100%;
		position: relative;
		top: 100upx;
	}
	.login-input-group{
		top: 200upx;
		position: relative;
	}
</style>
