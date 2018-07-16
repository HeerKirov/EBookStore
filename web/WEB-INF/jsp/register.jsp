<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>新用户注册 - EBookStore</title>
    <%@include file="meta.jsp"%>
</head>
<body>
<%@include file="nav.jsp"%>
    <div class="container">
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
                         确认密码：<input type="password" class="form-control" id="check_password" placeholder="请再次输入密码"/>
                     </div>
                 </div>
                 <div class="row">
                     <div class="col">
                         昵称：<input type="text" class="form-control" id="name" placeholder="请输入您的昵称"/>
                     </div>
                 </div>
                 <div class="row">
                     <div class="col">
                         电话：<input type="text" class="form-control" id="phone" placeholder="请输入您的联系方式"/>
                     </div>
                 </div>
                 <div class="row">
                     <div class="col">
                         收货地址：<input type="text" class="form-control" id="address" placeholder="请输入您的收货地址"/>
                     </div>
                 </div>
                 <div class="row">
                     <div class="col">
                         <button type="button" id="register_btn" class="btn btn-block">注册</button>
                     </div>
                 </div>
                 <div class="alert alert-danger" style="display: none" id="err">
                     <strong>错误</strong> 注册失败，请重试。
                 </div>
             </div>
             <div class="col-4"></div>
        </div>
    </div>
</body>
<script>
    $(document).ready(function () {
        $('#register_btn').click(function () {
           var username = $('#username').val();
           var password = $('#password').val();
           var check_password = $('#check_password').val();
           var name = $('#name').val();
           var phone = $('#phone').val();
           var address = $('#address').val();
           if(password !== check_password) {
               $('#err').show();
               return;
           }
           if(phone === "") phone = null;
           if(address === "") address = null;
           var data = {id: username, pw: password, name: name, phone: phone, address: address};
           $.ajax({
               url: '<c:url value="/api/auth/register"/>',
               method: 'POST',
               data: {json: JSON.stringify(data)},
               success: function () {
                   alert("注册成功！");
                   location.href = '<c:url value="/login"/>';
               },
               error: function () {
                    $('#err').show();
               }
           })
        });
    })
</script>
</html>
