package SearchEngine;
import SearchEngine.In;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.*;
import edu.stanford.nlp.coref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.logging.RedwoodConfiguration;

public class PreProcessing {

	public static void cleantxt() throws IOException {
		
		File input = new File("textfiles-raw/");
		File[] st = input.listFiles();
//		System.out.println(st.length);
		String content;
		String lemContent;
		String swContent;
		
		HashMap<String, Integer> stopWords = stopwordsHash();
		
		for(int i = 0 ; i< st.length;i++) {
			if(st[i].isFile() && st[i].getName().contains("txt")) {
				content = "";
				In in = new In(st[i]);
				content = in.readAll();
				
				//First we lemmatize every word
				lemContent = "";
				lemContent = createLemma(content);
				
				//Then delete stop words
				swContent = "";
				swContent = deleteStopwords(lemContent, stopWords);

				writeCleantxt(st[i], swContent);
			}
		}
	}
	
	//Function to lemmatize the input string
	public static String createLemma(String content) {
		
		Properties props = new Properties();
		
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		
		RedwoodConfiguration.current().clear().apply();
		
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		 // create an empty Annotation just with the given text
		Annotation document = new Annotation(content);
		
		// run all Annotators on this text
		pipeline.annotate(document);
		
		 // these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		 
		String lemma = "";
		
		for(CoreMap sentence: sentences) {
			
			 for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
				 
				 String lem = token.get(LemmaAnnotation.class);
				 
				 lemma += lem;
				 lemma+=" ";
			 }
			 
			 lemma+="\r\n";
		}
		
		return lemma;
	}
	
	
	//Function to split input string by word and check if the word is a stop word
	public static String deleteStopwords(String content, HashMap<String, Integer> stopWords) {
		
		String result = "";
		
		for(String s : content.split("\\b")) {
			if(!stopWords.containsKey(s)) {
				result += s;
			}
		}
		
//		String[] stopWords = new String[100];
//		
//		for(String s : content.split("\\b")) {
//			for(int i =0; i<stopWords.length; i++) {
//				if(stopWords[i].equals(s)) {
//					System.out.println("Delete word");
//				}else {
//					result +=s;
//				}
//			}
//			
//		}
		return result;
		
		
	}
	
	//Function to create and write a "clean" txt file for each file read
	public static void writeCleantxt(File F, String content) throws IOException {
		
		String filename = "";
		filename = F.getName().replace(".txt", "-clean.txt");
		BufferedWriter out = new BufferedWriter(new FileWriter("textfiles-clean/" + filename));
        out.write(content);
        out.close();
        System.out.println("Created File" + filename);
		
	}
	
	//Function to create hash map of stopwords using the stopwords txt
	public static HashMap<String, Integer> stopwordsHash() {
		
		File input1 = new File(Path.stopWordsPath);
		In in = new In(input1);
		String text = "";
		int ctr = 0;
		
		String[] stoplist = new String[851];					//Initially create an array which is then converted to a hashmap
		
		while((text = in.readLine()) != null) {
			stoplist[ctr] = text;
			ctr++;
		}
		
		HashMap<String, Integer> stopWords = new HashMap<>();	//This allows for much quicker checking of data
		
		for(int j = 0; j< stoplist.length; j++) {
			stopWords.put(stoplist[j], j);
		}
		
		return stopWords;
	}
	
	
}
