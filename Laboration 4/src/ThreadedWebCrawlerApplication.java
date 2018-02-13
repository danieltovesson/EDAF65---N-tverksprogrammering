import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ThreadedWebCrawlerApplication {

	// Variables
	private Set<String> remainingUrls;
	private Set<String> traversedUrls;
	private Set<String> urls;
	private Set<String> addresses;

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
			remainingUrls = new HashSet<String>();
			traversedUrls = new HashSet<String>();
			urls = new HashSet<String>();
			addresses = new HashSet<String>();

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
			while (urls.size() < 1000) {

				// Creates and runs the threads
				ExecutorRunner task = new ExecutorRunner(remainingUrls, traversedUrls, urls, addresses);
				service.submit(task);

				// Break if there are no remaining or pending URLs
				if (remainingUrls.isEmpty()) {
					break;
				}
			}

			// Shutdown the executor service
			service.shutdown();

			// Print result to file
			try {
				PrintWriter writer = new PrintWriter("result.txt", "UTF-8");
				writer.println("--------------------------------------------------");
				writer.println("List of URLs (" + urls.size() + "):");
				writer.println("--------------------------------------------------");
				for (String url : urls) {
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
	private Set<String> remainingUrls;
	private Set<String> traversedUrls;
	private Set<String> urls;
	private Set<String> addresses;

	/**
	 * Creates a ExecutorRunner object
	 * 
	 * @param threadName
	 *            the thread name
	 * @param url
	 *            the URL
	 */
	public ExecutorRunner(Set<String> remainingUrls, Set<String> traversedUrls, Set<String> urls,
			Set<String> addresses) {
		this.remainingUrls = remainingUrls;
		this.traversedUrls = traversedUrls;
		this.urls = urls;
		this.addresses = addresses;
	}

	/**
	 * Runs the program
	 */
	public void run() {
		fetchUrls(getUrl());
	}

	/**
	 * Fetch URLs on a specific URL
	 */
	private void fetchUrls(String urlString) {

		if (urlString == null) {
			return;
		}

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
						addresses.add(s);
					} else if (s != "" && !traversedUrls.contains(s)) {
						remainingUrls.add(s);
					}

				}

				// Close stream
				is.close();

				// Add to URLs
				urls.add(urlString);

			} else {
				System.out.println("URL does not contain any text (" + urlString + ")");
			}

		} catch (IOException e) {
			System.out.println("Error fetching URL: " + e.getMessage() + " (" + urlString + ")");
		}

		// Remove from remaining URLs
		remainingUrls.remove(urlString);
	}

	/**
	 * Gets the URL from remaining URLs
	 * 
	 * @return the URL
	 */
	private synchronized String getUrl() {
		for (String url : remainingUrls) {
			if (!traversedUrls.contains(url)) {
				traversedUrls.add(url);
				return url;
			}
		}
		return null;
	}
}
