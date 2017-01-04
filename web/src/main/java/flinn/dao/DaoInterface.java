package flinn.dao;

import java.sql.SQLException;

import javax.naming.NamingException;

public interface DaoInterface
{

	void renewConnection() throws SQLException, NamingException, Exception;
	void closeConnection() throws SQLException;
	void commitConnection() throws SQLException;

}
