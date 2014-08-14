;
define(function(require, exports, module) {
    var IEVersion = require('utils/ieversion.js');
    var Hash = require('utils/url/hash.js');
    var $ = require('jquery');
    var STATIC = require('static');
    var LOAD_CONTAINER_ID = '#' + STATIC.LOAD_CONTAINER_ID;
    var $LOAD_CONTAINER_ID = $(LOAD_CONTAINER_ID);

    window.onhashchange = function() {
        var hash = Hash.get();
        if (!hash) {
            return;
        }
        $.ajax({
            type: 'GET'
                    , url: hash
                    , success: function(data, textStatus, jqXHR) {
                $('html,body').animate({scrollTop: 0}, 'slow');
                $LOAD_CONTAINER_ID.html(data);
            }
            , error: function(XMLHttpRequest, textStatus, errorThrown) {
                switch (XMLHttpRequest.status) {
                    case 404:
                        location.replace(location.pathname);
                        break;
                }
            }
        });
    }
    window.onhashchange();
    if (IEVersion && IEVersion < 8) {
        require.async('plugins/hashchange/iehashchange.js');
    }
});