<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信关键词信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			var msgType = "${wxMsgKey.msgType}";
			if(msgType=='text'){
				$("#msgTypeStr").val("文本消息");
			}else if(msgType=='news'){
				$("#msgTypeStr").val("图文消息");
			}else{
				$("#msgTypeStr").val("");
			}
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
		function selectMsg(){
			top.$.jBox.open("iframe:${ctx}/wx/wxMsgText/selectList","选择发送消息列表",900,$(top.document).height() - 180,
					{id : "id-html",buttons : {"确定" : "ok","关闭" : false},
						submit : function(v, h, f) {
							if (v == "ok") {
								var title= h.find("iframe")[0].contentWindow.title;
								var baseId = h.find("iframe")[0].contentWindow.baseId;
								var type = h.find("iframe")[0].contentWindow.multType;
								if(baseId!=''){
									$("#baseId").val(baseId);
									$("#title").val(title);
									$("#msgType").val(type);
									if(type=='text'){
										$("#msgTypeStr").val("文本消息");
									}else if(type=='news'){
										$("#msgTypeStr").val("图文消息");
									}else{
										$("#msgTypeStr").val("");
									}
								}
							}
						},
						loaded : function(h) {
							$(".jbox-content", top.document).css(
									"overflow-y", "hidden");
						}
					});
		}
		function checkInputCode(){//验证关键词是否重复
			var inputCode = $("#inputCode").val();
			if(inputCode!=''){
				if($("#id").val()!=""&&"${wxMsgKey.inputCode}"==inputCode){
					
				}else{
				$.ajax({
	                url: '${ctx}/wx/wxMsgKey/checkInputCode',
	                data:{inputCode:$("#inputCode").val()},
	                success: function (result) {
	                    if (result.success) {
	                    	$("#keyMessage").text("该关键词已存在。");
	                    	$("#inputCode").val("");
	                    }else{
	                    	$("#keyMessage").text("");
	                    }
	                },
	                error: function () {
	                    layer.msg("检测异常");
	                }
	            })
				}
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/wx/wxMsgKey/">微信关键词信息列表</a></li>
		<li class="active"><a href="${ctx}/wx/wxMsgKey/form?id=${wxMsgKey.id}">微信关键词信息<shiro:hasPermission name="wx:wxMsgKey:edit">${not empty wxMsgKey.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="wx:wxMsgKey:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="wxMsgKey" action="${ctx}/wx/wxMsgKey/save" method="post" class="form-horizontal">
		<form:hidden path="id" id="id" />
		<form:hidden path="baseId" id="baseId"/>
		<form:hidden path="msgType" id="msgType"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">关键词：</label>
			<div class="controls">
				<form:input path="inputCode" id="inputCode" htmlEscape="false" onblur="checkInputCode();" maxlength="50" class="input-xlarge "/>
				<span style="color: red;" id="keyMessage"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">消息标题：</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" id="title" maxlength="255" onclick="selectMsg();" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">息消类型：</label>
			<div class="controls">
				<input htmlEscape="false" id="msgTypeStr" maxlength="500" onclick="selectMsg();" value="" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="wx:wxMsgKey:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>