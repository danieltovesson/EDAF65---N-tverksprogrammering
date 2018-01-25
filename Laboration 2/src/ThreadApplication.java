import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

class ThreadExample extends Thread {

	// Variables
	String threadName;
	URL url;

	/**
	 * Creates a TreadExample object
	 * 
	 * @param threadName
	 *            the thread name
	 */
	public ThreadExample(String threadName) {
		this.threadName = threadName;
	}

	/**
	 * Starts the thread
	 * 
	 * @param url
	 *            the URL to download
	 */
	public void start(URL url) {
		this.url = url;
		start();
	}

	/**
	 * Runs the program
	 */
	public void run() {
		downloadLink(url);
	}

	/**
	 * Downloads all the PDFs
	 * 
	 * @param links
	 *            the PDF links
	 */
	private void downloadLink(URL url) {

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

				} else {
					System.out.println("No links found");
				}
			}

		}
		scanner.close();
	}
}
