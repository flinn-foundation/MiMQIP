<%@page contentType="text/html" %>
<%@page import="flinn.beans.response.ResponseSessionContainerBean"%>
<%@page import="flinn.recommend.beans.RecommendDiagnosisBean"%>
<%@page import="flinn.recommend.beans.RecommendMessageBean"%>
<%@page import="flinn.recommend.beans.RecommendRuleCriteriaBean"%>
<%@page import="flinn.recommend.beans.response.ResponseMessageBean"%>
<%@page import="flinn.recommend.beans.response.ResponseRuleBean"%>
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

            int rid = -1;
            int ruleid = -1;
            int delid = -1;
            int criteriaid = -1;
            int priority = -1;
            int lastactivity = -1;
            String type = "", fieldname = "", operator = "", value = "";
            RecommendRuleCriteriaBean[] rulecriterion = null;

            String[] types = {"Numeric", "NumericValue", "Text", "TextValue"};
            String[] operators = {"<", "<=", "=", ">=", ">", "!="};


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

            if (request.getParameter("ruleid") != null) {
                ruleid = Integer.parseInt(request.getParameter("ruleid"));
            }

            if (request.getParameter("id") != null) {
                if (!request.getParameter("id").equals("undefined")) {
                    criteriaid = Integer.parseInt(request.getParameter("id"));
                }
            }

            if (request.getParameter("delid") != null) {
                if (!request.getParameter("delid").equals("undefined")) {
                    criteriaid = Integer.parseInt(request.getParameter("delid"));
                    delFlag = true;
                }
            }


            if (request.getParameter("Priority") != null) {
                priority = Integer.parseInt(request.getParameter("Priority"));
            }

            if (request.getParameter("Type") != null) {
                type = request.getParameter("Type");
            }

            if (request.getParameter("FieldName") != null) {
                fieldname = request.getParameter("FieldName");
            }

            if (request.getParameter("Operator") != null) {
                operator = request.getParameter("Operator");
            }

            if (request.getParameter("Value") != null) {
                value = request.getParameter("Value");
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

                if (ruleid > 0) {
                    // Update rule with criteria

                    flinn.recommend.beans.response.ResponseRuleBean rule = new flinn.recommend.beans.response.ResponseRuleBean();
                    try {
                        rule = dm.findRule(ruleid);

                        if (rule != null) {
                            rulecriterion = rule.getCriteria();
                            if (criteriaid > 0 && rulecriterion != null) {
                                if (delFlag) {
                                    // Delete criteria

                                    // convert array to arraylist and back again to delete an element
                                    List<flinn.recommend.beans.RecommendRuleCriteriaBean> critList = new ArrayList<flinn.recommend.beans.RecommendRuleCriteriaBean>();
                                    for (int i = 0; i < rulecriterion.length; i++) {
                                        if (rulecriterion[i].getCriteriaid() != criteriaid) {
                                            critList.add(rulecriterion[i]);
                                        }
                                    }

                                    RecommendRuleCriteriaBean[] ar = new RecommendRuleCriteriaBean[rulecriterion.length - 1];
                                    rulecriterion = critList.toArray(ar);

                                } else {
                                    // Update criteria
                                    for (int i = 0; i < rulecriterion.length; i++) {
                                        if (rulecriterion[i].getCriteriaid() == criteriaid) {

                                            rulecriterion[i].setPriority(priority);
                                            rulecriterion[i].setType(type);
                                            rulecriterion[i].setElement(fieldname);
                                            rulecriterion[i].setOperator(operator);
                                            rulecriterion[i].setValue(value);
                                            break;
                                        }
                                    }
                                }
                            } else {
                                // Create criteria
                                flinn.recommend.beans.RecommendRuleCriteriaBean rcb = new flinn.recommend.beans.RecommendRuleCriteriaBean();

                                rcb.setRuleid(ruleid);
                                rcb.setPriority(priority);
                                rcb.setType(type);
                                rcb.setElement(fieldname);
                                rcb.setOperator(operator);
                                rcb.setValue(value);

                                // convert array to arraylist and back again to add another element
                                List<flinn.recommend.beans.RecommendRuleCriteriaBean> critList = new ArrayList<flinn.recommend.beans.RecommendRuleCriteriaBean>();
				int critLength = 0;
				if (rulecriterion != null) {
                                	critList.addAll(Arrays.asList(rulecriterion));
					critLength = rulecriterion.length;
				}
                                critList.add(rcb);
                                RecommendRuleCriteriaBean[] ar = new RecommendRuleCriteriaBean[critLength + 1];
                                rulecriterion = critList.toArray(ar);

                            }

                            flinn.recommend.beans.request.RequestRuleBean input = new flinn.recommend.beans.request.RequestRuleBean();
                            flinn.beans.request.RequestContainerBean rqcont = new flinn.beans.request.RequestContainerBean();
                            input.setRuleid(ruleid);
                            input.setCriteria(rulecriterion);
                            rqcont.setRule(input);
                            flinn.recommend.beans.response.ResponseRuleContainerBean rspBean = new flinn.recommend.beans.response.ResponseRuleContainerBean();

                            try {
                                rspBean = (flinn.recommend.beans.response.ResponseRuleContainerBean) dm.updateRule(rqcont, userSession);
                                rid = rspBean.getRule().getRuleid();
                                closeUpdateFlag = true;
                            } catch (Exception e) {
                                dm.rollbackConnection("updateRule");
                                dm.LOG.debug("Unable to commit changes to updateRule");
                            }

                        }


                    } catch (Exception e) {
                        dm.LOG.debug("Unable to open connection findRule (1) where ruleid = " + ruleid + e.getMessage());
                    }


                } else {
                    // Shouldn't be able to create a new rule... it must already exist
                    dm.LOG.debug("Unable to create a new rule when working with criteria");
                }


            }

            if (ruleid > 0) {
                RecommendRuleCriteriaBean adminCriteria = null;
                flinn.recommend.beans.response.ResponseRuleBean rule = new flinn.recommend.beans.response.ResponseRuleBean();
                try {
                    rule = dm.findRule(ruleid);
                } catch (Exception e) {
                    dm.LOG.debug("Unable to open connection findRule (2)");
                }
                if (rule != null) {
                    rulecriterion = rule.getCriteria();
		    if (rulecriterion != null) {
                    	for (int i = 0; i < rulecriterion.length; i++) {
                   	     if (rulecriterion[i].getCriteriaid() == criteriaid) {
                   	         // populate the criteria object
                   	         adminCriteria = rulecriterion[i];
                   	         priority = adminCriteria.getPriority();
                   	         type = adminCriteria.getType();
                   	         fieldname = adminCriteria.getElement();
                   	         operator = adminCriteria.getOperator();
                   	         value = adminCriteria.getValue();
                    	    }
                    	}
		    }
                }
            }

            editable = false;   // Whether or not this is to edit the information
            if (isAdmin && hasEdit) {
                editable = true;
            }
