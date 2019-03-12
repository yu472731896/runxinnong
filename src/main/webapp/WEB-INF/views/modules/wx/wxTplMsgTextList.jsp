<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信模板消息管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="/static/layui/css/layui.css">
	<script src="/static/layui/src/layui.js"></script>
	<script src="/static/layui/mylayui.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		 layui.use(['layer', 'table'], function () {
		        var layer = layui.layer;
		        var $ = layui.$;
		        var table = layui.table;
				
			    // 批量同步粉丝
		        $("#fans_sync").click(function () {
		            layer.confirm('确认同步消息模板？', {
		                icon: 7,
		                title: "提示",
		                btn: ['确认', '取消'] //按钮
		            }, function () {
		                $.ajax({
		                    url: '/wx/syncWxTplMsgTextList',
		                    success: function (result) {
		                        if (result.success) {
		                            layer.msg("同步成功");
		                            location.href ="${ctx}/wx/wxTplMsgText/list?istongbu=ture";
		                        }
		                    },
		                    error: function () {
		                        layer.msg("同步异常");
		                    }
		                })
		            }, function () {
		                layer.closeAll();
		            });
		        });
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
		<li class="active"><a href="${ctx}/wx/wxTplMsgText/list">微信模板消息列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="wxTplMsgText" action="${ctx}/wx/wxTplMsgText/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>消息标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="200" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"><button class="layui-btn btn-danger" id="fans_sync" type="button">批量同步模板消息</button></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th width="250px;">模板ID</th>
				<th width="200px;">消息标题</th>
				<th>模板内容</th>
				<th width="150px;">更新时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wxTplMsgText">
			<tr>
				<td>
					${wxTplMsgText.tplId}
				</td>
				<td><a href="${ctx}/wx/wxTplMsgText/form?id=${wxTplMsgText.id}">
					${wxTplMsgText.title}
				</a></td>
				<td>
					${wxTplMsgText.content}
				</td>
				<td>
					<fmt:formatDate value="${wxTplMsgText.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>