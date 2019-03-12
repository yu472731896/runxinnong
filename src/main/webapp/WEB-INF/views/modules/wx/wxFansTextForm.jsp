<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>粉丝发送消息管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		
		function selectContent(){
			top.$.jBox.open("iframe:${ctx}/wx/wxMsgText/selectList?forward=wxMsgTextListselectForContent","选择发送消息列表",900,$(top.document).height() - 180,
					{id : "id-html",buttons : {"确定" : "ok","关闭" : false},
						submit : function(v, h, f) {
							if (v == "ok") {
								var content = h.find("iframe")[0].contentWindow.content;
								$("#reply").val(content);
							}
						},
						loaded : function(h) {
							$(".jbox-content", top.document).css(
									"overflow-y", "hidden");
						}
					});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/wx/wxFansText/">粉丝发送消息管理列表</a></li>
		<li class="active"><a href="${ctx}/wx/wxFansText/form?id=${wxFansText.id}">粉丝发送消息管理<shiro:hasPermission name="wx:wxFansText:edit">${not empty wxFansText.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="wx:wxFansText:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="wxFansText" action="${ctx}/wx/wxFansText/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<div class="control-group">
			<label class="control-label">发送时间：</label>
			<div class="controls">
				<input name="createDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${wxFansText.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">消息内容：</label>
			<div class="controls">
				<form:textarea path="content" htmlEscape="false" rows="4" readonly="true" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">消息类型：</label>
			<div class="controls">
				<form:input path="type" htmlEscape="false" readonly="true" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">粉丝昵称：</label>
			<div class="controls">
				<input type="text" name="fans.nickNameStr" value="${wxFansText.fans.nickNameStr }" readonly="true" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">回复内容：</label>
			<div class="controls">
				<form:textarea path="reply" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
				<input id="btnCancel" class="btn" type="button" value="选择内容模板" onclick="selectContent();"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="wx:wxFansText:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="发 送"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>