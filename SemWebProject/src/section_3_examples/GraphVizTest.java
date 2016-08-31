/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package section_3_examples ;
import section_3_examples.simple_graph.SimpleGraph;
import section_3_examples.graph_utils.GraphUtil;
import section_3_examples.graph_utils.GraphMaker;
import java.util.* ;
/**
 *
 * This tests the use of GraphViz to create a visual representation of a set of triples first, then some query bindings
 */
public class GraphVizTest {

    public static void main(String[] args) {
        SimpleGraph placesGraph = new SimpleGraph() ;

        //Read in triples from the file given:
        String fileName = "C:\\Users\\Michael\\DT249\\year_4\\final_year_project\\semweb_programming\\place_triples.txt" ;
        if (placesGraph.readTriples(fileName)) {
            System.out.println("Triples read from " + fileName + " ok.");
        } else {
            System.out.println("Error reading from " + fileName);
            System.exit(1);
        }

        Set<String> placesInCalifTriples = (HashSet<String>) placesGraph.queryTriples("None", "inside", "California") ;

        //Set for mayor results:
        Set<String> mayorTriples = new HashSet<String>() ;


        int count = 1 ;
        for (String placeTriple: placesInCalifTriples) {
            String[] placeTripleTokens = placeTriple.split(GraphUtil.delimiter) ;
            mayorTriples.addAll(placesGraph.queryTriples(placeTripleTokens[0], "mayor", "None")) ;
            //queryTriple returns a null set if no results are found that match a qlQuery. If this happens, continue to the next iteration of the loop.
            if (mayorTriples.isEmpty()) {
                System.out.println(placeTripleTokens[0] + " does not have a mayor") ;
                count++ ;
                continue ;
            //Other wise show the results for each iteration
            } else {
                System.out.println("Place: " + count) ;
                GraphUtil.showResults(mayorTriples);
                count++ ;
            }
        }
        System.out.println("Mayor triples size = " + mayorTriples.size()) ;
        GraphMaker.drawGraph(placesGraph, mayorTriples, "C:\\Users\\Michael\\DT249\\year_4\\final_year_project\\semweb_programming\\mayor_places_graph.dot") ;
    }//end main()
}//end class
