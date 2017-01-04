<%
String userlogin = "", role = "";
if (request.getParameter("userlogin") != null){
	userlogin = request.getParameter("userlogin");
}
if (request.getParameter("role") != null){
	role = request.getParameter("role");
}
%>
</tr>
<tr valign="top" bgColor="#7F7F7F">
        <td width="199" valign="center" align="left">
	<p class="pageFooter">login name:  <%=userlogin%></p>
        </td>
        <td width=1 bgColor="#E6E5E3"><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
        <td valign="center" align="left"><p class="pageFooter">assigned roles:  <%=role%></p></td>
</tr>
</table>
</body>
</html>