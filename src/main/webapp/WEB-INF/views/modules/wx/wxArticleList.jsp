<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信文章管理</title>
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
		<li class="active"><a href="${ctx}/wx/wxArticle/">微信文章列表</a></li>
		<shiro:hasPermission name="wx:wxArticle:edit"><li><a href="${ctx}/wx/wxArticle/form">微信文章添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wxArticle" action="${ctx}/wx/wxArticle/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>标题</th>
				<th>作者</th>
				<th>是否显示封面图片</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="wx:wxArticle:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wxArticle">
			<tr>
				<td><a href="${ctx}/wx/wxArticle/form?id=${wxArticle.id}">
					${wxArticle.title}
				</a></td>
				<td>
					${wxArticle.author}
				</td>
				<td>
					${wxArticle.showCoverPic}
				</td>
				<td>
					<fmt:formatDate value="${wxArticle.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${wxArticle.remarks}
				</td>
				<shiro:hasPermission name="wx:wxArticle:edit"><td>
    				<a href="${ctx}/wx/wxArticle/form?id=${wxArticle.id}">修改</a>
					<a href="${ctx}/wx/wxArticle/delete?id=${wxArticle.id}" onclick="return confirmx('确认要删除该微信文章吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>