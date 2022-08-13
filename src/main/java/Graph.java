package main.java;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;



/**
 * @author Aaron Zachariah
 * Graph class to represent a graph in the form of an adjacency list
 * Each node is a key. The value it is paired with is a list of all
 * reachable nodes from the key node
 * 
 * The graph has edges that are directed and labeled. An undirected version of the graph will simply have
 * a two Edge objects going in opposite directions, which would indicate that direction does not matter
 * 
 * Each node has 4 states:
 *  suscepible, infected, recovered or dead
 * 
 * Each node follows a specific naming convention. The node name is followed by a letter which indicated the nodes status
 * s - suscepible
 * i -infected
 * r - recovered
 * d - dead
 * 
 * Example: Adding the node "1" to the graph will have its name initialized as "1s", 
 * since it will start off as a susceptable node
 * 
 */

public class Graph {
	
	/**
	 * Graph represented as a hashmap of key-list pairs 
	 */
	public HashMap<Node, ArrayList<Edge> > g;
	
	/**
	 * Container to hold all the nodes in the graph
	 */
	public ArrayList<Node> nodes;

	/**
	 * Container to hold all infected nodes
	 */
	public ArrayList<Node> infectedNodes = new ArrayList<Node>();



	/**
	 * Counters for simulation statistics and calculations
	 */
	public int susceptible = 0;
	public int infected = 0;
	public int recovered = 0;
	public int dead = 0;

	/**
	 * number of random nodes to be infected
	 */
	public int n_infected = 3;
	
	/**
	 * Nodes with a degree greater than this value are infected
	 */
	public int s_infected = 5;

	/**
	 * k nodes will be infected starting from a random node, and spreading via BFS
	 */
	public int k_infected = 3;

	/**
	 * recovery rate of infection
	 */
	public double d = 0.5;

	/**
	 * Length of infection in time unit t
	 */
	public int t = 5;

	/**
	 * Target force of infection parameter
	 */
	public double lambda = 1.25;

	/**
	 * Counter for discrete unit of time
	 */
	public int tick = 0;

	/**
	 * Number fo threads to use when running the simulation
	 */
	public int num_threads = 1;


	/**
	 * Graph constructor which initializes the datastructure
	 */
	public Graph(){
		
		this.g = new HashMap<Node, ArrayList<Edge> >();
		this.nodes = new ArrayList<Node>();
		this.infectedNodes = new ArrayList<Node>();
		
	}
	
	/**
	 * size getter
	 * @return number of nodes in the graph
	 */
	public int size() {
		return this.g.size();
	}
	
	/**
	 * susceptible getter
	 * @return number of susceptible nodes
	 */
	public int getSusceptible(){
		return this.susceptible;
	}

	/**
	 * infected getter
	 * @return nuber of infected nodes
	 */
	public int getInfected(){
		return this.infected;
	}

	/**
	 * infected getter
	 * @return the nuber of infected nodes
	 */
	public int getRecovered(){
		return this.recovered;
	}

	/**
	 * dead getter
	 * @return the number of dead nodes
	 */
	public int getDead(){
		return this.dead;
	}

	/**
	 * getter for all nodes in the graph
	 * @return a list of nodes represented as strings
	 */
	public ArrayList<Node> getNodes(){
		// return node data as arraylist
		return new ArrayList<Node>(g.keySet());
	}
	

	
	/**
	 * getter for all adjacent nodes
	 * @param node the node whose neighbors are needed
	 * @return all neighboring nodes as a list
	 */
	public ArrayList<Edge> getChildren(Node node){
		if(!g.containsKey(node)) {
			return new ArrayList<Edge>();
		}
		
		ArrayList<Edge> children = new ArrayList<Edge>();
		ArrayList<Edge> t = g.get(node);
		
		//deep copy list of children and return the list
		for(Edge e : t) {
			Edge new_edge = new Edge(e.getDest(), e.getLabel());
			children.add(new_edge);
		}
		
		// sort children and return 
		Collections.sort(children);
		
		return children;
		
	}
	

