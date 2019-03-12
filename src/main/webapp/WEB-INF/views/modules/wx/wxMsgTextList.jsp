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
	   function selectFans(id){
		   top.$.jBox.open("iframe:${ctx}/wx/wxAccountFans/listselect","选择粉丝列表",900,$(top.document).height() - 180,
					{id : "id-html",buttons : {"确定" : "ok","关闭" : false},
						submit : function(v, h, f) {
							if (v == "ok") {
								var openIds = h.find("iframe")[0].contentWindow.Test();
								if(openIds.length<2){
									 layer.msg("至少选择2个粉丝");
								}else{
									toqunfa(id,openIds);
								}
							}
						},
						loaded : function(h) {
							$(".jbox-content", top.document).css(
									"overflow-y", "hidden");
						}
					});
     }
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function toqunfa(id,openIds){
			$.ajax({
                url: '/wx/massSendTextByOpenIds',
                data: {
                	textId:id,
                    openIds: openIds.join(",")
                },
                success: function (result) {
                    if ( result == "1") {
                        layer.msg("群发成功");
                        layer.close(index);
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
		<li class="active"><a href="${ctx}/wx/wxMsgText/list">微信文本消息表列表</a></li>
		<shiro:hasPermission name="wx:wxMsgText:edit"><li><a href="${ctx}/wx/wxMsgText/form">微信文本消息表添加</a></li></shiro:hasPermission>
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
				<th>消息标题</th>
				<th>消息描述</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="wx:wxMsgText:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wxMsgText">
			<tr>
				<td><a href="${ctx}/wx/wxMsgText/form?id=${wxMsgText.id}">
					${wxMsgText.title}
				</a></td>
				<td>
					${wxMsgText.content}
				</td>
				<td>
					<fmt:formatDate value="${wxMsgText.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${wxMsgText.remarks}
				</td>
				<shiro:hasPermission name="wx:wxMsgText:edit"><td>
					<a href="#" onclick="selectFans('${wxMsgText.id}')">群发</a>
    				<a href="${ctx}/wx/wxMsgText/form?id=${wxMsgText.id}">修改</a>
					<a href="${ctx}/wx/wxMsgText/delete?id=${wxMsgText.id}" onclick="return confirmx('确认要删除该微信文本消息表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>