<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>推广二维码模板管理</title>
	<meta name="decorator" content="default"/>
	<style>
		#caozuo a:hover{
			background:rgba(0,0,0,0.8) !important;
			text-decoration:none;
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
		<li class="active"><a href="${ctx}/sys/shopQrcodeTemp/list">推广二维码模板列表</a></li>
		<li><a href="${ctx}/sys/shopQrcodeTemp/form">推广二维码模板添加</a></li>
	</ul>
	<c:forEach items="${page.list}" var="shopQrcodeTemp">
	<div style="width:200px;height:400px;margin-right:10px;float:left;position:relative;">
		<img src="${shopQrcodeTemp.qrimageUrl }" style="width:100%;height:100%;">
		<div id="caozuo" style="width:80%;position:absolute;top:40%;left:10%;text-align:center;">
			<a href="${ctx}/sys/shopQrcodeTemp/form?id=${shopQrcodeTemp.id}" style="display:block;padding:5px 20px;background:rgba(0,0,0,0.5);color:#fff;border:1px solid #ccc;margin-bottom:5px;">编辑</a>
			<a href="${ctx}/sys/shopQrcodeTemp/delete?id=${shopQrcodeTemp.id}" onclick="return confirmx('确认要删除该模板吗？', this.href)" style="display:block;padding:5px 20px;background:rgba(0,0,0,0.5);color:#fff;border:1px solid #ccc;">删除</a>
		</div>
	</div>
	</c:forEach>
</body>
</html>