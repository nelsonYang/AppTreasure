;
define(function(require, exports, module) {
    var sessionName = require('common/static.js').WXPT_SESSION_NAME;
    var tempGet = require('common/temp.js').get;

    exports.get = getSession;

    function getSession() {
        return tempGet(sessionName);
    }

});