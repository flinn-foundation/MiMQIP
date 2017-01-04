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
<div id="colorboxForm" class="enterLabs">
		<h1>Enter laboratory test results</h1>
		<h2 id="labsPatID" class="secondaryHeading">Patient ID#: <span></span></h2>
		
		<div id="labFields" class="show">
			<form action="" id="enterLabsForm">
				<div class="fieldWrapper">
				<label for="mmDropdown">Lab test date:</label>
				<div class="dropdown dob" id="mmDropdownLabs">
					<a href="#" class="pullDown">MM</a>
					<ul>
						<li class="odd"><a href="#" class="selected">MM</a></li>
						<%out.print(computeLi(1,13,false));%>
					</ul>
				</div>
				<div class="dropdown dob" id="ddDropdownLabs">
					<a href="#" class="pullDown">DD</a>
					<ul>
						<li class="odd"><a href="#" class="selected">DD</a></li>
						<%out.print(computeLi(1,32,false));%> 
					</ul>
				</div>
				<div class="dropdown dob" id="yyyyDropdownLabs">
					<a href="#" class="pullDown">YYYY</a>
					<ul>
						<li class="odd"><a href="#" class="selected">YYYY</a></li>
						<% 						
						GregorianCalendar cal = new GregorianCalendar();
						Date date1 = new Date();
						cal.setTime(date1);
						int year = cal.get(GregorianCalendar.YEAR);
						out.print(computeLi(year-50,year,true));
						%>
					</ul>
				</div>
			</div>
			<div class="fieldWrapper">
				<label for="labTestsDropdownColorbox">Test Name:</label>
				<div class="dropdown" id="labTestsDropdownColorbox">
					<a href="#" class="pullDown">Select</a>
					<ul></ul>
				</div>
			</div>
			<div id="labTestFields"></div>
			<input type="submit" id="saveLab" class="orangeBtn" value="Save" />
			<div id="labsErrorMsg" class="errorMsg"></div>
		</form>
	</div>
	<div id="savedLab">
		<p>The lab test results have been saved.</p>
		<p class="buttons">
			<a href="#" id="moreLabs" class="orangeBtn">Enter More Labs</a> 
			<span>or</span>
			<a href="#" id="closeLabs">Close</a>
		</p>
	</div>
</div>