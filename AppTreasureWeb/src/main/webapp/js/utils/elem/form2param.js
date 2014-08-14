;
define(function(require, exports, module) {
    var MD5 = require('component/md5.cmd.min.js');
    var ObjType = require('utils/object/type.js');
    var isUndefined = ObjType.isUndefined;
    var isArray = ObjType.isArray;

    exports.to = formToParam;

    function formToParam(form) {
        var param = {};
        var eInput = form.getElementsByTagName("input");
        var eSelect = form.getElementsByTagName("select");
        var eTextarea = form.getElementsByTagName("textarea");
        var elem, name, value;

        for (var i = eInput.length; i--; ) {
            elem = eInput[i];
            if (elem.name && validElemFlag(elem)) {
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
                            value = MD5(value);
                        }
                        param[name] = addParam(param[name], value);
                        break;
                    case "radio":
                    case "checkbox":
                        elem.checked && (param[name] = addParam(param[name], value));
                        break;
                }
            }
        }
        for (var i = eSelect.length; i--; ) {
            elem = eSelect[i];
            name = elem.name;
            if (name && validElemFlag(elem)) {
                var options = elem.options;
                var option;
                for (var j = options.length; j--; ) {
                    option = options[j];
                    if (option.selected) {
                        param[name] = addParam(param[name], option.value);
                    }
                }
            }
        }
        for (var i = eTextarea.length; i--; ) {
            elem = eTextarea[i];
            name = elem.name;
            if (name && validElemFlag(elem)) {
                param[name] = addParam(param[name], elem.value);
            }
        }
        return param;
    }
    function validElemFlag(elem) {
        return !(elem.disabled || elem.getAttribute('data-disabled'));
    }
    function addParam(data, value) {
        var result;
        if (isUndefined(data)) {
            result = value;
        } else {
            if (isArray(data)) {
                result = [].concat(data);
            } else {
                result = [data];
            }
            result.push(value);
        }
        return result;
    }
});