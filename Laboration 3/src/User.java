
public class User {

	// Variables
	private static int userCounter = 0;
	private int id;
	private String name;

	/**
	 * Creates a User object
	 * 
	 * @param name
	 *            the name of the user
	 */
	public User(String name) {
		userCounter++;
		id = userCounter;
		this.name = name;
	}

	/**
	 * Gets the id of the user
	 * 
	 * @return the id of the user
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the name of the user
	 * 
	 * @return the name of the user
	 */
	public String getName() {
		return name;
	}
}
