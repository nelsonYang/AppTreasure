/* 
* 微乐后台管理的常用脚本
* filename: wlcommon.js
* author: jierhcou
* date: 2013.8.22
*/
$(function(){ 
	$("body").on("focus","input,textarea",function(){
		$(this).parent(".item-ipt").addClass('item-ipt-focus');
	}).on("blur","input,textarea",function(){
		$(this).parent(".item-ipt").removeClass('item-ipt-focus');
	}).on("mouseenter","tr",function(){
        $(this).addClass('hovertr');
    }).on("mouseleave","tr",function(){
        $(this).removeClass('hovertr');
    });
})