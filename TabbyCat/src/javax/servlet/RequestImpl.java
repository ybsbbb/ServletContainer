package javax.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class RequestImpl implements ServletRequest{

	private InputStream input;
    private String uri;
    private HashMap<String,String> params = null;

	public RequestImpl(InputStream input) {
		this.input = input;
	}

	public String getUri() {
		return uri;
	}
	/**
     * 
     * requestString形式如下：
     * GET /index.html HTTP/1.1
     * Host: localhost:8080
     * Connection: keep-alive
     * Cache-Control: max-age=0
     * ...
     * 该函数目的就是为了获取/index.html字符串
     */
	private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1)
                return requestString.substring(index1 + 1, index2);
        }
        return null;
    }
	
	//从InputStream中读取request信息，并从request中获取uri值
    public void parse() {
        // Read a set of characters from the socket
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            request.append((char) buffer[j]);
        }
        System.out.println(request.toString());
        uri = parseUri(request.toString());
    }
	
	@Override
	public void setCharacterEncoding(String string) throws UnsupportedEncodingException{
		
	}

	@Override
	public String getParameter(String string) {
		return params.get(string);
	}

	public void setParams(HashMap<String,String> param){
		params = param;
	}

}
