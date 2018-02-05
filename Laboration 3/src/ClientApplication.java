import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientApplication {

	// java ChatClient localhost 24593

	public static void main(String[] args) {
		ClientApplication a = new ClientApplication();
		a.run();
	}

	/**
	 * Runs the program
	 */
	private void run() {

		// Create scanner
		Scanner scanner = new Scanner(System.in);

		// Get user input from console
		String s = scanner.nextLine();

		String machine = null;
		Number port = null;

		// Parse user input
		Pattern p = Pattern.compile("java ChatClient (.+?) (\\d+)");
		Matcher m = p.matcher(s);
		while (m.find()) {
			machine = m.group(1);
			port = Integer.parseInt(m.group(2));
		}

		if (machine != null && port != null) {

			Socket socket = null;
			InputStream is = null;
			OutputStream os = null;
			try {

				// Create socket and get input and output streams
				socket = new Socket(machine, port.intValue());
				is = socket.getInputStream();
				os = socket.getOutputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(is));
				PrintWriter out = new PrintWriter(os, true);

				// Start input stream thread
				InputThread inputThread = new InputThread(in);
				inputThread.start();

				// Start output stream thread
				OutputThread outputThread = new OutputThread(scanner, out);
				outputThread.start();

			} catch (IOException e) {
				System.out.println("Connection error");
			}

		} else {
			System.out.println("Invalid command");
		}
	}
}

class InputThread extends Thread {

	// Variables
	private BufferedReader in;

	/**
	 * Creates a InputThread object
	 * 
	 * @param in
	 *            the input stream
	 */
	public InputThread(BufferedReader in) {
		this.in = in;
	}

	public void run() {

		// Show server output
		try {
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
			}
		} catch (IOException e) {
			System.out.println("Connection error");
		}
	}
}

class OutputThread extends Thread {

	// Variables
	private Scanner scanner;
	private PrintWriter out;

	/**
	 * Creates a OutputThread object
	 * 
	 * @param scanner
	 *            the scanner
	 * @param out
	 *            the output stream
	 */
	public OutputThread(Scanner scanner, PrintWriter out) {
		this.scanner = scanner;
		this.out = out;
	}

	public void run() {

		// Get user input from console
		while (true) {
			String s = scanner.nextLine();
			out.println(s);
			out.flush();
		}
	}
}
