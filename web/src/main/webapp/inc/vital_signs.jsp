<form action="" class="patientVitalSigns">
<h1>Patient Vital Signs</h1>
<div class="evalNav">
	<a class="prevEval" href="#"></a>
	<span class="evalDate"></span>
	<a class="nextEval" href="#"></a>
</div>
<ul class="vitalSigns">
	<li class="fieldWrapper symptom" id="height">
		<label for="appearance">Height:</label>
		<div class="dropdown" id="heightFt">
			<a href="#" class="pullDown">Select</a>
			<ul>
				<li class="odd"><a href="#" class="selected">Select</a></li>
				<li><a href="#1">1</a></li>
				<li class="odd"><a href="#2">2</a></li>
				<li><a href="#3">3</a></li>
				<li class="odd"><a href="#4">4</a></li>
				<li><a href="#5">5</a></li>
				<li class="odd"><a href="#6">6</a></li>
			</ul>
		</div>
		<span class="inputVal"></span>
		<span class="labelText marginRight">ft</span>
		<div class="dropdown" id="heightIn">
			<a href="#0" class="pullDown">0</a>
			<ul>
				<li class="odd"><a href="#0" class="selected">0</a></li>
				<li><a href="#1">1</a></li>
				<li class="odd"><a href="#2">2</a></li>
				<li><a href="#3">3</a></li>
				<li class="odd"><a href="#4">4</a></li>
				<li><a href="#5">5</a></li>
				<li class="odd"><a href="#6">6</a></li>
				<li><a href="#7">7</a></li>
				<li class="odd"><a href="#8">8</a></li>
				<li><a href="#9">9</a></li>
				<li class="odd"><a href="#10">10</a></li>
				<li><a href="#11">11</a></li>
			</ul>
		</div>
		<span class="inputVal heightIn"></span>
		<span class="labelText">in</span>		
	</li>
	<li class="fieldWrapper symptom" id="weight">
		<label for="motorActivity">Weight:</label>
		<input class="greenInput" type="text" maxlength="3" />
		<span class="inputVal"></span>
		<span class="labelText">lbs</span>
		
	</li>
	<li class="fieldWrapper symptom"id="bmi">
		<label for="motorActivity">BMI:</label>
		<span class="inputVal"></span>
	</li>
	<li class="fieldWrapper symptom" id="blood_pressure">
		<label for="motorActivity">Blood Pressure:</label>
		<input class="greenInput" type="text" maxlength="3" /><span class="inputVal"></span> 
		<span class="labelText">/</span> 
		<input class="greenInput" type="text" maxlength="3" /><span class="inputVal"></span>
		
	</li>
	<li class="fieldWrapper symptom" id="heart_rate">
		<label for="motorActivity">Heart Rate:</label>
		<input class="greenInput" type="text" maxlength="3" />
		<span class="inputVal"></span>
		<span class="labelText">bpm</span> 		
	</li>
	<li class="clear"></li>
</ul>
<div class="errorMsg" style="display:none">Please enter data in one or more of the fields.</div>
<div class="evaluatedBy">Evaluated By: <span></span></div>
<div class="buttons bottom">
	<input type="submit" class="orangeBtn" value="Save" />
</div>
</form>