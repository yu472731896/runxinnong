<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>聊天系统在线用户</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<%--<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/run/chatInfo/">聊天信息列表</a></li>
		<shiro:hasPermission name="run:chatInfo:edit"><li><a href="${ctx}/run/chatInfo/form">聊天信息添加</a></li></shiro:hasPermission>
	</ul>--%>
	<%--<form:form id="searchForm" modelAttribute="chatInfo" action="${ctx}/run/chatInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>--%>
	<sys:message content="${message}"/>
    <h5>在线客服</h5>
	<table id="onlineCustomerTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>用户名称</th>
				<th>ip地址</th>
				<th>连接时间</th>
				<%--<shiro:hasPermission name="run:chatInfo:edit"><th>操作</th></shiro:hasPermission>--%>
			</tr>
		</thead>
        <tbody>
        <c:forEach items="${onlineCustomWebSocketMap}" var="mapItem">
            <tr>
                    <td>
                        <c:out value="${mapItem.value.name}"/>
                    </td>
                    <td>
                        <c:out value="${mapItem.value.ipAddr}"/>
                    </td>
                    <td>
                        <fmt:formatDate value="${mapItem.value.connectTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>
            </tr>
        </c:forEach>
        </tbody>
	</table>

    <h5>在线游客</h5>
    <table id="onlineGuestTable" class="table table-striped table-bordered table-condensed">
        <thead>
        <tr>
            <th>用户名称</th>
            <th>ip地址</th>
            <th>连接时间</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${onlinGuestWebSocketMap}" var="mapItem">
            <tr>
                   <td>
                         <c:out value="${mapItem.value.name}"/>
                   </td>
                   <td>
                        <c:out value="${mapItem.value.ipAddr}"/>
                   </td>
                   <td>
                        <fmt:formatDate value="${mapItem.value.connectTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                   </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
	<%--<div class="pagination">${page}</div>--%>
</body>
</html>