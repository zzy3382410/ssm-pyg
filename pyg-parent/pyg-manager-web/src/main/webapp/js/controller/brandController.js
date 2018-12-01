app.controller("brandController", function ($scope, $controller, brandService) {
    $controller('baseController',{$scope:$scope});

    $scope.findPage = function (page, size) {
        brandService.findPage(page,size).success(
            function (response) {
                $scope.paginationConf.totalItems = response.total;
                $scope.list = response.rows;
            }
        );
    }

    $scope.save = function () {
        var serviceObject;//服务层对象
        if($scope.entity.id!=null){//如果有ID
            serviceObject=brandService.update( $scope.entity ); //修改
        }else{
            serviceObject=brandService.add( $scope.entity  );//增加
        }
        serviceObject.success(
            function(response){
                if(response.success){
                    //重新查询
                    $scope.reloadList();//重新加载
                }else{
                    alert(response.message);
                }
            }
        );
    }

    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    $scope.dele = function () {
        //获取选中的复选框
        brandService.dele( $scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                }
            }
        );
    }
    // 定义搜索对象
    $scope.searchEntity = {};

    //条件查询
    $scope.search = function (page, rows) {
        brandService.search(page,rows,$scope.searchEntity).success(
            function (response) {
                $scope.paginationConf.totalItems = response.total;
                $scope.list = response.rows;
            }
        )
    }

});
