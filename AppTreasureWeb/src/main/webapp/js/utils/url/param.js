;
define(function(require, exports) {
    var TEMP = require('common/temp.js');
    var STATIC = require('static');
    var TEMP_PARAM = STATIC.TEMP_PARAM;
    var temp_get = TEMP.get;
    var temp_set = TEMP.set;
    var temp_remove = TEMP.remove;

    exports.get = get;
    exports.set = set;

    function get() {
        var param = temp_get(TEMP_PARAM) || {};
        temp_remove(TEMP_PARAM);
        return param;
    }
    function set(value) {
        temp_set(TEMP_PARAM, value);
    }
});