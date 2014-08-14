;
define(function(require, exports, module) {
    var AES = require('component/aes.cmd.min.js');
    var aesDecrypt = AES.decrypt;
    var getAesKey = require('utils/request/getKey.js').get;

    exports.format = responseTextFormat;

    function responseTextFormat(text) {
        var o;
        try {
            o = JSON.parse(text);
        } catch (ex) {
            o = {};
            o.errorMsg = 'responseText JSON 解析异常！';
            return o;
        }
        switch (o.encryptCode) {
            case 1:
            case "1":
                var data = o.data;
                try {
                    data = aesDecrypt(data, getAesKey());
                } catch (ex) {
                    o.errorMsg = '解密异常！';
                    return o;
                }
                try {
                    o.data = JSON.parse(data);
                } catch (ex) {
                    o.errorMsg = 'JSON 解析异常！';
                    return o;
                }
                break;
        }
        return o;
    }
});
