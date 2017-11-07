var FormValidation = function () {

	// basic validation
	var handleValidation1 = function() {
		// for more info visit the official plugin documentation: 
		// http://docs.jquery.com/Plugins/Validation

		var form1 = $('#form_save');
		var error1 = $('.alert-danger', form1);
		var success1 = $('.alert-success', form1);

		form1.validate({
			errorElement: 'span', //default input error message container
			errorClass: 'help-block help-block-error', // default input error message class
			focusInvalid: false, // do not focus the last invalid input
			ignore: "",  // validate all fields including form hidden input
			messages: {
				apk:{
					required: "上传文件不允许为空",  
					extension: "文件类型错误，请上传apk文件" 
				},
				change_type:{
					required:"修改类型不能为空"

				},
				version: {
					required: "版本号不能为空",
					remote:"版本号已存在"
				},

				note: {
					required: "修改内容不能为空",
					maxlength:"最大长度不能超过250"
				},
				isValid:{
					required:"开放设置不能为空"
				}


			},
			rules: {
				version:{
					required: true,
					remote:{
						type:"POST",
						url:"v_check_version.do",
						data:{
							veryCode:function(){return $("#form_version_error").val();}
						}
					}
				},
				apk: {  
					required: true,  
					extension: "apk"  
				},
				change_type: {
					required: true
				},
				note: {
					required: true,
					maxlength:250
				},
				isValid:{
					required:true
				}

			},

			errorPlacement: function (error, element) {

				if (element.attr("name") == "change_type") { // for uniform radio buttons, insert the after the given container
					error.insertAfter("#form_change_type_error");
				}
				else if(element.attr("name") == "apk"){
					error.insertAfter("#form_apk_error");
				}
				else if(element.attr("name") == "note"){
					error.insertAfter("#form_note_error");
				}
				else if(element.attr("name") == "isValid"){
					error.insertAfter("#form_isValid_error");
				}else if(element.attr("name") == "version"){
					error.insertAfter("#form_version_error");
				}
			},

			invalidHandler: function (event, validator) { //display error alert on form submit              
				success1.hide();
				error1.show();
				App.scrollTo(error1, -200);
			},

			highlight: function (element) { // hightlight error inputs
				$(element)
				.closest('.form-group').addClass('has-error'); // set error class to the control group
			},

			unhighlight: function (element) { // revert the change done by hightlight
				$(element)
				.closest('.form-group').removeClass('has-error'); // set error class to the control group
			},

			success: function (label) {
				label
				.closest('.form-group').removeClass('has-error'); // set success class to the control group
			},

			submitHandler: function (form) {
				success1.show();
				error1.hide();
				form[0].submit(); // submit the form
			}
		});


	}

	var handleValidation2 = function() {
		// for more info visit the official plugin documentation: 
		// http://docs.jquery.com/Plugins/Validation

		var form1 = $('#form_update');
		var error1 = $('.alert-danger', form1);
		var success1 = $('.alert-success', form1);

		form1.validate({
			errorElement: 'span', //default input error message container
			errorClass: 'help-block help-block-error', // default input error message class
			focusInvalid: false, // do not focus the last invalid input
			ignore: "",  // validate all fields including form hidden input
			messages: {
				
				change_type:{
					required:"修改类型不能为空"

				},

				note: {
					required: "修改内容不能为空",
					maxlength:"最大长度不能超过250"
				},


			},
			rules: {
				apk: {  
					required: true,  
					extension: "apk"  
				},
				change_type: {
					required: true
				},
				note: {
					required: true,
					maxlength:250
				},
				isValid:{
					required:true
				}

			},

			invalidHandler: function (event, validator) { //display error alert on form submit              
				success1.hide();
				error1.show();
				App.scrollTo(error1, -200);
			},

			highlight: function (element) { // hightlight error inputs
				$(element)
				.closest('.form-group').addClass('has-error'); // set error class to the control group
			},

			unhighlight: function (element) { // revert the change done by hightlight
				$(element)
				.closest('.form-group').removeClass('has-error'); // set error class to the control group
			},

			success: function (label) {
				label
				.closest('.form-group').removeClass('has-error'); // set success class to the control group
			},

			submitHandler: function (form) {
				success1.show();
				error1.hide();
				form[0].submit(); // submit the form
			}
		});


	}



	return {
		//main function to initiate the module
		init: function () {
			handleValidation1();
			handleValidation2();
		}

	};

}();

jQuery(document).ready(function() {
	FormValidation.init();
});