app.controller("contentController",function ($scope,contentService) {

   $scope.contentList=[];//广告集合
    $scope.findByCategoryId=function (categoryId) {
        contentService.findByCategoryId(categoryId).success(
            function (response) {
                $scope.contentList=response;
            }
        )
    }
});