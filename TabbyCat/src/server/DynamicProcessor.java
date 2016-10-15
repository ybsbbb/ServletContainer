package server;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestImpl;
import javax.servlet.ResponseImpl;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class DynamicProcessor {
	ServletContext context = ServletContext.getContext();
	Map<String,String> ucMap = context.getMap();
	public void process(RequestImpl request, ResponseImpl response) {
		String uri = request.getUri();
		int paraFlag = -1;
		paraFlag = uri.indexOf("?");
		String servletName = null;
		if(paraFlag!=-1){
	        servletName = ucMap.get(uri.substring(uri.lastIndexOf("/"),paraFlag));
	        HashMap<String,String> param = new HashMap<String, String>();
	        int lastParaFlag = paraFlag;
	        int nextParaFlag = -1;
	        String p,p1,p2;
	        while(true){
	        	nextParaFlag = uri.indexOf("&",nextParaFlag+1);
	        	if(nextParaFlag!=-1){
		        	p = uri.substring(lastParaFlag+1, nextParaFlag);	        		
	        	}else{
	        		p = uri.substring(lastParaFlag+1);
	        	}
	        	lastParaFlag = nextParaFlag;
	        	p1 = p.substring(0, p.indexOf("="));
	        	p2 = p.substring(p.indexOf("=")+1);
	        	param.put(p1, p2);
	        	request.setParams(param);
	        	//String line = p1+":"+param.get(p1);
	        	//System.out.println(line);
	        	if(nextParaFlag==-1){
		        	break;	        		
	        	}
	        }
		}else{
	        servletName = ucMap.get(uri.substring(uri.lastIndexOf("/")));
			HashMap<String,String> param = new HashMap<String, String>();
			request.setParams(param);
		}
        
        
        //类加载器，用于从指定JAR文件或目录加载类
        URLClassLoader loader = null;
        try {
            URLStreamHandler streamHandler = null;
            //创建类加载器
            loader = new URLClassLoader(new URL[]{new URL(null, "file:" + Constants.WEB_SERVLET_ROOT, streamHandler)});
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        
        Class<?> myClass = null;
        try {
            //加载对应的servlet类
            myClass = loader.loadClass(servletName);
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }

        Servlet servlet = null;

        try {
            //生产servlet实例
            servlet = (Servlet) myClass.newInstance();
            //执行servlet的service方法
            servlet.service((ServletRequest) request,(ServletResponse) response);
        } catch (Exception e) {
            System.out.println(e.toString());
        } catch (Throwable e) {
            System.out.println(e.toString());
        }
		
	}

}
