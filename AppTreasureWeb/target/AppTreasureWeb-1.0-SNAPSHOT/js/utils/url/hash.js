;
define(function(require, exports) {
    exports.get = get;
    exports.set = set;

    function get() {
        return location.hash.replace(/^#+/, '');
    }
    function set(hash) {
        location.hash = '#' + hash;
    }
});