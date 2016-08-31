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
public class BusinessGraphTest {
    public static void main(String[] args) {
        SimpleGraph busGraph = new SimpleGraph() ;
        String fileName = "C:\\Users\\Michael\\DT249\\year_4\\final_year_project\\semweb_programming\\business_triples.csv" ;

        if (busGraph.readTriples(fileName)) {
            System.out.println("Triples read from " + fileName + " ok") ;
        } else {
            System.out.println("Error reading tiples from " + fileName) ;
            System.exit(1) ;
        }

        //Find all investment banks
        System.out.println() ;
        System.out.println("Example of querying triples.") ;
        System.out.println("Find all investment banks who made contributions to US politicians, the amount of the contribution, and the name of the politician:") ;
        Set<String> iBanksTriples = busGraph.queryTriples("None", "industry", "Investment banking") ;
        Set<String> iBanksIds = new HashSet<String>() ;

        System.out.print("First find all investment banks - total no. of banks: ") ;
        for (String iBankTriples: iBanksTriples) {
            GraphUtil.placeTripleTokens = iBankTriples.split(GraphUtil.delimiter) ;
            iBanksIds.add(GraphUtil.placeTripleTokens[0]) ;
        }
        System.out.println(iBanksIds.size()) ;

        //Find all contributions from Investment Banks to politicians
        
        Set<String> bankContributors = new TreeSet<String>() ;
        
        //Find all use the bank Ids to find contributions by investment banks
        for (String iBankId: iBanksIds) {
            //Get the ids of all contributors which are banks:
            Set<String> contributionTriples = busGraph.queryTriples("None", "contributor", iBankId) ;
            if (!contributionTriples.isEmpty()) {
                //For each contributor bank
                for (String contributionTriple: contributionTriples) {
                    String contribution = busGraph.singleValueQuery(iBankId, "name", "None") ;
                    GraphUtil.placeTripleTokens = contributionTriple.split(GraphUtil.delimiter) ;
                    //Get the amount of the contribution and the reciever
                    contribution += ": " + busGraph.singleValueQuery(GraphUtil.placeTripleTokens[0], "amount", "None") ;
                    contribution += ", " + busGraph.singleValueQuery(GraphUtil.placeTripleTokens[0], "recipient", "None") + "." ;
                    bankContributors.add(contribution) ;
                }
            } else {
                continue ;
            }
        }
        System.out.print("Find all contributions from Investment Banks to politicians - " ) ;
        System.out.println(bankContributors.size() + " results:") ;
        GraphUtil.showResults(bankContributors);

        System.out.println() ;
        System.out.print("Find locations with 3 or more software cos. headquarters.") ;
        //Get all s/w cos ids:
        Set<String> swCosTriples = busGraph.queryTriples("None", "industry", "Computer software") ;
        String location = "" ;
        List<String> locations = new ArrayList<String>() ;
        Set<String> locationsWith3OrMoreCos = new TreeSet<String>() ;
        //Get the HQ for each S/W company
        for (String swCoTriple: swCosTriples) {
            GraphUtil.placeTripleTokens = swCoTriple.split(GraphUtil.delimiter) ;
            location = busGraph.singleValueQuery(GraphUtil.placeTripleTokens[0], "headquarters", "None") ;
            if (!location.equals("")) {
                locations.add(location) ;
            }
        }
        //Sort the list of companies. Then find locations which occur more than 3 times
        Collections.sort(locations) ;
        int locStartIndex = 0 ;
        int locEndIndex = 0 ;
        for (String loc: locations) {
            locStartIndex = locations.indexOf(loc) ;
            locEndIndex = locations.lastIndexOf(loc) ;
            //Then take out only the locations with 3 or more companies' HQs
            if (locEndIndex - locStartIndex >= 2) {
                locationsWith3OrMoreCos.add(loc) ;
            }
        }
        //Show the locations
        System.out.println(locationsWith3OrMoreCos.size() + " results:") ;
        GraphUtil.showResults(locationsWith3OrMoreCos) ;
    }//end main()   
}//end class
