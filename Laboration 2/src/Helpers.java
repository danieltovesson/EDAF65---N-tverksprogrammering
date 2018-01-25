import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {

	/**
	 * Gets the PDF links from the specified URL
	 * 
	 * @param s
	 *            the URL
	 * @return the PDF links
	 */
	public List<URL> fetchLinks(String s) {

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
}
