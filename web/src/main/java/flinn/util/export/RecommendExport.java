package flinn.util.export;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import flinn.dao.dbconnection.DBConnectionPool;
import flinn.dao.dbconnection.Database;

public class RecommendExport {

	protected static final Logger LOG = Logger.getLogger(RecommendExport.class);

	public RecommendExport() {

	}

	public String generateExport() {
		String exportData = null;
		Connection connection = null;

		try {
			connection = DBConnectionPool.getConnection();

			if (clearTables(connection)) {
				if (populateTables(connection)) {
					exportData = dumpTables(connection);
				}
			}

		} catch (SQLException ex) {
			LOG.error("failed to generate export.  " + ex.toString());
		}
		finally {
			// close the Connection
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException ex) {
				connection = null;
			}
		}
		return exportData;
	}

	private Boolean clearTables(Connection connection) {
		Boolean success = true;
		try {
			executeQuery(connection, "TRUNCATE RecommendFacility");
			executeQuery(connection, "TRUNCATE RecommendPatient");
			executeQuery(connection, "TRUNCATE RecommendPatientDetail");
			executeQuery(connection, "TRUNCATE RecommendPatientStatus");
			executeQuery(connection, "TRUNCATE RecommendPrescriptionEvent");
//			executeQuery(connection, "TRUNCATE RecommendReason");
		} catch (SQLException ex) {
			LOG.error("failed to clear tables.  " + ex.toString());
			success = false;
		}

		return success;
	}

	private Boolean populateTables(Connection connection) {
		Boolean success = false;

		if (transferFacility(connection)) {
			if (transferPatient(connection)) {
				if (transferPatientDetail(connection)) {
					if (transferPatientStatus(connection)) {
						if (transferPrescriptionEvent(connection)) {
							success = true;
						}
					}
				}

			}
		}

		return success;
	}

	private Boolean transferFacility(Connection connection) {
		Boolean success = true;

		StringBuilder query = new StringBuilder(512);
		query.append("INSERT INTO `RecommendFacility` (`ID`, `RecommendFacilityIdentifier`, `RecommendFacilityName`, `StartDate`) ");
		query.append("SELECT `ID`, `ID`, `FacilityName`, `Launch` FROM `Facility`;");

		try {
			executeQuery(connection, query.toString());
		} catch (SQLException ex) {
			LOG.error("failed to transfer facility.  " + ex.toString());
		}

		return success;

	}

	private Boolean transferPatient(Connection connection) {
		Boolean success = true;

		StringBuilder query = new StringBuilder(512);
		query.append("INSERT INTO `RecommendPatient` (`ID`, `RecommendFacilityID`, `RecommendPatientIdentifier`, `Valid`, `StartDate`) ");
		query.append("SELECT `ID`, `FacilityID`, `ID`, `Valid`, `StartDate` FROM `Patient`;");

		try {
			executeQuery(connection, query.toString());
		} catch (SQLException ex) {
			LOG.error("failed to transfer patient.  " + ex.toString());
		}

		return success;

	}

	private Boolean transferPatientDetail(Connection connection) {
		Boolean success = true;

		StringBuilder query = new StringBuilder(512);
		query.append("INSERT INTO `RecommendPatientDetail` (`ID`, `RecommendPatientID`, `RecommendPatientDetailName`, `RecommendPatientDetailValue`, `EntryDate`) ");
		query.append("SELECT `ID`, `PatientID`, `PatientDetailName`, `PatientDetailValue`, `EntryDate` FROM `PatientDetail` ");
		query.append("WHERE PatientDetailName IN ('sex','birth', 'zip', 'race', 'ethnicity', 'marital', 'employment', 'living');");

		try {
			executeQuery(connection, query.toString());
		} catch (SQLException ex) {
			LOG.error("failed to transfer patient detail.  " + ex.toString());
		}

		return success;

	}

	private Boolean transferPatientStatus(Connection connection) {
		Boolean success = true;

		StringBuilder query = new StringBuilder(512);
		query.append("INSERT INTO `RecommendPatientStatus` (`ID`, `RecommendPatientID`, `RecommendPatientStatusName`, `RecommendPatientStatusValue`, `EntryDate`) ");
		query.append("SELECT `ID`, `PatientID`, `PatientStatusName`, `PatientStatusValue`, `EntryDate` FROM `PatientStatus`;");

		try {
			executeQuery(connection, query.toString());
		} catch (SQLException ex) {
			LOG.error("failed to transfer patient status.  " + ex.toString());
		}

		return success;

	}

	private Boolean transferPrescriptionEvent(Connection connection) {
		Boolean success = true;

		StringBuilder query = new StringBuilder(512);
		query.append("INSERT INTO `RecommendPrescriptionEvent` (`ID`, `RecommendPatientID`, `RecommendTreatmentID`, `EntryDate`, `Duration`, `Discontinue`, `DailyDose`, `DoctorName`)");
		query.append("SELECT `ID`, `PatientID`, `TreatmentID`, `EntryDate`, `Duration`, `Discontinue`, `DailyDose`, `DoctorName` FROM `PrescriptionEvent`;");

		try {
			executeQuery(connection, query.toString());
		} catch (SQLException ex) {
			LOG.error("failed to transfer perscription event.  "
					+ ex.toString());
		}

		return success;

	}

	private String dumpTables(Connection connection) {
		StringBuilder sb = new StringBuilder(20480);
		 
		try {
			sb.append(RecommendDump.dumpTable(connection, "RecommendFacility"));
			sb.append(RecommendDump.dumpTable(connection, "RecommendPatient"));
			sb.append(RecommendDump.dumpTable(connection, "RecommendPatientDetail"));
			sb.append(RecommendDump.dumpTable(connection, "RecommendPatientStatus"));
			sb.append(RecommendDump.dumpTable(connection, "RecommendPrescriptionEvent"));
			sb.append(RecommendDump.dumpTable(connection, "RecommendReason"));
			sb.append(RecommendDump.dumpTable(connection, "Treatment"));
			sb.append(RecommendDump.dumpTable(connection, "TreatmentDetail"));
			sb.append(RecommendDump.dumpTable(connection, "TreatmentGroup"));

		} catch (Exception ex) {
			LOG.error("dump tables failed : " + ex);
			sb.setLength(0);
		}

		return sb.toString();
	}

	private int executeQuery(Connection connection, String query)
			throws SQLException {
		Object[] params = new Object[] {};
		int status = Database.updateRows(connection, query, params);
		LOG.debug("query = " + query);
		LOG.debug("status = " + status);
		return status;
	}

}
