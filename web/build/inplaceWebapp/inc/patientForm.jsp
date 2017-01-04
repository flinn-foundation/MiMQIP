<%@ page import="java.util.*" %>
<%!
public String computeLi(int lower, int upper, boolean isYear){
	StringBuffer liOut = new StringBuffer();
	if (isYear){
		for(int i=upper; i>lower; i--){ 
			liOut.append(buildLi(i));													
		}	
	}	
	else{
		for(int i=lower; i<upper; i++){ 
			liOut.append(buildLi(i));													
		}
	}
	return liOut.toString();
}

public String buildLi(int i){
	StringBuffer liOut = new StringBuffer();
	liOut.append("<li");							
	if (i % 2 == 0) {
		liOut.append(" class=\"odd\""); 
	} 
	liOut.append("><a href=\"#");
	liOut.append(Integer.toString(i));
	liOut.append("\">");							
	liOut.append(Integer.toString(i));
	liOut.append("</a></li>");
	return liOut.toString();
}
%>
<div class="floatLeft">
	<div class="fieldWrapper">
		<label for="fNameInput">First Name:</label>
		<input type="text" class="greenInput" id="fNameInput" />
	</div>
	<div class="fieldWrapper">
		<label for="lNameInput">Last Name:</label>
		<input type="text" class="greenInput" id="lNameInput" />
	</div>
	<div class="fieldWrapper" id="raceField">
		<label for="raceWhite">Race:</label>
		<ul id="raceList" class="inputList">
			<li><input type="checkbox" value="White" id="raceWhite" /> <label for="raceWhite">White</label></li>
			<li><input type="checkbox" value="African-American, Black" id="raceBlack" /> <label for="raceBlack">African-American, Black</label></li>
			<li><input type="checkbox" value="American Indian" id="raceIndian" /> <label for="raceIndian">American Indian</label></li>
			<li><input type="checkbox" value="Asian (Indian, Chinese, Filipino, Japanese, Korean, or other)" id="raceAsian" /> <label for="raceAsian">Asian <br /><span>(Indian, Chinese, Filipino, Japanese, Korean, or other)</span></label></li>
			<li><input type="checkbox" value="Pacific Islander (Hawaiian, Samoan, or other)" id="racePacific" /> <label for="racePacific">Pacific Islander <span>(Hawaiian, Samoan, or other)</span></label></li>
			<li><input type="checkbox" value="No response" id="raceNone" /> <label for="raceNone">No response</label></li>
		</ul>
	</div>	
	<div class="fieldWrapper" id="ethnicityField">
		<label for="raceDropdown">Ethnicity:</label>
		<div class="floatLeft">
			<p>Hispanic, Latino or Spanish? </p>
			<ul id="ethnicityList" class="inputList">		
				<li><input type="radio" name="hispanic" value="yes" id="hispanicYes" /> <label for="hispanicYes">Yes</label></li>
				<li><input type="radio" name="hispanic" value="no" id="hispanicNo" /> <label for="hispanicNo">No</label></li>
				<li><input type="radio" name="hispanic" value="no response" id="hispanicNoResponse" /> <label for="hispanicNoResponse">No response</label></li>
			</ul>
		</div>
	</div>
	<div class="fieldWrapper">
		<label for="pcpnInput" class="noPadding">Primary Care Physician Name:</label>
		<input type="text" class="greenInput" id="pcpnInput" />
	</div>
	<div class="fieldWrapper">
		<label for="pcpeInput" class="noPadding">Primary Care Physician Email:</label>
		<input type="text" class="greenInput" id="pcpeInput" />
	</div>
</div>
<div class="floatLeft right">	
	<div class="fieldWrapper">
		<label for="patientIdInput">ID #:</label>
		<input type="text" class="greenInput" id="patientIdInput" />
	</div>
	<div class="fieldWrapper">
		<label for="zipCodeInput">ZIP Code:</label>
		<input type="text" maxlength="6" class="greenInput" id="zipCodeInput" />
	</div>		
	<div class="fieldWrapper">
		<label for="employmentDropdown" class="noPadding">Employment Status:</label>
		<div class="dropdown" id="employmentDropdown">
			<a href="#" class="pullDown">Select</a>
			<ul>
				<li class="odd"><a href="#" class="selected">Select</a></li>
				<li><a href="#Unemployed">Unemployed</a></li>
				<li class="odd"><a href="#Full time salaried">Full time salaried</a></li>
				<li><a href="#Part time salaried">Part time salaried</a></li>
				<li class="odd"><a href="#Full time non-salaried">Full time non-salaried</a></li>
				<li><a href="#Part time non-salaried">Part time non-salaried</a></li>
				<li class="odd"><a href="#Self employed">Self employed</a></li>
			</ul>
		</div>
	</div>
	<div class="fieldWrapper">
		<label for="sexDropdown">Sex:</label>
		<div class="dropdown" id="sexDropdown">
			<a href="#" class="pullDown">Select</a>
			<ul>
				<li class="odd"><a href="#" class="selected">Select</a></li>
				<li><a href="#M">Male</a></li>
				<li class="odd"><a href="#F">Female</a></li>
			</ul>
		</div>
	</div>
	<div class="fieldWrapper">
		<label for="mmDropdown">Date of Birth:</label>
		<div class="dropdown dob" id="mmDropdown">
			<a href="#" class="pullDown">MM</a>
			<ul>
				<li class="odd"><a href="#" class="selected">MM</a></li>
				<%out.print(computeLi(1,13,false));%>
			</ul>
		</div>
		<div class="dropdown dob" id="ddDropdown">
			<a href="#" class="pullDown">DD</a>
			<ul>
				<li class="odd"><a href="#" class="selected">DD</a></li>
				<%out.print(computeLi(1,32,false));%> 
			</ul>
		</div>
		<div class="dropdown dob" id="yyyyDropdown">
			<a href="#" class="pullDown">YYYY</a>
			<ul>
				<li class="odd"><a href="#" class="selected">YYYY</a></li>
				<% 						
				GregorianCalendar cal = new GregorianCalendar();
				Date date1 = new Date();
				cal.setTime(date1);
				int year = cal.get(GregorianCalendar.YEAR);
				out.print(computeLi(year-100,year,true));
				%>
			</ul>
		</div>
	</div>


	<div class="fieldWrapper">
		<label for="maritalDropdown">Marital Status:</label>
		<div class="dropdown" id="maritalDropdown">
			<a href="#" class="pullDown">Select</a>
			<ul>
				<li class="odd"><a href="#" class="selected">Select</a></li>
				<li><a href="#Single">Single</a></li>
				<li class="odd"><a href="#Married">Married</a></li>
				<li><a href="#Separated">Separated</a></li>
				<li class="odd"><a href="#Divorced">Divorced</a></li>
				<li><a href="#Widowed">Widowed</a></li>
			</ul>
		</div>
	</div>

	<div class="fieldWrapper">
		<label for="livingDropdown" class="noPadding">Living Arrangement:</label>
		<div class="dropdown" id="livingDropdown">
			<a href="#" class="pullDown">Select</a>
			<ul>
				<li class="odd"><a href="#" class="selected">Select</a></li>
				<li><a href="#Alone">Alone</a></li>
				<li class="odd"><a href="#With roommates">With roommates</a></li>
				<li><a href="#With spouse">With spouse</a></li>
				<li class="odd"><a href="#With spouse and children">With spouse and children</a></li>
				<li><a href="#With parents">With parents</a></li>
				<li class="odd"><a href="#Group home">Group home</a></li>
			</ul>
		</div>
	</div>	
</div>