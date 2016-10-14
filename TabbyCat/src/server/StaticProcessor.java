package server;

import java.io.IOException;

import javax.servlet.RequestImpl;
import javax.servlet.ResponseImpl;

public class StaticProcessor {

	public void process(RequestImpl request, ResponseImpl response) {
	        try {
	            response.sendStaticResource();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}

}
