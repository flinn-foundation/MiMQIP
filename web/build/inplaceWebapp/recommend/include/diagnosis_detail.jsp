<%@page contentType="text/html" %>
<%@page import="flinn.beans.response.ResponseSessionContainerBean"%>
<%@page import="flinn.recommend.beans.RecommendTreatmentGuidelineBean"%>
<%@page import="flinn.recommend.beans.RecommendDiagnosisBean"%>
<%@page import="flinn.recommend.beans.RecommendRuleCriteriaBean"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%
            int adminid = -1;
            String serverName = request.getServerName();
            Boolean isAdmin = false, isSuperAdmin = false;
            Boolean editable = false;
            Boolean postType = false;
            Boolean hasEdit = false;
            String jspSelf = request.getRequestURI().toString();

            int did = -1;
            int diagnosisid = -1;
            String diagnosisName = "", stage = "", notes = "";
            int lastactivity = -1;
            RecommendDiagnosisBean diagnosis = null;
            RecommendTreatmentGuidelineBean[] treatments = null;
            RecommendRuleCriteriaBean[] rulecriterion = null;

            String authcode = flinn.util.CookieHandler.getCookie("authcode", request);
            flinn.recommend.dao.DaoRecommendManager dm = new flinn.recommend.dao.DaoRecommendManager();
            flinn.beans.response.ResponseSessionContainerBean userSession = dm.getSession(authcode, request);
            try {
                //function call to update user's last activity
                lastactivity = dm.updateLastActivity(userSession);
                dm.commitConnection("updateLastActivity");
            } catch (Exception e) {
                dm.rollbackConnection("updateLastActivity");
                dm.LOG.debug("Unable to commit changes to updateLastActivity");
            }

            adminid = userSession.getUser().getAppuserid();

            if (request.getParameter("id") != null) {
                diagnosisid = Integer.parseInt(request.getParameter("id"));
            }

            if (request.getParameter("Diagnosis") != null) {
                diagnosisName = request.getParameter("Diagnosis");
            }

            if (request.getParameter("Stage") != null) {
                stage = request.getParameter("Stage");
            }

            if (request.getParameter("Notes") != null) {
                stage = request.getParameter("Notes");
            }

            if (flinn.util.AdminRole.isRecommendAdmin(userSession)) {
                isAdmin = true;
            }
            if (flinn.util.AdminRole.isAdmin(userSession)) {
                isSuperAdmin = true;
            }

            if (request.getMethod() != null) {
                if (request.getMethod().equals("POST")) {
                    postType = true;
                }
            }

            if (request.getParameter("edit") != null) {
                if (request.getParameter("edit").equals("y")) {
                    hasEdit = true;
                }
            }


            if (isAdmin && postType) {
            }


            try {

                if (diagnosisid > 0) {

                    flinn.recommend.beans.RecommendDiagnosisBean input = new flinn.recommend.beans.RecommendDiagnosisBean();
                    flinn.recommend.beans.RecommendDiagnosisBean[] diagnosisList = null;
                    diagnosisList = dm.findAllDiagnoses(); //function call to get diagnosis
                    if (diagnosisList != null) {
                        for (int i = 0; i < diagnosisList.length; i++) {
                            if (diagnosisList[i].getDiagnosisid() == diagnosisid) {
                                diagnosis = diagnosisList[i];
                                treatments = diagnosis.getTreatments();
                                break;
                            }
                        }
                    }

                    if (diagnosis != null) {
                        diagnosisName = diagnosis.getDiagnosis();
                        stage = diagnosis.getStage();
                        notes = diagnosis.getNotes();
                    }
                }
            } catch (Exception e) {
                dm.LOG.debug("Unable to open connection findAllDiagnoses");
            }


            editable = false;   // Whether or not this is to edit the information
            if (isAdmin && hasEdit) {
                editable = true;
            }
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#CAD8DF">
    <% if (editable) {%>
    <script type="text/javascript" language="JavaScript" src="common/js/form_validate.js"></script>
    <FORM method="POST" action="<% out.print(jspSelf);%>" name="adminform">
        <input type="hidden" name="id" value="<% if (diagnosisid > 0) {
                 out.print(diagnosisid);
             } else {
                 out.print(0);
             }%>">
        <% }%>
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">Diagnosis</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;"><%=diagnosisName.replace("&","&amp;").replace("'", "&#039;")%></p></td>
            <td>&nbsp;</td>
        </tr>


        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>


        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">Stage</p></td>
            <td align="right">
