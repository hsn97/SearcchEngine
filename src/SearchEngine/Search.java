package SearchEngine;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Search {
	
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {
		
		long start, end, totalTime, totalTimeClean, totalTimeRaw;
		
		System.out.println("Starting the Crawler-");
		String url = "https://en.wikipedia.org/wiki/William_Zinsser";
		WebCrawler.startCrawler(url, 1);
		
		if(!isValid(url)) {
			 System.out.println("The url " + url + " is not valid");
			 System.out.println("Please try again\n");
		}
		
		System.out.println("------------------");
		System.out.println("Crawler has Finished-");
		System.out.println("------------------");
		
		PreProcessing.cleantxt();
		
		
		System.out.println("------------------");
		
		System.out.println("Enter word to Search");
		String searchTerm = sc.next();
		
//		String searchTerm = "critic";
		searchTerm = PreProcessing.createLemma(searchTerm);
		searchTerm = searchTerm.trim();
		
//		start = System.currentTimeMillis();
//		parseFiles(searchTerm, "textfiles-raw/");
//		end = System.currentTimeMillis();
//		totalTimeRaw = end - start;
		
		
		start = System.currentTimeMillis();
		parseFiles(searchTerm, "textfiles-clean/");
		end = System.currentTimeMillis();
		totalTimeClean = end - start;
//		System.out.println("Time taken to search through textfiles-raw/ is " + totalTimeRaw + " milliseconds");
		System.out.println("Time taken to search through textfiles-clean/ is " + totalTimeClean + " milliseconds");
		System.out.println("------------------");
		
		
		System.out.println("Find similar words to _____(Please Enter)");
		String simTerm = sc.next();
        
        start = System.currentTimeMillis();
		SearchWord.suggestAltWord(simTerm,"textfiles-clean/");
		end = System.currentTimeMillis();
		
		totalTime = end - start;
		System.out.println("Time taken to find similar words through " + "textfiles-clean/" + " is " + totalTime + " milliseconds");
		
		start = System.currentTimeMillis();
//		SearchWord.suggestAltWord(simTerm,"textfiles-raw/");
		end = System.currentTimeMillis();
		totalTime = end - start;
		
//		System.out.println("Time taken to find similar words through " + "textfiles-raw/" + " is " + totalTime + " milliseconds");
		
	}
	
	public static void parseFiles(String word, String folder ) {
		
		File input = new File(folder);
		File[] st = input.listFiles();
		String content ="";
		
		Hashtable<String, Integer> listOfFiles = new Hashtable<String, Integer>();
		int frequency = 0;
		int noOfFiles = 0;
		
		long start, end, totalTime;
        
        start = System.currentTimeMillis();
		for(int i = 0; i<st.length; i++) {
			if(st[i].isFile() && st[i].getName().contains("txt")) {
				content ="";
				In in = new In(st[i]);
				content = in.readAll();
				frequency = SearchWord.wordSearch(content, word, st[i].getName());	
				
				if (frequency != 0) {
					listOfFiles.put(st[i].getName(), frequency);
					noOfFiles++;
				}	
			}
		}
		end = System.currentTimeMillis();
		totalTime= end - start;
		
		if (noOfFiles>0) {
			System.out.println("\nTotal Number of Files containing word - " + word + " is : " + noOfFiles);
			} else {
				System.out.println("Word:" + word + "Not Found");
			}
		
		SearchWord.rankFiles(listOfFiles, noOfFiles);
		
		
	}
	
	/**
	 * It will validate url entered by user with DNS
	 * @param url
	 * @return
	 */
	public static boolean isValid(String url)
    {
        /* Try creating a valid URL */
        try {
        	System.out.println("Validating URL...");
        	URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            //Sending the request
            conn.setRequestMethod("GET");
            int response = conn.getResponseCode();
            if(response==200) {
            	 return true;
            }else {
            	return false;
            }
           
        }
          
        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }

}
