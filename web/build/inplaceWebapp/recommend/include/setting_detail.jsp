<%@page contentType="text/html" %> 
<%@page import="flinn.beans.response.ResponseSessionContainerBean"%>

<%
            int adminid = -1;
            String serverName = request.getServerName();
            Boolean isAdmin = false, isSuperAdmin = false;
            Boolean editable = false;
            Boolean postType = false;
            Boolean hasEdit = false;
            String jspSelf = request.getRequestURI().toString();

            int sid = -1;
            int settingid = -1;
            String name = "", value = "";
            int lastactivity = -1;

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
                settingid = Integer.parseInt(request.getParameter("id"));
            }

            if (request.getParameter("Name") != null) {
                name = request.getParameter("Name");
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

            if (request.getParameter("edit") != null) {
                if (request.getParameter("edit").equals("y")) {
                    hasEdit = true;
                }
            }


            if (isAdmin && postType) {
                flinn.beans.request.RequestContainerBean rqcont = new flinn.beans.request.RequestContainerBean();
                flinn.recommend.beans.request.RequestSettingBean input = new flinn.recommend.beans.request.RequestSettingBean();

                input.setSettingid(settingid);
                input.setSettingname(name);
                input.setSettingvalue(value);

                if (settingid > 0) {
                    // Update Setting
                    rqcont.setSetting(input);
                    flinn.recommend.beans.response.ResponseSettingContainerBean rspBean = new flinn.recommend.beans.response.ResponseSettingContainerBean();

                    try {
                        rspBean = (flinn.recommend.beans.response.ResponseSettingContainerBean) dm.updateSetting(rqcont, userSession);
                        sid = rspBean.getSetting().getSettingid();
                    } catch (Exception e) {
                        dm.rollbackConnection("updateSetting");
                        dm.LOG.debug("Unable to commit changes to updateSetting");
                    }
                } else {
                    // Create setting
                    try {
                        rqcont.setSetting(input);
                        sid = dm.createSetting(rqcont, userSession);
                    } catch (Exception e) {
                        dm.rollbackConnection("createSetting");
                        dm.LOG.debug("Unable to commit changes to createSetting");
                    }
                }

                // generate the response setting
                if (sid > 0) {
                    if (settingid > 0) {
                        response.sendRedirect("/recommend/setting_detail.jsp?id=" + sid + "&reason=setting+changes+saved");
                    } else {
                        response.sendRedirect("/recommend/setting_detail.jsp?id=" + sid + "&reason=setting+created");
                    }
                } else {
                    response.sendRedirect("/admin/error.jsp?reason=" + sid);
                }

                settingid = sid;

            }


            flinn.recommend.beans.response.ResponseSettingBean adminSetting = new flinn.recommend.beans.response.ResponseSettingBean();
            if (settingid > 0) {

                try {
                    adminSetting = dm.findSetting(settingid);
                    name = adminSetting.getSettingname();
                    value = adminSetting.getSettingvalue();
                } catch (Exception e) {
                    dm.LOG.debug("Unable to open connection findSetting");
                }


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
        <input type="hidden" name="id" value="<% if (settingid > 0) {
                 out.print(settingid);
             } else {
                 out.print(0);
             }%>">
        <% }%>
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">Name</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <% if (editable) {
                                    out.print("<input type='text' name='Name' maxlength='50' value='");
                                }%><% if (settingid > 0) {
                                                out.print(name.replace("&","&amp;").replace("'", "&#039;"));
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
            <td nowrap><p class="formText">Value</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <% if (editable) {
                                    out.print("<input type='text' name='Value' maxlength='50' value='");
                                }%><% if (settingid > 0) {
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
                            <div class="updateText"><a href="/recommend/setting_detail.jsp?id=<% out.print(settingid);%>&edit=y" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Edit</a></div>
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
                                             if (settingid > 0) {
                                                 out.print(jspSelf);
                                                 out.print("?id=" + settingid);
                                             } else {
                                                 out.print("/recommend/setting.jsp");
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
                    dm.disposeConnection("setting_detail");
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
            frmvalidator.addValidation("Name","req","Please enter a name");
            frmvalidator.addValidation("Name","maxlen=50","Max length for message is 50");
            frmvalidator.addValidation("Value","req","Please enter a value");
            frmvalidator.addValidation("Value","maxlen=50","Max length for message is 50");
        <% }%>

    </script>
    <% }%>
</table>

