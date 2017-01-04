<%@page contentType="text/html" %> 
<%@page import="flinn.beans.response.ResponseSessionContainerBean"%>

<%
  int mid = -1;
  int adminid = -1;
  int medid = -1;
  Boolean isAdmin = false, isSuperAdmin = false;
  Boolean editable = false;
  Boolean postType = false;
  Boolean hasEdit = false;
  String date_include_date = "";
  String jspSelf = request.getRequestURI().toString();
  String mName = "", mAbbrev = "", mGroupAbbrev = "";

  String mStartDose = "", mDailyLowDose = "", mDailyHighDose = "", mMaxDose = "";
  String mUnit = "", mResponseTime = "", mDrugFunction = "", mDurationOfAction = "", mAMTGracePeriod = "", mAdministrationMechanism = "", mAltNamePattern = "", mGenericNamePattern = "";
  String mFormulationPattern = "", mDisplayName = "", mGuidelineChartName = "", mLabNamePattern = "";
  String mMDDStartDose = "", mMDDDailyLowDose = "", mMDDDailyHighDose = "";
  String mSerumLevelLow = "", mSerumLevelHigh = "", mSerumLevelUnit = "";
  String mLongActingFlag = "", mLongActingFrequency = "", mAMTPercentResponseTime = "", mAMTPercentDoseRange = "";

  int mGroupID = -1;
  int lastactivity = -1;
  
  String authcode = flinn.util.CookieHandler.getCookie("authcode", request);
  flinn.dao.DaoAppManager dm = new flinn.dao.DaoAppManager();
  flinn.beans.response.ResponseSessionContainerBean userSession = dm.getSession(authcode, request);
	try{
		//function call to update user's last activity
		lastactivity = dm.updateLastActivity(userSession);
		dm.commitConnection("updateLastActivity");
	}
	catch(Exception e) {
		dm.rollbackConnection("updateLastActivity");
		dm.LOG.debug("Unable to commit changes to updateLastActivity");
	}
	
	adminid = userSession.getUser().getAppuserid(); 
  
  if (request.getParameter("id") != null) medid = Integer.parseInt(request.getParameter("id"));

  if (request.getParameter("Name") != null) mName = request.getParameter("Name");
  
  if (request.getParameter("Abbreviation") != null) mAbbrev = request.getParameter("Abbreviation");

  if (request.getParameter("MedGroup") != null) mGroupID = Integer.parseInt(request.getParameter("MedGroup"));

  if (request.getParameter("StartDose") != null) mStartDose = request.getParameter("StartDose");
  if (request.getParameter("DailyLowDose") != null) mDailyLowDose = request.getParameter("DailyLowDose");
  if (request.getParameter("DailyHighDose") != null) mDailyHighDose = request.getParameter("DailyHighDose");
  if (request.getParameter("MaxDose") != null) mMaxDose = request.getParameter("MaxDose");
  if (request.getParameter("Unit") != null) mUnit = request.getParameter("Unit");
  if (request.getParameter("ResponseTime") != null) mResponseTime = request.getParameter("ResponseTime");
  if (request.getParameter("AMTPercentResponseTime") != null) mAMTPercentResponseTime = request.getParameter("AMTPercentResponseTime");
  if (request.getParameter("AMTPercentDoseRange") != null) mAMTPercentDoseRange = request.getParameter("AMTPercentDoseRange");
  if (request.getParameter("DrugFunction") != null) mDrugFunction = request.getParameter("DrugFunction");
  if (request.getParameter("DurationOfAction") != null) mDurationOfAction = request.getParameter("DurationOfAction");
  if (request.getParameter("AMTGracePeriod") != null) mAMTGracePeriod = request.getParameter("AMTGracePeriod");
  if (request.getParameter("AdministrationMechanism") != null) mAdministrationMechanism = request.getParameter("AdministrationMechanism");
  if (request.getParameter("GenericNamePattern") != null) mGenericNamePattern = request.getParameter("GenericNamePattern");
  if (request.getParameter("AltNamePattern") != null) mAltNamePattern = request.getParameter("AltNamePattern");
  if (request.getParameter("FormulationPattern") != null) mFormulationPattern = request.getParameter("FormulationPattern");
  if (request.getParameter("LongActingFlag") != null) mLongActingFlag = request.getParameter("LongActingFlag");
  if (request.getParameter("LongActingFrequency") != null) mLongActingFrequency = request.getParameter("LongActingFrequency");
  if (request.getParameter("DisplayName") != null) mDisplayName = request.getParameter("DisplayName");
  if (request.getParameter("GuidelineChartName") != null) mGuidelineChartName = request.getParameter("GuidelineChartName");
  if (request.getParameter("LabNamePattern") != null) mLabNamePattern = request.getParameter("LabNamePattern");
  if (request.getParameter("MDDStartDose") != null) mMDDStartDose = request.getParameter("MDDStartDose");
  if (request.getParameter("MDDDailyLowDose") != null) mMDDDailyLowDose = request.getParameter("MDDDailyLowDose");
  if (request.getParameter("MDDDailyHighDose") != null) mMDDDailyHighDose = request.getParameter("MDDDailyHighDose");
  if (request.getParameter("SerumLevelLow") != null) mSerumLevelLow = request.getParameter("SerumLevelLow");
  if (request.getParameter("SerumLevelHigh") != null) mSerumLevelHigh = request.getParameter("SerumLevelHigh");
  if (request.getParameter("SerumLevelUnit") != null) mSerumLevelUnit = request.getParameter("SerumLevelUnit");
    
  if(flinn.util.AdminRole.isFacilityAdmin(userSession))isAdmin = true;
  if (flinn.util.AdminRole.isAdmin(userSession))isSuperAdmin = true;
  
  if (request.getMethod() != null){
	  	if(request.getMethod().equals("POST")) postType = true;
  }
  
  if (request.getParameter("edit") != null){
	  	if(request.getParameter("edit").equals("y")) hasEdit = true;
  }
  
  
  if (isAdmin && postType) {
		flinn.beans.request.RequestContainerBean rqcont = new flinn.beans.request.RequestContainerBean();
		flinn.beans.request.RequestTreatmentBean rqTreatmentBean = new flinn.beans.request.RequestTreatmentBean();
		
		rqTreatmentBean.setTreatmentid(medid);
		rqTreatmentBean.setTreatmentname(mName);
		rqTreatmentBean.setTreatmentabbreviation(mAbbrev);
		rqTreatmentBean.setTreatmentgroupid(mGroupID);
		java.util.HashMap<String, String> details = new java.util.HashMap<String, String>();
		
		details.put("StartDose", mStartDose);
		details.put("DailyLowDose", mDailyLowDose);
		details.put("DailyHighDose", mDailyHighDose);
		details.put("MaxDose", mMaxDose);
		details.put("Unit", mUnit);
		details.put("ResponseTime", mResponseTime);
		details.put("AMTPercentResponseTime", mAMTPercentResponseTime);
		details.put("AMTPercentDoseRange", mAMTPercentDoseRange);
		details.put("DrugFunction", mDrugFunction);
		details.put("DurationOfAction", mDurationOfAction);
		details.put("AMTGracePeriod", mAMTGracePeriod);
		details.put("AdministrationMechanism", mAdministrationMechanism);
		details.put("GenericNamePattern", mGenericNamePattern);
		details.put("AltNamePattern", mAltNamePattern);
		details.put("FormulationPattern", mFormulationPattern);
		details.put("LongActingFlag", mLongActingFlag);
		details.put("LongActingFrequency", mLongActingFrequency);
		details.put("DisplayName", mDisplayName);
		details.put("GuidelineChartName", mGuidelineChartName);
		details.put("LabNamePattern", mLabNamePattern);
		details.put("MDDStartDose", mMDDStartDose);
		details.put("MDDDailyLowDose", mMDDDailyLowDose);
		details.put("MDDDailyHighDose", mMDDDailyHighDose);
		details.put("SerumLevelLow", mSerumLevelLow);
		details.put("SerumLevelHigh", mSerumLevelHigh);
		details.put("SerumLevelUnit", mSerumLevelUnit);

		rqTreatmentBean.setDetails(details);
		Boolean isValid = false;
		
		if (request.getParameter("Valid") != null){
			if (request.getParameter("Valid").equals("1")){isValid = true;}
		}
		rqTreatmentBean.setValid(isValid);
		    	
    	if (medid > 0){//Update treatment
    		try{
        		rqcont.setTreatment(rqTreatmentBean);
        		flinn.beans.response.ResponseTreatmentContainerBean rspBean= new flinn.beans.response.ResponseTreatmentContainerBean();
        		rspBean = (flinn.beans.response.ResponseTreatmentContainerBean)dm.updateTreatment(rqcont, userSession);
        		mid = rspBean.getTreatment().getTreatmentid();
    		}
    		catch(Exception e) {
    			dm.rollbackConnection("updateTreatment");
    			dm.LOG.debug("Unable to commit changes to updateTreatment");
    		}
    	}
    	else{ //Create treatment
    		try{
        		rqcont.setTreatment(rqTreatmentBean);
        		mid = dm.createTreatment(rqcont, userSession);
    		}
    		catch(Exception e) {
    			dm.rollbackConnection("createTreatment");
    			dm.LOG.debug("Unable to commit changes to createTreatment");
    		}
    	}
    	medid = mid;
        
      if (mid > 0) {
	      if (medid > 0) {  		response.sendRedirect("/admin/treatment_detail.jsp?id="+mid+"&reason=treatment+changes+saved");    	  
	      } else {  		response.sendRedirect("/admin/treatment_detail.jsp?id="+mid+"&reason=treatment+created"); 
	      }
	    } else {
			response.sendRedirect("/admin/error.jsp?reason="+mid);     	
	    }
  }


