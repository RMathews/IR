package edu.neu.ir;

public class WordCount implements Comparable<WordCount>{
	
	public String word;
	public int count;
	
	public WordCount(String word, int count){
		this.word = word;
		this.count = count;
	}
	
	@Override
	public int compareTo(WordCount wc1) {		
		return this.count - wc1.count;
	}
	
	@Override
	public String toString(){
		return word + ":" + count;
	}
}
