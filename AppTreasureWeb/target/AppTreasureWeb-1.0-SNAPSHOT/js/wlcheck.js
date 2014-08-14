/* 
* 自定义checkbox和radio样式
* filename: wlcheck.js
* author: jierhcou
* date: 2013.8.21
*/
$.fn.extend({
	wlcheck: function  () {
		return $(this).on('change', ':radio, :checkbox', function(){
            var checked = this.checked;
            var $this = $(this);
            var type = this.type;
            var type_checked = type + '-checked';
            if (checked) {
                $this.siblings("span").removeClass(type).addClass(type_checked);
                type === 'radio' && $this.parent().siblings("label").children("span").removeClass("radio-checked").addClass('radio');
            } else {
                $this.siblings("span").removeClass(type_checked).addClass(type);
            }
		})
        .find(':radio, :checkbox').trigger('change')
        .end();
	}
}
);