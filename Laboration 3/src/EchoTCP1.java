import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoTCP1 {

	public static void main(String[] args) {

		while (true) {

			ServerSocket serverSocket;
			try {

				// Start connection
				serverSocket = new ServerSocket(30000);
				Socket clientSocket = serverSocket.accept();
				InputStream is = clientSocket.getInputStream();
				OutputStream os = clientSocket.getOutputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(is));
				PrintWriter out = new PrintWriter(os, true);

				// Variables
				String inputLine, outputLine;

				// Read lines
				EchoProtocol ep = new EchoProtocol();
				while ((inputLine = in.readLine()) != null) {
					outputLine = ep.processInput(inputLine);
					out.println(outputLine);
					if (inputLine.equals("q")) {
						clientSocket.close();
						break;
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
