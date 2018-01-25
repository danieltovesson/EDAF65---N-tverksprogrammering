import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

class UrlStack extends Stack<URL> {

	/**
	 * Creates a UrlStack object
	 * 
	 * @param urls
	 *            the URLs
	 */
	public UrlStack(List<URL> urls) {
		for (int i = 0; i < urls.size(); i++) {
			this.push(urls.get(i));
		}
	}

	/**
	 * Gets the top URL
	 * 
	 * @return the URL
	 */
	public synchronized URL getUrl() {
		if (!this.empty()) {
			return this.pop();
		} else {
			return null;
		}
	}
}

class Runner extends Thread {

	// Variables
	String threadName;
	UrlStack urlStack;

	/**
	 * Creates a Runner object
	 * 
	 * @param threadName
	 *            the thread name
	 * @param urlStack
	 *            the URL stack
	 */
	public Runner(String threadName, UrlStack urlStack) {
		this.threadName = threadName;
		this.urlStack = urlStack;
	}

	/**
	 * Runs the program
	 */
	public void run() {
		while (!urlStack.isEmpty()) {
			downloadLink();
		}
	}

	/**
	 * Downloads a PDF
	 */
	public synchronized void downloadLink() {

		// Gets the URL
		URL url = urlStack.getUrl();

		InputStream in;
		try {

			// Create file name
			String fileName = url.toString();
			fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
			fileName = fileName.substring(0, fileName.indexOf(".pdf"));

			// Add count and extension to file name
			fileName = threadName + " - " + fileName + ".pdf";

			// Open input stream
			in = url.openStream();

			// Create file to write to
			FileOutputStream fos = new FileOutputStream(new File(fileName));

			// Write to file
			int length = -1;
			byte[] buffer = new byte[1024];
			while ((length = in.read(buffer)) > -1) {
				fos.write(buffer, 0, length);
			}

			// Close streams
			fos.close();
			in.close();

		} catch (IOException e) {
			System.out.println("Could not download file: " + url.toString());
		}

		System.out.println("PDF downloaded successfully on thread " + threadName);
	}
}

public class ThreadApplication {

	public static void main(String[] args) {

		Helpers helpers = new Helpers();

		Scanner scanner = new Scanner(System.in);
		while (true) {

			// Get user input from console
			System.out.println("Type URL: ");
			String s = scanner.nextLine();

			// Quit program
			if (s.equals("quit")) {
				System.out.println("Application terminated");
				break;
			}

			// Get the PDF links
			List<URL> links = helpers.fetchLinks(s);

			// Download the PDFs
			if (links != null) {
				if (links.size() != 0) {

					// Specifies the number of threads
					int numThreads = 3;

					// Creates a URL stack
					UrlStack urlStack = new UrlStack(links);

					// Creates threads and starts them
					for (int i = 0; i < numThreads; i++) {
						Runner thread = new Runner("Thread " + (i + 1), urlStack);
						thread.start();
					}

				} else {
					System.out.println("No links found");
				}
			}

		}
		scanner.close();
	}
}
