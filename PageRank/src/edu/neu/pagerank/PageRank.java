package edu.neu.pagerank;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class PageRank {

	private static Map<String, Link> linkMap = new HashMap<String, Link>();	
	private static List<Link> sinks = new ArrayList<Link>();	
	private static final double DAMPING_FACTOR = 0.85;
	
	public static void main(String[] args) {		
		File inputFile = new File("data\\inLinks_Sample.txt");
		System.out.println(inputFile.getAbsolutePath());
		readData(inputFile);
		/*for(Map.Entry<String, Link> entry: linkMap.entrySet()){
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}*/
		preProcess();
		process();
	}
	
	private static void process(){
		double nLinks = linkMap.size();
		int iteration = 1;		
		do{			
			double sinkPR = 0;
			
			double perplexity = getPerplexity(getEntropyAndResetRank());
			//System.out.println("Perplexity at iteration " + iteration + " = " + perplexity );
			
			for(Link sink : sinks){
				sinkPR += sink.getOldPageRank();
			}
			for(Link link : linkMap.values()){
				double newPageRank = (1 - DAMPING_FACTOR) / nLinks;
				newPageRank += DAMPING_FACTOR * sinkPR / nLinks;
				for(Link inLink : link.getInLinks()){
					newPageRank += DAMPING_FACTOR * inLink.getOldPageRank() / inLink.getOutLinks().size();
				}
				link.setNewPageRank(newPageRank);
			}
			
			if(iteration == 1 || iteration == 10 || iteration == 100){
				System.out.println("Iteration : " + iteration);
				System.out.println("Perplexity : " +  perplexity);
				for(Link link : linkMap.values()){
					System.out.println(link);
				}
			}
		}while(++iteration <= 100);
	}
	
	private static double getEntropyAndResetRank(){
		double entropy = 0;		
		for(Link link : linkMap.values()){
			entropy -= link.getNewPageRank() * ( Math.log(link.getNewPageRank()) / Math.log(2));
			link.setOldPageRank(link.getNewPageRank());		
		}
		return entropy;
	}
	
	private static double getPerplexity(double entropy){
		return Math.pow(2, entropy);
	}
	
	private static void preProcess(){
		double nLinks = linkMap.size();
		double initialPgRank = Double.valueOf(1) / nLinks;
		// First lets find out which are the sinks
		for(Link currentLink : linkMap.values()){			
			if(currentLink.getOutLinks().isEmpty()){
				sinks.add(currentLink);
			}
			currentLink.setNewPageRank(initialPgRank);
		}
	}
	
	public static void readData(File f){
		Scanner sc = null;
		try{
			sc = new Scanner(f);
			while(sc.hasNextLine()){
				String line = sc.nextLine();
				// Split the line by space
				String [] links = line.trim().split("\\s+");
				Link currentLink = linkMap.get(links[0]);
				if(currentLink == null){
					currentLink = new Link(links[0]);
					linkMap.put(links[0], currentLink);
				}
				
				for (int idx = 1; idx < links.length; idx++){
					Link inLink = linkMap.get(links[idx]);
					if(inLink == null){
						inLink = new Link(links[idx]);
						linkMap.put(links[idx], inLink);
					}
					currentLink.getInLinks().add(inLink);
					inLink.getOutLinks().add(currentLink);
				}
				
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally{
			if(sc != null){
				sc.close();
			}
		}
		
	}


}
