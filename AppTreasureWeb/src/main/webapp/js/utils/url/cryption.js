;
define(function(require, exports) {
    exports.encrypt = encrypt;
    exports.decrypt = decrypt;

    function encrypt(text) {
        return encodeURIComponent(text);
    }
    function decrypt(text) {
        return decodeURIComponent(text);
    }
});