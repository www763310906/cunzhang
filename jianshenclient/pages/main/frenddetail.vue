<template>
	<view class="content1">
		<view class="logo">

			<image class="logo-img" :src="avatarUrl"></image>
			<view class="logo-title">
				<text class="uer-name">
					用户： {{item.nickname}}
				</text>

			</view>
			<view class="logo-title">
				<text class="uer-name">
					生日： {{item.birthday}}
				</text> </view>
			<view class="logo-title">
				<text class="uer-name">
					地址： {{item.address}}
				</text>
			</view>
		</view>
		<view class="example-body">
			<view class="button-sp-area">
				<button class="mini-btn" type="primary" @click="touser">添加好友</button>
				<button class="mini-btn" type="primary" @click="fabu1">对话</button>

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


				item: [],
				frendId: ''



			};
		},
		onLoad(option) {
			var that = this;
			this.frendId = option.id;
			uni.request({
				url: this.$serverUrl + '/cus/user/info/' + this.frendId,
				method: 'post',
				header: {

					'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
				},
				data: {

				},
				success: res => {

					if (res.statusCode == 200) {
						if (res.data.code == 0) {
							let data = res.data.user;
							that.item = data;
							if (data.pic != null)
								that.avatarUrl = that.$serverPicUrl + data.pic;

						}
					}

				}
			});



		},
		onShow() {

		},
		methods: {
			touser() {
				var that = this;
				uni.request({
					url: that.$serverUrl + '/cus/userrelation/save', //获取访问令牌
					method: 'post',
					header: {

						//	'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},

					data: {

						userId: this.muser.id,
						frendId: this.frendId,
					},
					success: res => {
						console.log(res);
						if (res.statusCode == 200) {
							if (res.data.code == 0) {
								uni.showToast({
									title: "添加成功"
								});
							} else {
								uni.showToast({
									title: "已经是好友了",
									icon: "none"
								});


							}
						}
					}

				});
			},
			fabu1() {
				console.log("go");
				uni.navigateTo({
					url: "../main/chat?frendId="+this.frendId
				});
			}



		}
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
		border-radius: 100upx;
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
