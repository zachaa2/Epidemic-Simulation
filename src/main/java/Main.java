package main.java;

/**
 * 
 * @author Aaron Zachariah
 * Main class for the Simulation application. This main method is invoked when
 * the user compiles and runs the application. The main will show the gui to the user
 * where the user will be able to configure and start the simulation.
 * 
 */
public class Main {
    
    /**
     * Main method invoked upon running the application
     * @param args command line args (unused)
     */
    public static void main(String[] args){

        Graph g = new Graph();
        
        // try {
        //     g.buildGraph("input.txt");
        // } catch (IOException e) {
        //     System.err.println("IO error when building graph");
        // }
        
        GUI gui = new GUI("Simulation", g);
        gui.showGUI();

    }
    

}