<p class="formText" style="text-align:left;"><%=stage.replace("&","&amp;").replace("'", "&#039;")%></p></td>
            <td>&nbsp;</td>
        </tr>


        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">Notes</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <% if (editable) {
                                    out.print("<textarea rows='6' cols='80' name='Notes'>");
                                }
                                if (diagnosisid > 0) {
                                    out.print(notes.replace("&","&amp;").replace("'", "&#039;"));
                                }
                                if (editable) {
                                                    out.print("</textarea>");
                                                }%></p></td>
            <td>&nbsp;</td>
        </tr>



        <% if (!editable) {%>
        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>

        <%            }







                    if (isAdmin && !editable) {
        %>
<!--        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>
        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=37 alt="" border="0"></td>
            <td>&nbsp;</td>
            <td>
                <table border="0" cellspacing="0" cellpadding="4">
                    <tr>
                        <td>
                            <div class="updateText"><a href="/recommend/diagnosis_detail.jsp?id=<% out.print(diagnosisid);%>&edit=y" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Edit</a></div>
                        </td>
                    </tr>
                </table>
            </td>
            <td>&nbsp;</td>
        </tr> -->
        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>
        <tr>
            <td><img src="/recommend/images/s.gif" alt="" border="0" height="1" width="1"></td>
            <td colspan="2" style="background-repeat: repeat-x;" background="/admin/images/row_separator.gif"></td>
            <td><img src="/recommend/images/s.gif" alt="" border="0" height="1" width="1"></td>
        </tr>
        <tr>
            <td align="left" width="12"><img src="/recommend/images/s.gif" alt="" border="0" height="1" width="12"></td>
            <td nowrap="nowrap"><p class="formText">Treatment</p></td>
            <td align="right">
                <p class="formText" style="text-align: left;">
                    <%
                                            if (treatments != null) {
                                                for (int j = 0; j < treatments.length; j++) {
                                                    RecommendTreatmentGuidelineBean guideline = (RecommendTreatmentGuidelineBean) treatments[j];
                                                    out.print(guideline.getTreatment().getTreatmentname() + " " + guideline.getRow() + " " + guideline.getDrug());
                    %>
                    &nbsp;&nbsp;<a href="javascript:AddTreatmentWindow(<% out.print(diagnosisid);%>,<% out.print(guideline.getTreatmentid());%>,<% out.print(guideline.getTreatmentguidelineid());%>)" class="subLink">
                        <img src="/recommend/images/blue_arrow.gif" alt="" border="0" height="10" width="5"> Edit</a>
                    &nbsp;&nbsp;<a href="javascript:DeleteTreatment(<% out.print(diagnosisid);%>,<% out.print(guideline.getTreatmentid());%>,<% out.print(guideline.getTreatmentguidelineid());%>)" class="subLink">
                        <img src="/recommend/images/blue_arrow.gif" alt="" border="0" height="10" width="5"> Delete</a><br />
                        <%
                                                    }
                                                }
                        %>

                    <a href="javascript:AddTreatmentWindow(<% out.print(diagnosisid);%>)" class="subLink">
                        <img src="/recommend/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Add New Treatment</a>
            </td>
        </tr>
        <%
                    }
                    if (editable) {
        %>
        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>
        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=37 alt="" border="0"></td>
            <td>&nbsp;</td>
            <td>
                <table border="0" cellspacing="0" cellpadding="4">
                    <tr>
                        <td>
                            <div class="updateText"><a href="javascript:formsubmit('adminform')" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Save Updates</a></div>
                        </td>
                        <td>
                            <div class="updateText"><a href="<%
                                                    if (diagnosisid > 0) {
                                                        out.print(jspSelf);
                                                        out.print("?id=" + diagnosisid);
                                                    } else {
                                                        out.print("/recommend/rule.jsp");
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
        <%            dm.disposeConnection("rule_detail");
                    if (editable) {%>
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
        <% if (editable) {%>
            frmvalidator.addValidation("Notes","req","Please enter a note");
            frmvalidator.addValidation("Notes","maxlen=500","Max length for notes is 500");  
            
        <% }%>

    </script>
    <% }%>
</table>
