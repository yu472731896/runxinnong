<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>轮播图管理</title>
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/run/runSlideShow/">轮播图列表</a></li>
		<li class="active"><a href="${ctx}/run/runSlideShow/form?id=${runSlideShow.id}">轮播图<shiro:hasPermission name="run:runSlideShow:edit">${not empty runSlideShow.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="run:runSlideShow:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="runSlideShow" action="${ctx}/run/runSlideShow/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">标题：</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">图片上传：</label>
			<div class="controls">
				<form:hidden id="path" path="path" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="path" type="files" uploadPath="/run/runSlideShow" selectMultiple="false"/>
			</div>
		</div>
		<%--<div class="control-group">
			<label class="control-label">文章id：</label>
			<div class="controls">
				<form:input path="articleId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>--%>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>

		<!-- 加载编辑器的容器 -->
		<script id="container" name="content"  style="width:100%;height:350px;" type="text/plain">

		</script>
		<!-- 配置文件 -->
		<script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.config.js"></script>
		<!-- 编辑器源码文件 -->
		<script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.all.js"></script>
		<!-- 实例化编辑器 -->
		<script type="text/javascript">

            var ue = UE.getEditor('container');
            //对编辑器的操作最好在编辑器ready之后再做
            ue.ready(function() {

                /* document.getElementById("mydiv").innerHTML = '${runSlideShow.content}'; */
                //设置编辑器的内容
                /*  ue.setContent(${runSlideShow.content},false); */
                ue.setContent('${runSlideShow.content}');
                //获取html内容，返回: <p>hello</p>
                var html = ue.getContent();
                //获取纯文本内容，返回: hello
                var txt = ue.getContentTxt();
            });

            //请求自己的接口上传文件
            UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
            UE.Editor.prototype.getActionUrl = function (action) {
                if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadvideo') {
                    return '${ctx}/ueditor/uploadFile';
                } else {
                    return this._bkGetActionUrl.call(this, action);
                }
            };
		</script>

		<div class="form-actions">
			<shiro:hasPermission name="run:runSlideShow:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>