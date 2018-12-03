app.service("brandService", function ($http) {

    this.findPage = function (page,size) {
        return $http.get("../brand/findPage.do?page=" + page + "&size=" + size);
    }

    this.findOne = function (id) {
        return $http.get('../brand/findOne.do?id=' + id);
    }

    //增加
    this.add=function(entity){
        return  $http.post('../brand/add.do',entity );
    }
    //修改
    this.update=function(entity){
        return  $http.post('../brand/update.do',entity );
    }

    this.dele=function (ids) {
        return $http.get('../brand/delete.do?ids=' + $scope.selectIds);
    }

    //搜索
    this.search=function(page,rows,searchEntity){
        return $http.post('../brand/search.do?page='+page+"&rows="+rows, searchEntity);
    }

    this.selectOptionList=function () {
        return $http.get('../brand/selectOptionList.do');
    }

});