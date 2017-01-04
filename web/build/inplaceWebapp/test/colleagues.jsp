<%
int diagnosisid = Integer.parseInt(request.getParameter("d"));
flinn.dao.DaoAppManager dm = new flinn.dao.DaoAppManager();
java.sql.Connection conn = null;
java.util.HashMap<String,Float> totals = null;
flinn.recommend.dao.imp.MessageDaoImp md = new flinn.recommend.dao.imp.MessageDaoImp();

try {
	conn = dm.getConnection();
	totals = new flinn.dao.imp.PatientDaoImp().handlePatientPrescriptionByDiagnosis(diagnosisid, conn);
} catch (Exception e) {
} finally {
	conn.close();
	conn = null;
	dm.disposeConnection("coll");
}
int messageid = 87+diagnosisid;

java.lang.StringBuffer outbuff = new java.lang.StringBuffer();
if (totals != null) {
	outbuff.append("Your colleagues have prescribed the following medications with the associated frequencies over the past 6 months to patients with the same diagnosis and stage as this patient.<br/><br/>");
	java.util.ArrayList<String> keys = new java.util.ArrayList<String>();
	java.util.Iterator<String> keyit = totals.keySet().iterator();
	while (keyit.hasNext()) {
		String key = keyit.next();
		boolean done = false;
		for (int i=0; i<keys.size() && !done; i++) {
			if (totals.get(keys.get(i)).floatValue()-totals.get(key).floatValue() < 0) {
				keys.add(i, key);
				done = true;
			}
		}
		if (!done) keys.add(key);
	}
	for (int i=0; i<keys.size(); i++) {
		outbuff.append(keys.get(i)+": "+totals.get(keys.get(i))+"%<br/>\n");
	}

} else {
	outbuff.append("There is currently not sufficient data in the MiMQIP system to allow an analysis of your colleagues' pharmacological treatment of patients with this diagnosis and stage of treatment.");
}

String message = "SAVED";
try {
	conn = dm.getConnection();
	flinn.recommend.beans.request.RequestMessageBean rmb = new flinn.recommend.beans.request.RequestMessageBean();
	rmb.setMessageid(messageid);
	java.util.List<flinn.recommend.beans.response.ResponseMessageBean> messages = md.find(rmb, null, conn);
	if (messages != null && messages.size() > 0) {
		rmb = new flinn.recommend.beans.request.RequestMessageBean(messages.get(0));
		rmb.setMessageid(messageid);
		rmb.setMessage(outbuff.toString());
		md.update(rmb, messages.get(0), conn);
	} else {
		message = "Not saved: message not found";
	}
} catch (Exception e) {
	message = "Not saved: "+e.toString();
} finally {
	conn.close();
	conn = null;
	dm.disposeConnection("coll");
}
%>
<%=outbuff.toString()%>
<br/><br/><br/>
<%=message%>
