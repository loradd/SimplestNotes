$(document).ready(function(){
        /*VUOTI*/
        $("#passProfileOld").addClass("0");
        $("#passProfile1").addClass("0");
        $("#passProfile2").addClass("0");
	/*variabili */
        var stringReg = /^(([a-zA-Z0-9\"\xE0\xE8\xE9\xF9\xF2\xEC\x27]\s?)+)*$/;
        var emailReg = /^([\w-\.]+@([\w-]{2,63}\.)+[\w-]{2,4})?$/;
        var passReg = /^(([a-z0-9]*[A-Z]+[A-Za-z0-9]*[0-9]+[A-Za-z0-9]*)*([a-z0-9]*[0-9]+[A-Za-z0-9]*[A-Z]+[A-Za-z0-9]*)*)$/;
	var Obbligatorio="Required";
	var SqlInjection="It looks like you entered an invalid character";
        var filter = "invalid input, it will be ignored"
        var checkPass="A valid password has to contain at least one capital letter and one numeric character"
	var mailTest="Check your mail addres";
        var passTest="It looks like the two passwords do not match";
	/*var errorPass="La password inserita non e' utilizzabile";*/
        var long = "Title is too long, reduce it or it will be repleaced by a default name";


/**************************** METODI ******************************************/
function setIt0(item){
    $(item).addClass("0").removeClass("2").removeClass("1");
}
function setIt1(item){
    $(item).addClass("1").removeClass("0").removeClass("2");
}
function setIt2(item){
    $(item).addClass("2").removeClass("0").removeClass("1");
}
        
/**************************** LOGIN *******************************************/	

	/*check mail Login LOGIN*/
	$('.checkButton').hover(function(){

	    if ($('.mailField').val() != ""){ 
	                   	var email=$('.mailField').val();
	            if( !emailReg.test( email ) ) {
		    		$(".mailField").addClass("validateForm");
                                setIt2(".mailField");
		    		$(".genericSuggest").addClass("alertOn");
		    		$(".genericSuggest").removeClass("alertOff");
    				$(".genericMessage").text("");
    				$(".genericMessage").append(mailTest);
	            } else {
			$(".mailField").removeClass("validateForm");
                        setIt1(".mailField");
	                }
		}else{
    		$(".mailField").addClass("validateForm");
                setIt0(".mailField");
                $(".mailField").attr("placeholder",Obbligatorio);
		}
	});
        
	/*check pass login "passField" LOGIN*/
	$('.checkButton').hover(function(){

	    if ($(".passField").val() != ""){ 
	            var string=$(".passField").val();
	            if( !passReg.test( string ) ) {
		    		$(".passField").addClass("validateForm");
                                setIt2(".passField");
                                $(".genericSuggest").addClass("alertOn");
		    		$(".genericSuggest").removeClass("alertOff");
    				$(".genericMessage").text("");
				$(".genericMessage").append(checkPass);
                                
	            } else {
                        $(".passField").removeClass("validateForm");
                        setIt1(".passField");
	            }
		}else{
                $(".passField").addClass("validateForm");
                setIt0(".passField");
                $(".passField").attr("placeholder",Obbligatorio);
		}
	});

	/*abilita button submit LOGIN*/
	$('.checkButton').hover(function(){
		if(($(".mailField").hasClass("1"))&&($(".passField").hasClass("1"))){
			/* alert("true");*/
			$("#buttonLogin").removeAttr("disabled");
                        $(".genericSuggest").addClass("alertOff");
                        $(".genericSuggest").removeClass("alertOn");
                    }else{
                        $("#buttonLogin").attr("disabled","true");
                    }
	});
/**************************** UNLEASH *****************************************/
        /*unleash field at focusout*/
        $('.unleash').click(function(){
                $(this).removeClass("validateForm"); 
                $(".genericSuggest").addClass("alertOff");
                $(".genericSuggest").removeClass("alertOn");
                $(this).attr("placeholder","");
        });
        $('.unleash').focusout(function(){
                $(this).removeClass("validateForm"); 
                $(".genericSuggest").addClass("alertOff");
                $(".genericSuggest").removeClass("alertOn");
        });

/**************************** SIGNIN ******************************************/
        /*check if pass equals SIGNIN*/
	$('#checkButtonSign').hover(function(){

		if (($('.pass1Sign').val() != "")&&($('.pass2Sign').val() != "")){ 
	                   	var pass1=$('.pass1Sign').val();
	              		var pass2=$('.pass2Sign').val();
	            if( pass1==pass2) {
					$('.pass2Sign').removeClass("validateForm");
                                        setIt1(".pass2Sign");
					$(".pass1Sign").removeClass("validateForm");
                         		$(".genericSuggest").removeClass("alertOn");
                                        $(".genericSuggest").addClass("alertOff");
                                        $('.pass2Sign').attr("placeholder","");
	            } else {
				$('.pass2Sign').addClass("validateForm");
                                setIt2(".pass2Sign");
		    		$(".pass1Sign").addClass("validateForm");

                            }
		}else{
    		$('.pass2Sign').addClass("validateForm");
                setIt0(".pass2Sign");
                $('.pass2Sign').attr("placeholder",Obbligatorio);
		}
	});
        
        /*check ifname SIGNIN*/
        $('#checkButtonSign').hover(function(){

	    if ($('.nameSign').val() != ""){ 
	            var string=$('.nameSign').val();
	            if( !stringReg.test( string ) ) {
		    		$('.nameSign').addClass("validateForm");
                                setIt2(".nameSign");
	            } else {
					$('.nameSign').removeClass("validateForm");
                                        $('.nameSign').attr("placeholder","");
					setIt1(".nameSign");
	                }
		}else{
                    $('.nameSign').addClass("validateForm");
                    setIt0(".nameSign");
                    $('.nameSign').attr("placeholder",Obbligatorio);
		}
	});
        
        /*check ifsurname SIGNIN*/
        $('#checkButtonSign').hover(function(){

	    if ($('.surnameSign').val() != ""){ 
	            var string=$('.surnameSign').val();
	            if( !stringReg.test( string ) ) {
		    		$('.surnameSign').addClass("validateForm");
                                setIt2(".surnameSign");
	            } else {
					$('.surnameSign').removeClass("validateForm");
					setIt1(".surnameSign");
                                        $('.surnameSign').attr("placeholder","");
	                }
		}else{
                    $('.surnameSign').addClass("validateForm");
                    setIt0(".surnameSign");
                    $('.surnameSign').attr("placeholder",Obbligatorio);
		}
	});
 
        /*check ifmail SIGNIN*/
	$('#checkButtonSign').hover(function(){

	    if ($('.mailSign').val() != ""){ 
	            var email=$('.mailSign').val();
	            if( !emailReg.test( email ) ) {
		    		$('.mailSign').addClass("validateForm");
                                setIt2(".mailSign");
	            } else {
					setIt1(".mailSign");
					$('.mailSign').removeClass("validateForm");
                                        $(".mailSign").attr("placeholder","");
	                }
		}else{
    		$('.mailSign').addClass("validateForm");
                setIt0(".mailSign");
                $(".mailSign").attr("placeholder",Obbligatorio);
		}
	});

        /*check ifpass1 SIGNIN*/
        $('#checkButtonSign').hover(function(){

	    if ($('.pass1Sign').val() != ""){ 
	            var string=$('.pass1Sign').val();
	            if( !passReg.test( string ) ) {
		    		$('.pass1Sign').addClass("validateForm");
                                setIt2(".pass1Sign");
	            } else {
					$('.pass1Sign').removeClass("validateForm");
					setIt1(".pass1Sign");
                                        $('.pass1Sign').attr("placeholder","");
	                }
		}else{
                    $('.pass1Sign').addClass("validateForm");
                    setIt0(".pass1Sign");
                    $('.pass1Sign').attr("placeholder",Obbligatorio);
		}
	});
        /*check ifpass2 SIGNIN*/
        $('#checkButtonSign').hover(function(){

	    if (($('.pass1Sign').val() != "")&&($('.pass2Sign').val() != "")){ 
	                   	var pass1=$('.pass1Sign').val();
	              		var pass2=$('.pass2Sign').val();
	            if( pass1==pass2) {
                            $('.pass2Sign').removeClass("validateForm");
                            setIt1(".pass2Sign");
                            $(".pass1Sign").removeClass("validateForm");
                            $('.pass2Sign').attr("placeholder","");
	            } else {
                            $('.pass2Sign').addClass("validateForm");
                            setIt2(".pass2Sign");
                            $(".pass1Sign").addClass("validateForm");
                            }
		}else{
    		$('.pass2Sign').addClass("validateForm");
                setIt0(".pass2Sign");
                $('.pass2Sign').attr("placeholder",Obbligatorio);
            }
	});
        
        /*check Button SIGNIN*/
        $('#checkButtonSign').hover(function(){
		if(($(".nameSign").hasClass("1"))&&($(".surnameSign").hasClass("1"))&&($(".pass1Sign").hasClass("1"))&&($(".pass2Sign").hasClass("1"))&&($(".mailSign").hasClass("1")))
		{
		/*alert("true");*/
		$("#signinButton").removeAttr("disabled");
		}
                else if ($(".nameSign").hasClass("2")){
                    /*surname error*/
                    $(".genericSuggest").addClass("alertOn");
                    $(".genericSuggest").removeClass("alertOff");
                    $(".genericMessage").text("");
                    $(".genericMessage").append(SqlInjection);
                    $("#signinButton").attr("disabled","true");
                }
                else if ($(".surnameSign").hasClass("2")){
                    /*name error*/
                    $(".genericSuggest").addClass("alertOn");
                    $(".genericSuggest").removeClass("alertOff");
                    $(".genericMessage").text("");
                    $(".genericMessage").append(SqlInjection);
                    $("#signinButton").attr("disabled","true");
                }
                else if (($(".mailSign").hasClass("2"))){
                    /*mail error*/
                    $(".genericSuggest").addClass("alertOn");
                    $(".genericSuggest").removeClass("alertOff");
                    $(".genericMessage").text("");
                    $(".genericMessage").append(mailTest);
                    $("#signinButton").attr("disabled","true");
                }
                else if (($(".pass2Sign").hasClass("2"))&&($(".pass1Sign").val()!="")){
                    /*pass error equals*/
                    $(".genericSuggest").addClass("alertOn");
                    $(".genericSuggest").removeClass("alertOff");
                    $(".genericMessage").text("");
                    $(".genericMessage").append(passTest);
                    $("#signinButton").attr("disabled","true");
                }
                else if (($(".pass1Sign").hasClass("2"))){
                    /*pass error 1*/
                    $(".genericSuggest").addClass("alertOn");
                    $(".genericSuggest").removeClass("alertOff");
                    $(".genericMessage").text("");
                    $(".genericMessage").append(checkPass);
                    $("#signinButton").attr("disabled","true");
                }
                else{
                    $("#signinButton").attr("disabled","true");
                    /*alert("false");*/
                }
	});
/***************************** ALERT ******************************************/     
	/*close alert on click*/
	$('.genericSuggest').click(function(){
			    	$(this).addClass("alertOff").removeClass("alertOn");
	});
      
/*************************** PROFILE ******************************************/
/* CHECK (1)*/

        /*check ifname PROFILE*/
        $('#checkEdit1').hover(function(){

	    if ($('#nameProfile').val() != ""){ 
	                   	var string=$('#nameProfile').val();
	            if( !stringReg.test( string ) ) {
		    		$('#nameProfile').addClass("validateForm");
                                setIt2("#nameProfile");
		    		$(".genericSuggest").addClass("alertOn");
		    		$(".genericSuggest").removeClass("alertOff");
    				$(".genericMessage").text("");
				$(".genericMessage").append(SqlInjection);
	            } else {
					$('#nameProfile').removeClass("validateForm");
					setIt1("#nameProfile");
                                        $(".genericSuggest").addClass("alertOff");
                                        $(".genericSuggest").removeClass("alertOn");
	                }
		}else{
                    $('#nameProfile').addClass("validateForm");
                    setIt0("#nameProfile");
                    $('#nameProfile').attr("placeholder",Obbligatorio);
		}
	});
        
        /*check ifsurname PROFILE*/
        $('#checkEdit1').hover(function(){

	    if ($('#surnameProfile').val() != ""){ 
	                   	var string=$('#surnameProfile').val();
	            if( !stringReg.test( string ) ) {
		    		$('#surnameProfile').addClass("validateForm");
                                setIt2("#surnameProfile");
		    		$(".genericSuggest").addClass("alertOn");
		    		$(".genericSuggest").removeClass("alertOff");
    				$(".genericMessage").text("");
				$(".genericMessage").append(SqlInjection);
	            } else {
					$('#surnameProfile').removeClass("validateForm");
					setIt1("#surnameProfile");
	                }
		}else{
                    $('#surnameProfile').addClass("validateForm");
                    setIt0("#surnameProfile");
                    $('#surnameProfile').attr("placeholder",Obbligatorio);
		}
	});
        
        /*check ifmail PROFILE*/
	$('#checkEdit1').hover(function(){

	    if ($('#mailProfile').val() != ""){ 
	                   	var email=$('#mailProfile').val();
	            if( !emailReg.test( email ) ) {
		    		$('#mailProfile').addClass("validateForm");
                                setIt2("#mailProfile");
		    		$(".genericSuggest").addClass("alertOn");
		    		$(".genericSuggest").removeClass("alertOff");
    				$(".genericMessage").text("");
    				$(".genericMessage").append(mailTest);
	            } else {
					setIt1("#mailProfile");
					$('#mailProfile').removeClass("validateForm");
	                }
		}else{
    		$('#mailProfile').addClass("validateForm");
                $('#mailProfile').attr("placeholder",Obbligatorio);
                setIt0("#mailProfile");		}
	});

/* CHECK (2)*/
        /*check ifpass1 SIGNIN*/
        $('#checkEdit2').hover(function(){
            if(($('#passProfile1').val()!="")){
                    var string=$('#passProfile1').val();
	            if( !passReg.test( string ) ) {
		    		$('#passProfile1').addClass("validateForm");
                                setIt2("#passProfile1");
		    		$(".genericSuggest").addClass("alertOn");
		    		$(".genericSuggest").removeClass("alertOff");
    				$(".genericMessage").text("");
				$(".genericMessage").append(checkPass);
	            } else {
					$('#passProfile1').removeClass("validateForm");
					setIt1("#passProfile1");
                                        $(".genericSuggest").addClass("alertOff");
                                        $(".genericSuggest").removeClass("alertOn");
	                }
            }else {
                setIt0("#passProfile1");
                $('#passProfile1').removeClass("validateForm");
                $('#passProfile1').attr("placeholder","");
                $("#buttonEdit2").attr("disabled","true");
            }
	});

        /*check ifpass2 PROFILE*/
        $('#checkEdit2').hover(function(){
            if(($('#passProfile2').val()!="")){
                    var string=$('#passProfile2').val();
	            if( !passReg.test( string ) ) {
		    		$('#passProfile2').addClass("validateForm");
                                setIt2("#passProfile2");
		    		$(".genericSuggest").addClass("alertOn");
		    		$(".genericSuggest").removeClass("alertOff");
    				$(".genericMessage").text("");
				$(".genericMessage").append(checkPass);
	            } else {
					$('#passProfile2').removeClass("validateForm");
					setIt1("#passProfile2");
                                        $(".genericSuggest").addClass("alertOff");
                                        $(".genericSuggest").removeClass("alertOn");
	                }
            }else{
                setIt0("#passProfile2");
                $('#passProfile2').removeClass("validateForm");
                $('#passProfile2').attr("placeholder","");
                $("#buttonEdit2").attr("disabled","true");
            }
	});

        
        /*check ifpassOld PROFILE*/
        $('#checkEdit2').hover(function(){
            if(($('#passProfileOld').val()!="")){
                    var string=$('#passProfileOld').val();
	            if( !passReg.test( string ) ) {
		    		$('#passProfileOld').addClass("validateForm");
                                setIt2("#passProfileOld");

	            } else {
					$('#passProfileOld').removeClass("validateForm");
					setIt1("#passProfileOld");
                                        $(".genericSuggest").addClass("alertOff");
                                        $(".genericSuggest").removeClass("alertOn");
	                }
            }else{
                setIt0("#passProfileOld");
                $('#passProfileOld').removeClass("validateForm");
                $('#passProfileOld').attr("placeholder","");
                $("#buttonEdit2").attr("disabled","true");
            }
        });
        /*check pass equals PROFILE*/
	$('#checkEdit2').hover(function(){
            if(($('#passProfile1').val()!="")&&($('#passProfile1').hasClass("1"))){
		if (($('#passProfile1').val() != "")&&($('#passProfile2').val() != "")){ 
	                   	var pass1=$('#passProfile1').val();
	              		var pass2=$('#passProfile2').val();
	            if( pass1==pass2) {
                            $('#passProfile2').removeClass("validateForm");
                            $('#passProfile1').removeClass("validateForm");
                            $(".genericSuggest").removeClass("alertOn");
                            $(".genericSuggest").addClass("alertOff");
                            setIt1("#passProfile2");
	            } else {
                        $('#passProfile2').addClass("validateForm");
                        $('#passProfile1').addClass("validateForm");
                        setIt2("#passProfile2");
	                }
		}else{
                    $('#passProfile2').addClass("validateForm");
                    setIt0("#passProfile2");
                    $('#passProfile2').attr("placeholder",Obbligatorio);
                    $("#buttonEdit2").attr("disabled","true");
                }
            }
	});
        
/* BUTTON*/
        /*controllo button cambio delle informazioni di profilo*/
        $('#checkEdit1').hover(function(){
		if(($("#mailProfile").hasClass("1"))&&($("#nameProfile").hasClass("1"))&&($("#surnameProfile").hasClass("1")))
		{
                    $("#buttonEdit1").removeAttr("disabled");
		}else{
                    $("#buttonEdit1").attr("disabled","true");
                }
	});
        /*controllo button cambio della mail*/
        $('#checkEdit2').hover(function(){
		if(($("#passProfileOld").hasClass("1"))&&($("#passProfile2").hasClass("1"))){
                    $("#buttonEdit2").removeAttr("disabled");
		}else if ($("#passProfileOld").hasClass("2")){
                    $("#buttonEdit2").attr("disabled","true");
                    $(".genericSuggest").addClass("alertOn");
                    $(".genericSuggest").removeClass("alertOff");
                    $(".genericMessage").text("");
                    $(".genericMessage").append(checkPass);
                    $('#passProfile1').removeClass("validateForm");
                    $('#passProfile2').removeClass("validateForm");
                }else if ($("#passProfile1").hasClass("2")){
                    $("#buttonEdit2").attr("disabled","true");
                    $(".genericSuggest").addClass("alertOn");
                    $(".genericSuggest").removeClass("alertOff");
                    $(".genericMessage").text("");
                    $(".genericMessage").append(checkPass);
                    $('#passProfileOld').removeClass("validateForm");
                }else if ($("#passProfile2").hasClass("2")){
                    $("#buttonEdit2").attr("disabled","true");
                    $(".genericSuggest").addClass("alertOn");
                    $(".genericSuggest").removeClass("alertOff");
                    $(".genericMessage").text("");
                    $(".genericMessage").append(passTest);
                    $('#passProfileOld').removeClass("validateForm");
                }else if(($("#passProfileOld").hasClass("0"))&&(!$("#passProfile2").hasClass("0"))){
                    $("#buttonEdit2").attr("disabled","true")
                    $('#passProfileOld').addClass("validateForm");
                    setIt0("#passProfileOld");
                    $('#passProfileOld').attr("placeholder",Obbligatorio);
                }else if(($("#passProfile2").hasClass("0"))&&(!$("#passProfileOld").hasClass("0"))){
                    $("#buttonEdit2").attr("disabled","true");
                    $('#passProfile2').addClass("validateForm");
                    setIt0("#passProfile2");
                    $('#passProfile2').attr("placeholder",Obbligatorio);
                    $('#passProfile1').addClass("validateForm");
                    setIt0("#passProfile1");
                    $('#passProfile1').attr("placeholder",Obbligatorio);
                } else if (($("#passProfile1").hasClass("0"))&&(!$("#passProfileOld").hasClass("0"))){
                    $("#buttonEdit2").attr("disabled","true");
                    $('#passProfile1').addClass("validateForm");
                    setIt0("#passProfile1");
                    $('#passProfile1').attr("placeholder",Obbligatorio);
                }
	});
  
    
/********************************** EDITOR ************************************/

        /*check long title 2*/
        $('#checkSaveButton1').hover(function(){
		var title = $('#title1').val().length;
                $("#saveButton1").attr("disabled","true");
		if (title>40){
                    $('#title1').addClass("validateForm");
                    $(".genericSuggest").addClass("alertOn");
                    $(".genericSuggest").removeClass("alertOff");
                    $(".genericMessage").text("");
                    $(".genericMessage").append(long);
                    $("#saveButton1").attr("disabled","true");
                }else{
                    if ($('#title1').val() != ""){ 
	                   	var string=$('#title1').val();
                                var questa = $('#title1').attr("id").toString();
                        if( !stringReg.test( string ) ) {
                                    $('#title1').addClass("validateForm");
                                    $("#saveButton1").attr("disabled","true");
                        } else {
                                    $('#title1').removeClass("validateForm");
                                    $("#saveButton1").removeAttr("disabled");
                            }
                    }else{
                        $("#saveButton1").attr("disabled","true");
                    }
                }
	});
        /*check long title 2*/
        $('#checkSaveButton2').hover(function(){
		var title = $('#title2').val().length;
                var textarea = $('#title2').attr("disabled").toString();
                $("#saveButton2").attr("disabled","true");
		if (title>40){
                    $('#title2').addClass("validateForm");
                    $(".genericSuggest").addClass("alertOn");
                    $(".genericSuggest").removeClass("alertOff");
                    $(".genericMessage").text("");
                    $(".genericMessage").append(long);
                    $("#saveButton2").attr("disabled","true");
                }else{
                    if ($('#title2').val() != ""){ 
	                   	var string=$('#title2').val();
                                var questa = $('#title2').attr("id").toString();
                        if( !stringReg.test( string ) ) {
                                    $('#title2').addClass("validateForm");
                                    $("#saveButton2").attr("disabled","true");
                        } else {
                                    $('#title2').removeClass("validateForm");
                                    $("#saveButton2").removeAttr("disabled");
                            }
                    }else{
                        $("#saveButton2").attr("disabled","true");
                    }
                }
	});

/**************************** CHECK STRING ************************************/

	/* checkString POPUP*/
	$('.checkString').keyup(function(){

	    if ($(this).val() != ""){ 
	                   	var string=$(this).val();
	            if( !stringReg.test( string ) ) {
		    		$(this).addClass("validateForm");
                                $(this).addClass("2").removeClass("0").removeClass("1");
		    		$(".genericSuggest").addClass("alertOn");
		    		$(".genericSuggest").removeClass("alertOff");
    				$(".genericMessage").text("");
                                if($(this).hasClass("search-note")){
                                    $(".genericMessage").append(filter);
                                }else{
                                    $(".genericMessage").append(SqlInjection);
                                }
	            } else {
					$(this).removeClass("validateForm");
					$(this).addClass("1").removeClass("0").removeClass("2");
                                        $(".genericSuggest").addClass("alertOff");
                                        $(".genericSuggest").removeClass("alertOn");
	                }
		}else{
                    $(this).addClass("0").removeClass("1").removeClass("2");
		}
	});
        filter
                /*checkstring NO POPUP*/
        	$('.checkStringN').keyup(function(){

                    if ($(this).val() != ""){ 
	                   	var string=$(this).val();
                                var questa = $(this).attr("id").toString();
                        if( !stringReg.test( string ) ) {
                                    $(this).addClass("validateForm");
                                    $(this).addClass("2").removeClass("0").removeClass("1");
                        } else {
                                    $(this).removeClass("validateForm");
                                    $(this).addClass("1").removeClass("0").removeClass("2");
                            }
                    }else{
                        $(this).addClass("validateForm");
                        $(this).attr("placeholder",Obbligatorio);
                        $(this).addClass("0").removeClass("1").removeClass("2");
                    }
	});
        
/**************************** MODAL VIEW **************************************/

        /*mail modal*/
        $('.checkMailP').keyup(function(){
                    if ($('.checkMailP').val() != ""){ 
	                   	var email=$('.checkMailP').val();
	            if( !emailReg.test( email ) ) {
		    	$('.checkMailP').addClass("validateForm");
                        $('.submitThis').attr("disabled","true");
                        $('.boxMessageP').html("is not still a valid mail address");
	            } else {
                            $('.submitThis').removeAttr("disabled");
                            $('.checkMailP').removeClass("validateForm");
                            $('.boxMessageP').html("");
	            }
		}else{
    		$('.checkMailP').addClass("validateForm");
                $('.checkMailP').attr("placeholder",Obbligatorio);
                $('.submitThis').attr("disabled","true");
		}
            });
        /*string modal*/
        $('.checkStringP').keyup(function(){
                    if ($(this).val() != ""){ 
	                   	var string=$(this).val();
                                var questa = $(this).attr("id").toString();
                        if( !stringReg.test( string ) ) {
                                    $(this).addClass("validateForm");
                                    $('.submitThis').attr("disabled","true");
                                    $('.boxMessageP').html("tag non corretto");
                        } else {
                                    $(this).removeClass("validateForm");
                                    $('.submitThis').removeAttr("disabled");
                                    $('.boxMessageP').html("");
                            }
                    }else{
                        $(this).addClass("validateForm");
                        $(this).attr("placeholder",Obbligatorio);
                        $('.submitThis').attr("disabled","true");
                    }
	});
});/*init*/
