<template>
	<view class="content">
		<view class="input-group">
			<view class="uni-media-list">
				<view class="rowright">
			
					<view class="input-row  ">
						<text class="title">头像：</text>
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
			<view class="input-row border">				
				<text class="title">账号：</text>
				<m-input class="m-input" type="text" clearable focus v-model="account" placeholder="请输入账号"></m-input>
			</view>
			<view class="input-row" border>
				<text class="title1">密码：</text>
				<m-input type="password" displayable v-model="password" placeholder="请输入密码"></m-input>
			</view>
			<view class="input-row"border>
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
			<view class="input-row border"  @tap="onShowDatePicker(1,'date')">
				<text class="title">生日：</text>
					<text class="title1">{{day}}</text></view>
			<view class="input-row border">
				<text class="title">地址：</text>
				<m-input class="m-input" type="text" clearable focus v-model="address" placeholder="请输入地址"></m-input>
			</view>
		</view>
		<view class="btn-row">
			<button type="primary" class="primary" @tap="bindLogin">注册</button>
		</view>
		<view class="action-row">
			<!--   <navigator url="../reg/reg">注册账号</navigator>
            <text>|</text>
            <navigator url="../pwd/pwd">忘记密码</navigator> -->
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
				providerList: [],
				hasProvider: false,
				imageSrc:'',
				imageSrcshow:'',
				pic:'',
				account: '',
				password: '',
				password1: '',
				phone: '',
				positionTop: 0,
				showDatePicker: false,
				
				type: 'datetime',
				value: '',
				format: 'yyyy-mm-dd',
				formatTime: 'hh:ii',
				index: 0,
				day: dateutil.formatDate(new Date(), "yyyy-MM-dd"),
				address: '',
				realname: '',
			}
		},
		computed: mapState(['forcedLogin']),
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
				this.positionTop = uni.getSystemInfoSync().windowHeight - 100;
			},
			close(){
				this.imageSrc='',
				this.imageSrcshow='',
				this.pic=''
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
			bindLogin() {

				 
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
				if (this.address.length < 1) {
					uni.showToast({
						icon: 'none',
						title: '地址不能为空'
					});
					return;
				}
				if (this.day.length < 1) {
					uni.showToast({
						icon: 'none',
						title: '生日不能为空'
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
				if (this.phone.length < 11) {
					uni.showToast({
						icon: 'none',
						title: '手机号为 11 个字符'
					});
					return;
				}

				if (this.password != this.password1) {
					uni.showToast({
						icon: 'none',
						title: '两次密码不对'
					});
					return;
				}



				uni.request({

					url: this.$serverUrl + '/cus/api/user/register', //获取访问令牌


					data: {
						username: this.account,
						password: this.password,
						phone: this.phone,
						pic:this.pic,
						realname: this.realname,
						userType: 1,
						address: this.address,
						birthday: this.day,
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
								uni.showToast({
									icon: 'none',
									title: '注册成功',
								});
								this.login(res.data.data);
								uni.reLaunch({
									url: '../login/login',
								});
							} else {
								uni.showToast({
									icon: 'none',
									title: '用户注册失败，请稍后重试',
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
						url: '../main/main',
					});
				} else {
					uni.navigateBack();
				}

			}
		},
		onReady() {
			this.initPosition();
		},
		onLoad: function(option) {

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
	.image {
		width: 100%;
		height: 200upx;
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
</style>
