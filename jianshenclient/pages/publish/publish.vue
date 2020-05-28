<template>
	<view class="page" @touchstart="touchStart" @touchend="touchEnd">
		<form>
			<view class="uni-textarea">
				<textarea placeholder="这一刻的想法..." v-model="input_content" />
				</view>
			<view class="uni-list list-pd">
				<view class="uni-list-cell cell-pd">
					<view class="uni-uploader">
						<view class="uni-uploader-head">
							<view class="uni-uploader-title"></view>
							<view class="uni-uploader-info">{{imageList.length}}/9</view>
						</view>
						<view class="uni-uploader-body">
							<view class="uni-uploader__files">
								<block v-for="(image,index) in imageList" :key="index">
									<view class="uni-uploader__file" style="position: relative;">
										<image class="uni-uploader__img" mode="aspectFill" :src="image" :data-src="image" @tap="previewImage"></image>
										<view class="close-view" @click="close(index)">×</view>
									</view>
								</block>
								<view class="uni-uploader__input-box" v-show="imageList.length < 9">
									<view class="uni-uploader__input" @tap="chooseImage"></view>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
			
			<view class="footer">
				<button type="default" class="feedback-submit" @click="publish">提交</button>
			</view>
		</form>
	</view>
</template>

<script>
	import image from '@/common/image.js';
	import {
		mapState,
		mapMutations
	} from 'vuex'
	var sourceType = [
		['camera'],
		['album'],
		['camera', 'album']
	]
	var sizeType = [
		['compressed'],
		['original'],
		['compressed', 'original']
	]
	export default {
		computed: mapState(['forcedLogin', 'hasLogin', 'muser']),
		
		data() {
			return {
				
				input_content:'',
				imageList: [],
				uploadPath: '',
				sourceTypeIndex: 2,
				sourceType: ['拍照', '相册', '拍照或相册'],
				sizeTypeIndex: 2,
				sizeType: ['压缩', '原图', '压缩或原图'],
				countIndex: 8,
				count: [1, 2, 3, 4, 5, 6, 7, 8, 9],
				
				//侧滑返回start
				startX: 0, //点击屏幕起始位置
				movedX: 0, //横向移动的距离
				endX: 0, //接触屏幕后移开时的位置
				//end
			}
		},
		onUnload() {
			this.imageList = [],
				this.sourceTypeIndex = 2,
				this.sourceType = ['拍照', '相册', '拍照或相册'],
				this.sizeTypeIndex = 2,
				this.sizeType = ['压缩', '原图', '压缩或原图'],
				this.countIndex = 8;
		},
		
		methods: {
			
			async publish() {
				 
				if (this.input_content.length <= 0) {
					uni.showModal({
						content: "请填写内容",
						title: '消息提示',
						showCancel: false,
						success: (res) => {
			
						}
					});
					return;
			
				}
				for (let i = 0; i < this.imageList.length; i++) {
					console.log("上传中：" + i);
			
					try {
						await this.upload(this.imageList[i]); // 关键 
					} catch (err) {
						console.log(err)
					}
				}
			
			
				var that = this;
				if(that.uploadPath=="")that.uploadPath="null";
				uni.request({
					url: that.$serverUrl + '/cus/hui/save', //获取访问令牌
					method: 'post',
					dataType: "json",
					header: {
			
					//	'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
			
					data: {
			
						pic: that.uploadPath,
						name: that.input_content,
						content: that.input_content,
			
			
						userId: that.muser.id,
						 
					},
					success: res => {
						console.log(res);
						if (res.statusCode == 200) {
							if (res.data.code == 0) {
								uni.showToast({
									title: "发布成功"
								});
								uni.reLaunch({
									url: '../main/home',
								});
			
			
							} else {
								uni.showToast({
									title: res.data.msg,
									icon: "none"
								});
			
			
							}
						}
					}
			
				});
			
			},
			upload: function(imageSrc) {
				return new Promise((resolve, reject) => {
					uni.uploadFile({
						url: this.$serverUrl + '/cus/api/upload', //获取访问令牌,
						filePath: imageSrc,
						name: 'file',
						/* 	files:this.imgList , */
						fileType: 'image',
						success: (res) => {
							console.log(res);
							var data = JSON.parse(res.data); //转换为json对象
							this.uploadPath = this.uploadPath + data.url + ',';
							resolve('suc');
			
			
						},
						fail: (err) => {
							console.log('uploadImage fail', err);
							// uni.showModal({
							// 	content: err.errMsg,
							// 	showCancel: false
							// });
							reject('err')
			
						}
					});
				});
			}, 
			close(e){
			    this.imageList.splice(e,1);
			},
			chooseImage: async function() {
				if (this.imageList.length === 9) {
					let isContinue = await this.isFullImg();
					console.log("是否继续?", isContinue);
					if (!isContinue) {
						return;
					}
				}
				uni.chooseImage({
					sourceType: sourceType[this.sourceTypeIndex],
					sizeType: sizeType[this.sizeTypeIndex],
					count: this.imageList.length + this.count[this.countIndex] > 9 ? 9 - this.imageList.length : this.count[this.countIndex],
					success: (res) => {

						// #ifdef APP-PLUS
						//提交压缩,因为使用了H5+ Api,所以自定义压缩目前仅支持APP平台
						var compressd = cp_images=> {
							this.imageList = this.imageList.concat(cp_images)//压缩后的图片路径
						}
						image.compress(res.tempFilePaths,compressd);
						// #endif
						
						// #ifndef APP-PLUS
						this.imageList = this.imageList.concat(res.tempFilePaths)//非APP平台不支持自定义压缩,暂时没有处理,可通过uni-app上传组件的sizeType属性压缩
						// #endif
						
						 
						
					}
				})
			},
			isFullImg: function() {
				return new Promise((res) => {
					uni.showModal({
						content: "已经有9张图片了,是否清空现有图片？",
						success: (e) => {
							if (e.confirm) {
								this.imageList = [];
								res(true);
							} else {
								res(false)
							}
						},
						fail: () => {
							res(false)
						}
					})
				})
			},
			previewImage: function(e) {
				var current = e.target.dataset.src
				uni.previewImage({
					current: current,
					urls: this.imageList
				})
			},
			touchStart: function(e) {
				this.startX = e.mp.changedTouches[0].pageX;
			},
			
			touchEnd: function(e) {
				this.endX = e.mp.changedTouches[0].pageX;
				if (this.endX - this.startX > 200) {
					uni.navigateBack();
				}
			}
		}
	}
</script>

<style scoped>
	
	.footer {
		margin-top: 80upx;
	}
	
	.cell-pd {
		padding: 20upx 30upx;
	}

	.uni-textarea {
		width: auto;
		padding: 20upx 25upx;
		line-height: 1.6;
		height: 150upx;
	}
	.uni-list::before {
		height: 0;
	}
	.uni-list:after {
		height: 0;
	}
	.list-pd {
		margin-top: 0;
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
	.page {
		width: 750upx;
		height: 100%;
	}
</style>