flinn.beans.response.ResponseTreatmentBean adminTreatment = new flinn.beans.response.ResponseTreatmentBean();
if (medid > 0){

try{
	adminTreatment = dm.getTreatment(medid);
}
catch(Exception e) {
	dm.LOG.debug("Unable to open connection getTreatment");
}
String title = adminTreatment.getTreatmentname();
mName = title;
medid = adminTreatment.getTreatmentid();
mAbbrev = adminTreatment.getTreatmentabbreviation();

// get required fields first 
mStartDose = adminTreatment.getDetails().get("StartDose");
mDailyLowDose = adminTreatment.getDetails().get("DailyLowDose");
mDailyHighDose = adminTreatment.getDetails().get("DailyHighDose");
mMaxDose = adminTreatment.getDetails().get("MaxDose");
mUnit = adminTreatment.getDetails().get("Unit");
mResponseTime = adminTreatment.getDetails().get("ResponseTime");
mAMTPercentResponseTime = adminTreatment.getDetails().get("AMTPercentResponseTime");
mAMTPercentDoseRange = adminTreatment.getDetails().get("AMTPercentDoseRange");
mDrugFunction = adminTreatment.getDetails().get("DrugFunction");
mDurationOfAction = adminTreatment.getDetails().get("DurationOfAction");
mAMTGracePeriod = adminTreatment.getDetails().get("AMTGracePeriod");
mAdministrationMechanism = adminTreatment.getDetails().get("AdministrationMechanism");

// now get optional fields
if (adminTreatment.getDetails().get("GenericNamePattern") != null)
	mGenericNamePattern = adminTreatment.getDetails().get("GenericNamePattern");
if (adminTreatment.getDetails().get("AltNamePattern") != null)
	mAltNamePattern = adminTreatment.getDetails().get("AltNamePattern");
if (adminTreatment.getDetails().get("FormulationPattern") != null)
	mFormulationPattern = adminTreatment.getDetails().get("FormulationPattern");
if (adminTreatment.getDetails().get("LongActingFlag") != null)
	mLongActingFlag = adminTreatment.getDetails().get("LongActingFlag");
if (adminTreatment.getDetails().get("LongActingFrequency") != null)
	mLongActingFrequency = adminTreatment.getDetails().get("LongActingFrequency");
if (adminTreatment.getDetails().get("DisplayName") != null)
	mDisplayName = adminTreatment.getDetails().get("DisplayName");
if (adminTreatment.getDetails().get("GuidelineChartName") != null)
	mGuidelineChartName = adminTreatment.getDetails().get("GuidelineChartName");
if (adminTreatment.getDetails().get("LabNamePattern") != null)
	mLabNamePattern = adminTreatment.getDetails().get("LabNamePattern");
if (adminTreatment.getDetails().get("MDDStartDose") != null)
	mMDDStartDose = adminTreatment.getDetails().get("MDDStartDose");
if (adminTreatment.getDetails().get("MDDDailyLowDose") != null)
	mMDDDailyLowDose = adminTreatment.getDetails().get("MDDDailyLowDose");
if (adminTreatment.getDetails().get("MDDDailyHighDose") != null)
	mMDDDailyHighDose = adminTreatment.getDetails().get("MDDDailyHighDose");
if (adminTreatment.getDetails().get("SerumLevelLow") != null)
	mSerumLevelLow = adminTreatment.getDetails().get("SerumLevelLow");
if (adminTreatment.getDetails().get("SerumLevelHigh") != null)
	mSerumLevelHigh = adminTreatment.getDetails().get("SerumLevelHigh");
if (adminTreatment.getDetails().get("SerumLevelUnit") != null)
	mSerumLevelUnit = adminTreatment.getDetails().get("SerumLevelUnit");


mGroupID = adminTreatment.getTreatmentgroupid();
mGroupAbbrev = adminTreatment.getGroup().getTreatmentgroupabbreviation();


editable = false;   // Whether or not this is to edit the information
}
if (isAdmin && hasEdit) editable = true;
%>

	<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#CAD8DF">
