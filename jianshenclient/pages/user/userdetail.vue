<template>
	<view class="content">
		
		<view class="uni-media-list">
			<view class="rowright">
				<view class="input-row  ">
					<text class="user-title">原头像：</text>
					<view ><image class="logo-img" :src="avatarUrl"></image></view>
					<text class="user-title">新头像：</text>
					<view class="uni-uploader__input-box">
						<block v-if="imageSrc">
							<image :src="imageSrcshow" class="image" mode="aspectFill"></image>
							<view class="close-view" @click="close">×</view>
						</block>
						<block v-else>
							<view class="uni-uploader__input" @click="chooseImage"></view>
						</block>
					</view>
		
				</view>
		
			</view>
		</view>
		<view class="input-group">
			<view class="input-row border">
				<text class="title">账号：</text>
				<label class="title" type="text" clearable focus v-model="account" placeholder="请输入账号">{{account}}</label>
			</view>
			<view class="input-row">
				<text class="title1">旧密码：</text>
				<m-input type="password" displayable v-model="pass" placeholder="请输入旧密码"></m-input>
			</view>
			<view class="input-row">
				<text class="title1">新密码：</text>
				<m-input type="password" displayable v-model="password" placeholder="请输入新密码"></m-input>
			</view>
			<view class="input-row border">
				<text class="title1">确认密码：</text>
				<m-input type="password" displayable v-model="password1" placeholder="确认密码"></m-input>
			</view>
			<view class="input-row border">
				<text class="title">手机号：</text>
				<m-input class="m-input" type="text" clearable focus v-model="phone" placeholder="请输入手机号"></m-input>
			</view>

			<view class="input-row border">
				<text class="title">姓名：</text>
				<m-input class="m-input" type="text" clearable focus v-model="realname" placeholder="请输入姓名"></m-input>
			</view>
			<view class="input-row border" @tap="onShowDatePicker(1,'date')">
				<text class="title">生日：</text>
				<text class="title1">{{day}}</text></view>
			<view class="input-row border">
				<text class="title">地址：</text>
				<m-input class="m-input" type="text" clearable focus v-model="address" placeholder="请输入地址"></m-input>
			</view>
		</view>
		<view class="btn-row">
			<button type="primary" class="primary" @tap="bindLogin">修改</button>
		</view>
		<view class="action-row">

		</view>
		<mx-date-picker :show="showDatePicker" :type="type" :format="format" :value="value" :show-tips="true" :begin-text="''"
		 :end-text="''" :show-seconds="true" @confirm="onSelected" @cancel="onSelected" />

	</view>
</template>

