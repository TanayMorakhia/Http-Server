package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class RequestHandler {
    
    private final Socket clientSocket;
    private final String dir;

    public RequestHandler(Socket clientSocket, String dir){
        this.clientSocket = clientSocket;
        this.dir = dir;
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

                
                HashMap<String, String> reqHeaders = new HashMap<>();

                String temp;

                temp = in.readLine();

                while(!temp.equals("")){
                    reqHeaders.put(temp.split(" ")[0], temp.split(" ")[1]);
                    temp = in.readLine();
                }

                if(reqHeaders.containsKey("Accept-Encoding:") && reqHeaders.get("Accept-Encoding:").contains("gzip")){
                    responseString = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Encoding: "
                        + reqHeaders.get("Accept-Encoding:") + "\r\n\r\n";
                }else{
                    responseString = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " 
                                    + requestString.split(" ")[1].split("/")[2].length() 
                                    + "\r\n\r\n" + requestString.split(" ")[1].split("/")[2];
                }
            }else if(requestString.split(" ")[1].equals("/user-agent")){
                in.readLine();  
                String userAgent = in.readLine().split(" ")[1];

                responseString = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " 
                                    + userAgent.length()
                                    + "\r\n\r\n" + userAgent;
            }else if(requestString.split(" ")[1].startsWith("/file")){
                if(requestString.split(" ")[0].equals("GET")){
                    String fileName = requestString.split(" ")[1].split("/")[2];
                    File file = new File(dir, fileName);
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
                }else{

                    HashMap<String, String> req = new HashMap<>();
                    String fileName = requestString.split(" ")[1].split("/")[2];

                    String temp;

                    temp = in.readLine();

                    while(!temp.equals("")){
                        req.put(temp.split(" ")[0], temp.split(" ")[1]);
                        temp = in.readLine();
                    }

                    int length = Integer.parseInt(req.get("Content-Length:"));

                    // needed for read function
                    char[] tempBuff = new char[length];
                
                    in.read(tempBuff, 0, length);
                    System.out.println(String.valueOf(temp));

                    File file = new File(dir + fileName);

                    if(!file.exists()){
                        file.createNewFile();
                    }

                    PrintWriter write = new PrintWriter(file);
                    write.print(String.valueOf(String.valueOf(tempBuff)));
                
                    responseString = ResponseText.STATUS_201;

                    write.close();
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
