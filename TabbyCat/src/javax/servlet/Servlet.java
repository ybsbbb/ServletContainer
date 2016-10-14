package javax.servlet;

import java.io.IOException;

public interface Servlet {

	void destroy();

	ServletConfig getServletConfig();

	String getServletInfo();

	void init(ServletConfig arg0) throws ServletException;

	void service(ServletRequest request, ServletResponse response) throws ServletException, IOException;

}
