;
define(function(require, exports, module) {
    var CONFIG = require('common/config.js');
    var serverPath = CONFIG.SERVER_PATH;
    var webRootPath = CONFIG.WEB_ROOT_PATH;

    return {isCrossDomain: serverPath === webRootPath};
});