<% if (editable) { %>
<script type="text/javascript" language="JavaScript" src="common/js/form_validate.js"></script>
<FORM method="POST" action="<% out.print(jspSelf); %>" name="adminform">
<input type="hidden" name="id" value="<% if (medid > 0) out.print(medid); else out.print(0); %>">
<input type="hidden" name="staff" value="<% out.print(request.getParameter("staff")); %>">
<% } %>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Name</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='Name' value='"); %><% if (medid > 0)out.print(mName.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Abbreviation</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='Abbreviation' value='"); %><% if (medid > 0)out.print(mAbbrev.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Start Dose</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='StartDose' maxlength='30' value='"); %><% if (medid > 0)out.print(mStartDose.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Daily Low Dose</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='DailyLowDose' maxlength='30' value='"); %><% if (medid > 0)out.print(mDailyLowDose.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>
	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Daily High Dose</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='DailyHighDose' maxlength='30' value='"); %><% if (medid > 0)out.print(mDailyHighDose.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Max Dose</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='MaxDose' maxlength='30' value='"); %><% if (medid > 0)out.print(mMaxDose.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Unit</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='Unit' maxlength='30' value='"); %><% if (medid > 0)out.print(mUnit.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Response Time</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='ResponseTime' maxlength='30' value='"); %><% if (medid > 0)out.print(mResponseTime.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">AMT Treatment Response Time Percentage Required</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='AMTPercentResponseTime' maxlength='30' value='"); %><% if (medid > 0)out.print(mAMTPercentResponseTime.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">AMT Treatment Dose Range Percentage Required</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='AMTPercentDoseRange' maxlength='30' value='"); %><% if (medid > 0)out.print(mAMTPercentDoseRange.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Drug Function</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='DrugFunction' maxlength='30' value='"); %><% if (medid > 0)out.print(mDrugFunction.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Duration of Action (in days)</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='DurationOfAction' maxlength='30' value='"); %><% if (medid > 0)out.print(mDurationOfAction.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment AMT Grace Period (in days)</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='AMTGracePeriod' maxlength='30' value='"); %><% if (medid > 0)out.print(mAMTGracePeriod.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Administration Mechanism (oral, injection, etc.)</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='AdministrationMechanism' maxlength='30' value='"); %><% if (medid > 0)out.print(mAdministrationMechanism.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Generic Name Pattern</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='GenericNamePattern' maxlength='30' value='"); %><% if (medid > 0)out.print(mGenericNamePattern.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Alternate Name Pattern</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='AltNamePattern' maxlength='30' value='"); %><% if (medid > 0)out.print(mAltNamePattern.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Formulation Pattern</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='FormulationPattern' maxlength='30' value='"); %><% if (medid > 0)out.print(mFormulationPattern.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Long Acting Flag </p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='Checkbox' name='LongActingFlag' value='1' "); %><% if (medid > 0 && mLongActingFlag.equals("1"))out.print("checked"); %><% if (editable) out.print(">"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Long Acting Frequency </p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='LongActingFrequency' maxlength='30' value='"); %><% if (medid > 0)out.print(mLongActingFrequency.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Display Name</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='DisplayName' maxlength='30' value='"); %><% if (medid > 0)out.print(mDisplayName.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Guideline Chart Name</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='GuidelineChartName' maxlength='30' value='"); %><% if (medid > 0)out.print(mGuidelineChartName.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Serum Level Lab Name Pattern</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='LabNamePattern' maxlength='30' value='"); %><% if (medid > 0)out.print(mLabNamePattern.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment MDD Start Dose</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='MDDStartDose' maxlength='30' value='"); %><% if (medid > 0)out.print(mMDDStartDose.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment MDD Daily Low Dose</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='MDDDailyLowDose' maxlength='30' value='"); %><% if (medid > 0)out.print(mMDDDailyLowDose.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment MDD Daily High Dose</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='MDDDailyHighDose' maxlength='30' value='"); %><% if (medid > 0)out.print(mMDDDailyHighDose.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Serum Level Low</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='SerumLevelLow' maxlength='30' value='"); %><% if (medid > 0)out.print(mSerumLevelLow.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Serum Level High</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='SerumLevelHigh' maxlength='30' value='"); %><% if (medid > 0)out.print(mSerumLevelHigh.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
			<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Serum Level Unit</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='SerumLevelUnit' maxlength='30' value='"); %><% if (medid > 0)out.print(mSerumLevelUnit.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>
	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
				<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Treatment Group</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) {
				flinn.beans.request.RequestContainerBean input = new flinn.beans.request.RequestContainerBean();
				try{
					flinn.beans.response.ResponseTreatmentGroupContainerBean adminTreatmentGroup = (flinn.beans.response.ResponseTreatmentGroupContainerBean)dm.findAllTreatmentGroups(input, userSession, true, isSuperAdmin, null);
					flinn.beans.TreatmentGroupBean[] tgBean = adminTreatmentGroup.getGroups();
					if(tgBean.length > 0){
						out.print("<SELECT name='MedGroup'>");
						for(int i=0; i < tgBean.length; i++)
						{
							out.print("<option value='");
							out.print(tgBean[i].getTreatmentgroupid());
							out.print("'");
							if (mGroupID == tgBean[i].getTreatmentgroupid())out.print(" SELECTED"); 
							out.print(">");
							out.print(tgBean[i].getTreatmentgroupabbreviation());
						}
						out.print("</SELECT>");
					}
				}
				catch(Exception e) {
					dm.LOG.debug("Unable to open connection findAllTreatmentGroups");
				}

			}	 
			else{out.print(mGroupAbbrev);}
			%></p></td>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Valid?</p></td>
		<td align="left"><p><%
