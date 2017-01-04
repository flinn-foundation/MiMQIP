package flinn.util.export;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class RecommendDump {

	protected static final Logger LOG = Logger.getLogger(RecommendDump.class);

	public static String dumpTable(Connection connection, String table) {
		StringBuilder out = new StringBuilder();
		try {
			out.append(dumpTableCreate(connection, connection.getCatalog(),
					table));
			out.append(dumpTableContent(connection, table));
		} catch (SQLException ex) {
			LOG.error("error getting schema " + ex.getMessage());
		}

		return out.toString();
	}

	/**
	 * Create script with insert statements: NOTE THIS IS INCOMPLETE AND ANY
	 * CONTRIBUTIONS WOULD BE WELCOME
	 * 
	 * @param out
	 *            BufferedWriter
	 * @param table
	 *            Table Name
	 */
	private static String dumpTableContent(Connection connection, String table) {
		StringBuilder out = new StringBuilder();
		try {
			Statement s = connection
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			s.executeQuery("SELECT /*!40001 SQL_NO_CACHE */ * FROM `" + table
					+ "`");
			ResultSet rs = s.getResultSet();
			ResultSetMetaData rsMetaData = rs.getMetaData();
			if (rs.last()) {
				out.append("--\n-- Dumping data for table `" + table
						+ "`\n--\n\n");
				rs.beforeFirst();
			}
			int columnCount = rsMetaData.getColumnCount();
			StringBuilder prefix = new StringBuilder();
			prefix.setLength(0);
			prefix.append("INSERT INTO `" + table + "` (");
			for (int i = 1; i <= columnCount; i++) {
				if (i == columnCount) {
					prefix.append(rsMetaData.getColumnName(i) + ") VALUES(");
				} else {
					prefix.append(rsMetaData.getColumnName(i) + ",");
				}
			}
			StringBuilder postfix = new StringBuilder();
			int count = 0;
			while (rs.next()) {
				postfix.setLength(0);
				for (int i = 1; i <= columnCount; i++) {
					if (i == columnCount) {
						// System.err.println(rs.getMetaData().getColumnClassName(i));
						postfix.append("'" + rs.getString(i) + "');\n");

					} else {
						// System.err.println(rs.getMetaData().getColumnTypeName(i));
						if (rs.getMetaData().getColumnTypeName(i)
								.equalsIgnoreCase("LONGBLOB")) {
							try {
								postfix.append("'"
										+ escapeString(rs.getBytes(i))
												.toString() + "',");
							} catch (Exception e) {
								postfix.append("NULL,");
							}
						} else {
							try {
								postfix.append("'"
										+ escapeString(rs.getBytes(i))
												.toString() + "',");
							} catch (Exception e) {
								postfix.append("NULL,");
							}
						}
					}
				}
				out.append(prefix);
				out.append(postfix);
				++count;
			}
			rs.close();
			s.close();
			out.append("\n");
		} catch (SQLException ex) {
			LOG.error("error dumping table " + ex.getMessage());
		}
		return out.toString();
	}

	private static String dumpTableCreate(Connection connection, String schema,
			String table) {
		StringBuilder createTable = new StringBuilder();
		createTable.append("--\n-- Table structure for table `" + table
				+ "`\n--\n\n");
		try {
			Statement s = connection
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			s.executeQuery("SHOW CREATE TABLE `" + schema + "`.`" + table + "`");
			ResultSet rs = s.getResultSet();
			while (rs.next()) {
				createTable.append(rs.getString("Create Table") + ";");
			}
			createTable.append("\n\n");
		} catch (SQLException e) {
			LOG.error("error dumping table create " + e.getMessage());
			createTable.setLength(0);
		}
		return createTable.toString();
	}

	/**
	 * Escape string ready for insert via mysql client
	 * 
	 * @param bIn
	 *            String to be escaped passed in as byte array
	 * @return bOut MySQL compatible insert ready ByteArrayOutputStream
	 */
	private static ByteArrayOutputStream escapeString(byte[] bIn) {
		int numBytes = bIn.length;
		ByteArrayOutputStream bOut = new ByteArrayOutputStream(numBytes + 2);
		for (int i = 0; i < numBytes; ++i) {
			byte b = bIn[i];

			switch (b) {
			case 0: /* Must be escaped for 'mysql' */
				bOut.write('\\');
				bOut.write('0');
				break;

			case '\n': /* Must be escaped for logs */
				bOut.write('\\');
				bOut.write('n');
				break;

			case '\r':
				bOut.write('\\');
				bOut.write('r');
				break;

			case '\\':
				bOut.write('\\');
				bOut.write('\\');

				break;

			case '\'':
				bOut.write('\\');
				bOut.write('\'');

				break;

			case '"': /* Better safe than sorry */
				bOut.write('\\');
				bOut.write('"');
				break;

			case '\032': /* This gives problems on Win32 */
				bOut.write('\\');
				bOut.write('Z');
				break;

			default:
				bOut.write(b);
			}
		}
		return bOut;
	}

}
