"use strict";
(function(){	
	var _match = $('#topNavBar ul li a').filter(
	function(index){
		var _txt = $(this).text();
		var _title = document.title;
		var _sub = _title.slice(0, _txt.length);
		return _sub === _txt;
	}).parent().addClass("active");
})();