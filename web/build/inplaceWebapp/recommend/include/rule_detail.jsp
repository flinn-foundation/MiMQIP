<%@page contentType="text/html" %>
<%@page import="flinn.beans.response.ResponseSessionContainerBean"%>
<%@page import="flinn.recommend.beans.RecommendDiagnosisBean"%>
<%@page import="flinn.recommend.beans.RecommendMessageBean"%>
<%@page import="flinn.recommend.beans.RecommendRuleCriteriaBean"%>
<%@page import="flinn.recommend.beans.response.ResponseMessageBean"%>
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

            int rid = -1;
            int ruleid = -1;
            int priority = -1;
            int truemessageid = -1, falsemessageid = -1;
            String ruletype = "", rulename = "";
            int lastactivity = -1;
            RecommendDiagnosisBean[] rulediagnoses = null;
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
                ruleid = Integer.parseInt(request.getParameter("id"));
            }

            if (request.getParameter("Priority") != null) {
                priority = Integer.parseInt(request.getParameter("Priority"));
            }

            if (request.getParameter("TrueMessage") != null) {
                truemessageid = Integer.parseInt(request.getParameter("TrueMessage"));
            }

            if (request.getParameter("FalseMessage") != null) {
                falsemessageid = Integer.parseInt(request.getParameter("FalseMessage"));
            }

            if (request.getParameter("RuleType") != null) {
                ruletype = request.getParameter("RuleType");
            }

            if (request.getParameter("RuleName") != null) {
                rulename = request.getParameter("RuleName");
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
                flinn.beans.request.RequestContainerBean rqcont = new flinn.beans.request.RequestContainerBean();
                flinn.recommend.beans.request.RequestRuleBean input = new flinn.recommend.beans.request.RequestRuleBean();

                input.setRuleid(ruleid);
                input.setPriority(priority);
                input.setRuletype(ruletype);
                input.setRulename(rulename);
                input.setDiagnoses(flinn.util.AdminFunctions.getDiagnosisFromForm(request));

                ArrayList<RecommendMessageBean> mb = new ArrayList<RecommendMessageBean>();
                mb.add(dm.findMessage(truemessageid).getRecommendMessageBean());
                if (falsemessageid > 0) {
                    mb.add(dm.findMessage(falsemessageid).getRecommendMessageBean());
                }
                input.setMessages(mb.toArray(new RecommendMessageBean[mb.size()]));

                Boolean isValid = false;
                if (request.getParameter("Valid") != null) {
                    if (request.getParameter("Valid").equals("1")) {
                        isValid = true;
                    }
                }
                input.setValid(isValid);


                // input.setMessages();

                if (ruleid > 0) {
                    // Update rule
                    rqcont.setRule(input);
                    flinn.recommend.beans.response.ResponseRuleContainerBean rspBean = new flinn.recommend.beans.response.ResponseRuleContainerBean();

                    try {
                        rspBean = (flinn.recommend.beans.response.ResponseRuleContainerBean) dm.updateRule(rqcont, userSession);
                        rid = rspBean.getRule().getRuleid();
                    } catch (Exception e) {
                        dm.rollbackConnection("updateRule");
                        dm.LOG.debug("Unable to commit changes to updateRule");
                    }
                } else {
                    // Create rule
                    try {
                        rqcont.setRule(input);
                        rid = dm.createRule(rqcont, userSession);
                    } catch (Exception e) {
                        dm.rollbackConnection("createRule");
                        dm.LOG.debug("Unable to commit changes to createRule");
                    }
                }

                // generate the response rule
                if (rid > 0) {
                    if (ruleid > 0) {
                        response.sendRedirect("/recommend/rule_detail.jsp?id=" + rid + "&reason=rule+changes+saved");
                    } else {
                        response.sendRedirect("/recommend/rule_detail.jsp?id=" + rid + "&reason=rule+created");
                    }
                } else {
                    response.sendRedirect("/admin/error.jsp?reason=" + rid);
                }

                ruleid = rid;

            }


            flinn.recommend.beans.response.ResponseRuleBean adminRule = new flinn.recommend.beans.response.ResponseRuleBean();
            if (ruleid > 0) {

                try {
                    adminRule = dm.findRule(ruleid);
                    ruleid = adminRule.getRuleid();
                    priority = adminRule.getPriority();
                    rulename = adminRule.getRulename();
                    ruletype = adminRule.getRuletype();
                    rulediagnoses = adminRule.getDiagnoses();
                    rulecriterion = adminRule.getCriteria();
                    truemessageid = adminRule.getMessages()[0].getMessageid();
                    falsemessageid = adminRule.getMessages()[1].getMessageid();


                } catch (Exception e) {
                    dm.LOG.debug("Unable to open connection findRule");
                }


            }

            editable = false;   // Whether or not this is to edit the information
            if (isAdmin && hasEdit) {
                editable = true;
            }
	    StringBuffer ruletypeSelect = new StringBuffer("<select name='RuleType'>");
	    ruletypeSelect.append("<option value='generalmessages'");
	    if (ruletype.equals("generalmessages")) ruletypeSelect.append(" selected");
	    ruletypeSelect.append(">generalmessages</option>");
	    ruletypeSelect.append("<option value='generalconsistency'");
	    if (ruletype.equals("generalconsistency")) ruletypeSelect.append(" selected");
	    ruletypeSelect.append(">generalconsistency</option>");
	    ruletypeSelect.append("<option value='additionalconsistency'");
	    if (ruletype.equals("additionalconsistency")) ruletypeSelect.append(" selected");
	    ruletypeSelect.append(">additionalconsistency</option>");
	    ruletypeSelect.append("<option value='treatmentmessages'");
	    if (ruletype.equals("treatmentmessages")) ruletypeSelect.append(" selected");
	    ruletypeSelect.append(">treatmentmessages</option>");
	    ruletypeSelect.append("<option value='specialmessages'");
	    if (ruletype.equals("specialmessages")) ruletypeSelect.append(" selected");
	    ruletypeSelect.append(">specialmessages</option>");
	    ruletypeSelect.append("<option value='clinicalresponse'");
	    if (ruletype.equals("clinicalresponse")) ruletypeSelect.append(" selected");
	    ruletypeSelect.append(">clinicalresponse</option>");
	    ruletypeSelect.append("<option value='medicaltrial'");
	    if (ruletype.equals("medicaltrial")) ruletypeSelect.append(" selected");
	    ruletypeSelect.append(">medicaltrial</option>");
	    ruletypeSelect.append("<option value='othersideeffects'");
	    if (ruletype.equals("othersideeffects")) ruletypeSelect.append(" selected");
	    ruletypeSelect.append(">othersideeffects</option>");
	    ruletypeSelect.append("<option value='otherreports'");
	    if (ruletype.equals("otherreports")) ruletypeSelect.append(" selected");
	    ruletypeSelect.append(">otherreports</option>");
	    ruletypeSelect.append("</select>");
	    ruletypeSelect.append("<br/>(Currently: "+ruletype+")");
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#CAD8DF">
    <% if (editable) {%>
    <script type="text/javascript" language="JavaScript" src="common/js/form_validate.js"></script>
    <FORM method="POST" action="<% out.print(jspSelf);%>" name="adminform">
        <input type="hidden" name="id" value="<% if (ruleid > 0) {
                 out.print(ruleid);
             } else {
                 out.print(0);
             }%>">
        <% }%>
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">Rule Type</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <% if (editable) {
                        out.print(ruletypeSelect);
                    } else if (ruleid > 0) {
                        out.print(ruletype.replace("&","&amp;").replace("'", "&#039;"));
                    }%></p></td>
            <td>&nbsp;</td>
        </tr>


        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">Diagnosis</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <%
                                if (editable) {
                                    //Pull all diagnoses
                                    flinn.recommend.beans.RecommendDiagnosisBean[] ar = dm.findAllDiagnoses();

                                    //Pull user role
                                    //flinn.beans.AppUserRoleBean[] urar = adminUser.getUser().getRoles();

                                    for (int i = 0; i < ar.length; i++) { //Loop thru all diagnoses
                                        if (!isSuperAdmin && i == 0) {
                                            i++;
                                        } //Skip Admin if user isn't a superadmin
                                        RecommendDiagnosisBean diagnoses = (RecommendDiagnosisBean) ar[i];
                                        out.print("<input type='checkbox' name='diagnoses_" + diagnoses.getDiagnosisid() + "' value='" + diagnoses.getDiagnosis() + "'");

                                        if (rulediagnoses != null) {
                                            for (int j = 0; j < rulediagnoses.length; j++) {
                                                RecommendDiagnosisBean diag = (RecommendDiagnosisBean) rulediagnoses[j];
                                                if (diagnoses.getDiagnosisid() == diag.getDiagnosisid()) {
                                                    out.print(" CHECKED");
                                                    break;
                                                }
                                            }
                                        }
                                        out.print(">" + diagnoses.getDiagnosis() + " Stage " + diagnoses.getStage() + "<br>\n");

                                    }
                                } else {
                                    if (rulediagnoses != null) {
                                        for (int i = 0; i < rulediagnoses.length; i++) {
                                            RecommendDiagnosisBean diagnoses = (RecommendDiagnosisBean) rulediagnoses[i];
                                            out.print(diagnoses.getDiagnosis() + " Stage " + diagnoses.getStage());
                                            if (i != rulediagnoses.length - 1) {
                                                out.print("<br/>");
                                            }
                                        }
                                    }
                                }
                    %></p></td>
            <td>&nbsp;</td>
        </tr>

        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">Rule Name</p></td>
            <td align="right">

                <p class="formText" style="text-align:left;">
                    <% if (editable) {
                                    out.print("<input type='text' name='RuleName' maxlength='50' value='");
                                }%><% if (ruleid > 0) {
                                                out.print(rulename.replace("&","&amp;").replace("'", "&#039;"));
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
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">True Message</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <%
                                if (editable) {
                                    //Pull all diagnoses
                                    List<ResponseMessageBean> lma = dm.findAllMessages(null, userSession, null);
                                    ResponseMessageBean[] ar = lma.toArray(new ResponseMessageBean[lma.size()]);
                                    out.print("<SELECT name='TrueMessage'>");

                                    for (int i = 0; i < ar.length; i++) { //Loop thru all messages
                                        ResponseMessageBean message = (ResponseMessageBean) ar[i];
                                        out.print("<option value='");
                                        out.print(message.getMessageid());
                                        out.print("'");
                                        if (truemessageid == message.getMessageid()) {
                                            out.print(" SELECTED");
                                        }
                                        out.print(">");
                                        out.print(message.getMessagetag());
                                        out.print("</option>");

                                    }
                                    out.print("</SELECT>");
                                } else {
                                    if (truemessageid > 0) {
                                        out.print((dm.findMessage(truemessageid)).getMessagetag());
                                    }
                                }
                    %></p></td>
            <td>&nbsp;</td>
        </tr>


        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">False Message</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <%
                                if (editable) {
                                    //Pull all diagnoses
                                    List<ResponseMessageBean> lma = dm.findAllMessages(null, userSession, null);
                                    ResponseMessageBean[] ar = lma.toArray(new ResponseMessageBean[lma.size()]);
                                    out.print("<SELECT name='FalseMessage'>");
                                    out.print("<option value='0'></option>");

                                    for (int i = 0; i < ar.length; i++) { //Loop thru all messages
                                        ResponseMessageBean message = (ResponseMessageBean) ar[i];
                                        out.print("<option value='");
                                        out.print(message.getMessageid());
                                        out.print("'");
                                        if (falsemessageid == message.getMessageid()) {
                                            out.print(" SELECTED");
                                        }
                                        out.print(">");
                                        out.print(message.getMessagetag());
                                        out.print("</option>");

                                    }
                                    out.print("</SELECT>");
                                } else {
                                    if (falsemessageid > 0) {
                                        out.print((dm.findMessage(falsemessageid)).getMessagetag());
                                    }
                                }
                    %></p></td>
            <td>&nbsp;</td>
        </tr>






        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">Priority</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <% if (editable) {
                                    out.print("<input type='text' name='Priority' maxlength='30' value='");
                                }%><% if (ruleid > 0) {
                                                out.print(priority);
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
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">Valid?</p></td>
            <td align="left"><p><%
                        if (editable) {
                            out.print("<input type='radio' name='Valid' value='1'");
                            if (rid <= 0) {
                                out.print(" CHECKED");
                            } else if (adminRule.getValid() != null && adminRule.getValid().booleanValue()) {
                                out.print(" CHECKED");
                            }
                            out.print("> valid<br>\n");
                            out.print("<input type='radio' name='Valid' value='0'");
                            if (rid > 0 && !adminRule.getValid()) {
                                out.print(" CHECKED");
                            }
                            out.print("> invalid<br>\n");
                        } else {
                            out.print("<p class='formText' style='text-align:left;'>");
                            if (adminRule.getValid() != null && adminRule.getValid().booleanValue()) {
                                out.print("<span>valid</span>");
                            } else {
                                out.print("<span class=\"formTextRed\">invalid</span>");
                            }
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
                            <div class="updateText"><a href="/recommend/rule_detail.jsp?id=<% out.print(ruleid);%>&edit=y" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Edit</a></div>
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
        <tr>
            <td><img src="/recommend/images/s.gif" alt="" border="0" height="1" width="1"></td>
            <td colspan="2" style="background-repeat: repeat-x;" background="/admin/images/row_separator.gif"></td>
            <td><img src="/recommend/images/s.gif" alt="" border="0" height="1" width="1"></td>
        </tr>
        <tr>
            <td align="left" width="12"><img src="/recommend/images/s.gif" alt="" border="0" height="1" width="12"></td>
            <td nowrap="nowrap"><p class="formText">Criteria</p></td>
            <td align="right">
                <p class="formText" style="text-align: left;">
                    <%
		    if (rulecriterion != null) {
                                            for (int j = 0; j < rulecriterion.length; j++) {
                                                RecommendRuleCriteriaBean crit = (RecommendRuleCriteriaBean) rulecriterion[j];
                                                out.print(crit.getElement() + " " + crit.getOperator() + " " + crit.getValue());
                    %>
                    &nbsp;&nbsp;<a href="javascript:AddCriteriaWindow(<% out.print(ruleid);%>,<% out.print(crit.getCriteriaid());%>)" class="subLink">
                        <img src="/recommend/images/blue_arrow.gif" alt="" border="0" height="10" width="5"> Edit</a>
                    &nbsp;&nbsp;<a href="javascript:DeleteCriteria(<% out.print(ruleid);%>,<% out.print(crit.getCriteriaid());%>)" class="subLink">
                        <img src="/recommend/images/blue_arrow.gif" alt="" border="0" height="10" width="5"> Delete</a><br />
                        <%

                                                }
		    }
                        %>

                    <a href="javascript:AddCriteriaWindow(<% out.print(ruleid);%>)" class="subLink">
                        <img src="/recommend/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Add New Criteria</a>
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
                                                    if (ruleid > 0) {
                                                        out.print(jspSelf);
                                                        out.print("?id=" + ruleid);
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
        <%
                    dm.disposeConnection("rule_detail");
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
            frmvalidator.addValidation("RuleName","req","Please enter a name for the rule");
            frmvalidator.addValidation("RuleName","maxlen=50","Max length for rule name  is 50");
            frmvalidator.addValidation("RuleType","req","Please enter a type for the rule");
            frmvalidator.addValidation("RuleType","maxlen=50","Max length for rule type  is 50");
        <% }%>

    </script>
    <% }%>
</table>

