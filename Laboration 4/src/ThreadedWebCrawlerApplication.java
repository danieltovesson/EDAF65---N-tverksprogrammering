import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ThreadedWebCrawlerApplication {

	// Variables
	private List<String> remainingUrls;
	private List<String> traversedUrls;
	private List<String> pendingUrls;
	private List<String> addresses;

	public static void main(String[] args) {
		ThreadedWebCrawlerApplication a = new ThreadedWebCrawlerApplication();
		a.run();
	}

	/**
	 * Runs the program
	 */
	private void run() {

		Scanner scanner = new Scanner(System.in);
		while (true) {

			// Initiate list with all URLs
			remainingUrls = new ArrayList<String>();
			traversedUrls = new ArrayList<String>();
			pendingUrls = new ArrayList<String>();
			addresses = new ArrayList<String>();

			// Get user input from console
			System.out.println("Type URL: ");
			String s = scanner.nextLine();

			// Quit program
			if (s.equals("quit")) {
				System.out.println("Application terminated");
				scanner.close();
				System.exit(1);
			}

			// Creates executor service that limits the number of active threads
			ExecutorService service = Executors.newFixedThreadPool(10);

			// Add user input to URLs
			remainingUrls.add(s);
			while (traversedUrls.size() < 1000) {

				// Creates and runs the threads
				ExecutorRunner task = new ExecutorRunner(remainingUrls, traversedUrls, pendingUrls, addresses);
				service.submit(task);

				// Break if there are no remaining or pending URLs
				if (remainingUrls.isEmpty() && pendingUrls.isEmpty()) {
					break;
				}
			}

			// Shutdown the executor service
			service.shutdown();

			// Print result to file
			try {
				PrintWriter writer = new PrintWriter("result.txt", "UTF-8");
				writer.println("--------------------------------------------------");
				writer.println("List of URLs (" + traversedUrls.size() + "):");
				writer.println("--------------------------------------------------");
				for (String url : traversedUrls) {
					writer.println(url);
				}
				writer.println("--------------------------------------------------");
				writer.println("List of addresses(" + addresses.size() + "):");
				writer.println("--------------------------------------------------");
				for (String address : addresses) {
					writer.println(address);
				}
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
}

class ExecutorRunner extends Thread {

	// Variables
	private List<String> remainingUrls;
	private List<String> traversedUrls;
	private List<String> pendingUrls;
	private List<String> addresses;

	/**
	 * Creates a ExecutorRunner object
	 * 
	 * @param threadName
	 *            the thread name
	 * @param url
	 *            the URL
	 */
	public ExecutorRunner(List<String> remainingUrls, List<String> traversedUrls, List<String> pendingUrls,
			List<String> addresses) {
		this.remainingUrls = remainingUrls;
		this.traversedUrls = traversedUrls;
		this.pendingUrls = pendingUrls;
		this.addresses = addresses;
	}

	/**
	 * Runs the program
	 */
	public void run() {
		fetchUrls();
	}

	/**
	 * Fetch URLs on a specific URL
	 */
	private void fetchUrls() {

		// Get URL string
		String urlString = getUrl();

		try {

			// Create URL
			URL url = new URL(urlString);

			// Check if the URL contains text
			URLConnection uc = url.openConnection();
			if (uc.getContentType() != null && uc.getContentType().startsWith("text/html")) {

				// Open stream
				InputStream is = url.openStream();

				// Fetch document
				String baseUri = url.getProtocol() + "://" + url.getHost();
				Document doc = Jsoup.parse(is, "UTF-8", baseUri);

				// Fetch and add a and frame links to URLs list
				Elements links = doc.getElementsByTag("a");
				links.addAll(doc.getElementsByTag("frame"));
				for (Element link : links) {

					// Try to fetch whole URL
					String s = link.attr("abs:href");
					if (s == "") {

						// Try to fetch URL without host
						s = link.attr("href");
						if (s != "") {

							// Append host
							if (!s.startsWith("/")) {
								s = "/" + s;
							}
							s = doc.baseUri() + s;
						}
					}

					// Sort addresses and URLs into different arrays
					if (s.startsWith("mailto:")) {
						if (!addresses.contains(s)) {
							addresses.add(s);
						}
					} else if (s != "") {
						if (!remainingUrls.contains(s) && !traversedUrls.contains(s)) {
							remainingUrls.add(s);
						}
					}

					is.close();
				}

				if (!traversedUrls.contains(urlString)) {
					traversedUrls.add(urlString);
				}
				pendingUrls.remove(urlString);

			} else {
				pendingUrls.remove(urlString);
				System.out.println("URL does not contain any text (" + urlString + ")");
			}

		} catch (IOException e) {
			pendingUrls.remove(urlString);
			System.out.println("Error fetching URL: " + e.getMessage() + " (" + urlString + ")");
		}
	}

	/**
	 * Gets the URL from remaining URLs
	 * 
	 * @return the URL
	 */
	private synchronized String getUrl() {
		String temp = remainingUrls.get(0);
		remainingUrls.remove(0);
		pendingUrls.add(temp);
		return temp;
	}
}
