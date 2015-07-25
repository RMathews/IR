package edu.neu.ir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Indexer {
	
	private static File inputFile = null;
	private static File outputFile = null;
	
	public static void main(String args[]){
		inputFile = new File(args[0]);
		outputFile = new File(args[1]);
		
		FileWriter fw = null;
		try {
			Map<String, List<DocFreq>> index = buildIndex(inputFile);
			fw = new FileWriter(outputFile);
			for(Map.Entry<String, List<DocFreq>> entry : index.entrySet()){
				fw.write(entry.getKey() + " -> " + entry.getValue() + "\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(fw != null){
				try {
					fw.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			}
		}
	}
	
	private static Map<String, List<DocFreq>> buildIndex(File inputFile)  throws FileNotFoundException {
		Scanner sc = null;
		Map<String, List<DocFreq>> results = new HashMap<String, List<DocFreq>>();
		try {
			sc = new Scanner(inputFile);
			String documentId = "";
			Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
			while(sc.hasNextLine()){
				String line = sc.nextLine();				
				if(line.trim().startsWith("#")){
					// We came across a new document.
					// Step 1: Store the results of parsing the previous doc
					invertStatistics(documentId, wordCountMap, results);
					// Note the id of the new document
					documentId = line.replace("#", "").trim();
					// For this document, we need a map of word v/s nOccurences.
					// So re-initialize the map
					wordCountMap = new HashMap<String, Integer>();
				}
				else{
					for(String word : line.split("\\s+")){
						if(wordCountMap.containsKey(word)){
							wordCountMap.put(word, wordCountMap.get(word) + 1);
						}
						else{
							wordCountMap.put(word, 1);
						}
					}
				}
			}
		} finally {
			if(sc != null){
				sc.close();
			}
		}
		return results;
	}
	
	private static void invertStatistics(String documentId, Map<String, Integer> wordCountMap, Map<String, List<DocFreq>> results){
		for(Map.Entry<String, Integer> entry : wordCountMap.entrySet()){
			if(results.get(entry.getKey()) == null){
				results.put(entry.getKey(), new ArrayList<DocFreq>());
			}
			results.get(entry.getKey()).add(new DocFreq(documentId, entry.getValue()));
		}
	}
}
