;
define(function(require, exports, module) {

    exports.trim = trim;

    function trim(text) {
        (text || "").replace(/^\s+|\s+$/g, "");
    }
});