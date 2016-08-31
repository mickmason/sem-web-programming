/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package triple_store_examples;
import triple_store.simple_graph.SimpleGraph ;
import triple_store.graph_utils.GraphUtil ;
import java.util.* ;
/**
 *
 * @author Michael
 */
public class JoinGraphsTest {
    public static void main(String[] args) {
            //Create a business graph and import triples from file
            SimpleGraph busGraph = new SimpleGraph() ;

            //Read in triples from the file given:
            String fileName = "C:\\Users\\Michael\\DT249\\year_4\\final_year_project\\semweb_programming\\business_triples_edited_2.txt" ;
            if (busGraph.readTriples(fileName)) {
                System.out.println("Triples read from " + fileName + " ok.");
            } else {
                System.out.println("Error reading from " + fileName);
                System.exit(1);
            }

            //create a new places graph and read in triples from file
            SimpleGraph placesGraph = new SimpleGraph() ;
            fileName = "C:\\Users\\Michael\\DT249\\year_4\\final_year_project\\semweb_programming\\place_triples.txt" ;
            //read a set of triples into the graph from the filename given
            if (placesGraph.readTriples(fileName)) {
                System.out.println("Triples read from " + fileName + " ok.");
            } else {
                System.out.println("Error reading from " + fileName);
                System.exit(1);
            }
            System.out.println() ;
            System.out.println("Example of merging info from one graph into another. ") ;
            //Do a query over each graph
            System.out.println() ;
            System.out.println("Query business graph for all businesses located in San Francisco, California:") ;
            Set<String> busQueryResults = (HashSet<String>) busGraph.queryTriples("None", "headquarters", "San_Francisco_California") ;
            if (!busQueryResults.isEmpty()) {
                System.out.println(busQueryResults.size() + " results found:") ;
                for (String busQueryResult: busQueryResults) {
                    System.out.println(busQueryResult) ;
                }
            } else {
                    System.out.println("Sorry, no results were found, please check your query items.") ;
            }

            //Do a query over each graph
            System.out.println() ;
            System.out.println("Query places graph for San Francisco triples ie all triples with San_Francisco_California as their key:") ;
            Set<String> placesQueryResults = (HashSet<String>) placesGraph.queryTriples("San_Francisco_California", "None", "None") ;
            if (!placesQueryResults.isEmpty()) {
                System.out.println(placesQueryResults.size() + " results found:") ;
                for (String placesQueryResult: placesQueryResults) {
                    System.out.println(placesQueryResult) ;
                }
            } else {
                    System.out.println("Sorry, no results were found, please check your query items.") ;
            }

            System.out.println() ;
            System.out.println("Merge data from the places graph into the business graph. ") ;
            System.out.println("Find company HQs, then get triples for each HQ from placesGraph and add it to busGraph") ;

            Set<String> businessHQsTriples = (HashSet<String>) busGraph.queryTriples("None", "headquarters", "None") ;
            Set<String> businessHQs = new HashSet<String>() ;
            //System.out.println() ;
            //System.out.println("Places where cos have HQs:") ;
            for (String businessHQsTriple: businessHQsTriples) {
                String[] businessHQsTripleTokens = businessHQsTriple.split(GraphUtil.delimiter) ;
              //  System.out.println(businessHQsTripleTokens[2]) ;
                businessHQs.add(businessHQsTripleTokens[2]) ;
            }

            Set<String> placesTriples = (HashSet<String>) placesGraph.queryTriples("None", "None", "None") ;
            //System.out.println() ;
            //System.out.println("Places triples:") ;
            for (String placesTriple: placesTriples) {
                //System.out.println(placesTriple) ;
                String[] placesTripleTokens = placesTriple.split(GraphUtil.delimiter) ;
                if (businessHQs.contains(placesTripleTokens[0])) {
                    busGraph.addTriple(placesTripleTokens[0], placesTripleTokens[1], placesTripleTokens[2]);
                } else {
                    continue ;
                }
            }
            
            System.out.println("Then, run query language queries over the merged data.") ;
            System.out.println("Find regions containing companies whose industry is Computer software:") ;
            List<String[]> graphPatterns = new ArrayList<String[]>() ;
            String[] triplePatterns = new String[3] ;
            triplePatterns[0] = "?company,headquarters,?city" ;
            triplePatterns[1] = "?city,inside,?region" ;
            triplePatterns[2] = "?company,industry,Computer software" ;

            graphPatterns.add(triplePatterns) ;
            Set<HashMap<String, String>> mergedDataResults = busGraph.qlQuery(graphPatterns) ;
            //System.out.println(mergedDataResults) ;

            for (HashMap<String, String> result: mergedDataResults) {
                Set<String> resultKeys = result.keySet() ;
                for (String resultKey: resultKeys) {
                    if (resultKey.equals("?region")) {
                        System.out.print(" Region: " + result.get(resultKey)) ;
                    } else if (resultKey.equals("?company")) {
                        System.out.print("Company: " + result.get(resultKey)) ;
                    }   
                }
                System.out.println() ;
            }

            System.out.println() ;
            System.out.println("Query for investment banks located in cities with population greater than 1,000,000 people: ") ;

            graphPatterns =  new ArrayList<String[]>() ;
            triplePatterns = new String[3] ;
            triplePatterns[0] = "?company,headquarters,?city" ;
            triplePatterns[1] = "?city,population,?pop" ;
            triplePatterns[2] = "?company,industry,Investment_Banking" ;

            graphPatterns.add(triplePatterns);
            mergedDataResults = busGraph.qlQuery(graphPatterns) ;
            int pop = 0 ;
            String company = "" ;
            String city = "" ;
                    
            for (HashMap<String, String> result: mergedDataResults) {
                Set<String> resultKeys = result.keySet() ;
                for (String resultKey: resultKeys) {
                   if (resultKey.equals("?pop")) {
                        pop = Integer.parseInt(result.get(resultKey)) ;
                    } else if (resultKey.equals("?company")) {
                        company = result.get(resultKey) ;
                    } else if (resultKey.equals("?city")) {
                        city = result.get(resultKey) ;
                    }
                }
                if (pop < 1000000) {
                        continue ;
                }  
                System.out.println("City: " + city + "\tCompany: " + company + "\tPopulation: " + pop) ;
            }
     }//end main()
}//end class
