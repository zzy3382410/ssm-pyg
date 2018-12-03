app.controller('indexController',function($scope,$controller,loginService) {
    $scope.showLoginName=function () {
        loginService.loginName().success(
            function (response) {
                $scope.loginName=response.loginName;
            }
        );
    }
});