import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoTCP2 {

	public static void main(String[] args) {

		try {

			while (true) {

				// Start server connection
				ServerSocket serverSocket = new ServerSocket(24592);

				// Get client socket
				Socket clientSocket = serverSocket.accept();

				// Get socket streams
				InputStream is = clientSocket.getInputStream();
				OutputStream os = clientSocket.getOutputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(is));
				PrintWriter out = new PrintWriter(os, true);

				// Get client address and print it
				InetAddress inetAddress = clientSocket.getInetAddress();
				out.println("Server: Connected to " + inetAddress.toString());

				// Read lines
				String inputLine, outputLine;
				EchoProtocol ep = new EchoProtocol();
				while ((inputLine = in.readLine()) != null) {
					outputLine = ep.processInput(inputLine);
					out.println("Server: " + outputLine);
					out.flush();
					if (inputLine.equals("q")) {
						clientSocket.close();
						serverSocket.close();
						break;
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
