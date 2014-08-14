;
define(function(require, exports, module) {

    exports.isString = isString;
    exports.isUndefined = isUndefined;
    exports.isArray = isArray;
    exports.isObject = isObject;
    exports.isDate = isDate;
    exports.isBoolean = isBoolean;
    exports.isFunction = isFunction;
    exports.isNull = isNull;

    function isString(o) {
        return typeof o === 'string';
    }
    function isUndefined(o) {
        return typeof o === 'undefined';
    }
    function isArray(o) {
        return o instanceof Array;
    }
    function isObject(o) {
        return typeof o === 'object';
    }
    function isDate(o) {
        return o instanceof Date;
    }
    function isBoolean(o) {
        return o.constructor == Boolean
    }
    function isFunction(o) {
        return typeof o === 'function';
    }
    function isNull(o) {
        return o === null;
    }
});