app.service("loginService",function ($http) {
    //读取登录人的名字
    this.loginName=function () {
        return $http.get("../login/name.do");
    }
});