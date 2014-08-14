/* 页面的跳转 */
;
define(function(require, exports) {
    var hashSet = require('utils/url/hash.js').set;
    var IEVersion = require('utils/ieversion.js');

    exports.to = to;

    function to(url) {
        hashSet(url);
        if (IEVersion && IEVersion < 8) {
            try {
                window.onhashchange();
            } catch (ex) {
            }
            ;
        }
    }
});