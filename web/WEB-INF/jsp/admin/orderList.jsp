<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="v-on" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>处理客户订单 - EBookStore</title>
    <%@include file="../meta.jsp"%>
</head>
<body>
<%@include file="admin_nav.jsp"%>
<div class="container" id="app">
    <div class="row m-5 bg-light rounded">
        <div class="col">
        </div>
    </div>
    <div class="row m-5">
        <div class="col">
            <table class="table">
                <thead>
                <tr>
                    <th>订单编号</th>
                    <th>书名/数量</th>
                    <th>总支付价格</th>
                    <th>订单状态</th>
                    <th>提交日期</th>
                    <th>用户</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="item in list">
                    <td>{{item.id}}</td>
                    <td>
                        <div class="row" v-for="product in item.products">
                            <div class="col"><img alt="预览图" v-bind:src="'/api/product/' + product.id + '/image.jpg'"></div>
                            <div class="col">{{product.name}} × {{product.count}}</div>
                        </div>
                    </td>
                    <td>￥{{item.price}}</td>
                    <td>{{getStatusName(item.status)}}</td>
                    <td>{{getDateTime(item.createTime)}}</td>
                    <td>{{item.userId}}</td>
                    <td>
                        <button class="btn btn-info" v-if="item.status == 'submitted'" v-on:click="complete(item.id)">执行发货</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="row bg-light rounded">
        <div class="col"></div>
        <div class="col-auto">

        </div>
    </div>
</div>
</body>
<script>
    var vm = new Vue({
        el: '#app',
        data: {
            list: [],
            sum_price: 0
        },
        methods: {
            complete: function(id) {
                if(confirm("确认对该订单执行发货？")) {
                    $.ajax({
                        url: '/api/admin/order/check/' + id,
                        method: 'POST',
                        success: function (ret) {
                            if(ret["success"]) {
                                alert("确认发货成功！");
                                vm.refresh();
                            }else{
                                alert("发生了意料之外的错误。");
                            }
                        },
                        error: function () {
                            alert("发生了意料之外的错误。");
                        }
                    })
                }
            },
            getStatusName: function(flag) {
                if(flag === "cart") return "购物车";
                else if(flag === "submitted") return "等待发货";
                else if(flag === "running") return "等待收货";
                else if(flag === "complete") return "已完成";
                else return "未知状态";
            },
            getDateTime: function(ms) {
                var d = new Date(ms);
                return d.getFullYear() + "年" + (d.getMonth() + 1) + "月" + d.getDate() + "日 " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds();
            },
            refresh: function () {
                $.ajax({
                    url: '<c:url value="/api/order/"/>',
                    method: 'GET',
                    success: function (ret) {
                        vm.list = ret;
                    }
                })
            }
        }
    });
    $(document).ready(function () {
        vm.refresh();
    });
</script>
</html>
