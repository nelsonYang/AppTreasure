;
define(function(require, exports, module) {
    var ObjType = require('utils/object/type.js');
    var isUndefined = ObjType.isUndefined;
    var isString = ObjType.isString;
    var isObject = ObjType.isObject;
    var isArray = ObjType.isArray;
    var isDate = ObjType.isDate;
    var storage = window.sessionStorage;

    function get(name) {
        var result;
        if (storage) {
            result = storage.getItem(name);
        } else {
            var cookies = fnParseCookieString(document.cookie);
            var cookie = cookies[name];
            if (isUndefined(cookie)) {
                cookie = null;
            }
            result = cookie;
        }
        return result;
    }
    function set(name, value) {
        if (isObject(name)) {
            for (var i in name) {
                setItem(i, name[i]);
            }
        } else {
            setItem(name, value);
        }
        if (storage) {
            storage.setItem(name, value);
        } else {
            document.cookie = fnCreateCookieString(name, value);
        }
    }
    function setItem(name, value) {
        if (storage) {
            storage.setItem(name, value);
        } else {
            document.cookie = fnCreateCookieString(name, value);
        }
    }
    function remove(name) {
        if (storage) {
            storage.removeItem(name);
        } else {
            document.cookie = fnCreateCookieString(name, '', {path: "/", expires: new Date(0), secure: "false"}); //过期
        }
    }
    function clear() {
        if (storage) {
            storage.clear();
        } else {
            var cookies = fnParseCookieString(document.cookie);
            for (var cookie_name in cookies) {
                this.set(cookie_name, '', {path: "/", expires: new Date(0), secure: "false"}); //过期
            }
        }
    }

    function fnParseCookieString(text) {

        var cookies = {};

        if (isString(text) && text.length > 0) {

            var cookieParts = text.split(/;\s/g),
                    cookieName = null,
                    cookieValue = null,
                    cookieNameValue = null;

            for (var i = 0, len = cookieParts.length; i < len; i++) {

                cookieNameValue = cookieParts[i].match(/([^=]+)=/i);
                if (isArray(cookieNameValue)) {
                    try {
                        cookieName = decodeURIComponent(cookieNameValue[1]);
                        cookieValue = decodeURIComponent(cookieParts[i].substring(cookieNameValue[1].length + 1));
                    } catch (ex) {
                        //intentionally ignore the cookie - the encoding is wrong
                    }
                } else {
                    //means the cookie does not have an "=", so treat it as a boolean flag
                    cookieName = decodeURIComponent(cookieParts[i]);
                    cookieValue = "";
                }
                cookies[cookieName] = cookieValue;
            }

        }

        return cookies;
    }
    function fnCreateCookieString(name, value, options) {

        options = options || {path: "/", secure: "false"}; //默认浏览器关闭失效

        var text = encodeURIComponent(name) + "=" + encodeURIComponent(value),
                expires = options.expires,
                path = options.path,
                domain = options.domain;


        if (isObject(options)) {
            //expiration date
            if (isDate(expires)) {
                text += "; expires=" + expires.toUTCString();
            }

            //path
            if (isString(path) && path !== "") {
                text += "; path=" + path;
            }

            //domain
            if (isString(domain) && domain !== "") {
                text += "; domain=" + domain;
            }

            //secure
            if (options.secure === true) {
                text += "; secure";
            }
        }

        return text;
    }

    module.exports = {
        get: get
                , set: set
                , remove: remove
                , clear: clear
                , storage: storage
    }
});