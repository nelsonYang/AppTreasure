;
define(function(require, exports, module) {
    var $ = require('jquery');
    exports.show = show;
    function show(type, txt) {
        var className = type == 1 ? "success" : type == 2 ? "fail" : "warning",
                tipContent = txt,
                $queryTipBox = $("#query-tip-box");
        if ($queryTipBox.length > 0) {
            document.getElementById("query-icon").className = className + "-icon";
            var queryTipContent = document.getElementById("query-tip-content");
            queryTipContent.className = className;
            queryTipContent.innerHTML = tipContent;
            $queryTipBox.css("margin-left", -($queryTipBox.width() / 2)).show();
        } else {
            $("body").append('<div id="query-tip-box" class="clearfix"><i id="query-icon" class="' + className + '-icon"></i><div id="query-tip-content" class="' + className + '">' + tipContent + '</div></div>');
            $queryTipBox = $("#query-tip-box");
            $queryTipBox.css("margin-left", -($queryTipBox.width() / 2)).show();
        }
        setTimeout(function() {
            $queryTipBox.hide();
        }, 1500);
    }
    function hide() {

    }
});