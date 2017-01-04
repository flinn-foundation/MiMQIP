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

            int mid = -1;
            int messageid = -1;
            int priority = -1;
            String message = "", messagetag = "";
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
                messageid = Integer.parseInt(request.getParameter("id"));
            }

            if (request.getParameter("Message") != null) {
                message = request.getParameter("Message");
            }

            if (request.getParameter("MessageTag") != null) {
                messagetag = request.getParameter("MessageTag");
            }

            if (request.getParameter("Priority") != null) {
                priority = Integer.parseInt(request.getParameter("Priority"));
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
                flinn.recommend.beans.request.RequestMessageBean input = new flinn.recommend.beans.request.RequestMessageBean();

                input.setMessageid(messageid);
                input.setMessage(message);
                input.setMessagetag(messagetag);
                input.setPriority(priority);

                if (messageid > 0) {
                    // Update message
                    rqcont.setMessage(input);
                    flinn.recommend.beans.response.ResponseMessageContainerBean rspBean = new flinn.recommend.beans.response.ResponseMessageContainerBean();

                    try {
                        rspBean = (flinn.recommend.beans.response.ResponseMessageContainerBean) dm.updateMessage(rqcont, userSession);
                        mid = rspBean.getMessage().getMessageid();
                    } catch (Exception e) {
                        dm.rollbackConnection("updateMessage");
                        dm.LOG.debug("Unable to commit changes to updateMessage");
                    }
                } else {
                    // Create message
                    try {
                        rqcont.setMessage(input);
                        mid = dm.createMessage(rqcont, userSession);
                    } catch (Exception e) {
                        dm.rollbackConnection("createMessage");
                        dm.LOG.debug("Unable to commit changes to createMessage");
                    }
                }

                // generate the response message
                if (mid > 0) {
                    if (messageid > 0) {
                        response.sendRedirect("/recommend/message_detail.jsp?id=" + mid + "&reason=message+changes+saved");
                    } else {
                        response.sendRedirect("/recommend/message_detail.jsp?id=" + mid + "&reason=message+created");
                    }
                } else {
                    response.sendRedirect("/admin/error.jsp?reason=" + mid);
                }

                messageid = mid;

            }


            flinn.recommend.beans.response.ResponseMessageBean adminMessage = new flinn.recommend.beans.response.ResponseMessageBean();
            if (messageid > 0) {

                try {
                    adminMessage = dm.findMessage(messageid);
                    messageid = adminMessage.getMessageid();
                    priority = adminMessage.getPriority();
                    message = adminMessage.getMessage();
                    messagetag = adminMessage.getMessagetag();
                } catch (Exception e) {
                    dm.LOG.debug("Unable to open connection findMessage");
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
        <input type="hidden" name="id" value="<% if (messageid > 0) {
                 out.print(messageid);
             } else {
                 out.print(0);
             }%>">
        <% }%>
        <tr>
            <td width="12" align="left"><img src="/recommend/images/s.gif" width=12 height=1 alt="" border="0"></td>
            <td nowrap><p class="formText">Message Tag</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <% if (editable) {
                                    out.print("<input type='text' name='MessageTag' maxlength='50' value='");
                                }%><% if (messageid > 0) {
                                                out.print(messagetag.replace("&","&amp;").replace("'", "&#039;"));
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
            <td nowrap><p class="formText">Message</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <% if (editable) {
                                    out.print("<textarea rows='6' cols='80' name='Message'>");
                                }
                                if (messageid > 0) {
                                    out.print(message.replace("&","&amp;").replace("'", "&#039;"));
                                }
                                if (editable) {
                                                    out.print("</textarea>");
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
            <td nowrap><p class="formText">Priority</p></td>
            <td align="right">
                <p class="formText" style="text-align:left;">
                    <% if (editable) {
                                    out.print("<input type='text' name='Priority' maxlength='30' value='");
                                }%><% if (messageid > 0) {
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
                            <div class="updateText"><a href="/recommend/message_detail.jsp?id=<% out.print(messageid);%>&edit=y" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Edit</a></div>
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
                                             if (messageid > 0) {
                                                 out.print(jspSelf);
                                                 out.print("?id=" + messageid);
                                             } else {
                                                 out.print("/recommend/message.jsp");
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
                    dm.disposeConnection("message_detail");
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
            frmvalidator.addValidation("MessageTag","req","Please enter a name for the message tag");
            frmvalidator.addValidation("MessageTag","maxlen=50","Max length for message is 50");
            frmvalidator.addValidation("Message","req","Please enter a message");
            frmvalidator.addValidation("Message","maxlen=50000","Max length for message is 50000");  
        <% }%>

    </script>
    <% }%>
</table>

