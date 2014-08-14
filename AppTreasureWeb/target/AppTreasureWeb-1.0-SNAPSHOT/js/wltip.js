/* 
* 微乐后台管理提示脚本
* filename: wltip.js
* author: jierhcou
* date: 2013.8.28
*/
$.fn.extend({
	wltip: function(titTxt,tipType){
        var $tipBox=$("#tip-box");
        if($tipBox.length>0){
            $tipBox.addClass(tipType+"-tip").children().text(titTxt);
        }else {
            var tip='<div id="tip-box" class="'+tipType+'-tip"><div class="tip-content">'+titTxt+'</div></div>';
            $("body").append(tip);
            $tipBox=$("#tip-box");
        }
        $tipBox.css("margin-left",-($tipBox.width()/2)).show();
        setTimeout('$("#tip-box").hide()', 1500);
	},
	wltabletip: function(){
        var thisClass,$helpTip;
		$(this).on("mouseenter","button",function(){
            $helpTip=$("#help-tip");
            if($helpTip.length>0){
                $helpTip.children('.help-tip-content').text($(this).attr("data-tip"));
                $helpTip.show();
            }else{
                $("body").append('<div id="help-tip" class="scale-div"><div class="help-tip-content">'+$(this).attr("data-tip")+'</div><div class="help-tip-arrow"></div></div>');
                $helpTip=$("#help-tip");
            }
            thisClass=this.className;
            var blank_last_index = thisClass.lastIndexOf(' ');
            if (blank_last_index < 0) {
                blank_last_index = 0;
            }
            var lastClass = thisClass.slice(blank_last_index);
            var addClass = lastClass + '-hover';
            $(this).css("z-index","1005").addClass(addClass);
            var thisOffset=$(this).offset();
            var tipW=$helpTip.width(),tipH=$helpTip.height();
            $helpTip.css({"top": thisOffset.top-tipH/2-17, "left": thisOffset.left-tipW/2+10});
        }).on("mouseleave","button",function(){
            $(this).css("z-index","9").attr('class', thisClass);
            $helpTip.hide();
        }).on("click","button",function(){
            $helpTip.hide();
        });
	},
    wldialog: function(title,dialogCallback){
        Y.use("$dialog",function(Y){
            $.dialog({
                fixed: true,
                lock: true,
                drag: false,
                icon: "question",
                width: 360,
                title: title,
                content: "你确定 <strong>"+title+"</strong>？",
                ok: function(){
                    dialogCallback();
                },
                cancel: function () {}
            });
        });
    }
}
);