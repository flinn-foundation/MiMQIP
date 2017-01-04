package flinn.recommend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import flinn.dao.imp.TreatmentDaoImp;
import flinn.recommend.beans.RecommendDiagnosisBean;
import flinn.recommend.beans.RecommendMessageBean;
import flinn.recommend.beans.RecommendRuleCriteriaBean;
import flinn.recommend.beans.RecommendTreatmentGuidelineBean;
import flinn.recommend.beans.request.RequestRuleBean;
import flinn.recommend.beans.response.ResponseRuleBean;
import flinn.util.cache.EHCacheImpl;
import flinn.util.cache.ICache;

public abstract class RuleDao {

	protected static final Logger LOG = Logger.getLogger(RuleDao.class);
	static {
		LOG.debug("Log appender instantiated for " + RuleDao.class);
	}
	public static final String WHERE = " where ";
	public static final String AND = " and ";
	public static final String EQUALS_QUESTION = " = ? ";

	private ResponseRuleBean getBeanFromRs(final ResultSet resultSet,
			Connection connection) throws Exception {
		final ResponseRuleBean bean = new ResponseRuleBean();
		bean.setRuleid(resultSet.getInt("RuleID"));
		bean.setPriority(resultSet.getInt("Priority"));
		bean.setValid(resultSet.getBoolean("Valid"));
		bean.setRuletype(resultSet.getString("RuleType"));
		bean.setRulename(resultSet.getString("RuleName"));
		if (bean.getRuleid() > 0) {
			bean.setDiagnoses(getDiagnoses(bean.getRuleid(), connection));
			bean.setCriteria(getCriteria(bean.getRuleid(), connection));
			bean.setMessages(getMessages(bean.getRuleid(), connection,
					resultSet.getInt("TrueMessageID"),
					resultSet.getInt("FalseMessageID")));
		}
		return bean;
	}

	public List<ResponseRuleBean> getListFromRs(final ResultSet resultSet,
			Connection connection) throws Exception {
		final List<ResponseRuleBean> results = new ArrayList<ResponseRuleBean>();
		ResponseRuleBean bean = null;
		while (resultSet.next()) {
			bean = getBeanFromRs(resultSet, connection);
			results.add(bean);
		}
		return results;
	}

	public int create(final RequestRuleBean bean, Connection connection)
			throws Exception {
		int ruleid = 0;
		
		clearRulesCache();
		
		if (bean != null) {
			if (connection == null || connection.isClosed()) {
				LOG.debug("bad DB Connection");
				throw new Exception("bad DB Connection");
			}

			final String command = "insert into RecommendRule (Priority, Valid, TrueMessageID, FalseMessageID, RuleType, RuleName) "
					+ " values(?, ?, ?, ?, ?, ?)";

			LOG.warn("Running query: " + command);
			PreparedStatement insertStatement = connection
					.prepareStatement(command);

			// Insert into AppUser
			insertStatement.setInt(1, bean.getPriority());
			insertStatement.setBoolean(2, bean.getValid());
			if (bean.getMessages() != null && bean.getMessages().length > 0) {
				insertStatement.setInt(3,
						(bean.getMessages())[0].getMessageid());
			} else {
				insertStatement.setInt(3, 0);
			}
			if (bean.getMessages() != null && bean.getMessages().length > 1) {
				insertStatement.setInt(4,
						(bean.getMessages())[1].getMessageid());
			} else {
				insertStatement.setInt(4, 0);
			}
			insertStatement.setString(5, bean.getRuletype());
			insertStatement.setString(6, bean.getRulename());
			insertStatement.executeUpdate();

			ResultSet rs = insertStatement.getGeneratedKeys();
			if (rs.next()) {
				ruleid = rs.getInt(1);
			}

			if (ruleid > 0) {
				saveDiagnoses(ruleid, bean.getDiagnoses(), null, connection);
				saveCriteria(ruleid, bean.getCriteria(), null, connection);
			}

			insertStatement.close();
		}
		return ruleid;
	}

