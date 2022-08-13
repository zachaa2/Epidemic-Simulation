package main.java;

/**
 * 
 * @author Aaron Zachariah
 * Simple Node class, which represents a node in a graph. Each node has a unique name, 
 * a certain status and a running counter of the time the node has spent infected.
 * 
 */
public class Node implements Comparable<Node> {
    
    /**
     * Unique name which is mutable
     */
    public final String name;

    /**
     * Status of the node. A node can be in four states:
     * s - susceptible
     * i - infected
     * r - recovered
     * d - dead
     */
    public String status = "s";

    /**
     * Running counter of time (in ticks) spent infected
     */
    public int infection_time = 0;

    /**
     * Node constructor
     * @param name unique name given to the node
     */
    public Node(String name){
        this.name = name;
    }


    @Override
    public int hashCode(){

        return name.hashCode();

    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Node)){
            return false;
        }
        
        Node n = (Node) o;
        return (this.name.equals( n.name ) );

    }

    @Override
    public int compareTo(Node n){
        int r1 = this.name.compareTo(n.name);
        if(r1 != 0){
            return r1;
        }
        int r2 = this.status.compareTo(n.status);
        if(r2 != 0){
            return r2;
        }

        Integer v1 = this.infection_time;
        Integer v2 = n.infection_time;
        return v1.compareTo(v2);
    }

    /**
     * Print method to output the node's name to the terminal
     */
    public void print(){
        System.out.print(name);
    }

}
