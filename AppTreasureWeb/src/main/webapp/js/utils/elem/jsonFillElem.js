;
define(function(require, exports, module) {
    var $ = require('jquery');

    exports.fill = fill;

    function fill(elem, json) {
        var $elem = $(elem);
        for (var name in json) {
            var $input = $elem.find('[name=' + name + ']');
            var value;
            if (typeof json[name] === 'object') {
                value = JSON.stringify(json[name]);
            } else {
                value = json[name];
            }
            $input.each(function() {
                var $this = $(this);
                if ($this.is(':radio, :checkbox')) {
                    if (this.value == value) {
                        this.checked = true;
                        $this.change();
                    }
                } else if ($input.is(':input')) {
                    $this.val(value);
                } else {
                    $this.html(value);
                }
            });
        }
        $elem.find('[data-dynamic-name]').each(function() {
            var $this = $(this);
            var dynamic_name = $this.attr('data-dynamic-name');
            var value = json[dynamic_name];
            if ($this.is(':input')) {
                $this.val(value);
            } else {
                $this.html(value).attr('title', value);
            }
        });
    }
});