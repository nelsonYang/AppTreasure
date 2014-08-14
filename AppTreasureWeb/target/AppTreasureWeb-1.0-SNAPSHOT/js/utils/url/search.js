;
define(function(require, exports) {
    var Hash = require('utils/url/hash.js');
    var hashGet = Hash.get;
    var Cryption = require('utils/url/cryption.js');
    var encrypt = Cryption.encrypt;
    var decrypt = Cryption.decrypt;
    var ObjType = require('utils/object/type.js');
    var isUndefined = ObjType.isUndefined;
    var isArray = ObjType.isArray;
    var isString = ObjType.isString;
    var search_reg = /(^\?*)([^#]*)((#.*)*$)/;

    exports.get = get;
    exports.getParam = getParam;
    exports.serialize = serialize;

    function get() {
        var search = {};
        var _search = '';
        var hash = hashGet();
        var search_start = hash.indexOf('?');
        var anchor_start = hash.indexOf('#', search_start);
        if (search_start == -1) {
            return search;
        }
        if (anchor_start == -1) {
            _search = hash.slice(++search_start);
        } else {
            _search = hash.slice(++search_start, anchor_start);
        }
        _search = _search.split('&');
        for (var i = _search.length; i--; ) {
            var j = _search[i].split('=');
            var j0 = decrypt(j[0]);
            var j1 = decrypt(j[1]);
            var m = search[j0];
            if (isUndefined(m)) {
                // m = j1;
                search[j0] = j1;
            } else if (isArray(m)) {
                m.push(j1);
            } else {
                m = search[j0] = [search[j0]];
                m.push(j1);
            }
        }
        return search;
    }
    function serialize(obj) {
        var search = [];

        for (var i in obj) {
            var _param = obj[i];
            var _name = encrypt(i);
            if (isArray(_param)) {
                for (var j = _param.length; j--; ) {
                    var _value = encrypt(_param[j]);
                    search.push(_name + '=' + _value);
                }
            } else {
                search.push(_name + '=' + encrypt(_param));
            }
        }

        return search.join('&');
    }
    // 参数解析
    function getParam(s) {
        var p = {};
        if (isUndefined(s)) {
            s = location.search;
        }
        if (search_reg.test(s)) {
            s = RegExp.$2;
        }
        var ss = decrypt(s);
        s = ss.split('&');
        for (var i = s.length; i--; ) {
            var arr = s[i].split('=');
            var s1 = arr[0];
            s1 = decrypt(s1);
            if (s1 == "token") {
                s2 = s[i].substring(s[i].indexOf('=') + 1)
                s2 = decrypt(s2);
            } else {
                var s2 = arr[1] || '';
                s2 = decrypt(s2);
            }
            if (isUndefined(p[s1])) {
                p[s1] = s2;
            } else {
                if (isArray[p[s1]]) {
                    p[s1].push(s2);
                } else {
                    p[s1] = [p[s1], s2];
                }
            }
        }
        return p;
    }
});