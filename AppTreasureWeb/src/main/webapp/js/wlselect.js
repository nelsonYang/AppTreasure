/* 
* 拓展下拉框(带搜索)
* filename: wlselect.js
* author: jierhcou
* date: 2013.8.20
*/
$.fn.extend({
	wlselect: function(obj){
		var myobj={};
		myobj.data=[];
		myobj.relate=obj.relate?obj.relate:false;
		myobj.selectParam=obj.selectParam?obj.selectParam:false;
		myobj.map=obj.map?obj.map:false;
		// 选项鼠标滑过及选定
		var selectLi = function() {
			var selectUl = $(".select-list");
			selectUl.on("mouseover", "li", function() {
				if($(this).index()>0){
					$(this).addClass('li-hover');
				}
			});
			selectUl.on("mouseout", "li", function() {
				$(this).removeClass('li-hover');
			});
			selectUl.on("click", "li", function() {
				var inputVal=$(this).text();
				$(this).parent().parent().hide().siblings('input:text').val(inputVal).siblings('input:hidden').val($(this).attr("data-id")).parent().removeClass('item-zindex');
				/*if(myobj.relate){
					if(myobj.relate.nextRelate){
						$("#"+myobj.relate.nextRelate).val(myobj.relate.nextRelateDefault);
					}
				}*/
			});
		}
		var getList = function() {
            var sUrl = YDH.Fun.fnGetActionUrl(myobj.selectParam.getAct);
            var oParam = {
                "encryptType": 1
            };
            if (myobj.selectParam.data) {
                oParam.data = myobj.selectParam.data;
            }
            var callback = function(data) {
                if (data && data.resultCode == 0) {
                    dataToMyobj(data.data[myobj.map.listname],myobj.map);
                } else {
                    $("body").wltip("查询数据失败", "error");
                }
            }
            YDH.PUBLIC.fnPostAjax(sUrl, oParam, callback);
        }
		var loadList = function() {
			var selectArr = []; // 列表li数组
			if(myobj.defaultVal){ selectArr.push('<li data-id="">' + myobj.defaultVal + '</li>'); }
			for (var i = 0, len = myobj.data.length; i < len; i++) {
				selectArr.push('<li data-id="' + myobj.data[i]["id"] + '">' + myobj.data[i]["value"] + '</li>');
			}
			selectArr = selectArr.join("");
			var $selecboxUl=myobj.selectBox.toggle().children('ul');
			$selecboxUl.html(selectArr);
			if(!myobj.selectParam){
				$selecboxUl.addClass('no-database-list');
			}else {
				myobj.selectBox.find('input').click(function(event) {
					event.stopPropagation();
					$(this).keyup(function() {
						var searchVal = $(this).val().toLowerCase();
						var ul = $(this).parent().parent().siblings('ul');
						if (searchVal != "") {
							var resArr = [];
							if(myobj.defaultVal){ resArr.push('<li data-id="">' + myobj.defaultVal + '</li>'); }
							for (var j=myobj.data.length; j--;) {
								var dataValue=myobj.data[j]
								if (dataValue["value"].toLowerCase().indexOf(searchVal) != -1) {
									resArr.push('<li data-id="' + dataValue["id"] + '">' + dataValue["value"] + '</li>');
								}
							}
							ul.html(resArr.join(""));
						} else {
							ul.html(selectArr);
						}
					});
				});
			}
			selectLi();
		}
		var dataToMyobj = function(data,dataParam){
			myobj.data=[];
			for(var i=0,len=data.length; i<len; i++){
				myobj.data.push({});
				for(var key in data[i]){
					myobj.data[i]["id"]=data[i][dataParam.key];
					myobj.data[i]["value"]=data[i][dataParam.name];
				}
			}
			loadList();
		}
		// 点击聚焦显示列表选项及搜索等处理
		$(this).on("click",function(e) {
			e.stopPropagation();
			$(this).attr("readonly","readonly");
			if(!myobj.defaultVal){
				myobj.defaultVal=obj.defaultVal?obj.defaultVal:$(this).val();
			}
			myobj.selectBox = $(this).siblings('.select-box');
			$(".select-box").not(myobj.selectBox).hide().parent().removeClass('item-zindex');
			$(this).parent().toggleClass('item-zindex');
			/* 判断是否从数据字典中获取 */
			if(obj.data){
				if(myobj.data.length<1){
					dataToMyobj(obj.data, {"key": "value","name": "text"});
				}else{
					loadList();
				}
			} else {
				/* 从数据库中获取数据, 此时判断是否有关联 */
				if(myobj.relate){
					/* 如果有关联, 接着判断是否有关联前一个表单 */
					if(myobj.relate.prevRelate){
						/* 关联前一个表单，先判断前一个表单是否有值 */
						var prevInputVal = $("#"+obj.relate.prevRelate).val();
						/* 如果为空, 则提示错误 */
						if(prevInputVal==""){
							$("body").wltip(obj.relate.prevRelateNotice,"error");
							return;
						}
						/* 判断关联的表单值跟储存的值是否相等, 相等则表示存在data数据 */
						if(myobj.prevInputVal==prevInputVal){
							loadList();
						} else {
							myobj.prevInputVal=prevInputVal;
							if(!myobj.selectParam.data){
								myobj.selectParam.data={};
							}
							myobj.selectParam.data[myobj.selectParam.paramId]=prevInputVal;
							getList(myobj.selectParam,myobj.map);
						}
					} else {
						/* 如果没有关联前一个表单, 则判断是否存在data */
						if(myobj.data.length<1){
						/* 不存在则从数据库中获取 */
							getList(obj.selectParam,obj.map);
						}else{
							/* 存在则直接调用load */
							loadList();
						}
					}
				} else {
					/* 如果没有关联 */
					/* 判断是否存在data数据 */
					if(myobj.data.length<1){
						/* 不存在则从数据库中获取 */
						getList(obj.selectParam,obj.map);
					}else{
						/* 存在则直接调用load */
						loadList();
					}
				}
			}
		});
		$("html").click(function(event) {
			$(".select-box").hide().parent().removeClass('item-zindex');
		});
	}
});