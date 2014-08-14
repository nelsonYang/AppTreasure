;define(function (require, exports, module) {
    require('plugins/uploader/imageUploader.css');
    var $ = require('jquery');
//    var SWFUpload = require('plugins/uploader/swfupload/swfupload.cmd.min.js');
    require('plugins/uploader/swfupload/swfupload.min.js');
    var getSession = require('utils/request/getSession.js').get;
    var Config = require('common/config.js');
    var webRootPath = Config.WEB_ROOT_PATH+"/";
    var swfPath = webRootPath + 'js/plugins/uploader/swfupload/swfupload.swf';
    var serverPath = Config.SERVER_PATH;
    var actionParamName = Config.ACTION_PARAM_NAME;
    var actionEntry = Config.ACTION_ENTRY;
    var imageUploaderAction = 'uploadPhoto';
    var imageUploaderServerPath = serverPath + actionEntry + '?' + actionParamName + '=' + imageUploaderAction;
    var responseTextFormat = require('utils/response/format.js').format;
    var Request = require('request');
    var Post = Request.post;
    var Url = Request.url;

    // flash 的 默认参数
    var flashDefaultParam = {
        upload_url: imageUploaderServerPath,

        button_text: '<span class="upload_button_text">上传</span>',
        button_text_style : ".upload_button_text { color: #ffffff; font-size: 14px; text-align: center;}",
        button_text_top_padding: 8,

        // File Upload Settings
        file_size_limit : "10MB", // 10MB
        file_types : "*.*",
        file_types_description : "All Files",
        file_upload_limit : "10",
        file_queue_limit : "0",

        // Button Settings
        button_image_url : "", // Relative to the SWF file
        button_placeholder_id : "",
        button_width: 61,
        button_height: 22,

        // Flash Settings
        flash_url : swfPath
    };
    // flash image 的 几种类型
    var btn_types = {
        normal: {
            image: webRootPath + 'js/plugins/uploader/swfupload/images/upload-btn.png',
            width: '160',
            height: '36'
        }
    };
    var file_types = {
        image: {
            types: '*.jpg;*.jpeg;*.png;*.gif;',
            description: '图片文件'
        },
        excel: {
            types: '*.xls;*.xlsx',
            description: 'Excel文件'
        },
        all: {
            types: '*.jpg;*.jpeg;*.png;*.gif;*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.rar;*.7z;*.zip;*.txt;*.chm;*.pdf;*.epub;*.image',
            description: '所有类型文件'
        }
    };

    var Instance = (function () {
        function Instance (element, options) {
            var that = this;
            var settings = $.extend({}, $.fn.imageUpload.defaults, options);
            var $element = $(element);
            var CACHE = that.CACHE = {};
            CACHE['$element'] = $element;

            // 上传的数据
            /**
             *
             * @type {Array}
             */
            CACHE['DATA'] = [];

            // 初始化渲染
            fnInitRender($element);
            var $uploadMain = $element.find('.upload-main');
            var $uploadTip = $element.find('.upload-tip');
            CACHE['$uploadMain'] = $uploadMain;
            CACHE['$uploadTip'] = $uploadTip;
            CACHE['maxNum'] = settings['max_num'];
            CACHE['limitSize'] = settings['limit_size'];
            CACHE['imageInfo'] = settings['image_info'];
            var reset = CACHE['reset'] = settings['reset'];
            if (reset) {
                CACHE['changeCallback'] = function (d) {
                    settings['change_callback'](d);
                    fnUploaderReset(CACHE);
                };
            } else {
                CACHE['changeCallback'] = settings['change_callback'];
            }

            // flash 参数初始化
            var swfu_options = fnInitFlashParam(settings, CACHE);
            // 限制文件单选
            swfu_options['button_action'] = SWFUpload.BUTTON_ACTION.SELECT_FILE;
            // flash 渲染
            var swfu = new SWFUpload(swfu_options);
            CACHE['swfu'] = swfu;
            // flash sessionId 兼容
            fnFlashSessionId(swfu);

            // 事件绑定
            fnEventBind($element, CACHE);

            // 初始化数据
            fnInitData(settings['init_data'], CACHE);
        };
        Instance.prototype.main = function () {};
        Instance.prototype.getSuccessData = function () {
            return fnFilterSuccessData(this.CACHE['DATA']);
        }
        Instance.prototype.renderData = function (data) {
            fnInitData(data, this.CACHE);
        }
        return Instance;
    })();
    $.fn.imageUpload = function (options, param) {
        var _data_plugins_name = '_plugins_image_upload';
        var result;
        this.each(function () {
            var $this = $(this);
            var instance = $this.data(_data_plugins_name);
            var instance_flag = true;
            if (!instance) {
                instance_flag = false;
                instance = new Instance(this, options);
                $this.data(_data_plugins_name, instance);
            }
            if (typeof options === 'string' && instance[options]) {
                result =  instance[options](param);
            } else if (typeof options === 'object') {
                instance_flag && $.extend(instance.settings, options);
                instance['main'] && instance['main']();
            }
        });
        if (result === undefined) {
            result = this;
        }
        return result;
    }
    $.fn.imageUpload.defaults = {
        btn_id: '',
        btn_type: 'normal',
        file_type: 'image',
        max_num: 10,
        limit_size: '10MB',
        init_data: [],
        change_callback: function () {}
        ,screen_shot: true
        ,image_info: []
        ,reset: false
    }
    // 私有方法
    // 初始化渲染
    function fnInitRender ($element) {
        var htmlArr = [];
        // 主体
        htmlArr.push('<div class="upload-main"></div>');

        // 提示
        htmlArr.push('<div class="upload-tip"></div>');

        $element.addClass('upload-container').html(htmlArr.join(''));
    }
    // 上传后页面元素显示
    function fnImageBoxHtml (index, filePaths) {
        var image = fnFormatImageUrl(filePaths);
        var htmlArr = [];

        htmlArr.push('<div class="image-box" data-index="' + index + '">');
        htmlArr.push('<i class="item-delete" title="点击删除"></i>');
        htmlArr.push('<i class="item-complete" title="上传完成"></i>');
        htmlArr.push('<img src="' + image['small_image'] + '" />');
        // htmlArr.push('<a class="item-preview dn" href="' + image['big_image'] + '" target="_blank" title="点击预览大图"></a>');
        htmlArr.push('</div>');
        return htmlArr.join('');
    }
    // 图片处理出大小图的url
    function fnFormatImageUrl (filePaths) {
        filePaths = [].concat(filePaths).sort(function (o1, o2) {return o1['type'] - o2['type'];});
        return {small_image: filePaths[0]['filePath'], big_image: filePaths[filePaths.length - 1]['filePath']};
    }
    // 上传中页面元素显示
    function fnImageUploadingHtml (index, file_id) {
        var htmlArr = [];

        htmlArr.push('<div class="image-box m5 fl" data-index="' + index + '" data-file-id="' + file_id + '">');
        htmlArr.push(   '<i class="item-cancel" title="点击取消"></i>');
        htmlArr.push(   '<div class="wait">图片上传中...</div>');
        htmlArr.push(   '<div class="wait_con">');
        htmlArr.push(       '<p class="pershow">上传进度：0%</p>');
        htmlArr.push(       '<div class="wait_loading">');
        htmlArr.push(           '<div class="loading_progress" style="width: 0;"></div>');
        htmlArr.push(       '</div>');
        htmlArr.push(   '</div>');
        htmlArr.push('</div>');

        return htmlArr.join('');
    }
    // flash 参数初始化
    function fnInitFlashParam (settings, CACHE) {
        var flashParam = {};
        // post 请求参数
        flashParam['post_params'] = { request: '{encryptType:"0",session: "' + getSession() + '"}' };
        // flash 按钮ID
        flashParam['button_placeholder_id'] = settings['btn_id'];
        // 按钮格式
        var btn_type = btn_types[settings['btn_type']];
        flashParam['button_image_url'] = btn_type['image'];
        flashParam['button_width'] = btn_type['width'];
        flashParam['button_height'] = btn_type['height'];
        // 文件格式
        var file_type = file_types[settings['file_type']];
        flashParam['file_types'] = file_type['types'];
        flashParam['file_types_description'] = file_type['description'];
        // 最大上传个数
        flashParam['file_upload_limit'] = settings['max_num'];
        // 单文件最大容量
        flashParam['file_size_limit'] = settings['limit_size'];
        // 按钮文字显示
        flashParam['button_text'] = '<span class="upload_button_text">上传图片</span>';

        return $.extend({}, flashDefaultParam, flashParam, fnFlashEventParam(CACHE));
    }
    // flash 事件参数初始化
    function fnFlashEventParam (CACHE) {
        var $uploadMain = CACHE['$uploadMain'];
        var $uploadTip = CACHE['$uploadTip'];
        var maxNum = CACHE['maxNum'];
        var limitSize = CACHE['limitSize'];

        var swfu_options = {};
        // flash 载入前
        swfu_options.swfupload_preload_handler = function() {
            if (!this.support.loading) {
                alert('flash未安装或版本过低，请下载最新版的Adobe Flash player');
                return false
            }
        };
        // flash 成功加载
        swfu_options.swfupload_loaded_handler = function() {
            this.setStats({successful_uploads: 0});
            this.setButtonDisabled(false)
        };
        // flash 加载失败
        swfu_options.swfupload_load_failed_handler = function() {
            alert("上传插件未正确加载,请尝试刷新页面")
        };
        // 选择文件窗口
        swfu_options.file_dialog_start_handler = function() {};
        // 队列上传中
        swfu_options.file_queued_handler = function() {
//            this.setButtonDisabled(true);
        };
        // 队列上传失败
        swfu_options.file_queue_error_handler = function(file, error_code, message) {
            var msg = '';
            switch (error_code) {
                case -100: // 超过最大上传个数
                    msg = '超过最大上传个数(' + maxNum + '个)，请重新上传';
                    break;
                case -110: // 超过文件最大容量
                    msg = '您上传图片容量太大，请重新上传';
                    break;
                case -120: // 空文件
                    msg = '您选择的文件问空文件，请重新选择';
                    break;
                case -130: // 无效文件

                case -140:
                    msg = '您上传的文件为无效文件，请重新选择';
                    break;
            }
            this.cancelUpload(file && file.id, false);
            fnUploadTip($uploadTip, msg)
        };
        // 文件准备就绪
        swfu_options.file_dialog_complete_handler = function(selected_num, queued_num) {
            if (queued_num < 1) {
                return false;
            }
            var success_num = fnFilterSuccessData(CACHE['DATA']).length;
            if (queued_num + success_num >= maxNum) {
                this.setButtonDisabled(true);
            } else {
                this.setButtonDisabled(false);
            }
            if (success_num < maxNum) {
                this.startUpload();
            }
        };
        // 文件开始上传，一个一个来
        swfu_options.upload_start_handler = function(file) {
            var index = CACHE['DATA'].length;
            var file_id = file['id'];
            var $currentUploadElem = $(fnImageUploadingHtml(index, file_id));
            $uploadMain.append($currentUploadElem);
        };
        // 文件上传中...
        swfu_options.upload_progress_handler = function( file, bytes_complete, total_bytes) {
            fnUploadProgress(file['id'], CACHE, (bytes_complete / total_bytes * 100));
        };
        // 文件上传出错
        swfu_options.upload_error_handler = function(file, error, message) {
//            fnUploadTip($uploadTip, '上传图片出错，请重新上传！');
            this.setButtonDisabled(false);
//            fnUploadError();
        };
        // 文件上传成功
        swfu_options.upload_success_handler = function( file, server_data) {
            var responseJson = responseTextFormat(server_data);
            if (responseJson.resultCode == 0) {
                var file_path = responseJson.data.filePath;
                // 成功后截图
                var $img = $('<img>');
                fnImageEventBind($img, CACHE);
                $img.attr('data-file-id', file['id']).attr('src', file_path);
            }
        };
        // 文件上传完成，不管成功与否都会调用
        swfu_options.upload_complete_handler = function() {
            this.startUpload();
        };
        return swfu_options;
    }
    // flashSessionId 兼容
    function fnFlashSessionId (swfu) {
        var actionName = 'inquireHttpSessionId';
        var param = {encryptType: 0};
        function success (o) {
            if (o.resultCode == 0) {
                fnFlashAddParam(swfu, 'flashSessionId', o.data['sessionId']);
            }
        }
        Post(Url(actionName), param, success);
    }
    // 事件绑定 todo
    function fnEventBind ($element, CACHE) {
        var maxNum = CACHE['maxNum'];
        $element
            // 上传中取消
            .on('click', '.item-cancel', function () {
                var $this = $(this);
                var $imageBox = $this.parent();
                var file_id = $imageBox.attr('data-file-id');
                fnFlashCancelUpload(CACHE['swfu'], file_id);
                $imageBox.remove();
            })
            // 触发预览
            .on('mouseenter', '.item_box', function () {
                $(this).next().show();
            })
            // 结束预览
            .on('mouseleave', '.item-preview', function () {
                $(this).hide();
            })
            // 点击预览
            .on('click', '.item-preview', function () {

            })
            // 点击删除
            .on('click', '.item-delete', function () {
                var $this = $(this);
                var $parent = $this.parent();
                var index = $parent.attr('data-index');
                CACHE['DATA'][index] = null;
                $parent.remove();
                var success_data = fnFilterSuccessData(CACHE['DATA']);
                CACHE['changeCallback'](success_data);
                var success_num = success_data.length;
                fnFlashChange(CACHE['swfu'], success_num, success_num >= maxNum);
            })
        ;
    }
    // 初始化数据
    function fnInitData (init_data, CACHE) {
        var index = CACHE['DATA'].length;
        var htmlArr = [];
        // 数据处理
        for (var i = 0, j = init_data.length; i < j; i++) {
            htmlArr[i] = fnImageBoxHtml(index++, init_data[i]);
        }
        CACHE['DATA'] = CACHE['DATA'].concat(init_data);
        // 页面元素渲染
        var $uploadMain = CACHE['$uploadMain'];
        $uploadMain.html(htmlArr.join(''));
        // 上传变化的回调
        CACHE['changeCallback'](fnFilterSuccessData(CACHE['DATA']));
        // 成功个数
        var swfu = CACHE['swfu'];
        var success_num = CACHE['DATA'].length;
        // flash 按钮样式
        var disabledFlag = success_num >= CACHE['maxNum'];
        // flash 参数变化
        fnFlashChange(swfu, success_num, disabledFlag);
    }
    // 上传控件的提示
    function fnUploadTip ($tip, msg) {
        $tip.html(msg).show().stop().delay(2000).hide('fast');
    }
    // 上传的进度条的显示
    function fnUploadProgress (file_id, CACHE, progress) {
        progress = Math.floor(progress);
        var $imageBox = CACHE['$uploadMain'].find('.image-box[data-file-id="' + file_id + '"]');
        $imageBox.find('.wait_con').find('.pershow').html('上传进度：' + progress + '%')
            .end().find('.loading_progress').css('width', progress + '%');
    }
    // 图片的事件绑定处理
    function fnImageEventBind ($img, CACHE) {
        var imageInfo = CACHE['imageInfo'];
        $img.on('load', function () {
            var width = this.width;
            var height = this.height;
            var src = this.src;

            var file_id = $img.attr('data-file-id');
            var filePaths = [{type: 1, filePath: src}];

            if (imageInfo.length) {
                var params = [];

                for (var i = 0, j = imageInfo.length; i < j; i++) {
                    var _imageInfo = imageInfo[i];
                    var _width = _imageInfo['width'];
                    var _height = _imageInfo['height'];
                    var _type = _imageInfo['type'];
                    params.push({
                        filePath: src
                        ,width: _width
                        ,height: _height
                        ,dx1: 0
                        ,dy1: 0
                        ,dx2: width
                        ,dy2: height
                        ,degrees: 0
                        ,rate: 1
                        ,type: _type
                    });
                }

                // 截图开始
                Post(Url('uploadRequiredPhoto'), {encryptType:0, data: params}, function (o) {
                    // 截图成功
                    if (o.resultCode == 0) {
                        filePaths = o['data']['filePaths'];
                        fnImageShow(CACHE, file_id, filePaths);
                    }
                })
            } else {
                fnImageShow(CACHE, file_id, filePaths);
            }
        });
    }
    // 成功上传的数据的过滤
    function fnFilterSuccessData (data) {
        var success = [];
        for (var i = 0, j = data.length; i < j; i++) {
            if (data[i]) {
                // 从小到大排序
                success.push([].concat(data[i]).sort(function (o1, o2) {return o1['type'] - o2['type'];}));
            }
        }
        return success;
    }
    // 设置成功的个数
    function fnSetSuccessNum (swfu, num) {
        var t;
        function _t () {
            try {
                swfu.setStats({successful_uploads: num});
                t && clearTimeout(t);
            } catch (ex) {
                t = setTimeout(_t, 0);
            }
        }
        _t();
    }
    // 设置flash按钮的样式
    function fnSetFlashButtonStyle (swfu, disabledFlag) {
        var t;
        function _t () {
            try {
                swfu.setButtonDisabled(disabledFlag);
                t && clearTimeout(t);
            } catch (ex) {
                t = setTimeout(_t, 0)
            }
        }
        _t();
    }
    // flash 参数变化
    function fnFlashChange (swfu, num, disabledFlag) {
        fnSetSuccessNum(swfu, num);
        fnSetFlashButtonStyle(swfu, disabledFlag);
    }
    // 添加flash上传的参数
    function fnFlashAddParam (swfu, name, value) {
        var t;
        function _t () {
            try{
                swfu.addPostParam(name, value);
                t && clearTimeout(t);
            }catch(ex){
                t = setTimeout(_t, 0);
            };
        }
        _t();
    }
    // 取消上传
    function fnFlashCancelUpload (swfu, file_id) {
        var t;
        function _t () {
            try{
                swfu.cancelUpload(file_id, false);
                t && clearTimeout(t);
            }catch(ex){
                t = setTimeout(_t, 0);
            };
        }
        _t();
    }
    // 图像呈现
    function fnImageShow (CACHE, file_id, filePaths) {
        var $uploadMain = CACHE['$uploadMain'];
        var $imageBox = $uploadMain.find('.image-box[data-file-id="' + file_id + '"]');
        var index = $imageBox.attr('data-index');
        CACHE['DATA'][index] = filePaths;
        $imageBox.replaceWith(fnImageBoxHtml(index, filePaths));
        var success_data = fnFilterSuccessData(CACHE['DATA']);
        CACHE['changeCallback'](success_data);
        var success_num = success_data.length;
        fnFlashChange(CACHE['swfu'], success_num, success_num >= CACHE['maxNum']);
    }
    // 上传控件重置
    function fnUploaderReset (CACHE) {
        CACHE['DATA'] = [];
        var $uploadMain = CACHE['$uploadMain'];
        $uploadMain.empty();
        var $uploadTip = CACHE['$uploadTip'];
        $uploadTip.empty();
        var swfu = CACHE['swfu'];
        fnFlashChange(swfu, 0, false);
    }
    return $;
});
