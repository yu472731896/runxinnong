<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信视频文件管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="/static/layui/css/layui.css">
    <link rel="stylesheet" href="/static/weixin/css/iconfont.css">
    <link rel="stylesheet" href="/static/weixin/css/build/common.css">
    <script src="/static/jquery/jquery-1.8.3.min.js"></script>
	<script src="/static/pagination/pagination.js"></script>
	<script src="/static/template-web.js"></script>
	<script src="/static/layui/src/layui.js"></script>
	<script src="/static/layui/mylayui.js"></script>
	<script src="/static/weixin/util.js"></script>
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
		layui.use(['layer',"upload"], function () {
	        var upload = layui.upload;
	        var uploader = upload.render({
	            elem: '#uploader',
	            url: '${ctx}/wx/wxMediaFiles/uploadFile',
	            size: 10240,
	            accept:"video",
	            exts:"mp4|MP4",
	            before: function(){
	            },
	            done: function(result){
	                if (result.success) {
	                    layer.closeAll('loading');
	                    $("#uploader").html("<i class='iconfont icon-bofang' data-src='"+result.data.src+"'>222222</i><p>点击播放预览，点击空白处或将文件拖拽到此处重新上传</p>");
	                    $("#add_path").val(result.data.src);
	                    $("#add_url").val(result.data.url);
	                    alert(result.data.src);
	                    alert(result.data.url);
	                } else {
	                    layer.msg(result.msg || "上传接口异常");
	                }
	            },
	            error: function(){
	                layer.closeAll('loading');
	                layer.msg(result.msg || "上传接口异常");
	            }
	        });

	        
	        var player = null;

	        util.loadCss("/static/weixin/video-js/video-js.css");
	        util.loadJs("/static/weixin/video-js/video.js", function () {

	        });
	        // 播放
	        $("#uploader").on("click",".icon-bofang",function () {
	            popvideo($(this).attr("data-src"));
	            return false
	        });
	        
	        window.popvideo = function (videoUrl) {
	            var $p = $("#player");
	            $p.length > 0 && $p.remove();

	            var TEMP_video =
	                '<div id="player" style="width:900px;height:600px;display:none;overflow: hidden">'
	                + '<video width="900" height="600" controls="controls" class="video-js" id="video">'
	                + '<source src="' + videoUrl + '">'
	                + '</video>'
	                + '</div>';

	            $("body").append(template.render(TEMP_video));
	            var videoPlayer = videojs('video');
	            layer.open({
	                type: 1,
	                content: $("#player"),
	                anim: '1',
	                title: "视频",
	                area: '900px',
	                scrollbar: false,
	                skin: "",
	                shadeClose: true,
	                btn: 0,
	                success: function () {
	                    videoPlayer.play();
	                },
	                end: function () {
	                    videoPlayer.dispose()
	                }
	            });
	        }
	        
	        
	        
	    })
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/wx/wxMediaFiles/list?mediaType=${wxMediaFiles.mediaType}">微信视频文件列表</a></li>
		<li class="active"><a href="${ctx}/wx/wxMediaFiles/formnew?id=${wxMediaFiles.id}">微信视频文件<shiro:hasPermission name="wx:wxMediaFiles:edit">${not empty wxMediaFiles.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="wx:wxMediaFiles:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="wxMediaFiles" action="${ctx}/wx/wxMediaFiles/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" name="mediaType" value="video"/>
	<div class="fsh-pop">
    <div id="add_form" class="layui-form">
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label"><i>*</i>标题</label>
            <div class="layui-input-block">
               <form:input path="title" htmlEscape="false" maxlength="50" class="input-xlarge "/>
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label"><i>*</i>上传<br>(只支持mp4)</label>
            <div class="layui-input-block">
                <input type="hidden" name="uploadUrl" id="add_path" value="">
                <input type="hidden" name="url" id="add_url" value="">
                <div class="layui-upload-drag" id="uploader">
                    <i class="layui-icon"></i>
                    <p>点击上传，或将文件拖拽到此处</p>
                </div>
                <button ></button>
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label"><i>*</i>描述</label>
            <div class="layui-input-block">
            	<form:textarea path="introduction" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
            </div>
        </div>
    </div>
</div>
<div class="form-actions">
			<shiro:hasPermission name="wx:wxMediaFiles:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
</form:form>
</body>
</html>