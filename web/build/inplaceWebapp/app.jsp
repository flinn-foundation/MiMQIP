<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
	<title>Flinn Foundation</title>
	<link type="text/css" rel="Stylesheet" href="css/global.css" />
	<link type="text/css" rel="Stylesheet" href="css/app.css" />
	<link type="text/css" rel="Stylesheet" href="css/colorbox.css" />
	<!--<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6/jquery.min.js"></script>-->
	<script type="text/javascript" src="js/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="js/globalUtils.js"></script>
	<script type="text/javascript" src="js/jquery.ui.tabs.min.js"></script>		
	<script type="text/javascript" src="js/jHtmlArea-min.js"></script>
	<script type="text/javascript" src="js/app.js"></script>
</head>
<!--[if IE 7 ]><body class="ie7"><![endif]-->
<!--[if IE 8 ]><body class="ie8"><![endif]-->
<!--[if IE 9 ]><body class="ie9"><![endif]-->
<!--[if !IE]><!--><body><!--<![endif]-->
<div id="container">
	<div id="utilityBarWrapper">
		<div id="utilityBar">
			<h1></h1>
			<span class="welcome"></span>	
			<a href="#" id="logoutBtn" class="roundedBtn">Logout</a>
			<div class="buttons">
				<a href="/patient-search.jsp" id="patientSearchBtn" class="roundedBtn">Patient Search</a>							
				<a href="/admin/" id="adminBtn" class="roundedBtn">Administration</a>
			</div>
		</div>
	</div>
	<div id="instructionsWrapper">
		<div id="instructions">
			<p><strong class="heading">Instructions:</strong> For MiMQIP to provide the best recommendation, it is important that all patient information be updated. <strong class="pleaseRemember">Please remember to do the following:</strong> <a href="#" class="btn" id="hideInstructions"><span>Hide</span></a></p>
			<ul>
				<li id="firstStep"><span>Verify diagnosis &amp; stage</span>  <a href="#">Update</a></li>
				<li id="secondStep"><span>Verify current medications</span>  <a href="#">Update</a></li>
				<li id="thirdStep"><span>Complete diagnosis-related scale</span> <a href="#">Go</a></li>
				<li id="fourthStep"><span>Complete global rating scales</span> <a href="#">Go</a></li>
			</ul>
		</div>
	</div>
	<div id="topWrapper">
		<div id="top">			
			<div id="boxes">
				<div id="boxInformation" class="box">
					<div class="headingBar">
						<h2>Patient Information</h2>
					</div>
					<div class="fieldBox" id="first">
						<span class="title">First</span>
						<span class="content"></span>
					</div>
					<div class="fieldBox" id="last">
						<span class="title">Last</span>
						<span class="content"></span>
					</div>
					<div class="fieldBox" id="id">
						<span class="title">ID #</span>
						<span class="content"></span>
					</div>
					<div class="fieldBox" id="sex">
						<span class="title">Sex</span>
						<span class="content"></span>
					</div>
					<div class="fieldBox" id="age">
						<span class="title">Age</span>
						<span class="content"></span>
					</div>
					<div class="fieldBox" id="dob">
						<span class="title">Dob</span>
						<span class="content"></span>
					</div>
				</div>
				<div id="boxDiagnosisStage" class="box">
					<div class="headingBar">
						<h2>Diagnosis &amp; Stage</h2>
					</div>
					<div class="floatWrapper">
						<div class="fieldBox" id="diagnosis">
							<span class="title">Primary Diagnosis</span>
							<span class="content"></span>
						</div>
						<div class="fieldBox" id="stage">
							<span class="title">Stage</span>
							<span class="content"></span>
						</div>
						<a href="#" id="updateViewPast" class="btn"><span>Update / View Past</span></a>
					</div>
				</div>
				<div id="boxMedications" class="box">
					<div class="headingBar">
						<h2>Current Medications<span></span></h2>
					</div>
					<div class="fieldBox" id="meds">
						<span class="content">
							<!--Bupropion:     60mg<br />
							Fluphenazine Decanoate:   30mg<br />
							Tranylcypromine:     60mg<br />
							Lorazepam:   30mg<br />
							Fluoxetine:     60mg<br />
							Zolpidem:   30mg<br />
							Venlafaxine:     60mg-->
						</span>
					</div>
				</div>
				<div id="boxNotes" class="box">
					<div class="headingBar">
						<h2>Progress Notes</h2>
						<!--<a href="#" id="searchNotesBtn" class="btn"><span>Search Notes</span></a>-->
						<a href="#" id="createNoteBtn" class="btn"><span>Create Note</span></a>
					</div>
					<div class="noteInfoBar">
						<span class="date"></span>
						<span class="time"></span>
						<span class="author"></span>
					</div>
					<div class="note"></div>
				</div>
				<div id="createProgressNote">
					<div id="selectTags">
						<h3>Select Tag(s)</h3>
						<div id="tagSelectList">
							<ul></ul>
						</div>
					</div>
					<div id="writeNote">
						<div id="topToolBar">
							<h3>Progress Notes</h3>
							<a href="#" class="disabled" id="saveNote">Save</a>
							<a href="#" id="cancelNote">Cancel</a>
						</div>
						<textarea id="noteTextarea" cols="10" rel="Enter text..." rows="10">Enter text...
						</textarea>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="tabsOuterWrapper">
	<div id="tabs">
		<div id="ajaxLoader"><img src="images/global/ajax-loader.gif" alt="" /></div>
		<div id="tabWrapper">
			<ul>
				<li id="tabEvaluations">
					<a href="#evaluations"><span>Patient Evaluations</span></a>
				</li>				
				<li id="tabMedications" class="on">
					<a href="#medications"><span>Medications</span></a>
				</li>
				<li id="tabRecommendations">
					<a href="#recommendations"><span>Decision Support</span></a>
				</li>
				<li id="tabProgress">
					<a href="#progress"><span>Progress Notes</span></a>
				</li>
				<li id="tabLabs">
					<a href="#labs"><span>Patient Labs</span></a>
				</li>				
				<li id="tabInformation">
					<a href="#information"><span>Patient Information</span></a>
				</li>
				<li id="tabReports">
					<a href="#reports"><span>Reports</span></a>
				</li>
			</ul>
		</div>
		<div id="evaluations" class="tabBox">			
			<div id="evalNav">
				<ul>
					<li><h3>Required Evaluations</h3></li>
					<li id="CRSNav"><a href="#CRS">Enter New <span class="upper">Diagnosis-Related Scale</span></a></li>
					<li id="PastCRSNav" class="pastEval"><a href="#PastCRS">View Past <span>Diagnosis-Related Scales</span></a></li>
					<li><a href="#global_rating_scale">Enter New <span class="upper">Global Rating Scale</span></a></li>
					<li class="pastEval"><a href="#Pastglobal_rating_scale">View Past Global <span>Rating Scales</span></a></li>
					
					<li><h3>Additional Evaluations</h3></li>
					<li><a href="#psychiatric_evaluation">Enter New <span class="upper">Psychiatric Evaluation</span></a></li>
					<li class="pastEval"><a href="#Pastpsychiatric_evaluation">View Past <span>Psychiatric Evaluation</span></a></li>
					<li><a href="#mental_status">Enter New <span class="upper">Mental Status</span></a></li>
					<li class="pastEval"><a href="#Pastmental_status">View Past <span>Mental Status</span></a></li>
					<li><a href="#substance_abuse">Enter New <span class="upper">Substance Abuse Assessment</span></a></li>
					<li class="pastEval"><a href="#Pastsubstance_abuse">View Past <span>Substance Abuse Assessment</span></a></li>
					<li><a href="#vital_signs">Enter New <span class="upper">Patient Vital Signs</span></a></li>
					<li class="pastEval"><a href="#Pastvital_signs">View Past <span>Patient Vital Signs</span></a></li>
				</ul>
			</div>

			<div id="evalContent">
				<div id="CRS" class="eval"><div class="content"></div></div>
				<div id="PastCRS" class="past eval"><div class="content"></div></div>
				<div id="global_rating_scale" class="eval"><div class="content"></div></div>
				<div id="Pastglobal_rating_scale" class="past eval"><div class="content"></div></div>
				<div id="psychiatric_evaluation" class="eval"><div class="content"></div></div>
				<div id="Pastpsychiatric_evaluation" class="past eval"><div class="content"></div></div>
				<div id="mental_status" class="eval"><div class="content"></div></div>
				<div id="Pastmental_status" class="past eval"><div class="content"></div></div>
				<div id="substance_abuse" class="eval"><div class="content"></div></div>
				<div id="Pastsubstance_abuse" class="past eval"><div class="content"></div></div>		
				<div id="vital_signs" class="eval"><div class="content"></div></div>
				<div id="Pastvital_signs" class="past eval"><div class="content"></div></div>
			</div>
		</div>
		<div id="medications" class="tabBox">
			<div class="floatHolder">
				<a href="#" class="btn" id="discontinue"><span>Discontinue</span></a>	
				<a href="#" class="btn" id="prescribe"><span>ePrescribe</span></a>
				<h3>Manage Medications:</h3>				
			</div>
			<div id="graphWrapper">
				<div id="key">
					<div class="dropdown" id="scales">
						<a href="#" class="pullDown"></a>
						<ul></ul>
					</div>
					<div id="keyWrapper">
						<ul>
							<li class="brown"></li>
							<li class="purple"></li>
							<li class="red"></li>
						</ul>
					</div>
				</div>
				<div id="graph">					
					<ul>
						<li></li>
						<li></li>
						<li></li>
						<li></li>
						<li></li>
						<li></li>
						<li></li>
						<li></li>
					</ul>
				</div>			
			</div>
			<div id="chartWrapper">
				<a href="#" class="previous navArrow"><<</a>
				<div class="outerTable">
					<div class="innerTable">
						<table>
							<thead>
							<tr class="dateRow">
								<th class="appt">
									<span class="week">Stage:</span>
									<span class="date">Date:</span>
								</th>
								<th>
									<span class="week"></span>
									<span class="stage"></span>
									<span class="date"></span>
								</th>
								<th>
									<span class="week"></span>
									<span class="stage"></span>
									<span class="date"></span>
								</th>
								<th>
									<span class="week"></span>
									<span class="stage"></span>
									<span class="date"></span>
								</th>
								<th>
									<span class="week"></span>
									<span class="stage"></span>
									<span class="date"></span>
								</th>
								<th>
									<span class="week"></span>
									<span class="stage"></span>
									<span class="date"></span>
								</th>
								<th>
									<span class="week"></span>
									<span class="stage"></span>
									<span class="date"></span>
								</th>
								<th>
									<span class="week"></span>
									<span class="stage"></span>
									<span class="date"></span>
								</th>
								<th>
									<span class="week"></span>
									<span class="stage"></span>
									<span class="date"></span>
								</th>
							</tr>
							</thead>			
							<tbody>
							<!--<tr class="current">
								<td class="heading">Buproprion</td>
								<td></td>
								<td>30</td>
								<td>30</td>
								<td>30</td>
								<td>30</td>
								<td>30</td>
								<td>30</td>
								<td>30</td>
							</tr>
							<tr class="odd current">
								<td class="heading">Venlafaxine</td>
								<td></td>
								<td></td>
								<td>15</td>
								<td>15</td>
								<td>15</td>
								<td>15</td>
								<td>15</td>
								<td>15</td>
							</tr>
							<tr class="discontinuedDivider">
								<td colspan="9">Discontinued Medications <img src="images/graph/downArrow.png" alt="" /></td>
							</tr>
							<tr class="discontinued">
								<td class="heading">Lorazepam</td>
								<td>20</td>
								<td>D/C</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
							<tr class="discontinued odd">
								<td class="heading">Fluoxetine</td>
								<td>20</td>
								<td>D/C</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
							<tr class="discontinued">
								<td class="heading">Zolpidem</td>
								<td>10</td>
								<td>D/C</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
							<tr class="discontinued odd">
								<td class="heading">Fluoxetine</td>
								<td>20</td>
								<td>D/C</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
							<tr class="discontinued">
								<td class="heading">Zolpidem</td>
								<td>10</td>
								<td>D/C</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>-->
							</tbody>
						</table>
					</div>
				</div>
				<a href="#" class="next navArrow">>></a>
			</div>
			<div id="noMedications" class="noresults"><p>No medications have been added for this patient.</p><p>Click the ePrescribe button above to add a medication.</p></div>
			<div id="longMedications" class="longresults"><p>* Treatment was last administered on the date indicated in <span style="color:red;">red</span> next to the drug name to the left.</p></div>
		</div>
		<div id="recommendations" class="tabBox">
			<div class="floatLeft">
				<h2 class="conditionalHeading hide" id="generalMessagesHdr">General Messages</h2>
				<div class="generalmessages"></div>
				<h2 class="conditionalHeading hide" id="guidelineConsistencyHdr">Guideline Consistency</h2>
				<div class="generalconsistency"></div>
				<div class="additionalconsistency"></div>
				<h2 class="conditionalHeading hide" id="clinicalResponseHdr">Clinical Response and Treatment Recommendations</h2>
				<div class="treatmentmessages"></div>
				<div class="specialmessages"></div>
				<div class="clinicalresponse"></div>
				<div class="medicationresponse"></div>
			</div>
			<div class="floatLeft right">
				<div class="treatmentTableHolder">
					<h2>Current Guideline Stage Treatment Table</h2>
					<table class="guidelinechart">
						<thead>
						<tr>
							<th class="narrow odd" scope="col">Stage</th>
							<th scope="col">Drug 1</th>
							<th class="odd" scope="col">Drug 2</th>
							<th scope="col">Drug 3</th>
						</tr>
						</thead>
						<tbody></tbody>
					</table>
					<a href="MQIP_Treatment_Tables.pdf" target="_blank" id="treatmentTableLink">Open treatment table for all stages (pdf)</a>
					<div class="notes"></div>
				</div>
				<h2 class="conditionalHeading hide" id="otherInfoHdr">Other Information</h2>
				<div class="othermessages"></div>	
			</div>
		</div>
		<div id="progress" class="tabBox">
			<div id="searchBar">
				<h2>Search Notes</h2>
				<div id="searchBoxesWrapper">
					<div class="fieldWrapper" id="searchKeywords">
						<form action="" id="keywordSearchForm">
							<p>Enter a search term:</p>
							<input type="text" disabled="disabled" rel="Enter keyword(s)" id="noteKeywords" class="greenInput" value="Enter keyword(s)" />
							<input type="submit" class="orangeBtn" id="searchNotesKeyword" value="Search" disabled="disabled" />
						</form>					
					</div>					
					<div class="fieldWrapper" id="tagHolder">
						<p>Select a tag to filter:</p>
						<div class="dropdown disabled" id="tagDropdown">
							<a href="#" class="pullDown">Select</a>
							<ul></ul>
						</div>
					</div>
					<a href="#" class="clearNoteSearch">Clear All</a>
					<div class="clear"></div>
				</div>
			</div>
			<div id="searchContent">
				<div id="noteSamples">
					<div class="pagination topPagination"></div>
					<ul></ul>
					<div class="pagination bottomPagination"></div>
				</div>
				<div id="noteContent"></div>				
			</div>
			<div id="noNotesWritten" class="noresults"><p>No progress notes have been written for this patient.</p><p>Click the Create Note button in the upper right to create a note.</p></div>
			<div id="noNotesSearch" class="noresults">Your search did not match any progress notes. <a href="#" class="clearNoteSearch">Clear your search</a></div>
		</div>
		<div id="labs" class="tabBox">
			<div id="labsTopBar">
				<div id="floatWrapper">
					<div class="fieldWrapper">
						<p>View by test:</p>
						<div class="dropdown disabled" id="labTestsDropdown">
							<a href="#" class="pullDown">View All</a>
							<ul></ul>
						</div>
					</div>
					<div class="pagination topPagination"></div>
					<div class="orangeBtnContainer"><a href="#" id="enterLabTests" class="orangeBtn">Enter Labs</a></div>
					<div class="clear"></div>
				</div>
			</div>
			<table>
				<thead>
					<tr>
						<th class="labName">Laboratory Test Name</th>
						<th class="date">Date</th>
						<th class="results">Results</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
			<div class="pagination bottomPagination"></div>
			<div id="noLabsWritten" class="noresults"><p>No lab test results have been entered for this patient.</p><p>Click the Enter Labs button above to enter a lab test result.</p></div>
			<div id="noLabsSearch" class="noresults">There are no results for that test. <a href="#" class="showAllLabs">Show all lab test results</a></div>

		</div>
		<div id="information" class="tabBox">
			<div id="displayPatientInfo">
				<h2>Patient Information: </h2>
				<div id="displayInfo" class="patientForm">
					<div class="floatLeft">
						<div class="fieldWrapper">
							<label for="fNameText">First Name:</label>
							<span id="fNameText" class="infoText"></span>
						</div>
						<div class="fieldWrapper">
							<label for="lNameText">Last Name:</label>
							<span id="lNameText" class="infoText"></span>
						</div>
						<div class="fieldWrapper">
							<label for="raceText">Race:</label>
							<span id="raceText" class="infoText"></span>
						</div>
						<div class="fieldWrapper">
							<label for="ethnicityText">Ethnicity:</label>
							<span id="ethnicityText" class="infoText"></span>
						</div>
						<div class="fieldWrapper">
							<label for="pcpnText">Primary Care Physician Name:</label>
							<span id="pcpnText" class="infoText paddingTop"></span>
						</div>
						<div class="fieldWrapper">
							<label for="pcpeText">Primary Care Physician Email:</label>
							<span id="pcpeText" class="infoText paddingTop"></span>
						</div>	
					</div>
					<div class="floatLeft">						
						<div class="fieldWrapper">
							<label for="patientIdText">ID #:</label>
							<span id="patientIdText" class="infoText"></span>
						</div>
						<div class="fieldWrapper">
							<label for="zipCodeText">ZIP Code:</label>
							<span id="zipCodeText" class="infoText"></span>
						</div>
						<div class="fieldWrapper">
							<label for="sexText">Sex:</label>
							<span id="sexText" class="infoText"></span>
						</div>
						<div class="fieldWrapper">
							<label for="dobText">Date of Birth:</label>
							<span id="dobText" class="infoText"></span>
						</div>
						
						<div class="fieldWrapper">
							<label for="maritalText">Marital Status:</label>
							<span id="maritalText" class="infoText"></span>
						</div>
						<div class="fieldWrapper">
							<label for="employmentText" class="noPadding">Employment Status:</label>
							<span id="employmentText" class="infoText"></span>
						</div>
						<div class="fieldWrapper">
							<label for="livingText" class="noPadding">Living Arrangement:</label>
							<span id="livingText" class="infoText"></span>
						</div>
					</div>
					<a href="#" id="editBtn" class="orangeBtn">Edit</a>					
				</div>					
			</div>
			<div id="editPatientInfo">
				<h2>Edit Patient Information: </h2>
				<div id="createFields" class="patientForm">
					<jsp:include page="/inc/patientForm.jsp" />					
					<a href="#" id="closeEditPatient">Cancel</a><input type="submit" value="Save" class="orangeBtn" id="savePatient" />
					<div class="clear"></div>
				</div>
			</div>
		</div>
		<div id="reports" class="tabBox">Reports</div>
	</div>
	</div>	
</div>
</body>
</html>