if (editable) {
	out.print("<input type='radio' name='Valid' value='1'");
  if(medid == 0)adminTreatment.setValid(true);
  if (adminTreatment.getValid()) out.print(" CHECKED");
  out.print("> valid<br>\n");
  out.print("<input type='radio' name='Valid' value='0'");
  if (!adminTreatment.getValid()) out.print(" CHECKED");
  out.print("> invalid<br>\n");
} else {
	out.print("<p class='formText' style='text-align:left;'>");
  if (adminTreatment.getValid()) { out.print("valid"); } else { out.print("<span class=\"formTextRed\">invalid</span>"); } 
} if(medid == 0)adminTreatment.setValid(false);%></p></td>
		<td>&nbsp;</td>
	</tr>	
<% if (!editable) { %>
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>

<%
  }

if (isAdmin && !editable) {
  %>
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td><img src="/s.gif" width=1 height=37 alt="" border="0"></td>
		<td>&nbsp;</td>
		<td>
			<table border="0" cellspacing="0" cellpadding="4">
			<tr>
				<td>
					<div class="updateText"><a href="/admin/treatment_detail.jsp?id=<% out.print(medid); %>&edit=y" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Edit</a></div>
				</td>
			</tr>
			</table>
		</td>
		<td>&nbsp;</td>
	</tr>
  <%
} 
if (editable) {
  %>
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td><img src="/s.gif" width=1 height=37 alt="" border="0"></td>
		<td>&nbsp;</td>
		<td>
			<table border="0" cellspacing="0" cellpadding="4">
			<tr>
				<td>
                                        <div class="updateText"><a href="javascript:formsubmit('adminform')" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Save Updates</a></div>
                                </td>
                                <td>
                                        <div class="updateText"><a href="<%
if (medid > 0) {
  out.print(jspSelf); 
  out.print("?id="+medid); 
} else {
	out.print("/admin/treatment.jsp");
}

%>" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Cancel Updates</a></div></td>
			</tr>
			</table>
		</td>
		<td>&nbsp;</td>
	</tr>
  <%
} 
%>
<%
dm.disposeConnection("treatment_detail");
if (editable) { %>
</FORM>	
<script language="JavaScript" type="text/javascript">
  function formsubmit(frmname) {
    if(document.forms[frmname]) {
      if (document.forms[frmname].onsubmit()) {
        document.forms[frmname].submit();
      }
    }
  }

  
  var frmvalidator  = new Validator("adminform");
<% if (editable) { %>
  frmvalidator.addValidation("Name","req","Please enter a name for the treatment");
  frmvalidator.addValidation("Name","maxlen=100","Max length for treatment name is 100");
  frmvalidator.addValidation("Abbreviation","req","Please enter an abbreviation name for the treatment");
  frmvalidator.addValidation("Abbreviation","maxlen=100","Max length for treatment abbreviation");
  frmvalidator.addValidation("MedGroup","dontselect","Please select a treatment group");
  
  frmvalidator.addValidation("StartDose","req","Please enter a start dose for the treatment");
  frmvalidator.addValidation("StartDose","maxlen=30","Max length for start dose unit is 30");
  frmvalidator.addValidation("DailyLowDose","req","Please enter a daily low dose for the treatment");
  frmvalidator.addValidation("DailyLowDose","maxlen=30","Max length for treatment daily low dose is 30");
  frmvalidator.addValidation("DailyHighDose","req","Please enter a daily high dose for the treatment");
  frmvalidator.addValidation("DailyHighDose","maxlen=30","Max length for treatment daily high dose is 30");
  frmvalidator.addValidation("MaxDose","req","Please enter a max dose for the treatment");
  frmvalidator.addValidation("MaxDose","maxlen=30","Max length for treatment max dose is 30");
  frmvalidator.addValidation("Unit","req","Please enter a unit for the treatment");
  frmvalidator.addValidation("Unit","maxlen=30","Max length for treatment unit is 30");
  frmvalidator.addValidation("ResponseTime","req","Please enter a response time for the treatment");
  frmvalidator.addValidation("ResponseTime","maxlen=30","Max length for treatment response time is 30");
  frmvalidator.addValidation("AMTPercentResponseTime","req","Please enter a response time for the treatment");
  frmvalidator.addValidation("AMTPercentResponseTime","maxlen=30","Max length for treatment response time is 30");
  frmvalidator.addValidation("AMTPercentDoseRange","req","Please enter a response time for the treatment");
  frmvalidator.addValidation("AMTPercentDoseRange","maxlen=30","Max length for treatment response time is 30");
  frmvalidator.addValidation("DrugFunction","req","Please enter a drug function for the treatment");
  frmvalidator.addValidation("DrugFunction","maxlen=30","Max length for treatment drug function is 30");
  frmvalidator.addValidation("DurationOfAction","req","Please enter a duration of action for the treatment");
  frmvalidator.addValidation("DurationOfAction","numeric","Duration of action is numeric");
  frmvalidator.addValidation("AMTGracePeriod","req","Please enter an AMT Grace Period for the treatment");
  frmvalidator.addValidation("AMTGracePeriod","numeric","AMT Grace Period is numeric");
  frmvalidator.addValidation("AdministrationMechanism","req","Please enter a drug function for the treatment");
  frmvalidator.addValidation("AdministrationMechanism","maxlen=30","Max length for treatment drug function is 30");

  frmvalidator.addValidation("GenericNamePattern","maxlen=30","Max length for treatment generic name pattern is 30");
  frmvalidator.addValidation("AltNamePattern","maxlen=30","Max length for treatment alt generic name pattern is 30");
  frmvalidator.addValidation("FormulationPattern","maxlen=30","Max length for treatment formulation pattern is 30");
  frmvalidator.addValidation("DisplayName","maxlen=30","Max length for treatment display name is 30");
  frmvalidator.addValidation("GuidelineChartName","maxlen=30","Max length for treatment guideline chart name is 30");
  frmvalidator.addValidation("LabNamePattern","maxlen=30","Max length for treatment lab name patternis 30");
  frmvalidator.addValidation("MDDStartDose","maxlen=30","Max length for treatment MDD start dose is 30");
  frmvalidator.addValidation("MDDDailyLowDose","maxlen=30","Max length for treatment MDD daily low dose is 30");
  frmvalidator.addValidation("MDDDailyHighDose","maxlen=30","Max length for treatment MDD daily high dose is 30");
  frmvalidator.addValidation("SerumLevelLow","maxlen=30","Max length for treatment serum level low is 30");
  frmvalidator.addValidation("SerumLevelHigh","maxlen=30","Max length for treatment serum level high is 30");
  frmvalidator.addValidation("SerumLevelUnit","maxlen=30","Max length for treatment serum level unit is 30");
  
<% } %>

</script>
<% } %>
	</table>

