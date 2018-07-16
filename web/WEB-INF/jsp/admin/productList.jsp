<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="v-on" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>书单管理 - EBookStore</title>
    <%@include file="../meta.jsp"%>
  </head>
  <body>
    <%@include file="admin_nav.jsp"%>
  <div class="container" id="app">
    <div class="row m-5 bg-light">
        <div class="col"></div>
        <div class="col-auto btn-group">
            <button class="btn btn-primary p-2" v-on:click="add()">新建</button>
        </div>
    </div>
      <div class="row m-5">
          <div class="col">
              <table class="table">
                  <thead>
                        <tr>
                            <th>ID</th>
                            <th>预览</th>
                            <th>书名</th>
                            <th>价格</th>
                            <th>简介</th>
                            <th>销售状况</th>
                            <th>上架状态</th>
                            <th>操作</th>
                        </tr>
                  </thead>
                  <tbody>
                        <tr v-for="item in list">
                            <td>{{item.id}}</td>
                            <td><img alt="预览图" v-bind:src="'/api/product/' + item.id + '/image.jpg'"></td>
                            <td>{{item.name}}</td>
                            <td>￥{{item.price * item.discount}}</td>
                            <td>{{item.description}}</td>
                            <td>销量{{item.sale}}/库存{{item.stock}}</td>
                            <td>{{usefulStatus(item.useful)}}</td>
                            <td>
                                <button class="btn btn-info" v-on:click="edit(item.id)">编辑</button>
                            </td>
                        </tr>
                  </tbody>
              </table>
          </div>
      </div>
  </div>
  </body>
<script>
    var vm = new Vue({
        el: '#app',
        data: {
            list: []
        },
        methods: {
            usefulStatus: function(status) {
                return status ? "在售" : "下架";
            },
            add: function() {
                location.href = '<c:url value="/admin/product/add"/>';
            },
            edit: function (id) {
                location.href = '/admin/product/detail/' + id;
            }
        }
    });
    $(document).ready(function () {
        $.ajax({
            url: '<c:url value="/api/admin/product/"/>',
            method: 'GET',
            success: function (ret) {
                vm.list = ret;
            }
        });
    });
</script>
</html>
