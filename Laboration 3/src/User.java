import java.io.PrintWriter;

public class User {

	// Variables
	private static int userCounter = 0;
	private int id;
	private PrintWriter out;

	/**
	 * Creates a User object
	 * 
	 * @param out
	 *            the print writer of the users socket
	 */
	public User(PrintWriter out) {
		userCounter++;
		id = userCounter;
		this.out = out;
	}

	/**
	 * Gets the name of the user
	 * 
	 * @return the name of the user
	 */
	public String getName() {
		return "User " + id;
	}

	/**
	 * Gets the print writer of the users socket
	 * 
	 * @return the print writer of the users socket
	 */
	public PrintWriter getPrintWriter() {
		return out;
	}
}
