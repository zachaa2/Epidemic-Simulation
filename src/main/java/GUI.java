package main.java;

import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Aaron Zachariah
 * 
 * GUI class which acts as the View component of the application. Responsible for
 * allowing the user to interface with the model via buttons and input.
 * Will show useful statistics regarding the simulation as well as a useful chart plotting
 * the data
 * 
 */
public class GUI extends JFrame {
    
    /**
     * Graph object which holds the simulation information  
     */
    Graph graph;

    /**
     * Main panel made of 4 subpanels
     */
    JPanel parentPanel = new JPanel(new BorderLayout());

    /**
     * 4 sub panels added in a BorderLayout on the main panel
     * Both right and left panels will be broken into more subpanels which hold the board information
     */
    JPanel headerPanel = new JPanel();
    JPanel footerPanel = new JPanel();
    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    
    /**
     * Chart components
     */
    JPanel centerPanel = new JPanel(new BorderLayout());
    Chart chart;

    /**
     * JLabels for statistics
     */
    JLabel susc_count = new JLabel();
    JLabel inf_count = new JLabel();
    JLabel rec_count = new JLabel();
    JLabel dead_count = new JLabel();
    JLabel tick_count = new JLabel();

    /**
     * Various JLabels for application information
     */
    JLabel title;
    JLabel righttitle;
    JLabel lefttitle;
    JLabel ninfo;
    JLabel sinfo;
    JLabel kinfo;
    JLabel rateinfo;
    JLabel linfo;
    JLabel foi;
    JLabel tc;
    
    /**
     * Containers to hold the data for each statistic
     */
    ArrayList<Integer> susceptible = new ArrayList<Integer>();
    ArrayList<Integer> infected = new ArrayList<Integer>();
    ArrayList<Integer> recovered = new ArrayList<Integer>();
    ArrayList<Integer> dead = new ArrayList<Integer>();

    /**
     * GUI contructor
     * @param name name of JFrame
     * @param g Graph object which is the model for the simulation
     */
    public GUI(String name, Graph g){

        super(name);
        this.graph = g;
        this.chart = new Chart(centerPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new Dimension(1200, 800));
        setTitle("Simulator");

        createPanels();
        createMenuBar();
        createText();
        createToolbar();
        
    }

