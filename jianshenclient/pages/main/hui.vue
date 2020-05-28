<template>
	<view class="page">

		<view class="uni-product-list">
			<view class="uni-product" v-for="(product,index) in list" :key="index" @click="goDetail(product)">
				<view class="image-view">
					<image lazy-load="true" class="uni-product-image" :src="product.pic"></image>
				</view>
			   <view  v-if="product.name" class="uni-product-title">{{product.name}}</view>  
				<view class="uni-product-price">
					<text class="uni-product-price-favour"></text>
					<text class="uni-product-price-original"></text>
					<!-- <text class="uni-product-tip">{{product.gsize}}</text> -->
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				refreshing: false,
				list: [],
				fetchPageNum: 1,
				loadall: false
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
				uni.request({
					url: this.$serverUrl + '/cus/api/huilist',
					method: 'post',
					header: {

						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
					data: {

						page: this.fetchPageNum,
						limit: 10
					},
					success: res => {

						if (res.statusCode == 200) {
							if (res.data.code == 0) {
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
										pic: that.$serverPicUrl + data[i].pic,
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
					url: 'huidetail?id='+e.id
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
 
</style>
