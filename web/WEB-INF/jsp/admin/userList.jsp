<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="v-on" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户列表 - EBookStore</title>
    <%@include file="../meta.jsp"%>
</head>
<body>
<%@include file="admin_nav.jsp"%>
<div class="container" id="app">
    <div class="row m-5 bg-light rounded">
        <div class="col">
        </div>
    </div>
    <div id="list" class="row m-5">
        <div class="col">
            <table class="table">
                <thead>
                <tr>
                    <th>账户名</th>
                    <th>用户名</th>
                    <th>管理员</th>
                    <th>其他信息</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="item in list">
                    <td>{{item.id}}</td>
                    <td>{{item.name}}</td>
                    <td>{{getAdminStatus(item.isAdmin)}}</td>
                    <td>
                        <div class="row">收货地址：{{item.address}}</div>
                        <div class="row">电话：{{item.phone}}</div>
                    </td>
                    <td>
                        <button class="btn btn-sm btn-outline-secondary" v-on:click="toPasswordPage(item)">修改密码</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div id="setps" class="row m-5 jumbotron">
        <div class="col-2">
            <button class="btn btn-link" v-on:click="toListPage()">返回</button>
        </div>
        <div class="col-8">
            <div class="row">
                <div class="col">
                    账户名： {{choose.id}}
                </div>
            </div>
            <div class="row">
                <div class="col">
                    用户名： {{choose.name}}
                </div>
            </div>
            <div class="row">
                <div class="col">
                    新密码： <input type="password" class="form-control" v-model="newPassword" placeholder="输入新密码"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    确认密码： <input type="password" class="form-control" v-model="checkPassword" placeholder="确认新密码"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <button class="btn btn-block" v-on:click="setPassword()">提交</button>
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
            list: [],
            choose: {},
            newPassword: "",
            checkPassword: ""
        },
        methods: {
            getAdminStatus: function(admin) {
                return admin ? "√" : "×";
            },
            toPasswordPage: function(id) {
                vm.choose = id;
                $('#list').hide();
                $('#setps').show();
            },
            toListPage: function() {
                $('#list').show();
                $('#setps').hide();
                vm.newPassword = "";
                vm.checkPassword = "";
            },
            setPassword: function(){
                if(vm.newPassword === "" || vm.checkPassword === "") alert("密码不能为空。");
                else if(vm.newPassword !== vm.checkPassword) alert("两次输入的密码不一致。");
                else {
                    var data = {password: vm.checkPassword};
                    $.ajax({
                        url: '/api/admin/user/' + vm.choose["id"] + '/setPassword',
                        method: 'POST',
                        data: {json: JSON.stringify(data)},
                        success: function () {
                            alert("密码修改成功！");
                            vm.toListPage();
                            vm.choose = {};
                        },
                        error: function () {
                            alert("发生了不可预料的错误。");
                        }
                    });
                }
            },
            refresh: function () {
                $.ajax({
                    url: '<c:url value="/api/admin/user/"/>',
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
        vm.toListPage();
    });
</script>
</html>
