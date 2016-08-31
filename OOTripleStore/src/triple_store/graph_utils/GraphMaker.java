/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package triple_store.graph_utils;
import triple_store.simple_graph.SimpleGraph ;
import java.util.* ;
import java.io.FileWriter ;
import java.io.PrintWriter ;
import java.io.IOException ;
/**
 *  Utility class for creating .dot files for GraphViz
 *  Provides one method - drawGraph() - overloaded for a variety of types of data sets - Set of triples, Set of query bindings
 *  List of nodes and produces a GraphViz file for each.
 *
 *  All versions of the method are static to make this a utility class.
 *
 * @author Michael
 */
public class GraphMaker {
    //Takes a graph, a Set of triples and a filename and creates a .dot file for neato
    public static void drawGraph(SimpleGraph graph, Set<String> triples, String fileName) {
        //Write the file header
        try {
            FileWriter graphVizFile = new FileWriter(fileName, true) ;
            PrintWriter pw = new PrintWriter(graphVizFile) ;
            pw.println("overlap = \"scale\" ");
            graphVizFile.close();
            pw.close() ;
        } catch(IOException ioe) {
            System.out.println(ioe.getMessage()) ;
        }
        //for each element in a set of triples, write nodes and edges
        int count = 0 ;
        //Write an entry for each triple
        for (String triple: triples) {         
            String[] tripleTokens = triple.split(GraphUtil.delimiter) ;
            try {
                if (count < triples.size()) {
                    FileWriter graphVizFile = new FileWriter(fileName, true) ;
                    PrintWriter pw = new PrintWriter(graphVizFile) ;
                    pw.println("\"" + tripleTokens[0] + "\" -- \"" + tripleTokens[2] + "\" [label=\"" + tripleTokens[1] + "\"]");
                    graphVizFile.close();
                    pw.close() ;
                } else {
                    FileWriter graphVizFile = new FileWriter(fileName, true) ;
                    PrintWriter pw = new PrintWriter(graphVizFile) ;
                    pw.println(" }\n");
                    graphVizFile.close();
                    pw.close() ;
                }
            } catch(IOException ioe) {
                System.out.println(ioe.getMessage()) ;
            }
        count++ ;
        }
    }//end drawGraph(SimpleGraph, Set triples, fileName) version

    public static void drawGraph(SimpleGraph graph, List<String> nodes, String fileName) {
        //Create file
        //Write header
        try {
            FileWriter graphVizFile = new FileWriter(fileName, true) ;
            PrintWriter pw = new PrintWriter(graphVizFile) ;
            pw.println("overlap = \"scale\" ");
            graphVizFile.close();
            pw.close() ;
        } catch(IOException ioe) {
            System.out.println(ioe.getMessage()) ;
        }
        //For each node in nodes
        int count = 0 ;
        String previousNode = "" ;
        String newLine = "" ;
        for (String node: nodes) {
        String line = "" ;
            try {
                FileWriter graphVizFile = new FileWriter(fileName, true) ;
                PrintWriter pw = new PrintWriter(graphVizFile) ;

                if (!node.equals("None")) {
                    count++ ;

                    if (count < nodes.size()) {

                        if (count % 2 != 0) {

                            if (count > 1) {
                                line = "\"" + graph.singleValueQuery(previousNode, "name", "None") + "\" -- \"" + graph.singleValueQuery(node, "name", "None") + "\"" ;
                                pw.println(line) ;
                            }
                            //odd node - new Line
                            newLine = "\"" + graph.singleValueQuery(node, "name", "None") + "\" -- \""   ;
                        } else  {
                            //even line - finish the line and save the current node as the previous node
                            line += newLine + graph.singleValueQuery(node, "name", "None") + "\"" ;
                            previousNode = node ;
                            pw.println(line) ;
                            newLine = "" ;
                        }
                    //On Nth node of N nodes
                    }  else {
                        if (count % 2 != 0) {
                           //last node is odd so new line containing the previous node and this one
                           line =  "\"" + graph.singleValueQuery(previousNode, "name", "None") + "\" -- \"" + graph.singleValueQuery(node, "name", "None") + "\"" ;
                           pw.println(line) ;
                        } else {
                           //last node is even so finish the new line created in previous iteration
                           line = newLine + node + "\"" ;
                           pw.println(line) ;
                        }
                        //print the last line
                        pw.println(" }\n");
                    }
                    graphVizFile.close();
                    pw.close() ;

                }
            } catch(IOException ioe) {
                System.out.println(ioe.getMessage()) ;
            }
        }
            //Add a node and an edge,
            //For every second node, connect it with the edge previously added
    }// end drawGraph that takes a List for nodes to represent.
}//end class