	/**
	 * get the degree of a node
	 * @param nodeData node name to analyze the degree
	 * @return the degree as an integer
	 */
	public int getChildrenCount(Node nodeData) {
		// return 0 if the node DNE
		if(!g.containsKey(nodeData)) {
			return 0;
		}
		
		for(Map.Entry<Node, ArrayList<Edge> >  entry : g.entrySet()) {
			if(entry.getKey().equals(nodeData)) {
				return entry.getValue().size();
			}
		}
		
		return 0;
		
	}
	
	/**
	 * Adds an empty node to the graph 
	 * @param nodeData name of the node to add
	 */
	public void addNode(Node nodeData) {
		
		// if the node is already present, do nothing
		if(g.containsKey(nodeData)) {
			return;
		} else {
			g.put(nodeData, new ArrayList<Edge >());
			nodes.add(nodeData);
			susceptible++;
		}

		return;
	}
	
	/**
	 * adds an edge to the graph. If either node does not yet exist, it will be added to the graph 
	 * @param parentNode the node from which the edge starts
	 * @param childNode the node where the edge reaches
	 * @param edgeLabel the edge label or status of the destination node 
	 */
	public void addEdge(Node parentNode, Node childNode, String edgeLabel) {
		
		// if parent DNE, add it
		if(!(g.containsKey(parentNode))) {

			g.put(parentNode, new ArrayList<Edge>());
			nodes.add(parentNode);
			susceptible++;
		}

		// if child DNE, add it
		if(!(g.containsKey(childNode))){

			g.put(childNode, new ArrayList<Edge>());
			nodes.add(childNode);
			susceptible++;
		}

		// create new Edge object
		Edge e = new Edge(childNode, edgeLabel);
		
		//find get parent node and add edge
		g.get(parentNode).add(e);
		
	}

	/**
	 * BFS implementation for the graph that represents the simulations. Will traverse until k nodes
	 * are visited or the whole graph is. Will mark nodes as infected alnog the way
	 * @param node1 the starting node
	 * @param k number of nodes to traverse 
	 */
	public void BFS(Node node1, int k) {
		
		// INITIALIZE BFS
		Node start = node1;
		int ctr = 0;
		
		
		// queue will be a linkedlist
		LinkedList<Node> queue = new LinkedList<Node>();
		// visited "array" will be a map from visited nodes to paths
		HashMap<Node, ArrayList<Edge> > visited = new HashMap<Node, ArrayList<Edge> >();
		
		// queue start and add to the map

		queue.addLast(start);
		visited.put(start, new ArrayList<Edge>());
		
		//while loop for BFS
		while(queue.size() > 0) {
			if(ctr == k){
				break;
			}
			
			// dequeue next node 
			// mark as infected
			Node next_node = queue.removeFirst();
			
			for(Node n : g.keySet()){
				if(n.name.equals(next_node.name)){
					infectedNodes.add(n);
					n.status = "i";
					susceptible--;
					infected++;
				}
			}

			// get all edges e = <n,m>
			ArrayList<Edge> edges = this.getChildren(next_node); // this should be sorted
			for (Edge e : edges) {
				if(!visited.containsKey(e.getDest())) { // if dest node is unvisited
					//get path p
					ArrayList<Edge> p = visited.get(next_node);
					//build p' (new path)
					ArrayList<Edge> new_path = new ArrayList<Edge>();
					for(Edge edge_path : p) {
						new_path.add(new Edge(edge_path.getDest(), edge_path.getLabel()));
					}
					new_path.add(e); //add new edge to new_path
					// add to map and queue the new node
					visited.put(e.getDest(), new_path);
					queue.addLast(e.getDest());
				}
			}

			ctr++;
			
		}
		
	}

	/**
	 * Function to randomly infect up to n_infected nodes
	 */
	public void infectRandom(){
		Random rand = new Random();
		int ctr = 0;
		while(ctr < this.n_infected){

			int index = rand.nextInt(nodes.size());
			// get node from list
			Node n = nodes.get(index);
			// find node in graph
			for(Node node : g.keySet()){
				// if we find the node and it is uninfected, infect it
				if(node.name.equals(n.name) && node.status.equals("s") ){
					node.status = "i";
					n.status = "i";
					infectedNodes.add(n);
					ctr ++;
					susceptible--;
					infected++;
				}
				// in any other case, do nothing
			}
		}

	}

