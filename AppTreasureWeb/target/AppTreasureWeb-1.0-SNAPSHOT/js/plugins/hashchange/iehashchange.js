;
define(function(require, exports, module) {
    var $ = require('jquery');
    var STATIC = require('static');
    var HASH_JUMP_CSS = '.' + STATIC.HASH_JUMP_CSS;

    $(document).on('click', HASH_JUMP_CSS, function() {
        try {
            setTimeout(function() {
                window.onhashchange();
            }, 0);
        } catch (ex) {
        }
        ;
    });
});