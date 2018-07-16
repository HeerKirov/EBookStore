<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="v-on" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>书籍详情 - EBookStore</title>
    <%@include file="../meta.jsp"%>
</head>
<body>
<%@include file="admin_nav.jsp"%>
<div class="container" id="app">
    <div class="row m-5 jumbotron">
        <div class="col">
            <div class="row">
                <div class="col">
                    编号： {{item.id}}
                </div>
                <div class="col-auto">
                    <button class="btn btn-primary" v-on:click="save()">保存更改</button>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    书名：<input type="text" class="form-control" v-model="item.name" placeholder="商品的名称"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    简介：<input type="text" class="form-control" v-model="item.description" placeholder="商品简介"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    价格：<input type="number" class="form-control" v-model="item.price" placeholder="标准价格"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    折扣：<input type="number" class="form-control" v-model="item.discount" placeholder="给出的折扣"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    库存数量：<input type="number" class="form-control" v-model="item.stock" placeholder="剩余库存"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    上架状态：<input type="checkbox" class="form-control" v-model="item.useful" placeholder="商品是否可用"/>
                </div>
            </div>
        </div>
    </div>
    <div class="row m-5 jumbotron">
        <div class="row">
            <div class="col-auto">预览：</div>
            <div class="col-auto">
                <img alt="预览图" v-bind:src="'/api/product/' + item.id + '/image.jpg'">
            </div>
            <div class="col">
                <form id="uploadForm" enctype="multipart/form-data">
                    <input type="file" id="img" name="img"/>
                </form>
            </div>
            <div class="col">
                <button class="btn btn-light" v-on:click="upload()">上传预览图</button>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    var vm = new Vue({
        el: '#app',
        data: {
            item: {}
        },
        methods: {
            save: function () {
                $.ajax({
                    url: '/api/admin/product/${id}',
                    method: 'PUT',
                    data: JSON.stringify(vm.item),
                    success: function (ret) {
                        alert("保存成功。");
                        vm.item = ret;
                    },
                    error: function () {
                        alert("发生了预料之外的错误。")
                    }
                })
            },
            refresh: function () {
                $.ajax({
                    url: '/api/admin/product/${id}',
                    method: 'GET',
                    success: function (ret) {
                        vm.item = ret;
                    }
                })
            },
            upload: function () {
                var data = new FormData($('#uploadForm')[0]);
                $.ajax({
                    url: '/api/admin/product/${id}/upload',
                    type: 'POST',
                    data: data,
                    cache: false,
                    processData: false,
                    contentType: false,
                    success: function (ret) {
                        alert("预览图上传成功。");
                        //todo refresh
                    },
                    error: function () {
                        alert("发生了预料之外的错误。")
                    }
                });
            }
        }
    });
    $(function () {
        vm.refresh();
    })
</script>
</html>
