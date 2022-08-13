package main.java;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Aaron Zachariah
 * Class which extends the Thread class and functionality. This class is responsible
 * for updating the graph model using mutual exclusion. Each object represents a thread which
 * processes and edits parts of the Graph.
 */
public class SimThread extends Thread {

    /**
     * Graph object used for the simulation
     */
    public Graph graph;

    /**
     * Starting index to sequentially process nodes 
     */
    public int starting_index;

    /**
     * Ending index to sequentially process nodes
     */
    public int ending_index;

    /**
     * target force of infection rate
     */
    public double lambda;

    /**
     * Container to hold node names and their updated states
     */
    public HashMap<String, Node> updatedNodes = new HashMap<String, Node>();

    /**
     * Lock for mutual exclusion
     */
    public ReentrantLock myLock;

    /**
     * SimThread contructor 
     * @param graph Graph model object
     * @param start staring index in the list of infected nodes
     * @param end ending index in the list of nodes
     * @param l lock used to avoid race conditions
     */
    public SimThread(Graph graph, int start, int end, ReentrantLock l){

        this.graph = graph;
        this.starting_index = start;
        this.lambda = graph.lambda;
        this.ending_index = end;
        this.myLock = l;
    }

    /**
     * Function to sequentially infect adjacent susceptible nodes and add them to the 
     * collection of Nodes to update at the end of the thread
     */
    public void infectNodes(){
        
        // get reference to list of infected nodes
        ArrayList<Node> infected = graph.infectedNodes;

        // generate arraylist of 
        int node = 1;
        int infect = 0;
        int total_infect = 0;
        double foi = 0.0;

        double diff = Math.abs(foi - lambda);
        double prev_diff = Math.abs(foi - lambda);

        ArrayList<Integer> infect_count = new ArrayList<Integer>();

        for(int i = starting_index; i < ending_index; i++){
            
            foi = (double) total_infect / node;
            diff = Math.abs(foi - lambda);
            prev_diff = Math.abs(foi - lambda);

            while(true){
                
                foi = (double) total_infect / node;
                prev_diff = diff;
                diff = Math.abs(foi - lambda);

                if(diff > prev_diff){
                    infect--;
                    total_infect--;
                    break;
                } else {
                    infect++;
                    total_infect++;
                }

            }
            //check the total number of susceptible neighbors
            ArrayList<Edge> neighbors = graph.g.get(infected.get(i));
            int ctr = 0;
            for(Edge e : neighbors){
                String name = e.getDest().name;
                for(Node n : graph.nodes){
                    if(n.name.equals(name)){
                        if(n.status.equals("s")){
                            ctr++;
                        }
                        break;
                    }
                }
            }

            if(infect > ctr){
                total_infect -= Math.abs((infect - ctr));
                infect = ctr;
            }
            
            // add infect count to list and reset
            infect_count.add(infect);
            infect = 0;
            // increment node and reset diff values
            node++;

        }
        
        // use the infect_count array to infect neighboring nodes
        int ctr = 0; Random rand = new Random();
        for(int i = starting_index;i < ending_index;i++){
            
            // get neighbors
            ArrayList<Edge> neighbors = graph.g.get(infected.get(i));
            // get number of nodes to infect
            int to_infect = infect_count.get(ctr);
            
            // randomly infect nodes
            int num_infected = 0;
            int failures = 0;
            while(num_infected < to_infect){
                
                int index = rand.nextInt(neighbors.size());
                // get node from list
			    Edge e = neighbors.get(index);
                String name = e.getDest().name;
                // search for node and add to map if possible
                for(Node n : graph.g.keySet()){
                    if(n.name.equals(name)){
                        if(n.status.equals("s")){
                            Node new_n = new Node(n.name);
                            new_n.status = "i";
                            new_n.infection_time = 0;
                            updatedNodes.put(n.name, new_n);

                            num_infected++;
                        } 
                        // failed to infect a node!
                        else {  
                            failures++;
                        }
                        break;
                    }
                }

                if(failures > 5){
                    break;
                }

            }

            ctr++;
        }





    }

    /**
     * Function to increment the infection time of all infected nodes, possibly change states to recovered or dead
     */
    public void incrementInfected(){

        
        Random r = new Random();
        
        // go thru all currently infected nodes
        // get reference to list of infected nodes
        ArrayList<Node> infected = graph.infectedNodes;

        for(int i = starting_index; i < ending_index; i++){

            int next_time = infected.get(i).infection_time + 1;
            // infection has run its course 
            if(next_time >= graph.t){

                double val = r.nextDouble();
                // node recovers
                if(val <= graph.d){
                    myLock.lock();
                    infected.get(i).status = "r";
                    graph.infected--;
                    graph.recovered++;
                    myLock.unlock();
                } 
                // node dies
                else {  
                    
                    myLock.lock();
                    infected.get(i).status = "d";
                    graph.infected --;
                    graph.dead++;
                    myLock.unlock();
                
                }

            } 
            
            // infection is not done, increment time
            else {

                myLock.lock();
                infected.get(i).infection_time++;
                myLock.unlock();
                
            }

        }   

    }

    /**
     * Function to pass thru the map of updated nodes and update each node
     */
    public synchronized void updateNodes(){

        for(String name : updatedNodes.keySet()){

            //find the node in the list of nodes
            for(Node node : graph.nodes){

                if(node.name.equals(name) && node.status.equals("s")){

                    // update the graph node with the new state
                    Node new_node = updatedNodes.get(name);
                    node.status = "i";
                    node.infection_time = new_node.infection_time;
                    graph.infectedNodes.add(node);
                    graph.infected++;
                    graph.susceptible--;
                    break;
                }

            }

        }

    }

    /**
     * Overriden method from the Thread class.
     * Responsible for processing infected nodes, and simulating the infection spread 
     */
    @Override
    public void run(){

        infectNodes();
        incrementInfected();
        updateNodes();

    }

}
