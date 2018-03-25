$(document).ready(function() {

/*INIZIO POPUP*/

	$(".max-edt1").click(
	    function(){
			$('#n1-editor').addClass('editor-max');
                        $('#n2-editor').addClass('hidden');
                        $('.max-edt1').addClass('hidden');
                        $('.min-edt1').addClass('show');
		});
		
	$(".min-edt1").click(
	    function(){
			$('#n1-editor').removeClass('editor-max');
                        $('#n2-editor').removeClass('hidden');
                        $('.max-edt1').removeClass('hidden');
                        $('.min-edt1').removeClass('show');
		});

	$(".max-edt2").click(
	    function(){
			$('#n2-editor').addClass('editor-max');
                        $('#n1-editor').addClass('hidden');
                        $('.max-edt2').addClass('hidden');
                        $('.min-edt2').addClass('show');
		});
		
	$(".min-edt2").click(
	    function(){
			$('#n2-editor').removeClass('editor-max');
                        $('#n1-editor').removeClass('hidden');
                        $('.max-edt2').removeClass('hidden');
                        $('.min-edt2').removeClass('show');
		});
/*FINE POPUP*/

   });   

