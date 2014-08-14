;
define(function(require, exports, module) {
    var dataFormat = require('utils/request/dataFormat').format;
    var getAesKey = require('utils/request/getKey.js').get;
    var MD5 = require('component/md5.cmd.min.js');
    var sessionName = require('common/static.js').WXPT_SESSION_NAME;
    var tempGet = require('common/temp.js').get;
    var aesEncrypt = require('component/aes.cmd.min.js').encrypt;

    exports.format = paramFormat;

    function paramFormat(p) {
        var param = {};
        p = p || {};

        var encryptType = p.encryptType || 0;
        var data = dataFormat(p.data);
        var session = getSession();

        var request = {};
        request.encryptType = encryptType;
        request.data = data;
        if (session) {
            request.session = session;
        }
        var sign;
        switch (encryptType) {
            case 1:
            case "1":
                data = aesEncrypt(JSON.stringify(data), getAesKey());
                request.data = data;
                break;
            case 2:
            case "2":
                sign = MD5(JSON.stringify(request));
                break;
        }
        if (sign) {
            param.sign = sign;
        }
        param.request = JSON.stringify(request);

        if (p.act) {
            param.act = p.act;
        }

        return param;
    }

    function getSession() {
        return tempGet(sessionName);
    }
});