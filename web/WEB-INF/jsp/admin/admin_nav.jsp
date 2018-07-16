<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-expand-sm bg-info navbar-dark">
    <a class="navbar-brand" href="<c:url value="/admin/"/>">EBookStore后台管理系统</a>
    <ul class="navbar-nav">
        <li class="nav-item">
            <a class="nav-link" href="<c:url value="/admin/order/list"/>">订单管理</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="<c:url value="/admin/product/list"/>">书单管理</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="<c:url value="/"/>">前台</a>
        </li>
        <c:if test="${user != null}">
            <li class="nav-item">
                <a class="nav-link" href="<c:url value="/myInfo"/>"><c:out value="${user.name}"/></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="javascript: logout()">注销</a>
            </li>
        </c:if>
        <c:if test="${user == null}">
            <li class="nav-item">
                <a class="nav-link" href="<c:url value="/login"/> ">登录</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<c:url value="/register"/>">注册</a>
            </li>
        </c:if>

    </ul>
</nav>
<script>
    function logout() {
        $.ajax({
            url: '<c:url value="/api/auth/logout"/>',
            method: 'POST',
            success: function () {
                location.href = '<c:url value="/list"/>';
            }
        })
    }
</script>