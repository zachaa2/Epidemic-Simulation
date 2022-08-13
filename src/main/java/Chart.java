package main.java;

import java.util.ArrayList;

import javax.swing.JPanel;
import java.awt.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author Aaron Zachariah
 * Chart class using the JFreeChart library - see: https://www.jfree.org/jfreechart/
 * This is a wrapper class for a JPanel, which will hold the components that display the line chart
 * @see <a href="https://www.jfree.org/jfreechart/">JFreeChart</a>
 */
public class Chart {
    
    /**
     * Reference to JPanel object from GUI
     */
    JPanel panel = null;

    /**
     * Chart constructor
     * @param p - JPanel reference from the GUI class
     */
    public Chart(JPanel p){
        
        this.panel = p;

    }


    /**
     * Method to reset panel components, and repaint the new chart onto the panel
     * See Documentation for createDataset() for extra info on the parameters
     * @param susceptible data for susceptible nodes
     * @param infected data for infected nodes
     * @param recovered data for recovered nodes
     * @param dead data for dead nodes
     */
    public void updatePanel(ArrayList<Integer> susceptible, ArrayList<Integer> infected, 
    ArrayList<Integer> recovered, ArrayList<Integer> dead ){
        // clear panel contents
        this.panel.removeAll();
        this.panel.setLayout(new BorderLayout());

        // get new chart panel and add to parent panel
        JPanel chartpanel = createChartPanel(susceptible, infected, recovered, dead);
        this.panel.add(chartpanel, BorderLayout.CENTER);
        this.panel.revalidate();
        this.panel.repaint();
        
    }   

    /**
     * Method to create the JPanel with the new chart data
     * See Documentation for createDataset() for extra info on the parameters
     * @param susceptible data for susceptible nodes
     * @param infected data for infected nodes
     * @param recovered data for recovered nodes
     * @param dead data for dead nodes
     * @return returns the JPanel which contains the chart
     */
    public JPanel createChartPanel(ArrayList<Integer> susceptible, ArrayList<Integer> infected, 
    ArrayList<Integer> recovered, ArrayList<Integer> dead ){

        String title = "Simulation Data";
        String xLabel = "Tick";
        String yLabel = "# of Nodes";

        XYDataset data = createDataset(susceptible, infected, recovered, dead);

        JFreeChart chart = ChartFactory.createXYLineChart(title, xLabel, yLabel, data);
        
        return new ChartPanel(chart);

    }


    /**
     * Method to create a dataset which is plotted on a JFreeChart
     * Uses four ArrayList containers, which hold the data to be plotted
     * Data is stored as such: each index i is a tick (x pos)
     * and ArrayList[i] is the data (y-pos) at that tick 
     * @param susceptible data for susceptible nodes
     * @param infected data for infected nodes
     * @param recovered data for recovered nodes
     * @param dead data for dead nodes
     * @return XYDataset of a collection of series of xy data points as pairs (x, y)
     */
    public XYDataset createDataset(ArrayList<Integer> susceptible, ArrayList<Integer> infected, 
    ArrayList<Integer> recovered, ArrayList<Integer> dead )
    {   // dataset
        XYSeriesCollection dataset = new XYSeriesCollection();
        // series for the dataset
        XYSeries series1 = new XYSeries("Susceptible");
        XYSeries series2 = new XYSeries("Infected");
        XYSeries series3 = new XYSeries("Recovered");
        XYSeries series4 = new XYSeries("Dead");
        // add data to series
        for(int i = 0;i < susceptible.size(); i++){
            series1.add(i, susceptible.get(i));
        }
        for(int i = 0;i < infected.size(); i++){
            series2.add(i, infected.get(i));
        }
        for(int i = 0;i < recovered.size(); i++){
            series3.add(i, recovered.get(i));
        }
        for(int i = 0;i < dead.size(); i++){
            series4.add(i, dead.get(i));
        }

        // add series to dataset
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
        dataset.addSeries(series4); 

        return dataset;
        
    }

    


}
