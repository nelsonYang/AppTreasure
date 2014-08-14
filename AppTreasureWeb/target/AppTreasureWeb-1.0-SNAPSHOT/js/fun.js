;window["YDH"] = window["YDH"] || {};

/**
 * 便捷的匿名闭包
 * 只通过全局的命名空间暴露公共方法
 */
(function () {
    YDH.Fun = {
        fnGetActionUrl: function (sActionName, serverName) {
            return YDH.CONST.ServerPath + (serverName ? serverName : 'WeiLeService') + '?act=' + sActionName;
        },
        fnJsonFillToElem: function (elem, json) {
            var $elem = $(elem);
            for (var name in json) {
                var $input = $elem.find('[name=' + name + ']');
                var value = json[name];
                $input.each(function () {
                    var $this = $(this);
                    if ($this.is(':radio, :checkbox')) {
                        if (this.value == value) {
                            this.checked = true;
                            $this.change();
                        }
                    } else if ($input.is(':input')) {
                        $this.val(value);
                    } else {
                        $this.html(value).attr('title', value);
                    }
                });
            }
            $elem.find('[data-dynamic-name]').each(function () {
                var $this = $(this);
                var dynamic_name = $this.attr('data-dynamic-name');
                var value = json[dynamic_name];
                if ($this.is(':input')) {
                    $this.val(value);
                } else {
                    $this.html(value).attr('title', value);
                }
            });
        },
        fnLoadJump: function (url, oParam, time) {
            var a_elem = document.createElement("a");
            a_elem.href = url;
            url = a_elem.href;
            var main_url = location.protocol + "//" + location.host + location.pathname;
            main_url = main_url.slice(0, main_url.lastIndexOf('/') + 1);
            var _url = url.replace(main_url, '');
            var _url_arr = _url.split('/');
            // nav
            var group;
            var module;
            var item;

            var control_flag = false;

            var controls = [];

            switch (_url_arr.length) {
                case 1:
                    item = _url_arr[0];
                    break;
                case 2:
                    group = _url_arr[0];
                    item = _url_arr[1];
                    break;
                case 3:
                    group = _url_arr[0];
                    module = _url_arr[1];
                    item = _url_arr[2];
                    break;
            }
            var $sub_nav = $('#__ydh_main-sub-nav').trigger('show-sub-nav');

            var current_group = $sub_nav.data('__current_group');
            var current_controls = $sub_nav.data('__current_controls');
            if (typeof oParam !== 'object') {
                time = oParam;
            } else {
                oParam = {};
            }
            this.fnSetLoadParam(url, oParam);
            function _load () {
                $('#__ydh_main_content_wrap').load(url, {_r: (+ new Date())}, function () {
                    // 设置当前模块、页面
                    YDH.Fun.fnFilterPermission('#__ydh_main_content_wrap');
                });
            }
            if (time) {
                setTimeout(_load, time);
            } else {
                _load();
            }
        },
        fnSetLoadParam: function (url, oParam) {
            oParam = oParam || {};

            var split_url = _fnSplitUrl(url);
            var name = split_url.name;
            var search = split_url.search;
            var search_param = _fnUrlSearchToParam(search);
            window['_YDH_LOAD_URL_PARAM'] = window['_YDH_LOAD_URL_PARAM'] || {};
            var param = $.extend({}, search_param, oParam);
            _YDH_LOAD_URL_PARAM[name] = param;
        },
        fnGetLoadParam: function (pageName) {
            window['_YDH_LOAD_URL_PARAM'] = window['_YDH_LOAD_URL_PARAM'] || {};
            var oParam = _YDH_LOAD_URL_PARAM[pageName] || {};
            try {
//                delete _YDH_LOAD_URL_PARAM[pageName];
            } catch (ex) {}
            return oParam;
        },
        fnGetDictionary: function (name, value, flag) {
            var dictionary = YDH.Dictionary;
            var arr = dictionary[name] || [];
            if (typeof value === 'string') {
                var text = '';
                for (var i = 0, j = arr.length; i < j; i++) {
                    if (arr[i].value == value) {
                        text = arr[i].text;
                        return text;
                    }
                }
            } else if (typeof value === 'boolean') {
                flag = value;
            }
            flag && arr.splice(0, 1);
            return arr;
        },
        /* 过滤权限 */
        fnFilterPermission: function (selector) {
            if (!$('#_ydh_permission_style').length){
                return;
            }
            selector = selector || 'body';
            var $selector = $(selector);
            _fnControlPermission($selector);
            $selector.find('.ydh-permission').each(function () {
                _fnControlPermission($(this));
            });
        },
        /*临时全局变量的处理*/
        fnSetTempVariate: function (name, value) {
            YDH._TEMP = YDH._TEMP || {};
            YDH._TEMP[name] = value;
        },
        fnGetTempVariate: function (name) {
            var _temp = YDH._TEMP || {};
            var value = _temp[name];
            try{delete YDH._TEMP[name]}catch(ex){}
            return value;
        }
    };
    function _fnControlPermission ($elem) {
        if ($elem.is('.ydh-permission') && $elem.css('visibility') === 'hidden') {
            $elem.remove();
        }
    }
    function _fnSplitUrl (url) {
        var path_index = url.lastIndexOf('/');
        var point_index = url.indexOf('.', path_index);
        var search_index = url.indexOf('?', point_index);
        (search_index == -1) && (search_index = url.length);
        var anchor_index = url.indexOf('#', point_index);
        (anchor_index == -1) && (anchor_index = url.length);
        var name = url.slice(path_index + 1, point_index);
        var search = url.slice(search_index + 1, anchor_index);
        return {name: name, search: search};
    }

    function _fnUrlSearchToParam  (url_search) {
        var param = {};
        var search_arr = url_search.split(/&/g),
            search_name  = null,
            search_value = null,
            search_name_value = null;

        for (var i=0, len=search_arr.length; i < len; i++){
            search_name_value = search_arr[i].match(/([^=]+)=/i);
            if (search_name_value instanceof Array){
                try {
                    search_name = decodeURIComponent(search_name_value[1]);
                    search_value = decodeURIComponent(search_arr[i].substring(search_name_value[1].length+1));
                } catch (ex){
                }
            } else {
                search_name = decodeURIComponent(search_arr[i]);
                search_value = "";
            }
            if (param[search_name] == undefined) {
                param[search_name] = search_value;
            } else {
                if (param[search_name] instanceof Array) {
                    param[search_name].push(search_value);
                } else {
                    param[search_name] = [param[search_name], search_value];
                }
            }
        }
        return param;
    }
})();