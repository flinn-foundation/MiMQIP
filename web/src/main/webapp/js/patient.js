$(document).ready(function(){
	utils.getSessionInfo();
	patient.init();	
	$("#newPatientForm").submit(function(e){
		e.preventDefault();
		patient.submitNewPatient();
	});	
});

		