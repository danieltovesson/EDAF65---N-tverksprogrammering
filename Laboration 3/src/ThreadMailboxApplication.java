
// ANSWERS
// Before: It does not print all thread names
// After: It prints more threads but still not all, wait() only affects current thread and no others

public class ThreadMailboxApplication {

	public static void main(String[] args) {

		// Create mailbox
		Mailbox mailbox = new Mailbox();

		// Create 10 mailbox threads and start them
		Thread thread;
		for (int i = 1; i <= 10; i++) {
			thread = new MailboxThread("Thread " + i, mailbox);
			thread.start();
		}

		// Create print mailbox thread and start it
		PrintMailboxThread printMailboxThread = new PrintMailboxThread(mailbox);
		printMailboxThread.start();
	}
}

class Mailbox {

	// Variables
	String message;

	/**
	 * Sets a message
	 * 
	 * @param message
	 *            the message
	 */
	public synchronized void setMessage(String message) {
		try {
			this.message = message;
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets and removes a the message
	 * 
	 * @return the message
	 */
	public synchronized String getMessage() {
		String temp = message;
		message = null;
		notifyAll();
		return temp;
	}
}

class MailboxThread extends Thread {

	// Variables
	private String name;
	private Mailbox mailbox;

	/**
	 * Creates a MailboxThread object
	 * 
	 * @param name
	 *            the name of the thread
	 * @param mailbox
	 *            the mailbox
	 */
	public MailboxThread(String name, Mailbox mailbox) {
		this.name = name;
		this.mailbox = mailbox;
	}

	/**
	 * Runs the thread
	 */
	public void run() {

		// Adds name to mailbox 5 times and sets a random sleep
		for (int i = 0; i < 5; i++) {
			try {
				mailbox.setMessage(name);
				sleep((int) Math.random());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class PrintMailboxThread extends Thread {

	// Variables
	private Mailbox mailbox;

	/**
	 * Creates a PrintMailboxThread object
	 * 
	 * @param mailbox
	 *            the mailbox
	 */
	public PrintMailboxThread(Mailbox mailbox) {
		this.mailbox = mailbox;
	}

	/**
	 * Runs the thread
	 */
	public void run() {

		// Prints the mailbox message
		while (true) {
			try {
				String message = mailbox.getMessage();
				if (message != null) {
					System.out.println(message);
					sleep((int) Math.random());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
