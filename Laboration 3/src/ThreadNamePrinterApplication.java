
// ANSWERS
// The order is random

public class ThreadNamePrinterApplication {

	public static void main(String[] args) {

		// Create 10 threads and start them
		Thread thread;
		for (int i = 1; i <= 10; i++) {
			thread = new NameThread("Thread " + i);
			thread.start();
		}
	}
}

class NameThread extends Thread {

	// Variables
	private String name;

	/**
	 * Creates a NameThread object
	 * 
	 * @param name
	 *            the name of the thread
	 */
	public NameThread(String name) {
		this.name = name;
	}

	/**
	 * Runs the thread
	 */
	public void run() {

		// Prints the thread name 5 times and sets a random sleep
		for (int i = 0; i < 5; i++) {
			try {
				System.out.println(name);
				sleep((int) Math.random());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
