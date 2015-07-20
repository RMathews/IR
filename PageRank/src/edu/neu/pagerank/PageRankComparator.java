package edu.neu.pagerank;

import java.util.Comparator;

public class PageRankComparator implements Comparator<Link>{

	@Override
	public int compare(Link link1, Link link2) {
		return link1.getNewPageRank().compareTo(link2.getNewPageRank());
	}

}