    /**
     * Method to set the background color of the application
     * @param c Color to set the backgroud to
     */
    public void setColor(Color c){
        
        this.getContentPane().setBackground(c);
        
        parentPanel.setBackground(c);

        headerPanel.setBackground(c);
        footerPanel.setBackground(c);
        leftPanel.setBackground(c);
        rightPanel.setBackground(c);

        susc_count.setBackground(c);
        inf_count.setBackground(c);
        rec_count.setBackground(c);
        dead_count.setBackground(c);

        title.setBackground(c);
        righttitle.setBackground(c);
        lefttitle.setBackground(c);

        ninfo.setBackground(c);
        sinfo.setBackground(c);
        kinfo.setBackground(c);
        
        rateinfo.setBackground(c);
        linfo.setBackground(c);
        tc.setBackground(c);
        foi.setBackground(c);

    }

    
    /**
     * Method to initialize all the panels that make up the JFrame window
     */
    public void createPanels(){

        // add top level panel to frame
        this.add(parentPanel);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.PAGE_AXIS));

        // set sizes
        headerPanel.setPreferredSize(new Dimension(1200, 100));
        footerPanel.setPreferredSize(new Dimension(1200, 100));
        leftPanel.setPreferredSize(new Dimension(300, 300));
        rightPanel.setPreferredSize(new Dimension(300, 300));
        centerPanel.setPreferredSize(new Dimension(600, 300));
        

        parentPanel.add(headerPanel, BorderLayout.NORTH);
        parentPanel.add(footerPanel, BorderLayout.SOUTH);
        parentPanel.add(leftPanel, BorderLayout.WEST);
        parentPanel.add(rightPanel, BorderLayout.EAST);
        parentPanel.add(centerPanel, BorderLayout.CENTER);
        
    }

    /**
     * Function to initialize the menu bar and its components
     */
    public void createMenuBar(){

        JMenuBar menuBar = new JMenuBar();

        // -------- File JMenu -------- //
        JMenu file = new JMenu("File");
        // open
        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0){
                JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int r = j.showOpenDialog(null);
                if(r == JFileChooser.APPROVE_OPTION){
                    
                    String path = j.getSelectedFile().getAbsolutePath();
                    // build the graph if the simulation has yet to
                    if(graph.tick == 0 && graph.size() == 0){
                        try {
                            graph.buildGraph(path);
                        } catch (IOException e){
                            JOptionPane.showMessageDialog(null, "IO Error while building graph!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        refreshStats();
                    }
                }
            }
        });
        file.add(open);


        // ------- View Menu -------- //
        JMenu view = new JMenu("View");
        // set backgroud color
        JMenuItem backgroud = new JMenuItem("Set Background Color");
        backgroud.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Color c = JColorChooser.showDialog(null, "Please Select a Color", Color.WHITE);
                setColor(c);
            }
        });
        view.add(backgroud);


        // -------- Configure Menu -------- //
        JMenu config = new JMenu("Configure");
        // set thread count
        JMenuItem SetThreadCount = new JMenuItem("Set Thread Count");
        SetThreadCount.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent evt){
                if(graph.tick == 0){
                    String input = JOptionPane.showInputDialog(null, "Enter the Thread Count");
                    int n;
                    try{
                        n = Integer.parseInt(input);
                    } catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(null, "Thread Count must be an integer!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(n < 1){
                        JOptionPane.showMessageDialog(null, "Thread Count must be > 0", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        graph.num_threads = n;
                    }

                }
            }
        });
        config.add(SetThreadCount);
        // set n
        JMenuItem n = new JMenuItem("Set n");
        n.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e){
                if(graph.tick != 0) return;
                // get input
                String input = JOptionPane.showInputDialog(null, "Enter the n parameter");
                int n;
                try{
                    n = Integer.parseInt(input);
                } catch (NumberFormatException nfe){
                    JOptionPane.showMessageDialog(null, "n must be an integer!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // validate value
                if(n < 1 || n > graph.size()){
                    JOptionPane.showMessageDialog(null, "Invalid n value!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    graph.n_infected = n;
                }
            }
        });
        config.add(n);
        // set s
        JMenuItem s = new JMenuItem("Set s");
        s.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent evt){
                if(graph.tick != 0) return;
                // get input
                String input = JOptionPane.showInputDialog(null, "Enter the s parameter");
                int s;
                try{
                    s = Integer.parseInt(input);
                } catch (NumberFormatException nfe){
                    JOptionPane.showMessageDialog(null, "s must be an integer!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // validate value
                if(s < 0){
                    JOptionPane.showMessageDialog(null, "Invalid s value!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    graph.s_infected = s;
                }
            }
        });
        config.add(s);
        // set k
        JMenuItem k = new JMenuItem("Set k");
        k.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent arg0){
                if(graph.tick != 0) return;
                // get input
                String input = JOptionPane.showInputDialog(null, "Enter the s parameter");
                int k;
                try{
                    k = Integer.parseInt(input);
                } catch (NumberFormatException nfe){
                    JOptionPane.showMessageDialog(null, "k must be an integer!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // validate input
                if(k < 1 || k > graph.size()){
                    JOptionPane.showMessageDialog(null, "Invalid k value!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    graph.k_infected = k;
                }
            }
        });
        config.add(k);
        // set recovery rate
        JMenuItem d = new JMenuItem("Set Recovery Rate");
        d.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg1){
                if(graph.tick != 0) return;
                // get input
                String input = JOptionPane.showInputDialog(null, "Enter the recovery rate");
                double d;
                try{
                    d = Double.parseDouble(input);
                } catch (NumberFormatException nfe){
                    JOptionPane.showMessageDialog(null, "The rate must be a decimal value!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(d < 0.0 || d > 1.0){
                    JOptionPane.showMessageDialog(null, "Invalid rate value!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    graph.d = d;
                }
            }
        });
        config.add(d);
        // set infection length
        JMenuItem time = new JMenuItem("Set period of infection");
        time.addActionListener(new ActionListener(){
           
            public void actionPerformed(ActionEvent evt){
                if(graph.tick != 0) return;
                // get input
                String input = JOptionPane.showInputDialog(null, "Enter the recovery rate");
                int length;
                try{
                    length = Integer.parseInt(input);
                } catch (NumberFormatException nfe){
                    JOptionPane.showMessageDialog(null, "The period must be an Integer!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(length < 1){
                    JOptionPane.showMessageDialog(null, "Invalid period!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    graph.t = length;
                }
            }
        });
        config.add(time);
        // set force of infection
        JMenuItem foi = new JMenuItem("Set force of infection");
        foi.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent ev){
                if(graph.tick != 0) return;
                // get input
                String input = JOptionPane.showInputDialog(null, "Enter the recovery rate");
                double lam;
                try{
                    lam = Double.parseDouble(input);
                } catch (NumberFormatException nfe){
                    JOptionPane.showMessageDialog(null, "The FOI must be an decimal value!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(lam <= 0.0){
                    JOptionPane.showMessageDialog(null, "Invalid FOI!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    graph.lambda = lam;
                }
            }
        });
        config.add(foi);
        

        menuBar.add(file);
        menuBar.add(view);
        menuBar.add(config);
        menuBar.revalidate();
        this.setJMenuBar(menuBar);

    }

    /**
     * Function to refresh all the statistics fields with new 
     * data regarding the state of the Model
     */
    public void refreshStats(){

        susc_count.setText(String.format("Susceptible Nodes: %d", graph.susceptible));
        inf_count.setText(String.format("Infected Nodes: %d", graph.infected));
        rec_count.setText(String.format("Recovered Nodes: %d", graph.recovered));
        dead_count.setText(String.format("Dead Nodes: %d", graph.dead));
        tick_count.setText(String.format("Current Tick: %d", graph.tick));

    }

    /**
     * Function to initiaize all text fields used in the application
     */
    public void createText(){
        // ---------- HEADER ---------- //
        // title 
        title = new JLabel("Simulation");
        title.setFont(new Font("Ariel Black", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(title);
        // ---------- RIGHT PANEL ---------- //
        // info section (right panel)
        righttitle = new JLabel("CONFIGURATION INFO");
        righttitle.setFont(new Font("Ariel Black", Font.BOLD, 16));
        righttitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(righttitle);

        String info;
        String labelText;
        // n parameter
        info = "n - Randomly Infecting Nodes will infect n nodes in the graph";
        labelText = String.format("<html><div style=\"width:%dpx;\">%s</div></html>", 200, info);
        ninfo = new JLabel(labelText);
        ninfo.setFont(new Font("Ariel Black", Font.BOLD, 12));
        ninfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(ninfo);

        // s parameter
        info = "s - All nodes with degree greater than s will be infected";
        labelText = String.format("<html><div style=\"width:%dpx;\">%s</div></html>", 200, info);
        sinfo = new JLabel(labelText);
        sinfo.setFont(new Font("Ariel Black", Font.BOLD, 12));
        sinfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(sinfo);

        // k parameter
        info = "k - BFS traversal will start from a random node and infect up to k nodes";
        labelText = String.format("<html><div style=\"width:%dpx;\">%s</div></html>", 200, info);
        kinfo = new JLabel(labelText);
        kinfo.setFont(new Font("Ariel Black", Font.BOLD, 12));
        kinfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(kinfo);

        // recovery rate
        info = "Recovery Rate - infected nodes will recover with recovery rate probability";
        labelText = String.format("<html><div style=\"width:%dpx;\">%s</div></html>", 200, info);
        rateinfo = new JLabel(labelText);
        rateinfo.setFont(new Font("Ariel Black", Font.BOLD, 12));
        rateinfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(rateinfo);

        // infection length
        info = "Infection Length - the # of ticks an infected node will stay infected";
        labelText = String.format("<html><div style=\"width:%dpx;\">%s</div></html>", 200, info);
        linfo = new JLabel(labelText);
        linfo.setFont(new Font("Ariel Black", Font.BOLD, 12));
        linfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(linfo);

        // FOI
        info = "FOI - the rate which the infection spreads \nto other nodes";
        labelText = String.format("<html><div style=\"width:%dpx;\">%s</div></html>", 200, info);
        foi = new JLabel(labelText);
        foi.setFont(new Font("Ariel Black", Font.BOLD, 12));
        foi.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(foi);

        // Thread count
        info = "Thread Count - # of threads to run the simulation on";
        labelText = String.format("<html><div style=\"width:%dpx;\">%s</div></html>", 200, info);
        tc = new JLabel(labelText);
        tc.setFont(new Font("Ariel Black", Font.BOLD, 12));
        tc.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(tc);

        // ---------- LEFT PANEL ---------- // 
        lefttitle = new JLabel("STATISTICS");
        lefttitle.setFont(new Font("Ariel Black", Font.BOLD, 32));
        leftPanel.add(lefttitle);

        susc_count.setText(String.format("Susceptible Nodes: %d", graph.susceptible));
        inf_count.setText(String.format("Infected Nodes: %d", graph.infected));
        rec_count.setText(String.format("Recovered Nodes: %d", graph.recovered));
        dead_count.setText(String.format("Dead Nodes: %d", graph.dead));
        tick_count.setText(String.format("Current Tick: %d", graph.tick));
        
        susc_count.setFont(new Font("Ariel Black", Font.BOLD, 18));
        inf_count.setFont(new Font("Ariel Black", Font.BOLD, 18));
        rec_count.setFont(new Font("Ariel Black", Font.BOLD, 18));
        dead_count.setFont(new Font("Ariel Black", Font.BOLD, 18));
        tick_count.setFont(new Font("Ariel Black", Font.BOLD, 18));

        leftPanel.add(susc_count);
        leftPanel.add(inf_count);
        leftPanel.add(rec_count);
        leftPanel.add(dead_count);
        leftPanel.add(Box.createRigidArea(new Dimension(200, 0)));
        leftPanel.add(tick_count);

    }
    
    /**
     * Function to initialize the toolbar and all its components
     */
    public void createToolbar(){

        JToolBar bar = new JToolBar();

        // infect random button
        JButton random = new JButton("Random");
        random.addActionListener(new ActionListener(){
           
            public void actionPerformed(ActionEvent e2){
                if(graph.tick > 0 || graph.infected != 0 || graph.size() == 0) return;
                graph.infectRandom();
                //update data containers
                susceptible.add(graph.susceptible);
                infected.add(graph.infected);
                recovered.add(graph.recovered);
                dead.add(graph.dead);
                chart.updatePanel(susceptible, infected, recovered, dead);

                refreshStats();

            }

        });

        // infect based on degree button
        JButton degree = new JButton("Degree");
        degree.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e){
                if(graph.tick > 0 || graph.infected != 0 || graph.size() == 0) return;
                graph.infectDegree();
                //update data containers
                susceptible.add(graph.susceptible);
                infected.add(graph.infected);
                recovered.add(graph.recovered);
                dead.add(graph.dead);
                chart.updatePanel(susceptible, infected, recovered, dead);

                refreshStats();

            }

        });

        // infect using BFS
        JButton bfs = new JButton("BFS");
        bfs.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(graph.tick > 0 || graph.infected != 0 || graph.size() == 0) return;
                graph.infectBFS();
                // update data containers
                susceptible.add(graph.susceptible);
                infected.add(graph.infected);
                recovered.add(graph.recovered);
                dead.add(graph.dead);
                chart.updatePanel(susceptible, infected, recovered, dead);

                refreshStats();

            }
        });
        
        // next tick
        JButton next = new JButton("Next Tick");
        next.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionE){
                if(graph.size() == 0 || graph.infected == 0) return;
                graph.nextTick();
                // update data containers
                susceptible.add(graph.susceptible);
                infected.add(graph.infected);
                recovered.add(graph.recovered);
                dead.add(graph.dead);
                chart.updatePanel(susceptible, infected, recovered, dead);

                refreshStats();
                
            }
            
        });

        bar.add(random);
        bar.add(degree);
        bar.add(bfs);
        bar.add(next);
        bar.setFloatable(false);
        bar.setRollover(true);

        footerPanel.add(bar);

    }

    /**
     * function to make the View visible to the user
     */
    public void showGUI(){

        setVisible(true);

    }

    /**
     * Function to remove the View from the User's screen
     */
    public void hideGUI(){

        setVisible(false);
        // this.dispose();
    }


}
