<template>
	<view>
		<view class="content" @touchstart="hideEmoji">
			 <view class="chat-username" id="touserName"></view>
			<scroll-view class="msg-list" scroll-y="true" :scroll-with-animation="scrollAnimation" 
				:scroll-top="scrollTop"
				:scroll-into-view="scrollToView">				
				<view class="row" v-for="(row,index) in msgList" :key="index" :id="'msg'+row.id">
					<view v-if="row.uid==myuid" class="time">
					<rich-text :nodes="row.msg.time"></rich-text>
					</view>
					<!-- 自己发出的消息 -->
					<view class="my" v-if="row.uid==myuid">						
						<view class="left">							
							<view v-if="row.type=='text'" class="bubble">								
								<rich-text :nodes="row.msg.content"></rich-text>
							</view>
							<view v-if="row.type=='img'" class="bubble img" @tap="showPic(row)">
								<image :src="row.msg.pic" ></image>
							</view>
						</view>
						<view class="right">							
							<image :src="row.msg.userUrl"></image>
						</view>
					</view>
					<view class="username" v-if="row.uid!=myuid">
						
						<view class="tousertime">{{row.time}}</view>
					</view>
					<!-- 别人发出的消息 -->					
					<view class="other" v-if="row.uid!=myuid">
						
						<view class="left">
							<image :src="row.msg.touserUrl"></image>
						</view>
						<view class="right">
							
							<view v-if="row.type=='text'" class="bubble">
								<rich-text :nodes="row.msg.content"></rich-text>
							</view>
							<!-- <view v-if="row.msg.pic!=null" class="bubble img" @tap="showPic(row)">
								<image :src="row.msg.url" ></image>
							</view> -->
						</view>
					</view>
				</view>
			</scroll-view>
		</view>

		<!-- 底部输入栏-->
		<view class="input-box" :class="showEmji" @touchmove.stop.prevent="discard">			
			<!-- #ifdef H5 -->
			<view class="more" @tap="chooseImage">
				<image class="icon tupian" src="../../static/img/plus.png" style="width: 40px;height: 40px;"></image>
			</view>
			<!-- #endif -->			
			<view class="textbox">
				<view class="text-mode" >
					<view class="box">
						<textarea auto-height="true" v-model="content" id="content" />
						</view>					 
				</view>
			</view>
			<!-- #ifndef H5 -->
			<view class="more" @tap="chooseImage">
				<view class="icon tupian"></view>
			</view>
			<!-- #endif -->
			<label for="textMsg">
			<view class="send" @tap="sendText">
				<view class="btn">
					发送
				</view>
			</view>
			</label>
		</view>
		 
	</view>
</template>

