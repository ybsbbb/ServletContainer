package javax.servlet;

import server.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ServletContext {
	private static ServletContext context = new ServletContext();
	private Map<String,String> map = null;
	private List<String> list = null;
	private ServletContext(){
		init();
	}
	public static ServletContext getContext(){
		return context;
	}
	public Map<String,String> getMap(){
		return map;
	}
	public List<String> getList(){
		return list;
	}
	private void init(){
		SAXReader reader = new SAXReader();
		File file = new File(Constants.WEB_ROOT,"web.xml");
		List<String> welcomePages = new ArrayList<String>();
		Map<String,String> ncMap = new HashMap<String,String>();
		Map<String,String> nuMap = new HashMap<String,String>(); 
		Map<String,String> ucMap = new HashMap<String,String>();
		Document document;
		Element root;
		List<Element> childElements = null;
		try {
			document = reader.read(file);
			root = document.getRootElement();
			childElements = root.elements();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		for (Element child : childElements) {
			if("welcome-file-list".equals(child.getName())){
				List<Element> subs = child.elements();
				for(Element sub : subs){
					welcomePages.add(sub.getText());
				}
			} else if("servlet".equals(child.getName())){				
				ncMap.put(child.elementText("servlet-name"), child.elementText("servlet-class"));
			} else if("servlet-mapping".equals(child.getName())){
				nuMap.put(child.elementText("servlet-name"), child.elementText("url-pattern"));
			}
		}
		Set<String> ks = ncMap.keySet();
		Iterator<String> it = ks.iterator();
		while(it.hasNext()){
			String s = it.next();
			//System.out.println(s+" "+nuMap.get(s)+" "+ncMap.get(s));			
			ucMap.put(nuMap.get(s), ncMap.get(s));
		}
		map = ucMap;
		list = welcomePages;
		//Set<String> key = ucMap.keySet();
		//System.out.println(key.size());
		/*it = key.iterator();
		while(it.hasNext()){
			String s = it.next();
			System.out.println("url:"+s+"\n"+"class:"+ucMap.get(s));
		}*/
	}
}
