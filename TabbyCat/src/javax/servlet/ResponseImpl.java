package javax.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import server.Constants;

public class ResponseImpl implements ServletResponse{

	private static final int BUFFER_SIZE = 1024;
    RequestImpl request;
    OutputStream output;
    PrintWriter writer;
	
    public ResponseImpl(OutputStream output) {
        this.output = output;
    }
    public void setRequest(RequestImpl request) {
        this.request = request;
    }
    //将web文件写入到OutputStream字节流中
    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            /* request.getUri has been replaced by request.getRequestURI */
        	String rootPath = Constants.WEB_ROOT;
        	String requestUri = request.getUri();
        	String path = null;
        	if("".equals(requestUri)||"/".equals(requestUri)){
        		ServletContext context = ServletContext.getContext();
        		List<String> wel = context.getList();        		
        		path = rootPath + getWelcomeFile(wel);
        	}
        	else{
            	path = rootPath + requestUri;        		
        	}
        	System.out.println(path);
            File file = new File(path);
            fis = new FileInputStream(file);
            /*
             * HTTP Response = Status-Line(( general-header | response-header |
             * entity-header ) CRLF) CRLF [ message-body ] Status-Line =
             * HTTP-Version SP Status-Code SP Reason-Phrase CRLF
             */
            int ch = fis.read(bytes, 0, BUFFER_SIZE);
            while (ch != -1) {
                output.write(bytes, 0, ch);
                ch = fis.read(bytes, 0, BUFFER_SIZE);
            }
        } catch (FileNotFoundException e) {
            String errorMessage = "HTTP/1.1 404 File Not Found\r\n"
                    + "Content-Type: text/html\r\n" + "Content-Length: 23\r\n"
                    + "\r\n" + "<h1>File Not Found</h1>";
            output.write(errorMessage.getBytes());
        } finally {
            if (fis != null)
                fis.close();
        }
    }
	@Override
	public PrintWriter getWriter() {
		// Autoflush is true, println() will flush,
        // but print() will not.
        writer = new PrintWriter(output, true);
        return writer;
	}
	
	private String getWelcomeFile(List<String> list){
		String path = Constants.WEB_ROOT;
		File root = new File(path);
		String[] all = root.list();
		List<String> allList = new ArrayList<String>();
		for(int i=0;i<all.length;i++){
			allList.add(all[i]);
		}
		allList.retainAll(list);
		System.out.println(allList.get(0));
		if(allList.isEmpty()){
			return null;
		}
		return File.separator+allList.get(0);
	}

}
