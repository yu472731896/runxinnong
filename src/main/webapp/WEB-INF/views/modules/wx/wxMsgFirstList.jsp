<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信首次回复管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function selectMsg(){
			top.$.jBox.open("iframe:${ctx}/wx/wxMsgText/selectList","选择发送消息列表",900,$(top.document).height() - 180,
					{id : "id-html",buttons : {"确定" : "ok","关闭" : false},
						submit : function(v, h, f) {
							if (v == "ok") {
								var title= h.find("iframe")[0].contentWindow.title;
								var baseId = h.find("iframe")[0].contentWindow.baseId;
								var type = h.find("iframe")[0].contentWindow.multType;
								if(baseId!=''){
									tosaveFirst(baseId,title,type);
								}
							}
						},
						loaded : function(h) {
							$(".jbox-content", top.document).css(
									"overflow-y", "hidden");
						}
					});
		}
		
		function tosaveFirst(baseId,title,type){
			$.ajax({
                url: '${ctx}/wx/wxMsgFirst/newsave',
                data:{baseId:baseId,title:title,msgType:type},
                success: function (result) {
                    if (result.success) {
                        location.href ="${ctx}/wx/wxMsgFirst/list?isselect=ture";
                    }
                },
                error: function () {
                    layer.msg("同步异常");
                }
            })
			
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/wx/wxMsgFirst/">微信首次回复列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="wxMsgFirst" action="${ctx}/wx/wxMsgFirst/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>消息标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"><input class="btn btn-primary" type="button" onclick="selectMsg();" value="选择首次回复消息"/></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>消息标题</th>
				<th>消息类型</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="wx:wxMsgFirst:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wxMsgFirst">
			<tr>
				<td>
					${wxMsgFirst.title}
				</td>
				<td>
					${wxMsgFirst.msgType=="text"?"文本消息":""}
					${wxMsgFirst.msgType=="news"?"图文消息":""}
				</td>
				<td>
					<fmt:formatDate value="${wxMsgFirst.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${wxMsgFirst.remarks}
				</td>
				<shiro:hasPermission name="wx:wxMsgFirst:edit"><td>
					<a href="${ctx}/wx/wxMsgFirst/delete?id=${wxMsgFirst.id}" onclick="return confirmx('确认要删除该微信首次回复吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
				
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>