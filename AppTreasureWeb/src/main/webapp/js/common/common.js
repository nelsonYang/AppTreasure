;
define(function(require, exports, module) {
    // 添加锚点变化监听
    require('plugins/hashchange/hashchange.js');
    // 跨页面的参数传递
    var TEMP = require('common/temp.js');
    var tempSet = TEMP.set;
    var tempGet = TEMP.get;
    var STORAGE = require('common/storage.js');
    var storageGet = STORAGE.get;
    var storageSet = STORAGE.set;
    var storageRemove = STORAGE.remove;
    var STATIC = require('common/static.js');
    var sessionName = STATIC.WXPT_SESSION_NAME;
    var aesKeyName = STATIC.WXPT_AES_KEY_NAME;
    var saves = [sessionName, aesKeyName];
    var $ = require('jquery');

    for (var i = saves.length; i--; ) {
        var name = saves[i];
        var value = storageGet(name);
        storageRemove(name);
        if (value) {
            tempSet(name, value);
        }
    }

    $(window).on('unload', function() {
        for (var i = saves.length; i--; ) {
            var name = saves[i];
            var value = tempGet(name);
            if (value) {
                storageSet(name, value);
            }
        }
    })
});