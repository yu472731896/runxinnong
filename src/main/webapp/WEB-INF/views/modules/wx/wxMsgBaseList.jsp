<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信消息类主表管理</title>
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
		<li class="active"><a href="${ctx}/wx/wxMsgBase/">微信消息类主表列表</a></li>
		<shiro:hasPermission name="wx:wxMsgBase:edit"><li><a href="${ctx}/wx/wxMsgBase/form">微信消息类主表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wxMsgBase" action="${ctx}/wx/wxMsgBase/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>是否可用：</label>
				<form:input path="enable" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>息消类型</th>
				<th>是否可用</th>
				<th>消息阅读数</th>
				<th>消息点赞数</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="wx:wxMsgBase:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wxMsgBase">
			<tr>
				<td><a href="${ctx}/wx/wxMsgBase/form?id=${wxMsgBase.id}">
					${wxMsgBase.msgType}
				</a></td>
				<td>
					${wxMsgBase.enable}
				</td>
				<td>
					${wxMsgBase.readCount}
				</td>
				<td>
					${wxMsgBase.favourCount}
				</td>
				<td>
					<fmt:formatDate value="${wxMsgBase.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${wxMsgBase.remarks}
				</td>
				<shiro:hasPermission name="wx:wxMsgBase:edit"><td>
    				<a href="${ctx}/wx/wxMsgBase/form?id=${wxMsgBase.id}">修改</a>
					<a href="${ctx}/wx/wxMsgBase/delete?id=${wxMsgBase.id}" onclick="return confirmx('确认要删除该微信消息类主表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>