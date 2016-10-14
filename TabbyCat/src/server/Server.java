package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.servlet.RequestImpl;
import javax.servlet.ResponseImpl;

public class Server {
	// �رշ�������
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    public static void main(String[] args) {
        Server server = new Server();
        //�ȴ���������
        server.await();
    }

    public void await() {
        ServerSocket serverSocket = null;
        int port = 8080;
        try {
            //�������׽��ֶ���
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // ѭ���ȴ�����
        while (true) {
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            try {
                //�ȴ����ӣ����ӳɹ��󣬷���һ��Socket����
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();

                // ����Request���󲢽���
                RequestImpl request = new RequestImpl(input);
                request.parse();
                // ����Ƿ��ǹرշ�������
                if(request.getUri()!=null){
                    if (request.getUri().equals(SHUTDOWN_COMMAND)) {
                        break;
                    }
                    // ���� Response ����
                    ResponseImpl response = new ResponseImpl(output);
                    response.setRequest(request);

                    if (request.getUri().startsWith("/servlet/")) {
                    	//����uri��/servlet/��ͷ����ʾservlet����
                    	DynamicProcessor processor = new DynamicProcessor();
                        processor.process(request, response);
                    } else {
                        //��̬��Դ����
                        StaticProcessor processor = new StaticProcessor();
                        processor.process(request, response);
                    }
                }
                
                // �ر� socket  
                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
