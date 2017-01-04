<div id="colorboxForm" class="diagnosisStage">
	<h1><a href="#updateDiagnosis" class="update active">Update Diagnosis &amp; Stage</a><span class="line"></span><a href="#viewPastDiagnosis" class="viewPast">View Past</a></h1>
	<div id="updateDiagnosis" class="diagnosisStageContainer show">
	<form action="" class="diagnosis">	
	<ul>
		<li class="fieldWrapper symptom">
			<label>Primary DSM Axis 1 Diagnosis:</label>
			<div class="dropdown" id="primary_diagnosis">
				<a href="#" class="pullDown"></a>
				<ul>
					<li class="odd"><a href="#296.21 Major Depressive Disorder, Single Episode, Mild">296.21 Major Depressive Disorder, Single Episode, Mild</a></li>
					<li><a href="#296.22 Major Depressive Disorder, Single Episode, Moderate">296.22 Major Depressive Disorder, Single Episode, Moderate</a></li>
					<li class="odd"><a href="#296.23 Major Depressive Disorder, Single Episode, Severe w/o Psychotic Features">296.23 Major Depressive Disorder, Single Episode, Severe w/o Psychotic Features</a></li>
					<li><a href="#296.31 Major Depressive Disorder, Recurrent, Mild">296.31 Major Depressive Disorder, Recurrent, Mild</a></li>
					<li class="odd"><a href="#296.32 Major Depressive Disorder, Recurrent, Moderate">296.32 Major Depressive Disorder, Recurrent, Moderate</a></li>
					<li><a href="#296.33 Major Depressive Disorder, Recurrent, Severe w/o Psychotic Features">296.33 Major Depressive Disorder, Recurrent, Severe w/o Psychotic Features</a></li>				
					<li class="odd"><a href="#296.24 Major Depressive Disorder, Single Episode, Severe w/Psychotic Features">296.24 Major Depressive Disorder, Single Episode, Severe w/Psychotic Features</a></li>
					<li><a href="#296.34 Major Depressive Disorder, Recurrent, Severe w/Psychotic Features">296.34 Major Depressive Disorder, Recurrent, Severe w/Psychotic Features</a></li>
					<li class="odd"><a href="#296.01 Bipolar I Disorder, Single Manic Episode, Mild">296.01 Bipolar I Disorder, Single Manic Episode, Mild</a></li>
					<li><a href="#296.02 Bipolar I Disorder, Single Manic Episode, Moderate">296.02 Bipolar I Disorder, Single Manic Episode, Moderate</a></li>
					<li class="odd"><a href="#296.03 Bipolar I Disorder, Single Manic Episode, Severe w/o psychotic Features">296.03 Bipolar I Disorder, Single Manic Episode, Severe w/o psychotic Features</a></li>
					<li><a href="#296.04 Bipolar I Disorder, Single Manic Episode, Severe w/Psychotic Features">296.04 Bipolar I Disorder, Single Manic Episode, Severe w/Psychotic Features</a></li>
					<li class="odd"><a href="#296.40 Bipolar I Disorder, Most Recent Episode Hypomanic">296.40 Bipolar I Disorder, Most Recent Episode Hypomanic</a></li>
					<li><a href="#296.41 Bipolar I Disorder, Most Recent Episode Manic, Mild">296.41 Bipolar I Disorder, Most Recent Episode Manic, Mild</a></li>
					<li class="odd"><a href="#296.42 Bipolar I Disorder, Most Recent Episode Manic, Moderate">296.42 Bipolar I Disorder, Most Recent Episode Manic, Moderate</a></li>
					<li><a href="#296.43 Bipolar I Disorder, Most Recent Episode Manic, Severe w/o Psychotic Features">296.43 Bipolar I Disorder, Most Recent Episode Manic, Severe w/o Psychotic Features</a></li>
					<li class="odd"><a href="#296.44 Bipolar I Disorder, Most Recent Episode Manic, Severe w/Psychotic Features">296.44 Bipolar I Disorder, Most Recent Episode Manic, Severe w/Psychotic Features</a></li>
					<li><a href="#296.51 Bipolar I Disorder, Most Recent Episode Depressed, Mild">296.51 Bipolar I Disorder, Most Recent Episode Depressed, Mild</a></li>
					<li class="odd"><a href="#296.52 Bipolar I Disorder, Most Recent Episode Depressed, Moderate">296.52 Bipolar I Disorder, Most Recent Episode Depressed, Moderate</a></li>
					<li><a href="#296.53 Bipolar I Disorder, Most Recent Episode Depressed, Severe w/o Psychotic Features">296.53 Bipolar I Disorder, Most Recent Episode Depressed, Severe w/o Psychotic Features</a></li>
					<li class="odd"><a href="#296.54 Bipolar I Disorder, Most Recent Episode Depressed, Severe w/Psychotic Features">296.54 Bipolar I Disorder, Most Recent Episode Depressed, Severe w/Psychotic Features</a></li>
					<li><a href="#295.30 Schizophrenia, Paranoid Type">295.30 Schizophrenia, Paranoid Type</a></li>
					<li class="odd"><a href="#295.10 Schizophrenia, Disorganized Type">295.10 Schizophrenia, Disorganized Type</a></li>
					<li><a href="#295.20 Schizophrenia, Catatonic Type">295.20 Schizophrenia, Catatonic Type</a></li>
					<li class="odd"><a href="#295.90 Schizophrenia, Undifferentiated Type">295.90 Schizophrenia, Undifferentiated Type</a></li>
					<li><a href="#295.60 Schizophrenia, Residual Type">295.60 Schizophrenia, Residual Type</a></li>
					<li class="odd"><a href="#Other">Other</a></li>
				</ul>
			</div>
		</li>
		<li class="fieldWrapper symptom">
			<label>Secondary and Other Diagnoses:</label>
			<textarea id="secondary_diagnoses"></textarea>		
		</li>
	</ul>
	<ul class="stage">
		<li class="fieldWrapper symptom">
			<label>Stage:</label>
			<div class="dropdown" id="stageDropdown">
				<a href="#" class="pullDown">1</a>
				<ul>
					<li class="odd"><a href="#1">1</a></li>
					<li><a href="#2">2</a></li>
					<li class="odd"><a href="#3">3</a></li>
					<li><a href="#4">4</a></li>
				</ul>
			</div>
		</li>
	</ul>
	<div class="errorMsg">Please update the diagnosis or stage.</div>
	<input type="submit" class="orangeBtn" value="Save" />
	</form>
	</div>
	<div id="viewPastDiagnosis" class="diagnosisStageContainer">
		<table>
			<thead>
				<th class="date">Date</th>
				<th class="primaryDiagnosis">Primary Diagnosis</th>
				<th class="secondaryDiagnoses">Secondary &amp; Other Diagnoses</th>
				<th class="stage">Stage</th>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
</div>