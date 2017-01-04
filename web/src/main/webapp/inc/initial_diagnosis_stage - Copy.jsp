<div id="colorboxForm" class="initialDiagnosisStage">
	<h1>Diagnosis &amp; Stage</h1>
	<div class="diagnosisStageContainer show">
		<p class="instructions secondaryHeading">Please select the patient's diagnosis. <strong>Primary diagnosis and stage are required.</strong></p>
		<form action="" class="diagnosis">	
		<ul>
			<li class="fieldWrapper symptom">
				<label>Primary DSM Axis 1 Diagnosis:</label>
				<div class="dropdown" id="primary_diagnosis">
					<a href="#" class="pullDown">Select</a>
					<ul>
						<li class="odd"><a href="#" class="selected">Select</a></li>
						<li><a href="#296.21 Major Depressive Disorder, Single Episode, Mild">296.21 Major Depressive Disorder, Single Episode, Mild</a></li>
						<li class="odd"><a href="#296.22 Major Depressive Disorder, Single Episode, Moderate">296.22 Major Depressive Disorder, Single Episode, Moderate</a></li>
						<li><a href="#296.23 Major Depressive Disorder, Single Episode, Severe w/o Psychotic Features">296.23 Major Depressive Disorder, Single Episode, Severe w/o Psychotic Features</a></li>
						<li class="odd"><a href="#296.31 Major Depressive Disorder, Recurrent, Mild">296.31 Major Depressive Disorder, Recurrent, Mild</a></li>
						<li><a href="#296.32 Major Depressive Disorder, Recurrent, Moderate">296.32 Major Depressive Disorder, Recurrent, Moderate</a></li>
						<li class="odd"><a href="#296.33 Major Depressive Disorder, Recurrent, Severe w/o Psychotic Features">296.33 Major Depressive Disorder, Recurrent, Severe w/o Psychotic Features</a></li>				
						<li><a href="#296.24 Major Depressive Disorder, Single Episode, Severe w/Psychotic Features">296.24 Major Depressive Disorder, Single Episode, Severe w/Psychotic Features</a></li>
						<li class="odd"><a href="#296.34 Major Depressive Disorder, Recurrent, Severe w/Psychotic Features">296.34 Major Depressive Disorder, Recurrent, Severe w/Psychotic Features</a></li>
						<li><a href="#296.01 Bipolar I Disorder, Single Manic Episode, Mild">296.01 Bipolar I Disorder, Single Manic Episode, Mild</a></li>
						<li class="odd"><a href="#296.02 Bipolar I Disorder, Single Manic Episode, Moderate">296.02 Bipolar I Disorder, Single Manic Episode, Moderate</a></li>
						<li><a href="#296.03 Bipolar I Disorder, Single Manic Episode, Severe w/o psychotic Features">296.03 Bipolar I Disorder, Single Manic Episode, Severe w/o psychotic Features</a></li>
						<li class="odd"><a href="#296.04 Bipolar I Disorder, Single Manic Episode, Severe w/Psychotic Features">296.04 Bipolar I Disorder, Single Manic Episode, Severe w/Psychotic Features</a></li>
						<li><a href="#296.40 Bipolar I Disorder, Most Recent Episode Hypomanic">296.40 Bipolar I Disorder, Most Recent Episode Hypomanic</a></li>
						<li class="odd"><a href="#296.41 Bipolar I Disorder, Most Recent Episode Manic, Mild">296.41 Bipolar I Disorder, Most Recent Episode Manic, Mild</a></li>
						<li><a href="#296.42 Bipolar I Disorder, Most Recent Episode Manic, Moderate">296.42 Bipolar I Disorder, Most Recent Episode Manic, Moderate</a></li>
						<li class="odd"><a href="#296.43 Bipolar I Disorder, Most Recent Episode Manic, Severe w/o Psychotic Features">296.43 Bipolar I Disorder, Most Recent Episode Manic, Severe w/o Psychotic Features</a></li>
						<li><a href="#296.44 Bipolar I Disorder, Most Recent Episode Manic, Severe w/Psychotic Features">296.44 Bipolar I Disorder, Most Recent Episode Manic, Severe w/Psychotic Features</a></li>
						<li class="odd"><a href="#296.51 Bipolar I Disorder, Most Recent Episode Depressed, Mild">296.51 Bipolar I Disorder, Most Recent Episode Depressed, Mild</a></li>
						<li><a href="#296.52 Bipolar I Disorder, Most Recent Episode Depressed, Moderate">296.52 Bipolar I Disorder, Most Recent Episode Depressed, Moderate</a></li>
						<li class="odd"><a href="#296.53 Bipolar I Disorder, Most Recent Episode Depressed, Severe w/o Psychotic Features">296.53 Bipolar I Disorder, Most Recent Episode Depressed, Severe w/o Psychotic Features</a></li>
						<li><a href="#296.54 Bipolar I Disorder, Most Recent Episode Depressed, Severe w/Psychotic Features">296.54 Bipolar I Disorder, Most Recent Episode Depressed, Severe w/Psychotic Features</a></li>
						<li class="odd"><a href="#295.30 Schizophrenia, Paranoid Type">295.30 Schizophrenia, Paranoid Type</a></li>
						<li><a href="#295.10 Schizophrenia, Disorganized Type">295.10 Schizophrenia, Disorganized Type</a></li>
						<li class="odd"><a href="#295.20 Schizophrenia, Catatonic Type">295.20 Schizophrenia, Catatonic Type</a></li>
						<li><a href="#295.90 Schizophrenia, Undifferentiated Type">295.90 Schizophrenia, Undifferentiated Type</a></li>
						<li class="odd"><a href="#295.60 Schizophrenia, Residual Type">295.60 Schizophrenia, Residual Type</a></li>
						<li><a href="#Other">Other</a></li>
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
				<ul class="radioList">
					<li>
						<div class="floatWrapper">
							<input type="radio" id="stage1" value="I know the stage" name="stage" /> <label for="stage1">I know the patient's stage. </label>
						</div>
						<div class="dropdown disabled" id="stageDropdown">
							<a href="#" class="pullDown">Select</a>
							<ul>
								<li class="odd"><a href="#">Select</a></li>
								<li><a href="#1">1</a></li>
								<li class="odd"><a href="#2">2</a></li>
								<li><a href="#3">3</a></li>
								<li class="odd"><a href="#4">4</a></li>
							</ul>
						</div>
					</li>
					<li><input type="radio" id="stage2" value="I know the medication" name="stage" /> <label for="stage2">I don't know the patient's stage, but I know the medications the patient has recently responded to.</label></li>
					<li><input type="radio" id="stage3" value="I don't know" name="stage" /> <label for="stage3">I don't know the patient's stage and I don't know the medications the patient has recently responded to.</label></li>
				</ul>			
			</li>
		</ul>
		<div class="errorMsg"></div>
		<div class="buttonHolder">
			<a href="#" id="ids1" class="orangeBtn next">Next</a>	
		</div>
		</form>
	</div>
	<div class="diagnosisStageContainer">
		<form action="" class="">	
		<p class="instructions">Please select the medications the patient has most recently responded to. The patient's stage will be determined based on the medications selected.
		<div id="ips">
			<ul>
				<li class="minHeight">
					<label>SSRIs</label>
					<ul class="checkboxList">
						<li>
							<input type="checkbox" name="SSRI" value="citalopram" id="citalopram" /> <label for="citalopram">citalopram</label>
						</li>
						<li>
							<input type="checkbox" name="SSRI" value="escitalopram" id="escitalopram" /> <label for="escitalopram">escitalopram</label>
						</li>
						<li>
							<input type="checkbox" name="SSRI" value="fluoxetine" id="fluoxetine" /> <label for="fluoxetine">fluoxetine</label>
						</li>
						<li>
							<input type="checkbox" name="SSRI" value="paroxetine" id="paroxetine" /> <label for="paroxetine">paroxetine</label>
						</li>
						<li>
							<input type="checkbox" name="SSRI" value="fluvoxamine" id="fluvoxamine" /> <label for="fluvoxamine">fluvoxamine</label>
						</li>
						<li>
							<input type="checkbox" name="SSRI" value="sertraline" id="sertraline" /> <label for="sertraline">sertraline</label>
						</li>
					</ul>
				</li>
				<li class="minHeight">
					<label>SNRIs</label>
					<ul class="checkboxList">
						<li>
							<input type="checkbox" name="SNRI" value="duloxetine" id="duloxetine" /> <label for="duloxetine">duloxetine</label>
						</li>
						<li>
							<input type="checkbox" name="SNRI" value="venlafaxine" id="venlafaxine" /> <label for="venlafaxine">venlafaxine <br /><span>(All drug forms)</span></label>
						</li>
						<li>
							<input type="checkbox" name="SNRI" value="desvenlafaxine" id="desvenlafaxine" /> <label for="desvenlafaxine">desvenlafaxine</label>
						</li>
					</ul>				
				</li>
				<li class="minHeight">
					<label>SGAs</label>
					<ul class="checkboxList">
						<li>
							<input type="checkbox" name="SGA" value="aripiprazole" id="aripiprazole" /> <label for="aripiprazole">aripiprazole</label>
						</li>
						<li>
							<input type="checkbox" name="SGA" value="olanzapine" id="olanzapine" /> <label for="olanzapine">olanzapine</label>
						</li>
						<li>
							<input type="checkbox" name="SGA" value="quetiapine" id="quetiapine" /> <label for="quetiapine">quetiapine</label>
						</li>
						<li>
							<input type="checkbox" name="SGA" value="risperidone" id="risperidone" /> <label for="risperidone">risperidone <br /><span>(All drug forms)</span></label>
						</li>
						<li>
							<input type="checkbox" name="SGA" value="ziprasidone" id="ziprasidone" /> <label for="ziprasidone">ziprasidone</label>
						</li>
					</ul>					
				</li>
				<li class="minHeight">
					<label>MAOIs</label>
					<ul class="checkboxList">
						<li>
							<input type="checkbox" name="MAOI" value="phenelzine" id="phenelzine" /> <label for="phenelzine">phenelzine</label>
						</li>
						<li>
							<input type="checkbox" name="MAOI" value="tranylcpromine" id="tranylcpromine" /> <label for="tranylcpromine">tranylcpromine</label>
						</li>
						<li>
							<input type="checkbox" name="MAOI" value="selege" id="selege" /> <label for="selege">selege <br />(transdermal)</label>
						</li>
						<li>
							<input type="checkbox" name="MAOI" value="isocarboxazid" id="isocarboxazid" /> <label for="isocarboxazid">isocarboxazid</label>
						</li>
						<li>
							<input type="checkbox" name="MAOI" value="moclobemide" id="moclobemide" /> <label for="moclobemide">moclobemide</label>
						</li>
					</ul>	
				</li>
				<li>
					<label>FGAs</label>
					<ul class="checkboxList">
						<li>
							<input type="checkbox" name="FGA" value="chlorpromazine" id="chlorpromazine" /> <label for="chlorpromazine">chlorpromazine</label>
						</li>
						<li>
							<input type="checkbox" name="FGA" value="perphenazine" id="perphenazine" /> <label for="perphenazine">perphenazine</label>
						</li>
						<li>
							<input type="checkbox" name="FGA" value="fluphenazine" id="fluphenazine" /> <label for="fluphenazine">fluphenazine <br /><span>(All drug forms)</span></label>
						</li>
						<li>
							<input type="checkbox" name="FGA" value="thiothixene" id="thiothixene" /> <label for="thiothixene">thiothixene</label>
						</li>
						<li>
							<input type="checkbox" name="FGA" value="haloperidol" id="haloperidol" /> <label for="haloperidol">haloperidol <br /><span>(All drug forms)</span></label>
						</li>
						<li>
							<input type="checkbox" name="FGA" value="trifluoperazine" id="trifluoperazine" /> <label for="trifluoperazine">trifluoperazine</label>
						</li>
						<li>
							<input type="checkbox" name="FGA" value="loxapine" id="loxapine" /> <label for="loxapine">loxapine</label>
						</li>
						<li>
							<input type="checkbox" name="FGA" value="molindone" id="molindone" /> <label for="molindone">molindone</label>
						</li>
					</ul>					
				</li>
				<li>
					<label>TCAs</label>
					<ul class="checkboxList">
						<li>
							<input type="checkbox" name="TCA" value="amitriptyline" id="amitriptyline" /> <label for="amitriptyline">amitriptyline</label>
						</li>
						<li>
							<input type="checkbox" name="TCA" value="desipramine" id="desipramine" /> <label for="desipramine">desipramine</label>
						</li>
						<li>
							<input type="checkbox" name="TCA" value="doxepin" id="doxepin" /> <label for="doxepin">doxepin</label>
						</li>
						<li>
							<input type="checkbox" name="TCA" value="nortriptyline" id="nortriptyline" /> <label for="nortriptyline">nortriptyline</label>
						</li>
						<li>
							<input type="checkbox" name="TCA" value="imipramine" id="imipramine" /> <label for="imipramine">imipramine</label>
						</li>
						<li>
							<input type="checkbox" name="TCA" value="protriptyline" id="protriptyline" /> <label for="protriptyline">protriptyline</label>
						</li>
						<li>
							<input type="checkbox" name="TCA" value="trimipramine" id="trimipramine" /> <label for="trimipramine">trimipramine</label>
						</li>
						<li>
							<input type="checkbox" name="TCA" value="maprotiline" id="maprotiline" /> <label for="maprotiline">maprotiline</label>
						</li>
					</ul>				
				</li>
				<li class="mood_stabilizers">
					<label>Mood Stabilizers</label>
					<ul class="checkboxList">
						<li>
							<input type="checkbox" name="mood_stabilizers" value="carbamazepine" id="carbamazepine" /> <label for="carbamazepine">carbamazepine</label>
						</li>
						<li>
							<input type="checkbox" name="mood_stabilizers" value="lamotrigine" id="lamotrigine" /> <label for="lamotrigine">lamotrigine</label>
						</li>
						<li>
							<input type="checkbox" name="mood_stabilizers" value="lithium" id="lithium" /> <label for="lithium">lithium</label>
						</li>
						<li>
							<input type="checkbox" name="mood_stabilizers" value="oxcarbamazepine" id="oxcarbamazepine" /> <label for="oxcarbamazepine">oxcarbamazepine</label>
						</li>
						<li>
							<input type="checkbox" name="mood_stabilizers" value="valproate" id="valproate" /> <label for="valproate">valproate</label>
						</li>
					</ul>				
				</li>
				<li>
					<label>Anxiolytics</label>
					<ul class="checkboxList">
						<li>
							<input type="checkbox" name="anxiolytic" value="buspirone" id="buspirone" /> <label for="buspirone">buspirone</label>
						</li>
						<li>
							<input type="checkbox" name="anxiolytic" value="benzodiazepine" id="benzodiazepine" /> <label for="benzodiazepine">benzodiazepine <br /><span>(Any drug)</span></label>
						</li>
					</ul>	
				</li>
				
				<li>
					<label>Other Drugs</label>
					<ul class="checkboxList">
						<li>
							<input type="checkbox" name="other_drugs" value="liothyronine" id="liothyronine" /> <label for="liothyronine">liothyronine</label>
						</li>
					</ul>	
				</li>
				<li>
					<label>Clozapine</label>
					<ul class="checkboxList">
						<li>
							<input type="checkbox" name="clozapine" value="clozapine" id="clozapine" /> <label for="clozapine">clozapine</label>
						</li>
					</ul>	
				</li>
				<li>
					<label>Other Antidepressants</label>
					<ul class="checkboxList">
						<li>
							<input type="checkbox" name="other_antidepressants" value="buproprion" id="buproprion" /> <label for="buproprion">buproprion</label>
						</li>
						<li>
							<input type="checkbox" name="other_anti_depressants" value="mirtazapine" id="mirtazapine" /> <label for="mirtazapine">mirtazapine</label>
						</li>
					</ul>
				</li>				
			</ul>
			<div id="drugKey">
				<span class="blue">SSRI</span> = Selective Serotonin Reuptake Inhibitor; <span class="blue">SNRI</span> = Serotonin Norepinephrine Reuptake Inhibitor;<br />
				<span class="blue">FGA</span> = First Generation Antipsychotic; <span class="blue">SGA</span> = Second Genertaion Antipsychotic; <span class="blue">TCA</span> = Tricyclic Antidepressant<br />
				<span class="blue">MAOI</span> = Monoamine Oxidase Inhibitor; <span class="blue">All drug forms</span> = All forms including oral, injected, short acting &amp; long acting;
			</div>
			<div class="errorMsg">Please select at least one medication.</div>	
		</div>				
		<div class="buttonHolder">
			<a href="#" class="orangeBtn next">Next</a>
			<a href="#" class="orangeBtn prev">Prev</a>
		</div>
	</form>
	</div>
	<div class="diagnosisStageContainer">
		<div id="saveDiagnosisStage">
			<ul>
				<li class="fieldWrapper" id="primaryDiagnosisField">
					<label>Primary DSM Axis 1 Diagnosis: </label><span class="inputVal"></span>
				</li>
				<li class="fieldWrapper" id="secondaryDiagnosisField">
					<label>Secondary and Other Diagnoses: </label><span class="inputVal"></span>
				</li>
				<li class="fieldWrapper">
					<label>Stage: </label>
					<p id="noStageInfo" class="stageInfo">Because you don't have information about the patient's recent response to medications, the patient's stage is: </p>
					<p id="ipsInfo" class="stageInfo">Based on the information you provided about the medications the patient has responded to, the patient's stage is: </p>
					<div class="dropdown" id="stageSelectedDropdown">
						<a href="#" class="pullDown"></a>
						<ul>
							<li class="odd"><a href="#1">1</a></li>
							<li><a href="#2">2</a></li>
							<li class="odd"><a href="#3">3</a></li>
							<li><a href="#4">4</a></li>
						</ul>
					</div>
				</li>
			</ul>
			<div class="buttonHolder">
				<a href="#" class="orangeBtn prev">Prev</a>
				<a href="#" class="orangeBtn next">Save</a>
			</div>
		</div>
	</div>
</div>