	/**
	 * Any node with degree greater than s_infected will be infected
	 */
	public void infectDegree(){

		for(Node node : g.keySet()){
			if(g.get(node).size() > s_infected){
				node.status = "i";
				infectedNodes.add(node);
				susceptible--;
				infected++;
			}
		}

	}

	/**
	 * Conduct a BFS of up to k nodes from a random starting node and mark all as infected
	 */
	public void infectBFS(){
		
		// get random starting position
		Random rand = new Random();
		int index = rand.nextInt(nodes.size());
		Node start = nodes.get(index);

		// run bfs
		this.BFS(start, k_infected);

	}

	/**
	 * Method to create multiple threads and update the graph to the state of the 
	 * next tick
	 */
	public void nextTick(){

		SimThread[] threadlist = new SimThread[num_threads];
		ReentrantLock lock = new ReentrantLock();

		// initialize all thread objects
		for(int i = 0; i < num_threads - 1; i++){

			int start = (infectedNodes.size() / num_threads) * i;
			int end = (infectedNodes.size() / num_threads) * (i+1);
			SimThread t = new SimThread(this, start, end, lock);
			threadlist[i] = t;

		}

		int last_start = (infectedNodes.size() / num_threads) * (num_threads-1);
		int last_end = infectedNodes.size();
		SimThread tlast = new SimThread(this, last_start, last_end, lock);
		threadlist[num_threads-1] = tlast;

		// start all threads
		for(int i = 0;i < num_threads; i++){

			threadlist[i].start();

		}

		// join threads 
		for(int i = 0;i < num_threads; i++){

			try{
				threadlist[i].join();
			} catch (InterruptedException e){
				System.err.println("ERROR: THREAD " + i + " INTERRUPTED!");
			}
			
		}
		// remove all nulls
		ListIterator<Node> iter = infectedNodes.listIterator();
		while(iter.hasNext()){
			if(iter.next() == null){
				iter.remove();
			}
		}

		// remove nodes that are no longer infected
		for(int i = infectedNodes.size() - 1; i >= 0; i--){
			
			if(! ((infectedNodes.get(i).status).equals("i")) ) {
				infectedNodes.remove(i);
			}
			
		}

		tick++;
		
	}



	/**
	 * Method to build the grid from a given input text file, which is stored as an adjacency list
	 * @param path the path of the text file to read from 
	 * @throws IOException exception thrown on IO error when reading/closing the file
	 */
	public void buildGraph(String path) throws IOException {

		BufferedReader inFile = null;
		try{

			inFile = new BufferedReader(new FileReader(path));
			String line;
			while( (line = inFile.readLine()) != null ) {
				// split a line based on some valid separators 
				String[] data = line.split("[ ,;|]+");
				// first element is the node, and subsequent elements are adjacent nodes
				for(int i = 1; i < data.length; i++){
					this.addEdge(new Node(data[0]), new Node(data[i]), "");
				}

			}

		} finally{
			if (inFile != null){
				inFile.close();
			}
		}

	}
	
	/**
	 * Print method to output the graph to the terminal as an adjacency list
	 */
	public void print() {
		for(Entry<Node, ArrayList<Edge> > entry : g.entrySet()) {
			entry.getKey().print();
			System.out.print("=[");
			for (Edge e : entry.getValue()) {
				e.print();
				
			}
			System.out.print("]\n");
		}
	}
	
	/**
	 * Utility method to print just the nodes
	 */
	public void printNodes(){

		for(Node n : this.nodes){
			n.print();
			System.out.print("-" + n.status);
			System.out.print(" ");
		}
		System.out.println();

	}

	/**
	 * Utility method to print just the infected nodes
	 */
	public void printInfected(){

		for(Node n : infectedNodes){

			n.print();
			System.out.print("-" + n.status);
			System.out.print(" ");
			
		}
		System.out.println();
	}


}