%>

<% if (closeUpdateFlag) {%>
<script type="text/javascript" language="JavaScript">
    updateParent();
</script>
<% }%>


<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#CAD8DF">
    <% if (editable) {%>
    <script type="text/javascript" language="JavaScript" src="common/js/form_validate.js"></script>
    <FORM method="POST" action="<% out.print(jspSelf);%>" name="adminform">
        <input type="hidden" name="ruleid" value="<% if (ruleid > 0) {
                 out.print(ruleid);
             } else {
                 out.print(0);
             }%>">
        <input type="hidden" name="id" value="<% if (criteriaid > 0) {
                 out.print(criteriaid);
             } else {
                 out.print(0);
             }%>">
        <% }%>


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
                                }%><% if (criteriaid > 0) {
                                                out.print(priority);
                                            }%><% if (editable) {
                                                            out.print("'>");
                                                        }%></p></td>
            <td>&nbsp;</td>
        </tr>

        <!-- Type -->


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
                    <%
                                if (editable) {
                                    out.print("<SELECT name='Type'>");

                                    for (int i = 0; i < types.length; i++) { //Loop thru all types
                                        out.print("<option value='");
                                        out.print(types[i]);
                                        out.print("'");
                                        if (type.equals(types[i])) {
                                            out.print(" SELECTED");
                                        }
                                        out.print(">");
                                        out.print(types[i]);
                                        out.print("</option>");

                                    }
                                    out.print("</SELECT>");
                                } else {
                                    out.print(type);
                                }
                    %></p></td>
            <td>&nbsp;</td>
        </tr>

        <!-- FieldName -->

        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">Field Name</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <% if (editable) {
                                    out.print("<input type='text' name='FieldName' maxlength='50' size='50' value='");
                                }%><% if (criteriaid > 0) {
                                                out.print(fieldname.replace("&","&amp;").replace("'", "&#039;"));
                                            }%><% if (editable) {
                                                            out.print("'>");
                                                        }%></p></td>
            <td>&nbsp;</td>
        </tr>


        <!-- Operator -->


        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">Operator</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <%
                                if (editable) {
                                    out.print("<SELECT name='Operator'>");

                                    for (int i = 0; i < operators.length; i++) {
                                        out.print("<option value='");
                                        out.print(operators[i]);
                                        out.print("'");
                                        if (operator.equals(operators[i])) {
                                            out.print(" SELECTED");
                                        }
                                        out.print(">");
                                        out.print(operators[i]);
                                        out.print("</option>");

                                    }
                                    out.print("</SELECT>");
                                } else {
                                    out.print(operator);
                                }
                    %></p></td>
            <td>&nbsp;</td>
        </tr>







        <!-- Value -->

        <tr>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
            <td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
            <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        </tr>
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">Value</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <% if (editable) {
                                    out.print("<input type='text' name='Value' maxlength='50' value='");
                                }%><% if (criteriaid > 0) {
                                                out.print(value.replace("&","&amp;").replace("'", "&#039;"));
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
            frmvalidator.addValidation("FieldName","req","Please enter a value for the field name");
            frmvalidator.addValidation("FieldName","maxlen=50","Max length for field name  is 50");
            frmvalidator.addValidation("Priority","req","Please enter a value for the field priority");
            frmvalidator.addValidation("Priority","maxlen=50","Max length for field priority  is 50");
            frmvalidator.addValidation("Priority","numeric","The entered value must be numeric");
            frmvalidator.addValidation("Value","req","Please enter a value");
            frmvalidator.addValidation("Value","maxlen=50","Max length for value  is 50");
        <% }%>

    </script>
    <% }%>
</table>

