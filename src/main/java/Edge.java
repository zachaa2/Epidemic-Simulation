package main.java;

/**
 * 
 * @author Aaron Zachariah
 * Edge class to represent directed edges between nodes in a Graph
 * Edge labels are not used but exist to support potential edge labels for graphs 
 * 
 */

public class Edge implements Comparable<Edge> {
	
	/**
	 * Destination node
	 */
	private final Node dest;
	
	/**
	 * Label of the edge
	 */
	private final String label;
	
	/**
	 * Edge constructor
	 * @param d reference to destination node
	 * @param l label for the edge
	 */
	public Edge(Node d, String l){
		this.dest = d;
		this.label = l;
	}
		
	/**
	 * Getter for a reference to the destionation node
	 * @return destionation node
	 */
	public Node getDest() {
		return this.dest;
	}
	
	/**
	 * Getter for the edge's label
	 * @return edge label as a string
	 */
	public String getLabel() {
		return this.label;
	}
	
	/**
	 * Utility method to print an edge
	 */
	public void print() {
		System.out.print("(");
		dest.print();
		System.out.print(", " + label + ")");
	}
	
	
	@Override
	public int hashCode() {
		return dest.hashCode() + label.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Edge)) {
			return false;
		}
		Edge e = (Edge) o;
		return (this.dest.equals(e.dest) && this.label.equals(e.label));
	}
	

	@Override
	public int compareTo(Edge o) {
		int result1 = (this.getDest()).compareTo(o.getDest());
		if(result1 != 0) {
			return result1;
		}
		
		return this.getLabel().compareTo(o.getLabel());
		
	}
	
	
}
