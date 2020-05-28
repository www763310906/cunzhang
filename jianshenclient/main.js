import Vue from 'vue'
import App from './App'

import uLink from '@/components/uLink.vue'
import store from './store'

Vue.config.productionTip = false

Vue.prototype.$store = store
 
Vue.component('uLink', uLink)
 

Vue.prototype.$serverUrl = 'http://127.0.0.1';
Vue.prototype.$serverPicUrl = 'http://127.0.0.1';

App.mpType = 'app'

const app = new Vue({
    store,
    ...App
})
app.$mount()
