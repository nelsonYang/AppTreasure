;
define(function(require, exports, module) {
    var ObjType = require('utils/object/type.js');
    var isUndefined = ObjType.isUndefined;
    var isArray = ObjType.isArray;

    exports.format = dataFormat;

    function dataFormat(d) {
        var data = {};
        if (isUndefined(d)) {
            return data;
        }
        var _d;
        var listFlag = false;
        var listLength = 0;
        for (var i in d) {
            _d = d[i];
            if (isArray(_d)) {
                listFlag = true;
                listLength = Math.max(listLength, _d.length);
            }
        }
        if (!listFlag) {
            data = d;
            return data;
        }
        var _dArrType;
        for (var i in d) {
            _d = d[i];
            isArray(_d) ? _dArrType = true : _dArrType = false;
            var _data = data[i] = [];
            for (var j = listLength; j--; ) {
                if (_dArrType) {
                    _data[j] = isUndefined(_d[j]) ? isUndefined(_data[j - 1]) ? '' : _data[j - 1] : _d[j];
                } else {
                    _data[j] = _d;
                }
            }
        }
        return data;
    }
});