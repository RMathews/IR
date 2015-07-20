package edu.neu.pagerank;
import java.util.HashSet;
import java.util.Set;


public class Link  {

	private String link;
	private Double oldPageRank;
	private Double newPageRank;
	private Set <Link> inLinks;
	private Set <Link> outLinks;	
	
	public Link(String link){
		this.link = link;
		this.oldPageRank = Double.valueOf(0);
		inLinks = new HashSet<Link>();
		outLinks = new HashSet<Link>();		
	}
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Set<Link> getInLinks() {
		return inLinks;
	}
	public void setInLinks(Set<Link> inLinks) {
		this.inLinks = inLinks;
	}

	public Set<Link> getOutLinks() {
		return outLinks;
	}

	public void setOutLinks(Set<Link> outLinks) {
		this.outLinks = outLinks;
	}
		
	public Double getOldPageRank() {
		return oldPageRank;
	}

	public void setOldPageRank(Double pageRank) {
		this.oldPageRank = pageRank;
	}
	
	public Double getNewPageRank() {
		return newPageRank;
	}

	public void setNewPageRank(Double newPageRank) {
		this.newPageRank = newPageRank;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		sb.append("Link : ").append(link);
		sb.append("; New page rank : ").append(newPageRank);
		sb.append("; In links : ");		
		for(Link l : inLinks){
			sb.append(l.getLink());
			sb.append(",");
		}
		sb.append("; Out links :");
		for(Link l : outLinks){
			sb.append(l.getLink());
			sb.append(",");
		}
		return sb.toString();
		
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Link){
			Link l = (Link) o;
			return l.link.equals(this.link);
		}
		return false;		
	}
	
	@Override
	public int hashCode(){
		return link.hashCode();		
	}

}