<script>
	import {
		mapState,
		mapMutations
	} from 'vuex'
	
	export default {
		computed: mapState(['forcedLogin', 'hasLogin', 'muser']),
		
		data() {
			return {
				username:'',
				tousername:'',
				//文字消息				
				content:'',
				//消息列表
				scrollAnimation:false,
				scrollTop:0,
				scrollToView:'',
				msgList:[],
				msgImgList:[],
				myuid:0,				
				//表情定义
				showEmji:'',
				emojiList:[
				 ],
				 touser:'',
				 frendId:'' ,
				 uploadPath:''
				 };
		},
		onPullDownRefresh() {
			this.msgList=[];
			this.search();
		},
		onLoad(option) {
			console.log("onLoading");
			  this.frendId=option.frendId;
			  this.myuid=this.muser.id;
			  console.log(this.frendId);
			  this.search();
			 
		},
		methods:{
			 getMsgList(){
			 	// 获取消息中的图片,并处理显示尺寸
			 	for(let i=0;i<list.length;i++){
			 		if(list[i].type=='img'){
			 			list[i] = this.setPicSize(list[i]);
			 			console.log("list[i]: " + JSON.stringify(list[i]));
			 			this.msgImgList.push(list[i].msg.url);
			 		}
			 	}
			 	this.msgList = list;
			 	this.search()
			 },
			//处理图片尺寸，如果不处理宽高，新进入页面加载图片时候会闪
			setPicSize(row){
				let maxW = uni.upx2px(350);
				let maxH = uni.upx2px(350);
				if(row.msg.w>maxW||row.msg.h>maxH){
					let scale = row.msg.w/row.msg.h;
					if(row.msg.w>row.msg.h){
						row.msg.w = maxW;
						row.msg.h = row.msg.w/scale
					}else{
						row.msg.h = maxH;
						row.msg.w = row.msg.h*scale;
					}
				}
				return row;
			},
			// 接受消息(筛选处理)
			screenMsg(msg){
				//从长连接处转发给这个方法，进行筛选处理
				if(msg.uid!=this.myuid){
					uni.vibrateLong();
				}
				switch (msg.type){
					case 'text':
						this.addTextMsg(msg);
						break;
					case 'voice':
						this.addVoiceMsg(msg);
						break;
					case 'img':
						this.addImgMsg(msg);
						break;
				}
				this.$nextTick(function() {
					this.scrollToView = 'msg'+msg.id
				});
			},
			// 选择表情
			chooseEmoji(){
				this.showEmji = this.showEmji=='showEmoji'?'hideEmoji':'showEmoji';
			},
			// 隐藏表情
			hideEmoji(){
				this.showEmji = this.showEmji=='showEmoji'?'hideEmoji':'';
			},
			//添加表情
			addEmoji(em){
				this.textMsg+=em.alt;
			},
			// 发送文字消息
			sendText(){
			 
				if(!this.content){
					return;
				}
				let content = this.replaceEmoji(this.content);
				let msg = {content:content}
				this.sendMsg(msg,'text');
				this.content = '';
			},
			//替换表情符号为图片
			replaceEmoji(str){
				let replacedStr = str.replace(/\[([^(\]|\[)]*)\]/g,(item, index)=>{
					console.log("item: " + item);
					for(let i=0;i<this.emojiList.length;i++){
						let row = this.emojiList[i];
						for(let j=0;j<row.length;j++){
							let EM = row[j];
							if(EM.alt==item){
								//在线表情路径，图文混排必须使用网络路径
								let onlinePath = 'https://s2.ax1x.com/2019/04/12/'
								let imgstr = '<img src="'+onlinePath+this.onlineEmoji[EM.url]+'">';
								console.log("imgstr: " + imgstr);
								return imgstr;
							}
						}
					}
				});
				return '<div style="display: flex;align-items: center;">'+replacedStr+'</div>';
			},
			// 选择图片发送
			chooseImage(){
				var that=this;
				this.hideEmoji();
				uni.chooseImage({
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					success: (res)=>{
						for(let i=0;i<res.tempFilePaths.length;i++){
							uni.getImageInfo({
								src: res.tempFilePaths[i],
								success: (image)=>{
									console.log(image.width);
									console.log(image.height);
									var imageSrc = res.tempFilePaths[i];
									uni.uploadFile({
										url: this.$serverUrl + "/cus/api/upload",
										filePath: imageSrc,
										name: 'file',
										success: (res) => {
											 
											var data = JSON.parse(res.data); //转换为json对象
																		 
											that.uploadPath=data.url;
											console.log("pic send:",that.uploadPath);
											 let msg = {url:that.uploadPath,w:image.width,h:image.height};
											 that.sendMsg(msg,'img');
										},
										fail: (err) => {
											 
										}
									});
									
									
								}
							});
						}
					}
				});
			},
			// 发送消息
			sendMsg(content,type){
				//实际应用中，此处应该提交长连接
				content = this.content;
				console.log("content:",this.content);
				var that = this;
				uni.request({
					url: that.$serverUrl + '/cus/chat/save', //获取访问令牌
					method: 'post',										
					data: {					 
						content: content,					
						pic: type=="img"?that.uploadPath:'',
						touserId: that.frendId,
						userId: that.muser.id,					 
					},
					success: res => {
						console.log(res);
						if (res.statusCode == 200) {
							if (res.data.code == 0) {																
								that.msgList=[];
								that.search();
								setTimeout(
								function(){
									that.scrollTop = document.documentElement.scrollTop;
									that.$nextTick(function() {
										that.scrollTop = 9999;
										that.scrollAnimation = true;
									});									
									console.log("滚动到底部");
									this.screenMsg(content);
									}
								, 100);
								
								
							} 
						}
					}
				
				});						 
			},			
			// 处理文字消息
			addTextMsg(msg){
				this.msgList.push(msg);
			},
			
			// 处理图片消息
			addImgMsg(msg){
				msg = this.setPicSize(msg);
				this.msgImgList.push(msg.msg.url);
				this.msgList.push(msg);
			},
			// 预览图片
			showPic(row){
				uni.previewImage({
					indicator:"none",
					urls:this.msgImgList,
					content: row.msg.pic
				});
			},
			
			discard(){
				return;
			},
			search() { //查看聊天记录
			console.log("开始查看");				
				var that = this;				
				uni.request({
					url: this.$serverUrl + '/cus/api/chatlist',
					method: 'post',					
					data: {												 
						userId:this.muser.id,
						touserId:this.frendId,
					},
					success: res => {			
						if (res.statusCode == 200) {
							if (res.data.code == 0) {
								console.log("res:",res);
								let data = res.data.rows;								
								console.log("total:",data);
								if(data!=null){
								for (let i = 0, length = data.length; i < length; i++) {
								//tohead是发送的图片文件
			                     let tohead= data[i].pic;
								 if(data[i].tohead!=null){
									  tohead=that.$serverPicUrl+tohead;
								 }
								 this.msgImgList.push({
									 
								 });
								 let type="text";
								 console.log("tohead:",tohead);
								 if(tohead!=''){
									  type="img";
								  }else{
									  type="text";
								  }
								  console.log("type:",type);
										that.msgList.push({
										id:data[i].id,
										uid:data[i].userId,
										username:res.data.username,
										tousername:res.data.tousername,
										pic:tohead,
										time:data[i].createTime,
										type:type,										
										userPic:res.data.userPic,
										touserPic:res.data.touserPic,
										msg:{
											username:+res.data.username,
											tousername:+res.data.tousername,
											content:data[i].content,
											time:data[i].createTime,
											userUrl:that.$serverPicUrl+res.data.userPic,
											touserUrl:that.$serverPicUrl+res.data.touserPic,
											pic:that.$serverPicUrl+tohead
											},
										});	
										
										document.getElementById('touserName').innerHTML = res.data.tousername;
									}
									}
								}

								}
								console.log("chatlist:",this.msgList);
								
							// 滚动到底部
							that.$nextTick(function() {
								that.scrollTop = 9999;
								that.scrollAnimation = true;
							});

					},complete() {
						uni.stopPullDownRefresh()
					} 
				});
			},
		}
	}
