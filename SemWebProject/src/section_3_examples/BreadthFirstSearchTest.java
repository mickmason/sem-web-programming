/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package section_3_examples;

import section_3_examples.simple_graph.SimpleGraph ;
import java.util.* ;
import section_3_examples.graph_utils.GraphMaker ;

/**
 *
 * @author Michael
 */
public class BreadthFirstSearchTest {
    public static void main(String[] args) {
        
        SimpleGraph bfsGraph = new SimpleGraph() ;
        //Read in triples from the file given:
        String fileName = "C:\\Users\\Michael\\DT249\\year_4\\final_year_project\\semweb_programming\\movies.txt" ;
        if ( bfsGraph.readTriples(fileName) ) {
            System.out.println("Triples read from " + fileName + " ok.");
        } else {
            System.out.println("Error reading from " + fileName);
            System.exit(1);
        }

        //return the path found from start node to end node
        System.out.println() ;
        System.out.println("Find connections between Harrison Ford and Kevin Bacon:") ;
        List<String> path = (ArrayList<String>) bfsGraph.graphBfs("/en/harrison_ford", "/en/kevin_bacon", bfsGraph) ;
        System.out.println("Distance: " + (path.size() - 1) ) ;
        for (String nextPathElement: path) {
            if (!nextPathElement.equals("None")) {
                nextPathElement = bfsGraph.singleValueQuery(nextPathElement, "name", "None") ;
                System.out.println(nextPathElement) ;
            } else {
                continue ;
            }
        }
        GraphMaker.drawGraph(bfsGraph, path, "C:\\Users\\Michael\\DT249\\year_4\\final_year_project\\semweb_programming\\GraphVizExamples\\list_example.dot");
    }//end main()
}//end class
