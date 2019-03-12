<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信图片信息管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="/static/layui/css/layui.css">
    <link rel="stylesheet" href="/static/weixin/css/iconfont.css">
    <link rel="stylesheet" href="/static/weixin/css/build/common.css">
    <link rel="stylesheet" href="/static/weixin/viewerjs/viewer.min.css">
    <script src="/static/jquery/jquery-1.8.3.min.js"></script>
	<script src="/static/pagination/pagination.js"></script>
	<script src="/static/template-web.js"></script>
	<script src="/static/layui/src/layui.js"></script>
	<script src="/static/layui/mylayui.js"></script>
	<script src="/static/weixin/viewerjs/viewer.min.js"></script>
	<style type="text/css">
		.wxmp-list-image li .img-box {
		    display: table-cell;
		    width: 180px;
		    height: 180px;
		    text-align: center;
		    vertical-align: middle;
		}
		.wxmp-list-image li {
		    width: 180px;
		    height: 180px;
		    float: left;
		    position: relative;
		    overflow: hidden;
		    border: 1px solid #beceeb;
		    box-sizing: content-box;
		    line-height: 1;
		    margin-right: 20px;
		    margin-bottom: 20px;
		    background: #fff;
		    -webkit-transition: all 300ms;
		    -moz-transition: all 300ms;
		    -ms-transition: all 300ms;
		    -o-transition: all 300ms;
		    transition: all 300ms;
		}
		.wxmp-list-image li .btn-box {
		    display: block;
		    position: absolute;
		    left: 0;
		    bottom: -40px;
		    right: 0;
		    -webkit-transition: all 300ms;
		    -moz-transition: all 300ms;
		    -ms-transition: all 300ms;
		    -o-transition: all 300ms;
		    transition: all 300ms;
		    line-height: 40px;
		    text-align: center;
		    background: rgba(0,0,0,0.4);
		}
		.wxmp-list-image li .img-main {
		    max-width: 160px;
		    max-height: 160px;
		    vertical-align: middle;
		    cursor: -webkit-zoom-in;
		    cursor: zoom-in;
		}
		.wxmp-list-image li .layui-btn {
		    background: transparent !important;
		    padding: 0 5px;
		}
		.wxmp-list-image:before {
		    content: "";
		    display: table;
		}
		.wxmp-list-image:after {
		    content: "";
		    display: block;
		    clear: both;
		}
		.wxmp-list-image li:hover .btn-box {
		    bottom: 0;
		}
		[class^="icon-"], [class*=" icon-"]{
		    display: inline-block;
		    width: auto;
		    height: auto;
		    margin-top: 1px;
	        line-height: 38px;
		    vertical-align: text-top;
		    background-image: none;
		    background-position: none;
		    background-repeat: none;
		}
	</style>
	<script type="text/javascript">
		layui.use(['layer',"upload","laydate"], function () {
	        var layer = layui.layer;
	        var table = layui.table;
		});
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		// 删除
        function deleteImg (id){
        	showConfirm("确认删除？",function () {
        		location.href="${ctx}/wx/wxImgResource/delete?id="+id;
            });
     }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/wx/wxImgResource/">微信图片信息列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="wxImgResource" action="${ctx}/wx/wxImgResource/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tbody>
		<tr>
			<td colspan="6">
				<div class="layui-anim layui-anim-upbit">
					<div id="image_wrap">
						<ul class="wxmp-list-image">
						    <c:forEach items="${page.list}" var="wxImgResource">
							    <li>
							        <div class="img-box">
							            <img src="${wxImgResource.url }" alt="" class="img-main">
							        </div>
							        <div class="btn-box" data-id="${wxImgResource.id }">
							            <button class="layui-btn" onclick="deleteImg('${wxImgResource.id}')"><i class="iconfont icon-delete"></i></button>
							            <a class="layui-btn" href="${wxImgResource.url }"  download="${wxImgResource.url.substring(wxImgResource.url.lastIndexOf('/')+1) }"><i class="iconfont icon-icondownload"></i></a>
							        </div>
							    </li>
						  	</c:forEach>
						</ul>
					</div>
				</div>
			</td>
		</tr>
		<%-- <c:forEach items="${page.list}" var="wxImgResource">
			<tr>
				<td><a href="${ctx}/wx/wxImgResource/form?id=${wxImgResource.id}">
					${wxImgResource.name}
				</a></td>
				<td>
					<fmt:formatDate value="${wxImgResource.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${wxImgResource.remarks}
				</td>
				<shiro:hasPermission name="wx:wxImgResource:edit"><td>
    				<a href="${ctx}/wx/wxImgResource/form?id=${wxImgResource.id}">修改</a>
					<a href="${ctx}/wx/wxImgResource/delete?id=${wxImgResource.id}" onclick="return confirmx('确认要删除该微信图片信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach> --%>
		</tbody>
	</table>
	
	
		<%-- <c:forEach items="${page.list}" var="wxImgResource">
			<tr>
				<td><a href="${ctx}/wx/wxImgResource/form?id=${wxImgResource.id}">
					${wxImgResource.name}
				</a></td>
				<td>
					<fmt:formatDate value="${wxImgResource.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${wxImgResource.remarks}
				</td>
				<shiro:hasPermission name="wx:wxImgResource:edit"><td>
    				<a href="${ctx}/wx/wxImgResource/form?id=${wxImgResource.id}">修改</a>
					<a href="${ctx}/wx/wxImgResource/delete?id=${wxImgResource.id}" onclick="return confirmx('确认要删除该微信图片信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach> --%>
	<div class="pagination">${page}</div>
</body>
</html>