$(document).ready(function(){
	$("#logoutBtn").click(function(e){
		e.preventDefault();
		utils.logOut();
	});

	$("#drFirstMessageBtn").click(function(e){
		e.preventDefault();
		utils.openColorbox("/inc/drFirstMessage.jsp","drFirstMessage", false, 900, 700, true);
	});

	$("#drFirstReportBtn").click(function(e){
		e.preventDefault();
		utils.openColorbox("/inc/drFirstReport.jsp","drFirstReport", false, 900, 700, true);
	});

});

var utils = {
	lightboxOpen: false,
	sessionObj: null,
	setSession: false,
	loginWindowOpen:false, 
	popupWindow:null,
	searchPatientsComplete: false,
	getInitialData: false,
	initialDiagnosis: false,
	getLabData: false,
	getNoteData: false,
	getMedsData: false,
	getEvalData: false,
	getRecoData: false,
	loggedIn: false,
	loadEvals: false,
	callWebservice: function(JSONobj, submitType){
		$.ajax({
			type: "POST",
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			url: "/MQIP?format=json",
			data: JSONobj, 
			processData: true,
			cache: false,
			complete: function(xhr,textstatus) { },
			error: function(xhr,textstatus) { utils[submitType + "Error"](xhr, textstatus);},
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
					utils[submitType + "Success"](msg, textstatus);
				}
				else{
					utils[submitType + "Success"](msg, textstatus);
					
					// if haven't gotten session yet, get session.  this applies for all pages
					if(!utils.setSession){utils.getSessionInfo();}
										
					// if on app page, get patient info & latest progress note for upper panel; get tags for create note
					if(typeof(app) != "undefined" &&  app.pID != ""){
						if(!utils.getInitialData){
							app.getPatientInfo(true, "patientShortForm");
							app.getTags();
							app.getProgressNotes(1,1,"","","getLatestProgressNote");
							app.getCurrentMeds();
							utils.getInitialData = true;
							app.getLabTestTypes();
						}						
						switch(app.tabIndex){
							case 0:
								if(!utils.getEvalData){
									var theHref = utils.getHref($("#evalNav li").eq(app.evalSelected + 1).find("a"));				
									var thePrefix = theHref.replace("Past","").toLowerCase() + "_";
									if(theHref.indexOf("Past") >= 0){
										app.getPatientInfo(false,"patientEval",thePrefix);
										utils.getEvalData = true;
									}
								}
								break;
							case 1:
								if(!utils.getMedsData){
									app.getMedicationData();
									utils.getMedsData = true;
								}
								break;
							case 2:
								if(!utils.getRecoData){
									app.getRecommendations();
									utils.getRecoData = true;
								}
								break;
							case 3: 
								if(!utils.getNoteData){
									app.getProgressNotes(app.notePageSelected,app.notesPerPage,app.tagSelected,app.searchText,"getProgressNotes"); 
									app.getProgressNotes(1,1,"","","getLatestProgressNote");
									utils.getNoteData = true;
								}
								break;
							case 4: 
								if(!utils.getLabData){
									app.getPatientLabs(); 
									utils.getLabData = true;
								}
								break;							
						}
					}
					// if on patient search page, get patient list
					if(typeof(patSearch) != "undefined" && !utils.searchPatientsComplete){
						patSearch.searchPatients(patSearch.pageSelected, patSearch.numPerPage); 
					}
				}					
			}
		});
	},
	patientSearchSuccess: function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
			utils.searchPatientsComplete = false;
		}
		else if(returnObj.patients){
			patSearch.buildSearchResults(returnObj);			
		}
	},
	patientSearchError: function(returnObj, textstatus){
		alert("patientSearchError");
	},
	patientShortFormSuccess: function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.patient){
			app.patientObj = returnObj.patient;
			app.populatePatientInfo(returnObj.patient);
			app.populateEditPatientInfo(app.patientObj);
			app.populateDisplayPatientInfo(returnObj.patient);				
			
			if(returnObj.patient.status){
				app.populateDiagnosisStage(returnObj.patient.status);			
				if(!utils.loadEvals){
					app.loadEvals(returnObj.patient.status);
					app.setScaleNameNav();	
				}
				else{
					app.populateSubstanceAbuse(returnObj.patient.status);
					app.populateVitalSigns(returnObj.patient.status);
				}
			}
			else{
				utils.initialDiagnosis = true;  
				if(!utils.lightboxOpen){
					utils.openColorbox("/inc/initial_diagnosis_stage.jsp","initialDiagnosisStage", false, 700);
				}	
			}
		}
	},
	patientShortFormError: function(returnObj, textstatus){
		alert("patientShortFormError");
	},
	createPatientSuccess: function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.patient){
			utils.openNewWindow("app.jsp?id=" + returnObj.patient.patientid + "/#evaluations");
		}
	},
	createPatientError: function(returnObj, textstatus){
		alert("createPatientError");
	},
	updatePatientSuccess: function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.patient){
			app.getPatientInfo(true, "patientShortForm");
			$("#displayPatientInfo").show();
			$("#editPatientInfo").removeClass("open");				
		}
	},
	updatePatientError: function(returnObj, textstatus){
		alert("updatePatientError");
	},
	sessionSuccess: function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.user){
			utils.sessionObj = returnObj;
			utils.setSession = true;
			$.cookie("facilityID", returnObj.facility.facilityid, { path: '/' });
			$.cookie("username", returnObj.user.login, { path: '/' });
			
			var facilityName = returnObj.facility.facilityname.replace(/\s/g, "&nbsp;");
			$("#utilityBar h1").html(facilityName);
			var utilityBarHeaderWidth = $("#utilityBar h1").width();
			$("#utilityBar h1").css({"width":utilityBarHeaderWidth + "px", "float":"left"});
			var userFullName = returnObj.user.settings.FullName.replace(/ /g, "&nbsp;");
			$("#utilityBar .welcome").html("Welcome,&nbsp;" + userFullName);
			var spanWidth = $("#utilityBar .welcome").width();
			$("#utilityBar .welcome").css({"width":spanWidth + "px", "float":"left"});
			var userRole = returnObj.user.roles[0].approle.toLowerCase();
			if(userRole.indexOf("admin") >= 0){$("a#adminBtn").show();}
			else{$("a#adminBtn").hide();}
		}
	},
	sessionError: function(returnObj, textstatus){
		alert("sessionError");
	},
	loginSuccess: function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			$("#loginErrorMsg").text(returnObj.error.message);
		}
		else if(returnObj.authenticate){
			if(returnObj.authenticate.message == "Authentication Successful"){				
				utils.loggedIn = true;
				if(utils.lightboxOpen){
					utils.loginWindowOpen = false;
					utils.closeLightbox();
				}
				else{
					$(".login").hide();
					$("#openPopup").addClass("show");					
					utils.openNewWindow("/patient-search.jsp");
				}
				$.cookie("authcode", returnObj.authenticate.authcode, { path: '/' });
			}
		}
	},
	loginError: function(returnObj, textstatus){
		if(returnObj.error){
			alert(returnObj.error.message);
		}
	},
	getTagsSuccess: function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.tags){
			var tagsArray = [];
			for(var i=0; i<returnObj.tags.length; i++){
				var innerTagsArray = [];
				innerTagsArray.push(returnObj.tags[i].progressnotetag);
				innerTagsArray.push(returnObj.tags[i].progressnotetagid);
				tagsArray.push(innerTagsArray);
			}
			app.buildTagLists(tagsArray);
		}
	},
	getTagsError: function(returnObj, textstatus){
		alert("getTagsError");
	},
	getProgressNotesSuccess:function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.notes){
			if(returnObj.notes.length == 0){ 
				$("#progress").addClass("noNotes");
				if(app.searchText != "" || app.tagSelected != ""){
					if($("#noNotesWritten:visible").length != 1){
						$("#noNotesSearch").show();
						$("#searchContent").hide();
					}
				}
				else{
					$("#noNotesWritten").show();
					$("#searchContent").hide();
				}
			}
			else{
				$("#noNotesSearch, #noNotesWritten").hide();
				$("#searchContent").show();
				$("#tagDropdown").removeClass("disabled");
				$("#keywordSearchForm input").prop("disabled", false);
				$("#progress").removeClass("noNotes");
				app.buildProgressNotesInTab(returnObj);
			}
		}
	},
	getProgressNotesError: function(returnObj, textstatus){
		alert("getProgressNotesError");
	},
	getLatestProgressNoteSuccess:function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.notes){
			if(returnObj.notes.length == 0){
				$("#boxNotes .note").html('<p class="paddingTop">No progress notes have been written for this patient.</p><p>Click the Create Note button in the upper right to create a note.</p>');
				$(".noteInfoBar").hide();
			}
			else{
				$(".noteInfoBar").show();
				app.buildLatestProgressNoteBox(returnObj);
			}
		}
	},
	getLatestProgressNoteError:function(returnObj, textstatus){
		alert("getLatestProgressNoteError");
	},	
	createProgressNoteSuccess:function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.progressnote){
			app.getProgressNotes(1,1,"","","getLatestProgressNote");
			if(app.tabIndex == 3){
				app.getProgressNotes(app.notePageSelected,app.notesPerPage,app.tagSelected,app.searchText,"getProgressNotes");
			}
			$("#cancelNote").click();
		}
	},
	createProgressNoteError: function(returnObj, textstatus){
		alert("createProgressNoteError");
	},	
	logoutSuccess: function(returnObj, textstatus){		
		if(utils.popupWindow != null){
			utils.popupWindow.close();
		}
		else{window.close();}
		if(window.opener != null){
			window.opener.location.reload(true);
		}
		else{window.location = "/";}
	},
	logoutError: function(returnObj, textstatus){
		alert("getProgressNotesError");
	},
	getLabTestTypesSuccess: function(returnObj, textstatus){		
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.labtests){
			app.buildLabTestTypes(returnObj);
		}
	},
	getLabTestTypesError: function(returnObj, textstatus){
		alert("getLabTestTypesError");
	},
	enterLabTestSuccess: function(returnObj, textstatus){		
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.lab){
			$("#savedLab").addClass("show");
			$("#labFields").removeClass("show");
		}
	},
	enterLabTestError: function(returnObj, textstatus){
		alert("enterLabTestError");
	},
	getLabResultsSuccess: function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.labs){
			if(returnObj.labs.length == 0){
				if(app.labTestSelected != ""){
					if($("#noLabsWritten:visible").length != 1){
						$("#noLabsSearch").show();
						$("#labs table").hide();
					}
				}
				else{
					$("#noLabsWritten").show();
					$("#labs table").hide();
				}
			}
			else{
				$("#noLabsSearch, #noLabsWritten").hide();
				$("#labTestsDropdown").removeClass("disabled");
				$("#labs table").show();
				app.buildLabResultsInTab(returnObj);
			}			
		}
	},
	getLabResultsError: function(returnObj, textstatus){
		alert("getLabResultsError");
	},
	updatePatientEvaluationSuccess: function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.patient){
			$(".eval").eq(app.evalSelected).find('.symptom input[type="text"]').val("");
			$(".eval").eq(app.evalSelected).find('.symptom textarea').val("Enter text...");
			$(".eval").eq(app.evalSelected).find(".symptom input").prop({"checked":false, "disabled": false});
			$(".eval").eq(app.evalSelected).find(".fieldWrapper .inputVal").text("");
			$evalTabs.tabs("select", (app.evalSelected +1));
			$("#evalNav li").eq(app.evalSelected + 2).show();
			$("#evaluations").scrollTop(0);
		}
	},
	updatePatientEvaluationError: function(returnObj, textstatus){
		alert("updatePatientEvaluationError");
	},	
	updateDiagnosisSuccess: function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.patient){			
			if($(".eval:not('.ui-tabs-hide')").attr("id").indexOf("PastCRS") != -1){$evalTabs.tabs("select", 0);	}
			var oldDiagnosis = app.diagnosis;
			app.populateDiagnosisStage(returnObj.patient.status);
			if(app.diagnosis != null && oldDiagnosis != app.diagnosis ){
				app.setScaleNameNav();	
				app.updateCRS(returnObj.patient.status);
			}
			if(!utils.loadEvals){ // if first time creating diagnosis
				app.loadEvals(returnObj.patient.status);
			}
			utils.closeLightbox();	
		}
	},
	updateDiagnosisError: function(returnObj, textstatus){
		alert("updatePatientEvaluationError");
	},	
	patientEvalSuccess: function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.patient){			
			var theID = $("#evalContent .eval").eq(app.evalSelected).attr("id");	
			var status = false;			
			var newObj = app.reSortObj(returnObj.patient.status);
			for(var props in returnObj.patient.status){	status = true;}
			
			if(status){
				$('#evalNav li a[href="#' + theID + '"]').parent().show();	
				var evalName, CRS;		
				for(var props in returnObj.patient.status){
					if(props.indexOf("crs") >= 0){
						evalName = "CRS";
						if(props.indexOf("crs_bipolar_") >= 0){CRS = "bipolar";}
						if(props.indexOf("crs_schizophrenia_") >= 0){CRS = "schizophrenia";}
						if(props.indexOf("crs_depression_") >= 0){CRS = "depression";}
					}	
					if(props.indexOf("evaluation") >= 0){evalName = "PsychiatricEvaluation";}
					if(props.indexOf("mental_status") >= 0){evalName = "MentalStatus";}		
					if(props.indexOf("vital_signs") >= 0){evalName = "VitalSigns";}						
					if(props.indexOf("diagnosis") >= 0){evalName = "DiagnosisStage";}		
					if(props.indexOf("global_rating_scale") >= 0){evalName = "GlobalRatingScales";}	
					if(props.indexOf("substance_abuse") >= 0){evalName = "SubstanceAbuse";}					
				}
				$("#" + theID + " .content").show();
				app["buildPast" + evalName](newObj, 0, theID, CRS);
			}
		}
	},
	patientEvalError: function(returnObj, textstatus){
		alert("patientShortFormError");
	},
	initialStagingSuccess: function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.initialstage){		
			app.setDropdownValAndSelected("stageSelectedDropdown", returnObj.initialstage.stage);
			app.setDropdownValAndSelected("stageDropdown", returnObj.initialstage.stage);	
			$(".initialDiagnosisStage .show").removeClass("show");
			$(".diagnosisStageContainer").eq(2).addClass("show");			
		}
	},
	initialStagingError: function(returnObj, textstatus){
		alert("initialStagingError");
	},
	getCurrentMedsSuccess: function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.prescriptions){
			app.populateCurrentMeds(returnObj.prescriptions);
		}
	},
	getCurrentMedsError: function(returnObj, textstatus){
		alert("getCurrentMedsError");
	},
	discontinueMedsSuccess: function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else { // change lightbox
			utils.closeLightbox();
			app.getCurrentMeds();
			app.getMedicationData(); 			
		}
	},
	discontinueMedsError: function(returnObj, textstatus){
		alert("discontinueMedsError");
	},
	getMedicationDataSuccess:  function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.prescriptions){
			var hasPescriptionProps = false;
			for(var props in returnObj.prescriptions){hasPescriptionProps = true;}
			if(hasPescriptionProps){
				$("#ajaxLoader").show();
				$("#noMedications").hide();
				$("#discontinue").removeClass("disabled");
				$("#graphWrapper").show();
				
				var prefixes = app.buildPrefixes();
				var patientObj = utils.buildPatientObj(false,app.pID,"",prefixes);
				var actionObj = utils.buildActionObj("read", "patient");
				var jsonObj = '{"action":' + actionObj + ',"patient":' + patientObj + '}';				
				$.ajax({
					type: "POST",
					contentType: "application/json; charset=utf-8",
					dataType: "json",
					url: "/MQIP?format=json",
					data: jsonObj, 
					processData: true,
					cache: false,
					success: function(returnObj2,textstatus) {	
						$("#ajaxLoader").hide();
						var hasStatusProps = false;
						for(var props in returnObj2.patient.status){hasStatusProps = true;}
						if(hasStatusProps){$("#chartWrapper").show();}
						else{$("#chartWrapper").hide();}
						app.buildMedicationsTable(returnObj2.patient.status, returnObj.prescriptions);
					}
				});				
			}
			else{ // do something different if there are no meds
				$("#noMedications").show();
				$("#discontinue").addClass("disabled");
				$("#graphWrapper,#chartWrapper").hide();
			}
		}
	},
	getMedicationDataError: function(returnObj, textstatus){
		alert("getMedicationDataError");
	},
	getRecommendationsSuccess: function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else if(returnObj.recommendation){ 	
			app.populateRecos(returnObj.recommendation);
		}
	},
	getRecommendationsError: function(returnObj, textstatus){
		alert("getRecommendationsError");
	},
	guidelineReasonSuccess: function(returnObj, textstatus){
		if(returnObj.error && returnObj.error.number != -1){
			alert(returnObj.error.message);
		}
		else{
			utils.closeLightbox();
		}
	},
	guidelineReasonError: function(returnObj, textstatus){
		alert("guidelineReasonError");
	},
	// end success/error functions
	// start global functions
	buildActionObj: function(typeVal, commandVal){
		var authcode = $.cookie("authcode");
		if(authcode == ""){authcode = "";}
		var time = new Date().getTime();
		var actionObj = '{"type":"' + typeVal + '","command":"' + commandVal + '","authcode":"' + authcode + '","messageid":"1","correlationid":"1","interfaceversion":"1.0","clientidentifier":"MiMQIP","timestamp":' + time + '}';
		return actionObj;
	},
	buildLoginObj: function(loginName, password, facilityID, terms){
		var time = new Date().getTime();
		var actionObj = utils.buildActionObj("login", "authenticate");
		var loginObj = '{"action":' + actionObj + ',"authenticate":{"facilityid":"' + facilityID + '","login":"' + loginName + '","password":"' + password + '","terms":"' + terms + '"}}';
		return loginObj;
	},
	buildPatientObj: function(shortform,patientID,detailObj,statusPrefix){
		if(detailObj == ""){detailObj = "{}";}
		else{detailObj = "{" + detailObj + "}"}
		var patient = '{"shortform":"' + shortform + '",';
		if(patientID != ""){patient += '"patientid":"' + patientID + '",';}		
		patient += '"valid":"true",';
		if(statusPrefix){patient += '"statusprefix":"' + statusPrefix + '",';}
		patient += '"details":' + detailObj + '';
		patient += '}';
		return patient;
	},	
	logOut: function(){
		var actionObj = utils.buildActionObj("delete", "session");
		utils.callWebservice('{"action":' + actionObj + '}' , "logout");
		$.cookie("authcode", null);
		$.cookie("username", null);
	},	
	getSessionInfo: function(){
		var actionObj = utils.buildActionObj("read", "session");
		utils.callWebservice('{"action":' + actionObj + '}' , "session");
	},
	buildPagination: function(pageOn, pageTotal, numPerPage, paginationMaxNum, elementToAppendPagination, functionToCall){
		var numOfPages = Math.ceil(pageTotal / numPerPage);
		var paginationHTML = "";
		var loopStart = 1;
		var loopEnd = numOfPages + 1;
		if(pageOn < 4 && numOfPages > paginationMaxNum){loopEnd = 8;}
		if(numOfPages > paginationMaxNum){
			if(((pageOn * 1) - 3) > 1){loopStart = (pageOn * 1) - 3;}
			if(((pageOn * 1) + 3) < numOfPages && pageOn > 3){loopEnd = (pageOn * 1) + 4;}
			var totalLoop = (loopEnd) - loopStart;
			if(totalLoop < paginationMaxNum){
				var numAtEnd = (loopEnd - 1) - pageOn;
				var numAtBeg = pageOn - loopStart;
				loopStart = loopStart - (numAtBeg - numAtEnd);
			}
		}
		for(var i=loopStart; i<loopEnd; i++){
			paginationHTML += '<a href="#"';
			if(pageOn == i){paginationHTML += ' class="selected"';}
			paginationHTML += '>' + i + '</a>';
		}
		var nextNavHTML = '<a href="#" class="nav next">></a>';
		var prevNavHTML = '<a href="#" class="nav prev"><</a>'
		if(pageOn == 1 && numOfPages > 1){ paginationHTML = paginationHTML + nextNavHTML;  }
		else if(pageOn == numOfPages && pageOn != 1){paginationHTML = prevNavHTML + paginationHTML;}
		else if(pageOn != numOfPages && pageOn > 1){paginationHTML = prevNavHTML + paginationHTML + nextNavHTML;}
		if(numOfPages == 1){$(elementToAppendPagination).hide();$(elementToAppendPagination).parents("div").addClass("noPagination");}
		else{$(elementToAppendPagination).show().html(paginationHTML).parents("div").removeClass("noPagination");}
	},
	buildTagsForNote:function(tagObj){
		var tags = "";
		for(var i=0; i<tagObj.length; i++){
			tags += tagObj[i].progressnotetag + ", ";
		}
		tags = tags.substring(0, tags.length - 2);
		return tags;
	},
	convertMilitaryTime: function(militaryTime){
		var firstTimePart = militaryTime[0] * 1;
		var secondTimePart = militaryTime[1];
		var dayPart = "am";
		if(firstTimePart > 12){
			dayPart = "pm";
			firstTimePart = firstTimePart - 12;
		}
		
		var time = firstTimePart + ":" + secondTimePart + dayPart;		
		return time;
	},
	openColorbox: function(cboxHref, pageType, closeBool, boxWidth, boxHeight, iframeBool){
		if(pageType == "login"){
			var facilityid = "";
			if(utils.sessionObj != null){facilityid = utils.sessionObj.user.facilityid;}
			else{facilityid = $.cookie("facilityID");}
			cboxHref = cboxHref + facilityid;
		}
		$.colorbox({
			href: cboxHref,
			opacity:.6,
			transition:"none",
			overlayClose: closeBool,
			escKey: closeBool,
			closeButton: closeBool,
			width: boxWidth,
			height:boxHeight,
			iframe: iframeBool,
			onComplete: function(){ 
				utils.lightboxOpen = true;
				if(pageType == "login"){					
					utils.loginWindowOpen = true;
					utils.setLoginEvents();
				}
				if(pageType == "enterLabs"){
					app.setEnterLabsEvents();					
				}
				if(pageType == "diagnosisStage"){
					app.setEventsForDiagnosisStage();
				}
				if(pageType == "initialDiagnosisStage"){
					utils.initialDiagnosis = false;
					app.setEventsForInitialDiagnosisStage();
				}
				if(pageType == "discontinueMeds"){
					app.setEventsForDiscontinueMeds();
				}
				if(pageType == "nonConsistency"){
					app.setEventsForNonConsistency();
				}
				if(pageType == "trainingAge"){
					patSearch.setEventsForAge();
				}
			},
			onClosed: function(){				
				if(pageType == "enterLabs"){
					app.getPatientLabs();
				}
				if(utils.initialDiagnosis){
					utils.openColorbox("/inc/initial_diagnosis_stage.jsp","initialDiagnosisStage", false, 700);
				}
				if(pageType == "nonConsistency" || "drFirst"){
					app.getCurrentMeds();
					app.getMedicationData(); 
					utils.getMedsData = true;
				}
			}
		});
	},
	checkForError: function(element, value, regExTest){
		if(value == "" || regExTest == false){
			$(element).addClass("error").parents(".fieldWrapper").addClass("error");
			if($(".ie7, .ie8").length > 0){$(element).css("visibility", "hidden").css("visibility", "visible");}
			if ($(element).hasClass("inputList")) {
				$(element).children('li:first input').focus();	
			}
			else {
				$(element).focus();	
			}				
			return false;
		}
	},
	setLoginEvents: function(){
		var userName = $.cookie("username");
		if(utils.sessionObj != null){
			userName = utils.sessionObj.user.login;
		}
		if(utils.lightboxOpen){
			$("#uName").hide().val(userName);
			$(".uName").text(userName);
			$("#newUser").css("visibility","visible");
			setTimeout(function(){ $("#loginFormScreen #passwrd").blur().focus(); }, 10);
		}
		
		$("#newUser").click(function(e){
			e.preventDefault();
			utils.logOut();
		});
		$("#closeWindow").click(function(e){
			e.preventDefault();
			if(utils.popupWindow != null){
				utils.popupWindow.close();
			}
			else{window.close();}
		});
		$("#loginForm").submit(function(e){
			e.preventDefault();
			var uName = $("#uName").val();
			var password = $("#passwrd").val();
			var facilityID = $("#facilityID").val();
			var terms = $("#terms").val();
			if (!($("#terms").is(":checked"))) {terms = false;}
			$(".error").removeClass("error");
			if (!terms) {$("#loginErrorMsg").text("Please read and confirm the Terms and Conditions.");}
			if((password == "") || (uName == "")){$("#loginErrorMsg").text("Please enter a username and password.");}

			utils.checkForError($("#passwrd"), password);
			utils.checkForError($("#uName"), uName);

			if(uName != "" && password != "" && terms){
				var loginObj = utils.buildLoginObj(uName,password,facilityID,terms);
				utils.callWebservice(loginObj, "login");
			}
		});	
		$("#loginForm #uName").blur().focus();
	},
	openNewWindow: function(url){
		if (utils.popupWindow != null && !utils.popupWindow.closed){
			utils.popupWindow.focus();
		} else {
			utils.popupWindow = window.open(url,'mywindow','toolbar=no,location=no,menubar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		}
	},
	roundNumber: function(num, dec) {
        var result = Math.round(num * Math.pow(10, dec)) / Math.pow(10, dec);
        return result;
    },
	pad2: function(number) {   
		return (number < 10 ? '0' : '') + number;
	},
	getHref:function(anchor){
		var theHref = $(anchor).attr("href").substring($(anchor).attr("href").indexOf("#") + 1, $(anchor).attr("href").length);
		return theHref;
	},
	setEventsForDropdown: function(openDiv){
		$(openDiv).unbind().click(function(e) {
			e.preventDefault();	
			e.stopPropagation();
			var theID = $(openDiv).attr("id");
			utils.closeDropdown($(".dropdown.open:not('#" + theID + "')")); // close any exisiting open dropdowns
			if ($(this).hasClass("open") == false && $(this).hasClass("disabled") == false) { 
				var index = $(this).find("a.selected").parent().index();
				var topOffset = 25 * index;				
				$(this).find("ul").removeClass("showTop");
				var offsetTop = $(this).find("ul").offset().top;
				var tabOffsetTop = 0;
				if($("#instructionsWrapper").length > 0){
					if($(this).parents(".tabBox").length == 0){tabOffsetTop = $(this).parents("#cboxContent").offset().top; }
					else{tabOffsetTop = $(this).parents(".tabBox").offset().top;}		
				}
				var ddHeight = $(this).find("ul").height() * 1 + 6;
				var windowHeight = $(window).height() * 1;				
				if(((windowHeight - offsetTop) < ddHeight) && ((offsetTop - tabOffsetTop) > ddHeight)){
					$(this).find("ul").addClass("showTop");
				}				
				$(this).find("ul").scrollTop(topOffset);
				$(this).find("ul").css({"visibility":"visible"});
				$(this).addClass("open");
				$("body, #tabs").click(function(e) {
					$("body, #tabs").unbind();
					utils.closeDropdown(openDiv);						
				});
			}
			else{
				utils.closeDropdown(openDiv);
				$("body, #tabs").unbind();			
			}
		});	
		utils.assignDropdownLinkEvents();	
	}, 	
	assignDropdownLinkEvents: function(){
		$(".dropdown li a").click(function(e) {	
			e.preventDefault();
			$(this).parents(".dropdown").find(".selected").removeClass("selected");
			$(this).addClass("selected");
			var selectedText =  $(this).text();
			var theVal = $(this).attr("href");
			$(this).parents(".dropdown").find(".pullDown").attr("href", theVal).text(selectedText);
		});
	},
	closeDropdown: function(openDiv) {
		if ($(openDiv).hasClass("open")) {
			$(openDiv).children("ul").css({"visibility":"hidden"});
			$(openDiv).removeClass("open");						
			$(openDiv).find("a").blur();
		}
	},
	encode: function(str, allowHtml, allowNewLine){
		if (typeof allowHtml == "undefined") {var allowHtml = false;}
		if (typeof allowNewLine == "undefined") {var allowNewLine = false;}
		str = str.replace( /\u2018|\u2019|\u201A|\uFFFD/g, "'" );
		str = str.replace( /\u201c|\u201d|\u201e/g, '"' );
		str = str.replace( /\u02C6/g, '^' );
		str = str.replace( /\u2039/g, '<' );
		str = str.replace( /\u203A/g, '>' );
		str = str.replace( /\u2013/g, '-' );
		str = str.replace( /\u2014/g, '--' );
		str = str.replace( /\u2026/g, '...' );
		str = str.replace( /\u00BC/g, '1/4' );
		str = str.replace( /\u00BD/g, '1/2' );
	        str = str.replace( /\u00BE/g, '3/4' );
		str = str.replace(/[\u02DC|\u00A0]/g, " ");
		if (allowNewLine == false) {
			str = str.replace(/\r/g, "");
			str = str.replace(/\n/g, "<br>");
		} else {
			str = str.replace(/\r/g, "\\r");
			str = str.replace(/\n/g, "\\n");
		}

		var aStr = str.split(''),
        
        aRet = [];
		for(var i=0; i<aStr.length; i++){
			var iC = aStr[i].charCodeAt();
			//if ((iC < 65 && (iC != 32 && iC != 10 && iC != 46)) || iC > 127 || (iC>90 && iC<97)) {
			if (iC == 34 || iC == 39 || (allowHtml==false&&(iC == 60 || iC == 62)) || iC == 64 || iC > 127 || (iC>90 && iC<97)) {
				aRet.push('&#'+iC+';');
			}
			else {
				aRet.push(aStr[i]);
			}
			
		}
		var returnString = aRet.join('');
		if (allowNewLine==true) { returnString = returnString.replace(/&#92;n/g,'\\n'); }
		return returnString;
	},
	
	decode: function(value){		
		return $('<div/>').html(value).text();
	},
	closeLightbox: function(){
		$.colorbox.close();
		utils.lightboxOpen = false;
	},
	checkDate: function(theMonth,theDay){
		var returnVal = true;
		if ((theMonth == 4 || theMonth == 6 || theMonth == 9 || theMonth == 11) && theDay > 30)
			returnVal = false;
		if (theMonth == 2 && theDay > 29)
			returnVal = false;
		return returnVal;
	}
}

var patient = {
	fNameVal: "",
	lNameVal: "",
	pIdentifierVal: "",
	zipVal: "",
	sexVal: "",
	mmVal: "",
	ddVal: "",
	yyyyVal: "",	
	raceVal: "",
	ethnicityVal: "",
	maritalVal: "",
	employmentVal: "",
	livingVal: "",
	physicianNameVal: "",
	physicianEmailVal: "",
	init: function(){
		utils.setEventsForDropdown($("#yyyyDropdown"));
		utils.setEventsForDropdown($("#ddDropdown"));
		utils.setEventsForDropdown($("#mmDropdown"));
		utils.setEventsForDropdown($("#sexDropdown"));	
		utils.setEventsForDropdown($("#maritalDropdown"));
		utils.setEventsForDropdown($("#employmentDropdown"));	
		utils.setEventsForDropdown($("#livingDropdown"));	
		$("#raceList input").click(function(){
			if($(this).prop("id")!="raceNone" && $(this).prop("checked")){				
				$("#raceNone").prop({"checked": false});
			}			
		});
		$("#raceNone").click(function(){
			if($(this).prop("checked")){				
				$("#raceList input").prop({"checked": false});
				$(this).prop({"checked": true});
			}			
		});		
	},
	submitNewPatient: function(){
		patient.getPatientValues();		
		$(".error").removeClass("error");
		var formValidate = patient.validateCreatePatient();
		if(formValidate){
			var detailsObj = patient.buildPatientDetailsObj();
			var actionObj = utils.buildActionObj("create", "patient");
			var patientObj = utils.buildPatientObj(false,"",detailsObj);
			utils.callWebservice('{"action":' + actionObj + ',"patient":' + patientObj + '}' , "createPatient");
		}
		else{
			$("#patientErrorMsg").show();
		}
	},
	getPatientValues: function(){
		patient.raceVal = "";
		$("#raceList input:checked").each(function(){
			patient.raceVal += $(this).val() + ";";
		});
		patient.raceVal = patient.raceVal.substring(0, patient.raceVal.length - 1);
		
		patient.ethnicityVal = "";
		$("#ethnicityList input:checked").each(function(){
			patient.ethnicityVal += $(this).val();
		});		
		patient.fNameVal = $.trim($("#fNameInput").val());
		patient.lNameVal = $.trim($("#lNameInput").val());
		patient.pIdentifierVal = $.trim($("#patientIdInput").val());
		patient.zipVal = $.trim($("#zipCodeInput").val());
		patient.sexVal = utils.getHref($("#sexDropdown .pullDown"));
		patient.mmVal =  utils.getHref($("#mmDropdown .pullDown"));
		patient.ddVal =  utils.getHref($("#ddDropdown .pullDown"));
		patient.yyyyVal =  utils.getHref($("#yyyyDropdown .pullDown"));
		patient.maritalVal =  utils.getHref($("#maritalDropdown .pullDown"));
		patient.employmentVal =  utils.getHref($("#employmentDropdown .pullDown"));
		patient.livingVal =  utils.getHref($("#livingDropdown .pullDown"));
		patient.physicianNameVal =  $.trim($("#pcpnInput").val());
		patient.physicianEmailVal =  $.trim($("#pcpeInput").val());
	},
	buildPatientDetailsObj: function(){
		var detailsObj, dob;
		detailsObj = dob = "";
		if(patient.mmVal != "" && patient.ddVal != "" && patient.yyyyVal != ""){
			dob = patient.yyyyVal + "-" + utils.pad2(patient.mmVal) + "-" + utils.pad2(patient.ddVal);
		}
		if(patient.fNameVal != ""){detailsObj += '"firstname":"' + utils.encode(patient.fNameVal) + '",';}
		if(patient.lNameVal != ""){detailsObj += '"lastname":"' + utils.encode(patient.lNameVal) + '",';}
		if(patient.physicianNameVal != ""){detailsObj += '"physicianname":"' + utils.encode(patient.physicianNameVal) + '",';}
		if(patient.physicianEmailVal != ""){detailsObj += '"physicianemail":"' + utils.encode(patient.physicianEmailVal) + '",';}
		if(patient.sexVal != ""){detailsObj += '"sex":"' + patient.sexVal + '",';}
		if(dob != ""){detailsObj += '"birth":"' + dob + '",';}
		if(patient.zipVal != ""){detailsObj += '"zip":"' + utils.encode(patient.zipVal) + '",';}
		if(patient.raceVal != ""){detailsObj += '"race":"' + patient.raceVal + '",';}
		if(patient.ethnicityVal != ""){detailsObj += '"ethnicity":"' + patient.ethnicityVal + '",';}
		if(patient.maritalVal != ""){detailsObj += '"marital":"' + patient.maritalVal + '",';}
		if(patient.employmentVal != ""){detailsObj += '"employment":"' + patient.employmentVal + '",';}
		if(patient.livingVal != ""){detailsObj += '"living":"' + patient.livingVal + '",';}
		if(patient.pIdentifierVal != ""){detailsObj += '"patientidentifier":"' + utils.encode(patient.pIdentifierVal) + '",';}
		if(detailsObj.charAt(detailsObj.length - 1) == ","){detailsObj = detailsObj.substring(0, detailsObj.length - 1);}
		return detailsObj;
	},
	validateCreatePatient: function(){
		var returnVal = true;
		var objRegExpZip = /(^\d{5}$)/;
		var objRegExpEmail = /^\w+[\+\.\w-]*@([\w-]+\.)*\w+[\w-]*\.([a-z]{2,4}|\d+)$/i;
		if((patient.physicianEmailVal != "") && utils.checkForError($("#pcpeInput"), patient.physicianEmailVal, objRegExpEmail.test(patient.physicianEmailVal)) == false){returnVal = false;}
		if(utils.checkForError($("#livingDropdown"), patient.livingVal) == false){returnVal = false;}
		if(utils.checkForError($("#employmentDropdown"), patient.employmentVal) == false){returnVal = false;}
		if(utils.checkForError($("#maritalDropdown"), patient.maritalVal) == false){returnVal = false;}
		if(utils.checkForError($("#zipCodeInput"), patient.zipVal, objRegExpZip.test(patient.zipVal)) == false){returnVal = false;}
		if(utils.checkForError($("#mmDropdown"), patient.mmVal) == false){returnVal = false;}
		if(utils.checkForError($("#ddDropdown"), patient.ddVal, utils.checkDate(patient.mmVal,patient.ddVal)) == false){returnVal = false;}
		if(utils.checkForError($("#yyyyDropdown"), patient.yyyyVal) == false){returnVal = false;}
		if(utils.checkForError($("#patientIdInput"), patient.pIdentifierVal) == false){returnVal = false;}
		if(utils.checkForError($("#sexDropdown"), patient.sexVal) == false){returnVal = false;}
		if(utils.checkForError($("#raceList"), patient.raceVal) == false){returnVal = false;}	
		if(utils.checkForError($("#ethnicityList"), patient.ethnicityVal) == false){returnVal = false;}	
		if(utils.checkForError($("#lNameInput"), patient.lNameVal) == false){returnVal = false;}
		if(utils.checkForError($("#fNameInput"), patient.fNameVal) == false){returnVal = false;}
		return returnVal;
	}
}


// jquery cookie
jQuery.cookie=function(d,c,a){if(typeof c!="undefined"){a=a||{};if(c===null){c="";a.expires=-1}var h="";if(a.expires&&(typeof a.expires=="number"||a.expires.toUTCString)){var b;if(typeof a.expires=="number"){b=new Date;b.setTime(b.getTime()+a.expires*24*60*60*1e3)}else b=a.expires;h="; expires="+b.toUTCString()}var l=a.path?"; path="+a.path:"",j=a.domain?"; domain="+a.domain:"",k=a.secure?"; secure":"";document.cookie=[d,"=",encodeURIComponent(c),h,l,j,k].join("")}else{var f=null;if(document.cookie&&document.cookie!="")for(var g=document.cookie.split(";"),e=0;e<g.length;e++){var i=jQuery.trim(g[e]);if(i.substring(0,d.length+1)==d+"="){f=decodeURIComponent(i.substring(d.length+1));break}}return f}};

// ColorBox v1.3.15 - a full featured, light-weight, customizable lightbox based on jQuery 1.3+
// Copyright (c) 2010 Jack Moore - jack@colorpowered.com
// Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
(function(b,ib){var t="none",M="LoadedContent",c=false,v="resize.",o="y",q="auto",e=true,L="nofollow",m="x";function f(a,c){a=a?' id="'+i+a+'"':"";c=c?' style="'+c+'"':"";return b("<div"+a+c+"/>")}function p(a,b){b=b===m?n.width():n.height();return typeof a==="string"?Math.round(/%/.test(a)?b/100*parseInt(a,10):parseInt(a,10)):a}function U(b){return a.photo||/\.(gif|png|jpg|jpeg|bmp)(?:\?([^#]*))?(?:#(\.*))?$/i.test(b)}function cb(a){for(var c in a)if(b.isFunction(a[c])&&c.substring(0,2)!=="on")a[c]=a[c].call(l);a.rel=a.rel||l.rel||L;a.href=a.href||b(l).attr("href");a.title=a.title||l.title;return a}function w(c,a){a&&a.call(l);b.event.trigger(c)}function jb(){var b,e=i+"Slideshow_",c="click."+i,f,k;if(a.slideshow&&h[1]){f=function(){F.text(a.slideshowStop).unbind(c).bind(V,function(){if(g<h.length-1||a.loop)b=setTimeout(d.next,a.slideshowSpeed)}).bind(W,function(){clearTimeout(b)}).one(c+" "+N,k);j.removeClass(e+"off").addClass(e+"on");b=setTimeout(d.next,a.slideshowSpeed)};k=function(){clearTimeout(b);F.text(a.slideshowStart).unbind([V,W,N,c].join(" ")).one(c,f);j.removeClass(e+"on").addClass(e+"off")};a.slideshowAuto?f():k()}}function db(c){if(!O){l=c;a=cb(b.extend({},b.data(l,r)));h=b(l);g=0;if(a.rel!==L){h=b("."+G).filter(function(){return (b.data(this,r).rel||this.rel)===a.rel});g=h.index(l);if(g===-1){h=h.add(l);g=h.length-1}}if(!u){u=D=e;j.show();if(a.returnFocus)try{l.blur();b(l).one(eb,function(){try{this.focus()}catch(a){}})}catch(f){}x.css({opacity:+a.opacity,cursor:a.overlayClose?"pointer":q}).show();a.w=p(a.initialWidth,m);a.h=p(a.initialHeight,o);d.position(0);X&&n.bind(v+P+" scroll."+P,function(){x.css({width:n.width(),height:n.height(),top:n.scrollTop(),left:n.scrollLeft()})}).trigger("scroll."+P);w(fb,a.onOpen);Y.add(H).add(I).add(F).add(Z).hide();if(a.closeButton){ab.html(a.close).show();}else{ab.html(a.close).hide();}}d.load(e)}}var gb={transition:"elastic",speed:300,width:c,initialWidth:"600",innerWidth:c,maxWidth:c,height:c,initialHeight:"450",innerHeight:c,maxHeight:c,scalePhotos:e,scrolling:e,inline:c,html:c,iframe:c,photo:c,href:c,title:c,rel:c,opacity:.9,preloading:e,current:"image {current} of {total}",previous:"previous",next:"next",close:"close",closeButton:e,open:c,returnFocus:e,loop:e,slideshow:c,slideshowAuto:e,slideshowSpeed:2500,slideshowStart:"start slideshow",slideshowStop:"stop slideshow",onOpen:c,onLoad:c,onComplete:c,onCleanup:c,onClosed:c,overlayClose:e,escKey:e,arrowKey:e},r="colorbox",i="cbox",fb=i+"_open",W=i+"_load",V=i+"_complete",N=i+"_cleanup",eb=i+"_closed",Q=i+"_purge",hb=i+"_loaded",E=b.browser.msie&&!b.support.opacity,X=E&&b.browser.version<7,P=i+"_IE6",x,j,A,s,bb,T,R,S,h,n,k,J,K,Z,Y,F,I,H,ab,B,C,y,z,l,g,a,u,D,O=c,d,G=i+"Element";d=b.fn[r]=b[r]=function(c,f){var a=this,d;if(!a[0]&&a.selector)return a;c=c||{};if(f)c.onComplete=f;if(!a[0]||a.selector===undefined){a=b("<a/>");c.open=e}a.each(function(){b.data(this,r,b.extend({},b.data(this,r)||gb,c));b(this).addClass(G)});d=c.open;if(b.isFunction(d))d=d.call(a);d&&db(a[0]);return a};d.init=function(){var l="hover",m="clear:left";n=b(ib);j=f().attr({id:r,"class":E?i+"IE":""});x=f("Overlay",X?"position:absolute":"").hide();A=f("Wrapper");s=f("Content").append(k=f(M,"width:0; height:0; overflow:hidden"),K=f("LoadingOverlay").add(f("LoadingGraphic")),Z=f("Title"),Y=f("Current"),I=f("Next"),H=f("Previous"),F=f("Slideshow").bind(fb,jb),ab=f("Close"));A.append(f().append(f("TopLeft"),bb=f("TopCenter"),f("TopRight")),f(c,m).append(T=f("MiddleLeft"),s,R=f("MiddleRight")),f(c,m).append(f("BottomLeft"),S=f("BottomCenter"),f("BottomRight"))).children().children().css({"float":"left"});J=f(c,"position:absolute; /*width:9999px;*/ visibility:hidden; display:none");b("body").prepend(x,j.append(A,J));s.children().hover(function(){b(this).addClass(l)},function(){b(this).removeClass(l)}).addClass(l);B=bb.height()+S.height()+s.outerHeight(e)-s.height();C=T.width()+R.width()+s.outerWidth(e)-s.width();y=k.outerHeight(e);z=k.outerWidth(e);j.css({"padding-bottom":B,"padding-right":C}).hide();I.click(d.next);H.click(d.prev);ab.click(d.close);s.children().removeClass(l);b("."+G).live("click",function(a){if(!(a.button!==0&&typeof a.button!=="undefined"||a.ctrlKey||a.shiftKey||a.altKey)){a.preventDefault();db(this)}});x.click(function(){a.overlayClose&&d.close()});b(document).bind("keydown",function(b){if(u&&a.escKey&&b.keyCode===27){b.preventDefault();d.close()}if(u&&a.arrowKey&&!D&&h[1])if(b.keyCode===37&&(g||a.loop)){b.preventDefault();H.click()}else if(b.keyCode===39&&(g<h.length-1||a.loop)){b.preventDefault();I.click()}})};d.remove=function(){j.add(x).remove();b("."+G).die("click").removeData(r).removeClass(G)};d.position=function(f,d){function b(a){bb[0].style.width=S[0].style.width=s[0].style.width=a.style.width;K[0].style.height=K[1].style.height=s[0].style.height=T[0].style.height=R[0].style.height=a.style.height}var e,h=Math.max(document.documentElement.clientHeight-a.h-y-B,0)/2+n.scrollTop(),g=Math.max(n.width()-a.w-z-C,0)/2+n.scrollLeft();e=j.width()===a.w+z&&j.height()===a.h+y?0:f;A[0].style.width=A[0].style.height="9999px";j.dequeue().animate({width:a.w+z,height:a.h+y,top:h,left:g},{duration:e,complete:function(){b(this);D=c;A[0].style.width=a.w+z+C+"px";A[0].style.height=a.h+y+B+"px";d&&d()},step:function(){b(this)}})};d.resize=function(b){if(u){b=b||{};if(b.width)a.w=p(b.width,m)-z-C;if(b.innerWidth)a.w=p(b.innerWidth,m);k.css({width:a.w});if(b.height)a.h=p(b.height,o)-y-B;if(b.innerHeight)a.h=p(b.innerHeight,o);if(!b.innerHeight&&!b.height){b=k.wrapInner("<div style='overflow:auto'></div>").children();a.h=b.height();b.replaceWith(b.children())}k.css({height:a.h});d.position(a.transition===t?0:a.speed)}};d.prep=function(m){var c="hidden";function l(s){var p,f,m,c,l=h.length,q=a.loop;d.position(s,function(){function s(){E&&j[0].style.removeAttribute("filter")}if(u){E&&o&&k.fadeIn(100);k.show();w(hb);Z.show().html(a.title);if(l>1){typeof a.current==="string"&&Y.html(a.current.replace(/\{current\}/,g+1).replace(/\{total\}/,l)).show();I[q||g<l-1?"show":"hide"]().html(a.next);H[q||g?"show":"hide"]().html(a.previous);p=g?h[g-1]:h[l-1];m=g<l-1?h[g+1]:h[0];a.slideshow&&F.show();if(a.preloading){c=b.data(m,r).href||m.href;f=b.data(p,r).href||p.href;c=b.isFunction(c)?c.call(m):c;f=b.isFunction(f)?f.call(p):f;if(U(c))b("<img/>")[0].src=c;if(U(f))b("<img/>")[0].src=f}}K.hide();a.transition==="fade"?j.fadeTo(e,1,function(){s()}):s();n.bind(v+i,function(){d.position(0)});w(V,a.onComplete)}})}if(u){var o,e=a.transition===t?0:a.speed;n.unbind(v+i);k.remove();k=f(M).html(m); k/*.hide()*/.appendTo(J.show()).css({width:function(){a.w=a.w||k.width();a.w=a.mw&&a.mw<a.w?a.mw:a.w;return a.w}(),overflow:a.scrolling?q:c}).css({height:function(){a.h=a.h||k.height();a.h=a.mh&&a.mh<a.h?a.mh:a.h;return a.h}()}).prependTo(s);J.hide();b("#"+i+"Photo").css({cssFloat:t,marginLeft:q,marginRight:q});X&&b("select").not(j.find("select")).filter(function(){return this.style.visibility!==c}).css({visibility:c}).one(N,function(){this.style.visibility="inherit"});a.transition==="fade"?j.fadeTo(e,0,function(){l(0)}):l(e)}};d.load=function(u){var n,c,s,q=d.prep;D=e;l=h[g];u||(a=cb(b.extend({},b.data(l,r))));w(Q);w(W,a.onLoad);a.h=a.height?p(a.height,o)-y-B:a.innerHeight&&p(a.innerHeight,o);a.w=a.width?p(a.width,m)-z-C:a.innerWidth&&p(a.innerWidth,m);a.mw=a.w;a.mh=a.h;if(a.maxWidth){a.mw=p(a.maxWidth,m)-z-C;a.mw=a.w&&a.w<a.mw?a.w:a.mw}if(a.maxHeight){a.mh=p(a.maxHeight,o)-y-B;a.mh=a.h&&a.h<a.mh?a.h:a.mh}n=a.href;K.show();if(a.inline){f().hide().insertBefore(b(n)[0]).one(Q,function(){b(this).replaceWith(k.children())});q(b(n))}else if(a.iframe){j.one(hb,function(){j.addClass("iframe"); var c=b("<iframe frameborder='0' style='width:100%; height:100%; border:0; display:block'/>")[0];c.name=i+ +new Date;c.src=a.href;if(!a.scrolling)c.scrolling="no";if(E)c.allowtransparency="true";b(c).appendTo(k).one(Q,function(){c.src="//about:blank"})});q(" ")}else if(a.html)q(a.html);else if(U(n)){c=new Image;c.onload=function(){var e;c.onload=null;c.id=i+"Photo";b(c).css({border:t,display:"block",cssFloat:"left"});if(a.scalePhotos){s=function(){c.height-=c.height*e;c.width-=c.width*e};if(a.mw&&c.width>a.mw){e=(c.width-a.mw)/c.width;s()}if(a.mh&&c.height>a.mh){e=(c.height-a.mh)/c.height;s()}}if(a.h)c.style.marginTop=Math.max(a.h-c.height,0)/2+"px";h[1]&&(g<h.length-1||a.loop)&&b(c).css({cursor:"pointer"}).click(d.next);if(E)c.style.msInterpolationMode="bicubic";setTimeout(function(){q(c)},1)};setTimeout(function(){c.src=n},1)}else n&&J.load(n,function(d,c,a){q(c==="error"?"Request unsuccessful: "+a.statusText:b(this).children())})};d.next=function(){if(!D){g=g<h.length-1?g+1:0;d.load()}};d.prev=function(){if(!D){g=g?g-1:h.length-1;d.load()}};d.close=function(){if(u&&!O){O=e;u=c;w(N,a.onCleanup);n.unbind("."+i+" ."+P);x.fadeTo("fast",0);j.stop().fadeTo("fast",0,function(){w(Q);k.remove();j.add(x).css({opacity:1,cursor:q}).hide();setTimeout(function(){O=c;w(eb,a.onClosed)},1)})}};d.element=function(){return b(l)};d.settings=gb;b(d.init)})(jQuery,this);

/*
 * JSizes - JQuery plugin v0.33
 * Licensed under the revised BSD License.
 * Copyright 2008-2010 Bram Stein
 * All rights reserved.
 */
var jSizes = (function(b){var a=function(c){return parseInt(c,10)||0};b.each(["min","max"],function(d,c){b.fn[c+"Size"]=function(g){var f,e;if(g){if(g.width!==undefined){this.css(c+"-width",g.width)}if(g.height!==undefined){this.css(c+"-height",g.height)}return this}else{f=this.css(c+"-width");e=this.css(c+"-height");return{width:(c==="max"&&(f===undefined||f==="none"||a(f)===-1)&&Number.MAX_VALUE)||a(f),height:(c==="max"&&(e===undefined||e==="none"||a(e)===-1)&&Number.MAX_VALUE)||a(e)}}}});b.fn.isVisible=function(){return this.is(":visible")};b.each(["border","margin","padding"],function(d,c){b.fn[c]=function(e){if(e){if(e.top!==undefined){this.css(c+"-top"+(c==="border"?"-width":""),e.top)}if(e.bottom!==undefined){this.css(c+"-bottom"+(c==="border"?"-width":""),e.bottom)}if(e.left!==undefined){this.css(c+"-left"+(c==="border"?"-width":""),e.left)}if(e.right!==undefined){this.css(c+"-right"+(c==="border"?"-width":""),e.right)}return this}else{return{top:a(this.css(c+"-top"+(c==="border"?"-width":""))),bottom:a(this.css(c+"-bottom"+(c==="border"?"-width":""))),left:a(this.css(c+"-left"+(c==="border"?"-width":""))),right:a(this.css(c+"-right"+(c==="border"?"-width":"")))}}}})})(jQuery);
