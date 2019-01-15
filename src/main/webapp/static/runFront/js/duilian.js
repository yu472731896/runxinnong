$(document).ready(function(){
	var duilian = $("div.duilian");
	
	var window_w = $(window).width();
	if(window_w>1000){duilian.show();}
	$(window).scroll(function(){
		var scrollTop = $(window).scrollTop();
		duilian.stop().animate({top:scrollTop+300});
	});
});