package edu.neu.ir;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Zipf {
	
	public static void main(String args[]) throws Exception{
		Scanner s = null;
		File inpFile = new File("data\\Alice_1.txt");
		Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
		try{
			s = new Scanner(inpFile);
			while(s.hasNextLine()){
				String word = s.nextLine();
				wordCountMap.put(word, wordCountMap.get(word) == null ? 1 : wordCountMap.get(word) + 1);
			}
		}
		finally{
			s.close();
		}
		System.out.println("Got " + wordCountMap.size() + " words in Alice");
		MinHeap<WordCount> allWordHeap = new MinHeap<WordCount>(25);
		MinHeap<WordCount> mWordHeap = new MinHeap<WordCount>(25);
		int allWordCount = 0, mWordCount = 0, lessFreqCount = 0, uniqueWords = 0;
		for(Map.Entry<String, Integer> entry : wordCountMap.entrySet()){
			if(allWordHeap.size() < 25){
				allWordHeap.add(new WordCount(entry.getKey(), entry.getValue()));
			}
			else{
				if(entry.getValue() > allWordHeap.peekMin().count){
					allWordHeap.extractMin();
					allWordHeap.add(new WordCount(entry.getKey(), entry.getValue()));
				}				
			}
			// Let's look at just the m words now
			if(entry.getKey().startsWith("m")){
				if(mWordHeap.size() < 25){
					mWordHeap.add(new WordCount(entry.getKey(), entry.getValue()));
				}
				else{
					if(entry.getValue() > mWordHeap.peekMin().count){
						mWordHeap.extractMin();
						mWordHeap.add(new WordCount(entry.getKey(), entry.getValue()));
					}				
				}
				mWordCount +=  entry.getValue();
			}				
			uniqueWords ++;
			allWordCount += entry.getValue();
			if(entry.getValue() < 5){
				lessFreqCount ++;
			}
		}	
		System.out.println("Unique word count :" +uniqueWords);
		System.out.println("Total no of words :" +allWordCount);
		System.out.println("Max occuring words " + allWordHeap);
		System.out.println("Max occuring mWords " + mWordHeap);
		System.out.println("Words with frequency less than 5 :"+lessFreqCount);
		System.out.println("Proportion of low count words " + (double)lessFreqCount / uniqueWords);
	}

}