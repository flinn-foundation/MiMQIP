package flinn.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import flinn.dao.dbconnection.Database;
import flinn.dao.model.Medication;

public class MedicationDao
{

	protected static final Logger LOG = Logger.getLogger(MedicationDao.class);

	private static final int BY_MEDICATION_ID = 1;
	private static final int BY_RCOPIA_ID = 2;
	private static final int BY_RCOPIA_ID_AND_DATE = 3;

	static
	{
		LOG.debug("Log appender instantiated for " + MedicationDao.class);
	}

	public MedicationDao()
	{
		super();
	}

	public Medication findBy(Connection connection, Long medicationId) throws SQLException
	{
		String query = buildSelectQuery(BY_MEDICATION_ID);
		Map<String, Object> row = Database.getRow(connection, query, new Long[] { medicationId });

		LOG.debug("row=" + row);
		Medication medication = null;
		if (row != null)
		{
			medication = new Medication(row);
		}
		LOG.debug(ToStringBuilder.reflectionToString(medication));
		return medication;
	}

	public Medication findByRcopiaIdAndDate(Connection connection, Long rcopiaId, Date lastModifiedDate) throws SQLException
	{
		String query = buildSelectQuery(BY_RCOPIA_ID_AND_DATE);
		Map<String, Object> row = Database.getRow(connection, query, new Object[] { rcopiaId, lastModifiedDate });
		Medication medication = null;
		if (row != null)
		{
			medication = new Medication(row);
		}
		LOG.debug(ToStringBuilder.reflectionToString(medication));
		return medication;
	}

	public List<Medication> findByRcopiaId(Connection connection, Long rcopiaId) throws SQLException
	{
		List<Medication> medications = new ArrayList<Medication>();
		String query = buildSelectQuery(BY_RCOPIA_ID);
		List<Map<String, Object>> rows = Database.getRows(connection, query, new Object[] { rcopiaId });

		LOG.debug("number of rows=" + rows.size());
		Medication medication = null;
		for (Iterator<Map<String, Object>> iter = rows.iterator(); iter.hasNext();)
		{
			Map<String, Object> row = iter.next();
			medication = new Medication(row);
			medications.add(medication);
		}
		return medications;
	}

	public int merge(Connection connection, Medication medication) throws SQLException
	{
		LOG.debug("Merging Medication: " + " (" + medication.getRcopiaId() + ") " + medication.shortDescription() + " (" + medication.getLastModifiedDate() + ") " + medication.sigDescription());
		int status = 0;
		Medication medicationInDb = null;
		if (medication.getMedicationId() != null)
		{
			//medicationInDb = findBy(connection, medication.getMedicationId());
			status = update(connection, medication);
			if (status == 0) LOG.error("Update status of 0 for medication with Id " + medication.getMedicationId() + ". Medication does not exist in the database!");
		}
		else if (medication.getRcopiaId() != null && medication.getLastModifiedDate() != null)
		{
			medicationInDb = findByRcopiaIdAndDate(connection, medication.getRcopiaId(), medication.getLastModifiedDate());
			if (medicationInDb == null)
			{
				replace(connection, medication.getRcopiaId());
				status = insert(connection, medication);
				if (status == 0) LOG.error("Insert status of 0 for medication: " + ToStringBuilder.reflectionToString(medication));
			}
			else
			{
				LOG.debug("Skipping update of medication with rcopiaId " + medication.getRcopiaId() + " and lastModifiedDate " + medication.getLastModifiedDate());
			}
		}
		else
		{
			throw new IllegalArgumentException("Cannot call merge on medication without a valid medicationId or rcopiaId/lastModifiedDate");
		}

		return status;
	}

	/*
	 * All updated to the medication table should come through the merge method
	 */
	protected int insert(Connection connection, Medication medication) throws SQLException
	{
		String query = buildInsertQuery();
		List<Object> paramList = medication.getValues();
		paramList.remove(0);
		Object [] params = paramList.toArray();
		Long generatedKey = Database.insertRow(connection, query, params);
		medication.setMedicationId(generatedKey);
		LOG.debug("generatedKey = " + generatedKey);
		return (generatedKey != null) ? 1 : 0;
	}

	/*
	 * All updated to the medication table should come through the merge method
	 */
	protected int update(Connection connection, Medication medication) throws SQLException
	{
		String query = buildUpdateQuery();
		List<Object> paramsList = medication.getValues();
		paramsList.remove(0);
		paramsList.add(medication.getMedicationId());
		Object [] params = paramsList.toArray();
		int status = Database.updateRows(connection, query, params);
		LOG.debug("status = " + status);
		return status;
	}

	/*
	 * All updated to the medication table should come through the merge method
	 */
	protected int delete(Connection connection, Long medicationId) throws SQLException
	{
		String query = buildDeleteQuery();
		Object [] params = new Object[] { medicationId };
		int status = Database.updateRows(connection, query, params);
		LOG.debug("status = " + status);
		return status;
	}

	private void replace(Connection connection, Long rcopiaId) throws SQLException
	{
		String query = buildReplaceMedicationsQuery();
		Object [] params = new Object[] { rcopiaId };
		int status = Database.updateRows(connection, query, params);
		LOG.debug("status = " + status);
	}

	private String buildSelectQuery(int whereClause)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		for (Iterator<String> iter = Medication.getColumns().iterator(); iter.hasNext();)
		{
			String columnName = iter.next();
			sb.append(columnName + ", ");
		}
		sb.setLength(sb.length() - 2);
		sb.append(" FROM Medications");
		switch (whereClause)
		{
			case BY_MEDICATION_ID:
				sb.append(" WHERE medicationId = ?");
			case BY_RCOPIA_ID:
				sb.append(" WHERE rcopiaId = ?");
			case BY_RCOPIA_ID_AND_DATE:
				sb.append(" WHERE rcopiaId = ? AND lastModifiedDate = ?");
		}
		LOG.debug("Select sql: " + sb.toString());
		return sb.toString();
	}

	private String buildInsertQuery()
	{
		List<String> columns = Medication.getColumns();
		columns.remove(0); // Remove medicationId from the list
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO Medications ( ");
		for (Iterator<String> iter = columns.iterator(); iter.hasNext();)
		{
			String columnName = iter.next();
			sb.append(columnName + ", ");
		}
		sb.setLength(sb.length() - 2);
		sb.append(") VALUES (");
		for (int i = 0; i < columns.size(); i++)
		{
			sb.append("?, ");
		}
		sb.setLength(sb.length() - 2);
		sb.append(")");
		LOG.debug("Insert sql: " + sb.toString());
		return sb.toString();
	}

	private String buildUpdateQuery()
	{
		List<String> columns = Medication.getColumns();
		columns.remove(0); // Remove medicationId from the list
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE Medications SET ");
		for (Iterator<String> iter = columns.iterator(); iter.hasNext();)
		{
			String columnName = iter.next();
			sb.append(columnName + " = ?, ");
		}
		sb.setLength(sb.length() - 2);
		sb.append(" WHERE medicationId = ?");
		LOG.debug("Update sql: " + sb.toString());
		return sb.toString();
	}

	private String buildReplaceMedicationsQuery()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE Medications SET primaryFlag = NULL WHERE rcopiaId = ? AND primaryFlag = 'Y'");
		LOG.debug("Replace sql: " + sb.toString());
		return sb.toString();
	}

	private String buildDeleteQuery()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM Medications WHERE medicationId = ?");
		LOG.debug("Delete sql: " + sb.toString());
		return sb.toString();
	}

}
