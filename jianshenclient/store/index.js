import Vue from 'vue'
import Vuex from 'vuex'
 
Vue.use(Vuex)
// 管理账号信息
const USERS_KEY = 'ukey';
const STATE_KEY = 'skey';

const store = new Vuex.Store({
	 
	// #ifdef APP-PLUS|MP
	 state:  {
	    forcedLogin: true,
	    hasLogin: false,
	    muser: null
	},
	// #endif
	// #ifdef H5
	 state:sessionStorage.getItem('state') ? JSON.parse(sessionStorage.getItem('state')): {
	    
	    forcedLogin: true,
	    hasLogin: false,
	    muser: null
	},
	// #endif
    

    mutations: {
        login(state, muser) {
			console.log("-----------");
			console.log(state);
			console.log(muser);
            state.muser = muser || '新用户';
            state.hasLogin = true;
			// uni.setStorageSync(USERS_KEY, muser);
        },
        logout(state) {
            state.muser = null;
            state.hasLogin = false;
			// uni.setStorageSync(USERS_KEY, null);
        }
    }
})

export default store
