"use strict";
(function(){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var param = $("meta[name='_csrf_param']").attr("content");
	
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	
	$("form[method='post']").append('<input type="hidden" name="' + param +'" value="' + token + '" />');
})();