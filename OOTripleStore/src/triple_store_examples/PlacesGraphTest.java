package triple_store_examples;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import triple_store.graph_utils.GraphUtil;
import triple_store.simple_graph.SimpleGraph;
import java.util.* ;
/**
 *
 * @author Michael
 */
public class PlacesGraphTest {

    public static void main(String[] args) {
        //Create a new graph
        SimpleGraph placesGraph = new SimpleGraph() ;
        String fileName = "C:\\Users\\Michael\\DT249\\year_4\\final_year_project\\semweb_programming\\place_triples.txt" ;

        //read a set of triples into the graph from the filename given
        if (placesGraph.readTriples(fileName)) {
            System.out.println("Triples read from " + fileName + " ok.");
        } else {
            System.out.println("Error reading from " + fileName);
            System.exit(1);
        }
        
        //Get all information about SanFrancisco
        System.out.println() ;
        System.out.println("Get all information about San Francisco:") ;
        String sanFranId = placesGraph.singleValueQuery("None", "name", "San Francisco") ;
        System.out.println("San Francisco id: " + sanFranId) ;
        //Create a Set for the triples about SanFranc
        Set<String> sanFranTriples = new HashSet<String>() ;
        sanFranTriples = placesGraph.queryTriples(sanFranId, "None", "None") ;
        if (!sanFranTriples.isEmpty()) {
            GraphUtil.showResults(sanFranTriples);
        } else {
            System.out.println("Sorry no information could be retrieved for that query.");
        }

        //Get all information about mayors in the graph
        System.out.println() ;
        System.out.println("Get all information about mayors in the graph:") ;
        Set<String> mayorsTriples = new HashSet<String>() ;
        mayorsTriples = placesGraph.queryTriples("None", "mayor", "None") ;
        if (!mayorsTriples.isEmpty()) {
            GraphUtil.showResults(mayorsTriples) ;
        } else {
            System.out.println("Sorry no information could be retrieved for that query.");
        }
        //Now get all Mayors of places in California
        System.out.println() ;
        System.out.println("Get all mayors of places in California:") ;
        //Need all place triples for "inside", "California" - returns triples with placeId as subject
        Set<String> placesInCalifTriples = (HashSet<String>) placesGraph.queryTriples("None", "inside", "California") ;
        //Set for mayor results:
        Set<String> mayorTriples = new HashSet<String>() ;
        int count = 1 ;
        for (String placeTriple: placesInCalifTriples) {
            GraphUtil.placeTripleTokens = placeTriple.split(GraphUtil.delimiter) ;
            mayorTriples = placesGraph.queryTriples(GraphUtil.placeTripleTokens[0], "mayor", "None") ;
            //queryTriple returns a null set if no results are found that match a qlQuery. If this happens, continue to the next iteration of the loop.
            if (mayorTriples.isEmpty()) {
                System.out.println("Place: " + count) ;
                System.out.println(GraphUtil.placeTripleTokens[0] + " does not have a mayor") ;
                count++ ;
                continue ;
            //Other wise show the results for each iteration
            } else {
                System.out.println("Place: " + count) ;
                GraphUtil.showResults(mayorTriples);
                count++ ;
            }
           
        }
     }//end main()

}//end class
