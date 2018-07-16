<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>书单 - EBookStore</title>
    <%@include file="meta.jsp"%>
  </head>
  <body>
    <%@include file="nav.jsp"%>
  <div class="container" id="app">
      <div class="row m-2">
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
                            <td><button type="button" class="btn btn-light" v-on:click="add(item.id)">加入购物车</button></td>
                        </tr>
                  </tbody>
              </table>
          </div>
      </div>
      <div class="row m-2">
          <div class="col"></div>
          <div class="col-auto btn-group">
              <button class="btn" v-bind:class="{'btn-outline-primary': !pb.enable, 'btn-primary': pb.enable}" v-for="pb in page_buttons" v-on:click="jump(pb.value)">{{pb.title}}</button>
          </div>
      </div>
  </div>
  </body>
<script>
    var max_item_count = 5;
    var vm = new Vue({
        el: '#app',
        data: {
            all: [],
            list: [],
            page_buttons: [],
            page_now: 1
        },
        methods: {
            add: function (id) {
                $.ajax({
                    url: '<c:url value="/api/cart/products"/>',
                    method: 'POST',
                    data: {json: JSON.stringify({"productId": id})},
                    success: function () {
                        alert("已经添加到购物车。");
                    }
                })
            },
            jump: function (value) {
                //value为数值时，直接跳跃到对应的页码；
                //value为"head"/"tail"时，跳到首页/末页；
                //value为"next"/"prev"时，跳到下一页/上一页。
                //jump时，修改list、page_buttons、page_now。
                var max_page_count = Math.ceil(vm.all.length / max_item_count);
                var goal_page = value === "head" ? 1 :
                                value === "tail" ? max_page_count :
                                    value === "next" ? (vm.page_now + 1) :
                                        value === "prev" ? (vm.page_now - 1): value;
                goal_page = goal_page < 1 ? 1 : goal_page > max_page_count ? max_page_count : goal_page;
                var slice_first = (goal_page - 1) * max_item_count;
                var slice_last = slice_first + max_item_count;
                if(slice_last > vm.all.length) slice_last = vm.all.length;
                vm.list = vm.all.slice(slice_first, slice_last);
                vm.page_now = goal_page;
                var buttons = [];
                if(max_page_count > 1) {
                    var button_first = max_page_count > 5 ? goal_page - 2 : 1;
                    var button_last = max_page_count > 5 ? goal_page + 2 : max_page_count;
                    if(button_first < 1) {
                        button_first = 1;
                        button_last = 5;
                    }else if(button_last > max_page_count) {
                        button_last = max_page_count;
                        button_first = max_page_count - 4;
                    }
                    buttons.push({title: "<<", value: "head", enable: false});
                    if(button_first > 1) buttons.push({title: "...", value: button_first - 1, enable: false});
                    for(var i = button_first; i <= button_last; ++i) {
                        buttons.push({title: i, value: i, enable: i === goal_page});
                    }
                    if(button_last < max_page_count) buttons.push({title: "...", value: button_last + 1, enable: false});
                    buttons.push({title: ">>", value: "tail", enable: false});
                }
                vm.page_buttons = buttons;
            },
            refresh: function () {
                $.ajax({
                    url: '<c:url value="/api/product/"/>',
                    method: 'GET',
                    success: function (ret) {
                        vm.all = ret;
                        vm.jump(1);
                    }
                });
            }
        }
    });
    $(document).ready(function () {
        vm.refresh();
    });
</script>
</html>
