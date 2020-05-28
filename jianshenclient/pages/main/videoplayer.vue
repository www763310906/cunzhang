<template>
    <view class="page" >
        <swiper class="swiper" :circular="circular" :vertical="true" @change="onSwiperChange" >
            <swiper-item v-for="item in videoDataList" :key="item.id" >
                <video class="video" :id="item.id" :ref="item.id" :src="item.src" :controls="true" :loop="true"
                    :show-center-play-btn="false"></video>
            </swiper-item>
        </swiper> 
		<view class="uni-loadmore" v-if="showLoadMore">{{loadMoreText}}</view>
    </view>
</template>
<script>
    const videoData = [ 
    ];
    export default {
        data() {
            return {
				num:0,
                circular: true,
                videoList: [{
                        id: "video0",
                        src: "",
                        img: ""
                    },
					{
					    id: "video1",
					    src: "",
					    img: ""
					}
					,{
					    id: "video2",
					    src: "",
					    img: ""
					}
                ],
                videoDataList: [],
				list:[{
					id: 0,
					content: ""
				}],
				loadMoreText: "加载中...",
				windowHeight: '', //可用窗口高度(不计入软键盘)
				screenHeight: '', //屏幕高度(系统)
				showLoadMore:false,
				platform: ''
            }
        },
		onUnload() {
			console.log("onUnload"),
			this.loadMoreText = "加载更多",
			this.showLoadMore = false;
		},
		onHide() {
			uni.offWindowResize(); //取消监听窗口尺寸变化
		},
        onLoad(option) {
			uni.getSystemInfo({ //获取设备信息
				success: (res) => {
					this.screenHeight = res.screenHeight;
					this.platform = res.platform;
					console.log("hello");
				}
			});
			uni.startPullDownRefresh();
		},
		
        onReady() {
            this.init();
            this.getData();
        },
        methods: {
            init() {
                this._videoIndex = 0;
                this._videoContextList = [];
                for (var i = 0; i < this.videoList.length; i++) {
                    this._videoContextList.push(uni.createVideoContext('video' + i, this));
                }
                this._videoDataIndex = 0;
            },
            getData( ) {
				var that = this;
				that.list=[];
				that.videoDataList=[];
				uni.request({
					url: this.$serverUrl + '/cus/api/videolistrandom',
					method: 'post',
					header: {
					'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
					data: {
						
					},
					success: res => {
						if (res.statusCode == 200) {
							if (res.data.code == 0) {
								let data = res.data.data;
								console.log("videolistplayer:",data);
								// let ranlength = data.length;
								// let random = data.sort(function(){
								// 	return 0.5-Math.random();
								// });
								// console.log("random:",random);
								// for (let n = 0, length = data.length; n < length; n++) {
								// that.list.push({
								// 	"id":n,
								// 	"content":random[n]
								// 	});
								// }
								for (let n = 0, length = data.length; n < length; n++) {
										that.list.push({
											"id":that.num,
											"content":data[n]
										});
										that.videoDataList.push({
											"id":that.num,
											"src":that.$serverPicUrl+data[n].video
										} );
										this.num++;
										console.log(that.$serverPicUrl+ data[n].video);
										console.log("num:",that.num);
									}
								console.log("randomlist:",that.list);
								
								console.log("videoDataList:",that.videoDataList);
							} 
						}
				
					} 
				});
            },
             onSwiperChange(e) {
                 let currentIndex = e.detail.current;
				 console.log("change:",e.detail.current);
				  console.log("length:",this.videoDataList.length);
					if(currentIndex == 0){
						console.log("刷新");
						this.getData();
					}
            //     if (this._videoDataIndex < 0) {
            //         this._videoDataIndex = this.videoDataList.length - 1;
            //     } else if (this._videoDataIndex >= this.videoDataList.length) {
            //         this._videoDataIndex = 0;
            //     }
            //     this.circular = (this._videoDataIndex != 0);
            //     if (this._videoIndex >= 0) {
            //         this._videoContextList[this._videoIndex].pause();
            //         this._videoContextList[this._videoIndex].seek(0);
            //     }
            //     this._videoIndex = currentIndex;
            //      setTimeout(() => {
            //          this.updateVideo(isNext);
            //     }, 1000);
			
					
            },
            // getNextIndex(isNext) {
            //     let index = this._videoIndex + (isNext ? 1 : -1);
            //     if (index < 0) {
            //         return this.videoList.length - 1;
            //     } else if (index >= this.videoList.length) {
            //         return 0;
            //     }
            //     return index;
            // },
            // getNextDataIndex(isNext) {
            //     let index = this._videoDataIndex + (isNext ? 1 : -1);
            //     if (index < 0) {
            //         return this.videoDataList.length - 1;
            //     } else if (index >= this.videoDataList.length) {
            //         return 0;
            //     }
            //     return index;
            // },
            // updateVideo(isNext) {
            //     this.$set(this.videoList[this._videoIndex], 'src', this.videoDataList[this._videoDataIndex].src);
            //     this.$set(this.videoList[this.getNextIndex(isNext)], 'src', this.videoDataList[this.getNextDataIndex(isNext)].src);
            //     setTimeout(() => {
            //         this._videoContextList[this._videoIndex].play();
            //     }, 200);
            //     console.log("v:" + this._videoIndex + " d:" + this._videoDataIndex + "; next v:" + this.getNextIndex(
            //         isNext) + " next d:" + this.getNextDataIndex(isNext));
            //  }
			
			
        }
    }
</script>

<style>
    /* #ifndef APP-PLUS */
    page {
        width: 100%;
        min-height: 100%;
        display: flex;
    }
    /* #endif */
    .page {
        flex: 1;
      width: 100%;
      min-height: 100%;
    }
    .swiper {
    flex: 1;
	width: 100%;
	min-height: 100%;
    background-color: white;
    }
    .swiper-item {
        flex: 1;
    }
    .video {
        flex: 1;
		height: 100%;
        width: 100%;
		margin: 0 auto;
		position: relative;
		top: 300upx;
		margin-top: -300upx;
    }
	.videoplayer{
		background-color: black;
		width: 100%;
		height: 100%;
		margin: 0;
		padding: 0;
		
	}
</style>