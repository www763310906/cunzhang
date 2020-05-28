<template>
	<view class="content">
		<view class="input-group">


			<view class="uni-media-list">
				<view class="rowright  ">
					<text class="title">视频名称：</text>
					<m-input class="m-input" type="text" clearable focus v-model="name" placeholder="请输入视频名称"></m-input>

				</view>
			</view>
		</view>
		<view class="input-group">


			<view class="uni-media-list">
				<view class="rowright  ">
					<text class="title">视频介绍：</text>
					<textarea placeholder-style="color:#F76260" placeholder="输入介绍" v-model="content"> </textarea>
				</view>


			</view>
		</view>
		<view class="input-group">


			<view class="uni-media-list">
				<view class="rowright">

					<view class="input-row  ">
						<text class="title">图片：</text>
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
		</view>
		<view class="input-group">


			<view class="uni-media-list">

				<view class="rowright">

					<view class="input-row  ">
						<text class="title">视频：</text>
						<view class="uni-uploader__input-box">
							<block v-if="video">
							<video class="image" :src="videoSrcshow" :controls="true" :loop="false"></video>
							<view class="close-view" @click="close">×</view>
							</block>
							<block v-else>
								<view class="uni-uploader__input" @click="choosevideo"></view>
							</block>
						</view>

					</view>

				</view>


			</view>

		</view>
		 
		<view class="btn-row">
			<button type="primary" class="primary" @tap="add()">提交</button>
		</view>




	</view>
</template>

<script>
	import service from '../../service.js';
	import mInput from '@/components/m-input.vue';
	import mIcon from '@/components/m-icon/m-icon.vue';
	var dateutil = require('../../components/date.js');
	import {
		mapState
	} from 'vuex'
	export default {
		computed: mapState(['forcedLogin', 'hasLogin', 'muser']),
		components: {
			mInput,
			mIcon,
		},
		data() {
			return {
				video:'',
				videoSrcshow:'',
				imageSrc: '',
				imageSrcshow: '',
				video: '',
				name: '',
				content: '',
				ctype: '1',
			}
		},
		methods: {
			change(evt) {
				console.log(evt.target.value);
				this.ctype = evt.target.value;
			},
			close(){
				this.imageSrc='',
				this.imageSrcshow='',
				this.pic='',
				this.video='',
				this.videoSrcshow=''
				
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
								this.imageSrcshow=this.$serverPicUrl+data.url;
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
			choosevideo: function() {
				var that = this;
				uni.chooseImage({
					count: 1,
					sizeType: ['compressed'],
					sourceType: ['album'],
					success: (res) => {
						console.log('chooseVideo success, temp path is', res.tempFilePaths[0])
						var imageSrc = res.tempFilePaths[0]
						uni.uploadFile({
							url: that.$serverUrl + "/cus/api/upload",
							filePath: imageSrc,
							name: 'file',
							success: (res) => {
								console.log('uploadVideo success, res is:', res.data.url)
								uni.showToast({
									title: '上传视频成功',
									icon: 'success',
									duration: 1000
								})
								var data = JSON.parse(res.data); //转换为json对象
							 
								that.video = data.url;
								that.videoSrcshow =this.$serverPicUrl+data.url;
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
			add() {
				if (this.name.length <= 0) {
					uni.showModal({
						content: "请填写名称",
						title: '消息提示',
						showCancel: false,
						success: (res) => {

						}
					});
					return;

				}
				if (this.content.length <= 0) {
					uni.showModal({
						content: "请填写介绍",
						title: '消息提示',
						showCancel: false,
						success: (res) => {

						}
					});
					return;

				}

				var that = this;
				uni.request({
					url: that.$serverUrl + '/cus/api/videoadd', //获取访问令牌
					method: 'post',
					header: {

						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},

					data: {
						name: that.name,
						content: that.content,
						pic: that.imageSrc,
						video: that.video,
						userId: that.muser.id,
						free: 1
					},
					success: res => {
						console.log(res);
						if (res.statusCode == 200) {
							if (res.data.code == 0) {
								uni.showToast({
									title: "发布成功"

								});

								setTimeout(function() {
									uni.hideToast();
									uni.navigateBack({
										delta: 1
									})
								}, 1000);;

							} else {
								uni.showToast({
									title: res.data.msg,
									icon: "none"
								});


							}
						}
					}

				});

			}

		},
		onBackPress() {

		},
		onLoad(option) {

		}

	};
</script>

<style>
	.title1 {
		padding: 10upx;
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
		position: relative
	}

	.example .example-title {
		margin: 40upx 0
	}

	.example-body {
		padding: 0 40upx
	}

	.spacebetween {
		justify-content: space-between;
	}

	.mclear {
		font-family: uniicons;
		font-size: 48upx;
		font-weight: normal;
		font-style: normal;
		line-height: 1;
		display: inline-block;
		text-decoration: none;
		-webkit-font-smoothing: antialiased;
	}

	.input-row1 {
		display: flex;
		flex-direction: row;
		justify-content: center;
	}

	.cinput {
		width: auto;
		height: auto;
		min-height: 30upx;
		min-width: 200upx;
		padding: 5upx 0;

		line-height: 30upx;
	}

	.scan {
		width: 70upx;
		height: 70upx;
		min-height: 70upx;
		min-width: 70upx;
		padding: 15upx 0;

		line-height: 70upx;
	}

	.input-row .title1 {
		width: auto;
		height: 70upx;
		min-height: 70upx;
		min-width: 200upx;
		padding: 15upx 0;
		padding-left: 30upx;
		line-height: 70upx;
	}

	.input-row .title2 {
		width: auto;

		min-height: 70upx;

		padding: 15upx 0;
		padding-left: 30upx;

	}

	.heightcenter {
		width: auto;
		height: auto;
		min-height: 30upx;
		min-width: 84upx;

		padding: 5upx 0;

		line-height: 30upx;
	}

	.rowright {
		flex-direction: row;
		width: 100%;
	}

	.mclear {
		font-family: uniicons;
		font-size: 48upx;
		font-weight: normal;
		font-style: normal;
		line-height: 1;
		display: inline-block;
		text-decoration: none;
		-webkit-font-smoothing: antialiased;
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
		position: relative
	}

	.example .example-title {
		margin: 40upx 0
	}

	.example-body {
		padding: 0 40upx
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
