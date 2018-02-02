import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

// ANSWERS
// netstat -a shows the active Internet connections, my server can be seen under protocol tcp6
// Exception "Address already in use" is thrown. Can't connect to the same port twice

public class EchoTCP2 {

	public static void main(String[] args) {

		try {

			// Start server connection
			ServerSocket serverSocket = new ServerSocket(24592);

			// Count for separating clients
			int count = 1;

			while (true) {

				// Get client socket
				Socket clientSocket = serverSocket.accept();

				// Get socket streams
				InputStream is = clientSocket.getInputStream();
				OutputStream os = clientSocket.getOutputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(is));
				PrintWriter out = new PrintWriter(os, true);

				// Get client address and print it
				InetAddress inetAddress = clientSocket.getInetAddress();
				out.println("Server: Connected to client " + count + " (" + inetAddress.toString() + ")");

				// Start thread for new client connection
				Thread serverThread = new ServerThread(serverSocket, clientSocket, in, out);
				serverThread.start();

				// Increment count for next client
				count++;

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
