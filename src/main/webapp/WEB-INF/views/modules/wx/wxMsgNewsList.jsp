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
	<script src="${ctxStatic}/template-web.js"></script>
	<script src="${ctxStatic}/layui/src/layui.js"></script>
	<script src="${ctxStatic}/layui/mylayui.js"></script>
	<script type="text/javascript">
	   layui.use(['layer', 'table'], function () {
		   var upload=layui.upload;
	        var laydate = layui.laydate;

			
	       
	   });
		$(document).ready(function() {
			
		});
		function edit(id){
			location.href = "${ctx}/wx/wxMsgNews/formnew?multType=${multType}&id="+id;
		}
		 function selectFans(id){
			   top.$.jBox.open("iframe:${ctx}/wx/wxAccountFans/listselect","选择粉丝列表",900,$(top.document).height() - 180,
						{id : "id-html",buttons : {"确定" : "ok","关闭" : false},
							submit : function(v, h, f) {
								if (v == "ok") {
									var openIds = h.find("iframe")[0].contentWindow.Test();
									if(openIds.length<1){
										 layer.msg("请选择1个粉丝");
									}else if(openIds.length==1){
										toqunfa(id,openIds);
									}else{
										layer.msg("只能选择1个粉丝");
									}
								}
							},
							loaded : function(h) {
								$(".jbox-content", top.document).css(
										"overflow-y", "hidden");
							}
						});
	     }
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function deleteNews(id){
			location.href = "${ctx}/wx/wxMsgNews/delete?multType=${multType}&id="+id;
		}
		
		function toqunfa(id,openIds){
			$.ajax({
                url: '/wx/sendNewsByOpenId',
                data: {
                	id:id,
                	openid: openIds.join(",")
                },
                success: function (result) {
                	if (result.success) {
                        layer.msg("群发成功");
                    } else {
                        layer.msg("群发失败");
                    }
                }
            })
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/wx/wxMsgNews/list?multType=${multType}">微信${multType==2?"多":"单" }图文消息列表</a></li>
		<shiro:hasPermission name="wx:wxMsgNews:edit"><li><a href="${ctx}/wx/wxMsgNews/formnew?multType=${multType}">微信${multType==2?"多":"单" }图文消息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wxMsgNews" action="${ctx}/wx/wxMsgNews/list?multType=${multType }" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>创建时间：</label>
				<input name="createDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wxMsgNews.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
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
    

		<c:forEach items="${page.list}" var="wxMsgNews">
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
			<c:if test="${multType==1 }">
				<li class="item">
			        <a href="${wxMsgNews.url}" target="_blank">
			            <img src="${wxMsgNews.picPath}" alt="" class="main">
			        </a>
			        <div class="content">
			            <h3 class="ellipsis">${wxMsgNews.title}</h3>
			            <p class="time">
			           	 <fmt:formatDate value="${wxMsgNews.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
			            </p>
			            <p class="abstract">
			            	${wxMsgNews.brief}
			            </p>
			        </div>
			        <div class="btn-group" data-id="${wxMsgNews.id}">
			            <button class="layui-btn btn-primary-reversal" onclick="edit('${wxMsgNews.id}')">编辑</button>
			            <button class="layui-btn btn-danger-reversal" onclick="selectFans('${wxMsgNews.id}')">群发</button>
			            <button class="layui-btn btn-danger-reversal" onclick="deleteNews('${wxMsgNews.id}')">删除</button>
			        </div>
			    </li>
			</c:if>
			
    		<c:if test="${multType==2 }">
			    <li class="item">
			       <a href="${wxMsgNews.url}" target="_blank">
			            <img src="${wxMsgNews.picPath}" alt="" class="main">
			        </a>
			        <div class="content">
			             <h3 class="ellipsis">${wxMsgNews.title}</h3>
			            <div class="doc clear-after">
			                <a href="#"><img src="${wxMsgNews.articles[1].picUrl}" alt=""></a>
			                <p class="ellipsis"><a href="${wxMsgNews.articles[1].url}" target="_blank">${wxMsgNews.articles[1].title}</a></p>
			            </div>
			            <c:if test="${fn:length(wxMsgNews.articles)>2 }">
			            <p class="more">
			                <i class="iconfont icon-gengduo"></i>更多${fn:length(wxMsgNews.articles)-2}条未展示
			            </p>
			            </c:if>
			        </div>
			        <div class="btn-group" data-id="${wxMsgNews.id}">
			            <button class="layui-btn btn-primary-reversal" onclick="edit('${wxMsgNews.id}')">编辑</button>
			            <button class="layui-btn btn-danger-reversal" onclick="selectFans('${wxMsgNews.id}')">群发</button>
			            <button class="layui-btn btn-danger-reversal" onclick="deleteNews('${wxMsgNews.id}')">删除</button>
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