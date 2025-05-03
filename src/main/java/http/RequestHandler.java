package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler {
    
    private final Socket clientSocket;

    public RequestHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    public void handle(){
        
        try{

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String requestString, responseString;

            requestString = in.readLine();

            System.out.println("Msg received from client " + requestString);

            if (requestString != null && requestString.split(" ")[1].equals("/")) {
                responseString = "HTTP/1.1 200 OK\r\n\r\n";
            }else if(requestString.split(" ")[1].split("/")[1].equals("echo")){
                responseString = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " 
                                    + requestString.split(" ")[1].split("/")[2].length() 
                                    + "\r\n\r\n" + requestString.split(" ")[1].split("/")[2];
            }else if(requestString.split(" ")[1].equals("/user-agent")){
                in.readLine();  
                String userAgent = in.readLine().split(" ")[1];

                responseString = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " 
                                    + userAgent.length()
                                    + "\r\n\r\n" + userAgent;
            }else {
                responseString = "HTTP/1.1 404 Not Found\r\n\r\n";
            }

            out.println(responseString);
            System.out.println("Response message sent to client");

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
