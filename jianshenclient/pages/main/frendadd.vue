<template>
	<view class="content1">
		<view class="input-group">

			<view class="input-row border">
				<text class="title1">用户名：</text>
				<m-input type="text" clearable v-model="content" placeholder="请输入用户名"></m-input>
			</view>
			<view class="btn-row">
				<button type="default" class="uni-badge--small" @tap="refresh()">查询</button>
			</view>
		</view>
		<view class="uni-list">
			<view class="uni-list-cell" hover-class="uni-list-cell-hover" v-for="(item,key) in list" :key="key" @tap="goDetail(key)">
				<view class="uni-media-list  " style="float: left; ">
					<view style="  width: 100%;">


						<view class="uni-media-list  ">
							<view class="uni-media-list-logo">
								<image :src="item.pic"></image>
							</view>
							<view class="rowright">
								<view class="uni-text-small" style="width: 100%;">昵称：{{item.nickname}}</view>
								<view class="uni-text-small" style="width: 100%;">电话：{{item.phone}}</view>
							</view>
						</view>
					</view>
				</view>

			</view>
		</view>
	</view>
</template>
<script>
	import uniGrid from '@/components/uni-grid/uni-grid.vue'
	import uniGridItem from '@/components/uni-grid-item/uni-grid-item.vue'
	import uniSection from '@/components/uni-section/uni-section.vue'
	import uniTag from '@/components/uni-tag/uni-tag.vue'

import mInput from '../../components/m-input.vue';
	
	import {
		mapState
	} from 'vuex'


	export default {
		computed: mapState(['forcedLogin', 'hasLogin', 'muser']),
		components: {
			uniGrid,
			uniGridItem,
			uniSection,
			uniTag,mInput
	

		},
		data() {
			return {
				inverted1: true,
				inverted2: false,
				inverted3: false,
				choseindex: -1,
				refreshing: false,
				list: [],
				fetchPageNum: 1,
				loadall: false,
				content: ''
			}
		},
		onLoad() {

			this.refresh();
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
				console.log("searchname:",this.content);
				uni.request({
					url: this.$serverUrl + '/cus/user/list',
					method: 'post',
					header: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
					data: {
						userId: this.muser.id,
						page: this.fetchPageNum,
						limit: 10,
						nickname: this.content,

					},
					success: res => {
						if (res.statusCode == 200) {
							if (res.data.code == 0) {
								let data = res.data.rows;
								console.log("searchfriend:",data);
								if (data.length == 0) {
									uni.showToast({
										title: "没有更多数据了",
										icon: "none"

									});
									return;
								}
								for (let i = 0, length = data.length; i < length; i++) {
									let pic = '../../static/logo.png';
									if (data[i].pic != null) {
										pic = that.$serverPicUrl + data[i].pic;
									}
									that.list.push({
										id: data[i].id,
										nickname: data[i].nickname,

										phone: data[i].phone,

										pic: pic,
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
					url: './frenddetail?id=' + this.list[e].id
				});

			},
			addrecord() {


			 

			},
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
	}
</script>

<style>
	.pic {
		float: left;
		width: 60upx;
		height: 60upx;
		min-height: 60upx;
		min-width: 60upx;


		line-height: 60upx;
	}

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
		background-color: red;
		flex-direction: row;
		align-items: center;
	}

	.logo-hover {
		opacity: 0.8;
	}

	.logo-img {
		width: 100upx;
		height: 100upx;
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
		color: #FFFFFF;
	}

	.uer-name1 {
		height: 20upx;
		line-height: 20upx;
		font-size: 18upx;
		color: #FFFFFF;
	}.input-row .title1 {
		width: 40%;
		height: 70upx;
		min-height: 70upx;
		padding: 15upx 0;
		padding-left: 30upx;
		line-height: 70upx;
	}
	.input-row.border::after {
		position: absolute;
		right: 0;
		bottom: 0;
		left: 15upx;
		height: 1upx;
		content: '';
		-webkit-transform: scaleY(.5);
		transform: scaleY(.5);
		background-color: #c8c7cc;
	}

	.btn-row {
		margin-top: 10upx;
		padding: 20upx;
	}

	button.primary {
		background-color: #0faeff;
	}
</style>
