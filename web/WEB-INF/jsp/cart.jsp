<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>购物车 - EBookStore</title>
    <%@include file="meta.jsp"%>
  </head>
  <body>
    <%@include file="nav.jsp"%>
  <div class="container" id="app">
    <div class="row m-5 bg-light rounded">
        <div class="col p-2">
            总价格：￥{{sum_price}}
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
                            <th>数量</th>
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
                            <td><input type="number" class="form-control" v-model="item.count"/></td>
                            <td><div class="btn-group">
                                <button class="btn btn-primary btn-sm" v-on:click="save(item.id, item.count)">保存</button>
                                <button class="btn btn-danger btn-sm" v-on:click="remove(item.id)">删除</button>
                            </div></td>
                        </tr>
                  </tbody>
              </table>
          </div>
      </div>
      <div class="row m-5 bg-light rounded">
          <div class="col"></div>
          <div class="col-auto">
              <button class="btn btn-primary p-2" v-on:click="submit()">提交订单</button>
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
            save: function (id, count) {
                if(count <= 0) vm.remove(id);
                else {
                    $.ajax({
                        url: '<c:url value="/api/cart/products"/>',
                        method: 'PUT',
                        data: JSON.stringify({"productId": id, "count": count}),
                        success: function () {
                            vm.refresh();
                        },
                        error: function (x, t, e) {
                            var data = JSON.parse(x.responseText);
                            if(data["error"])alert("错误：" + data["error"]);
                            else if(data["keyword"])alert("错误：" + data["keyword"]);
                            else alert("错误：" + x.responseText);
                        }
                    })
                }
            },
            remove: function (id) {
                if(confirm("您正在移除该商品。确认移除吗？")) {
                    $.ajax({
                        url: '<c:url value="/api/cart/products"/>',
                        method: 'DELETE',
                        data: JSON.stringify({"productId": id}),
                        success: function () {
                            vm.refresh();
                        },
                        error: function (x, t, e) {
                            var data = JSON.parse(x.responseText);
                            if(data["error"])alert("错误：" + data["error"]);
                            else if(data["keyword"])alert("错误：" + data["keyword"]);
                            else alert("错误：" + x.responseText);
                        }
                    })
                }
            },
            refresh: function () {
                $.ajax({
                    url: '<c:url value="/api/cart/"/>',
                    method: 'GET',
                    success: function (ret) {
                        var products = ret["products"];
                        var sum = 0;
                        for(var i in products) if(products[i] !== null) {
                            products[i]["save"] = true;
                            sum += products[i]["price"] * products[i]["discount"] * products[i]["count"];
                        }
                        vm.list = products;
                        vm.sum_price = sum;
                    }
                });
            },
            submit: function () {
                if(confirm("确定要提交购物车生成订单吗？")) {
                    $.ajax({
                        url: '<c:url value="/api/cart/submit"/>',
                        method: 'POST',
                        success: function (ret) {
                            var success = ret["success"];
                            if(success) {
                                alert("订单提交成功！");
                                location.href = '<c:url value="/order"/>';
                            }else{
                                if(ret["keyword"] === "LACK_STOCK") {
                                    var name_list = "";
                                    for(var i in ret["list"]) name_list += "[" + ret["list"][i] + "]";
                                    alert("您的购物车中，商品" + name_list + "已经没有库存了。");
                                }else if(ret["keyword"] === "EMPTY_CART") {
                                    alert("您的购物车是空的。");
                                }else if(ret["keyword"] === "NOT_LOGIN") {
                                    alert("请您先登录再进行购买。");
                                }
                            }
                        }
                    })
                }
            }
        }
    });
    $(document).ready(function () {
        vm.refresh();
    });
</script>
</html>
