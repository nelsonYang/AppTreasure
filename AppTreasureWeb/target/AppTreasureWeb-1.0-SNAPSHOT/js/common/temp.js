;
define(function(require, exports, module) {
    var temp = {};

    exports.get = get;
    exports.set = set;
    exports.remove = remove;

    function get(key) {
        return temp[key]
    }
    function set(key, value) {
        temp[key] = value;
    }
    function remove(key) {
        delete temp[key];
    }
});