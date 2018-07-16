<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
    <a class="navbar-brand" href="<c:url value="/"/>">EBookStore</a>
    <ul class="navbar-nav">
        <li class="nav-item">
            <a class="nav-link" href="<c:url value="/list"/>">书单</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="<c:url value="/cart"/>">购物车</a>
        </li>
        <c:if test="${user != null}">
            <li class="nav-item">
                <a class="nav-link" href="<c:url value="/order"/>">订单</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<c:url value="/myInfo"/>"><c:out value="${user.name}"/></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="javascript: logout()">注销</a>
            </li>
            <c:if test="${user.isAdmin()}">
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/admin/"/>">后台</a>
                </li>
            </c:if>
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