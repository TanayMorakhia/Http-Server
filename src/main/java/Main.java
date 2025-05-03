import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) {
		// You can use print statements as follows for debugging, they'll be visible when running tests.
		System.out.println("Logs from your program will appear here!");

		// Uncomment this block to pass the first stage

		try {
			ServerSocket serverSocket = new ServerSocket(4221);

			// Since the tester restarts your program quite often, setting SO_REUSEADDR
			// ensures that we don't run into 'Address already in use' errors
			serverSocket.setReuseAddress(true);

			Socket clientSocket = serverSocket.accept(); // Wait for connection from client.
			System.out.println("accepted new connection");

			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

			String requestString, responseString, path;

			requestString = in.readLine();

			System.out.println("Msg received from client " + requestString);

			Matcher m = Pattern.compile("/\\S+").matcher(requestString);
			
			if(m.find()){
				path = m.group();
			}else{
				path = "";
			}

			if(path == "/"){
				responseString = "HTTP/1.1 200 OK\r\n\r\n";
			}else{
				responseString = "HTTP/1.1 404 Not Found\r\n\r\n";
			}

			out.println(responseString);

			System.out.println("Response message sent to client");
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}
}
