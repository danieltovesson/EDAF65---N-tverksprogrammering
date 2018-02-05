import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class ServerThread extends Thread {

	// Variables
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;
	private Mailbox mailbox;
	private Vector<User> users;
	private User user;

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
	 * @param mailbox
	 *            the mailbox
	 * @param users
	 *            the users
	 */
	public ServerThread(ServerSocket serverSocket, Socket clientSocket, BufferedReader in, PrintWriter out,
			Mailbox mailbox, Vector<User> users) {
		this.serverSocket = serverSocket;
		this.clientSocket = clientSocket;
		this.in = in;
		this.out = out;
		this.mailbox = mailbox;
		this.users = users;
	}

	/**
	 * Runs the thread
	 */
	public void run() {

		// Adds user to the vector
		addUser();

		try {

			// Read lines
			String inputLine, outputLine;
			EchoProtocol ep = new EchoProtocol();
			while ((inputLine = in.readLine()) != null) {
				outputLine = ep.processInput(inputLine);
				if (inputLine.startsWith("e ") || outputLine.equals(EchoProtocol.UNKNOWN)) {
					out.println("Server: " + outputLine);
					out.flush();
				} else {
					String timestamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
					mailbox.setMessage(timestamp + " " + user.getName() + ": " + outputLine);
				}
				if (inputLine.equals("q")) {
					removeUser();
					clientSocket.close();
					break;
				} else if (inputLine.equals("q server")) {
					removeAllUsers();
					serverSocket.close();
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the user to vector with active users
	 */
	private synchronized void addUser() {
		user = new User(out);
		users.add(user);
	}

	/**
	 * Removes the user
	 */
	private synchronized void removeUser() {
		users.remove(user);
	}

	/**
	 * Removes all users
	 */
	private synchronized void removeAllUsers() {
		users.clear();
	}
}
