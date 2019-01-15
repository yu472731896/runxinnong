$(function(){
	var navdt = $('#nav dt');
	var navdd = navdt.next();
	var time = '';
	navdt.hover(function(){
		clearTimeout(time);
		navdd.slideUp(150);
		navdt.removeClass('hove');
		$(this).addClass('hove');
		$(this).next('dd').slideDown(500);
	},function(){
		time = setTimeout(function(){
			navdt.removeClass('hove');
			navdd.slideUp(150);
		},500);
	});
	$('#nav ul li').hover(function(){
		clearTimeout(time);
	},function(){
		time = setTimeout(function(){
			navdt.removeClass('hove');
			navdd.slideUp(150);
		},500);
	});
});