;
define(function(require, exports, module) {
    var IEVersion;
    // IE 版本判断
    var notIe = false;
    var ieVersion = 4;
    var ieStart = ieVersion + 1;
    var elem = document.createElement("div");
    var childElems = elem.getElementsByTagName("i");
    while (elem.innerHTML = '<!--[if gt IE ' + (++ieVersion) + ']><i></i><![endif]-->',
            childElems[0])
        ;
    IEVersion = (ieVersion > ieStart) ? ieVersion : notIe;

    exports.version = IEVersion;
});