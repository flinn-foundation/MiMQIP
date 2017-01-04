<%@page contentType="text/html" %>
<%@page import="flinn.beans.response.ResponseSessionContainerBean"%>
<%@page import="flinn.recommend.beans.RecommendTreatmentGuidelineBean"%>
<%@page import="flinn.recommend.beans.RecommendDiagnosisBean"%>
<%@page import="flinn.beans.response.ResponseTreatmentBean"%>
<%@page import= "flinn.recommend.dao.imp.RuleDaoImp"%>
<%@page import="java.util.Arrays" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>


<%
            int adminid = -1;
            String serverName = request.getServerName();
            Boolean isAdmin = false, isSuperAdmin = false;
            Boolean editable = false;
            Boolean postType = false;
            Boolean hasEdit = true;
            Boolean delFlag = false;
            Boolean closeUpdateFlag = false;
            String jspSelf = request.getRequestURI().toString();

            int did = -1;
            int diagnosisid = -1;
            int delid = -1;
            int treatmentid = -1;
            int guidelineid = -1;
            int lastactivity = -1;
            int row = -1;
            int drug = -1;
            String treatment = null;


            List<ResponseTreatmentBean> treatmentList = null;
            ResponseTreatmentBean[] treatmentArray = null;
            RecommendTreatmentGuidelineBean[] oldTreatments = null;
            RecommendTreatmentGuidelineBean[] treatments = null;

            RecommendDiagnosisBean diagnosis = null;

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

            if (request.getParameter("diagid") != null) {
                diagnosisid = Integer.parseInt(request.getParameter("diagid"));
            }

            if (request.getParameter("treatid") != null) {
                if (!request.getParameter("treatid").equals("undefined")) {
                treatmentid = Integer.parseInt(request.getParameter("treatid"));
}            }

            if (request.getParameter("guideid") != null) {
                if (!request.getParameter("guideid").equals("undefined")) {
                    guidelineid = Integer.parseInt(request.getParameter("guideid"));
                }
            }

            if (request.getParameter("delid") != null) {
                if (!request.getParameter("delid").equals("undefined")) {
                    guidelineid = Integer.parseInt(request.getParameter("delid"));
                    delFlag = true;
                }
            }

            if (request.getParameter("Row") != null) {
                row = Integer.parseInt(request.getParameter("Row"));
            }

            if (request.getParameter("Drug") != null) {
                drug = Integer.parseInt(request.getParameter("Drug"));
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

            if (isAdmin && (postType || delFlag)) {
                if (diagnosisid > 0 && treatmentid > 0 && guidelineid > 0) {
                    treatments = new RuleDaoImp().getTreatmentGuidelinesByDiagnosis(diagnosisid, dm.getConnection());
                    oldTreatments = (RecommendTreatmentGuidelineBean[]) treatments.clone();


                    if (delFlag) {
                        // delete guideline
                        dm.LOG.debug("Deleting guideline id = " + guidelineid);

                        // convert array to arraylist and back again to delete an element
                        List<flinn.recommend.beans.RecommendTreatmentGuidelineBean> guideList = new ArrayList<flinn.recommend.beans.RecommendTreatmentGuidelineBean>();

                        for (int i = 0; i < treatments.length; i++) {
                            if (treatments[i].getTreatmentguidelineid() != guidelineid) {
                                guideList.add(treatments[i]);
                            }
                        }

                        RecommendTreatmentGuidelineBean[] ar = new RecommendTreatmentGuidelineBean[treatments.length - 1];
                        treatments = guideList.toArray(ar);


                    } else {
                        // update guideline
                        dm.LOG.debug("Updating guidelines");

                        for (int i = 0; i < treatments.length; i++) {
                            if (treatments[i].getTreatmentguidelineid() == guidelineid) {
                                treatments[i].setTreatmentid(treatmentid);
                                treatments[i].setRow(row);
                                treatments[i].setDrug(drug);
                                break;
                            }
                        }

                    }
                } else {
                    // create guideline
                    dm.LOG.debug("Creating guideline");
                    flinn.recommend.beans.RecommendTreatmentGuidelineBean rtgb = new flinn.recommend.beans.RecommendTreatmentGuidelineBean();

                    rtgb.setTreatmentid(treatmentid);
                    rtgb.setDiagnosisid(diagnosisid);

                    rtgb.setRow(row);
                    rtgb.setDrug(drug);

                    // convert array to arraylist and back again to add another element
                    List<flinn.recommend.beans.RecommendTreatmentGuidelineBean> guideList = new ArrayList<flinn.recommend.beans.RecommendTreatmentGuidelineBean>();
                    if (treatments != null) {
                        guideList.addAll(Arrays.asList(treatments));
                    }
                    guideList.add(rtgb);
                    int guidelength = 0;
                    if (treatments != null) {
                        guidelength = treatments.length;
                    }
                    RecommendTreatmentGuidelineBean[] ar = new RecommendTreatmentGuidelineBean[guidelength + 1];
                    treatments = guideList.toArray(ar);

                }

                // save changes
                dm.LOG.debug("Saving guidelines");
                new RuleDaoImp().saveTreatmentGuidelinesByDiagnosis(treatments, oldTreatments, dm.getConnection());
                dm.commitConnection();
                closeUpdateFlag = true;

            }

            if (diagnosisid > 0) {

                treatments = new RuleDaoImp().getTreatmentGuidelinesByDiagnosis(diagnosisid, dm.getConnection());

                if (treatments != null) {
                    for (int i = 0; i < treatments.length; i++) {
                        if (treatments[i].getTreatmentguidelineid() == guidelineid) {
                            row = treatments[i].getRow();
                            drug = treatments[i].getDrug();
                            treatment = treatments[i].getTreatment().getTreatmentname();
                            break;
                        }
                    }
                }
            }
            // get a list of all active treatment options ordered by treatment name
            treatmentList = dm.findAllTreatments(null, userSession, true, isSuperAdmin, "TreatmentName");
            editable = false;   // Whether or not this is to edit the information
            if (isAdmin && hasEdit) {
                editable = true;
            }
%>

<% if (closeUpdateFlag) {
                if (delFlag) {
%>
<script type="text/javascript" language="JavaScript">
    refreshDiagnosis(<% out.print(diagnosisid);%>);
</script>
<%

                } else {
%>
<script type="text/javascript" language="JavaScript">
    updateParent();
</script>
<% }
            }%>


<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#CAD8DF">
    <% if (editable) {%>
    <script type="text/javascript" language="JavaScript" src="common/js/form_validate.js"></script>
    <FORM method="POST" action="<% out.print(jspSelf);%>" name="adminform">
        <input type="hidden" name="diagid" value="<% if (diagnosisid > 0) {
                 out.print(diagnosisid);
             } else {
                 out.print(0);
             }%>">
        <input type="hidden" name="guideid" value="<% if (guidelineid > 0) {
                 out.print(guidelineid);
             } else {
                 out.print(0);
             }%>">
        <% }%>

        <!-- Treatment -->

        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">Type</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <%                        if (editable) {
                                    out.print("<SELECT name='treatid'>");

                                    for (int i = 0; i < treatmentList.size(); i++) { //Loop thru all treatments
                                        String name = treatmentList.get(i).getTreatmentname();
                                        out.print("<option value='");
                                        out.print(treatmentList.get(i).getTreatmentid());
                                        out.print("'");
					if (treatment != null) {
                                        if (treatment.equals(name)) {
                                            out.print(" SELECTED");
                                        }}
                                        out.print(">");
                                        out.print(name);
                                        out.print("</option>");

                                    }
                                    out.print("</SELECT>");
                                } else {
					if (treatment != null) {
                                    out.print(treatment);
                                    }
                                }
                    %></p></td>
            <td>&nbsp;</td>
        </tr>

        <!-- Row -->

        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">Row</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <% if (editable) {
                                    out.print("<input type='text' name='Row' maxlength='10' value='");
                                                    }%><% if (treatmentid
                                                                        > 0) {
                                                                    out.print(row);
                                                                }%><% if (editable) {
                                                            out.print("'>");
                                                        }%></p></td>
            <td>&nbsp;</td>
        </tr>

        <!-- Drug -->

        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">Drug</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <% if (editable) {
                                    out.print("<input type='text' name='Drug' maxlength='10' value='");
                                                    }%><% if (treatmentid
                                                                        > 0) {
                                                                    out.print(drug);
                                                                }%><% if (editable) {
                                                            out.print("'>");
                                                        }%></p></td>
            <td>&nbsp;</td>
        </tr>


        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
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
                            <div class="updateText"><a href="/recommend/diagnosis_detail.jsp?id=<% out.print(diagnosisid);%>&edit=y" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Edit</a></div>
                        </td>
                    </tr>
                </table>
            </td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
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
                            <div class="updateText"><a href="javascript:window.close()" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Cancel Updates</a></div></td>
                    </tr>
                </table>
            </td>
            <td>&nbsp;</td>
        </tr>
        <%                    }
        %>
        <%
                    dm.disposeConnection(
                            "treatment_detail");




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
            frmvalidator.addValidation("Row","req","Please enter a value for the field row");
            frmvalidator.addValidation("Row","maxlen=10","Max length for field priority is 10");
            frmvalidator.addValidation("Row","numeric","The entered value must be numeric");
            frmvalidator.addValidation("Drug","req","Please enter a value for the field drug");
            frmvalidator.addValidation("Drug","maxlen=10","Max length for field drug is 10");
            frmvalidator.addValidation("Drug","numeric","The entered value must be numeric");
        <% }%>

    </script>
    <% }%>
</table>

