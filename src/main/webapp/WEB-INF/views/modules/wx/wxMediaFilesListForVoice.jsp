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
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		layui.use(['layer',"upload"], function () {
	        var upload = layui.upload;

	        
	        var player = null;

	        util.loadCss("/static/weixin/video-js/video-js.css");
	        util.loadJs("/static/weixin/video-js/video.js", function () {

	        });
	        
	        // 添加
	        var uploader = upload.render({
	            elem: '#video_add',
	            url: '/wx/wxMediaFiles/addMateria',
	            size: 2048,
	            data:{type:"voice"},
	            accept:"audio",
	            exts: 'mp3|MP3|wma|WMA|wav|WAV|amr|AMR',
	            before: function(){
	                showLoading();
	            },
	            done: function(res){
	                layer.closeAll('loading');
	                if(res.success){
	                    layer.msg("上传成功");
	                    reloadTable(tableObj);
	                }else{
	                    layer.msg("上传失败");
	                }
	            }
	            ,error: function(){
	                layer.closeAll('loading');
	                layer.msg("上传失败");
	            }
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
		<li class="active"><a href="${ctx}/wx/wxMediaFiles/list?mediaType=${wxMediaFiles.mediaType}">微信视频文件列表</a></li>
		<shiro:hasPermission name="wx:wxMediaFiles:edit"><li><a href="${ctx}/wx/wxMediaFiles/formnew">微信视频文件添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="wxMediaFiles" action="${ctx}/wx/wxMediaFiles/list?mediaType=${wxMediaFiles.mediaType }" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>标题：</label>
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
				<th>标题</th>
				<th>简介说明</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="wx:wxMediaFiles:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wxMediaFiles">
			<tr>
				<td>
					${wxMediaFiles.title}
				</td>
				<td>
					${wxMediaFiles.introduction}
				</td>
				<td>
					<fmt:formatDate value="${wxMediaFiles.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${wxMediaFiles.remarks}
				</td>
				<shiro:hasPermission name="wx:wxMediaFiles:edit"><td>
				
				   	<a href="#" onclick="popvideo('${wxMediaFiles.uploadUrl}');">预览</a>
				   	<a href="${wxMediaFiles.uploadUrl }" download="${wxMediaFiles.uploadUrl.substring(wxMediaFiles.uploadUrl.lastIndexOf('/')+1) }" > 下载</a>
					<a href="${ctx}/wx/wxMediaFiles/delete?id=${wxMediaFiles.id}" onclick="return confirmx('确认要删除该微信视频文件吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>