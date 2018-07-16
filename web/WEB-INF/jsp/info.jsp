<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>个人信息 - EBookStore</title>
    <%@include file="meta.jsp"%>
</head>
<body>
<%@include file="nav.jsp"%>
<div class="container" id="app">
    <div class="row m-5">
        <div class="col-2"></div>
        <div class="col-8 jumbotron">
            <div class="row">
                <div class="col">
                    用户名：{{item.id}}
                </div>
            </div>
            <div class="row">
                <div class="col">
                    昵称：<input type="text" class="form-control" id="name" v-model="item.name" placeholder="请输入您的昵称"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    电话：<input type="text" class="form-control" id="phone" v-model="item.phone" placeholder="请输入您的联系方式"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    收货地址：<input type="text" class="form-control" id="address" v-model="item.address" placeholder="请输入您的收货地址"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <button class="btn btn-block" v-on:click="save()">保存</button>
                </div>
            </div>
        </div>
        <div class="col-2"></div>
    </div>
    <div class="row m-5">
        <div class="col-2">
            <button data-toggle="collapse" class="btn btn-link" data-target="#pw_div">修改密码</button>
        </div>
        <div class="col-8">
            <div id="pw_div" class="collapse jumbotron">
                <div class="row">
                    <div class="col">
                        旧密码：<input type="password" class="form-control" id="old_password" v-model="old_password" placeholder="请输入密码"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        新密码：<input type="password" class="form-control" id="new_password" v-model="new_password" placeholder="请再次输入密码"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        确认密码：<input type="password" class="form-control" id="check_password" v-model="check_password" placeholder="请再次输入密码"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <button class="btn btn-block" v-on:click="change_password()">保存</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-2"></div>
    </div>
</div>
</body>
<script>
    var vm = new Vue({
        el: '#app',
        data: {
            item: {},
            old_password: "",
            new_password: "",
            check_password: ""
        },
        methods: {
            refresh: function () {
                $.ajax({
                    url: '<c:url value="/api/auth/myInfo"/>',
                    method: 'GET',
                    success: function (ret) {
                        vm.item = ret;
                    }
                })
            },
            save: function () {
                $.ajax({
                    url: '<c:url value="/api/auth/myInfo"/>',
                    method: 'PUT',
                    data: JSON.stringify(vm.item),
                    success: function (ret) {
                        alert("保存成功。");
                        vm.item = ret;
                    },
                    error: function () {
                        alert("发生了不可预料的错误。");
                    }
                })
            },
            change_password: function () {
                if(vm.new_password !== vm.check_password) {
                    alert("确认密码不一致。请重新输入。");
                    return;
                }
                var data = {
                    oldPassword: vm.old_password,
                    newPassword: vm.newPassword
                };
                $.ajax({
                    url: '<c:url value="/api/auth/changePassword"/>',
                    method: 'PUT',
                    data: JSON.stringify(data),
                    success: function (ret) {
                        var success = ret["success"];
                        if(success)alert("密码修改成功。");
                        else alert("密码修改失败。请检查旧密码。");
                    },
                    error: function() {
                        alert("发生了不可预料的错误。");
                    }
                })
            }
        }
    });
    $(function () {
        vm.refresh();
    })
</script>
</html>
