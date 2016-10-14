package javax.servlet;

import java.io.UnsupportedEncodingException;

public interface ServletRequest {

	void setCharacterEncoding(String string) throws UnsupportedEncodingException;

	String getParameter(String string);

}