</script>

<style lang="scss">
page{
	background-color: #e5e5e5;
}

	.icon {
		font-size:56upx;
		color: #333;
	}
.hidden{
	display: none !important;
}
@keyframes showEM {
	0% {transform: translateY(0);}
	100% {transform: translateY(-42vw);}
}
@keyframes hideEM {
	0% {transform: translateY(-42vw);}
	100% {transform: translateY(0);}
}
.emoji-box{
	&.hideEmoji{animation: hideEM .15s linear both;}
	&.showEmoji{animation: showEM .15s linear both;}
	width: 96%;
	height: 42vw;
	padding: 20upx 2%;
	background-color: #f3f3f3;
	border-top: solid 1upx #ddd;
	position: fixed;
	z-index: 20;
	top: 100%;
	.swiper{
		swiper-item{
			display: flex;
			flex-wrap: wrap;
			view{
				width: 12vw;
				height: 12vw;
				display: flex;
				justify-content: center;
				image{
					width: 60%;
				}
			}
		}
	}
}
.input-box{
	&.hideEmoji{animation: hideEM .15s linear both;}
	&.showEmoji{animation: showEM .15s linear both;}
	width: 98%;
	min-height: 100upx;
	padding: 0 1%;
	background-color: #f2f2f2;
	display: flex;
	position: fixed;
	z-index: 20;
	bottom: 0;
	.voice,.more{
		flex-shrink: 0;
		width: 90upx;
		height: 100upx;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.send{
		//H5发送按钮左边距
		/* #ifdef H5 */
		margin-left: 20upx;
		/* #endif */
		flex-shrink: 0;
		width: 100upx;
		height: 100upx;
		display: flex;
		align-items: center;
		.btn{
			width: 90upx;
			height: 56upx;
			display: flex;
			justify-content: center;
			align-items: center;
			background:linear-gradient(to right,#f09b37,#eb632c);
			color: #fff;
			border-radius: 6upx;
			font-size: 24upx;
		}
	}
	.textbox{
		width: 100%;
		min-height: 70upx;
		margin-top: 15upx;
		.voice-mode{
			width: calc(100% - 2upx);
			height: 68upx;
			border-radius: 70upx;
			border:solid 1upx #cdcdcd;
			display: flex;
			justify-content: center;
			align-items: center;
			font-size: 28upx;
			background-color: #fff;
			color: #555;
			&.recording{
				background-color: #e5e5e5;
			}
		}
		.text-mode{
			width: 100%;
			min-height: 70upx;
			display: flex;
			background-color: #fff;
			border-radius: 40upx;
			.box{
				width: 100%;
				padding-left: 30upx;
				min-height: 70upx;
				display: flex;
				align-items: center;
				textarea{
					width: 100%;
				}
			}
			.em{
				flex-shrink: 0;
				width: 80upx;
				height: 70upx;
				display: flex;
				justify-content: center;
				align-items: center;
			}
		}
	}
}
.record{
	width: 40vw;
	height: 40vw;
	position: fixed;
	top: 55%;
	left: 30%;
	background-color: rgba(0,0,0,.6);
	border-radius: 20upx;
	.ing{
		width: 100%;
		height: 30vw;
		display: flex;
		justify-content: center;
		align-items: center;
		@keyframes volatility {
			0% {background-position: 0% 130%;}
			20% {background-position: 0% 150%;}
			30% {background-position: 0% 155%;}
			40% {background-position: 0% 150%;}
			50% {background-position: 0% 145%;}
			70% {background-position: 0% 150%;}
			80% {background-position: 0% 155%;}
			90% {background-position: 0% 140%;}
			100% {background-position: 0% 135%;}
		}
		.icon{
			background-image:linear-gradient(to bottom,#f09b37,#fff 50%); 
			background-size:100% 200%;
			animation: volatility 1.5s ease-in-out -1.5s infinite alternate;
			-webkit-background-clip:text; 
			-webkit-text-fill-color:transparent; 
			font-size: 150upx;
			color: #f09b37;
		}
	}
	.cancel{
		width: 100%;
		height: 30vw;
		display: flex;
		justify-content: center;
		align-items: center;
		.icon{
			color: #fff;
			font-size: 150upx;
		}
	}
	.tis{
		width: 100%;
		height: 10vw;
		display: flex;
		justify-content: center;
		font-size: 28upx;
		color: #fff;
		&.change{
			color: #f09b37;
		}
	}
}
.content{
	width: 100%;
	.msg-list{		
		width: 96%;
		margin: 0 1% 0 1%;
		position: absolute;
		top: 75upx;
		bottom: 100upx;
		.time{
			width: 50%;
			display: flex;
			float: right;
		}
		.tousertime{
			width: 50%;
			display: flex;
			float: left;
		}
		.row{
			&:first-child{
				margin-top: 20upx;
			}
			
			padding: 20upx 0;
			.my .left,.other .right{
				width: 100%;
				display: flex;
				
				.bubble{
					max-width: 70%;
					max-height: 200upx;
					min-height: 50upx;
					border-radius: 10upx;
					padding: 15upx 20upx;
					display: flex;
					align-items: center;
					font-size: 32upx;
					word-break: break-word;
					&.img{
						background-color: transparent;
						padding:0;
						overflow: hidden;
						image{
							max-width:200upx;
							max-height: 200upx;
						}
					}
					
				}
			}
			.my .right,.other .left{
				flex-shrink: 0;
				width: 80upx;
				height: 80upx;
				image{
					width: 80upx;
					height: 80upx;
					border-radius: 10upx;
				}
			}
			.my{
				width: 100%;
				display: flex;
				justify-content: flex-end;
				.left{
					min-height: 80upx;
					
					align-items: center;
					justify-content: flex-end;
					.bubble{
						background-color: #f06c7a;
						color: #fff;
						&.voice{
							.icon{
								color: #fff;
							}
							.length{
								margin-right: 20upx;
							}
						}
						&.play{
							@keyframes my-play {
								0% {
									transform: translateX(80%);
								}
								100% {
									transform: translateX(0%);
								}
							}
							.icon:after
							{
								border-left: solid 10upx rgba(240,108,122,.5);
								animation: my-play 1s linear infinite;
							}
						}
					}
				}
				.right{
					margin-left: 15upx;
				}
			}
			.other{
				width: 100%;
				display: flex;
				.left{
					margin-right: 15upx;
				}
				.right{
					flex-wrap: wrap;
					.username{
						width: 100%;
						height: 45upx;
						font-size: 24upx;
						color: #999;
						display: flex;
						.name{
							margin-right: 50upx;
						}
					}
					.bubble{
						background-color: #fff;
						color: #333;
						&.voice{
							.icon{
								color: #333;
								
							}
							.length{
								margin-left: 20upx;
							}
						}
						&.play{
							@keyframes other-play {
								0% {
									transform: translateX(-80%);
								}
								100% {
									transform: translateX(0%);
								}
							}
							.icon:after
							{
								border-right: solid 10upx rgba(255,255,255,.8);
								
								animation: other-play 1s linear infinite;
							}
						}
					}
				}
			}
		}
	}
}
.chat-username{
	
	width: 720upx;
	height:60upx;
	text-align: center;
	font-size: 40upx;
	font-weight: bold;
	
}
.content {
	width: 90%;
	padding:0;
	margin: 0 2%;
	text-align: center;
	
}
</style>
