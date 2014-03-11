"use strict";
(function(){
	var images = $('#imageSelect');
	$('.add-modal-lg').on('show.bs.modal', function (e) {
		var _i = images;
		_i.empty();
		_i.html('<h1><i class="fa fa-spinner fa-spin"></i></h1>');
		
		$.getJSON(_i.data('rootFolder'), {} )
		.done(function( json ) {
			var _f = folders;
			_f.empty();
			
			for(var folder in json){
				_f.append('<a href="' + folder + '" class="list-group-item">' + json[folder].name + '</a>')
			}
		})
		.fail(function( jqxhr, textStatus, error ) {
			var _f = folders;
			
			_f.html('<p>Unable to load data!</p>');
		});
	});
})();