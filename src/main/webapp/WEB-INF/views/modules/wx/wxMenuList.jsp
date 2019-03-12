<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信菜单表管理</title>
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
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/wx/wxMenu/">微信菜单表列表</a></li>
		<shiro:hasPermission name="wx:wxMenu:edit"><li><a href="${ctx}/wx/wxMenu/form">微信菜单表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wxMenu" action="${ctx}/wx/wxMenu/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>菜单名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>菜单名称</th>
				<th>关键词</th>
				<th>菜单url地址</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="wx:wxMenu:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wxMenu">
			<tr>
				<td><a href="${ctx}/wx/wxMenu/form?id=${wxMenu.id}">
					${wxMenu.name}
				</a></td>
				<td>
					${wxMenu.inputCode}
				</td>
				<td>
					${wxMenu.url}
				</td>
				<td>
					<fmt:formatDate value="${wxMenu.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${wxMenu.remarks}
				</td>
				<shiro:hasPermission name="wx:wxMenu:edit"><td>
    				<a href="${ctx}/wx/wxMenu/form?id=${wxMenu.id}">修改</a>
					<a href="${ctx}/wx/wxMenu/delete?id=${wxMenu.id}" onclick="return confirmx('确认要删除该微信菜单表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>