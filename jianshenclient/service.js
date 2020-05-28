// 管理账号信息
const USERS_KEY = 'USERS_KEY';
const STATE_KEY = 'STATE_KEY';

const getUsers = function () {
    let ret = '';
    ret = uni.getStorageSync(USERS_KEY);
    if (!ret) {
        ret = '[]';
    }
    return JSON.parse(ret);
}

const addUser = function (userInfo) {
    let users = getUsers();
    users.push({
        account: userInfo.account,
        password: userInfo.password
    });
    uni.setStorageSync(USERS_KEY, JSON.stringify(users));
}


const getCode = function (url,param) {  
	 
	var query =url.substring( url.indexOf("?"));
 
       let vars = query.split("&");
       for (let i=0;i<vars.length;i++) {
               let pair = vars[i].split("=");
               if(pair[0] !=null){return pair[1];}
       }
       return null;
}

export default {
    getUsers,
    addUser,
	getCode
}
