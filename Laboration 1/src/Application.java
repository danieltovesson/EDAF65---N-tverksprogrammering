import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application {

	public static void main(String[] args) {
		Application a = new Application();
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
				break;
			}

			// Get the PDF links
			List<URL> links = fetchLinks(s);

			// Download the PDFs
			if (links != null) {
				if (links.size() != 0) {
					downloadLinks(links);
				} else {
					System.out.println("No links found");
				}
			}

		}
		scanner.close();
	}

	/**
	 * Gets the PDF links from the specified URL
	 * 
	 * @param s
	 *            the URL
	 * @return the PDF links
	 */
	private List<URL> fetchLinks(String s) {

		// Array containing all the matches
		ArrayList<URL> matches = new ArrayList<>();

		// Variables used for getting the content of a URL
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;

		try {

			// Create URL, open input stream on URL and create a buffered
			// reader
			url = new URL(s);
			is = url.openStream();
			br = new BufferedReader(new InputStreamReader(is));

			// Read every line and create a content string
			String content = "";
			while ((line = br.readLine()) != null) {
				content += line;
			}

			// Find all PDF URL's in the content string
			Pattern pattern = Pattern.compile("<a href=\"(.*?)\">");
			Matcher matcher = pattern.matcher(content);
			while (matcher.find()) {
				String match = matcher.group(1);
				if (match.endsWith(".pdf")) {
					matches.add(new URL(match));
				}
			}

		} catch (MalformedURLException | FileNotFoundException e) {
			System.out.println("Malformed URL, try again");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return matches;
	}

	/**
	 * Downloads all the PDFs
	 * 
	 * @param links
	 *            the PDF links
	 */
	private void downloadLinks(List<URL> links) {

		// Counter separating files with identical names
		int count = 0;

		for (URL url : links) {

			// Increment counter for next file
			count++;

			InputStream in;
			try {

				// Create file name
				String fileName = url.toString();
				fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
				fileName = fileName.substring(0, fileName.indexOf(".pdf"));

				// Add count and extension to file name
				fileName = count + ". " + fileName + ".pdf";

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
				count--;
			}

		}

		System.out.println(count + " PDFs downloaded successfully");
	}
}
