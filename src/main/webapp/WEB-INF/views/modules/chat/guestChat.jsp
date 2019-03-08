<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
    <title>与客服对话中...</title>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <%--<link rel="alternate icon" type="image/png" href="${ctxStatic}/chat/more/i/favicon.png">--%>

    <link rel="stylesheet" href="${ctxStatic}/chat/more/css/amazeui.min.css"/>
    <script src="${ctxStatic}/chat/more/js/jquery.min.js"></script>
    <script src="${ctxStatic}/chat/more/js/amazeui.min.js"></script>
    <!-- UM相关资源 -->
    <link href="${ctxStatic}/umeditor/themes/default/css/umeditor.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" charset="utf-8" src="${ctxStatic}/umeditor/umeditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctxStatic}/umeditor/umeditor.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/umeditor/lang/zh-cn/zh-cn.js"></script>
    <style>
        img{
            width:100%;
            height:inherit;
        }
    </style>
</head>
<body>
<div data-role="page" id="main" style="overflow-y:auto;">
    <div data-role="content" class="container" role="main">
        <input type="hidden" id="guestSessionId" value=""/>
        <input type="hidden" id="customerSessionId" value=""/>
        <div id="main">
            <!-- 聊天内容展示区域 -->
            <div id="ChatBox" class="am-g am-g-fixed" >
                <div class="am-u-lg-12" style="height:400px;border:1px solid #999;overflow-y:scroll;">

                    <ul id="chatContent" class="am-comments-list am-comments-list-flip">
                        <li id="msgtmp" class="am-comment" style="display:none;">
                            <a href="">
                                <img id="photo" class="am-comment-avatar" src="${ctxStatic}/chat/more/images/sysPhoto.jpg" alt=""/>
                            </a>
                            <div class="am-comment-main" >
                                <header class="am-comment-hd">
                                    <div class="am-comment-meta">
                                        <a ff="nickname" href="#link-to-user" class="am-comment-author"></a>
                                        <time ff="msgdate" datetime="" title=""></time>
                                    </div>
                                </header>
                                <div ff="content" class="am-comment-bd"></div>
                            </div>
                        </li>
                    </ul>

                </div>
            </div>
            <!-- 聊天内容发送区域 -->
            <div id="EditBox" class="am-g am-g-fixed">
                <!--style给定宽度可以影响编辑器的最终宽度-->
                <script type="text/plain" id="myEditor" style="width:100%;height:140px;"></script>
                <button id="send" type="button" onclick="sendMsg();" class="am-btn am-btn-primary am-btn-block">发送</button>
            </div>

        </div>
    </div>
</div>
<script type="text/javascript">
    //记录IP地址
    var ipAddr='${ipAddr}';
    //实例化编辑器
    var um = UM.getEditor('myEditor',{
        initialContent:"",
        autoHeightEnabled:false,
        toolbar:[
            '',
            '',
            '',
            'emotion image video'
        ]
    });
</script>
<script type="text/javascript" src="${ctxStatic}/chat/js/guest.js?v=0.1"></script>
</body>
</html>