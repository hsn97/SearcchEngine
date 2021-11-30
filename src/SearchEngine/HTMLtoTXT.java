package SearchEngine;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLtoTXT {

	/**
	 * this method will save the html file into system
	 * 
	 * @param document
	 * @param url
	 */
	public static void saveHTML(Document document, String url) {
		try {
			
			PrintWriter html = new PrintWriter(
					new FileWriter(Path.htmlDirectoryPath + document.title().replace('/', '_') + ".html"));
			html.write(document.toString());
			html.close();
			saveText(Path.htmlDirectoryPath + document.title().replace('/', '_') + ".html", url,
					document.title().replace('/', '_') + ".txt");

		} catch (Exception e) {

		}

	}

	/**
	 * this method will save html file content to .txt file
	 * 
	 * @param htmlfile
	 * @param url
	 * @param filename
	 * @throws Exception
	 */
	public static void saveText(String htmlfile, String url, String filename) throws Exception {

		File file = new File(htmlfile);
		Document document = Jsoup.parse(file, "UTF-8");
		String data = document.text().toLowerCase();
		data = url + "::" + data;
		PrintWriter writer = new PrintWriter(Path.txtDirectoryPath + filename);
		writer.println(data);
		writer.close();
	}
}
