<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信图文消息管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="/static/layui/css/layui.css">
    <link rel="stylesheet" href="/static/weixin/css/iconfont.css">
    <link rel="stylesheet" href="/static/weixin/css/build/common.css">
    <script src="/static/jquery/jquery-1.8.3.min.js"></script>
	<script src="/static/pagination/pagination.js"></script>
	<script src="/static/template-web.js"></script>
	<script src="/static/layui/src/layui.js"></script>
	<script src="/static/layui/mylayui.js"></script>
	<script type="text/javascript">
	   layui.use(['layer', 'table'], function () {
	        var layer = layui.layer;
	        var table = layui.table;
			
	       
	   });
	   var checkindex = '';
	   var title = '';
	   var multType = 'news';
	   var baseId = '';
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function checkforselect(id,selecttitle,index,selectbaseId){
			if(checkindex != ''){
				$("#index"+checkindex).prop("checked",false);				
			}
			$("#index"+index).prop("checked",true);
			checkindex = index;
			title = selecttitle;
			baseId = selectbaseId;
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/wx/wxMsgText/selectList">微信文本消息表列表</a></li>
		<li class="${multType==1?'active':'' }"><a href="${ctx}/wx/wxMsgNews/selectList?multType=1">微信单图文消息表列表</a></li>
		<li class="${multType==2?'active':'' }"><a href="${ctx}/wx/wxMsgNews/selectList?multType=2">微信多图文消息表列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="wxMsgNews" action="${ctx}/wx/wxMsgNews/list?multType=${multType }" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tr>
		<td colspan="6">
		<ul class="wxmp-list-justify media-list">
    

		<c:forEach items="${page.list}" var="wxMsgNews" varStatus="index">
			<%-- <tr>
				<td><a href="${ctx}/wx/wxMsgNews/form?id=${wxMsgNews.id}">
					${wxMsgNews.title}
				</a></td>
				<td>
					${wxMsgNews.author}
				</td>
				<td>
					${wxMsgNews.brief}
				</td>
				<td>
					<fmt:formatDate value="${wxMsgNews.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${wxMsgNews.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${wxMsgNews.remarks}
				</td>
				<shiro:hasPermission name="wx:wxMsgNews:edit"><td>
    				<a href="${ctx}/wx/wxMsgNews/form?id=${wxMsgNews.id}">修改</a>
					<a href="${ctx}/wx/wxMsgNews/delete?id=${wxMsgNews.id}" onclick="return confirmx('确认要删除该微信图文消息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr> --%>
			<c:if test="${multType==1 }" >
				<li class="item" onclick="checkforselect('${wxMsgNews.id}','${wxMsgNews.title}','${index.count }','${wxMsgNews.baseId }')">
					<input type="checkbox" id="index${index.count }"> 
			            <img src="${wxMsgNews.picPath}" alt="" class="main">
			        <div class="content">
			            <h3 class="ellipsis">${wxMsgNews.title}</h3>
			            <p class="time">
			           	 <fmt:formatDate value="${wxMsgNews.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
			            </p>
			            <p class="abstract">
			            	${wxMsgNews.brief}
			            </p>
			        </div>
			    </li>
			</c:if>
			
    		<c:if test="${multType==2 }">
			    <li class="item" onclick="checkforselect('${wxMsgNews.id}','${wxMsgNews.title}','${index.count }','${wxMsgNews.baseId }')">
			            <input type="checkbox" id="index${index.count }"> 
			            <img src="${wxMsgNews.picPath}" alt="" class="main">
			        <div class="content">
			             <h3 class="ellipsis">${wxMsgNews.title}</h3>
			            <div class="doc clear-after">
			                <a href="#"><img src="${wxMsgNews.articles[1].picUrl}" alt=""></a>
			                <p class="ellipsis">${wxMsgNews.articles[1].title}</p>
			            </div>
			            <c:if test="${fn:length(wxMsgNews.articles)>2 }">
			            <p class="more">
			                <i class="iconfont icon-gengduo"></i>更多${fn:length(wxMsgNews.articles)-2}条未展示
			            </p>
			            </c:if>
			        </div>
			    </li>
    		</c:if>
		</c:forEach>
		</ul>
		</td>
		</tr>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>