import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class WebCrawlerApplication {

	public static void main(String[] args) {
		WebCrawlerApplication a = new WebCrawlerApplication();
		a.run();
	}

	/**
	 * Runs the program
	 */
	private void run() {

		Scanner scanner = new Scanner(System.in);
		while (true) {

			// Get user input from console
			System.out.println("Type URL: ");
			String s = scanner.nextLine();

			// Quit program
			if (s.equals("quit")) {
				System.out.println("Application terminated");
				scanner.close();
				System.exit(1);
			}

			try {
				URL url = new URL(s);
				System.out.println("Din URL: " + url);
			} catch (MalformedURLException e) {
				System.out.println("Malformed URL, try again");
			}
		}
	}
}
