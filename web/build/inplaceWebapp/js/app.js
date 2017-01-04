var winHeight, winWidth, innerTableHeight, $tabs, dragText,windowTimer,messageWindow;
// 238px for top (expanded) 205px for top (collapsed), 47px for tabs, 
// 25px for manage medications area, plus 5px for top padding, 130px for graph, 45px padding for top row of table

$(document).ready(function(){	
	app.init();	
});

var app = { 
	minimumWindowHeight: 680,
	topAreaHeight: 287,
	defaultTabHeight: 393,
	pID: null,
	patientObj: null,
	searchText: "",
	tagSelected: "",
	notePageSelected: 1,
	medTableIndex: 0,
	labPageSelected: 1,
	labTestSelected: "",
	notesPerPage: 10,
	labResultsPerPage: 20,
	paginationMaxNum: 7,
	tabIndex: 0,
	getPastCRSNav: false,
	diagnosis: null,
	secondaryDiagnosis: null,
	evalSelected:0,
	init: function(){
		if($.cookie("authcode") != null){utils.loggedIn = true;}		
		app.pID = app.gup("id");
		if(app.pID.indexOf("/") >= 0){app.pID  = app.pID.substring(0,app.pID.length - 1);}		
		utils.getSessionInfo();		
		patient.init();
		
		$("a").prop("draggable", false);		
		// messageWindow = window.open("messageWindow");
				
		var testStatusObj = { 
      "crs_depression_total":[{
          "value":"12",
          "entrydate":"2011-05-23 10:35:44.0"
        },
		{
          "value":"11",
          "entrydate":"2011-05-02 11:31:01.0"
        },
        {
          "value":"14",
          "entrydate":"2011-05-16 11:31:01.0"
        },
		{
          "value":"13",
          "entrydate":"2011-04-25 11:31:01.0"
        },
		{
		  "value":"9",
          "entrydate":"2011-04-18 11:31:01.0"
        },
		{
		  "value":"9",
          "entrydate":"2011-04-04 11:31:01.0"
        },
       	{
          "value":"6",
          "entrydate":"2011-03-28 11:31:01.0"
        },
		{
		  "value":"8",
          "entrydate":"2011-03-21 11:31:01.0"
        },
		{
		  "value":"8",
          "entrydate":"2011-01-05 11:31:01.0"
        }
      ],
      "global_rating_scale_overall_symptom_severity":[
		{
          "value":"4",
          "entrydate":"2011-05-23 11:31:01.0"
        },
		{
          "value":"4",
          "entrydate":"2011-05-02 11:31:01.0"
        },
        {
          "value":"4",
          "entrydate":"2011-05-16 11:31:01.0"
        },
		{
          "value":"3",
          "entrydate":"2011-04-25 11:31:01.0"
        },
		{
		  "value":"3",
          "entrydate":"2011-04-18 11:31:01.0"
        },
		{
		  "value":"3",
          "entrydate":"2011-04-04 11:31:01.0"
        },
       	{
          "value":"3",
          "entrydate":"2011-03-28 11:31:01.0"
        },
		{
		  "value":"3",
          "entrydate":"2011-03-21 11:31:01.0"
        },
		{
		  "value":"2",
          "entrydate":"2011-01-05 11:31:01.0"
        }
      ],
      "global_rating_scale_overall_functional_impairment":[
		{
          "value":"10",
          "entrydate":"2011-05-23 11:31:01.0"
        },
		{
          "value":"10",
          "entrydate":"2011-05-16 11:31:01.0"
        },
		{
          "value":"10",
          "entrydate":"2011-05-02 11:31:01.0"
        },       
		{
          "value":"9",
          "entrydate":"2011-04-25 11:31:01.0"
        },
		{
		  "value":"9",
          "entrydate":"2011-04-18 11:31:01.0"
        },
		{
		  "value":"9",
          "entrydate":"2011-04-04 11:31:01.0"
        },
       	{
          "value":"10",
          "entrydate":"2011-03-28 11:31:01.0"
        },
		{
		  "value":"10",
          "entrydate":"2011-03-21 11:31:01.0"
        },
		{
		  "value":"unable to assess",
          "entrydate":"2011-01-05 11:31:01.0"
        }
      ],
      "global_rating_scale_overall_side_effect_severity":[
		{
          "value":"7",
          "entrydate":"2011-05-23 11:31:01.0"
        },
		{
          "value":"8",
          "entrydate":"2011-05-16 11:31:01.0"
        },
		{
          "value":"7",
          "entrydate":"2011-05-02 11:31:01.0"
        },
		{
          "value":"6",
          "entrydate":"2011-04-25 11:31:01.0"
        },		
		{
		  "value":"5",
          "entrydate":"2011-04-18 11:31:01.0"
        },		
		{
		  "value":"9",
          "entrydate":"2011-04-04 11:31:01.0"
        },
       	{
          "value":"4",
          "entrydate":"2011-03-28 11:31:01.0"
        },
		{
		  "value":"3",
          "entrydate":"2011-03-21 11:31:01.0"
        },
		{
		  "value":"0",
          "entrydate":"2011-01-05 11:31:01.0"
        }
      ],
      "diagnosis_stage":[{
          "value":"1",
          "entrydate":"2011-03-21 10:24:51.0"
        },
        {
          "value":"2",
          "entrydate":"2011-04-18 10:24:13.0"
        }
      ]
    }

		var testMedObj = {
    "Escitalopram":[{
        "prescriptionid":5,
        "treatmentid":20,
        "patientid":1,
        "discontinue":false,
        "entrydate":"2011-05-23 00:00:00.0",
        "dailydose":"20",
        "doctorname":"Dr. Doctor",
        "duration":90,
        "treatment":{
          "treatmentid":20,
          "treatmentgroupid":10,
          "treatmentname":"Escitalopram",
          "treatmentabbreviation":"Escitalopram",
          "valid":true,
          "group":{
            "treatmentgroupid":10,
            "treatmentgroupname":"Selective serotonin reuptake inhibitor",
            "treatmentgroupabbreviation":"SSRI",
            "valid":true
          },
          "details":{
            "MinDosage":"0",
            "Unit":"mg",
            "MaxDosage":"100"
          }
        }
      },
      {
        "prescriptionid":4,
        "treatmentid":20,
        "patientid":1,
        "discontinue":false,
        "entrydate":"2011-04-25 00:00:00.0",
        "dailydose":"45",
        "doctorname":"Dr. Doctor",
        "duration":90,
        "treatment":{
          "treatmentid":20,
          "treatmentgroupid":10,
          "treatmentname":"Escitalopram",
          "treatmentabbreviation":"Escitalopram",
          "valid":true,
          "group":{
            "treatmentgroupid":10,
            "treatmentgroupname":"Selective serotonin reuptake inhibitor",
            "treatmentgroupabbreviation":"SSRI",
            "valid":true
          },
          "details":{
            "MinDosage":"0",
            "Unit":"mg",
            "MaxDosage":"100"
          }
        }
      },
      {
        "prescriptionid":2,
        "treatmentid":20,
        "patientid":1,
        "discontinue":false,
        "entrydate":"2011-04-04 00:00:00.0",
        "dailydose":"30",
        "doctorname":"Dr. Doctor",
        "duration":90,
        "treatment":{
          "treatmentid":20,
          "treatmentgroupid":10,
          "treatmentname":"Escitalopram",
          "treatmentabbreviation":"Escitalopram",
          "valid":true,
          "group":{
            "treatmentgroupid":10,
            "treatmentgroupname":"Selective serotonin reuptake inhibitor",
            "treatmentgroupabbreviation":"SSRI",
            "valid":true
          },
          "details":{
            "MinDosage":"0",
            "Unit":"mg",
            "MaxDosage":"100"
          }
        }
      }
    ],
	 "Tranylcpromine":[{
        "prescriptionid":5,
        "treatmentid":20,
        "patientid":1,
        "discontinue":false,
        "entrydate":"2011-05-16 00:00:00.0",
        "dailydose":"30",
        "doctorname":"Dr. Doctor",
        "duration":90,
        "treatment":{
          "treatmentid":20,
          "treatmentgroupid":10,
          "treatmentname":"Zolpidem",
          "treatmentabbreviation":"Zolpidem",
          "valid":true,
          "group":{
            "treatmentgroupid":10,
            "treatmentgroupname":"Selective serotonin reuptake inhibitor",
            "treatmentgroupabbreviation":"SSRI",
            "valid":true
          },
          "details":{
            "MinDosage":"0",
            "Unit":"mg",
            "MaxDosage":"100"
          }
        }
      }
	],
    "Lorazepam":[{
        "prescriptionid":3,
        "treatmentid":40,
        "patientid":1,
        "discontinue":false,
        "entrydate":"2011-03-21 00:00:00.0",
        "dailydose":"20",
        "doctorname":"Dr Droc",
        "duration":90,
        "treatment":{
          "treatmentid":40,
          "treatmentgroupid":11,
          "treatmentname":"Lorazepam",
          "treatmentabbreviation":"Lorazepam",
          "valid":true,
          "group":{
            "treatmentgroupid":11,
            "treatmentgroupname":"Second Generation Antipsychotic",
            "treatmentgroupabbreviation":"SGA",
            "valid":true
          },
          "details":{
            "MinDosage":"0",
            "Unit":"mg",
            "MaxDosage":"100"
          }
        }
      },
	  {
        "prescriptionid":3,
        "treatmentid":40,
        "patientid":1,
        "discontinue":true,
        "entrydate":"2011-03-28 00:00:00.0",
        "dailydose":"0",
        "doctorname":"Dr Droc",
        "duration":1,
        "treatment":{
          "treatmentid":40,
          "treatmentgroupid":11,
          "treatmentname":"Lorazepam",
          "treatmentabbreviation":"Lorazepam",
          "valid":true,
          "group":{
            "treatmentgroupid":11,
            "treatmentgroupname":"Second Generation Antipsychotic",
            "treatmentgroupabbreviation":"SGA",
            "valid":true
          },
          "details":{
            "MinDosage":"0",
            "Unit":"mg",
            "MaxDosage":"100"
          }
        }
      }
    ],
	"Fluoxetine":[{
        "prescriptionid":3,
        "treatmentid":40,
        "patientid":1,
        "discontinue":false,
        "entrydate":"2011-03-21 00:00:00.0",
        "dailydose":"10",
        "doctorname":"Dr Droc",
        "duration":21,
        "treatment":{
          "treatmentid":40,
          "treatmentgroupid":11,
          "treatmentname":"Fluoxetine",
          "treatmentabbreviation":"Fluoxetine",
          "valid":true,
          "group":{
            "treatmentgroupid":11,
            "treatmentgroupname":"Second Generation Antipsychotic",
            "treatmentgroupabbreviation":"SGA",
            "valid":true
          },
          "details":{
            "MinDosage":"0",
            "Unit":"mg",
            "MaxDosage":"100"
          }
        }
      }
	],
	"Sertraline":[{
        "prescriptionid":3,
        "treatmentid":40,
        "patientid":1,
        "discontinue":false,
        "entrydate":"2011-03-21 00:00:00.0",
        "dailydose":"15",
        "doctorname":"Dr Droc",
        "duration":7,
        "treatment":{
          "treatmentid":40,
          "treatmentgroupid":11,
          "treatmentname":"Sertraline",
          "treatmentabbreviation":"Sertraline",
          "valid":true,
          "group":{
            "treatmentgroupid":11,
            "treatmentgroupname":"Second Generation Antipsychotic",
            "treatmentgroupabbreviation":"SGA",
            "valid":true
          },
          "details":{
            "MinDosage":"0",
            "Unit":"mg",
            "MaxDosage":"100"
          }
        }
      }
	],
	"Buproprion":[{
        "prescriptionid":3,
        "treatmentid":40,
        "patientid":1,
        "discontinue":true,
        "entrydate":"2011-04-25 00:00:00.0",
        "dailydose":"0",
        "doctorname":"Dr Droc",
        "duration":0,
        "treatment":{
          "treatmentid":40,
          "treatmentgroupid":11,
          "treatmentname":"Buproprion",
          "treatmentabbreviation":"Buproprion",
          "valid":true,
          "group":{
            "treatmentgroupid":11,
            "treatmentgroupname":"Second Generation Antipsychotic",
            "treatmentgroupabbreviation":"SGA",
            "valid":true
          },
          "details":{
            "MinDosage":"0",
            "Unit":"mg",
            "MaxDosage":"100"
          }
        }
      },
	  {
        "prescriptionid":3,
        "treatmentid":40,
        "patientid":1,
        "discontinue":false,
        "entrydate":"2011-03-21 00:00:00.0",
        "dailydose":"5",
        "doctorname":"Dr Droc",
        "duration":90,
        "treatment":{
          "treatmentid":40,
          "treatmentgroupid":11,
          "treatmentname":"Buproprion",
          "treatmentabbreviation":"Buproprion",
          "valid":true,
          "group":{
            "treatmentgroupid":11,
            "treatmentgroupname":"Second Generation Antipsychotic",
            "treatmentgroupabbreviation":"SGA",
            "valid":true
          },
          "details":{
            "MinDosage":"0",
            "Unit":"mg",
            "MaxDosage":"100"
          }
        }
      }	  
	],
	"Paroxetine":[{
        "prescriptionid":3,
        "treatmentid":40,
        "patientid":1,
        "discontinue":false,
        "entrydate":"2011-01-05 00:00:00.0",
        "dailydose":"30",
        "doctorname":"Dr Droc",
        "duration":75,
        "treatment":{
          "treatmentid":40,
          "treatmentgroupid":11,
          "treatmentname":"Paroxetine",
          "treatmentabbreviation":"Paroxetine",
          "valid":true,
          "group":{
            "treatmentgroupid":11,
            "treatmentgroupname":"Second Generation Antipsychotic",
            "treatmentgroupabbreviation":"SGA",
            "valid":true
          },
          "details":{
            "MinDosage":"0",
            "Unit":"mg",
            "MaxDosage":"100"
          }
        }
      }
	],
	"Carbamazepine":[{
        "prescriptionid":3,
        "treatmentid":40,
        "patientid":1,
        "discontinue":true,
        "entrydate":"2011-04-11 00:00:00.0",
        "dailydose":"0",
        "doctorname":"Dr Droc",
        "duration":0,
        "treatment":{
          "treatmentid":40,
          "treatmentgroupid":11,
          "treatmentname":"Carbamazepine",
          "treatmentabbreviation":"Carbamazepine",
          "valid":true,
          "group":{
            "treatmentgroupid":11,
            "treatmentgroupname":"Second Generation Antipsychotic",
            "treatmentgroupabbreviation":"SGA",
            "valid":true
          },
          "details":{
            "MinDosage":"0",
            "Unit":"mg",
            "MaxDosage":"100"
          }
        }
      },
	  {
        "prescriptionid":3,
        "treatmentid":40,
        "patientid":1,
        "discontinue":true,
        "entrydate":"2011-03-21 00:00:00.0",
        "dailydose":"30",
        "doctorname":"Dr Droc",
        "duration":90,
        "treatment":{
          "treatmentid":40,
          "treatmentgroupid":11,
          "treatmentname":"Carbamazepine",
          "treatmentabbreviation":"Carbamazepine",
          "valid":true,
          "group":{
            "treatmentgroupid":11,
            "treatmentgroupname":"Second Generation Antipsychotic",
            "treatmentgroupabbreviation":"SGA",
            "valid":true
          },
          "details":{
            "MinDosage":"0",
            "Unit":"mg",
            "MaxDosage":"100"
          }
        }
      }
	],
    "Citalopram":[{
        "prescriptionid":1,
        "treatmentid":19,
        "patientid":1,
        "discontinue":false,
        "entrydate":"2011-05-02 00:00:00.0",
        "dailydose":"25",
        "doctorname":"Dr. Doctor",
        "duration":90,
        "treatment":{
          "treatmentid":19,
          "treatmentgroupid":10,
          "treatmentname":"Citalopram",
          "treatmentabbreviation":"Citalopram",
          "valid":true,
          "group":{
            "treatmentgroupid":10,
            "treatmentgroupname":"Selective serotonin reuptake inhibitor",
            "treatmentgroupabbreviation":"SSRI",
            "valid":true
          },
          "details":{
            "DrFirstID":"123",
            "MinDosage":"0",
            "Unit":"mg",
            "MaxDosage":"100"
          }
        }
      },
	{
        "prescriptionid":1,
        "treatmentid":19,
        "patientid":1,
        "discontinue":false,
        "entrydate":"2011-03-21 00:00:00.0",
        "dailydose":"15",
        "doctorname":"Dr. Doctor",
        "duration":21,
        "treatment":{
          "treatmentid":19,
          "treatmentgroupid":10,
          "treatmentname":"Citalopram",
          "treatmentabbreviation":"Citalopram",
          "valid":true,
          "group":{
            "treatmentgroupid":10,
            "treatmentgroupname":"Selective serotonin reuptake inhibitor",
            "treatmentgroupabbreviation":"SSRI",
            "valid":true
          },
          "details":{
            "DrFirstID":"123",
            "MinDosage":"0",
            "Unit":"mg",
            "MaxDosage":"100"
          }
        }
      }
    ]
  }
  
		$tabs = $("#tabs").tabs({	
			show: function(event, ui) {
				app.tabIndex = ui.index;				
				app.setTabBoxHeight(ui.panel.id);
				if(ui.panel.id != "reports" && ui.panel.id != "evaluations"){$("#ajaxLoader").show();} // used because not all tabs are currently loading real data
				if(ui.panel.id == "information" && utils.loggedIn){
					app.getPatientInfo(true,"patientShortForm");
				}
				if(ui.panel.id == "progress" && utils.loggedIn){
					app.getProgressNotes(app.notePageSelected,app.notesPerPage,app.tagSelected,app.searchText,"getProgressNotes");
					app.getProgressNotes(1,1,"","","getLatestProgressNote");
					utils.getNoteData = true;
				}
				if(ui.panel.id == "labs" && utils.loggedIn){
					app.getPatientLabs();
					utils.getLabData = true;
				}	
				if(ui.panel.id == "recommendations" && utils.loggedIn){
					app.getRecommendations();
					app.getRecoData = true;
				}
				if(ui.panel.id == "medications" && utils.loggedIn){
					app.getMedicationData(); // calls for real data to build table
					utils.getMedsData = true;
					//app.buildMedicationsTable(testStatusObj,testMedObj); // uses fake data to build table
				}	
			}
		});	
		
		$evalTabs = $("#evaluations").tabs({
			show:function(event, ui){
				app.evalSelected = ui.index;
				var theHref = utils.getHref($(ui.tab));				
				if(ui.panel.id == "substance_abuse" || ui.panel.id == "vital_signs"){
					$("#ajaxLoader").show();
					app.getPatientInfo(true,"patientShortForm");
				}
				if(utils.loggedIn && theHref.indexOf("Past") >= 0){
					$("#ajaxLoader").show();
					var thePrefix = theHref.replace("Past","").toLowerCase() + "_";
					app.getPatientInfo(false,"patientEval",thePrefix);
					utils.getEvalData = true;
				}
			}
		});	

		$("#diagnosis_stage .content, #Pastdiagnosis_stage .content").load('/inc/diagnosis_stage.jsp', function(){
			var theID = $(this).parent().attr("id");
			if(theID.indexOf("Past") < 0){ app['setEventsFor_' + theID]();}
		});		
			
		$("#noteTextarea").htmlarea({
			css: "css//jHtmlArea.Editor.css",
			loaded: function(){				
				$(this.editor).click(function(){					
					if(this.body.innerHTML.indexOf("Enter text...") > -1){
						this.body.innerHTML = "";
						this.body.innerHTML = this.body.innerHTML.replace("Enter text...", "");
					}
				});

				$(this.editor).keyup(function(){						
					var text = this.body.innerHTML.replace(/<br>/g, "");
					if(this.body.innerHTML.indexOf("Enter text...") == -1 && text != ""){
						parent.document.getElementById("saveNote").className = "";
					}	
					else{
						parent.document.getElementById("saveNote").className = "disabled";
					}
				});
			 }
		});		
		
		$("#createNoteBtn").click(function(e){
			e.preventDefault();
			$("#createProgressNote").addClass("show");			
		});
		
		$("#thirdStep a, #fourthStep a").click(function(e){ app.openTab(e, 0); });
		$("#secondStep a").click(function(e){app.openTab(e, 1);});
		$("#thirdStep a").click(function(){$evalTabs.tabs("select", 0);});
		$("#fourthStep a").click(function(){var theIndex = $("a[href='#global_rating_scale']").parent().index() * 1 - 1; $evalTabs.tabs("select", theIndex);});
		
		$("#hideInstructions").click(function(e){
			e.preventDefault();
			if($(this).parents("div").hasClass("hide")){
				$("#instructions").removeClass("hide"); 
				app.topAreaHeight = 287;
				app.defaultTabHeight = 393;		
			}			
			else{
				$("#instructions").addClass("hide"); 
				app.topAreaHeight = 254;	
				app.defaultTabHeight = 426;						
			}
			app.setTabBoxHeight();
		});
		
		$("#prescribe").click(function(e){
			e.preventDefault();
			utils.openColorbox("/inc/drFirst.jsp?patientid="+app.pID,"drFirst", false, 900, 700, true);
		});
		
		$("#discontinue").click(function(e){
			e.preventDefault();
			if($(this).hasClass("disabled") == false){
				utils.openColorbox("/inc/discontinueMeds.jsp","discontinueMeds", true, 700);
			}
		});
		
		$("#enterNew").click(function(e){
			e.preventDefault();
			utils.openColorbox("/inc/enterNew.jsp","enterNewMed", true, 700);
		});
		
		$("#cancelNote").click(function(e){
			e.preventDefault();
			$("#createProgressNote").removeClass("show");
			$("#noteTextarea").val("Enter text...");
			$("#tagSelectList input").prop("checked",false);
			document.getElementsByTagName("iframe")[0].contentWindow.document.body.innerHTML = "Enter text...";
			$("#saveNote").addClass("disabled");
		});
		
		$("#saveNote").click(function(e){
			e.preventDefault();
			var noteText = document.getElementsByTagName("iframe")[0].contentWindow.document.body.innerHTML;
			//noteText = noteText.replace(/\"/g, '\\\"');
			var noteTextTest = noteText.replace(/&nbsp;/g, '').replace(/<br>/g, '').replace(/(^\s*)|(\s*$)/g, "");
			noteText = utils.encode(noteText, true);
			if($(this).hasClass("disabled") == false && noteTextTest != ""){
				var tagObj = app.buildTagObj();
				var actionObj = utils.buildActionObj("create", "progressnote");
				var progressNoteObj = app.buildProgressNoteObj(noteText, tagObj);
				utils.callWebservice('{"action":' + actionObj + ',"progressnote":' + progressNoteObj + '}', "createProgressNote");
			}
		});
		
		$("#viewAllBtn").click(function(e){app.openTab(e, 1);});
		$("#searchNotesBtn").click(function(e){app.openTab(e, 2);});
	
		$(".greenInput").focus(function(){if ($(this).val() == $(this).attr("rel")){$(this).val("");}});
		$(".greenInput").blur(function(){if ($(this).val() == ""){$(this).val($(this).attr("rel"));}});
		$(".greenInput").blur();   
					
		winHeight = $(window).height();
		winWidth = $(window).width();
		
		$(window).resize(function(){
			var winNewWidth = $(window).width(),
			winNewHeight = $(window).height();
			if(winWidth!=winNewWidth || winHeight!=winNewHeight){
				window.clearTimeout(windowTimer);
				windowTimer = window.setTimeout(app.setTabBoxHeight, 100);
			}
			winWidth = winNewWidth;
			winHeight = winNewHeight;
		});
		
		$("#savePatient").click(function(e){
			$("#ajaxLoader").show();
			e.preventDefault();
			patient.getPatientValues();			
			var detailsObj = patient.buildPatientDetailsObj();
			var actionObj = utils.buildActionObj("update", "patient");
			var patientObj = utils.buildPatientObj(false,app.pID,detailsObj);
			utils.callWebservice('{"action":' + actionObj + ',"patient":' + patientObj + '}', "updatePatient");
		});
		
		$("#editBtn").click(function(e){
			e.preventDefault();
			app.populateEditPatientInfo(app.patientObj);
			$("#editPatientInfo").addClass("open");
			$("#displayPatientInfo").hide();
		});
		
		$("#closeEditPatient").click(function(e){
			e.preventDefault();
			$("#editPatientInfo").removeClass("open");
			$("#displayPatientInfo").show();
		});
		
		$(".clearNoteSearch").click(function(e){
			e.preventDefault();
			app.notePageSelected = 1;
			$("#noteKeywords").val("Enter keyword(s)");
			app.setDropdownValAndSelected("tagDropdown", "");
			app.searchText = app.tagSelected = "";
			app.getProgressNotes(app.notePageSelected, app.notesPerPage,app.tagSelected, app.searchText, "getProgressNotes");
		});
		
		$(".showAllLabs").click(function(e){
			e.preventDefault();
			app.setDropdownValAndSelected("labTestsDropdown", "");
			app.labTestSelected = "";
			app.getPatientLabs();
		});
				
		$("form#keywordSearchForm").submit(function(e){
			e.preventDefault();
			app.searchText = $("#noteKeywords").val();
			if(app.searchText == "Enter keyword(s)"){app.searchText = "";}
			app.notePageSelected = 1;
			app.getProgressNotes(app.notePageSelected, app.notesPerPage,app.tagSelected, app.searchText, "getProgressNotes");
		});
		
		$("#updateViewPast,#firstStep a").click(function(e){
			e.preventDefault();
			utils.openColorbox("/inc/diagnosis_stage.jsp","diagnosisStage", true, 700);
		});
		
		$("#enterLabTests").click(function(e){
			e.preventDefault();
			utils.openColorbox("/inc/enterLabs.jsp","enterLabs", true, 700);
		});
	},
	loadEvals: function(status){
		var theLength = $("#evalContent .eval").length;
		$("#evalContent .eval").each(function(i){
			var theCategoryID = $(this).attr("id").replace("Past","").toLowerCase();
			var pageToLoad = theCategoryID;
			if(theCategoryID == "crs"){pageToLoad = app.pageToLoad(app.diagnosis);}
			$(this).find(".content").load('/inc/' + pageToLoad + '.jsp', function(){
				var theID = $("#evalContent .eval").eq(i).attr("id");
				if(theID.indexOf("CRS") >= 0){theID = theID.substring(0,theID.indexOf("_"));}
				if(theID.indexOf("Past") < 0){ app['setEventsFor_' + theID](status);}
				if(i == (theLength - 1)){
					app.showPastEvalNav(status);
					utils.loadEvals = true;
				}
			});		
		});
	},
	updateCRS: function(status){
		var pageToLoad = app.pageToLoad(app.diagnosis);		
		var theLength = $("#evalContent .eval").length;
		var tabsToRemove = [];
		for(var i =0; i<theLength; i++){
			if(i == 0 || i == 1){
				$("#evalContent .eval").eq(i).find(".content").load('/inc/' + pageToLoad + '.jsp', function(){
					app.setEventsFor_CRS();			
				});	
			}
			if(i <$("#evalContent .eval").length){
				var theID = $("#evalContent .eval").eq(i).attr("id");
				if(theID.indexOf("PastCRS") != -1 && i > 1){				
					tabsToRemove.push(theID);
				}
			}
		}
		for(var i=0; i<tabsToRemove.length; i++){
			var theIDString = "PastCRS_" + app.diagnosis;
			var theTabIndex = $("#" + tabsToRemove[i]).index();
			if(theIDString == tabsToRemove[i]){theTabIndex = theTabIndex + 1;}
			$evalTabs.tabs('remove', theTabIndex);
		}
		app.showPastCRSEvalNav(status);
	},			
	buildPastCRS: function(returnObj, index, theID, diagnosis){
		var thePrefix = theID.replace("Past","").toLowerCase() + "_";
		switch(diagnosis){
			case "bipolar": 
				$("#" + theID + " .subscale.bottom").each(function(i){
					var theSubscaleArray = $(this).attr("class").split(" ");
					for(var i=0; i<theSubscaleArray.length; i++){
						if(theSubscaleArray[i].indexOf("subscale") >= 0 && theSubscaleArray[i] != "subscale"){
							var theSubscale = theSubscaleArray[i];
						}
					}
					var theSubscaleVal = returnObj.values[index][thePrefix + theSubscale].value;
					$("#" + theID + " ." + theSubscale).find(".inputVal").text(theSubscaleVal);
				});
				app.populateEvalFormQ(theID, "bbdss", index, thePrefix, returnObj);
			break;
			case "schizophrenia": 
				app.populateEvalFormQ(theID, "negative", index, thePrefix + "negative_", returnObj);
				app.populateEvalFormQ(theID, "positive", index, thePrefix + "positive_", returnObj);
			break;
			case "depression": 
				app.populateRadioButtons(theID, "phq9", index, thePrefix, returnObj);
			break;
		}		
		app.setEvalDateTime(returnObj, theID, thePrefix, index);		
		app.buildEvalNav(returnObj, thePrefix, theID, "prevEval", index, "prev", "CRS", diagnosis);
		app.buildEvalNav(returnObj, thePrefix, theID, "nextEval", index, "next", "CRS", diagnosis);	
	},		
	buildPastPsychiatricEvaluation: function(returnObj, index, theID){
		var thePrefix = theID.replace("Past","").toLowerCase() + "_";		
		$("#" + theID + " .inputVal").text('');
		app.populateEvalFormQ(theID, "psychiatricEvaluation", index, thePrefix, returnObj);				
		app.setEvalDateTime(returnObj, theID, thePrefix, index);		
		app.buildEvalNav(returnObj, thePrefix, theID, "prevEval", index, "prev", "PsychiatricEvaluation");
		app.buildEvalNav(returnObj, thePrefix, theID, "nextEval", index, "next", "PsychiatricEvaluation");	
	},	
	buildPastMentalStatus: function(returnObj, index, theID){
		var thePrefix = theID.replace("Past","").toLowerCase() + "_";		
		$("#" + theID + " .inputVal").text('');
		app.populateEvalFormQ(theID, "mentalStatus", index, thePrefix, returnObj);				
		app.setEvalDateTime(returnObj, theID, thePrefix, index);		
		app.buildEvalNav(returnObj, thePrefix, theID, "prevEval", index, "prev", "MentalStatus");
		app.buildEvalNav(returnObj, thePrefix, theID, "nextEval", index, "next", "MentalStatus");	
	},
	buildPastVitalSigns: function(returnObj, index, theID){
		var thePrefix = theID.replace("Past","").toLowerCase() + "_";		
		$("#" + theID + " .inputVal").text('');
		app.populatePastVitalSigns(returnObj, index);				
		app.setEvalDateTime(returnObj, theID, thePrefix, index);		
		app.buildEvalNav(returnObj, thePrefix, theID, "prevEval", index, "prev", "VitalSigns");
		app.buildEvalNav(returnObj, thePrefix, theID, "nextEval", index, "next", "VitalSigns");	
	},
	buildPastDiagnosisStage: function(returnObj){
		var tableHTML = "";
		for(var i=0; i<returnObj.indices.length; i++){
			var date = returnObj.indices[i].split(" ");
			var oddClass = ''
			if(i % 2 == 0){oddClass = ' class="odd"';}
			tableHTML += '<tr'+oddClass+'>';
			tableHTML += '<td>'+app.buildDate(date[0],"/")+'</td>';
			tableHTML += '<td>'+returnObj.values[i].diagnosis_primary.value+'</td>';
			var secondaryDiagnosis = '';
			if(returnObj.values[i].diagnosis_secondary){secondaryDiagnosis = returnObj.values[i].diagnosis_secondary.value;}
			tableHTML += '<td>'+secondaryDiagnosis.replace(/\n/g,'<br>')+'</td>'
			tableHTML += '<td>'+returnObj.values[i].diagnosis_stage.value+'</td>';
			tableHTML += '</tr>';
		}
		$("#viewPastDiagnosis table tbody").html(tableHTML);
	},
	buildPastGlobalRatingScales: function(returnObj, index, theID){
		var thePrefix = "global_rating_scale_";
		$("#" + theID + " .grsEval .fieldWrapper input").prop("checked",false);
		$("#" + theID + " .grsEval .fieldWrapper input").prop("disabled",true);
		$("#" + theID + " .grsEval .fieldWrapper").each(function(){
			var scaleName = $(this).find("h2").text().replace(/ /g, "_").toLowerCase();
			if(returnObj.values[index][thePrefix + scaleName]){
				var scaleValue = returnObj.values[index][thePrefix + scaleName].value;
				if(scaleValue == "unable to assess"){
					$(this).find('input[type="checkbox"]').prop("checked",true);
				}
				else{$(this).find('input[value="' + scaleValue +'"]').prop("checked",true);}
			}		
		});	
		var evaluatedBy = returnObj.values[index][thePrefix + "evaluatedby"].value;
		$("#" + theID + " .evaluatedBy span").text(evaluatedBy);
		app.setEvalDateTime(returnObj, theID, thePrefix, index);
		app.buildEvalNav(returnObj, thePrefix, theID, "prevEval", index, "prev", "GlobalRatingScales");
		app.buildEvalNav(returnObj, thePrefix, theID, "nextEval", index, "next", "GlobalRatingScales");	
	},
	buildPastSubstanceAbuse: function(returnObj, index, theID){
		var thePrefix = "substance_abuse_";
		$("#" + theID + " .substanceAbuse .fieldWrapper input").prop("checked",false);
		$("#" + theID + " .substanceAbuse .fieldWrapper input").prop("disabled",true);
		$("#" + theID + " .substanceAbuse .fieldWrapper").each(function(){
			var theSubstance = $(this).find("label").text().toLowerCase();
			if(theSubstance.indexOf("other") >= 0){theSubstance = "other";}
			if(returnObj.values[index][thePrefix + theSubstance]){
				var abuseStatus = returnObj.values[index][thePrefix + theSubstance].value.split("|");
				for(var i=0; i<2; i++){
					$(this).find('input[value="' + abuseStatus[i] +'"]').prop("checked", true);
				}
			}
		});	
		var evaluatedBy = returnObj.values[index][thePrefix + "evaluatedby"].value;
		$("#" + theID + " .evaluatedBy span").text(evaluatedBy);
		app.setEvalDateTime(returnObj, theID, thePrefix, index);
		app.buildEvalNav(returnObj, thePrefix, theID, "prevEval", index, "prev", "SubstanceAbuse");
		app.buildEvalNav(returnObj, thePrefix, theID, "nextEval", index, "next", "SubstanceAbuse");	
	},	
	buildEvalNav: function(returnObj, thePrefix, theID, navElement, index, navType, evalName, diagnosis){
		index = index * 1;
		var indexChange = -1;
		if(navType == "prev"){indexChange = 1;}
		if(returnObj.values[index + indexChange]){
			var entryDate = returnObj.indices[index + indexChange].split(" ");
			var date = app.buildDate(entryDate[0],"/");
			var theEvalNavText = date + " >";
			if(navType == "prev"){theEvalNavText = "< " + date;}			
			var theHref = "#" + (index + indexChange);
			if(diagnosis){theHref = theHref +  "-" + diagnosis;}
			$("#" + theID + " ." + navElement).attr("href",theHref);
			$("#" + theID + " ." + navElement).text(theEvalNavText).show();	
		}
		else{$("#" + theID + " ." + navElement).hide();}
		
		$(".evalNav a").unbind().click(function(e){
			e.preventDefault();
			var theHref = utils.getHref($(this)).split("-");
			var theID = $(this).parents(".eval").attr("id");
			app["buildPast" + evalName](returnObj, theHref[0], theID, theHref[1]);
		});
	},
	buildMedicationsTable: function(statusObj, medObj){
		$("#longMedications").hide();
		$("#chartWrapper .navArrow, #scales li a").unbind();
		var resortedObj = app.reSortObj(statusObj, true, medObj);
		// messageWindow.document.body.innerHTML = "<pre>"+JSON.stringify(resortedObj,null,2)+"</pre>";
		var currentMedsTableHTML = "";
		var dcMedsTableHTML = '<tr class="discontinuedDivider"><td colspan="9">Discontinued Medications <img src="images/graph/downArrow.png" alt="" /></td></tr>';
		for(var props in resortedObj.medObj){
			if(resortedObj.medObj[props][0].discontinue){
				dcMedsTableHTML = app.buildMedTableHTML("discontinued",dcMedsTableHTML,resortedObj.medObj[props][0].treatment.details.DisplayName);				
			}		
			else{currentMedsTableHTML = app.buildMedTableHTML("current",currentMedsTableHTML,resortedObj.medObj[props][0].treatment.details.DisplayName);}				
		}
		var currentMeds = app.setMedTableRowClass(currentMedsTableHTML, 1);
		var dcMeds = app.setMedTableRowClass(dcMedsTableHTML, 0);		
		$(".innerTable tbody").html($(currentMeds).html() + $(dcMeds).html());
		innerTableHeight = $(".innerTable").height("auto").height();
		app.setMedicationTableHeight();
		var grs = false;
		var crs = false;
		var cellLength = 8;
		var startVal = 0 + (8 * app.medTableIndex);		
		var endVal = resortedObj.indices.length;		
		if(endVal < cellLength){cellLength = endVal;}	
		if(endVal > (startVal + 8)){
			endVal = startVal + 8;
			$("#chartWrapper .previous").show();			
		}
		else{$("#chartWrapper .previous").hide();}
		var scaleObject = app.buildScaleObject();	
				
		for(var i=0; i<resortedObj.values.length;i++){
			for(var props in resortedObj.values[i]){
				if(props.indexOf("global_rating_scale") != -1){grs = true;}
				if(props.indexOf("crs_" + app.diagnosis.toLowerCase()) != -1){crs = true;}
			}
		}
		if(!grs && !crs){$("#key").hide();}
		else{
			$("#key").show();
			app.populateScaleDropdown(resortedObj,crs,grs);		
			utils.setEventsForDropdown($("#scales"));				
		}		
		app.populateMedTable(resortedObj,cellLength,startVal,endVal);
		$("#chartWrapper .navArrow").click(function(e){
			e.preventDefault();
			app.medTableIndex = utils.getHref($(this)) * 1;
			startVal = 0 + (8 * app.medTableIndex);		
			endVal = resortedObj.indices.length;		
			if(endVal > (startVal + 8)){
				endVal = startVal + 8;
				$("#chartWrapper .previous").show();			
			}
			else{$("#chartWrapper .previous").hide();}
			var scaleType = utils.getHref($("#scales .pullDown"));
			app.buildMedsGraph(resortedObj,startVal,endVal,cellLength,scaleType,scaleObject);
			app.populateMedTable(resortedObj,cellLength,startVal,endVal);
		});			
		$("#scales li a").click(function(){
			var scaleType = utils.getHref($(this));
			app.buildChartKey(scaleType, scaleObject);
			app.buildMedsGraph(resortedObj,startVal,endVal,cellLength,scaleType,scaleObject);
		});		
		if(grs || crs){	
			$("#scales li:first a").click();	
			$("body").click();
		}	
	},
	populateScaleDropdown: function(resortedObj,crs,grs){		
		var scaleDropdownHTML = '';
		var oddCount = 0;
		var oddClass = ' class="odd"';
		if(crs){
			switch (app.diagnosis){
				case "Bipolar": 
					scaleDropdownHTML += '<li class="odd"><a href="#crs_bipolar">Bipolar Scales</a></li>';
					scaleDropdownHTML += '<li><a href="#crs_bipolar_subscale">Bipolar Subcales</a></li>';
				break;
				case "Schizophrenia":
					scaleDropdownHTML += '<li class="odd"><a href="#crs_schizophrenia">Schizophrenia Scales</a></li>';
					oddCount += 1;
				break;
				case "Depression":
					scaleDropdownHTML += '<li class="odd"><a href="#crs_depression">Major Depressive Scales</a></li>';
					oddCount += 1;
				break;
			}	
		}
		if(grs){
			if(oddCount > 0){oddClass = '';}
			scaleDropdownHTML += '<li'+oddClass+'><a href="#global_rating_scale">Global Rating Scales</a></li>';
		}
		$("#scales ul").html(scaleDropdownHTML);
	},
	populateMedTable: function(resortedObj, cellLength, startVal, endVal){
		$(".current td:not('.heading'), .discontinued td:not('.heading'), .dateRow th:not('.appt') span").text('').attr("title","");
		$("#chartWrapper .previous").attr("href","#" + (app.medTableIndex + 1));
		$("#chartWrapper .next").attr("href","#" + (app.medTableIndex - 1));
		$("#chartWrapper th.current").removeClass("current");	
		if(app.medTableIndex > 0){$("#chartWrapper .next").show();}
		if(app.medTableIndex == 0){
			$("#chartWrapper .next").hide();
			$(".dateRow th").eq(cellLength).addClass("current");
		}			
		var dcRowIndex = $("tr.discontinuedDivider").index();
		for(var i=0; i<$(".innerTable tbody .heading").length; i++){
			for(var j=startVal; j<endVal; j++){
				var date = resortedObj.indices[j].split(" ");
				var dateString = app.buildDate(date[0],".");
				$(".dateRow").find("th").eq((cellLength * (app.medTableIndex + 1)) - j).find(".date").text(dateString);
				var drugName = $(".innerTable tbody .heading").eq(i).text();
				var stage = resortedObj.values[j].diagnosis_stage;
				if(stage){$('.dateRow').find("th").eq((cellLength * (app.medTableIndex + 1)) - j).find(".stage").text(stage.value);}
				var objDrugName = resortedObj.values[j][drugName];				
				if(objDrugName){
					var counter = i;
					var dose = objDrugName.dailydose;
					var cellTitle = dose + objDrugName.treatment.details.Unit;
					if(dose == "0"){dose = "D/C"; cellTitle = "Discontinued";}
					if(dose == -1){dose = "UNK"; cellTitle = "Unknown";}
					if(dose < 0){
						dose = -dose;
						cellTitle = dose+objDrugName.treatment.details.Unit;
						dose = dose+"*"; 
						if (!objDrugName.longActing && i < dcRowIndex) {
							$("#longMedications").show(); 
							var longDate = objDrugName.entrydate.split(" ");
							var longDateString = app.buildDate(longDate[0],".");
							$('.innerTable tbody tr').eq(counter).find("td").eq(0).append(" <span style='color:red;'>("+longDateString+")</span>");
							objDrugName.longActing = true;
						}
					}
					if(dose == "EXP"){cellTitle="Expired";}
					if(i >= dcRowIndex){counter = i + 1};
					$('.innerTable tbody tr').eq(counter).find("td").eq((cellLength * (app.medTableIndex + 1)) - j).attr("title",cellTitle).text(dose).addClass("data");			
				}
			}
		}
		app.setCellValues(resortedObj,"discontinued",cellLength+1);
		app.setCellValues(resortedObj,"current",cellLength+1);
		for(var j=1;j<cellLength + 1;j++){
			var stageCell = $("tr.dateRow th").eq(j);
			if($(stageCell).find(".stage").text() == ""){				
				var cellIndex = $(stageCell).index() - 1;
				var dataIndex = cellLength - cellIndex + (8 * app.medTableIndex);
				var stage = "";
				for(var k=dataIndex; k<resortedObj.values.length;k++){
					if(resortedObj.values[k].diagnosis_stage){
						var stage = resortedObj.values[k].diagnosis_stage.value;
						break;
					}
				}
				$(stageCell).find(".stage").text(stage);
			}
		}	
	},
	buildPrefixes: function(){
		var prefixes = "";
		switch(app.diagnosis){
			case "Bipolar": 
				prefixes = 'crs_bipolar_subscale,crs_bipolar_total,';
			break;
			case "Schizophrenia":
				prefixes = 'crs_schizophrenia_negative_total,crs_schizophrenia_positive_total,';
			break;
			case "Depression":
				prefixes = 'crs_depression_total,';
			break;
		}
		prefixes += 'global_rating_scale,';
		prefixes += 'diagnosis_stage';
		return prefixes;
	},
	buildScaleObject: function(){		
		var object = {};
		var keyArray = [];
		keyArray.push(app.addToObject("Manic","crs_bipolar_subscalem"));
		keyArray.push(app.addToObject("Depressed","crs_bipolar_subscaled"));
		keyArray.push(app.addToObject("Psychotic","crs_bipolar_subscalep"));
		object.crs_bipolar_subscale = keyArray;
		
		var keyArray = [];
		keyArray.push(app.addToObject("Brief Bipolar Disorder Symptom Scale","crs_bipolar_total"));
		object.crs_bipolar = keyArray;				
		
		var keyArray = [];
		keyArray.push(app.addToObject("Overall Side Effect Severity","global_rating_scale_overall_side_effect_severity"));
		keyArray.push(app.addToObject("Overall Symptom Severity","global_rating_scale_overall_symptom_severity"));
		keyArray.push(app.addToObject("Overall Functional Impairment","global_rating_scale_overall_functional_impairment"));
		object.global_rating_scale = keyArray;
		
		var keyArray = [];
		keyArray.push(app.addToObject("Positive Symptom Rating Scale","crs_schizophrenia_positive_total"));
		keyArray.push(app.addToObject("Brief Negative Symptom Assessment Scale","crs_schizophrenia_negative_total"));
		object.crs_schizophrenia = keyArray;

		var keyArray = [];
		keyArray.push(app.addToObject("PHQ9","crs_depression_total"));
		object.crs_depression = keyArray;			
		return object;
	},
	addToObject: function(item1,item2){
		var innerObject = {};
		innerObject.scaleTitle = item1;
		innerObject.prefix = item2;
		return innerObject;
	},
	buildChartKey: function(scaleType, scaleObject){			
		$("#keyWrapper li").hide();
		$("#graph li").html('').attr({"class":""});
		for(var i=0; i<scaleObject[scaleType].length;i++){
			var barHTML = '<div class="bar bar' + (i+1) + '" title="'+scaleObject[scaleType][i].scaleTitle+'"><div class="shadowWrapper"></div><span class="amount"></span></div>';
			$("#graph li").addClass("barLength" + scaleObject[scaleType].length).append(barHTML);
			$("#keyWrapper li").eq(i).show().text(scaleObject[scaleType][i].scaleTitle);
		}		
	},	
	buildMedTableHTML: function(rowClassName, tableHTML, props){
		var rowClass = ' class="' + rowClassName;
		tableHTML += '<tr' + rowClass + '">';
		tableHTML += '<td class="heading">' + props + '</td>';
		for(var i=0; i<8;i++){
			tableHTML += '<td></td>';
		}
		tableHTML += '</tr>';
		return tableHTML;
	},
	setMedTableRowClass: function(tableHTML, modulusAnswer){
		var medHTML = $("<div/>").html(tableHTML);
		var rowLength = $(medHTML).find("tr").length;
		for(var i=1; i<rowLength; i++){
			if(i % 2 == modulusAnswer){
				$(medHTML).find("tr").eq(i).addClass("odd");
			}
		}	
		return medHTML;
	},
	setCellValues: function(resortedObj, rowClass, cellLength){
		for(var i=0; i<$("tr." + rowClass).length;i++){				
			for(var j=1;j<cellLength;j++){
				var cell = $("tr." + rowClass).eq(i).find("td").eq(j);
				var drugName = $("tr." + rowClass).eq(i).find(".heading").text();
				var cellIndex = $(cell).index() - 1;
				if($(cell).text() == ""){					
					var dataIndex = (cellLength - 1) - cellIndex + (8 * app.medTableIndex);
					if(dataIndex > resortedObj.values.length){dataIndex = resortedObj.values.length;}
					var prevVal = "";
					for(var k=dataIndex; k<resortedObj.values.length;k++){
						if(resortedObj.values[k][drugName]){
							var dailyDose = resortedObj.values[k][drugName].dailydose;

							if(dailyDose != "EXP" && dailyDose != "0"){
								prevVal = resortedObj.values[k][drugName].dailydose;
								if(+prevVal == -1){prevVal = "UNK";}
								if(+prevVal < 0){prevVal = -prevVal; prevVal = prevVal+"*";}
								if (resortedObj.values[k][drugName].treatment.details.AdministrationMechanism == "injection") {
									prevVal = "";
								}
								
							}
							break;
						}
					}
					$(cell).text(prevVal);
				}
			}				
		}		
	},
	addDays: function(theDate, daysToAdd){
		var theDate = new Date(theDate);
		theDate.setDate(theDate.getDate()+daysToAdd);
		var theNewDate = new Date((theDate.getMonth() + 1)+"/"+theDate.getDate()+"/"+theDate.getFullYear());
		return theNewDate;
	},
	buildMedsGraph: function(resortedObj,startVal,endVal,cellLength,scaleType,scaleObject){
		var maxAmount = 0;
		$("#graph li").css("visibility", "hidden").find(".amount").hide().text('');
		for(var j=0; j<resortedObj.values.length; j++){
			for(var k=0; k<scaleObject[scaleType].length;k++){
				if(resortedObj.values[j][scaleObject[scaleType][k].prefix]){
					var amount = resortedObj.values[j][scaleObject[scaleType][k].prefix].value * 1;
					if(amount > maxAmount){maxAmount = amount;}
				}
			}			
		}
		for(var j=startVal; j<endVal; j++){
			for(var k=0; k<scaleObject[scaleType].length;k++){
				if(resortedObj.values[j][scaleObject[scaleType][k].prefix]){
					var amount = resortedObj.values[j][scaleObject[scaleType][k].prefix].value;
					var amountText =  amount;
					if(amount == "unable to assess"){
						amount = 0;
						amountText = "N/A";
					}
					var barHeight = (amount * 1) / maxAmount * 100;
					$("#graph li").eq((cellLength * (app.medTableIndex + 1)) - (j + 1)).css("visibility", "visible").find(".bar").eq(k).height(0 + "%").find(".amount").text(amountText);
					if($("body").attr("class") == undefined){
						$("#graph li").eq((cellLength * (app.medTableIndex + 1)) - (j + 1)).find(".bar").eq(k).delay(100).animate({"height": barHeight + "%"},700, function(){$(this).find(".amount").show();});
					}
					else{
						$("#graph li").eq((cellLength * (app.medTableIndex + 1)) - (j + 1)).find(".bar").eq(k).css({"height": barHeight + "%"}).find(".amount").show();;
					}
				}
			}
		}		
	},
	setUpTextareas: function(element){
		$(element).focus(function(){if ( $(this).val() == "Enter text..."){ $(this).val("");}});
		$(element).blur(function(){if ($(this).val() == ""){$(this).val("Enter text...");}});
		$(element).blur();
	},
	setEventsForInitialDiagnosisStage: function(){
		utils.setEventsForDropdown($("#primary_diagnosis"));	
		utils.setEventsForDropdown($("#stageDropdown"));	
		utils.setEventsForDropdown($("#stageSelectedDropdown"));			
		$(".initialDiagnosisStage .radioList input").click(function(){
			$("#ids1").text("Next").addClass("showButton").removeClass("save");
			$("#stageDropdown").addClass("disabled");
			if($(this).val() == "I know the stage"){
				$("#ids1").text("Save").addClass("save");
					$("#stageDropdown").removeClass("disabled");
			}	
		});		
		$("#stageSelectedDropdown li a").click(function(){
			var theValue =  utils.getHref($(this));
			app.setDropdownValAndSelected("stageDropdown", theValue);	
		});
		$(".next").click(function(e){
			e.preventDefault();
			var div = $(".show");
			var theDivIndex = $(".diagnosisStageContainer").index(div);
			var checked =  $(".initialDiagnosisStage .radioList input:checked");
			var theRadioIndex =  $(".initialDiagnosisStage .radioList input").index(checked);
			var primaryDiagnosis = utils.getHref($("#primary_diagnosis .pullDown"));
			var secondaryDiagnosis = $("#secondary_diagnoses").val();
			if(secondaryDiagnosis == ""){secondaryDiagnosis = "N/A";}
			var validateDiagnosis = app.validateDiagnosis(primaryDiagnosis);
			if(validateDiagnosis){
				$("#primaryDiagnosisField span").text(primaryDiagnosis);
				$("#secondaryDiagnosisField span").text(secondaryDiagnosis);									
				$(".stageInfo").hide();
				if(theRadioIndex == 2 && theDivIndex == 0){
					$("#noStageInfo").show();
					app.setDropdownValAndSelected("stageSelectedDropdown", 1);	
					app.setDropdownValAndSelected("stageDropdown", 1);
				}
				else if(theRadioIndex == 1){$("#ipsInfo").show();}
				if(theRadioIndex == 0 || theDivIndex == 2){			
					var validateStage = app.validateStage();
					if(validateStage){
						var patientObj = app.buildDiagnosisPatientObj();				
						var actionObj = utils.buildActionObj("update", "patient");
						utils.callWebservice('{"action":' + actionObj + ',"patient":' + patientObj + '}', "updateDiagnosis");
					}
				}
				else{
					var theCheckedBoxes = $("#ips input:checked").length;
					var divIndex = 2;		
					if(theDivIndex == 1){						
						$("#ips .errorMsg").hide();
						if(theCheckedBoxes == 0){$("#ips .errorMsg").show();}
						else{
							var actionObj = utils.buildActionObj("read", "initialstaging");
							var initialStagingObj = app.buildInitialStagingObj(primaryDiagnosis);
							utils.callWebservice('{"action":' + actionObj + ',"initialstaging":' + initialStagingObj + '}', "initialStaging");
						}
					}					
					else{								
						if(theRadioIndex == 1 && theDivIndex == 0){divIndex = 1;}			
						$(".initialDiagnosisStage .show").removeClass("show");
						$(".diagnosisStageContainer").eq(divIndex).addClass("show");
					}
				}	
			}
		});
		$(".prev").click(function(e){
			e.preventDefault();
			var divIndex = 0;
			var theIndex = $(".initialDiagnosisStage .show").index();
			if($(".initialDiagnosisStage .radioList input").eq(1).prop("checked") && theIndex == 3){divIndex = 1;}			
			$(".initialDiagnosisStage .show").removeClass("show");
			$(".diagnosisStageContainer").eq(divIndex).addClass("show");
		});
	},
	setEventsForDiagnosisStage: function(){
		utils.setEventsForDropdown($("#primary_diagnosis"));	
		utils.setEventsForDropdown($("#stageDropdown"));	
		var primaryDiagnosis = $("#diagnosis .content").text();
		var stage = $("#stage .content").text();
		app.setDropdownValAndSelected("primary_diagnosis", primaryDiagnosis);		
		app.setDropdownValAndSelected("stageDropdown", stage);		
		$("#secondary_diagnoses").val(app.secondaryDiagnosis);
		$(".diagnosisStage h1 a").click(function(e){
			e.preventDefault();
			$(".diagnosisStage h1 a").removeClass("active");
			$(this).addClass("active");
			var theHref = utils.getHref($(this));
			$(".diagnosisStageContainer").removeClass("show");
			$("#" + theHref).addClass("show");
			if(theHref == "viewPastDiagnosis"){app.getPatientInfo(false,"patientEval","diagnosis_");}
		});
		$("form.diagnosis").submit(function(e){
			e.preventDefault();
			var patientObj = app.buildDiagnosisPatientObj();
			$(".errorMsg").hide();
			if(!patientObj){
				$(this).find(".errorMsg").show();
			}
			else{
				var actionObj = utils.buildActionObj("update", "patient");
				utils.callWebservice('{"action":' + actionObj + ',"patient":' + patientObj + '}', "updateDiagnosis");
			}
		});
	},		
	setEventsFor_CRS: function(){
		$(".cancelEval").unbind().click(function(e){
			e.preventDefault();
			var selectedTabIndex = $evalTabs.tabs("option","selected") + 1;
			$evalTabs.tabs("select", selectedTabIndex);	
		});		
		$(".viewScale").unbind().click(function(e){
			e.preventDefault();
			$(".showPopup .show").removeClass("show");
			$(".showPopup").removeClass("showPopup");
			$(this).parents("li").addClass("showPopup");
			$(this).parents("li").find(".popupScale").addClass("show");
		});
		$(".closePopup").unbind().click(function(e){
			e.preventDefault();
			$(this).parent().removeClass("show");
			$(this).parents("li").removeClass("showPopup");
		});
		$(".crsEval").unbind().submit(function(e){
			e.preventDefault();
			app.setCRSSubmit();
		});		
		$(".questionList input").unbind().keyup(function(){app.setCRSKeyup();});				
		$("#CRS_Depression .radiolist input").unbind().click(function(){app.setPHQ9OnClick((this));});
	},	
	setEventsFor_psychiatric_evaluation: function(){
		app.setUpTextareas("#psychiatric_evaluation textarea");		
		$(".psychiatricEval").unbind().submit(function(e){
			e.preventDefault();			
			app.setPsychEvalMentalStatusSubmit("psychiatric_evaluation_", ".eval:not('.ui-tabs-hide') .psychiatricEval textarea");			
		});
	},
	setEventsFor_global_rating_scale: function(){	
		$("#global_rating_scale .symptom input").click(function(){
			$(this).parents(".symptom").find(".errorMsg").hide();
		});
		$("#unableToAssess").click(function(){
			var theScale = $(this).parents(".fieldWrapper").find(".scale");
			if($(this).prop("checked")){
				$(theScale).find("input").prop("disabled",true);
				$(theScale).find("input").prop("checked", false);
			}
			else{
				$(theScale).find("input").prop("disabled",false);
			}
		});
		$(".grsEval").unbind().submit(function(e){
			e.preventDefault();
			app.setGRSSubmit();
		});	
	},
	setEventsFor_mental_status: function(){	
		app.setUpTextareas("#mental_status textarea");	
		$(".mentalStatus").unbind().submit(function(e){
			e.preventDefault();			
			app.setPsychEvalMentalStatusSubmit("mental_status_", ".eval:not('.ui-tabs-hide') .mentalStatus textarea");			
		});
	},	
	setEventsFor_substance_abuse: function(){
		$(".substanceAbuse").unbind().submit(function(e){
			e.preventDefault();			
			app.setSubstanceAbuseSubmit();		
		});		
	},
	setEventsFor_vital_signs: function(status){
		utils.setEventsForDropdown($("#heightFt"));	
		utils.setEventsForDropdown($("#heightIn"));			
		var weight, feet, inches;
		$("#weight input").keyup(function(){
			if($(this).val().length >= 2){
				weight = $(this).val();
				if(feet){
					var bmi = app.calculateBMI((feet * 1), (inches * 1), weight);
					$(".eval:not('.ui-tabs-hide') #bmi .inputVal").text(bmi);
				}
			}
			else{$(".eval:not('.ui-tabs-hide') #bmi .inputVal").text('');}
		});
		$(".patientVitalSigns .dropdown a").click(function(){
			feet = utils.getHref($(".eval:not('.ui-tabs-hide') #heightFt .pullDown"));
			inches = utils.getHref($(".eval:not('.ui-tabs-hide') #heightIn .pullDown"));
			weight = $("#weight input").val();
			if(weight > 0 && feet != ""){
				var bmi = app.calculateBMI((feet * 1), (inches * 1), weight);
				$(".eval:not('.ui-tabs-hide') #bmi .inputVal").text(bmi);
			}
			else{$(".eval:not('.ui-tabs-hide') #bmi .inputVal").text('');}
		});		
		$(".patientVitalSigns").unbind().submit(function(e){
			e.preventDefault();						
			$(".patientVitalSigns li, .patientVitalSigns input").removeClass("error");
			$(".patientVitalSigns li").find(".errorMsg").remove();
			$(".errorMsg").hide();
			var formValidate = app.validateVitalSigns();
			if(formValidate){
				$("#ajaxLoader").show();
				var patientQObj = app.buildPatientVitalSignsObj();
				var patientObj = app.buildPatientEvalObj("vital_signs_", patientQObj);
				var actionObj = utils.buildActionObj("update", "patient");
				utils.callWebservice('{"action":' + actionObj + ',"patient":' + patientObj + '}', "updatePatientEvaluation");
			}		
		});
	},		
	setEnterLabsEvents: function(){
		app.getLabTestTypes();
		utils.setEventsForDropdown("#ddDropdownLabs");
		utils.setEventsForDropdown("#mmDropdownLabs");
		utils.setEventsForDropdown("#yyyyDropdownLabs");
		var theId = $("#id .content").text();
		$("#labsPatID span").text(theId);
	},
	setEventsForDiscontinueMeds: function(){
		app.buildDiscontinuedMeds();
		$(".discontinueMeds .dcMeds").submit(function(e){
			e.preventDefault();		
			var theLength = $(".discontinueMeds .checkboxList input:checked").length;
			if(theLength < 1){
				$(".errorMsg").show();
			}
			else{
				var prescriptionObj = "";
				for(var i=0; i<theLength; i++){
					var treatmentID = $(".discontinueMeds .checkboxList input:checked").eq(i).val();
					prescriptionObj += app.buildPrescriptionObj(treatmentID) + ",";
				}
				prescriptionObj = prescriptionObj.substring(0, prescriptionObj.length - 1);
				var actionObj = utils.buildActionObj("create", "prescriptions");
				utils.callWebservice('{"action":' + actionObj + ',"prescriptions":[' + prescriptionObj + ']}', "discontinueMeds");
			}
		});
	},
	setEventsForNonConsistency: function(){
		$("#nonConsistencyForm").submit(function(e){
			e.preventDefault();	
			if($("#nonConsistencyForm input:checked").length > 0){
				var selectedRadioVal = $("#nonConsistencyForm input:checked").val();
				var actionObj = utils.buildActionObj("read", "guidelinereason");
				var guidelinereasonObj = '{"reason":["'+selectedRadioVal+'"], "patientid":"'+app.pID+'"}'
				utils.callWebservice('{"action":' + actionObj + ',"guidelinereason":' + guidelinereasonObj + '}', "guidelineReason");
			}
			else{$(".errorMsg").show();}
		});
	},
	buildDiscontinuedMeds: function(){
		var medsHTML = "";
		for(var i=0; i<$("#boxMedications .content span").length;i++){
			var fullDrugName = $("#boxMedications .content span").eq(i);
			var drugOnly = $(fullDrugName).text().split(":");
			var drugID = $(fullDrugName).attr("rel");
			medsHTML += '<li><label for="' + drugOnly[0] + 'Drug">' + $(fullDrugName).text() + '</label><input type="checkbox" id="' + drugOnly[0] + 'Drug" value="' + drugID + '" /></li>';
		}
		$(".discontinueMeds .medList ul").html(medsHTML);
	},
	setCRSSubmit: function(){
		var thePrefix, crsFormRegexValidate;	
		var theTotal = $(".eval:not('.ui-tabs-hide') .questionList .total.bottom .inputVal").text();		
		switch(app.diagnosis){
			case "Bipolar": 
				var subscaleArray = [];	
				thePrefix = "crs_bipolar_";
				$(".eval:not('.ui-tabs-hide') .questionList .subscale.bottom").each(function(){
					var innerSubscaleArray = []
					innerSubscaleArray.push($(this).find(".title").text().substring(0,1).toLowerCase());
					innerSubscaleArray.push($(this).find(".inputVal").text());
					subscaleArray.push(innerSubscaleArray);
				});		
				crsFormRegexValidate = app.validateCRS(".eval:not('.ui-tabs-hide') .bbdss .symptom input", /[0-6]/, "0-6"); 
				var patientQObj = app.buildPatientEvalQObj(thePrefix, ".eval:not('.ui-tabs-hide') .bbdss .symptom input", theTotal);
				var patientObj = app.buildPatientEvalObj(thePrefix, patientQObj, subscaleArray);
			break;
			case "Schizophrenia": 
				thePrefix = "crs_schizophrenia_";				
				var form2 = app.validateCRS(".eval:not('.ui-tabs-hide') .negative .symptom input", /[0-5]/, "0-5"); 
				var form1 = app.validateCRS(".eval:not('.ui-tabs-hide') .positive .symptom input", /[0-6]/, "0-6"); 
				if(form1 == false || form2 == false){crsFormRegexValidate = false;	}
				else{crsFormRegexValidate = true;}
				var form1Total = $(".eval:not('.ui-tabs-hide') .positive .total.bottom .inputVal").text() * 1;	
				var form2Total = $(".eval:not('.ui-tabs-hide') .negative .total.bottom .inputVal").text() * 1;
				var patientQObj = app.buildPatientEvalQObj(thePrefix + "positive_", ".eval:not('.ui-tabs-hide') .positive .symptom input", form1Total);
				patientQObj += app.buildPatientEvalQObj(thePrefix + "negative_",".eval:not('.ui-tabs-hide') .negative .symptom input", form2Total);
				var patientObj = app.buildPatientEvalObj(thePrefix, patientQObj);
			break;
			case "Depression": 
				thePrefix = "crs_depression_";
				crsFormRegexValidate = app.validatePHQ9(); 
				var patientQObj = app.buildPatientEvalQObj(thePrefix, ".eval:not('.ui-tabs-hide') .phq9 .symptom input:checked", theTotal);
				var patientObj = app.buildPatientEvalObj(thePrefix, patientQObj);
			break;
		}			
		if(crsFormRegexValidate){
			$("#ajaxLoader").show();
			var actionObj = utils.buildActionObj("update", "patient");
			utils.callWebservice('{"action":' + actionObj + ',"patient":' + patientObj + '}', "updatePatientEvaluation");
		}
	},	
	setPsychEvalMentalStatusSubmit: function(prefix, element){
		$(".errorMsg").hide();
		var formValidate = app.validatePsychEvalMentalStatus();
		if(formValidate){
			$("#ajaxLoader").show();
			var patientQObj = app.buildPatientEvalQObj(prefix, element, "", "Enter text...");
			var patientObj = app.buildPatientEvalObj(prefix, patientQObj);
			var actionObj = utils.buildActionObj("update", "patient");
			utils.callWebservice('{"action":' + actionObj + ',"patient":' + patientObj + '}', "updatePatientEvaluation");
		}
	},	
	setGRSSubmit: function(){
		$(".errorMsg").hide();
		var formValidate = app.validateGRS();
		if(formValidate){
			$("#ajaxLoader").show();
			var patientQObj = app.buildGlobalRatingScalesObj();
			var patientObj = app.buildPatientEvalObj("global_rating_scale_", patientQObj);
			var actionObj = utils.buildActionObj("update", "patient");
			utils.callWebservice('{"action":' + actionObj + ',"patient":' + patientObj + '}', "updatePatientEvaluation");
		}
	},
	setSubstanceAbuseSubmit: function(){
		$(".errorMsg").hide();
		var formValidate = app.validateSubstanceAbuse();
		if(formValidate){
			$("#ajaxLoader").show();
			var patientQObj = app.buildSubstanceAbuseObj();
			var patientObj = app.buildPatientEvalObj("substance_abuse_", patientQObj);			
			var actionObj = utils.buildActionObj("update", "patient");
			utils.callWebservice('{"action":' + actionObj + ',"patient":' + patientObj + '}', "updatePatientEvaluation");
		}
	},	
	validatePHQ9: function(){
		var crsFormRegexValidate = true;
		$(".eval:not('.ui-tabs-hide') .phq9 .symptom").each(function(){
			if($(this).find("input:checked").length == 0){
				crsFormRegexValidate = false;
				if($(this).find(".errorMsg").length == 0){
					$(this).prepend('<p class="errorMsg">Please select a response.</p>');		
				}					
			}
		});
		return crsFormRegexValidate;
	},
	validateCRS: function(element, regEx, errorMsgSuffix){
		var crsFormRegexValidate = true;
		var theLength = $(element).length - 1;	
		for(var i=theLength; i>=0; i--){
			var theInput = $(element).eq(i);
			if(utils.checkForError(theInput, $(theInput).val(), regEx.test($(theInput).val().replace(/(^\s*)/g, ""))) == false){
				crsFormRegexValidate = false;
				if((theInput).parents("li").find(".errorMsg").length == 0){
					$(theInput).parents("li").find("div.floatLeft").prepend('<p class="errorMsg">Please enter a rating from '+ errorMsgSuffix+'.</p>')
				}
			}
		}
		return crsFormRegexValidate;
	},
	validateStage: function(){
		var returnVal = true;
		$(".error").removeClass("error");			
		var stage = utils.getHref($("#stageDropdown .pullDown"));
		if(utils.checkForError($("#stageDropdown"), stage) == false){returnVal = false;}
		return returnVal;
	},
	validateDiagnosis: function(primaryDiagnosis){
		var returnVal = true;
		$(".error").removeClass("error");			
		if(utils.checkForError($("#primary_diagnosis"), primaryDiagnosis) == false){returnVal = false;}
		return returnVal;
	},
	validatePsychEvalMentalStatus: function(){
		var oneFilledIn = false;
		$(".eval:not('.ui-tabs-hide') .symptom").each(function(i){
			if($(this).find("textarea").val() != "Enter text..."){
				oneFilledIn = true;
			}
		});
		if(!oneFilledIn){$(".eval:not('.ui-tabs-hide') .errorMsg").show();}
		return oneFilledIn;
	},
	validateVitalSigns: function(vitalSignsArray){
		var formValidate = true;
		var regEx = /^\d+$/;
		$(".patientVitalSigns li .greenInput").each(function(){
			if($(this).val() != ""){
				if(utils.checkForError($(this), $(this).val(), regEx.test($(this).val().replace(/(^\s*)/g, ""))) == false){
					formValidate = false;
					if($(this).parents("li").find(".errorMsg").length == 0){
						$(this).parents("li").prepend('<p class="errorMsg">Please enter a numeric value.</p>')
					}
				}
			}
		});
		var oneFilledIn = false;
		$(".eval:not('.ui-tabs-hide') .fieldWrapper .greenInput").each(function(i){
			if($(this).val() != "" || $(".eval:not('.ui-tabs-hide') #heightFt .pullDown").text() != "Select"){
				oneFilledIn = true;
			}
		});
		if(!oneFilledIn){
			formValidate = false;
			$(".eval:not('.ui-tabs-hide') .errorMsg").show();
		}		
		var bp1 = $("#blood_pressure input").eq(0).val();
		var bp2 = $("#blood_pressure input").eq(1).val();
		if((bp1 != "" && bp2 == "") ||(bp2 != "" && bp1 == "")){
			utils.checkForError($("#blood_pressure input"), "", false);
			$("#blood_pressure").prepend('<p class="errorMsg">Please enter values for diastolic and systolic.</p>')
			formValidate = false;
		}
		return formValidate;
	},
	validateGRS: function(){
		var formValidate = true;
		$(".eval:not('.ui-tabs-hide') .grsEval .symptom").each(function(){
			if($(this).find("input:checked").length == 0){
				$(this).find(".errorMsg").show();
				formValidate = false;
			}
		});
		return formValidate;
	},
	validateSubstanceAbuse: function(){
		var oneFilledIn = false;
		$(".eval:not('.ui-tabs-hide') .substanceAbuse .fieldWrapper").each(function(){
			if($(this).find("input:checked").length > 0){
				oneFilledIn = true;				
			}
		});
		if(!oneFilledIn){$(".eval:not('.ui-tabs-hide') .errorMsg").show();}
		return oneFilledIn;
	},
	setPHQ9OnClick: function(input){
		var theRadio = $(".radiolist");
		var theVal = $(input).val();
		$(input).parents(".radiolist").find(".inputVal").text(theVal);
		var total = 0;
		$("#CRS_Depression .questionList .symptom").each(function(i){
			total = (total * 1) + ($(this).find("input:checked").val() * 1);
		});
		if($(input).parents(".symptom").find(".errorMsg").length != 0 && $(input).prop("checked")){
			$(input).parents(".symptom").find(".errorMsg").remove();
		}
		if($("#CRS_Depression .questionList input:checked").length >= 9){
			$("#CRS_Depression .questionList .total .inputVal").text(total);
		}
	},
	setCRSKeyup: function(){		
		var allFilledIn;
		var totalScore = 0;			
		switch(app.diagnosis){
			case "Bipolar": 
				var manicSubscaleArray = [1,2,3,7,8];
				var depressedSubscaleArray = [4,5,9,10];
				var psychoticSubscaleArray = [6];
				var manicSubscale, depressedSubscale, psychoticSubscale;
				manicSubscale = depressedSubscale = psychoticSubscale = 0;
				
				$(".bipolar .questionList .symptom input").each(function(i){
					manicSubscale = app.calculateSubscale(manicSubscaleArray, manicSubscale, i, $(this));
					depressedSubscale = app.calculateSubscale(depressedSubscaleArray, depressedSubscale, i, $(this));
					psychoticSubscale = app.calculateSubscale(psychoticSubscaleArray, psychoticSubscale, i, $(this));					
				});
				allFilledIn = app.keyupValidateCRS(".eval:not('.ui-tabs-hide') .symptom input", /[0-6]/);
				manicSubscale = utils.roundNumber(manicSubscale / manicSubscaleArray.length, 1);
				depressedSubscale = utils.roundNumber(depressedSubscale / depressedSubscaleArray.length, 1);
				psychoticSubscale = utils.roundNumber(psychoticSubscale / psychoticSubscaleArray.length, 1);
				$(".eval:not('.ui-tabs-hide') .symptom input").each(function(i){
					totalScore = totalScore + $(this).val() * 1;
				});			
				if(allFilledIn){
					$(".eval:not('.ui-tabs-hide') .bipolar .total .inputVal").text(totalScore);
					$(".eval:not('.ui-tabs-hide') .bipolar .subscalem .inputVal").text(manicSubscale);
					$(".eval:not('.ui-tabs-hide') .bipolar .subscaled .inputVal").text(depressedSubscale);
					$(".eval:not('.ui-tabs-hide') .bipolar .subscalep .inputVal").text(psychoticSubscale);
				}
				else{
					$(".bipolar .total .inputVal, .bipolar .subscalem .inputVal, .bipolar .subscaled .inputVal, .bipolar .subscalep .inputVal").text("");
				}
			break;
			case "Schizophrenia": 
				var form1Total, form2Total, form1FilledOut, form2FilledOut;
				form1Total = form2Total = 0;
				form1FilledOut = form2FilledOut = true;
				
				var form1 = app.keyupValidateCRS(".eval:not('.ui-tabs-hide') .positive .symptom input", /[0-6]/);
				if(form1 == false){form1FilledOut = false;}
				$(".eval:not('.ui-tabs-hide') .positive .symptom input").each(function(){form1Total = form1Total + $(this).val() * 1;});
				if(form1FilledOut){	$(".schizophrenia .positive .total .inputVal").text(form1Total);}
				else{$(".eval:not('.ui-tabs-hide') .positive .total .inputVal").text("");}
				
				var form2 = app.keyupValidateCRS(".eval:not('.ui-tabs-hide') .negative .symptom input", /[0-6]/);
				if(form2 == false){form2FilledOut = false;}	
				$(".eval:not('.ui-tabs-hide') .negative .symptom input").each(function(){form2Total = form2Total + $(this).val() * 1;});					
				if(form2FilledOut){	$(".schizophrenia .negative .total .inputVal").text(form2Total);}
				else{$(".eval:not('.ui-tabs-hide') .negative .total .inputVal").text("");}		
			break;
		}	
	},
	keyupValidateCRS: function(element, regEx){
		var allFilledIn  = true;
		$(element).each(function(i){
			if($(this).val() == "" || regEx.test($(this).val()) == false){					
				allFilledIn = false;
			}
			else{
				$(this).parents(".fieldWrapper").removeClass("error");
				$(this).parents(".fieldWrapper").find(".errorMsg").remove();
			}			
		});
		return allFilledIn;
	},	
	buildLabResultsInTab:function(returnObj){
		var tableHTML = "";
		for(var i=0; i<returnObj.labs.length; i++){
			var date = returnObj.labs[i].labdate.split(" ");
			var oddClass = ''
			if(i % 2 == 0){oddClass = ' class="odd"';}
			tableHTML += '<tr'+oddClass+'>';
			tableHTML += '<td>'+returnObj.labs[i].labtest.labtestname+'</td>';
			tableHTML += '<td>'+app.buildDate(date[0],"/")+'</td>';
			tableHTML += '<td>'+returnObj.labs[i].labtext.replace(/\|\|/g, "<br />").replace(/\|/g," ")+'</td>';
			tableHTML += '</tr>';
		}
		$("#labs table tbody").html(tableHTML);
		utils.buildPagination(returnObj.page, returnObj.total, app.labResultsPerPage, app.paginationMaxNum, "#labs .pagination");
		app.setPaginationEventsForLabs();
	},
	buildLabTestTypes: function(returnObj){
		var labHTML = '';
		var selectText = "View All";
		if(utils.lightboxOpen){selectText = "Select";}
		var labTestDropdownHTML = '<li class="odd"><a class="selected" href="#">'+selectText+'</a></li>';
		var oddClass;
		for(var i=0; i<returnObj.labtests.length; i++){
			oddClass = ''
			if(i % 2 == 1){oddClass = ' class="odd"';}
			labTestDropdownHTML += '<li' + oddClass + '><a href="#' + returnObj.labtests[i].labtestid + '">' + returnObj.labtests[i].labtestname + '</a></li>';
		}		
		var colorboxSuffix = "";
		if(utils.lightboxOpen){colorboxSuffix = "Colorbox";}		
		$("#labTestsDropdown" + colorboxSuffix + " ul").html(labTestDropdownHTML);
		utils.setEventsForDropdown($("#labTestsDropdown" + colorboxSuffix));
		app["assignLabTestEvents" + colorboxSuffix](returnObj);	
	},
	assignLabTestEventsColorbox: function(returnObj){
		$("#labTestsDropdownColorbox li a").click(function(e){
			e.preventDefault();
			var testID =  utils.getHref($(this));
			var labTestHTML = '';
			$("#saveLab").hide();
			for(var i=0; i<returnObj.labtests.length; i++){
				if(returnObj.labtests[i].labtestid == testID){
					$("#saveLab").show();
					var labtest =  returnObj.labtests[i].details;
					var values = labtest.Format.split("||");
					for(j=0; j<values.length; j++){
						labTestHTML += 	'<div class="fieldWrapper">';
						labTestHTML +=	'<label rel="' + utils.encode(values[j]) + '">' + values[j] + '</label>';
						labTestHTML +=	'<input type="text" class="greenInput" />';
						labTestHTML +=	'</div>';
					}
				}
			}
			
			$("#labTestFields").html(labTestHTML);
			$("#enterLabsForm label").each(function(){
				if($(this).height() > 25){$(this).addClass("noTopPadding");}
			});			
		});
		$("#moreLabs").click(function(e){
			e.preventDefault();
			$("#savedLab").removeClass("show");
			$("#labFields").addClass("show");
			app.setDropdownValAndSelected("labTestsDropdownColorbox", "");
			app.setDropdownValAndSelected("mmDropdownLabs", "");
			app.setDropdownValAndSelected("ddDropdownLabs", "");
			app.setDropdownValAndSelected("yyyyDropdownLabs", "");
			$("#labTestFields").html('');
			$("#saveLab").hide();
		});
	
		$("#closeLabs").click(function(e){
			e.preventDefault();
			utils.closeLightbox();			
		});

		$("#enterLabsForm").submit(function(e){
			e.preventDefault();
			$(".error").removeClass("error");
			var mmDropdown = utils.getHref($("#mmDropdownLabs .pullDown"));
			var ddDropdown =  utils.getHref($("#ddDropdownLabs .pullDown"));
			var yyyyDropdown =  utils.getHref($("#yyyyDropdownLabs .pullDown"));
			var labTestID = utils.getHref($("#labTestsDropdownColorbox .pullDown"));
			var validLab = app.validateSaveLabTest(mmDropdown, ddDropdown, yyyyDropdown);
			var date = yyyyDropdown + "-" + mmDropdown + "-" + ddDropdown;
			if(validLab){
				var actionObj = utils.buildActionObj("create", "lab");
				var labObj = app.buildLabObj(date,labTestID);
				utils.callWebservice('{"action":' + actionObj + ',"lab":' + labObj + '}', "enterLabTest");
			}		
		});
	},
	assignLabTestEvents: function(returnObj){
		$("#labTestsDropdown li a").click(function(e){
			e.preventDefault();
			var testID =  utils.getHref($(this));
			app.labTestSelected = testID;
			$("#ajaxLoader").show();
			app.getPatientLabs();
		});
	},
	validateSaveLabTest: function(mmDropdown, ddDropdown, yyyyDropdown){
		var returnVal = true;		
		if(utils.checkForError($("#mmDropdownLabs"), mmDropdown) == false){returnVal = false;}
		if(utils.checkForError($("#ddDropdownLabs"), ddDropdown) == false){returnVal = false;}
		if(utils.checkForError($("#ddDropdownLabs"), ddDropdown, utils.checkDate(mmDropdown,ddDropdown)) == false){returnVal = false;}
		if(utils.checkForError($("#yyyyDropdownLabs"), yyyyDropdown) == false){returnVal = false;}
		var errorMsg = "";
		var emptyInputVal = true;
		$("#labTestFields input").each(function(){
			if($(this).val().replace(/(^\s*)/g, "") != ""){
				emptyInputVal = false;
			}
		});
		if(mmDropdown == "" || ddDropdown == "" || yyyyDropdown == "" || utils.checkDate(mmDropdown,ddDropdown)==false){errorMsg += "<p>Please select a valid date.</p>";}	
		if(emptyInputVal){errorMsg += "<p>Please enter data for at least one field.</p>"; returnVal = false;}
		$("#labsErrorMsg").html(errorMsg);
		return returnVal;
	},	
	getLabTestTypes: function(){
		var actionObj = utils.buildActionObj("read", "labtest");
		utils.callWebservice('{"action":' + actionObj + '}', "getLabTestTypes");
	},
	getPatientLabs: function(){
		var actionObj = utils.buildActionObj("read", "labsearch");
		var labSearchObj = app.buildLabSearchObj(app.labPageSelected,app.labResultsPerPage,app.labTestSelected);
		utils.callWebservice('{"action":' + actionObj + ',"labsearch":' + labSearchObj + '}', "getLabResults");
	},
	getMedicationData: function(){
		var actionObj = utils.buildActionObj("read", "prescriptionsearch");
		var prescriptionObj = app.buildPrescriptionSearchObj(false);
		utils.callWebservice('{"action":' + actionObj + ',"prescriptionsearch":' + prescriptionObj + '}', "getMedicationData");
	},
	getRecommendations: function(){
		var facilityID = $.cookie("facilityID");
		var actionObj = utils.buildActionObj("read", "recommendation");
		var patientObj = '{"patientid":"' + app.pID+'"}';
		utils.callWebservice('{"action":' + actionObj + ',"patient":' + patientObj + '}', "getRecommendations");
	},
	getProgressNotes: function(page,pagecount,tags,searchText, commandText){
		var actionObj = utils.buildActionObj("read", "progressnotesearch");
		var progressNoteObj = app.buildProgressSearchNoteObj(page,pagecount,tags,searchText);
		utils.callWebservice('{"action":' + actionObj + ',"progressnotesearch":' + progressNoteObj + '}', commandText);
	},	
	getPatientInfo: function(shortform, commandType, prefix){
		var patientObj = utils.buildPatientObj(shortform,app.pID,"");
		if(prefix){patientObj = utils.buildPatientObj(shortform,app.pID,"",prefix);}
		var actionObj = utils.buildActionObj("read", "patient");
		utils.callWebservice('{"action":' + actionObj + ',"patient":' + patientObj + '}', commandType);
	},
	getCurrentMeds: function(){
		var actionObj = utils.buildActionObj("read", "prescriptionsearch");
		var prescriptionObj = app.buildPrescriptionSearchObj(true,true);
		utils.callWebservice('{"action":' + actionObj + ',"prescriptionsearch":' + prescriptionObj + '}', "getCurrentMeds");
	},
	buildProgressNotesInTab: function(returnObj){
		var noteHTML = '';
		for(var i=0; i<returnObj.notes.length; i++){
			var entryDate = returnObj.notes[i].entrydate.split(" ");
			var time = utils.convertMilitaryTime(entryDate[1].split(":"));				
			var date = app.buildDate(entryDate[0],"/");
			var tags = "";
			if(returnObj.notes[i].tags){
				tags = utils.buildTagsForNote(returnObj.notes[i].tags);
			}
			noteHTML += '<li><div class="overflowContainer">';
			noteHTML += '<h3><span class="author">' + returnObj.notes[i].doctorname + '</span>';
			noteHTML += '<span class="time">' + time + '</span>';
			noteHTML += '<span class="date">' + date + '</span></h3>';
			noteHTML += '<p class="tags">' + tags + '</p>';
			var noteText = returnObj.notes[i].notetext;
			noteText = noteText.replace(/&#34;/g, '"');
			noteHTML += '<div class="noteText">' + noteText + '</div>';
			noteHTML += '</div></li>';
		}
		$("#noteSamples ul").html(noteHTML);
		
		utils.buildPagination(returnObj.page, returnObj.total, app.notesPerPage, app.paginationMaxNum, "#noteSamples .pagination");
		app.setPaginationEventsForNotes();
				
		$("#noteSamples li").click(function(){
			$("#noteSamples li").removeClass("selected");
			var content = $(this).find(".overflowContainer").html();
			$("#noteContent").html(content);
			$(this).addClass("selected");
		});
		$("#noteSamples ul li:first").click();
	},
	buildLatestProgressNoteBox: function(returnObj){
		var entryDate = returnObj.notes[0].entrydate.split(" ");
		var time = utils.convertMilitaryTime(entryDate[1].split(":"));				
		var date =  app.buildDate(entryDate[0],".");
		var tags = "";
		var noteHTML = '';
		if(returnObj.notes[0].tags){
			tags = utils.buildTagsForNote(returnObj.notes[0].tags);
			noteHTML = '<p class="tags">Tags: ' + tags + '</p>';
		}
		var noteText = returnObj.notes[0].notetext;
		noteText = noteText.replace(/&#34;/g, '"');
		noteHTML += "<p>" + noteText + "</p>";
		$("#boxNotes .note").html(noteHTML);
		var boxHeight = $("#boxNotes .note").height();
		var noteHeight = 0;
		$("#boxNotes .note p").each(function(){
			noteHeight += $(this).height();
		});
		if(noteHeight > boxHeight){$("#boxNotes .note").append('<span class="ellipses">...</span>');}
		$("#boxNotes .date").text(date);
		$("#boxNotes .time").text(time);
		$("#boxNotes .author").text(returnObj.notes[0].doctorname);	
	},
	setPaginationEventsForNotes: function(){
		if($("#searchKeywords input").val() != "Enter keyword(s)"){ app.searchText = $("#searchKeywords input").val();}
		$("#progress .pagination a").click(function(e){
			e.preventDefault();
			if($(this).hasClass("next")){
				app.notePageSelected = (app.notePageSelected * 1) + 1;
			}
			else if($(this).hasClass("prev")){
				app.notePageSelected = (app.notePageSelected * 1) - 1;
			}
			else{
				var thePage = $(this).text();
				app.notePageSelected = thePage;
			}
			app.getProgressNotes(app.notePageSelected, app.notesPerPage,app.tagSelected, app.searchText, "getProgressNotes");
		});
	},	
	setPaginationEventsForLabs: function(){
		$("#labs .pagination a").click(function(e){
			e.preventDefault();
			if($(this).hasClass("next")){
				app.labPageSelected = (app.labPageSelected * 1) + 1;
			}
			else if($(this).hasClass("prev")){
				app.labPageSelected = (app.labPageSelected * 1) - 1;
			}
			else{
				var thePage = $(this).text();
				app.labPageSelected = thePage;
			}
			app.getPatientLabs();
		});
	},
	getTags: function(){
		var actionObj = utils.buildActionObj("read", "progressnotetags");
		utils.callWebservice('{"action":' + actionObj + '}', "getTags");
	},
	buildTagLists: function(tagsArray){
		var tagListHTML = '';
		var tagDropdownHTML = '<li class="odd"><a class="selected" href="#">Select</a></li>';
		var oddClass;

		for(var i=0; i<tagsArray.length; i++){
			oddClass = ''
			if(i % 2 == 1){oddClass = ' class="odd"';}
			tagListHTML += '<li><input tabindex="-1" type="checkbox" id="'+tagsArray[i][0]+'Tag" value="' + tagsArray[i][1] + '"/><label for="'+tagsArray[i][0]+'Tag">'+tagsArray[i][0]+'</label></li>';
			tagDropdownHTML += '<li' + oddClass + '><a href="#' + tagsArray[i][1] + '">' + tagsArray[i][0] + '</a></li>';
		}
		$("#tagSelectList ul").html(tagListHTML);
		$("#tagDropdown ul").html(tagDropdownHTML);
		
		utils.setEventsForDropdown($("#tagDropdown"));
		$("#tagDropdown li a").click(function(e){
			e.preventDefault();
			var tagID =  utils.getHref($(this));
			if(tagID != ""){
				app.tagSelected = '{"progressnotetagid":' + tagID + ', "progressnotetag":"'+$(this).text()+'"}';
				app.notePageSelected = 1;
				app.getProgressNotes(app.notePageSelected, app.notesPerPage,app.tagSelected, app.searchText, "getProgressNotes");
			}
		});
	},
	buildPastCRSNav: function(CRSType){
		var scaleName = CRSType;
		if(CRSType == "Depression"){scaleName = "Major Depressive";}
		var theIndex =  $("#global_rating_scale").index();		
		$evalTabs.tabs("add", "#PastCRS_" + CRSType, "View Past <span>" + scaleName + " Scales</span>", theIndex);
		$("#evalContent .ui-tabs-panel").eq(theIndex).html('<div class="content"></div>').addClass("eval past");
		var theNewIndex =  $("#global_rating_scale").index();
		$("#evalNav li").eq(theNewIndex).addClass("pastEval").show();
		var pageToLoad = app.pageToLoad(CRSType);
		$("#evalContent .eval").eq(theIndex).find(".content").load('/inc/' + pageToLoad + '.jsp', function(){			
			var theID = $(this).parent().attr("id");
			theID = theID.substring(0,theID.indexOf("_"));
			app.setEventsFor_CRS();
		});
	},		
	pageToLoad: function(CRSType){
		switch(CRSType){
			case "Bipolar": pageToLoad = "bbdss"; break;
			case "Schizophrenia": pageToLoad = "psrs"; break;
			case "Depression": pageToLoad = "phq9"; break;
			case "Other": pageToLoad = "other"; break;
		}
		return pageToLoad;
	},
	showPastEvalNav: function(status){
		if(status.psychiatric_evaluation_evaluatedby){$('a[href="#Pastpsychiatric_evaluation"]').parent().show();}
		if(status.global_rating_scale_evaluatedby){$('a[href="#Pastglobal_rating_scale"]').parent().show();}
		if(status.mental_status_evaluatedby){$('a[href="#Pastmental_status"]').parent().show();}
		if(status.substance_abuse_evaluatedby){$('a[href="#Pastsubstance_abuse"]').parent().show();}
		if(status.vital_signs_evaluatedby){$('a[href="#Pastvital_signs"]').parent().show();}
		app.showPastCRSEvalNav(status);
	},
	showPastCRSEvalNav: function(status){
		var crs;
		var bipolar = "crs_bipolar_evaluatedby";
		var schizophrenia = "crs_schizophrenia_evaluatedby";
		var depression = "crs_depression_evaluatedby";
		switch(app.diagnosis){
			case "Bipolar": crs = bipolar; break;
			case "Schizophrenia": crs = schizophrenia; break;
			case "Depression": crs = depression; break;
		}
		if(status[crs]){$('#PastCRSNav').show();}
		else{$('#PastCRSNav').hide();}
		if(status[bipolar] && crs != bipolar){app.buildPastCRSNav("Bipolar");}
		if(status[schizophrenia] && crs != schizophrenia){app.buildPastCRSNav("Schizophrenia");}
		if(status[depression] && crs != depression){app.buildPastCRSNav("Depression");}
	},
	populateDisplayPatientInfo: function(patient){
		var patientGender = patient.details.sex.toUpperCase();
		var sex;
		var dob = patient.details.birth.split("-");
		var raceString = patient.details.race.replace(/\(/g, '<span class="subRace">(').replace(/\)/g, ')</span>');
		raceString = raceString.split(";");
		var raceList = '<ul>';
		for(var i = 0; i<raceString.length; i++){
			raceList += '<li>' + raceString[i] + '</li>';
		}
		raceList += '</ul>';
		$("#fNameText").text(patient.details.firstname);
		$("#lNameText").text(patient.details.lastname);
		$("#raceText").html(raceList);
		$("#ethnicityText").html("Hispanic, Latino or Spanish?<br />" + patient.details.ethnicity);
		if(patientGender == "M"){sex = "Male";}
		else if(patientGender == "F"){sex = "Female";}
		$("#sexText").text(sex);
		$("#pcpnText").text(patient.details.physicianname);
		$("#pcpeText").html(patient.details.physicianemail);
		$("#patientIdText").text(patient.details.patientidentifier);
		$("#dobText").text((dob[1] * 1) + "/" + (dob[2] * 1) + "/" + dob[0]);
		$("#zipCodeText").text(patient.details.zip);
		$("#maritalText").text(patient.details.marital);
		$("#employmentText").text(patient.details.employment);
		$("#livingText").text(patient.details.living);
	},
	populateSubstanceAbuse: function(status){
		$("#substance_abuse .substanceAbuse .fieldWrapper input").prop("checked", false);
		$("#substance_abuse .substanceAbuse .fieldWrapper").each(function(){
			var theSubstance = $(this).find("label").text().toLowerCase();
			if(theSubstance.indexOf("other") >= 0){theSubstance = "other";}
			if(status["substance_abuse_" + theSubstance]){
				var abuseStatus = status["substance_abuse_" + theSubstance].split("|");
				for(var i=0; i<2; i++){
					$(this).find('input[value="' + abuseStatus[i] +'"]').prop("checked", true);
				}
			}
		});	
	},
	populateVitalSigns: function(status){
		app.setDropdownValAndSelected("vital_signs #heightFt", status.vital_signs_height_feet);
		app.setDropdownValAndSelected("vital_signs #heightIn", status.vital_signs_height_inches);
	},
	populateEditPatientInfo:function(patient){
		var dob = patient.details.birth.split("-");
		var physicianEmail = "";
		if(patient.details.physicianemail){physicianEmail = patient.details.physicianemail;}		$("#fNameInput").val(patient.details.firstname);
		$("#lNameInput").val(patient.details.lastname);
		$("#patientIdInput").val(patient.details.patientidentifier);
		$("#zipCodeInput").val(patient.details.zip);		
		$("#pcpnInput").val(patient.details.physicianname);	
		$("#pcpeInput").val(utils.decode(physicianEmail));	
		app.setDropdownValAndSelected("sexDropdown", patient.details.sex.toUpperCase());
		app.setDropdownValAndSelected("mmDropdown", dob[1] * 1);
		app.setDropdownValAndSelected("ddDropdown", dob[2] * 1);
		app.setDropdownValAndSelected("yyyyDropdown", dob[0] * 1);
		var raceString = patient.details.race.split(";");
		$("#raceList input").prop("disabled",false);
		$("#raceList input").prop("checked",false);
		$("#raceList input").each(function(i){
			for(var j=0; j<raceString.length; j++){
				if($(this).val() == raceString[j]){
					$(this).prop("checked",true);
				}
			}
		});
		$("#ethnicityList input").each(function(i){
			if($(this).val() == patient.details.ethnicity){
				$(this).prop("checked",true);
			}
			else{$(this).prop("checked",false);}
		});
		app.setDropdownValAndSelected("maritalDropdown", patient.details.marital);
		app.setDropdownValAndSelected("employmentDropdown", patient.details.employment);
		app.setDropdownValAndSelected("livingDropdown", patient.details.living);
	},
	populatePastVitalSigns: function(returnObj, index){
		var heightFt = "vital_signs_height_feet";
		var heightIn = "vital_signs_height_inches";
		var weight = "vital_signs_weight";
		var bmi = "vital_signs_bmi";
		var bloodPressureTop = "vital_signs_blood_pressure_top";
		var bloodPressureBottom = "vital_signs_blood_pressure_bottom";
		var heartRate = "vital_signs_heart_rate";
		var evaluatedBy = "vital_signs_evaluatedby";
		$(".vitalSigns .notEntered").removeClass("notEntered");
		app.setVitalSignField(returnObj, index, $("#Pastvital_signs #height .inputVal").eq(0), heightFt);
		app.setVitalSignField(returnObj, index, $("#Pastvital_signs #height .inputVal").eq(1), heightIn);
		app.setVitalSignField(returnObj, index, $("#Pastvital_signs #weight .inputVal"), weight);
		app.setVitalSignField(returnObj, index, $("#Pastvital_signs #bmi .inputVal"), bmi);
		app.setVitalSignField(returnObj, index, $("#Pastvital_signs #blood_pressure .inputVal").eq(0), bloodPressureTop);
		app.setVitalSignField(returnObj, index, $("#Pastvital_signs #blood_pressure .inputVal").eq(1), bloodPressureBottom);
		app.setVitalSignField(returnObj, index, $("#Pastvital_signs #heart_rate .inputVal"), heartRate);
		app.setVitalSignField(returnObj, index, $("#Pastvital_signs .evaluatedBy span"), evaluatedBy);
	},
	populateEvalFormQ: function(theID, scaleType, index, thePrefix, returnObj){
		var evaluatedByPrefix = thePrefix;
		if(scaleType == "negative" || scaleType == "positive"){
			evaluatedByPrefix = evaluatedByPrefix.replace("negative_", "").replace("positive_","");
		}
		for(var i=0; i<$("#" + theID + " ." + scaleType +  " .symptom .inputVal").length; i++){
			var theQPrefix = thePrefix + "q" + (i+1);
			if(returnObj.values[index][theQPrefix]){
				var theQVal = utils.decode(returnObj.values[index][theQPrefix].value);
				$("#" + theID + " ." + scaleType +  " .symptom .inputVal").eq(i).html(theQVal);
			}		
			else{
				$("#" + theID + " ." + scaleType +  " .symptom .inputVal").eq(i).html("N/A");
			}			
		}
		if(returnObj.values[index][thePrefix + "total"]){
			var theTotalVal = returnObj.values[index][thePrefix + "total"].value;
			$("#" + theID + " ." + scaleType + " .total .inputVal").html(theTotalVal);
		}
		if(returnObj.values[index][evaluatedByPrefix + "evaluatedby"]){
			var evaluatedBy = returnObj.values[index][evaluatedByPrefix + "evaluatedby"].value;			
			$("#" + theID + " .evaluatedBy span").html(evaluatedBy);
		}
	},
	populateRadioButtons: function(theID, scaleType, index, thePrefix, returnObj){
		$("#" + theID + " ." + scaleType +  " .symptom input").prop("disabled",true);
		for(var i=0; i<$("#" + theID + " ." + scaleType +  " .symptom").length; i++){
			var theQPrefix = thePrefix + "q" + (i+1);
			var theQVal =  returnObj.values[index][theQPrefix].value;
			$("#" + theID + " ." + scaleType +  " .symptom .inputVal").eq(i).text(theQVal);
			$("#" + theID + " ." + scaleType +  ' .symptom').eq(i).find('input[value="'+theQVal+'"]').prop("checked",true);
		}
		var theTotalVal = returnObj.values[index][thePrefix + "total"].value;
		var evaluatedBy = returnObj.values[index][thePrefix + "evaluatedby"].value;
		$("#" + theID + " ." + scaleType + " .total .inputVal").text(theTotalVal);		
		$("#" + theID + " .evaluatedBy span").text(evaluatedBy);
	},
	populateDiagnosisStage: function(status){		
		if(status){
			app.setDiagnosis(status);
			$("#diagnosis .content").text(status.diagnosis_primary);
			var stage = status.diagnosis_stage;		
			$("#stage .content").text(stage);
		}
	},
	populatePatientInfo:function(patient){
		var fullDob = (patient.details.birth).split("-");
		var dob = (fullDob[1] * 1) + "/" + (fullDob[2] * 1) + "/" + fullDob[0]; 
		var today=new Date();		
		var age = today.getFullYear() - fullDob[0];	
		// check if birthday is later in the year and if so, subtract a year
		var birthdate = new Date(today.getFullYear(),(fullDob[1] * 1 - 1), (fullDob[2] * 1));
		if(today < birthdate){age = age - 1;}	
		$("#first .content").text(patient.details.firstname);
		$("#last .content").text(patient.details.lastname);
		$("#id .content").text(patient.details.patientidentifier);		
		$("#sex .content").text(patient.details.sex);
		$("#dob .content").text(dob);
		$("#age .content").text(age);
	},
	populateCurrentMeds: function(prescriptions){
		var medsHTML = "";
		var drugCount = 0;
		for(var props in prescriptions){
			var localdose = prescriptions[props][0].dailydose;
			if (localdose == -1) localdose = "UNK";
			if (localdose < 0) localdose = -localdose;
			if (localdose != "UNK") localdose = localdose + prescriptions[props][0].treatment.details.Unit;
			medsHTML += '<span rel="' +  prescriptions[props][0].treatmentid + '">' + prescriptions[props][0].treatment.details.DisplayName + ": " + localdose + " </span>";
			drugCount += 1; 
		}
		$("#boxMedications h2 span").html(" (" + drugCount + ")");
		$("#boxMedications .content").html(medsHTML);
	},
	populateRecos: function(recommendations){
		var guidelineTable = "";
		var notes = "";
		$("#recommendations .conditionalHeading").addClass("hide");
		var generalmessages = app.generateRecoHTML(recommendations.generalmessages, "paragraph",  "Most Recent Evaluations", "h3");
		var generalconsistency = app.generateRecoHTML(recommendations.generalconsistency, "paragraph", "General Guideline Consistency", "h3");
		var additionalconsistency = app.generateRecoHTML(recommendations.additionalconsistency, "paragraph",  "Diagnosis-Related Guideline Consistency", "h3");
		var treatmentmessages = app.generateRecoHTML(recommendations.treatmentmessages, "paragraph", "General Treatment Recommendations", "h3");
		var specialmessages = app.generateRecoHTML(recommendations.specialmessages, "paragraph", "Special Treatment Recommendations", "h3");
		var clinicalresponse = app.generateRecoHTML(recommendations.clinicalresponse, "paragraph", "Changes in Clinical Scales", "h3");
		var medicationresponse = app.generateRecoHTML(recommendations.medicaltrial, "paragraph", "Medication Trials", "h2");
		var othermessages = app.generateOtherInfoHTML(recommendations.othermessages);
		if(recommendations.guidelinechart){
			guidelineTable = app.generateRecoTable(recommendations.guidelinechart);
			$(".treatmentTableHolder").show();
			$(".guidelinechart tbody").html(guidelineTable);
			notes = recommendations.guidelinechart.notes;
			$(".notes").html("<h3>Treatment Table Notes</h3><p>" + notes + "</p>");
		}
		else{
			$(".treatmentTableHolder").hide();
		}
		if(generalmessages != "") {
			$("#generalMessagesHdr").removeClass("hide");
		}
		$(".generalmessages").html(generalmessages);
		if(generalconsistency != "" || additionalconsistency != ""){
			$("#guidelineConsistencyHdr").removeClass("hide");
		}
		$(".generalconsistency").html(generalconsistency);
		$(".additionalconsistency").html(additionalconsistency);
		if(treatmentmessages != "" || specialmessages != "" || clinicalresponse != ""){
			$("#clinicalResponseHdr").removeClass("hide");
		}
		$(".treatmentmessages").html(treatmentmessages);
		$(".specialmessages").html(specialmessages);
		$(".clinicalresponse").html(clinicalresponse);

		$(".medicationresponse").html(medicationresponse);		
		if(othermessages != ""){$("#otherInfoHdr").removeClass("hide");}
		$(".othermessages").html(othermessages);
	},
	generateRecoTable: function(guidelinechart){
		var tableHTML = "";
		for(var i=0; i<guidelinechart.rows.length;i++){
			tableHTML += '<tr><td class="odd narrow">' + guidelinechart.stage + "</td>";
			var drugArray = guidelinechart.rows[i].medications;
			for(var j=0; j<3;j++){
				var drug = "";
				var oddClass = ''
				if(j % 2 == 1){oddClass = ' class="odd"';}
				if(drugArray[j]){drug = drugArray[j];}
				tableHTML += "<td" + oddClass + ">" + drug + "</td>";
			}
			tableHTML += "</tr>";
		}
		return tableHTML;
	},
	generateOtherInfoHTML: function(messaging){
		var html = ""
		for(var i=0;i<messaging.length;i++){
			if (messaging[i])
			for(var j=0; j<messaging[i].length;j++){				
				if(j==0){
					html += "<h3>" + messaging[i][0] + "</h3>";
				}
				else{
					html += "<p>" + messaging[i][j] + "</p>";
				}
			}			
		}
		html += "</dl>";
		return html;
	},
	generateRecoHTML: function(messagingArray, markupType, header, headerType){
		var html = "";
		if(messagingArray && messagingArray.length){
			if(header){ html += "<"+headerType+">" + header + "</"+headerType+">";}
			if(markupType == "unorderedList"){html += "<ul>";}
			for(var i=0;i<messagingArray.length;i++){
				switch(markupType){
					case "paragraph":
						html += "<p>" + messagingArray[i] + "</p>";
						break;
					case "unorderedList":
						html += "<li>" + messagingArray[i] + "</li>";
						break;
				}
			}
			if(markupType == "unorderedList"){html += "</ul>";}
		}
		return html;
	},
	setEvalDateTime: function(returnObj, theID, thePrefix, index){
		var entryDate = returnObj.indices[index].split(" ");
		var time = utils.convertMilitaryTime(entryDate[1].split(":"));				
		var date = app.buildDate(entryDate[0],"/");
		$("#" + theID + " .evalDate").text(date + " " + time);
	},
	setVitalSignField: function(returnObj, index, element, valueType){		
		if(returnObj.values[index][valueType] && returnObj.values[index][valueType].value != ""){
			$(element).text(returnObj.values[index][valueType].value);
		}
		else{
			$(element).parents("li").addClass("notEntered").find(".inputVal").eq(0).text("Not Entered");
		}
	},
	setDiagnosis: function(status){
		var diagnosisString = status.diagnosis_primary;
		if(diagnosisString.indexOf("Bipolar") > -1){app.diagnosis = "Bipolar";}
		else if(diagnosisString.indexOf("Schizophrenia") > -1){app.diagnosis = "Schizophrenia";}
		else if(diagnosisString.indexOf("Major Depressive Disorder") > -1){app.diagnosis = "Depression";}
		else{app.diagnosis = "Other";}
		app.secondaryDiagnosis = status.diagnosis_secondary;
	},
	setDropdownValAndSelected: function(elementID, ddVal){
		if(ddVal || ddVal == ""){
			$("#" + elementID + " li a").removeClass("selected");
			$("#" + elementID + " li a[href='#" + ddVal + "']").addClass("selected");
			$("#" + elementID + " li").each(function(){
				var theHref = $(this).find("a").attr("href").substring($(this).find("a").attr("href").indexOf("#") + 1, $(this).find("a").attr("href").length);
				if(theHref == ddVal){					
					var aText = $(this).find("a").text();
					$("#" + elementID + " .pullDown").attr("href","#" + ddVal).text(aText);
				}
			});			
		}
	},
	setMedicationTableHeight: function(){
		var minTableHeight = 188;
		if($("#instructions").hasClass("hide")){minTableHeight = 219;}
		var offsetTop = $("#chartWrapper").position().top + app.topAreaHeight + 45;
		var availableSpace = winHeight - offsetTop;	
		if(availableSpace <= innerTableHeight){
			if(winHeight <= app.minimumWindowHeight){
				$(".innerTable").css({"height":minTableHeight + "px"});
			}
			else{$(".innerTable").css({"height":availableSpace + "px"});}
		}
		else{$(".innerTable").css({"height":"auto"});}		
	},
	setScaleNameNav: function(){
		var scaleName, navName, pastScaleName;
		var navName = app.diagnosis;
		switch(app.diagnosis){
			case "Bipolar": scaleName = "Bipolar Scale"; pastScaleName = scaleName + 's'; break;
			case "Schizophrenia": scaleName = "Schizophrenia Scales"; pastScaleName = scaleName; break;
			case "Depression": scaleName = "Major Depressive Scale"; pastScaleName = scaleName + 's'; break;
			case "Other": scalename = "Diagnosis-Related Scale"; break;
		}
		
		$("#evalNav li#CRSNav a").attr("href","#CRS_" + navName).find("span").text(scaleName);
		$("#evalNav li#PastCRSNav a").attr("href","#PastCRS_" + navName).find("span").text(pastScaleName);
		$("#evalContent .eval").eq(0).attr("id", "CRS_" + navName);
		$("#evalContent .eval").eq(1).attr("id", "PastCRS_" + navName);
	},		
	setTabBoxHeight: function(){		
		winHeight = $(window).height();
		var paddingTop = $(".tabBox:not('.ui-tabs-hide')").css("paddingTop").replace("px","") * 1;
		var paddingBottom = $(".tabBox:not('.ui-tabs-hide')").css("paddingBottom").replace("px","") * 1;
		var totalPadding = paddingTop + paddingBottom;
		if(winHeight > app.minimumWindowHeight){
			$(".tabBox,#tabsOuterWrapper,#ajaxLoader").css("height", winHeight - app.topAreaHeight - totalPadding);
			$("#ajaxLoader").css("height", winHeight - app.topAreaHeight);
		}
		else{
			$(".tabBox:not('.ui-tabs-hide'),#tabsOuterWrapper").css("height", app.defaultTabHeight - totalPadding);
			$("#ajaxLoader").css("height", app.defaultTabHeight);
		}
		app.checkDropdownHeights("createFields");
		app.setMedicationTableHeight();
		if($(".tabBox:not('.ui-tabs-hide')").attr("id") == "medications"){$(".tabBox,#tabsOuterWrapper").css("height", "auto");}
	},
	buildInitialStagingObj: function(diagnosis){
		var initialStagingObj =  '{';
		initialStagingObj += '"patientid":"'+app.pID+'",';
		initialStagingObj += '"diagnosis":"'+diagnosis+'",';
		initialStagingObj += '"treatments":[';
		$("#ips input:checked").each(function(){
			initialStagingObj += '"' + $(this).val() + '",';
		});
		initialStagingObj = initialStagingObj.substring(0, initialStagingObj.length - 1);
		initialStagingObj += "]}";
		return initialStagingObj;
	},
	buildDiagnosisPatientObj: function(){
		var primaryDiagnosis = utils.getHref($("#primary_diagnosis .pullDown"));
		var stage = utils.getHref($("#stageDropdown .pullDown"));
		var secondaryDiagnosis =  $.trim($("#secondary_diagnoses").val());
		secondaryDiagnosis = secondaryDiagnosis.replace(/\n/g, "\\n");
		if(primaryDiagnosis == $("#diagnosis .content").text() && stage == $("#stage .content").text() && secondaryDiagnosis == app.secondaryDiagnosis){
			patientObj = false;
		}
		else{
			var patientQObj = '"diagnosis_primary":"' + primaryDiagnosis + '",';
			patientQObj += '"diagnosis_secondary":"' + utils.encode(secondaryDiagnosis,false,true) + '",';
			patientQObj += '"diagnosis_stage":"' + stage + '"';
			var patientObj =  '{';
			patientObj += '"patientid":"'+app.pID+'",';
			patientObj += '"status":{' + patientQObj;
			patientObj += "}}";
		}
		return patientObj;
	},
	buildPatientVitalSignsObj: function(){
		var evaluationObj = '';
		var heightFtVal = utils.getHref($(".eval:not('.ui-tabs-hide') #height #heightFt .pullDown"));
		var heightInVal = utils.getHref($(".eval:not('.ui-tabs-hide') #height #heightIn .pullDown"));
		var weightVal = $(".eval:not('.ui-tabs-hide') #weight input").val();
		var bmiVal = $(".eval:not('.ui-tabs-hide') #bmi .inputVal").text();
		var bloodPressureVal1 = $(".eval:not('.ui-tabs-hide') #blood_pressure input").eq(0).val();
		var bloodPressureVal2= $(".eval:not('.ui-tabs-hide') #blood_pressure input").eq(1).val();
		var heightRateVal = $(".eval:not('.ui-tabs-hide') #heart_rate input").val();		
		if(heightFtVal == "" && heightInVal == "0"){heightInVal = "";}
		evaluationObj += '"vital_signs_height_feet":"' + heightFtVal + '",';
		evaluationObj += '"vital_signs_height_inches":"' + heightInVal + '",';
		if(weightVal != ""){evaluationObj += '"vital_signs_weight":"' + weightVal + '",';}
		if(bmiVal != ""){evaluationObj += '"vital_signs_bmi":"' + bmiVal + '",';}
		if(bloodPressureVal1){evaluationObj += '"vital_signs_blood_pressure_top":"' + bloodPressureVal1 + '",';}
		if(bloodPressureVal2){evaluationObj += '"vital_signs_blood_pressure_bottom":"' + bloodPressureVal2 + '",';}
		if(heightRateVal != ""){evaluationObj += '"vital_signs_heart_rate":"' + heightRateVal + '",';}
		return evaluationObj;
	},
	buildSubstanceAbuseObj: function(){
		var evaluationObj = '';
		$(".eval:not('.ui-tabs-hide') .substanceAbuse .fieldWrapper").each(function(i){
			var theSubstance = $(this).find("label").text().toLowerCase();
			if(theSubstance.indexOf("other") >= 0){theSubstance = "other";}
			var abuseStatus = '';
			$(this).find("input").each(function(){
				if($(this).prop("checked")){
					abuseStatus += $(this).val() + "|";
				}
			});
			abuseStatus = abuseStatus.substring(0, abuseStatus.length - 1);			
			evaluationObj += '"substance_abuse_' + theSubstance + '":"' + abuseStatus + '",';			
		});
		return evaluationObj;
	},
	buildGlobalRatingScalesObj: function(){
		var evaluationObj = '';
		$(".eval:not('.ui-tabs-hide') .grsEval .fieldWrapper").each(function(i){
			var theScale = $(this).find("h2").text().toLowerCase().replace(/ /g, "_");
			var theSeverity = $(this).find("input:checked").val();
			if($(this).find("input:checked").attr("type") != "checkbox"){
				evaluationObj += '"global_rating_scale_' + theScale + '":"' + theSeverity + '",';
			}
			else{
				evaluationObj += '"global_rating_scale_' + theScale + '":"unable to assess",';
			}
		});
		return evaluationObj;
	},
	buildPatientEvalQObj: function(prefix, element, total, defaultText){
		var evaluationObj = '';
		for(var i=0; i< $(element).length; i++){
			var theValue = $.trim($(element).eq(i).val());
			if(theValue != "" && theValue != defaultText){
				evaluationObj += '"' + prefix + 'q' + (i+1) + '":"' + utils.encode(theValue) + '",';
			}
		}		
		if(total != ""){evaluationObj += '"' + prefix + 'total":"' + total + '",';}
		return evaluationObj;
	},
	buildPatientEvalObj: function(prefix, evalQObj, subscaleArray){
		var evaluationObj = '{';
		evaluationObj += '"patientid":"'+app.pID+'",';
		evaluationObj += '"status":{';
		evaluationObj += evalQObj;		
		if(subscaleArray){
			for(var i=0; i<subscaleArray.length; i++){
				evaluationObj += '"' + prefix + 'subscale' + subscaleArray[i][0] + '":"' + subscaleArray[i][1] + '",';
			}
		}		
		var theEvalDoctor = $("#utilityBar .welcome").html().replace(/&nbsp;/g, " ");
		theEvalDoctor = theEvalDoctor.replace("Welcome, ", "");
		evaluationObj += '"' + prefix + 'evaluatedby":"' + theEvalDoctor + '"';
		evaluationObj += "}}";
		return evaluationObj;
	},
	buildLabObj: function(date,labTestID){
		var labText = "";
		$("#labTestFields input").each(function(){
			if($(this).val() != ""){
				labText += utils.encode($(this).parent().find("label").attr("rel")) + "|" + utils.encode($(this).val()) + "||";
			}
		});	
		labText = labText.substring(0, labText.length - 2);		
		var labObj = '{';
		labObj += '"patientid":"'+app.pID+'",';
		labObj += '"labdate":"'+date+'",';
		labObj += '"labtext":"'+labText+'",';
		labObj += '"labtest":{"labtestid":"'+labTestID+'"}';
		labObj += "}";
		return labObj;
	},	
	buildLabSearchObj: function(page, pagecount, labTestID){
		var labSearchObj = '{';
		labSearchObj += '"patientid":"'+app.pID+'",';
		if(labTestID != ""){labSearchObj += '"labtest":{"labtestid":"'+labTestID+'"},';}
		labSearchObj += '"page":"'+page+'",';
		labSearchObj += '"pagecount":"'+pagecount+'"';
		labSearchObj += "}";
		return labSearchObj;
	},		
	buildPrescriptionSearchObj: function(shortform, currentOnly){		
		var prescriptionSearchObj = '{';		
		prescriptionSearchObj += '"patientid":"'+app.pID+'",';
		if(currentOnly){prescriptionSearchObj += '"discontinue":false,';}
		prescriptionSearchObj += '"shortform":'+shortform;
		prescriptionSearchObj += "}";
		return prescriptionSearchObj;
	},		
	buildPrescriptionObj: function(treatmentID){
		var theDoctor = $("#utilityBar .welcome").html().replace(/&nbsp;/g, " ").replace("Welcome, ","");
		var theDate = new Date();
		var theTime = utils.pad2(theDate.getHours()) + ":" + utils.pad2(theDate.getMinutes()) + ":" + utils.pad2(theDate.getSeconds()) + ".0";
		var entryDate = app.formatDate(theDate, theTime);
		var prescriptionSearchObj = '{';
		prescriptionSearchObj += '"patientid":"'+app.pID+'",';
		prescriptionSearchObj += '"treatmentid":"'+treatmentID+'",';
		prescriptionSearchObj += '"entrydate":"'+entryDate+'",';
		prescriptionSearchObj += '"dailydose":"0",';
		prescriptionSearchObj += '"duration":"0",';
		prescriptionSearchObj += '"discontinue":"true",';
		prescriptionSearchObj += '"doctorname":"'+theDoctor+'"';
		prescriptionSearchObj += "}";
		return prescriptionSearchObj;
	},	
	buildProgressNoteObj: function(noteText,tags){
		var progressNoteObj = '{';
		progressNoteObj += '"patientid":"'+app.pID+'",';
		progressNoteObj += '"notetext":"'+noteText+'","tags":['+tags+']}';
		return progressNoteObj;
	},
	buildProgressSearchNoteObj: function(page,pagecount,tags,searchText){
		var progressNoteObj = '{';
		progressNoteObj += '"patientid":"'+app.pID+'",';
		if(searchText != ""){progressNoteObj += '"searchtext":"'+searchText+'",';}
		progressNoteObj += '"page":"'+page+'","pagecount":"'+pagecount+'","tags":['+tags+']}';
		return progressNoteObj;
	},
	buildTagObj: function(){
		var tags = "";
		if($("#tagSelectList input:checked").length > 0){
			$("#tagSelectList input:checked").each(function(){
				tags += '{"progressnotetagid":' + $(this).val() + '},';
			});
			tags = tags.substring(0, tags.length - 1);
		}
		return tags;
	},
	checkDropdownHeights: function(elementID){
		var windowHeight = $(window).height();		
		$("#" + elementID).find(".dropdown").each(function(){
			var offsetTop = $(this).find("ul").offset().top;
			var ddHeight = $(this).find("ul").height() * 1 + 6;
			var windowHeight = $(window).height() * 1;
			if((windowHeight - offsetTop) < ddHeight){
				$(this).find("ul").addClass("showTop");
			}
			else{
				$(this).find("ul").removeClass("showTop");
			}
		});
	},
	calculateSubscale: function(array, subscale, index, element){
		for(var j=0; j<array.length; j++){
			if((index+1) == array[j]){
				subscale = (subscale * 1) + ($(element).val() * 1);
			}
		}
		return subscale;
	},
	calculateBMI: function(feet, inches, weight){
		var heightInInches = (feet * 12) + inches;
		var bmi = (weight / (heightInInches * heightInInches)) * 703;
		bmi = utils.roundNumber(bmi, 2)
		return bmi;
	},	
	openTab: function(e, tabIndex){
		e.preventDefault();
		$tabs.tabs("select", tabIndex);	
	},
	buildDate: function(dateObj, seperator){
		var date = dateObj.split("-");
		date = (date[1] * 1) + seperator + date[2] + seperator + date[0];
		return date;
	},
	gup: function(name){
		name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
		var regexS = "[\\?&]"+name+"=([^&#]*)";
		var regex = new RegExp( regexS );
		var results = regex.exec( window.location.href );
		if( results == null ){ return "";}
		else{ return results[1];}
	},
	reSortObj: function(obj, uniqueDate, obj2){
		var newObj = {};
		var indices = [];
		var values = [];
		indices = app.pushIndices(obj, indices);
		if(obj2){indices = app.pushIndices(obj2, indices);}
		indices = indices.sort(app.sortNumber); 
		if(obj2){
			indices = app.addDCDates(indices, obj2, "indices");
			obj2 = app.addDCDates(indices, obj2, "obj2");
			for(var props in obj2){
				obj2[props] = obj2[props].sort(app.sortMedsDate);
			}
		}
		indices = indices.sort(app.sortNumber); // sort again because of added d/c dates from meds
		indices = app.uniqueArray(indices); // remove duplicate entries
		if(uniqueDate){indices = app.removeDuplicateDates(indices);}// remove entries from the same date
		for(var i=0; i<indices.length; i++){
			var innerObj = {};
			innerObj = app.setObjValues(obj, innerObj, indices[i], uniqueDate);
			if(obj2){innerObj = app.setObjValues(obj2, innerObj, indices[i], uniqueDate);}			
			values.push(innerObj);
		}
		newObj.indices = indices;
		newObj.values = values;	
		if(obj2){newObj.medObj = obj2;}
		return newObj;
	},
	addDCDates: function(indices, obj2, returnType){
		var currentDateArray = indices[0].split(" ");		
		var currentDate = app.buildDate(currentDateArray[0],"/");
		currentDate = new Date(currentDate);	
		for(var props in obj2){
			var dcDateArray = obj2[props][0].entrydate.split(" ");
			var dcDate = app.buildDate(dcDateArray[0],"/");
			dcDate = app.addDays(dcDate, obj2[props][0].duration);
			if (obj2[props][0].treatment.details.DurationOfAction > 1) {
				dcDate = app.addDays(dcDate, +obj2[props][0].treatment.details.DurationOfAction);
			}
			if(dcDate < currentDate && obj2[props][0].discontinue == false){
				var dcDateFormatted = app.formatDate(dcDate, dcDateArray[1]);
				if(returnType == "indices"){indices.push(dcDateFormatted);}
				else{
					var newDrugEntry = {};	
					newDrugEntry.duration = 0;
					newDrugEntry.discontinue = true;
					newDrugEntry.dailydose = "EXP";
					newDrugEntry.entrydate = dcDateFormatted;
					newDrugEntry.treatment = obj2[props][0].treatment;
					obj2[props].push(newDrugEntry);
				}												
			}
			if(obj2[props].length > 1){
				for(var i=0; i<obj2[props].length - 1; i++){
					var dp0Dose = obj2[props][i].dailydose;
					var dp1Dose = obj2[props][i + 1].dailydose;
					var dp0EntrydateArray = obj2[props][i].entrydate.split(" ");
					var dp0Entrydate = new Date(app.buildDate(dp0EntrydateArray[0],"/"));
					var dp1DcDateArray = obj2[props][i + 1].entrydate.split(" ");
					var dp1DcDate = app.buildDate(dp1DcDateArray[0],"/");
					dp1DcDate = app.addDays(dp1DcDate, obj2[props][i + 1].duration);
					if (obj2[props][i+1].treatment.details.DurationOfAction > 1) {
						dp1DcDate = app.addDays(dp1DcDate, +obj2[props][i+1].treatment.details.DurationOfAction);
					}
					if((dp0Dose > 0 && dp1Dose > 0) && (dp0Entrydate > dp1DcDate)){
						var dp1DcDateFormatted = app.formatDate(dp1DcDate, dp1DcDateArray[1]);
						if(returnType == "indices"){indices.push(dp1DcDateFormatted);}
						else{
							var newDrugEntry2 = {};	
							newDrugEntry2.duration = 0;
							newDrugEntry2.discontinue = true;
							newDrugEntry2.dailydose = "EXP";
							newDrugEntry2.entrydate = dp1DcDateFormatted;
							newDrugEntry2.treatment = obj2[props][i].treatment;
							obj2[props].push(newDrugEntry2);
						}										
					}
				}
			}
		}
		if(returnType == "indices"){return indices;}
		else{return obj2;}
	},
	formatDate: function(theDate, theTime){
		theDate = theDate.getFullYear() + "-" + utils.pad2(theDate.getMonth() + 1) + "-" + utils.pad2(theDate.getDate()) + " " + theTime;
		return theDate;
	},
	pushIndices: function(obj, indices){
		for(var props in obj){
			for(var i=0; i< obj[props].length; i++){				
				indices.push(obj[props][i].entrydate);
			}
		}	
		return indices;
	},
	setObjValues: function(obj, innerObj, indices, uniqueDate){
		for(var props in obj){
			for(var j=obj[props].length-1; j>=0; j--){
				if(uniqueDate){
					var date = obj[props][j].entrydate.split(" ");
					var dateIndices = indices.split(" ");
					if(date[0] == dateIndices[0]){
						innerObj[props] = obj[props][j];
					}
				}
				else{
					if(obj[props][j].entrydate == indices){
						innerObj[props] = obj[props][j];
					}
				}
			}
		}
		return innerObj;
	},
	removeDuplicateDates: function(array){
		for(var i=0; i<array.length;i++){
			var dateTime = array[i].split(" ");
			for(var j=0; j<array.length; j++){
				var compareDateTime = array[j].split(" ");
				if(i != j && dateTime[0] == compareDateTime[0]){						
					array.splice(j,1);
					app.removeDuplicateDates(array);
				}
			}
		}
		return array;
	},
	uniqueArray: function(arrayName){
		var newArray=new Array();
		label:for(var i=0; i<arrayName.length;i++ ){  
			for(var j=0; j<newArray.length;j++ ){
				if(newArray[j]==arrayName[i]) {
					continue label;
				}
			}
			newArray[newArray.length] = arrayName[i];
		}
		return newArray;
	},
	sortMedsDate: function(a,b){
		var dateRE = /^(\d{2})[\/\- ](\d{2})[\/\- ](\d{4})/;
		a = a.entrydate.replace(dateRE,"$1$2$3");
		b = b.entrydate.replace(dateRE,"$1$2$3");
		if (a>b) return -1;
		if (a<b) return 1;
		return 0;
	},
	sortNumber: function(a,b){
		var dateRE = /^(\d{2})[\/\- ](\d{2})[\/\- ](\d{4})/;
		a = a.replace(dateRE,"$1$2$3");
		b = b.replace(dateRE,"$1$2$3");
		if (a>b) return -1;
		if (a<b) return 1;
		return 0;
	}
}
