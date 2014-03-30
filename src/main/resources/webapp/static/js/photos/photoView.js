"use strict";
(function(){
	var $container = $('.list-inline').masonry({
		isFitWidth: true
	});

	$container.imagesLoaded( function() {
	  $container.masonry({
		  isFitWidth: true
	  });
	});

		
})();