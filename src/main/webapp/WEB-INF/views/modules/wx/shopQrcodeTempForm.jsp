<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>推广二维码模板管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/colorpick/colpick.css" type="text/css"/>
	<script type="text/javascript" src="${ctxStatic}/modules/shop/js/ajaxfileupload.js"></script>
	<script type="text/javascript" src="${ctxStatic}/colorpick/colpick.js"></script>
	<Style>
		.rightcont{
			width:50%;
			display:inline-block;
		}
		.clearfix:after{
			content:"";
			display:block;
			clear:both;
			visibility:hidden;
			height:0;
		}
		.clearfix{
			*+height:1px
		}
	</Style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/shopQrcodeTemp/list">推广二维码模板列表</a></li>
		<li class="active"><a href="${ctx}/sys/shopQrcodeTemp/form?id=${shopQrcodeTemp.id}">推广二维码模板<shiro:hasPermission name="wx:shopQrcodeTemp:edit">${not empty shopQrcodeTemp.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="wx:shopQrcodeTemp:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<div class="clearfix">
	<div id="ewmdiy" style="width:400px;height:600px;margin-top:20px;margin-left:20px;border:1px solid #ccc;position:Relative;display:inline-block;float:left;">
		<div id="EWMimg" style="width:150px;height:150px;position:absolute;top:75px;left:125px;background:url(${ctxStatic }/images/forTestEWM.png);background-size:100% 100%;">
		</div>
		<div id="usLogo" style="width:50px;height:50px;position:absolute;top:250px;left:100px;background:#f2f2f2;display:none;text-align:center;">logo</div>
		<div id="Nic" style="width:150px;height:40px;position:absolute;top:350px;left:75px;background:#dadada;display:none;font-size:14px;line-height:20px;text-align:center;">昵称</div>
	</div>
	<div class="rightcont">
		<div id="inputForm" class="form-horizontal">
			<input type="hidden" id="id" name="id" value="${shopQrcodeTemp.id}" />
			<div class="control-group">
				<label class="control-label">背景图片：</label>
				<div class="controls">
					<span style="display:block; left:5px;width:58px; height:58px; position:relative; background:url(${ctxStatic}/modules/shop/images/icon70.jpg) no-repeat;">
						<input type="hidden" id="imageId" name="logo" value="${shopQrcodeTemp.bgimageUrl}"/>
						<input type="file" id="uploadId" name="myfile" style="width:58px; height:58px; border:none; position:absolute; left:0px; top:0; opacity:0; cursor:pointer; z-index:9999;" onchange="ajaxFileUploadPub('${ctxFront}/shop/file/ajaxUpload', 'image', 'uploadId', 'imageId', 'showId');" value="" name="">
						<img id="showId" style="position:absolute; width:58px; height:58px; top:0px;left:0px;z-index:1;" src="${shopQrcodeTemp.bgimageUrl}" />
					</span>
					<b>（为了防止图片变形。建议图片分辨率为400x600像素）</b>
				</div>
				
			</div>
			<div class="control-group">
				<label class="control-label">是否显示描述文字：</label>
				<div class="controls">
					<input id="textWidth" type="checkbox" class="input-xlarge" onclick="changestate()"/>
				</div>
			</div>
			<div id="editFont" style="display:none">
				<div class="control-group">
					<label class="control-label">测试文字：</label>
					<div class="controls">
						<input id="forWrite" type="text" class="input-xlarge" oninput="wantWrite()">
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">文字的宽高：</label>
					<div class="controls">
						<input id="fontWidth" value="${shopQrcodeTemp.textWidth}" type="text" placeholder="文字框宽度">
						<input id="fontHeight" value="${shopQrcodeTemp.textHeight}" type="text" placeholder="文字框高度">
						<input type="button" value="确认" onclick="setFontWH()">
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">文字颜色：</label>
					<div class="controls">
						<input id="picker" value="${shopQrcodeTemp.textColor}" type="text" placeholder="选择颜色">
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">字体：</label>
					<div class="controls">
						<select id="setFontFamily">
							<option value ="">请选择</option>
						  	<option value ="weiruanyahei">微软雅黑</option>
						  	<option value ="KaiTi">楷体</option>
						  	<option value ="SimSun">宋体</option>
						</select>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">文字大小：</label>
					<div class="controls">
						<select id="setFontsize">
							<option value ="">请选择</option>
						  	<option value ="12px">12px</option>
						  	<option value ="14px">14px</option>
						  	<option value ="16px">16px</option>
						  	<option value ="18px">18px</option>
						  	<option value ="20px">20px</option>
						  	<option value ="22px">22px</option>
						  	<option value ="24px">24px</option>
						  	<option value ="26px">26px</option>
						  	<option value ="28px">28px</option>
						  	<option value ="30px">30px</option>
						  	<option value ="32px">32px</option>
						  	<option value ="34px">34px</option>
						  	<option value ="36px">36px</option>
						</select>
					</div>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">是否显示会员logo：</label>
				<div class="controls">
					<input id="userLogo" type="checkbox" class="input-xlarge" onclick="changeLogoState()"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">是否显示昵称：</label>
				<div class="controls">
					<input id="userNic" type="checkbox" class="input-xlarge" onclick="changeNicState()"/>
				</div>
			</div>
			<p style="text-align:Center;"><input type="button" value="保存" onclick="saveToserver()" class="btn-primary" style="padding:5px 20px;border:none;"></p>
		</div>
	</div>
	</div>
	<script type="text/javascript">
		var outD = document.getElementById('ewmdiy');
		$(document).ready(function() {
			$('#picker').colpick({    //颜色选择
				layout:'hex',
				submit:0,
				colorScheme:'dark',
				onChange:function(hsb,hex,rgb,el,bySetColor) {
					$("#fortext").css('color','#'+hex);
					if(!bySetColor) $(el).val('#'+hex);
				}
			}).keyup(function(){
				$(this).colpickSetColor(this.value);
			});
			
			$("#showId").on("load",function(){
				var imgurl = $("#imageId").val();
				$("#ewmdiy").css({"background":"url("+imgurl+") no-repeat center","background-size":"100% 100%"})
			});
			
			$("#setFontsize").change(function(){
				var fontsize = $("#setFontsize option:selected").val();
				$("#fortext").css({"font-size":fontsize,"line-height":fontsize});
		    });
			
			$("#setFontFamily").change(function(){
				var fontfamily = $("#setFontFamily option:selected").val();
				$("#fortext").css({"font-family":fontfamily});
		    });
			
			var EWMimg = document.getElementById('EWMimg');//二维码拖曳
	        var isDraging = false;
	        var startX = 0;
	        var startY = 0;
	        EWMimg.addEventListener('mousedown', function (e) {//鼠标事件1 - 在标题栏按下（要计算鼠标相对拖拽元素的左上角的坐标 ，并且标记元素为可拖动）
	        	$("#EWMimg").css("z-index","10");
	            var e = e || window.event;
	            startX = e.pageX - EWMimg.offsetLeft;
	            startY = e.pageY - EWMimg.offsetTop;
	            isDraging = true;
	        })
	        EWMimg.onmouseup = function (e) { //鼠标事件3 - 鼠标松开的时候（标记元素为不可拖动）
	            isDraging = false;
	            $("#EWMimg").css("z-index","1");
	        }
	        EWMimg.onmousemove = function (e) {//鼠标事件2 - 鼠标移动时（要检测，元素是否标记为移动）
	            var e = e || window.event;
	            if (isDraging === true) {
	                var moveX = e.pageX - startX,
	                    moveY = e.pageY - startY;
	                EWMimg.style.left = moveX + 'px';
	                EWMimg.style.top = moveY + 'px';
	                if((EWMimg.offsetLeft + EWMimg.offsetWidth)>outD.offsetWidth){
	                	EWMimg.style.left = outD.offsetWidth-EWMimg.offsetWidth - 2 +"px";
	                }
	               if(EWMimg.offsetLeft<0){
	            	   EWMimg.style.left = 0+"px"
	               }
	               if(EWMimg.offsetTop<0){
	            	   EWMimg.style.top = 0+"px"
	               }
	               if((EWMimg.offsetTop + EWMimg.offsetHeight) > outD.offsetHeight){
	            	   EWMimg.style.top = outD.offsetHeight-EWMimg.offsetHeight - 2 +"px";
	               }
	            }
	        };
	        
			if('${shopQrcodeTemp.id}' != ""){
				setImgs();
			}
		});
		
		function changestate(){
			if($("#textWidth").is(':checked')){
				$("#ewmdiy").append("<div id=\"fortext\"></div>");
				$("#fortext").css({"min-width":"100px","width":"100px","height":"50px","background":"#f3f3f3","position":"absolute","word-break":"break-all","line-height":"13px"});
				$("#forWrite").val("");
				$("#editFont").show();
				$("#fortext").text("")
				var oDrag = document.getElementById('fortext');
		        var isDraging2 = false;
		        var startX = 0;
		        var startY = 0;
		        oDrag.addEventListener('mousedown', function (e) {//鼠标事件1 - 在标题栏按下（要计算鼠标相对拖拽元素的左上角的坐标 ，并且标记元素为可拖动）
		        	$("#fortext").css("z-index","10");
		            var e = e || window.event;
		            startX = e.pageX - oDrag.offsetLeft;
		            startY = e.pageY - oDrag.offsetTop;
		            isDraging2 = true;
		        })
		        oDrag.onmouseup = function (e) { //鼠标事件3 - 鼠标松开的时候（标记元素为不可拖动）
		            isDraging2 = false;
		            $("#fortext").css("z-index","1");
		        }
		        oDrag.onmousemove = function (e) {//鼠标事件2 - 鼠标移动时（要检测，元素是否标记为移动）
		            var e = e || window.event;
		            if (isDraging2 === true) {
		                var moveX = e.pageX - startX,
		                    moveY = e.pageY - startY;
		                oDrag.style.left = moveX + 'px';
		                oDrag.style.top = moveY + 'px';
		                if((oDrag.offsetLeft + oDrag.offsetWidth)>outD.offsetWidth){
		                	oDrag.style.left = outD.offsetWidth-oDrag.offsetWidth - 2 +"px";
		                }
		               if(oDrag.offsetLeft<0){
		            	   oDrag.style.left = 0+"px"
		               }
		               if(oDrag.offsetTop<0){
		            	   oDrag.style.top = 0+"px"
		               }
		               if((oDrag.offsetTop + oDrag.offsetHeight) > outD.offsetHeight){
		            	   oDrag.style.top = outD.offsetHeight-oDrag.offsetHeight - 2 +"px";
		               }
		            }
		        };
			}else{
				$("#fortext").remove();
				$("#editFont").hide();
			}
		}
		
		function wantWrite(){
			var myWrite = $("#forWrite").val();
			$("#fortext").text(myWrite)
		}
		
		function setFontWH(){
			var fontw = $("#fontWidth").val() + "px";
			var fonth = $("#fontHeight").val() + "px";
			$("#fortext").css({"width":fontw,"height":fonth})
		}
		
		function changeLogoState(){
			if($("#userLogo").is(':checked')){
				$("#usLogo").show();
				var usLogo = document.getElementById('usLogo');
				var isDraging3 = false;
				usLogo.addEventListener('mousedown', function (e) {//鼠标事件1 - 在标题栏按下（要计算鼠标相对拖拽元素的左上角的坐标 ，并且标记元素为可拖动）
		        	$("#usLogo").css("z-index","10");
		            var e = e || window.event;
		            startX = e.pageX - usLogo.offsetLeft;
		            startY = e.pageY - usLogo.offsetTop;
		            isDraging3 = true;
		        })
		        usLogo.onmouseup = function (e) { //鼠标事件3 - 鼠标松开的时候（标记元素为不可拖动）
		            isDraging3 = false;
		            $("#usLogo").css("z-index","1");
		        }
				usLogo.onmousemove = function (e) {//鼠标事件2 - 鼠标移动时（要检测，元素是否标记为移动）
		            var e = e || window.event;
		            if (isDraging3 === true) {
		                var moveX = e.pageX - startX,
		                    moveY = e.pageY - startY;
		                usLogo.style.left = moveX + 'px';
		                usLogo.style.top = moveY + 'px';
		                if((usLogo.offsetLeft + usLogo.offsetWidth)>outD.offsetWidth){
		                	usLogo.style.left = outD.offsetWidth-usLogo.offsetWidth - 2 +"px";
		                }
		               if(usLogo.offsetLeft<0){
		            	   usLogo.style.left = 0+"px"
		               }
		               if(usLogo.offsetTop<0){
		            	   usLogo.style.top = 0+"px"
		               }
		               if((usLogo.offsetTop + usLogo.offsetHeight) > outD.offsetHeight){
		            	   usLogo.style.top = outD.offsetHeight-usLogo.offsetHeight - 2 +"px";
		               }
		            }
		        };
			}else{
				$("#usLogo").hide();
			}
		}
		
		function changeNicState(){
			if($("#userNic").is(':checked')){
				$("#Nic").show();
				var Nic = document.getElementById('Nic');
				var isDraging4 = false;
				Nic.addEventListener('mousedown', function (e) {//鼠标事件1 - 在标题栏按下（要计算鼠标相对拖拽元素的左上角的坐标 ，并且标记元素为可拖动）
		        	$("#Nic").css("z-index","10");
		            var e = e || window.event;
		            startX = e.pageX - Nic.offsetLeft;
		            startY = e.pageY - Nic.offsetTop;
		            isDraging4 = true;
		        })
		        Nic.onmouseup = function (e) { //鼠标事件3 - 鼠标松开的时候（标记元素为不可拖动）
		            isDraging4 = false;
		            $("#Nic").css("z-index","1");
		        }
				Nic.onmousemove = function (e) {//鼠标事件2 - 鼠标移动时（要检测，元素是否标记为移动）
		            var e = e || window.event;
		            if (isDraging4 === true) {
		                var moveX = e.pageX - startX,
		                    moveY = e.pageY - startY;
		                Nic.style.left = moveX + 'px';
		                Nic.style.top = moveY + 'px';
		                if((Nic.offsetLeft + Nic.offsetWidth)>outD.offsetWidth){
		                	Nic.style.left = outD.offsetWidth-Nic.offsetWidth - 2 +"px";
		                }
		               if(Nic.offsetLeft<0){
		            	   Nic.style.left = 0+"px"
		               }
		               if(Nic.offsetTop<0){
		            	   Nic.style.top = 0+"px"
		               }
		               if((Nic.offsetTop + Nic.offsetHeight) > outD.offsetHeight){
		            	   Nic.style.top = outD.offsetHeight-Nic.offsetHeight - 2 +"px";
		               }
		            }
		        };
			}else{
				$("#Nic").hide();
			}
		}
		
		function getParameter(id){//获取参数
			var obj = document.getElementById(id);
			var objparmas = {};
			objparmas.width = obj.style.width;
			objparmas.height = obj.style.height;
			objparmas.top = obj.style.top;
			objparmas.left = obj.style.left;
			objparmas.fontsize = obj.style.fontSize ? obj.style.fontSize : "";
			objparmas.color = obj.style.color ? obj.style.color : "";
			objparmas.fontfamily = obj.style.fontFamily  ? obj.style.fontFamily : "";
			return objparmas;
		}
		
		function saveToserver(){
			var fontparmas = {};
			var ewmparmas = {};
			var usLogo = {};
			var Nic = {};
			if(document.getElementById("fortext")){
				fontparmas = getParameter("fortext");
			}
			if(document.getElementById("EWMimg")){
				ewmparmas = getParameter("EWMimg");
			}
			if($("#userLogo").is(':checked')){
				usLogo = getParameter("usLogo");
			}
			if($("#userNic").is(':checked')){
				Nic = getParameter("Nic");
			}
			var id = $("#id").val();
			var bgimageUrl = $("#showId").attr("src");
			var data = {};
			if(id!=""&&id!=null){
				data.id=id;
			}
			
			data.bgimageUrl = bgimageUrl ? bgimageUrl : "";
			data.qrcodeX = movePX(ewmparmas.left);
			data.qrcodeY = movePX(ewmparmas.top);
			data.qrcodeWidth = movePX(ewmparmas.width);
			data.qrcodeHeight = movePX(ewmparmas.height);
			data.textX = movePX(fontparmas.left);
			data.textY = movePX(fontparmas.top);
			data.textWidth = movePX(fontparmas.width);
			data.textHeight = movePX(fontparmas.height);
			data.textColor = fontparmas.color ? fontparmas.color : "";
			data.textSize = movePX(fontparmas.fontsize);
			data.textType = movePX(fontparmas.fontfamily);
			data.headWidth = movePX(usLogo.width);
			data.headHeight = movePX(usLogo.height);
			data.headX = movePX(usLogo.left);
			data.headY = movePX(usLogo.top);
			data.nickWidth = movePX(Nic.width);
			data.nickHeight = movePX(Nic.height);
			data.nickX = movePX(Nic.left);
			data.nickY = movePX(Nic.top);
			data.qrimageUrl = "";
			$.ajax({
	            type: "POST",
	            dataType: "json",
	            url: "${ctx}/sys/shopQrcodeTemp/creatQrcodeTemp",
	            data:data,
	            async: false,
	            success: function(data) {
	            	if(data.code == "0"){
	            		alertx("保存成功");
	            		location.href = "${ctx}/sys/shopQrcodeTemp/list";
	            	}
				},
	            error: function(XMLHttpRequest, textStatus, errorThrown) {
	            	alertx(errorThrown);
	            }
	        });  
		}
		
		function movePX(cont){
			if(cont){
				var reCont = cont.replace("px","")
				return reCont;
			}
			return "";
		}
		
		function setImgs(){
			$("#EWMimg").css({"left":"${shopQrcodeTemp.qrcodeX}px","top":"${shopQrcodeTemp.qrcodeY}px"});
			if('${shopQrcodeTemp.textX}' != ""){
				$("#textWidth").attr('checked', true);
				changestate();setFontFamily
				$("#fortext").css({"width":"${shopQrcodeTemp.textWidth}px","height":"${shopQrcodeTemp.textHeight}px","top":"${shopQrcodeTemp.textY}px","left":"${shopQrcodeTemp.textX}px","color":"${shopQrcodeTemp.textColor}","font-size":"${shopQrcodeTemp.textSize}px","line-height":"${shopQrcodeTemp.textSize}px","font-family":"${shopQrcodeTemp.textType}"})
				$("#setFontsize").find("option:selected").text("${shopQrcodeTemp.textSize}px");
				$("#setFontFamily").find("option:selected").text("${shopQrcodeTemp.textType}");
			}
			if('${shopQrcodeTemp.nickWidth}' != 0){
				$("#userNic").attr('checked', true);
				changeNicState();
				$("#Nic").css({"top":"${shopQrcodeTemp.nickY}px","left":"${shopQrcodeTemp.nickX}px"})
			}
			if('${shopQrcodeTemp.headWidth}' != 0){
				$("#userLogo").attr('checked', true);
				changeLogoState();
				$("#usLogo").css({"top":"${shopQrcodeTemp.headY}px","left":"${shopQrcodeTemp.headX}px"})
			}
		}
	</script>
</body>
</html>