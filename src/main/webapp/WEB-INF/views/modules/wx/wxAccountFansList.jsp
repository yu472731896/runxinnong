<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信粉丝表管理</title>
	<link rel="stylesheet" href="${ctxStatic}/static/layui/css/layui.css">
	<script src="${ctxStatic}/template-web.js"></script>
	<script src="${ctxStatic}/layui/src/layui.js"></script>
	<script src="${ctxStatic}/layui/mylayui.js"></script>
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
	 layui.use(['layer', 'table'], function () {
	        var layer = layui.layer;
	        var $ = layui.$;
	        var table = layui.table;
			
		    // 批量同步粉丝
	        $("#fans_sync").click(function () {
	            layer.confirm('确认同步粉丝？', {
	                icon: 7,
	                title: "提示",
	                btn: ['确认', '取消'] //按钮
	            }, function () {
	                $.ajax({
	                    url: '${ctxf}/wx/syncAccountFansList',
	                    success: function (result) {
	                        if (result.success) {
	                            layer.msg("同步成功");
	                            location.href ="${ctx}/wx/wxAccountFans/list?istongbu=ture";
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
		<li class="active"><a href="${ctx}/wx/wxAccountFans/list">微信粉丝表列表</a></li>
		
	</ul>
	<form:form id="searchForm" modelAttribute="wxAccountFans" action="${ctx}/wx/wxAccountFans/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"><button class="layui-btn btn-danger" id="fans_sync" type="button">批量同步粉丝</button></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>昵称</th>
				<th>头像</th>
				<th>性别</th>
				<th>省/市</th>
				<th>关注时间</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="wx:wxAccountFans:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wxAccountFans">
			<tr>
				<td><a href="${ctx}/wx/wxAccountFans/form?id=${wxAccountFans.id}">
					${wxAccountFans.nickNameStr}
				</a></td>
				<td>
					<img class='fans-portrait' src="${wxAccountFans.headImgUrl}" />
				</td>
				<td>
					${wxAccountFans.gender==0?"女":""}
					${wxAccountFans.gender==1?"男":""}
					${wxAccountFans.gender==2?"未知":""}
				</td>
				<td>
					${wxAccountFans.province}-${wxAccountFans.city}
				</td>
				<td>
					<fmt:formatDate value="${wxAccountFans.subscribeTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${wxAccountFans.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${wxAccountFans.remarks}
				</td>
				<shiro:hasPermission name="wx:wxAccountFans:edit"><td>
    				<a href="${ctx}/wx/wxAccountFans/form?id=${wxAccountFans.id}">修改</a>
					<a href="${ctx}/wx/wxAccountFans/delete?id=${wxAccountFans.id}" onclick="return confirmx('确认要删除该微信粉丝表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>