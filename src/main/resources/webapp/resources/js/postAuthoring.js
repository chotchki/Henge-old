var counter = 0;

function preview(){
	var self=this, seq=++this.counter;
	$.post('/blog/preview', $('#postForm').serialize())
	.done(function(data){
		if(seq === self.counter){
			$('#preview').html(data);
		}
	});	
}

$('input[type=text],textarea').each(function() {
	var elem = $(this);

	// Look for changes in the value
	elem.bind("propertychange keyup input paste", function(event){
		preview();
	});
});