;
define(function(require, exports, module) {
    var aesKeyName = require('common/static.js').WXPT_AES_KEY_NAME;
    var tempGet = require('common/temp.js').get;

    exports.get = getAesKey;

    function getAesKey() {
        return tempGet(aesKeyName);
    }

});