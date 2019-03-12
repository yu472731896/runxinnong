<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信粉丝表管理</title>
	<link rel="stylesheet" href="/static/layui/css/layui.css">
	<script src="/static/layui/src/layui.js"></script>
	<script src="/static/layui/mylayui.js"></script>
		<link type="text/css" rel="stylesheet" href="/static/easyui/easyui.css" />
	<link rel="stylesheet" type="text/css" href="/static/easyui/icon.css">
    <link rel="stylesheet" type="text/css" href="/static/easyui/demo.css">
	
	<!-- easyui调用 -->
	<script src="${ctxStatic}/easyui/jquery.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/easyui/jquery.easyui.min.js" type="text/javascript"></script>
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
		fitCoulms();
		$('#contentTable').datagrid();	 
	});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function Test(){
			var x = $('#contentTable').datagrid('getSelections');
			var openIds=[];
			for(var i=0; i<x.length; i++){
				openIds.push(x[i].openId);
			}
			return openIds;
		}
			
		function fitCoulms(){
		    $('#contentTable').datagrid({
		        fitColumns:true
		    });
		}
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="wxAccountFans" action="${ctx}/wx/wxAccountFans/listselect" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" width="100%" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true,width:60"></th>
				<th data-options="field:'openId',width:60,hidden:true"></th>
				<th data-options="field:'nickNameStr',width:180">昵称</th>
				<th data-options="field:'headImgUrl',width:100">头像</th>
				<th data-options="field:'gender',width:50">性别</th>
				<th data-options="field:'province',width:180">省/市</th>
				<th data-options="field:'subscribeTime',width:180">关注时间</th>
				<th data-options="field:'updateDate',width:180">更新时间</th>
				<th data-options="field:'remarks',width:180">备注信息</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wxAccountFans">
			<tr>
				<td>
					
				</td>
				<td>${wxAccountFans.openId }</td>
				<td>
					${wxAccountFans.nickNameStr}</td>
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
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>