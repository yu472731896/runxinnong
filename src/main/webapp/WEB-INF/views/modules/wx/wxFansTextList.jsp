<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>粉丝发送消息管理管理</title>
	<meta name="decorator" content="default"/>
	<style>
    img.fans-portrait{
        width:50px;
        height:50px;
    }
    .fsh-rightPanel .layui-table-body .layui-table-cell {
        height: 50px;
        line-height: 50px;
    }
</style>
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
		<li class="active"><a href="${ctx}/wx/wxFansText/">粉丝发送消息管理列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="wxFansText" action="${ctx}/wx/wxFansText/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>消息内容：</label>
				<form:input path="content" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>粉丝昵称：</label>
				<form:input path="fans.nickNameStr" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>发送时间</th>
				<th>消息内容</th>
				<th>消息类型</th>
				<th>粉丝昵称</th>
				<th>粉丝头像</th>
				<th>性别</th>
				<th>国家</th>
				<th>省/市</th>
				<th>回复内容</th>
				<th>回复时间</th>
				
				<shiro:hasPermission name="wx:wxFansText:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wxFansText">
			<tr>
				<td>
					<fmt:formatDate value="${wxFansText.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td><a href="${ctx}/wx/wxFansText/form?id=${wxFansText.id}">
					${wxFansText.content}
				</a></td>
				<td>
					${wxFansText.type}
				</td>
				<td>
					${wxFansText.fans.nickNameStr}
				</td>
				<td>
					<img class='fans-portrait' src="${wxFansText.fans.headImgUrl}" />
				</td>
				<td>
					${wxFansText.fans.gender==0?"女":""}
					${wxFansText.fans.gender==1?"男":""}
					${wxFansText.fans.gender==2?"未知":""}
				</td>
				<td>
					${wxFansText.fans.country}
				</td>
				<td>
					${wxFansText.fans.province}-${wxFansText.fans.city}
				</td>
				<td>
					${wxFansText.reply}
				</td>
				<td>
					<fmt:formatDate value="${wxFansText.reTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				
				<shiro:hasPermission name="wx:wxFansText:edit"><td>
    				<a href="${ctx}/wx/wxFansText/form?id=${wxFansText.id}">修改</a>
					<a href="${ctx}/wx/wxFansText/delete?id=${wxFansText.id}" onclick="return confirmx('确认要删除该粉丝发送消息管理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>