package test.java;

import main.java.*;

public class SimulationTest {

    public void testNodeConstructor(){
        Node n = new Node("A");
        if(n.name.equals("A")){
            System.out.println("Node Constructor Test Passed");
        } else {
            System.out.println("Node Constructor Test Failed");
        }
    }

    public void testNodeEquals(){
        Node n1 = new Node("A");
        Node n2 = new Node("A");

        if(n1.equals(n2)){
            System.out.println("Node Equals Test Passed");
        } else {
            System.out.println("Node Equals Test Failed");
        }

    }
    
    public void testNodeNotEquals(){
        Node n1 = new Node("A");
        Node n2 = new Node("B");

        if(!n1.equals(n2)){
            System.out.println("Node Not Equals Test Passed");
        } else {
            System.out.println("Node Not Equals Test Failed");
        }

    }

    public void testNodeHashCode(){
        Node n1 = new Node("A");
        Node n2 = new Node("A");

        if(n1.hashCode() == n2.hashCode()){
            System.out.println("Node Hashcode Test Passed");
        } else {
            System.out.println("Node Hashcode Test Failed");
        }

    }

    public void testEdgeConstructor(){

        Edge e = new Edge(new Node("A"), "label");
        if(e.getDest().equals(new Node("A")) && e.getLabel().equals("label")){
            System.out.println("Edge Constructor Passed");
        } else {
            System.out.println("Edge Constructor Passed");
        }

    }

    public void testGetEdgeDest(){

        Edge e = new Edge(new Node("A"), "label");
        
        if(e.getDest().equals(new Node("A"))){
            System.out.println("Edge GetDest Passed");
        } else {
            System.out.println("Edge GetDest Passed");
        }

    }

    public void testGetEdgeLabel(){
        Edge e = new Edge(new Node("A"), "label");
        
        if(e.getLabel().equals("label")){
            System.out.println("Edge GetLabel Passed");
        } else {
            System.out.println("Edge GetLabel Passed");
        }

    }

    public void testEdgeHashCode(){

        Edge e = new Edge(new Node("A"), "label");
        Edge e2 = new Edge(new Node("A"), "label");

        if(e.hashCode() == e2.hashCode()){
            System.out.println("Edge Hashcode Passed");
        } else {
            System.out.println("Edge Hashcode Failed");
        }

    }

    public void testEdgeEquals(){

        Edge e = new Edge(new Node("A"), "label");
        Edge e2 = new Edge(new Node("A"), "label");

        if(e.equals(e2)){
            System.out.println("Edge Equals Passed");
        } else {
            System.out.println("Edge Equals Failed");
        }

    }

    public void testEdgeNotEquals(){

        Edge e = new Edge(new Node("A"), "label");
        Edge e2 = new Edge(new Node("B"), "label");

        if(!e.equals(e2)){
            System.out.println("Edge Not Equals Passed");
        } else {
            System.out.println("Edge Not Equals Failed");
        }

    }





    public static void main(String[] args){

        SimulationTest tester = new SimulationTest();
        
        System.out.println("\n-------- RUNNING TESTS --------\n");

        tester.testNodeEquals();
        tester.testNodeEquals();
        tester.testNodeNotEquals();
        tester.testNodeHashCode();
        tester.testEdgeConstructor();
        tester.testGetEdgeDest();
        tester.testGetEdgeLabel();
        tester.testEdgeHashCode();
        tester.testEdgeEquals();
        tester.testEdgeNotEquals();

        System.out.println("\n-------- FINISHED TESTS --------\n");

    }


}
