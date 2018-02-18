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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawlerApplication {

	// Variables
	private List<String> urls;
	private List<String> addresses;

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

			// Initiate list with all URLs
			urls = new ArrayList<String>();
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

			// Add user input to URLs
			urls.add(s);
			int i = 0;
			while (urls.size() < 1000) {

				// Fetch URLs on URL
				fetchUrls(urls.get(i));
				i++;
				if (i == urls.size()) {
					break;
				}
			}

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

	/**
	 * Fetch URLs on a specific URL
	 * 
	 * @param url
	 *            the URL
	 * @return the list with URLs
	 * @throws IOException
	 */
	private void fetchUrls(String urlString) {

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
						if (!urls.contains(s)) {
							urls.add(s);
						}
					}

					is.close();
				}

			} else {
				System.out.println("URL does not contain any text (" + urlString + ")");
			}

		} catch (IOException e) {
			System.out.println("Error fetching URL: " + e.getMessage() + " (" + urlString + ")");
		}
	}
}
