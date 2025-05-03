package http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private final int port;
    private final String dir;

    public HttpServer(int port, String dir){
        this.port = port;
        this.dir = dir;
    }

    public void start(){
        
        //all the resources that needs to be closed can be autoclosed if in the try parantheses
        try(ServerSocket serverSocket = new ServerSocket(4221)){
            
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
			// ensures that we don't run into 'Address already in use' errors
			serverSocket.setReuseAddress(true);
            System.out.println("Started HTTP server on port: " + port);

            
            while(true){
                Socket clientSocket = serverSocket.accept(); // Wait for connection from client.
                System.out.println("accepted new connection");

                RequestHandler handler = new RequestHandler(clientSocket, dir);

                new Thread(handler::handle).start();;
            }

			
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
    }
}
