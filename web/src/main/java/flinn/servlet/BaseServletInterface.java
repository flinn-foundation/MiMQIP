package flinn.servlet;

import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface BaseServletInterface{
	String getPageTitle();
	String getPageHeader();
	String getContentType(HttpServletRequest req);
	boolean shouldPrintHelpScreen(HttpServletRequest req);
	void printHelpScreen(PrintWriter out, Exception exception, String message);
	void create(HttpServletRequest req, HttpServletResponse resp) throws IOException;
	void read(HttpServletRequest req, HttpServletResponse resp) throws IOException;
	void update(HttpServletRequest req, HttpServletResponse resp) throws IOException;
	void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException;
	String [] getRequiredInputs();
	String[] getRequiredInputsHelpHtml();
	String [] getAvailableActions();
	String [] getAvailableActionsHelpHtml();
	String[] getReadActions();
	String[] getReadActionsHelpHtml();

}
