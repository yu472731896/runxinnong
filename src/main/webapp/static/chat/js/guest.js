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
        document.getElementById('chat').onkeydown = function (event) {
            if (event.keyCode == 13) {
                Chat.sendMessage();
            }
        };
    };

    Chat.socket.onclose = function () {
        document.getElementById('chat').onkeydown = null;
        //Console.log('-----系统已断开连接-----');
    };

    Chat.socket.onmessage = function (message) {
        var message = JSON.parse(message.data);
        if (message.type == "no_customer") {
            Console.log(message.nickname, message.picture, message.msg, "", "odd");
            $(".form-control").attr("disabled", "true");
            $(".send-button").attr("disabled", "disabled");
            return;
        } else {
            $(".form-control").removeAttr("disabled");
            $(".send-button").removeAttr("disabled");
        }
        if (message.type == "guest_join") {
            $("#mynickname").val(message.nickname);
            $("#mypicture").val(message.picture);
            $("#cnickname").val(message.cnickname);
            $("#cpicture").val(message.cpicture);
        }
        var cnickname = $("#cnickname").val();
        var cpicture = $("#cpicture").val();
        var createtime = new Date().pattern("yyyy-MM-dd HH:mm:ss");
        var msg = message.msg;
        Console.log(cnickname, cpicture, msg, createtime, "odd");
    };
});

Chat.initialize = function () {
    if (window.location.protocol == 'http:') {
        // Chat.connect('ws://' + window.location.host + '/runxinnong/websocket' + window.location.search);
        Chat.connect('ws://' + window.location.host + '/runxinnong/websocket/0');
    } else {
        Chat.connect('wss://' + window.location.host + '/runxinnong/websocket/0');
    }
};

Chat.sendMessage = (function () {
    var msg = document.getElementById('chat').value;
    console.log(msg);
    if (msg != '') {
        var message = {};
        message.type = "guest_send";
        message.msg = msg;
        Chat.socket.send(JSON.stringify(message));
        document.getElementById('chat').value = '';
        var mynickname = $("#mynickname").val();
        var mypicture = $("#mypicture").val();
        var time = new Date().pattern("yyyy-MM-dd HH:mm:ss");
        Console.log(mynickname, mypicture, msg, time, "even");
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

Chat.initialize();

function sendMsg() {
    Chat.sendMessage();
}

//定时任务断去请求服务器
setInterval("heart_connect()", 10000);

//检测是否在线
function heart_connect() {
    var message = {};
    message.type = "heart_connect";
    console.log(message);
    Chat.socket.send(JSON.stringify(message));
}

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