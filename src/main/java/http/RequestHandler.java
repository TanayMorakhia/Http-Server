package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

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
                responseString = ResponseText.STATUS_OK;
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
            }else if(requestString.split(" ")[1].startsWith("/file")){
                String fileName = requestString.split(" ")[1].split("/")[2];
                File file = new File(requestString.split(" ")[1], fileName);
                if(file.exists() && file.isFile()){

                    // try (FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.READ)) {
                    //     // get the size of the file
                    //     long fileSize = channel.size();    
                    // }
                    Scanner sc = new Scanner(file);

                    String content = "";
                    while(sc.hasNextLine()){
                        content += sc.nextLine();
                    }
                    responseString = "HTTP/1.1 200 OK\r\nContent-Type: application/octet-stream\r\nContent-Length:" 
                                        + file.length() + "\r\n\r\n" + content;

                    sc.close();
                }else{
                    responseString = ResponseText.STATUS_404;
                }
            }else {
                responseString = ResponseText.STATUS_404;
            }

            out.println(responseString);
            System.out.println("Response message sent to client");

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
