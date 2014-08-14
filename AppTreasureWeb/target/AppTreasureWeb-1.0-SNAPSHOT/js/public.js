;window["YDH"] = window["YDH"] || {};

/**
 * 便捷的匿名闭包
 * 只通过全局的命名空间暴露公共方法
 */
;(function () {
    // 载入属于public的组件
    var PUBLIC_COMPONENT = YDH.PUBLIC_COMPONENT || {};

    YDH.PUBLIC = {
        jsonpName: "ydhjsonpcallback",
        // GET 的 异步请求
        fnGetAjax: function (sUrl, oParam, callback) {
            log('请求的URL：', sUrl);
            callback = callback || function () {};
            var appendToElem = document.getElementsByTagName("head")[0] || document.body;
            var script = document.createElement("script");
            var jsonpName = fnGetJsonpName();
            var sParam = fnParamToUrl(oParam);
            sUrl += (sUrl.match(/\?/) ? "&" : "?") + YDH.PUBLIC.jsonpName + "=" + jsonpName + (sParam ? ("&" + sParam) : "");
            script.setAttribute('src', sUrl);

            function callbackAfter () {
                script.remove();
            }

            fnWindowOneCallback(jsonpName, callback, callbackAfter);

            appendToElem.appendChild(script);
        },
        // POST 的 异步请求
        fnPostAjax: function (sUrl, oParam, callback) {
            if (YDH.CONST.IE && YDH.CONST.DevelopMode) {
                YDH.PUBLIC.fnGetAjax(sUrl, oParam, callback);
                return;
            }
            log('请求的URL：', sUrl);
            callback = callback || function () {};
            // 跨域的一个解决方案
            var crossDomainMessenger = PUBLIC_COMPONENT.crossDomainMessenger;
            if (crossDomainMessenger) {
                fnCrossDomainRequest(sUrl, oParam, callback, crossDomainMessenger);
            } else {
                fnXMLHttpRequest(sUrl, oParam, callback, "POST");
            }
        },
        // form 的 post 请求
        fnFormAjax: function (form, sUrl, callback, oParam){
            // 防止同个表单多次提交
            if (!fnFormSubmittingFlag(form)) {
                // 表单提交中标示
                fnFormSubmitStateChange(form, true);
                oParam = fnFormParamToPostParam(form, oParam);
                callback = callback || function () {};
                var _callback = function (oRes) {
                    callback(oRes);
                    fnFormSubmitStateChange(form, false);
                };
                log('表单合成后的参数，oParam：', oParam);
                YDH.PUBLIC.fnPostAjax(sUrl, oParam, _callback);
            }
        },
        // 信标请求
        // 用于客户端不需要响应时使用
        // 如记录用户记录行为
        // 局限：GET请求，有url长度限制，2083
        fnBeaconAjax: function (sUrl, oParam) {
            log('请求的URL：', sUrl);
            sUrl += (sUrl.match(/\?/) ? "&" : "?") + fnParamToUrl(oParam);
            var beacon = new Image();
            beacon.src = sUrl;
        },
        fnResponseDataFormat: fnResponseDataFormat,
        fnFormParamToPostParam: fnFormParamToPostParam
    }

    // 表单提交状态的标志，用于防止表单多次提交
    var _formSubmittingFlagName = "data-form-submitting";

    // 创建XMLHttpRequest
    function createXMLHttpRequest () {
        var xmlHttp = null;
        if (window.XMLHttpRequest) {
            xmlHttp = new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        return xmlHttp;
    }
    // 发起创建XMLHttpRequest请求
    function fnXMLHttpRequest (url, oParam, callback, type) {
        var xmlHttp = createXMLHttpRequest();
        if (xmlHttp != null) {
            xmlHttp.onreadystatechange = requestStateChange;
            xmlHttp.open(type, url, true);
            xmlHttp.withCredentials = true;
            xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
            xmlHttp.send(fnParamToUrl(oParam));
        }
        function requestStateChange () {
            if (xmlHttp.readyState == 4) {
                if (xmlHttp.status == 200) {
                    callback && callback(fnResponseDataFormat(xmlHttp.responseText));
                }
            }
        }
    }
    // 发起跨域请求
    function fnCrossDomainRequest (url, oParam, callback, crossDomainMessenger) {
        log('发起跨域请求');
        var crossDomainParam = {};
        crossDomainParam["url"] = url;
        crossDomainParam["postContent"] = fnParamToUrl(oParam);
        var crossDomainCallbackName = crossDomainParam["crossDomainCallbackName"] = fnGetCrossDomainCallbackName();
        callback = callback || function () {};
        var _callback = function (oRes) {
            oRes = fnResponseDataFormat(oRes);
            callback(oRes);
        }
        fnWindowOneCallback(crossDomainCallbackName, _callback);
        // IE 等不支持 postMessage 的参数传递是字符串，所以要转义一次
        if (YDH.CONST.IE) {
            crossDomainParam = JSON.stringify(crossDomainParam);
        }
        crossDomainMessenger.send(crossDomainParam);
    }
    // 判断提交表单是否重复提交
    function fnFormSubmittingFlag (form) {
        return (form.getAttribute(_formSubmittingFlagName) === "true");
    }
    // 修改提交表单的提交状态
    function fnFormSubmitStateChange (form, flag) {
        form.setAttribute(_formSubmittingFlagName, flag);
        fnSubmitButtonStateChange(form, flag);
    }

    // 目前只支持一个表单一个提交按钮
    function fnSubmitButtonStateChange (form, flag) {
        var eSubmit = fnGetSubmitElem(form);
        fnSubmitDisabledSet (eSubmit, flag);
        return eSubmit;
    }
    function fnGetSubmitElem (form) {
        var elems = form.getElementsByTagName("input");
        var eSubmit = fnSubmitElemSift(elems);

        if (!eSubmit) {
            elems = form.getElementsByTagName("button");
            eSubmit = fnSubmitElemSift(elems);
        }

        return eSubmit;
    }
    function fnSubmitElemSift (elems) {
        var eSubmit = null;
        // 查询提交按钮
        // 倒序的查询更快
        for (var i = elems.length;i--;) {
            if (elems[i].type == "submit" || elems[i].type == "image") {
                eSubmit = elems[i];
                break;
            }
        }
        return eSubmit;
    }
    function fnSubmitDisabledSet (eSubmit, flag) {
        if (eSubmit) {
            var _disabled = "disabled";
            if (flag) {
                eSubmit.setAttribute(_disabled, _disabled);
            } else {
                eSubmit.removeAttribute(_disabled);
            }
        }
    }
    function fnFormToParam (form) {
        var eInput = form.getElementsByTagName("input");
        var eSelect = form.getElementsByTagName("select");
        var eTextarea = form.getElementsByTagName("textarea");
        var oParam = {};
        var elem, name, value;

        for (var i = eInput.length; i--;) {
            elem = eInput[i];
            if (elem.name && fnValidElemFlag(elem)) {
                name = elem.name;
                value = elem.value;
                // 为了解决数字类型非必填项为空值的bug
                if (elem.getAttribute("data-choosable-flag") && !value) {
                    if (elem.getAttribute('data-choosable-default') === null) {
                        continue;
                    }
                    value = elem.getAttribute('data-choosable-default');
                }
                switch (elem.type) {
                    case "text":
                    case "hidden":
                    case "password":
                        if (elem.getAttribute("data-md5-flag")) {
                            value =  hex_md5(value);
                        }
                        fnAddParam(oParam, name, value);
                        break;
                    case "radio":
                    case "checkbox":
                        elem.checked && fnAddParam(oParam, name, value);
                        break;
                }
            }
        }
        for (var i = eSelect.length; i--;) {
            elem = eSelect[i];
            if (elem.name && fnValidElemFlag(elem)) {
                var options = elem.options;
                var name = elem.name;
                for(var j = options.length; i--;){
                    if(options[i].selected){
                        fnAddParam(oParam, name, options[i].value);
                    }
                }
            }
        }
        for (var i = eTextarea.length; i--;) {
            elem = eTextarea[i];
            if (elem.name && fnValidElemFlag(elem)) {
                fnAddParam(oParam, elem.name, elem.value);
            }
        }
        return {"data": oParam};
    }
    function fnFormParamToPostParam (form, oParam) {
        // 表单参数生成
        var formParam = fnFormToParam(form);
        log(form.id + '表单的参数，formParam：', formParam);
        // 其余参数合成
        oParam = fnObjExtend(formParam, oParam);
        return oParam;
    }
    function fnValidElemFlag (elem) {
        return !elem.disabled;
    }
    function fnAddParam (oParam, name, value) {
        oParam = oParam || {};
        if (!oParam[name]) {
            oParam[name] = value;
        } else {
            if (oParam[name] instanceof Array) {
                oParam[name].push(value);
            } else {
                var oldValue = oParam[name];
                oParam[name] = [oldValue, value];
            }
        }
    }
    function fnObjExtend () {
        var target, copy, result;
        for (var i = 0, j = arguments.length; i < j -1; i++) {
            target = result || arguments[i];
            copy = arguments[i + 1];
            for (var n in copy) {
                if (target[n]) {
                    if (target[n] === copy[n]) {
                        continue;
                    } else if (typeof copy[n] !== "object" || copy[n] instanceof Array) {
                        target[n] = copy[n];
                    } else {
                        fnObjExtend(target[n], copy[n]);
                    }
                } else {
                    target[n] = copy[n];
                }
            }
            result = target;
        }
        return result;
    }
    function fnGetRandomPostfix () {
        return "_" + parseInt(Math.random() * 1000) + new Date().getTime();
    }
    function fnGetJsonpName () {
        return YDH.PUBLIC.jsonpName + fnGetRandomPostfix();
    }
    function fnGetCrossDomainCallbackName () {
        return "ydhcrossdomain_" + fnGetRandomPostfix();
    }
    function fnWindowOneCallback (functionName, callback, callbackAfter) {
        window[functionName] = function (oRes) {
            log('jsonp返回参数：', oRes);
            callback(fnResponseDecrypt(oRes));
            fnDisabledParam(window[functionName]);
            callbackAfter && callbackAfter();
        }
    }
    function fnDisabledParam (param) {
        param = undefined;
        delete param;
    }
    function fnParamToUrl (oParam) {
        return fnSerializeParam(fnRequestDataFormat(oParam));
    }

    function fnSerializeParam (oParam) {
        var arr = [];
        if (oParam) {
            for (var name in oParam) {
                var value = oParam[name];
                if (typeof value === "object") {
                    value = JSON.stringify(value);
                }
                arr.push(name + "=" + encodeURIComponent(value));
            }
        }
        return arr.join("&");
    }
    function fnRequestDataFormat (oParam) {
        if (typeof oParam.encryptType === 'undefined') {oParam.encryptType = 1};
        var encryptType = oParam.encryptType;
        var request = fnDataFormat(oParam);
        var result = {request: request};
        switch (encryptType) {
            case 1:
            case "1":
                var data = JSON.stringify(request.data);
                data = AESEncrypt(data, YDH.CONST.AesKey);
                request.data = data;
                break;
            case 2:
            case "2":
                result.sign = hex_md5(JSON.stringify(request));
                break;
        }
        return result;
    }
    function fnParamToData (oParam, type) {
        var maxLength = (type === "list" ? 1 : 0);
        var oData;
        for (var i in oParam) {
            if (oParam[i] instanceof Array) {
                (oParam[i].length > maxLength) && (maxLength = oParam[i].length);
            }
        }
        if (maxLength) {
            oData = [];
            for (var i = 0; i < maxLength; i++) {
                oData[i] = {};
                for (var n in oParam) {
                    if (oParam[n] instanceof Array) {
                        oData[i][n] = oParam[n][i];
                    } else if (typeof oParam[n] === "object") {
                        oData[i][n] = fnParamToData(oParam[n], type);
                    } else {
                        oData[i][n] = oParam[n];
                    }
                }
            }
        } else {
            oData = oParam;
        }
        return oData;
    }
    function fnDataFormat (oParam) {
        var encryptType = oParam.encryptType;
        encryptType = (encryptType === undefined) ? 1 : encryptType;
        var requestType = oParam['_requestType'] || 'json';
        var session = YDH.CONST.Session || undefined;
        var data = fnParamToData(oParam.data, requestType);
        var requestJson = {
            "encryptType": encryptType,
            "session": session,
            "data": data || {}
        };
        // 万恶的数据结构
        var _oParam = {};
        for (var n in oParam) {
            if (n != "_requestType" || n != "data") {
                _oParam[n] = oParam[n];
            }
        }
        fnObjExtend(requestJson, _oParam);
        log('请求的参数，requestJson: ', fnObjExtend({},requestJson));
        return requestJson;
    }
    function fnResponseDataFormat (responseText) {
        var oResponse = {};
        try {
            oResponse = JSON.parse(responseText);
        } catch (ex) {
            log('返回参数JSON解析异常', responseText, ex);
            return;
        }
        if(typeof(oResponse.data) !== "object"){
            oResponse = fnResponseDecrypt(oResponse);
        }
        log('返回JSON：', oResponse);
        oResponse.resultCode && YDH.GLOBAL_COMPONENT['fnMessageTip'](oResponse.msg, oResponse.resultCode);
        return oResponse;
    }
    function fnResponseDecrypt (oJson) {
        var encryptCode = oJson.encryptCode;
        if (encryptCode == 1) {
            var aesDecrypt = '';
            try {
                aesDecrypt = AESDecrypt(oJson.data, YDH.CONST.AesKey);
            } catch (ex) {
                log('返回参数解密异常', oJson.data, YDH.CONST.AesKey, ex);
                return;
            }
            try {
                oJson.data = JSON.parse(aesDecrypt);
            } catch (ex) {
                log('返回参数解密成功，JSON解析异常', oJson.data, ex);
                return;
            }
        }
        log('response，返回参数成功解密后', oJson);
        return oJson;
    }
    function log () {
        if (YDH.CONST.DEBUG && window.console) {
            for (var i = 0, j = arguments.length; i < j; i++) {
                var arg = arguments[i];
                if (YDH.CONST.IE && typeof arg === 'object') {
                    arg = JSON.stringify(arg);
                }
                console.log(arg);
            }
        }
    }
})();