	public List<ResponseRuleBean> find(final RequestRuleBean bean,
			final String orderBy, Connection connection) throws Exception {
		List<ResponseRuleBean> results = null;

		if (connection == null || connection.isClosed()) {
			LOG.debug("bad DB Connection");
			throw new Exception("bad DB Connection");
		}

		String query = fillInColumnNames(bean);
		if (orderBy != null) {
			query += " ORDER BY " + orderBy;
		}

		LOG.warn("Going to run query = " + query);

		final PreparedStatement preparedStatementQuery = connection
				.prepareStatement(query);

		fillInColumnValues(bean, preparedStatementQuery);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try {
			results = getListFromRs(resultSet, connection);
		} finally {
			resultSet.close();
			preparedStatementQuery.close();
		}
		return results;
	}

	// Order must be the same as fillInColumnNames
	private void fillInColumnValues(final RequestRuleBean bean,
			final PreparedStatement preparedStatementQuery) throws SQLException {
		int index = 1;

		if (bean.getPriority() != 0) {
			preparedStatementQuery.setInt(index, bean.getPriority());
			index++;
		}
		if (bean.getValid() != null) {
			preparedStatementQuery.setBoolean(index, bean.getValid());
			index++;
		}
		if (bean.getMessages() != null && bean.getMessages().length > 0) {
			preparedStatementQuery.setInt(index,
					bean.getMessages()[0].getMessageid());
			index++;
		}
		if (bean.getMessages() != null && bean.getMessages().length > 1) {
			preparedStatementQuery.setInt(index,
					bean.getMessages()[1].getMessageid());
			index++;
		}
		if (bean.getRuletype() != null) {
			preparedStatementQuery.setString(index, bean.getRuletype());
			index++;
		}
		if (bean.getRulename() != null) {
			preparedStatementQuery.setString(index, bean.getRulename());
			index++;
		}
		if (bean.getRuleid() != 0) {
			preparedStatementQuery.setInt(index, bean.getRuleid());
			index++;
		}
	}

	// Order must be the same as fillInColumnValues
	private String fillInColumnNames(final RequestRuleBean bean) {
		String query = "Select * from RecommendRule ";
		boolean first = true;

		if (bean.getPriority() != 0) {
			query = query + " " + (first ? WHERE : AND) + "Priority"
					+ EQUALS_QUESTION;
			first = false;
		}
		if (bean.getValid() != null) {
			query = query + " " + (first ? WHERE : AND) + "Valid"
					+ EQUALS_QUESTION;
			first = false;
		}
		if (bean.getMessages() != null && bean.getMessages().length > 0) {
			query = query + " " + (first ? WHERE : AND) + "TrueMessageID"
					+ EQUALS_QUESTION;
			first = false;
		}
		if (bean.getMessages() != null && bean.getMessages().length > 1) {
			query = query + " " + (first ? WHERE : AND) + "FalseMessageID"
					+ EQUALS_QUESTION;
			first = false;
		}
		if (bean.getRuletype() != null) {
			query = query + " " + (first ? WHERE : AND) + "RuleType"
					+ EQUALS_QUESTION;
			first = false;
		}
		if (bean.getRulename() != null) {
			query = query + " " + (first ? WHERE : AND) + "RuleName"
					+ EQUALS_QUESTION;
			first = false;
		}
		if (bean.getRuleid() != 0) {
			query = query + " " + (first ? WHERE : AND) + "RuleID"
					+ EQUALS_QUESTION;
			first = false;
		}
		return query;
	}