<script>
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";

	import service from '../../service.js';
	var dateutil = require('../../components/date.js');
	import {
		mapState,
		mapMutations
	} from 'vuex'
	import mInput from '../../components/m-input.vue'

	export default {
		components: {
			mInput,MxDatePicker
		},
		data() {
			return {
				avatarUrl: '../../static/logo.png',
				providerList: [],
				hasProvider: false,
				account: '',
				password: '',
				password1: '',
				phone: '',
				positionTop: 0,
				openId: '',
				email: '',
				realname: '', 
						showDatePicker: false,
				type: 'datetime',
				value: '',
				format: 'yyyy-mm-dd',
				formatTime: 'hh:ii',
				index: 0,
				day: dateutil.formatDate(new Date(), "yyyy-MM-dd"),
				address: '',
				imageSrc:'',
				imageSrcshow:'',
			}
		},
		computed: mapState(['forcedLogin', 'hasLogin', 'muser']),
		methods: {
			...mapMutations(['login']),
			onShowDatePicker(index, type) { //显示

				this.index = index;
				this.type = type;
				this.showDatePicker = true;
				this.value = this[type];
			},
			onSelected(e) { //选择

				this.showDatePicker = false;


				if (e) {

					this.day = e.value;


				}

			},
			initPosition() {
				/**
				 * 使用 absolute 定位，并且设置 bottom 值进行定位。软键盘弹出时，底部会因为窗口变化而被顶上来。
				 * 反向使用 top 进行定位，可以避免此问题。
				 */
				this.positionTop = uni.getSystemInfoSync().windowHeight - 100;
			},
			bindLogin() {
				var that = this;
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

				if (this.realname.length < 1) {
					uni.showToast({
						icon: 'none',
						title: '姓名不能为空'
					});
					return;
				}

				if (this.phone.length < 11) {
					uni.showToast({
						icon: 'none',
						title: '手机号为 11 个字符'
					});
					return;
				}
				if (this.pass == null ){
					uni.showToast({
						icon: 'none',
						title: '修改信息需要输入密码'
					});
					return;
				}
				if(this.pass != this.muser.password) {
					uni.showToast({
						icon: 'none',
						title: '密码输入错误'
					});
					return;
				}
				if (this.password.length>0){
					if(this.password.length < 4) {
						uni.showToast({
							icon: 'none',
							title: '密码不能少于4位'
						});
						return;
					}
				}
				if (this.password != this.password1) {
					uni.showToast({
						icon: 'none',
						title: '两次密码不对'
					});
					return;
				}
				if (this.imageSrc.length<1) {
					this.imageSrc = this.avatarUrl.substring(16,this.avatarUrl.length);
					this.imageSrcshow = '',
					console.log("图片路径：",this.avatarUrl);
					console.log("图片路径：",this.imageSrc);
				}

				uni.request({

					url: this.$serverUrl + '/cus/api/user/update', //获取访问令牌
					data: {
						username: this.account,
						password: this.password,
						openId: this.openId,
						phone: this.phone,
						email: this.email,
						realname: this.realname,
						address: this.address,
						birthday: this.day,
						userId: this.muser.id,
						pic: this.imageSrc
					},
					header: {

						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
					method: 'POST',
					success: (res) => { //  检验成功返回状态码,访问令牌等参数,使用vuex保存状态,后面请求服务器接口都要带上
						console.log(res);
						if (res.statusCode == 200) {
							if (res.data.code == 0) {
								uni.showModal({
									content: "修改成功",
									title: '消息提示',
									showCancel: false,
									success: (res) => {

									},
									complete() {
										that.login(res.data.data);
										uni.reLaunch({
											url: 'user',
										});
									}
								});

							} else {
								uni.showToast({
									icon: 'none',
									title: '修改异常',
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
			chooseImage: function() {
				var that = this;
				uni.chooseImage({
					count: 1,
					sizeType: ['compressed'],
					sourceType: ['album'],
					success: (res) => {
						console.log('chooseImage success, temp path is', res.tempFilePaths[0])
						var imageSrc = res.tempFilePaths[0]
						uni.uploadFile({
							url: that.$serverUrl + "/cus/api/upload",
							filePath: imageSrc,
							name: 'file',
							success: (res) => {
							 
								uni.showToast({
									title: '上传成功',
									icon: 'success',
									duration: 1000
								})
								var data = JSON.parse(res.data); //转换为json对象
								this.imageSrc = data.url;
								this.imageSrcshow = this.$serverPicUrl+data.url;
								this.pic = data.url;
								
							},
							fail: (err) => {
								console.log('uploadImage fail', err);
								uni.showModal({
									content: err.errMsg,
									showCancel: false
								});
								
							}
						});
					},
					fail: (err) => {
						console.log('chooseImage fail', err)
					}
				})
				},
				close(){
					this.imageSrc='',
					this.imageSrcshow='',
					this.pic=''
				},
			toMain(userName) {
				this.login(userName);
				/**
				 * 强制登录时使用reLaunch方式跳转过来
				 * 返回首页也使用reLaunch方式
				 */
				if (this.forcedLogin) {
					uni.reLaunch({
						url: '../main/main',
					});
				} else {
					uni.navigateBack();
				}

			}
		},
		onReady() {
			this.initPosition();
			this.realname = this.muser.nickname;
			this.email = this.muser.email;
			this.phone = this.muser.phone;
			this.account = this.muser.username;
			this.address = this.muser.address;
			this.day = this.muser.day;
			if (this.muser.pic != null) {
				this.avatarUrl = this.$serverPicUrl + this.muser.pic;
			}
			if (this.muser.birthday != null) {
				this.day =   this.muser.birthday;
			}

		},
		onLoad: function(option) { //option为object类型，会序列化上个页面传递的参数
			// #ifdef MP-WEIXIN

			this.openId = option.openId;
			// #endif


		}
	}
</script>

<style>
	.input-row .title1 {
		width: auto;
		height: 70upx;
		min-height: 70upx;
		min-width: 200upx;
		padding: 15upx 0;
		padding-left: 30upx;
		line-height: 70upx;
	}

	.action-row {
		display: flex;
		flex-direction: row;
		justify-content: center;
	}

	.action-row navigator {
		color: #007aff;
		padding: 0 20upx;
	}

	.logo {


		padding: 20px;
		box-sizing: border-box;
		background-color: red;
		flex-direction: row;
		align-items: center;
	}

	.logo-hover {
		opacity: 0.8;
	}

	.logo-img {
		width: 200upx;
		height:200upx;
		
		align-self: center;
	}

	.logo-title {
		height: 100upx;
		flex: 1;
		align-items: center;
		justify-content: flex-start;
		flex-direction: row;
		margin-left: 20upx;
	}
	.close-view{
	    text-align: center;
		line-height:30upx;
		height: 35upx;
		width: 35upx;
		background: #ef5350;
		color: #FFFFFF;
		position: absolute;
		top: 1upx;
		right: 1upx;
		font-size: 35upx;
		border-radius: 8upx;
	}
	.image {
		width: 100%;
		height: 200upx;
	}
</style>
