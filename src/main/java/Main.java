import http.HttpServer;

public class Main {
	public static void main(String[] args) {
		// You can use print statements as follows for debugging, they'll be visible when running tests.
		System.out.println("Logs from your program will appear here!");
		System.out.println("Starting http server....");

		String directory = null;

        for (int i = 0; i < args.length; i++) {
            if ("--directory".equals(args[i]) && i + 1 < args.length) {
                directory = args[i + 1];
            }
        }

        if (directory == null) {
            System.out.println("No directory specified, using default directory.");
            directory = ".";
        }

		HttpServer server = new HttpServer(4221, directory);
		server.start();
	}
}
