
public class User {

	// Variables
	private static int userCounter = 0;
	private int id;

	/**
	 * Creates a User object
	 * 
	 * @param name
	 *            the name of the user
	 */
	public User() {
		userCounter++;
		id = userCounter;
	}

	/**
	 * Gets the name of the user
	 * 
	 * @return the name of the user
	 */
	public String getName() {
		return "User " + id;
	}
}
