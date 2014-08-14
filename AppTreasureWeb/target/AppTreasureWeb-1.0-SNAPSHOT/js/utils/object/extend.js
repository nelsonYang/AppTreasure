;
define(function(require, exports, module) {
    var ObjType = require('utils/object/type.js');
    var isBoolean = ObjType.isBoolean;
    var isObject = ObjType.isObject;
    var isFunction = ObjType.isFunction;
    var isUndefined = ObjType.isUndefined;
    var isNull = ObjType.isNull;
    exports.extend = extend;
    function extend() {
        var target = arguments[0] || {}, i = 1, length = arguments.length, deep = false, options;
        /*如果第一个参数为boolean值，则取第二个参数为目标对象*/
        if (isBoolean(target)) {
            deep = target;
            target = arguments[1] || {};
            i = 2;
        }
        /*如果目标参数不是object或者function，那么就有可能是深度copy,*/
        if (!isObject(target) && isFunction(target)) {
            target = {};
        }
        if (length == i) {
            --i;
        }
        for (; i < length; i++)
            // Only deal with non-null/undefined values当参数都为非空时，
            if ((options = arguments[ i ]) != null)
                // Extend the base object
                for (var name in options) {
                    var src = target[ name ], copy = options[ name ];
                    // Prevent never-ending loop /*防止死循环*/
                    if (target === copy)
                        continue;
                    // Recurse if we're merging object values/*深度继承的实现*/
                    if (deep && copy && isObject(copy))
                        target[ name ] = extend(deep, src || (isNull(copy.length) ? [] : {}), copy);
                    // Don't bring in undefined values  /*正常情况下的继承实现*/
                    else if (!isUndefined(copy))
                        target[ name ] = copy;
                }
        // Return the modified object
        return target;
    }
});