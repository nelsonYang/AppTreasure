;
define(function(require, exports, module) {
    var isInline = 'yes';
     var serverPath = 'http://115.29.137.120/DianZhuan/';
    // var serverPath = 'http://localhost:8080/Sales/';
    var actionEntry = 'ServiceServlet';
    var actionParamName = 'act';
    var virtualPath = 'DianZhuan';
    var combo = false;
    var debug = false;

    /*if (isInline === 'yes') {
     serverPath = '${ServerPath}';
     virtualPath = "${VirtualPath}";
     combo = ("${Combo}" == "true");
     debug = ("${DEBUG}" == "true") ? true : false;
     }*/
    var webRootPath;
    // web 根路径获取
    (function() {
        var postPath = '/';
        var location = window.location;
        var fullHref = location.href;
        var pathName = location.pathname;
        var rootPath = fullHref.substring(0, fullHref.indexOf(pathName)) + postPath;
        webRootPath = rootPath + virtualPath;
    })();

    return {
        SERVER_PATH: serverPath
                , ACTION_ENTRY: actionEntry
                , ACTION_PARAM_NAME: actionParamName
                , COMBO: combo
                , DEBUG: debug
                , WEB_ROOT_PATH: webRootPath
    }
});
