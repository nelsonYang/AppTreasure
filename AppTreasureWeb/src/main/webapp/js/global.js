/**
 * 添加命名空间
 */
;window['YDH'] = {};

(function () {
    /**
     * 全局常量
     * YDH.CONST.Session 用于 session 值的存储，对应的cookie名为 ydhSession
     * YDH.CONST.AesKey  用于 aes 加密的 key 的存储，对应的cookie名为 ydhAesKey
     * YDH.CONST.UserId  用于 用户ID 的存储，对应的cookie名为 ydhUserId
     * 以上常量不可用于其它
     */
    YDH.CONST = {
        //是否部署版本
        Isinline: "${Isinline}"
        //服务器地址
        ,ServerPath: "http://115.29.137.120/DianZhuan/ServiceServlet"
        ,PayPath: "http://192.168.1.3:8080/"
        //是否启用服务器端脚本合并压缩
        ,Combo: false
        ,DEBUG: true
        ,DevelopMode: true
        ,VirtualPath: ''
    }

    /**
     * 打包时参数自动替换
     */
    if (YDH.CONST.Isinline == "yes") {
        YDH.CONST.ServerPath = "${ServerPath}";
        YDH.CONST.PayPath = "${PayPath}";
        YDH.CONST.Combo = ("${Combo}" == "true");
        YDH.CONST.DEBUG = ("${DEBUG}" == "true") ? true : false;
        YDH.CONST.DevelopMode = ("${DevelopMode}" == "true") ? true : false;
        YDH.CONST.VirtualPath = "${VirtualPath}";
    }

    /**
     * IE 版本判断
     * IE 版本不同，YUI 配置不同
     */
    YDH.CONST.IE = undefined;
    (function () {
        var notIe = undefined;
        var ieVersion = 4;
        var ieStart = ieVersion + 1;
        var elem = document.createElement("div");
        var childElems = elem.getElementsByTagName("i");
        while (elem.innerHTML = '<!--[if gt IE ' + (++ieVersion) + ']><i></i><![endif]-->',
            childElems[0]);
        YDH.CONST.IE = (ieVersion > ieStart) ? ieVersion : notIe;
    })();

    /**
     * WEB 根路径
     */
    var virtualPath = YDH.CONST.VirtualPath;
    YDH.CONST.WebRootPath = "/";
    (function () {
        var postPath = '/';
        var location = window.location;
        var fullHref = location.href;
        var pathName = location.pathname;
        var rootPath = fullHref.substring(0, fullHref.indexOf(pathName)) + postPath;
        YDH.CONST.WebRootPath = rootPath + virtualPath;
    })();

    var webRootPath = YDH.CONST.WebRootPath;

    /**
     * 是否跨域判断
     */
    YDH.CONST.CrossDomain = false;
    (function () {
        var postPath = '/';
        var serverPath = YDH.CONST.ServerPath;
        if (postPath === serverPath) {
            return;
        }
        YDH.CONST.CrossDomain = !(serverPath === webRootPath);
    })();

    /**
     * 全局配置信息
     */
    var combine = YDH.CONST.Combo;
    var ie = YDH.CONST.IE;
    var crossDomain = YDH.CONST.CrossDomain;
    // public 的依赖
    var public_requires = ["AES", "MD5", "messageTip"];
    // IE 的跨域依赖
    if (crossDomain && ie && ie < 10) {
        public_requires.push("public_messenger");
    }
    var public_messenger_requires= ["_Messenger"];
    // IE JSON依赖
    if (ie && ie < 8) {
        public_requires.push("JSON");
        public_messenger_requires.push('JSON');
    }

    YDH.CONFIG = {
        //全局cookie配置
        Cookie: [
            { path: "/", secure: "false" }, //浏览器关闭失效
            { path: "/", expires: new Date(Date.parse(new Date()) + 86400000), secure: "false" }, //一天
            { path: "/", expires: new Date(Date.parse(new Date()) + (86400000 * 7)), secure: "false" }, //一周
            { path: "/", expires: new Date(new Date().setMonth(new Date().getMonth() + 1)), secure: "false" }, //一月
            { path: "/", expires: new Date(new Date().setMonth(new Date().getMonth() + 3)), secure: "false" }, //一季
            { path: "/", expires: new Date(new Date().setFullYear(new Date().getFullYear() + 1)), secure: "false" } //一年
        ],
        YUI3: {
            base: webRootPath + 'js/vendor/yui/build/',
//            base: webRootPath + 'js/vendor/yui/build/',
            combine: combine,
//            comboBase: NE.CONST.ServerPath + 'Service.nd?act=compress&v=' + NE.CONST.Version + '&b=js&f=',
            comboSep: ',',
//            comboVer: NE.CONST.Version,
            root: 'vendor/yui/build/',
            classNamePrefix: 'ydh',
            groups: {
                plugins: {
                    base: webRootPath + 'js/plugins/',
                    combine: combine,
                    root: 'plugins/',
                    modules: {
                        _Messenger: {
                            path: "cross-domain/messenger.js"
                        },
                        template: {
                            path: "arttemplate/template.js"
                        },
                        template_helper: {
                            requires: ['fun', 'dictionary'],
                            condition : {
                                trigger: 'template',
                                test: function(Y) {
                                    return true;
                                }
                            },
                            path: "arttemplate/template-helper.js"
                        },
                        _$css_dialog: {
                            type: 'css',
                            path: "jquery-artdialog/skins/wldialog.css"
                        },
                        $dialog: {
                            requires: ['_$css_dialog'],
                            path: "jquery-artdialog/jquery.artDialog.js"
                        },
                        $dialog_plugin: {
                            condition : {
                                trigger: '$dialog',
                                test: function(Y) {
                                    return true;
                                }
                            },
                            path: 'jquery-artdialog/jquery.artDialog.plugin.js'
                        },
                        swf_object: {
                            path: "swfupload/swfupload.swfobject.js"
                        },
                        swf_queue: {
                            path: "swfupload/swfupload.queue.js"
                        },
                        swfupload: {
                            requires: ['swf_object'],
                            path: "swfupload/swfupload.js"
                        },
                        $swfupload: {
                            requires: ['swfupload'],
                            path: "jquery-swfupload/jquery.swfupload.js"
                        },
                        tab: {
                            path: "yetii.js"
                        },
                        css_ztree: {
                            type: "css",
                            path: "ztree/zTreeStyle/zTreeStyle.css"
                        },
                        tree: {
                            requires: ["css_ztree"],
                            path: "ztree/jquery.ztree.core-3.5.js"
                        },
                        $validation: {
                            path: "jquery-validation/jquery.validate.js"
                        },
                        $validation_method: {
                            condition : {
                                trigger: '$validation',
                                test: function(Y) {
                                    return true;
                                }
                            },
                            path: 'jquery-validation/custom-validate-methods.js'
                        },
                        wdatepicker: {
                            path: 'My97DatePicker/WdatePicker.js'
                        },
                        $pager: {
                            requires: ['public'],
                            path: 'pager.js'
                        },
                        _ueditor_config: {
                            path: 'ueditor/ueditor.config.js'
                        },
                        ueditor: {
                            requires: ['_ueditor_config'],
                            path: 'ueditor/ueditor.all.js'
                        },
                        $imgareaselect: {
                            requires: ['_css_imgareaselect'],
                            path: 'jquery-imgareaselect/jquery.imgareaselect.js'
                        }
                    }
                },
                components: {
                    base: webRootPath + 'js/component/',
                    combine: combine,
                    root: 'component/',
                    modules: {
                        public_messenger:{
                            requires: public_messenger_requires,
                            path: "public/public-messenger.js"
                        },
                        _aes: {
                            path: "aes.js"
                        },
                        _aes_mode_ecb: {
                            requires: ["_aes"],
                            path: "mode-ecb.js"
                        },
                        AES: {
                            requires: ["_aes_mode_ecb"],
                            path: "aes-jiemi.js"
                        },
                        MD5: {
                            path: "md5.js"
                        },
                        JSON: {
                            path: "json2.js"
                        },
                        messageTip: {
                            path: 'global/message-tip.js'
                        }
                    }
                },
                modules: {
                    base: webRootPath + 'js/',
                    combine: combine,
                    root: '',
                    modules: {
                        public: {
                            requires: public_requires,
                            path: "public.js"
                        },
                        fun: {
                            path: "fun.js"
                        },
                        $check: {
                            path: 'wlcheck.js'
                        },
                        $common: {
                            path: 'wlcommon.js'
                        },
                        $select: {
                            required: ['$tip'],
                            path: 'wlselect.js'
                        },
                        $tip: {
                            required: ['$dialog'],
                            path: 'wltip.js'
                        }
                    }
                },
                customs: {
                    base: webRootPath + 'js/custom/',
                    combine: combine,
                    root: 'custom',
                    modules: {
                        $imageUpload: {
                            requires: ['$swfupload'],
                            path: "custom-image-upload.js"
                        },
                        $customTagsEdit: {
                            requires: ['fun', '$dialog'],
                            path: "custom-tags-edit.js"
                        },
                        $customImgAreaSelect: {
                            requires: ['swfupload', 'public', 'fun', '$dialog'],
                            path: "custom-imgareaselect.js"
                        }
                    }
                },
                CSS: {
                    base: webRootPath + "css/",
                    combine: combine,
                    root: "css",
                    modules: {
                        _css_imgareaselect: {
                            type: "css",
                            path: "imgareaselect/imgareaselect-default.css"
                        }
                    }
                },
                other: {
                    base: webRootPath + "js/",
                    combine: combine,
                    root: "",
                    modules: {
                        dictionary: {
                            path: 'data/dictionary.js'
                        }
                    }
                }
            }
        }
    };

    YDH.Cookie = {
        get: function (name) {
            var cookies = fnParseCookieString(document.cookie);
            var cookie = cookies[name];
            if (typeof cookie === 'undefined') {
                cookie = null;
            }
            return cookie;
        },
        set: function (name, value, options) {
            document.cookie = fnCreateCookieString(name, value, options);
        },
        remove : function (name, options) {
            options = options || YDH.CONFIG.Cookie[0];
            options.expires = new Date(0);
            if (name) {
                this.set(name, '', options);
                return;
            }
            var cookies = fnParseCookieString(document.cookie);
            for (var cookie_name in cookies) {
                this.set(cookie_name, '', options);
            }
        }
    }

    YDH.CONST.Session = YDH.Cookie.get('ydhSession');
    YDH.CONST.AesKey = YDH.Cookie.get('ydhAesKey');
    YDH.CONST.UserId = YDH.Cookie.get('ydhUserId');

    YDH.GLOBAL = {
        fnBackToLogin: function () {
            YDH.Cookie.remove();
            location.replace(YDH.CONST.WebRootPath + 'user/login.html');
        }
    }

    function isString (str) {
        return typeof(str) === 'string';
    }
    function fnParseCookieString (text) {

        var cookies = {};

        if (isString(text) && text.length > 0) {

            var cookieParts = text.split(/;\s/g),
                cookieName  = null,
                cookieValue = null,
                cookieNameValue = null;

            for (var i=0, len=cookieParts.length; i < len; i++){

                cookieNameValue = cookieParts[i].match(/([^=]+)=/i);
                if (cookieNameValue instanceof Array){
                    try {
                        cookieName = decodeURIComponent(cookieNameValue[1]);
                        cookieValue = decodeURIComponent(cookieParts[i].substring(cookieNameValue[1].length+1));
                    } catch (ex){
                        //intentionally ignore the cookie - the encoding is wrong
                    }
                } else {
                    //means the cookie does not have an "=", so treat it as a boolean flag
                    cookieName = decodeURIComponent(cookieParts[i]);
                    cookieValue = "";
                }
                cookies[cookieName] = cookieValue;
            }

        }

        return cookies;
    }
    function fnCreateCookieString (name, value, options) {

        options = options || YDH.CONFIG.Cookie[0] || {};

        var text = encodeURIComponent(name) + "=" + encodeURIComponent(value),
            expires = options.expires,
            path    = options.path,
            domain  = options.domain;


        if (typeof(options) === 'object'){
            //expiration date
            if (expires instanceof Date){
                text += "; expires=" + expires.toUTCString();
            }

            //path
            if (isString(path) && path !== ""){
                text += "; path=" + path;
            }

            //domain
            if (isString(domain) && domain !== ""){
                text += "; domain=" + domain;
            }

            //secure
            if (options.secure === true){
                text += "; secure";
            }
        }

        return text;
    }
})();

//YUI全局变量
//window["Y"] = YUI(YDH.CONFIG.YUI3) || {"version":"3.10.0"};
window["Y"] = YUI(YDH.CONFIG.YUI3) || {"version":"3.4.1"};

