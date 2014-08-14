;
define(function(require, exports, module) {
    var $ = require('jquery');
    var REQUEST = require('request');
    var formToParam = require('utils/elem/form2param').to;
    (function() {
        /**
         * 定义一个插件
         */
        var Plugin,
                privateMethod;  //插件的私有方法
        /**
         * 这里是一个自运行的单例模式。
         *
         */
        Plugin = (function() {
            /**
             * 插件实例化部分，初始化时调用的代码可以放这里
             */
            function Plugin(element, options) {
                var that = this;
                //将插件的默认参数及用户定义的参数合并到一个新的obj里
                that.settings = $.extend({}, $.fn.pager.defaults, options);
                //将dom jquery对象赋值给插件，方便后续调用
                var $element = that.$element = $(element);
                $element.on('render', function(e, oParam) {
                    var currentIndex = typeof oParam['currentIndex'] === "undefined" ? that.settings.currentIndex : oParam['currentIndex'];
                    var totalPage = typeof oParam["totalPage"] === "undefined" ? that.settings.totalPage : oParam["totalPage"];
                    that.settings.currentIndex = currentIndex;
                    that.settings.totalPage = totalPage;
                    fnPageRender($element, currentIndex, totalPage);
                }).on('click', '.table-page-prev:not(.table-page-disabled)', function() {
                    that.request({data: {currentIndex: parseInt($element.find('.current-page').text()) - 1}});
                }).on('click', '.table-page-next:not(.table-page-disabled)', function() {
                    that.request({data: {currentIndex: parseInt($element.find('.current-page').text()) + 1}});
                }).on('click', 'a:not(.table-page-prev, .table-page-next)', function() {
                    that.request({data: {currentIndex: parseInt($(this).text())}});
                });
                that.request();
            }
            /**
             * 插件的公共方法，相当于接口函数，用于给外部调用
             */
            Plugin.prototype.request = function(oParam) {
                var that = this;
                var settings = that.settings;
                var param = $.extend(true, {}, that.settings.param, oParam);
                var form = settings.form;
                var url = settings.url;
                var callback = settings.callback;
                var _callback = function(o) {
                    callback(o);
                    if (o.resultCode == 0) {
                        that.$element.trigger('render', {currentIndex: param.data.currentIndex || 1, totalPage: o.data.totalPage});
                    }
                };
                if (form) {
                    REQUEST.form(form, url, param, _callback);
                    var fParam = formToParam(form);
                    param = $.extend(true, {}, param);
                    param.data = $.extend({}, fParam, param.data);
                    try {
                        that.settings.form = undefined;
                        delete that.settings.form;
                    } catch (ex) {
                    }
                } else {
                    REQUEST.post(url, param, _callback);
                }
                that.settings.param = param;
            };
            return Plugin;
        })();
        /**
         * 插件的私有方法
         */
        function fnPageRender($elem, currentIndex, totalPage) {
            if (totalPage <= 1) {
                $elem.html('');
                return;
            }
            var pager_length = 11;
            var offset = (pager_length - 1) / 2;
            var header_length, tailer_length;
            tailer_length = header_length = 2;
            var main_length = pager_length - header_length - tailer_length;
            var htmlArr = [];
            var prevPageClass = '';
            if (currentIndex <= 1) {
                prevPageClass = 'table-page-disabled';
            }
            htmlArr.push('<ul class="table-page">');
            htmlArr.push('<li><a class="table-page-prev ' + prevPageClass + '" href="javascript:;">上一页</a></li>');
            if (totalPage <= pager_length) {
                for (var i = 1, j = totalPage + 1; i < j; i++) {
                    htmlArr.push(fnFillPageATag(i, currentIndex));
                }
            } else {
                if (currentIndex <= offset + 1) {
                    for (var i = 1, j = header_length + main_length + 1; i < j; i++) {
                        htmlArr.push(fnFillPageATag(i, currentIndex));
                    }
                    htmlArr.push(fnFillPageEllipsis());
                    for (var i = totalPage - tailer_length + 2, j = totalPage + 1; i < j; i++) {
                        htmlArr.push(fnFillPageATag(totalPage, currentIndex));
                    }
                } else if (currentIndex >= totalPage - offset - 1) {
                    for (var i = 1; i < header_length; i++) {
                        htmlArr.push(fnFillPageATag(i, currentIndex));
                    }
                    htmlArr.push(fnFillPageEllipsis());
                    for (var i = totalPage - main_length - tailer_length + 1, j = totalPage + 1; i < j; i++) {
                        htmlArr.push(fnFillPageATag(i, currentIndex));
                    }
                } else {
                    for (var i = 1, j = header_length; i < j; i++) {
                        htmlArr.push(fnFillPageATag(i, currentIndex));
                    }
                    htmlArr.push(fnFillPageEllipsis());
                    for (var i = currentIndex - parseInt((main_length - 1) / 2), j = i + main_length; i < j; i++) {
                        htmlArr.push(fnFillPageATag(i, currentIndex));
                    }
                    htmlArr.push(fnFillPageEllipsis());
                    for (var i = totalPage - tailer_length + 2, j = totalPage + 1; i < j; i++) {
                        htmlArr.push(fnFillPageATag(i, currentIndex));
                    }
                }
            }
            var nextPageClass = '';
            if (currentIndex >= totalPage) {
                nextPageClass = 'table-page-disabled';
            }
            htmlArr.push('<li><a class="table-page-next ' + nextPageClass + '" href="javascript:;">下一页</a></li>');
            htmlArr.push('</ul>');
            $elem.html(htmlArr.join(''));
        }
        function fnFillPageTag(liHtml, liClassName) {
            liClassName = liClassName || '';
            return '<li class="' + liClassName + '">' + liHtml + '</li>';
        }
        function fnFillPageATag(index, currentIndex) {
            var aClassName = (index == currentIndex ? 'current-page' : '');
            return fnFillPageTag('<a class="' + aClassName + '" href="javascript:;">' + index + '</a>');
        }
        function fnFillPageEllipsis() {
            return fnFillPageTag('...', 'ellipsis');
        }
        /**
         * 这里是关键
         * 定义一个插件 plugin
         */
        $.fn.pager = function(options) {
            var instance;
            instance = this.data('_page_plugin');
            /**
             *判断插件是否已经实例化过，如果已经实例化了则直接返回该实例化对象
             */
            if (!instance) {
                return this.each(function() {
                    //将实例化后的插件缓存在dom结构里（内存里）
                    return $(this).data('_page_plugin', new Plugin(this, options));
                });
            }
            if (options === true)
                return instance;
            /**
             * 优雅处： 如果插件的参数是一个字符串，则 调用 插件的 字符串方法。
             * 如 $('#id').plugin('doSomething') 则实际调用的是 $('#id).plugin.doSomething();
             * doSomething是刚才定义的接口。
             * 这种方法 在 juqery ui 的插件里 很常见。
             */
            if ($.type(options) === 'string')
                instance[options]();
            if ($.type(options) === 'object') {
                instance.settings = $.extend({}, instance.settings, options);
                instance.request();
            }
            return this;
        };
        /**
         * 插件的默认值
         */
        $.fn.pager.defaults = {
            currentIndex: 1,
            totalPage: 1,
            form: null,
            param: {},
            url: '',
            callback: function() {
            }
        };
        /**
         * 优雅处： 通过data-xxx 的方式 实例化插件。
         * 这样的话 在页面上就不需要显示调用了。
         */
        //    $(function () {
        //        return new Plugin($('[data-plugin]'));
        //    });
    }).call(this);
});