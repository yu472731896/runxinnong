<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>留言管理</title>
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
		<li class="active"><a href="${ctx}/run/runLeaveMessage/">留言列表</a></li>
		<shiro:hasPermission name="run:runLeaveMessage:edit"><li><a href="${ctx}/run/runLeaveMessage/form">留言添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="runLeaveMessage" action="${ctx}/run/runLeaveMessage/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="12" class="input-medium"/>
			</li>
			<li><label>电话号码：</label>
				<form:input path="phoneNumber" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="email" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:radiobuttons path="state" items="${fns:getDictList('leave_message_state')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>姓名</th>
				<th>电话号码</th>
				<th>邮箱</th>
				<th>地址</th>
				<th>内容</th>
				<th>状态</th>
				<th>创建时间</th>
				<shiro:hasPermission name="run:runLeaveMessage:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="runLeaveMessage">
			<tr>
				<td><a href="${ctx}/run/runLeaveMessage/form?id=${runLeaveMessage.id}">
					${runLeaveMessage.name}
				</a></td>
				<td>
					${runLeaveMessage.phoneNumber}
				</td>
				<td>
					${runLeaveMessage.email}
				</td>
				<td>
					${runLeaveMessage.address}
				</td>
				<td>
					${runLeaveMessage.content}
				</td>
				<td>
					${fns:getDictLabel(runLeaveMessage.state, 'leave_message_state', '')}
				</td>
				<td>
					<fmt:formatDate value="${runLeaveMessage.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="run:runLeaveMessage:edit"><td>
    				<%--<a href="${ctx}/run/runLeaveMessage/form?id=${runLeaveMessage.id}">修改</a>
					<a href="${ctx}/run/runLeaveMessage/delete?id=${runLeaveMessage.id}" onclick="return confirmx('确认要删除该留言吗？', this.href)">删除</a>--%>
					<a href="">标记已读</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>