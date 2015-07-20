package edu.neu.pagerank;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class PageRank_1 {

	private static Map<String, Link> linkMap = new HashMap<String, Link>();	
	private static List<Link> sinks = new ArrayList<Link>();	
	private static final double DAMPING_FACTOR = 0.85;
	
	public static void main(String[] args) {		
		File inputFile = new File("data\\inLinks_Main.txt");
		System.out.println(inputFile.getAbsolutePath());
		readData(inputFile);
		/*for(Map.Entry<String, Link> entry: linkMap.entrySet()){
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}*/
		preProcess();
		process();
		List<Link> allLinks = new ArrayList<Link>(linkMap.values());
		// Get top 50 pages by page rank
		Collections.sort(allLinks, new Comparator<Link>(){
			@Override
			public int compare(Link link1, Link link2) {
				return link2.getNewPageRank().compareTo(link1.getNewPageRank());
			}			
		});
		System.out.println("Top 50 pages by page rank");
		for(int idx = 0; idx < 50; idx++){
			System.out.println(allLinks.get(idx).getLink() + " : " + allLinks.get(idx).getNewPageRank());
		}
		// Now sort by in links count
		Collections.sort(allLinks, new Comparator<Link>(){
			@Override
			public int compare(Link o1, Link o2) {				
				return o2.getInLinks().size() - o1.getInLinks().size();
			}
		});
		System.out.println("Top 50 pages by in link count");
		for(int idx = 0; idx < 50; idx++){
			System.out.println(allLinks.get(idx).getLink() + " : " + allLinks.get(idx).getInLinks().size());
		}
	}
	
	private static void process(){
		double nLinks = linkMap.size();
		int iteration = 1;
		double prevPerplexity = -1 ;
		int steadyPerplexityCount = 0;
		do{			
			double sinkPR = 0;
			
			double perplexity = getPerplexity(getEntropyAndResetRank());
			System.out.println("Perplexity at iteration " + iteration + " = " + perplexity );
			
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
			
			if(perplexity - prevPerplexity < 1){
				++steadyPerplexityCount;
				if(steadyPerplexityCount >= 4){
					System.out.println("Perplexity stabilized");
					break;
				}
			}
			else{
				steadyPerplexityCount = 0;
			}		
			prevPerplexity = perplexity;
			++iteration;
		}while(true);
		// Find top 50 by page rank
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
