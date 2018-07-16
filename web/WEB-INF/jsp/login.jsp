<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录 - EBookStore</title>
    <%@include file="meta.jsp"%>
</head>
<body>
<%@include file="nav.jsp"%>
    <div class="container" id="app">
        <div class="row m-5">
             <div class="col-4"></div>
             <div class="col-4">
                 <div class="row">
                     <div class="col">
                         用户名：<input type="text" class="form-control" id="username" placeholder="请输入用户名"/>
                     </div>
                 </div>
                 <div class="row">
                     <div class="col">
                         密 码：<input type="password" class="form-control" id="password" placeholder="请输入密码"/>
                     </div>
                 </div>
                 <div class="row">
                     <div class="col">
                         <button type="button" id="login_btn" class="btn btn-block">登录</button>
                     </div>
                 </div>
                 <div class="alert alert-danger" style="display: none" id="err">
                     <strong>错误</strong> {{error}}
                 </div>
             </div>
             <div class="col-4"></div>
        </div>
    </div>
</body>
<script>
    var vm = new Vue({
        el: '#app',
        data: {
            error: ""
        }
    });
    $(document).ready(function () {
        $('#login_btn').click(function () {
           var username = $('#username').val();
           var password = $('#password').val();
           var data = {id: username, pw: password};
           $.ajax({
               url: '<c:url value="/api/auth/login"/>',
               method: 'POST',
               data: {json: JSON.stringify(data)},
               success: function (ret) {
                   var success = ret["success"];
                   if(success) location.href = '<c:url value="/"/>';
                   else {
                       $('#err').show();
                       vm.error = "登录认证失败，请重试。";
                   }
               },
               error: function () {
                   $('#err').show();
                   vm.error = "由于登录次数过多，您已被禁止登录，请在一分钟之后再尝试。";
               }
           })
        });
    })
</script>
</html>
