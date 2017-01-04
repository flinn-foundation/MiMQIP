$(document).ready(function(){
	patSearch.init();	
});

var patSearch = {
	pageSelected: 1,
	numPerPage: 20,
	paginationMaxNum: 7,
	init: function(){
		utils.setEventsForDropdown($("#yyyyDropdown"));
		utils.setEventsForDropdown($("#ddDropdown"));
		utils.setEventsForDropdown($("#mmDropdown"));
		utils.setEventsForDropdown($("#sexDropdown"));	
		utils.setEventsForDropdown($("#raceDropdown"));
		$("#patientSearchForm").submit(function(e){
			e.preventDefault();
			patSearch.searchPatients(1, patSearch.numPerPage);
		});	
		patSearch.searchPatients(patSearch.pageSelected, patSearch.numPerPage);			
	},
	searchPatients: function(pageOn, numPerPage){
		var pIdentifier, fName, lName, sex, dob, sexVal;
		pIdentifier = fName = lName = sex = dob = sexVal = "";
		var pIdentifierVal = $("#patientId").val();
		var fNameVal = $("#fName").val();
		var lNameVal = $("#lName").val();
		var sexVal = $("#sexDropdown .pullDown").text();
		var mmVal = $("#mmDropdown .pullDown").text();
		var ddVal = $("#ddDropdown .pullDown").text();
		var yyyyVal = $("#yyyyDropdown .pullDown").text();
		
		if(pIdentifierVal){pIdentifier = pIdentifierVal;}
		if(fNameVal){fName = fNameVal;}
		if(lNameVal){lName = lNameVal;}
		if(sexVal != "Select"){
			if(sexVal == "Male") sex = "M";
			else if(sexVal == "Female") sex = "F";
		}
		if(mmVal != "MM" && ddVal != "DD" && yyyyVal != "YYYY"){dob = yyyyVal + "-" + utils.pad2(mmVal) + "-" + utils.pad2(ddVal);}
		
		var actionObj = utils.buildActionObj("read", "patientsearch");
		var patientSearchObj = patSearch.buildPatientSearchObj(pIdentifier,fName,lName,sex,dob,pageOn,numPerPage);
		utils.callWebservice('{"action":' + actionObj + ',"patientsearch":' + patientSearchObj + '}' , "patientSearch");
		utils.searchPatientsComplete = true;
	},	
	buildPatientSearchObj: function(patientidentifier, firstName, lastName, sex, birth, page, pagecount){
		var patientSearch = '{';
		if(patientidentifier != ""){patientSearch += '"patientidentifier":"'+patientidentifier+'",';}
		if(firstName != ""){patientSearch += '"firstname":"'+firstName+'",';}
		if(lastName != ""){patientSearch += '"lastname":"'+lastName+'",';}
		if(sex != ""){patientSearch += '"sex":"'+sex+'",';}
		if(birth != ""){patientSearch += '"birth":"'+birth+'",';}
		patientSearch += '"page":"'+page+'","pagecount":"'+pagecount+'"}';
		return patientSearch;
	},	
	buildSearchResults: function(returnObj){
		var tbodyHTML = "";
		var searchResultLength = returnObj.patients.length;
		if(searchResultLength < 1){
			$("#patientTable table").hide();
			$(".pagination").html("");
			$("#noSearchResults").show();
		}
		else{
			$("#noSearchResults").hide();
			$("#patientTable table").show();
			for(var i=0; i<searchResultLength; i++){
				var patientidentifier = "";
				if(returnObj.patients[i].details.patientidentifier){patientidentifier = returnObj.patients[i].details.patientidentifier;}
				var birthdate = returnObj.patients[i].details.birth.split("-");
				birthdate = birthdate[1] + "/" + birthdate[2] + "/" + birthdate[0];
				var oddClass = ''
				if(i % 2 == 0){oddClass = ' class="odd"';}
				tbodyHTML += '<tr'+oddClass+' rel="'+returnObj.patients[i].patientid+'">';
				tbodyHTML += '<td>' + returnObj.patients[i].details.lastname + '</td>';
				tbodyHTML += '<td>' + returnObj.patients[i].details.firstname + '</td>';
				tbodyHTML += '<td>' + patientidentifier + '</td>';
				tbodyHTML += '<td>' + birthdate + '</td>';
				tbodyHTML += '<td>' + returnObj.patients[i].details.sex.toUpperCase() + '</td>';
				if (window.location.href.indexOf("enlighten.com") > 0) {
					tbodyHTML += '<td class="timelapse" rel="'+returnObj.patients[i].patientid+'" relln="'+returnObj.patients[i].details.lastname+'" relfn="'+returnObj.patients[i].details.firstname+'"><strong>Time&nbsp;Lapse</a></strong></td>';
				}
				tbodyHTML += '</tr>'
			}
			$("#patientTable tbody").html(tbodyHTML);
			utils.buildPagination(returnObj.page, returnObj.total, patSearch.numPerPage, patSearch.paginationMaxNum, $(".pagination"));
			$("#patientSelect tbody tr").hover(
				function(){
					$(this).addClass("hover");
				}, 
				function(){
					$(this).removeClass("hover");
				}
			);
			$("#patientSelect tbody tr").click(function(){
				window.location.href = "app.jsp?id=" + $(this).attr("rel"); 
			});
			$("#patientSelect tbody tr td.timelapse").click(function(){
				// alert("Hi - " + $(this).attr("rel") + " - " + $(this).attr("relfn") + " - " + $(this).attr("relln"));
				patSearch.trainAgeOpen($(this).attr("rel"),$(this).attr("relfn"),$(this).attr("relln"));
				return false;
			});
			patSearch.setPaginationEvents();
		}
	},
	setPaginationEvents: function(){
		$(".pagination a").click(function(e){
			e.preventDefault();
			if($(this).hasClass("next")){
				patSearch.pageSelected = (patSearch.pageSelected * 1) + 1;
			}
			else if($(this).hasClass("prev")){
				patSearch.pageSelected = (patSearch.pageSelected * 1) - 1;
			}
			else{
				var thePage = $(this).text();
				patSearch.pageSelected = thePage;
			}
			patSearch.searchPatients(patSearch.pageSelected, patSearch.numPerPage);
		});
	},	
	trainWebservice: function(JSONobj, submitType){
		$.ajax({
			type: "POST",
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			url: "/MQIPTrain?format=json",
			data: JSONobj,
			processData: true,
			cache: false,
			complete: function(xhr,textstatus) { },
			error: function(xhr,textstatus) { patSearch[submitType + "Error"](xhr, textstatus);},
			success: function(msg,textstatus) {
				$("#ajaxLoader").hide();
				if(msg.error){
					if(msg.error.number == -1){
						$.cookie("authcode", null);
						utils.setSession = utils.searchPatientsComplete = utils.loggedIn = utils.getInitialData = utils.getLabData = utils.getNoteData = utils.getEvalData = utils.getMedsData = utils.getRecoData = false;
						if(utils.loginWindowOpen == false){
							utils.openColorbox("/inc/loginForm.jsp?facilityid=", "login", false);
						}
					}
					patSearch[submitType + "Success"](msg, textstatus);
				}
				else{
					patSearch[submitType + "Success"](msg, textstatus);

					// if haven't gotten session yet, get session.  this applies for all pages
					if(!utils.setSession){utils.getSessionInfo();}
				}
			}
		});
	},
	trainAgeSuccess: function(returnObj, textstatus) {
		if (returnObj.error && returnObj.error.number != -1) {
			alert(returnObj.error.message);
		}
	},
	trainAgeError: function(returnObj, textstatus) {
		alert("Patient Data Aging Error");
	},
	trainAgeOpen: function(patientid, fname, lname) {
		patSearch.patientid = patientid;
		utils.openColorbox("/inc/age.jsp","trainingAge", false, 700);
	},
	setEventsForAge: function() {
		$("a.trainAge").click(function(e){
			e.preventDefault();
			var days = utils.getHref($(this));
			if (days != "cancel") {
				var patientid = patSearch.patientid;
				var actionObj = utils.buildActionObj("update", "age");
				patSearch.trainWebservice('{"action":' + actionObj + ',"patient":{"patientid":"'+patientid+'","agingfactor":"'+days+'"}}', "trainAge");
			}
			utils.closeLightbox();
		});
	}

}

