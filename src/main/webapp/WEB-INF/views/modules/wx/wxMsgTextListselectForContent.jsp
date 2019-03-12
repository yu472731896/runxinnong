<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信文本消息表管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="/static/layui/css/layui.css">
	<script src="/static/layui/src/layui.js"></script>
<script src="/static/layui/mylayui.js"></script>
	<script type="text/javascript">
	   layui.use(['layer', 'table'], function () {
	        var layer = layui.layer;
	        var table = layui.table;
			
	       
	   });
	   var checkindex = '';
	   var title = '';
	   var multType = 'text';
	   var baseId = '';
	   var content = '';
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function checkforselect(id,selecttitle,index,selectbaseId,selectcontent){
			if(checkindex != ''){
				$("#index"+checkindex).prop("checked",false);				
			}
			$("#index"+index).prop("checked",true);
			checkindex = index;
			title = selecttitle;
			baseId = selectbaseId;
			content = selectcontent;
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/wx/wxMsgText/selectList">微信文本消息表列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="wxMsgText" action="${ctx}/wx/wxMsgText/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>消息标题：</label>
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
				<th></th>
				<th>消息标题</th>
				<th>消息描述</th>
				<th>更新时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wxMsgText" varStatus="index">
			<tr onclick="checkforselect('${wxMsgText.id}','${wxMsgText.title }','${index.count }','${wxMsgText.baseId }','${wxMsgText.content }')">
				<td><input type="checkbox" id="index${index.count }"></td>
				<td>
					${wxMsgText.title}
				</td>
				<td>
					${wxMsgText.content}
				</td>
				<td>
					<fmt:formatDate value="${wxMsgText.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>