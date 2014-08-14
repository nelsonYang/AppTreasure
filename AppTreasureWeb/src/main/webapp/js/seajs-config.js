/**
 * sea.config.js
 * 依赖关系配置
 * 版本控制
 **/
;
(function() {
    var version = "${WXPTVersion}";

    seajs.config({
        //模块系统的基础路径
        base: 'DianZhuan/js'

                //别名
                , alias: {
            jquery: 'jquery-1.8.3.cmd.min.js'
                    , static: 'common/static.js'
                    , request: 'common/request.js'
                    , validation: 'plugins/jquery-validation/jquery.validate.cmd.min.js'
                    , template: 'plugins/arttemplate/template.min.js'
                    , pager: 'plugins/pager.js'
                    , tip: 'plugins/tip.js'
                    , jump: 'utils/url/jump.js'
                    , search: 'utils/url/search.js'
                    , formFill: 'utils/elem/jsonFillElem.js'
                    , datePicker: 'plugins/datePicker/WdatePicker.js'
                    , upload: 'plugins/uploader/imageUploader.js'
                    , temp: 'common/temp.js'
                    , storage:'common/storage.js'
                    ,
        }
        // 模块的路径别名配置
        , paths: {
        }

        //预加载
        , preload: [
            'jquery'
                    , typeof JSON === 'undefined' ? 'component/json2.min.js' : ''
                    , 'common/common.js'
        ]

                //错误信息查看
                , debug: 1

                //文件映射
                , map: [
            //可配置版本号
            [/\.css$/, '.css?v=' + version]
                    , [/\.js$/, '.js?v=' + version]
        ]

                //编码
                , charset: 'utf-8'
    });
})();
