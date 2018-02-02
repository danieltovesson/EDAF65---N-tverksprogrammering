import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {

	// Variables
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;

	/**
	 * Creates a ServerThread object
	 * 
	 * @param serverSocket
	 *            the server socket
	 * @param clientSocket
	 *            the client socket
	 * @param in
	 *            the input from the client
	 * @param out
	 *            the output from the server
	 */
	public ServerThread(ServerSocket serverSocket, Socket clientSocket, BufferedReader in, PrintWriter out) {
		this.serverSocket = serverSocket;
		this.clientSocket = clientSocket;
		this.in = in;
		this.out = out;
	}

	/**
	 * Runs the thread
	 */
	public void run() {

		try {

			// Read lines
			String inputLine, outputLine;
			EchoProtocol ep = new EchoProtocol();
			while ((inputLine = in.readLine()) != null) {
				outputLine = ep.processInput(inputLine);
				out.println("Server: " + outputLine);
				out.flush();
				if (inputLine.equals("q")) {
					clientSocket.close();
					break;
				} else if (inputLine.equals("q server")) {
					serverSocket.close();
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
