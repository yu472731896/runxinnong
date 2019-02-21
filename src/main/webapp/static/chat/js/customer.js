var Chat = {};

Chat.socket = null;

Chat.connect = (function (host) {
    if ('WebSocket' in window) {
        Chat.socket = new WebSocket(host);
    } else if ('MozWebSocket' in window) {
        Chat.socket = new MozWebSocket(host);
    } else {
        //Console.log('-----该浏览器不支持此聊天-----');
        return;
    }

    Chat.socket.onopen = function () {
        //Console.log('-----系统已连接-----');
        console.log("-----系统已连接-----");
        document.getElementById('EditBox').onkeydown = function (event) {
            if (event.keyCode == 13) {
                Chat.sendMessage();
            }
        };
    };

    Chat.socket.onclose = function () {
        /*  document.getElementById('EditBox').onkeydown = null;*/
        Console.log('-----系统已断开连接-----');
        console.log("-----系统已断开连接-----");
    };

    Chat.socket.onmessage = function (message) {
        var message = JSON.parse(message.data);
        if(message.code == 0){//通讯成功
            if(message.type == "customer_init") { //系统消息（初始化用户信息）
                $("#customerSessionId").val(message.customerSessionId);
                $("#guestSessionId").val("");
                //添加已在线游客
                var onlineGuestList = message.data;
                for(var i = 0;i<onlineGuestList.length;i++){
                    var onlineGuset = onlineGuestList[i];
                    var guestList = '<li class="right_guest"><label class="online"></label><a href="javascript:;"><img src="' + message.picture + '" id="img' + message.inboundId + '"  height="30" width="30"></a><a class="chat03_name">' + onlineGuset.userName + '</a><input type="hidden" class="guestSessionId" value=' + onlineGuset.sessionId + '></li>';
                    $(".chat03_content ul").prepend(guestList);
                    var chat_div = '<div class="chat_guest_content cell_' + onlineGuset.sessionId + '" style="display:none;"></div>';
                    $("#chatContent").append(chat_div);
                    $(".chat03_content").scrollTop(0);
                }
                var customerSessionId = $("#customerSessionId").val();
                //显示系统消息
                // Console.log(customerName, customerPicture, msg, createtime, "odd");
            }else if(message.type == "guest_join"){
                //ToDo 添加新入游客
                //游客列表中添加客户信息
                var onlineGuset = message.data;
                var guest = '<li class="right_guest"><label class="online"></label><a href="javascript:;"><img src="' + message.picture + '" id="img' + message.inboundId + '"  height="30" width="30"></a><a class="chat03_name">' + onlineGuset.userName + '</a><input type="hidden" class="guestSessionId" value=' + onlineGuset.sessionId + '></li>';
                $(".chat03_content ul").prepend(guest);
                var chat_div = '<div class="chat_guest_content cell_' + onlineGuset.sessionId + '" style="display:none;"></div>';
                $("#chatContent").append(chat_div);

                //更新在线游客数量

            }else if(message.type == "guest_send" || message.type == "by_self"){
                addMessage(message);
            }
        }
    };
});

Chat.initialize = function () {
    if (window.location.protocol == 'http:') {
        // Chat.connect('ws://' + window.location.host + '/runxinnong/websocket' + window.location.search);
        Chat.connect('ws://' + window.location.host + '/runxinnong/websocket/1/'+ipAddr);
    } else {
        Chat.connect('wss://' + window.location.host + '/runxinnong/websocket/1/'+ipAddr);
    }
};

Chat.sendMessage = (function () {
    var guestSessionId = $("#guestSessionId").val();
    if(guestSessionId == ""){
        alert("请选择用户");
        return;
    }
    if (!um.hasContents()) {  // 判断消息输入框是否为空
        // 消息输入框获取焦点
        um.focus();
        // 添加抖动效果
        $('.edui-container').addClass('am-animation-shake');
        setTimeout("$('.edui-container').removeClass('am-animation-shake')", 1000);
    } else {
        //获取输入框的内容
        var txt = um.getContent();
        //获取发往的sessionId 和 发送方sessionId
        var guestSessionId = $("#guestSessionId").val();
        var customerSessionId = $("#customerSessionId").val();
        //构建一个标准格式的JSON对象
        var obj = {
            type:"customer_send",
            fromSessionId:customerSessionId,
            toSessionId:guestSessionId,
            msg:txt
        };
        // 发送消息
        // socket.send(obj);
        Chat.socket.send(JSON.stringify(obj));

        // 清空消息输入框
        um.setContent('');
        // 消息输入框获取焦点
        um.focus();
    }
});

var Console = {};

Console.log = (function (nickname, picture, msg, createtime, classtype) {
    var msghtml =
        '<li class="' + classtype + '">' +
        '<a class="user ui-link" href="#"><img class="img-responsive avatar_" alt="" src="' + picture + '"><span class="user-name">' + nickname + '</span></a>' +
        '<div class="reply-content-box">' +
        '	<span class="reply-time">' + createtime + '</span>' +
        '    <div class="reply-content pr">' +
        '    	<span class="arrow">&nbsp;</span>' + msg
    '    </div>' +
    '</div> </li>';

    $("#console ul").append(msghtml);
    window.scrollBy($("#console ul").last().height(), $("#console ul").last().height());
});

function sendMsg() {
    Chat.sendMessage();
}

//定时任务断去请求服务器
// setInterval("heart_connect()", 30000);

//检测是否在线
function heart_connect() {
    var message = {};
    message.type = "heart_connect";
    console.log(message);
    Chat.socket.send(JSON.stringify(message));
}

////监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
// window.onbeforeunload = function(){ websocket.close(); };

function addMessage(data){
    var sessionId = "";
    if(data.type == "guest_send"){
        sessionId = data.fromSessionId;
    }else if(data.type == "by_self"){
        sessionId = data.toSessionId;
    }
    //获取游客的sessionId拼接到对应的div中
    var box = $("#msgtmp").clone(); 	//复制一份模板，取名为box
    box.show();							//设置box状态为显示
    box.appendTo("#chatContent .cell_"+sessionId);		//把box追加到有游客对应的聊天面板中
    box.find('[ff="nickname"]').html(data.name); //在box中设置昵称
    if(!data.photo == null || !data.photo == ""){ //在box中设置头像
        box.find('[ff="photo"]').html(data.photo);
    }
    box.find('[ff="msgdate"]').html(data.date); 		//在box中设置时间
    box.find('[ff="content"]').html(data.msg); 	//在box中设置内容
    box.addClass(data.isSelf? 'am-comment-flip':'');	//右侧显示
    box.addClass(data.isSelf? 'am-comment-warning':'am-comment-success');//颜色
    box.css((data.isSelf? 'margin-left':'margin-right'),"20%");//外边距
    $("#ChatBox div:eq(0)").scrollTop(999999); 	//滚动条移动至最底部
}

//切换用户
$("body").delegate(".right_guest","click", function(){
    var thisGuestSessionId = $(this).find('.guestSessionId').val();
    $(".chat_guest_content").hide();
    $(".cell_"+thisGuestSessionId).show();
    $(this).addClass("choosed").siblings().removeClass("choosed");
    $("#guestSessionId").val(thisGuestSessionId);
});

Date.prototype.pattern = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    var week = {
        "0": "/u65e5",
        "1": "/u4e00",
        "2": "/u4e8c",
        "3": "/u4e09",
        "4": "/u56db",
        "5": "/u4e94",
        "6": "/u516d"
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    if (/(E+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "/u661f/u671f" : "/u5468") : "") + week[this.getDay() + ""]);
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
};

Chat.initialize();

