//购物车控制层
app.controller('cartController', function ($scope, cartService,addressService) {

    //数量加减
    $scope.addGoodsToCartList = function (itemId, num) {
        cartService.addGoodsToCartList(itemId, num).success(
            function (response) {
                if (response.success) {
                    $scope.findCartList();//刷新列表
                } else {
                    alert(response.message);
                }
            }
        );
    }

    //查询购物车列表
    $scope.findCartList = function () {
        cartService.findCartList().success(
            function (response) {
                $scope.cartList = response;
                $scope.totalValue = cartService.sum($scope.cartList);
            }
        );
    }

    //获取地址列表
    $scope.findAddressList = function () {
        cartService.findAddressList().success(
            function (response) {
                $scope.addressList = response;
                //设置默认地址
                for (var i = 0; i < $scope.addressList.length; i++) {
                    if ($scope.addressList[i].isDefault == '1') {
                        $scope.address = $scope.addressList[i];
                        break;
                    }
                }
            }
        );
    }

    //选择地址
    $scope.selectAddress = function (address) {
        $scope.address = address;
    }

    //判断当前是否为选中地址
    $scope.idSelectedAddress = function (address) {
        if (address == $scope.address) {
            return true;
        } else {
            return false;
        }
    }

    $scope.order={PaymentType:'1'};

    //选择支付方式
    $scope.selectPayType=function (type) {
        $scope.order.PaymentType=type;
        alert( $scope.order.PaymentType);
    }

    //新增地址
    $scope.add = function () {
        alert("添加地址");
        addressService.add($scope.entity).success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }

    //批量删除
    $scope.dele = function (id) {
        //获取选中的复选框
        addressService.dele(id).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                }
            }
        );
    }


    //查询实体
    $scope.findOne = function (id) {
        addressService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }


    //保存订单
    $scope.submitOrder=function () {
        $scope.order.receiverAreaName=$scope.address.address;//地址
        $scope.order.receiverMobile=$scope.address.mobile;//手机
        $scope.order.receiver=$scope.address.contact;//联系人
        cartService.submitOrder($scope.order).success(
            function (response) {
                if (response.success){
                    //页面跳转
                    if ($scope.order.PaymentType=='1'){
                        location.href='pay.html';
                    }else {
                        location.href="paysuccess.html";
                    }
                }else {
                    alert(response.message);
                }
            }
        );
    }
});