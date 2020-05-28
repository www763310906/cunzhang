<template>
	<view class="m-input-view">
		<view class="m-autoview">
			<input class="uni-input" placeholder="请输入" :value="value" @input="onInput" />
			<!-- 优先显示密码可见按钮 -->
			<view v-if="clearable_&&value" class="m-input-icon">
				<m-icon color="#666666" type="clear" size="20" @click="clear"></m-icon>
			</view>
		</view>
		<view class="str-auto-complete-container" v-if="isShow">
			<view v-for="(item, index) in showList" :key="index" class="str-auto-complete-item" @tap="selectThisItem(item)"
			 v-html="item.showString"></view>
		</view>
	</view>
</template>

<script>
	import mIcon from '@/components/m-icon/m-icon.vue';

	export default {
		components: {
			mIcon
		},
		props: {
			stringList: {
				type: Array
			},
			importvalue: {
				type: String
			},
			highlightColor: {
				type: String,
				default: 'lightcoral'
			},
			/**
			 * 是否显示清除按钮
			 */
			clearable: {
				type: [Boolean, String],
				default: false
			},
			/**
			 * 输入类型
			 */
			type: String,
			/**
			 * 值
			 */
			value: String,
			// 执行自动补全时的防抖时间
			debounce: {
				type: Number,
				default: 200
			},
			//动态数据，需要返回Promise包装的数据
			loadData: Function
		},
		data() {
			return {
				showList: [],
				debounceTask: undefined,
				needShow: false
			};
		},
		// watch: {
		// 	importvalue(newValue, oldValue) {
		// 		this.value = newValue
		// 	}
		// },
		model: {
			prop: 'value',
			event: 'change'
		},
		computed: {
			isShow() {
				return this.needShow && this.showList.length > 0;
			},
			clearable_() {
				return String(this.clearable) !== 'false'
			}
		},
		created() {
		//	this.stringList.sort();
			this.showList = this.stringList;
			// this.value = this.importvalue;
			this.$emit('input', this.importvalue);
			if (this.loadData) {
				this.defultLoadData = this.loadData;
			}
			
		},
		methods: {
			//由外部使用用者重写loadData覆盖
			defultLoadData(value) {
				return Promise.resolve(this.showList);
			},
			clear() {

				this.$emit('clear', '')
				this.needShow = false;

			},
			onInput(event) {
				let value = event.target.value;
			 
				// If Debounce
				if (this.debounce) {
					 
					if (this.debounceTask !== undefined) clearTimeout(this.debounceTask);
					this.debounceTask = setTimeout(() => {
						return this.getData(value);
					}, this.debounce);
				} else {
					return this.getData(value);
				}
				
				// this.filterList(event.target.value);
				// // this.$set(this, 'value', event.target.value);
				// this.$emit('input', event.target.value)
				// this.$emit('change', event.target.value);
				// if (event.target.value.length > 0) {
				// 	this.needShow = true;
				// } else {
				// 	this.needShow = false;
				// }
			},
			getData(value) {
				if (value.length < 1) {
					this.$emit('input', value);
					return;
				}
				const task = this.defultLoadData(value);
				if (task && task.then) {
					return task.then(data => {
						this.showList = data||[];
						
						 
						
					let pi=	this.filterList(value);
						 
						 this.$emit('input',value)
						 this.$emit('change', value);
						 if (value.length > 0) {
						 	this.needShow = true;
						 } else {
						 	this.needShow = false;
						 }
						 if(!pi)  this.needShow=false;
					});
				}
			},
			
			filterList(stringExp) {
				let tempArray = [];
				for (let i = 0; i < this.showList.length; i++) {
					let temp = this.showList[i].text;
					let showObject = this.filterString(this.showList[i],stringExp, temp);
					if (showObject) {
						if (showObject.number === stringExp.length) {
							tempArray.push(showObject);
						}
					}
				}
				if (tempArray.length === 0 && stringExp.length !== 0) {
					uni.showToast({
						title: '没有匹配的字符串',
						mask: false,
						icon: 'none',
						duration: 1500
					});
					 
					return false;
				}
				this.showList = tempArray;
			 return true;
			},
			filterString(item,stringExp, b) {
				 
				const tempByteArray = b.split('');
				let itemShow = [];
				const sArray = stringExp.split('');
				const fontStart = '<span style=color:' + this.highlightColor + '>';
				const fontEnd = '</span>';
				let sameCharNumber = 0;
				for (let j = 0; j < tempByteArray.length; j++) {
					if (tempByteArray[j] === sArray[sameCharNumber]) {
						let sameNumberNow = sameCharNumber;
						itemShow.push(fontStart);
						for (let k = 0; k < sArray.length - sameNumberNow; k++) {
							if (tempByteArray[j + k] === sArray[k + sameNumberNow]) {
								itemShow.push(tempByteArray[j + k]);
								sameCharNumber++;
							} else if (tempByteArray[j + k] !== sArray[k + sameNumberNow]) {
								itemShow.push(fontEnd);
								itemShow.push(tempByteArray[j + k]);
								j += k;
								break;
							}
							if (k + sameNumberNow === sArray.length - 1) {
								j += k;
								itemShow.push(fontEnd);
							}
						}
					} else {
						itemShow.push(tempByteArray[j]);
					}
				}
				if (sameCharNumber > 0) {
					return {
						orginalString: b,
						number: sameCharNumber,
						showString: itemShow.join(''),
						phone:item.phone,
						address:item.address,
						userId:item.userId,
						person:item.text
					};
				}
			},
			selectThisItem(item) {
				// this.$set(this, 'value', item.orginalString);
				console.log("select item");
				this.$emit('input', item.orginalString)
				this.needShow = false;
				this.$emit('change', item.orginalString);
				this.$emit('select', item);
			}
		}
	};
</script>

<style lang="scss">
	.m-autoview {

		display: flex;
		align-items: center;

	}

	.m-input-icon {
		width: 20px;
	}


	.m-input-view {
		/* 	display: flex; */
		/* 		flex-direction: row; */
		/* align-items: center; */
		/* width: 80%;
		flex: 1; */
		padding: 0 10rpx;
	}

	.str-auto-complete-container {
		// width: 100%; 
		height: auto;
		border: 1px solid #f3f3f4;
		position: absolute;
		z-index: 999;
		background: #fff;

		.str-auto-complete-item {
			padding: 10upx;
		}
	}
</style>