	public int update(final RequestRuleBean bean,
			final ResponseRuleBean original, Connection connection)
			throws Exception {
		int result = -1;
		
		clearRulesCache();

		if (bean != null) {
			if (connection == null || connection.isClosed()) {
				LOG.debug("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			int fieldcount = 0;
			String command = "update RecommendRule SET ";
			if (bean.getPriority() > 0
					&& (original == null || original.getPriority() != bean
							.getPriority())) {
				if (fieldcount > 0)
					command += ", ";
				command += "Priority = ?";
				fieldcount++;
			}
			if (bean.getValid() != null
					&& (original == null || original.getValid() != bean
							.getValid())) {
				if (fieldcount > 0)
					command += ", ";
				command += "Valid = ?";
				fieldcount++;
			}
			if (bean.getMessages() != null
					&& bean.getMessages().length > 0
					&& (original == null || original.getMessages() == null || (original
							.getMessages().length > 0 && original.getMessages()[0]
							.getMessageid() != bean.getMessages()[0]
							.getMessageid()))) {
				if (fieldcount > 0)
					command += ", ";
				command += "TrueMessageID = ?";
				fieldcount++;
			}
			if (bean.getMessages() != null
					&& bean.getMessages().length > 1
					&& (original == null || original.getMessages() == null || (original
							.getMessages().length > 1 && original.getMessages()[0]
							.getMessageid() != bean.getMessages()[0]
							.getMessageid()))) {
				if (fieldcount > 0)
					command += ", ";
				command += "FalseMessageID = ?";
				fieldcount++;
			}
			if (bean.getRuletype() != null
					&& (original == null || !original.getRuletype().equals(
							bean.getRuletype()))) {
				if (fieldcount > 0)
					command += ", ";
				command += "RuleType = ?";
				fieldcount++;
			}
			if (bean.getRulename() != null
					&& (original == null || !original.getRulename().equals(
							bean.getRulename()))) {
				if (fieldcount > 0)
					command += ", ";
				command += "RuleName = ?";
				fieldcount++;
			}

			command += " WHERE RuleID = ?";

			if (fieldcount > 0) {
				LOG.warn("Running query: " + command);
				final PreparedStatement updateStatement = connection
						.prepareStatement(command);
				int fieldnum = 1;

				if (bean.getPriority() > 0
						&& (original == null || original.getPriority() != bean
								.getPriority())) {
					updateStatement.setInt(fieldnum, bean.getPriority());
					;
					fieldnum++;
				}
				if (bean.getValid() != null
						&& (original == null || original.getValid() != bean
								.getValid())) {
					updateStatement.setBoolean(fieldnum, bean.getValid());
					;
					fieldnum++;
				}
				if (bean.getMessages() != null
						&& bean.getMessages().length > 0
						&& (original == null || original.getMessages() == null || (original
								.getMessages().length > 0 && original
								.getMessages()[0].getMessageid() != bean
								.getMessages()[0].getMessageid()))) {
					updateStatement.setInt(fieldnum,
							bean.getMessages()[0].getMessageid());
					fieldnum++;
				}
				if (bean.getMessages() != null
						&& bean.getMessages().length > 1
						&& (original == null || original.getMessages() == null || (original
								.getMessages().length > 1 && original
								.getMessages()[0].getMessageid() != bean
								.getMessages()[0].getMessageid()))) {
					updateStatement.setInt(fieldnum,
							bean.getMessages()[1].getMessageid());
					fieldnum++;
				}
				if (bean.getRuletype() != null
						&& (original == null || !original.getRuletype().equals(
								bean.getRuletype()))) {
					updateStatement.setString(fieldnum, bean.getRuletype());
					fieldnum++;
				}
				if (bean.getRulename() != null
						&& (original == null || !original.getRulename().equals(
								bean.getRulename()))) {
					updateStatement.setString(fieldnum, bean.getRulename());
					fieldnum++;
				}
				if (original != null)
					updateStatement.setInt(fieldnum, original.getRuleid());
				else
					throw new Exception(
							"Invalid or Missing RecommendRuleID in update");

				result = updateStatement.executeUpdate();

				updateStatement.close();
			}
			if (original != null && original.getRuleid() > 0) {
				saveDiagnoses(bean.getRuleid(), bean.getDiagnoses(),
						original.getDiagnoses(), connection);
				saveCriteria(bean.getRuleid(), bean.getCriteria(),
						original.getCriteria(), connection);
			}
		}
		return result;
	}

    public RecommendDiagnosisBean[] getAllDiagnoses(Connection connection) throws SQLException {
		final String command = "SELECT DiagnosisID, Diagnosis, Stage, Notes FROM RecommendDiagnosis ORDER BY DiagnosisID";

		ICache<String, List<RecommendDiagnosisBean>> diagnosisCache = EHCacheImpl.getDefaultInstance("diagnosisCache");

		List<RecommendDiagnosisBean> results = null;
		if (diagnosisCache != null) results = diagnosisCache.get("allDiagnoses");

		if (results == null || results.size() < 1) {
			results = new ArrayList<RecommendDiagnosisBean>();
			final PreparedStatement preparedStatementQuery = connection.prepareStatement(command);
	
			final ResultSet resultSet = preparedStatementQuery.executeQuery();
			try {
				while (resultSet.next()) {
					RecommendDiagnosisBean diag = new RecommendDiagnosisBean();
					diag.setDiagnosisid(resultSet.getInt("DiagnosisID"));
					diag.setDiagnosis(resultSet.getString("Diagnosis"));
					diag.setStage(resultSet.getString("Stage"));
					diag.setNotes(resultSet.getString("Notes"));
					diag.setTreatments(getTreatmentGuidelinesByDiagnosis(diag.getDiagnosisid(),connection));
					diag.setGuidelinechart(diag.getTreatments());
					results.add(diag);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				resultSet.close();
				preparedStatementQuery.close();
			}
			if (diagnosisCache != null) diagnosisCache.put("allDiagnoses", results);
		}
		if (results.size() > 0) {
			RecommendDiagnosisBean[] ar = new RecommendDiagnosisBean[results.size()];
			return results.toArray(ar);
		}
		return null;
	}	
	
    private RecommendDiagnosisBean[] getDiagnoses(int ruleid, Connection connection) throws Exception {
		final String command = "select D.DiagnosisID, D.Diagnosis, D.Stage, D.Notes from RecommendRule_RecommendDiagnosis RD, RecommendDiagnosis D WHERE RD.RuleID = ? AND RD.DiagnosisID = D.DiagnosisID";

		ICache<String, List<RecommendDiagnosisBean>> diagnosisCache = EHCacheImpl.getDefaultInstance("diagnosisCache");
		
		List<RecommendDiagnosisBean> results = null;
		if (diagnosisCache != null) results = diagnosisCache.get("rule-"+ruleid);
	
		if (results == null || results.size() < 1) {
			results = new ArrayList<RecommendDiagnosisBean>();
			final PreparedStatement preparedStatementQuery = connection
					.prepareStatement(command);
			preparedStatementQuery.setInt(1, ruleid);
	
			final ResultSet resultSet = preparedStatementQuery.executeQuery();
			try {
				while (resultSet.next()) {
					RecommendDiagnosisBean diag = new RecommendDiagnosisBean();
					diag.setDiagnosisid(resultSet.getInt("DiagnosisID"));
					diag.setDiagnosis(resultSet.getString("Diagnosis"));
					diag.setStage(resultSet.getString("Stage"));
					diag.setNotes(resultSet.getString("Notes"));
					diag.setTreatments(getTreatmentGuidelinesByDiagnosis(diag.getDiagnosisid(),connection));
					diag.setGuidelinechart(diag.getTreatments());
					results.add(diag);
				}
			} catch (Exception e) {
				throw new Exception(e);
			} finally {
				resultSet.close();
				preparedStatementQuery.close();
			}
			if (diagnosisCache != null) diagnosisCache.put("rule-"+ruleid, results);
		}
		if (results.size() > 0) {
			RecommendDiagnosisBean[] ar = new RecommendDiagnosisBean[results.size()];
			ar=results.toArray(ar);
			return ar;
		}
		return null;
	}

	public RecommendTreatmentGuidelineBean[] getTreatmentGuidelinesByDiagnosis(
			int diagnosisid, Connection connection) throws Exception {
		final String command = "select td.ID, td.TreatmentID, td.DiagnosisID, td.Row, td.Drug, t.TreatmentName, t.TreatmentGroupID, tg.TreatmentGroupAbbr from Treatment_Diagnosis td, Treatment t, TreatmentGroup tg WHERE DiagnosisID = ? AND td.TreatmentID=t.ID AND t.TreatmentGroupID=tg.ID ORDER BY td.Row ASC, td.Drug ASC, tg.TreatmentGroupAbbr ASC";

		ArrayList<RecommendTreatmentGuidelineBean> results = new ArrayList<RecommendTreatmentGuidelineBean>();
		final PreparedStatement preparedStatementQuery = connection.prepareStatement(command);
		preparedStatementQuery.setInt(1, diagnosisid);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try {
			while (resultSet.next()) {
				RecommendTreatmentGuidelineBean treat = new RecommendTreatmentGuidelineBean();
				treat.setTreatmentguidelineid(resultSet.getInt("ID"));
				treat.setDiagnosisid(resultSet.getInt("DiagnosisID"));
				treat.setTreatmentid(resultSet.getInt("TreatmentID"));
				treat.setRow(resultSet.getInt("Row"));
				treat.setDrug(resultSet.getInt("Drug"));
				treat.setTreatment(new TreatmentDaoImp().getTreatmentByID(treat.getTreatmentid(), connection));
				results.add(treat);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			resultSet.close();
			preparedStatementQuery.close();
		}
		if (results.size() > 0) {
			RecommendTreatmentGuidelineBean[] ar = new RecommendTreatmentGuidelineBean[results.size()];
			return results.toArray(ar);
		}
		return null;
	}

	public void saveTreatmentGuidelinesByDiagnosis(
			RecommendTreatmentGuidelineBean[] guidelines,
			RecommendTreatmentGuidelineBean[] oldGuidelines,
			Connection connection) throws Exception {
		final String command = "insert into Treatment_Diagnosis (TreatmentID, DiagnosisID, Row, Drug) values(?, ?, ?, ?)";
		final String updcommand = "update Treatment_Diagnosis set TreatmentID = ?, DiagnosisID = ?, Row = ?, Drug = ? WHERE ID = ?";
		final String delcommand = "delete from Treatment_Diagnosis WHERE ID = ? and DiagnosisID = ?";

		clearRulesCache();

		// Insert into Treatment_Diagnosis
		if (guidelines != null) {
			for (int i = 0; i < guidelines.length; i++) {
				RecommendTreatmentGuidelineBean guide = guidelines[i];
				if (guide != null) {
					int match = 0;
					if (oldGuidelines != null
							&& guide.getTreatmentguidelineid() > 0) {
						for (int j = 0; j < oldGuidelines.length; j++) {
							if (guide.getTreatmentguidelineid() == oldGuidelines[j]
									.getTreatmentguidelineid())
								match = j;
						}
					}

					if (match == 0) {
						PreparedStatement insertStatement = connection
								.prepareStatement(command);
						insertStatement.clearParameters();
						insertStatement.setInt(1, guide.getTreatmentid());
						insertStatement.setInt(2, guide.getDiagnosisid());
						insertStatement.setInt(3, guide.getRow());
						insertStatement.setInt(4, guide.getDrug());
						insertStatement.executeUpdate();
						insertStatement.close();
					} else {
						PreparedStatement insertStatement = connection
								.prepareStatement(updcommand);
						insertStatement.clearParameters();
						insertStatement.setInt(1, guide.getTreatmentid());
						insertStatement.setInt(2, guide.getDiagnosisid());
						insertStatement.setInt(3, guide.getRow());
						insertStatement.setInt(4, guide.getDrug());
						insertStatement.setInt(5,
								guide.getTreatmentguidelineid());
						insertStatement.executeUpdate();
						insertStatement.close();
					}
				}
			}
		}

		// Delete from Treatment_Diagnosis
		if (oldGuidelines != null) {
			for (int i = 0; i < oldGuidelines.length; i++) {
				RecommendTreatmentGuidelineBean guide = oldGuidelines[i];
				if (guide != null) {
					int match = 0;
					if (guidelines != null
							&& guide.getTreatmentguidelineid() > 0) {
						for (int j = 0; j < guidelines.length; j++) {
							if (guide.getTreatmentguidelineid() == guidelines[j]
									.getTreatmentguidelineid())
								match = j;
						}
					}

					if (guidelines != null && match == 0) {
						PreparedStatement delStatement = connection
								.prepareStatement(delcommand);
						delStatement.clearParameters();
						delStatement.setInt(1, guide.getTreatmentguidelineid());
						delStatement.setLong(2, guide.getDiagnosisid());
						delStatement.executeUpdate();
						delStatement.close();
					}
				}
			}
		}
	}

	public RecommendTreatmentGuidelineBean[] getTreatmentGuidelinesByTreatment(
			int treatmentid, Connection connection) throws Exception {
		final String command = "select ID, TreatmentID, DiagnosisID, Row, Drug from Treatment_Diagnosis WHERE TreatmentID = ? ORDER BY DiagnosisID, Row ASC, Drug ASC";

		ArrayList<RecommendTreatmentGuidelineBean> results = new ArrayList<RecommendTreatmentGuidelineBean>();
		final PreparedStatement preparedStatementQuery = connection.prepareStatement(command);
		preparedStatementQuery.setInt(1, treatmentid);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try {
			while (resultSet.next()) {
				RecommendTreatmentGuidelineBean treat = new RecommendTreatmentGuidelineBean();
				treat.setTreatmentguidelineid(resultSet.getInt("ID"));
				treat.setDiagnosisid(resultSet.getInt("DiagnosisID"));
				treat.setTreatmentid(resultSet.getInt("TreatmentID"));
				treat.setRow(resultSet.getInt("Row"));
				treat.setDrug(resultSet.getInt("Drug"));
				treat.setTreatment(new TreatmentDaoImp().getTreatmentByID(treat.getTreatmentid(), connection));
				results.add(treat);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			resultSet.close();
			preparedStatementQuery.close();
		}
		if (results.size() > 0) {
			RecommendTreatmentGuidelineBean[] ar = new RecommendTreatmentGuidelineBean[results.size()];
			return results.toArray(ar);
		}
		return null;
	}

	public void saveTreatmentGuidelinesByTreatment(
			RecommendTreatmentGuidelineBean[] guidelines, RecommendTreatmentGuidelineBean[] oldGuidelines, Connection connection) throws Exception {

		saveTreatmentGuidelinesByDiagnosis(guidelines, oldGuidelines, connection);
	}

	private void saveDiagnoses(int ruleID,
			RecommendDiagnosisBean[] recommendDiagnosisBeans,
			RecommendDiagnosisBean[] oldRecommendDiagnosisBeans,
			Connection connection) throws Exception {
		final String command2 = "insert into RecommendRule_RecommendDiagnosis (RuleID, DiagnosisID) values(?, ?)";
		final String delcommand = "delete from RecommendRule_RecommendDiagnosis WHERE RuleID = ? and DiagnosisID = ?";

		clearDiagnosisCache();
		
		// Insert into RecommendRule_RecommendDiagnosis
		if (recommendDiagnosisBeans != null) {
			for (int i = 0; i < recommendDiagnosisBeans.length; i++) {
				RecommendDiagnosisBean diag = recommendDiagnosisBeans[i];
				if (diag != null) {
					boolean save = false;
					if (oldRecommendDiagnosisBeans == null) {
						save = true;
					} else {
						boolean match = false;
						for (int j = 0; j < oldRecommendDiagnosisBeans.length; j++) {
							if (diag.getDiagnosisid() == oldRecommendDiagnosisBeans[j].getDiagnosisid())
								match = true;
						}
						if (!match)
							save = true;
					}
					if (save) {
						PreparedStatement insertStatement = connection
								.prepareStatement(command2);
						insertStatement.clearParameters();
						insertStatement.setInt(1, ruleID);
						insertStatement.setInt(2, diag.getDiagnosisid());
						insertStatement.executeUpdate();
						insertStatement.close();
					}
				}
			}
		}
		// Delete from RecommendRule_RecommendDiagnosis
		if (oldRecommendDiagnosisBeans != null) {
			for (int i = 0; i < oldRecommendDiagnosisBeans.length; i++) {
				RecommendDiagnosisBean diag = oldRecommendDiagnosisBeans[i];
				if (diag != null) {
					boolean match = false;
					if (recommendDiagnosisBeans != null) {
						for (int j = 0; j < recommendDiagnosisBeans.length; j++) {
							if (diag.getDiagnosisid() == recommendDiagnosisBeans[j].getDiagnosisid())
								match = true;
						}
					}
					if (!match) {
						PreparedStatement delStatement = connection
								.prepareStatement(delcommand);
						delStatement.clearParameters();
						delStatement.setInt(1, ruleID);
						delStatement.setLong(2, diag.getDiagnosisid());
						delStatement.executeUpdate();
						delStatement.close();
					}
				}
			}
		}

	}

	private RecommendRuleCriteriaBean[] getCriteria(int ruleid,
			Connection connection) throws SQLException {
		final String command = "select * from RecommendRuleCriterium WHERE RuleID = ? ORDER BY Priority DESC";

		ArrayList<RecommendRuleCriteriaBean> results = new ArrayList<RecommendRuleCriteriaBean>();
		final PreparedStatement preparedStatementQuery = connection
				.prepareStatement(command);
		preparedStatementQuery.setInt(1, ruleid);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try {
			while (resultSet.next()) {
				RecommendRuleCriteriaBean crit = new RecommendRuleCriteriaBean();
				crit.setCriteriaid(resultSet.getInt("RuleCriteriumID"));
				crit.setRuleid(ruleid);
				crit.setPriority(resultSet.getInt("Priority"));
				crit.setType(resultSet.getString("Type"));
				crit.setElement(resultSet.getString("FieldName"));
				crit.setOperator(resultSet.getString("Operator"));
				crit.setValue(resultSet.getString("Value"));
				results.add(crit);
			}
		} finally {
			resultSet.close();
			preparedStatementQuery.close();
		}
		if (results.size() > 0) {
			RecommendRuleCriteriaBean[] ar = new RecommendRuleCriteriaBean[results
					.size()];
			return results.toArray(ar);
		}
		return null;
	}

	private void saveCriteria(int ruleID, RecommendRuleCriteriaBean[] criteria,
			RecommendRuleCriteriaBean[] oldCriteria, Connection connection)
			throws Exception {
		final String command = "insert into RecommendRuleCriterium (RuleID, Priority, Type, FieldName, Operator, Value) values(?,?,?,?,?,?)";
		final String updcommand = "update RecommendRuleCriterium set Priority = ?, Type = ?, FieldName = ?, Operator = ?, Value = ? WHERE RuleID = ? and RuleCriteriumID = ?";
		final String delcommand = "delete from RecommendRuleCriterium WHERE RuleID = ? and RuleCriteriumID = ?";

		clearRulesCache();
		
		// Insert into FacilitySetting
		if (criteria != null) {
			for (int i = 0; i < criteria.length; i++) {
				RecommendRuleCriteriaBean crit = criteria[i];
				int match = 0;
				if (oldCriteria != null && crit.getCriteriaid() > 0) {
					for (int j = 0; j < oldCriteria.length; j++) {
						if (crit.getCriteriaid() == oldCriteria[j]
								.getCriteriaid())
							match = j;
					}
				}

				if (match == 0) {
					PreparedStatement insertStatement = connection
							.prepareStatement(command);
					insertStatement.clearParameters();
					insertStatement.setInt(1, ruleID);
					insertStatement.setInt(2, crit.getPriority());
					insertStatement.setString(3, crit.getType());
					insertStatement.setString(4, crit.getElement());
					insertStatement.setString(5, crit.getOperator());
					insertStatement.setString(6, crit.getValue());
					insertStatement.executeUpdate();
					insertStatement.close();
				} else {
					PreparedStatement updateStatement = connection
							.prepareStatement(updcommand);
					updateStatement.clearParameters();
					updateStatement.setInt(1, crit.getPriority());
					updateStatement.setString(2, crit.getType());
					updateStatement.setString(3, crit.getElement());
					updateStatement.setString(4, crit.getOperator());
					updateStatement.setString(5, crit.getValue());
					updateStatement.setInt(6, ruleID);
					updateStatement.setInt(7, crit.getCriteriaid());
					updateStatement.executeUpdate();
					updateStatement.close();
				}
			}
		}
		if (oldCriteria != null) {
			for (int i = 0; i < oldCriteria.length; i++) {
				RecommendRuleCriteriaBean crit = oldCriteria[i];
				int match = 0;
				if (criteria != null && crit.getCriteriaid() > 0) {
					for (int j = 0; j < criteria.length; j++) {
						if (crit.getCriteriaid() == criteria[j].getCriteriaid())
							match = j;
					}
				}
				if (criteria != null && match == 0) {
					PreparedStatement deleteStatement = connection
							.prepareStatement(delcommand);
					deleteStatement.clearParameters();
					deleteStatement.setInt(1, ruleID);
					deleteStatement.setInt(2, crit.getCriteriaid());
					deleteStatement.executeUpdate();
					deleteStatement.close();
				}
			}
		}
	}

	public RecommendMessageBean[] getAllMessages(Connection connection)
			throws SQLException {
		final String command = "SELECT MessageID, Priority, Message FROM RecommendMessage ORDER BY Priority, MessageID;";

		ArrayList<RecommendMessageBean> results = new ArrayList<RecommendMessageBean>();
		final PreparedStatement preparedStatementQuery = connection
				.prepareStatement(command);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try {
			while (resultSet.next()) {
				RecommendMessageBean mess = new RecommendMessageBean();
				mess.setMessageid(resultSet.getInt("MessageID"));
				mess.setPriority(resultSet.getInt("Priority"));
				mess.setMessage(resultSet.getString("Message"));
				results.add(mess);
			}
		} finally {
			resultSet.close();
			preparedStatementQuery.close();
		}
		if (results.size() > 0) {
			RecommendMessageBean[] ar = new RecommendMessageBean[results.size()];
			return results.toArray(ar);
		}
		return null;
	}

	private RecommendMessageBean[] getMessages(int appuserID,
			Connection connection, int trueMessageID, int falseMessageID)
			throws SQLException {

		ArrayList<RecommendMessageBean> results = new ArrayList<RecommendMessageBean>();

		if (trueMessageID > 0) {
			RecommendMessageBean mess = getMessage(trueMessageID, connection);
			if (mess != null)
				results.add(mess);
		}
		if (results.size() > 0 && falseMessageID > 0) {
			RecommendMessageBean mess = getMessage(falseMessageID, connection);
			if (mess != null)
				results.add(mess);
		}

		if (results.size() > 0) {
			RecommendMessageBean[] ar = new RecommendMessageBean[results.size()];
			return results.toArray(ar);
		}
		return null;
	}

	private RecommendMessageBean getMessage(int messageid, Connection connection)
	throws SQLException {
		final String command = "select * from RecommendMessage WHERE MessageID = ?";
		
		RecommendMessageBean mess = null;
		
		if (messageid > 0) {
			final PreparedStatement preparedStatementQuery = connection
					.prepareStatement(command);
			preparedStatementQuery.setInt(1, messageid);
		
			final ResultSet resultSet = preparedStatementQuery.executeQuery();
			try {
				if (resultSet.next()) {
					mess = new RecommendMessageBean();
					mess.setMessageid(resultSet.getInt("MessageID"));
					mess.setMessagetag(resultSet.getString("MessageTag"));
					mess.setPriority(resultSet.getInt("Priority"));
					mess.setMessage(resultSet.getString("Message"));
				}
			} finally {
				resultSet.close();
				preparedStatementQuery.close();
			}
		}
		return mess;
	}

	public void updateMessage(RecommendMessageBean message,
			RecommendMessageBean oldMessage, Connection connection)
			throws Exception {
		
		clearRulesCache();
		
		final String command = "update RecommendMessage set Priority = ?, Message = ?, MessageTag = ? where MessageID = ?";

		if (message != null && oldMessage != null) {
			if (message.getPriority() == oldMessage.getPriority()
					&& message.getMessage().equals(oldMessage.getMessage())) {
				return;
			}
		}
		if (message.getMessageid() > 0) {
			PreparedStatement updateStatement = connection.prepareStatement(command);
			updateStatement.clearParameters();
			updateStatement.setInt(1, message.getPriority());
			updateStatement.setString(2, message.getMessage());
			updateStatement.setInt(3, message.getMessageid());
			updateStatement.setString(4, message.getMessagetag());
			updateStatement.executeUpdate();
			updateStatement.close();
		}

		return;
	}

	public RecommendMessageBean createMessage(RecommendMessageBean message,
			Connection connection) throws Exception {
		final String command = "insert into RecommendMessage (Priority, Message) values(?,?)";

		// Insert into RecommendMessage
		if (message != null) {
			PreparedStatement insertStatement = connection.prepareStatement(command);
			insertStatement.clearParameters();
			insertStatement.setInt(1, message.getPriority());
			insertStatement.setString(2, message.getMessage());
			insertStatement.executeUpdate();
			ResultSet rs = insertStatement.getGeneratedKeys();
			if (rs.next()) {
				message.setMessageid(rs.getInt(1));
			}
			insertStatement.close();
		}
		return message;
	}

	protected void clearRulesCache() {
		ICache<String, List<ResponseRuleBean>> rulesCache = EHCacheImpl.getDefaultInstance("rulesCache");
		// remove rules from cache if exist
		if (rulesCache != null) rulesCache.deleteAll();
	}

	protected void clearDiagnosisCache() {
		ICache<String, List<RecommendDiagnosisBean>> diagnosisCache = EHCacheImpl.getDefaultInstance("diagnosisCache");
		// remove rules from cache if exist
		if (diagnosisCache != null) diagnosisCache.deleteAll();
	}

}
