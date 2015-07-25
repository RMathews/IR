package edu.neu.ir;

public class DocFreq {
	public String docId;
	public Integer freq;
	
	public DocFreq(String docId, Integer freq){
		this.docId = docId;
		this.freq = freq;
	}
	
	@Override
	public String toString(){
		return "(" + docId +"," + freq + ")";
	}
}
