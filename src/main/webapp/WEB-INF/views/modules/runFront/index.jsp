<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>润新农</title>
    <meta name="decorator" content="default"/>
    <script type="application/javascript">
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


        var ws = null;
        function startWebSocket() {
            if ('WebSocket' in window)
                ws = new WebSocket("ws://${ctx}/websocket");
            else if ('MozWebSocket' in window)
                ws = new MozWebSocket("ws://${ctx}/websocket");
            else
                alert("not support");

            ws.onmessage = function(evt) {
                alert(evt.data);
            };

            ws.onclose = function(evt) {
                alert("close");
            };

            ws.onopen = function(evt) {
                alert("open");
            };
        }

        function sendMsg() {
            ws.send(document.getElementById('writeMsg').value);
        }

    </script>
</head>
<body onload="startWebSocket();">
<div>
    <%--标题--%>
    <%--菜单--%>
    <%--轮播图--%>
    <h3>轮播图</h3>
    <table>
    <c:forEach items="${indexDate.slideShowList}" var="runSlideShow">
        <tr>
            <td> 标题 ：
                    ${runSlideShow.title}
            </td>
            <td> 最后修改时间 ：
                <fmt:formatDate value="${runSlideShow.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            <td> 图片路径 ：
                    ${runSlideShow.path}
            </td>

        </tr>
    </c:forEach>
    </table>

    <%--新闻--%>

        <input type="text" id="writeMsg"></input>
        <input type="button" value="send" onclick="sendMsg()"></input>
</div>
</body>
</html>
