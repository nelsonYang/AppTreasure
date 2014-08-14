;
define(function(require, exports, module) {
    var STATIC = require('common/static.js');
    var TEMP = require('common/temp.js');
    var Search = require('utils/url/search.js');
    var extend = require('utils/object/extend.js').extend;
    var isCrossDomain = require('utils/iscrossdomain.js').isCrossDomain;
    var ieVersion = require('utils/ieversion.js').version;
    var Config = require('common/config.js');
    var formToParam = require('utils/elem/form2param.js').to;
    var paramFormat = require('utils/request/paramFormat.js').format;
    var dataFormat = require('utils/request/dataFormat').format;
    var serverPath = Config.SERVER_PATH;
    var actionEntry = Config.ACTION_ENTRY;
    var actionParamName = Config.ACTION_PARAM_NAME;
    var searchSerialize = Search.serialize;

    var jsonpName = STATIC.JSONP_NAME;

    var responseTextFormat = require('utils/response/format.js').format;

    exports.get = get;
    exports.post = post;
    exports.form = form;
    exports.beacon = beacon;
    exports.url = getUrl;

    function get(url, param, callback) {
        var appendToElem = document.getElementsByTagName("head")[0] || document.body;
        var script = document.createElement("script");
        var serialize = paramToSerialize(param);
        url = getAjaxUrl(url, serialize);
        windowOneCallback(jsonpCallbackName(), callback, null, callbackAfter);
        script.setAttribute('src', url);
        appendToElem.appendChild(script);
        function callbackAfter() {
            appendToElem.removeChild(script);
        }
    }
    function post(url, param, callback) {
        if (isCrossDomain && ieVersion && ieVersion < 10) {
            get(url, param, callback);
            return;
        }
        postXMLHttpRequest(url, param, callback);
    }
    function form(form, url, param, callback) {
        var fParam = formToParam(form);
        var data = param.data || {};
        param.data = extend({}, fParam, data);
        post(url, param, callback);
    }
    function beacon(url, param) {
        var serialize = paramToSerialize(param);
        url += (url.match(/\?/) ? "&" : "?") + serialize;
        var beacon = new Image();
        beacon.src = url;
    }
    function getUrl(action) {
        return serverPath + actionEntry + '?' + actionParamName + '=' + action;
    }

    function paramToSerialize(p) {
        var param = paramFormat(p);
        return searchSerialize(param);
    }
    function getAjaxUrl(url, serialize) {
        return url + (url.match(/\?/) ? "&" : "?") + jsonpName + '=' + jsonpCallbackName() + '&' + serialize;
    }
    function jsonpCallbackName() {
        return jsonpName + '_' + getRandom();
    }
    function getRandom() {
        return parseInt(Math.random() * 1000) + new Date().getTime();
    }
    function windowOneCallback(functionName, callback, callbackBefore, callbackAfter) {
        window[functionName] = function(o) {
            window[functionName] = undefined;
            delete window[functionName];

            callbackBefore && callbackBefore(o);
            callback && callback(o);
            callbackAfter && callbackAfter(o);
        }
    }
    function postXMLHttpRequest(url, param, callback) {
        var xmlHttp = createXMLHttpRequest();
        if (xmlHttp != null) {
            xmlHttp.onreadystatechange = requestStateChange;
            xmlHttp.open('POST', url, true);
            xmlHttp.withCredentials = true;
            xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xmlHttp.send(paramToSerialize(param));
        }
        function requestStateChange() {
            if (xmlHttp.readyState == 4) {
                if (xmlHttp.status == 200) {
                    callback && callback(responseTextFormat(xmlHttp.responseText));
                }
            }
        }
    }
    function createXMLHttpRequest() {
        var xmlHttp = null;
        if (window.XMLHttpRequest) {
            xmlHttp = new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        return xmlHttp;
    }
});