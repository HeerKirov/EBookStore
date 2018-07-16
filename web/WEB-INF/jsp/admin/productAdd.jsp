<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="v-on" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加新书籍 - EBookStore</title>
    <%@include file="../meta.jsp"%>
</head>
<body>
<%@include file="admin_nav.jsp"%>
<div class="container" id="app">
    <div class="row m-5 jumbotron">
        <div class="col">
            <div class="row">
                <div class="col">
                    书名：<input type="text" class="form-control" id="name" placeholder="商品的名称"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    简介：<input type="text" class="form-control" id="description" placeholder="商品简介"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    价格：<input type="number" class="form-control" id="price" placeholder="标准价格"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    折扣：<input type="number" class="form-control" id="discount" placeholder="给出的折扣"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    库存数量：<input type="number" class="form-control" id="stock" placeholder="剩余库存"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    上架状态：<input type="checkbox" class="form-control" id="useful" placeholder="商品是否可用"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <button class="btn btn-block" id="submit">提交</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    function submit() {
        var data = {
            name: $('#name').val(),
            description: $('#description').val(),
            price: $('#price').val(),
            discount: $('#discount').val(),
            stock: $('#stock').val(),
            useful: $('#useful').val() === "on"
        };
        $.ajax({
            url: '<c:url value="/api/admin/product/"/>',
            method: 'POST',
            data: {json: JSON.stringify(data)},
            success: function () {
                location.href = '<c:url value="/admin/product/list"/>';

            },
            error: function () {
                alert("发生了预料之外的错误。");
            }
        })
    }
    $(function () {
        $('#submit').click(submit);
    })
</script>
</html>
