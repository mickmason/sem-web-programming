package section_3_examples;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import section_3_examples.simple_graph.SimpleGraph;
import section_3_examples.inference_rules.* ;
import java.util.* ;
import section_3_examples.graph_utils.GraphUtil ;
/**
 *
 * @author Michael
 */
public class BusinessGraphTest2 {
       public static void main(String[] args) {
            List<String[]> graphPattern = new ArrayList<String[]>() ;
            /*
                Query to find businesses in New York whose business is Investment Banking and who made a contirbution
             *  to Orrin Hatch
             */
            //Create a new SimpleGraph
            SimpleGraph busGraph = new SimpleGraph() ;

            //Read in triples from the file given:
            String fileName = "C:\\Users\\Michael\\DT249\\year_4\\final_year_project\\semweb_programming\\business_triples_edited_2.txt" ;
            if (busGraph.readTriples(fileName)) {
                System.out.println("Triples read from " + fileName + " ok.");
            } else {
                System.out.println("Error reading from " + fileName);
                System.exit(1);
            }
            /*
                Query for all investment banks in New York and which made contributions to politicain
             *  Orrin Hatch.
            */
            System.out.println() ;
            System.out.println("Example showing querying using query language.\nQuery for investment banks in New York who made contributions to politician Orrin Hatch:") ;
            String triplePatterns[] = new String[5] ;
            triplePatterns[0] = "?company,headquarters,New_York_New_York";
            triplePatterns[1] = "?company,industry,Investment_Banking" ;
            triplePatterns[2] = "?contribution,contributor,?company" ;
            triplePatterns[3] = "?contribution,recipient,Orrin Hatch" ;
            triplePatterns[4] = "?contribution,amount,?dollars" ;
            graphPattern.add(triplePatterns) ;
            
            //System.out.println(busGraph.qlQuery(queries).toString()) ;
            //Run the query. Get the results and present them. All of the results bindings are of interest.
            Set<HashMap<String, String>> results = (HashSet<HashMap<String, String>>) busGraph.qlQuery(graphPattern) ;
            int count = 1 ;
            for (HashMap<String, String> result: results) {
                System.out.println("Result no." + count + ": ") ;
                Set<String> keys =  result.keySet() ;
                for (String key: keys) {
                    System.out.print(key + ": " + result.get(key) + ". ");
                }
                System.out.println("") ;
            count++ ;
            }

            /*
             *  Here WestCoastRule() is used to apply a rule to the current triples and return a set of bindings as
             *  as the result of the rule's embedded queries, and then run a transformation on the bindings 
             *  and add them to the triplestore as triples.
             *
             *  After this a call to the BusinessGraph.queryTriples() which looks for the new triples just added
             */
            System.out.println() ;
            System.out.println("Executing West-Coast Rule:") ;
            InferenceRule westCoastRule = new WestCoastRule() ;
            busGraph.applyInferenceRule(westCoastRule) ;
            Set<String> triples = busGraph.queryTriples("None", "on_coast", "west_coast") ;
            System.out.println(triples.size() + " on_coast,west_coast triples:");
            for (String triple: triples) {
                System.out.println(triple) ;
            }

       }//end main()
}//end class
