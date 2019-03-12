<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信关键词信息管理</title>
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
		<li class="active"><a href="${ctx}/wx/wxMsgKey/">微信关键词信息列表</a></li>
		<shiro:hasPermission name="wx:wxMsgKey:edit"><li><a href="${ctx}/wx/wxMsgKey/form">微信关键词信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wxMsgKey" action="${ctx}/wx/wxMsgKey/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>关键词：</label>
				<form:input path="inputCode" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>消息标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>关键词</th>
				<th>消息标题</th>
				<th>息消类型</th>
				<shiro:hasPermission name="wx:wxMsgKey:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wxMsgKey">
			<tr>
				<td>
					${wxMsgKey.inputCode}
				</td>
				<td>
					${wxMsgKey.title}
				</td>
				<td>
					${wxMsgKey.msgType =='text'?'文本消息':''}
					${wxMsgKey.msgType =='news'?'图文消息':''}
				</td>
				<shiro:hasPermission name="wx:wxMsgKey:edit"><td>
    				<a href="${ctx}/wx/wxMsgKey/form?id=${wxMsgKey.id}">修改</a>
					<a href="${ctx}/wx/wxMsgKey/delete?id=${wxMsgKey.id}" onclick="return confirmx('确认要删除该微信关键词信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>