package triple_store.graph_utils;
import java.util.* ;
import triple_store.simple_graph.SimpleGraph ;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Collection ;
import java.util.Iterator ;
/**
 *
 * @author Michael
 */
public class GraphUtil {
    public static String[] placeTripleTokens = new String[3] ;
    public static String delimiter = ",(?! )" ;

    public static void showResults(Collection<String> results) {
        for (String result: results)
            System.out.println(result) ;
   }//end showResults

    public static void showAllTriples(SimpleGraph graph) {
        System.out.println() ;

        String[] triplePatterns = new String[1] ;
        triplePatterns[0] = "?sub,?pred,?obj" ;
        List<String[]> queryPattern = new ArrayList<String[]>() ;
        queryPattern.add(triplePatterns) ;

        Set<HashMap<String, String>> results = (HashSet<HashMap<String, String>>) graph.qlQuery(queryPattern) ;

        for (HashMap resultMap: results) {
            Set<String> variables = resultMap.keySet() ;
            String resultTriple = "" ;
            String sub = "" ;
            String pred = "" ;
            String obj = "" ;
            for (String variable: variables) {
                if (variable.equals("?sub")) {
                    sub += " sub: " + resultMap.get(variable) + "\t" ;
                } else if (variable.equals("?pred")) {
                    pred += " pred: " + resultMap.get(variable) + "\t" ;
                } else if (variable.equals("?obj")) {
                    obj += " obj: " + resultMap.get(variable) + "\t" ;
                }
            }
            resultTriple += sub + pred + obj ;
            System.out.println(resultTriple) ;
        }
    }//end showAllTriples()
}//end class
