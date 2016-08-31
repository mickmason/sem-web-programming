/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package section_3_examples.inference_rules;
import section_3_examples.graph_utils.GraphUtil;
import section_3_examples.graph_utils.* ;
import java.util.* ;
import java.net.* ;
import java.io.* ;
/**
 *
 * @author Michael
 */
public class GeocodeRule implements InferenceRule {

    public List<String[]> getQueries() {
        List<String[]> queriesList = new ArrayList<String[]>() ;
        String[] queries = new String[1] ;
        queries[0] = "?place,address,?address" ;
        queriesList.add(queries) ;
        
        return queriesList ;
    }

    public List<String> makeTriples(Set<HashMap<String, String>> bindingsSet) {
        //New List of triples to be added to graph after rule is run
        List<String> triples = new ArrayList<String>() ;
        //String URL for geocoding service
        String geocoderURL = "http://rpc.geocoder.us/service/csv?address=1600%2BPennsylvania%2BAve%2C%2BWashington%2BDC" ;
        //Value for each binding
        String value = "" ;
        //result as returned from gecoding service
        String geocoderResult = "" ;
        //Individual longitude triple - will be added to triples List
        String longTriple = ",longitude," ;
        //Individual latitude triple - will be added to triples List
        String latTriple = ",latitude," ;
        //This will be used to strip out each token in the geocoderResult String - placeTripleTokens[0] (long) and placeTripleTokens[1] (lat) will be of interest
        String[] geocoderResultTokens = new String[6] ;
        
        for (HashMap<String, String> bindings: bindingsSet) {
            Set<String> bindingsKeys = bindings.keySet() ;
            for (String bindingsKey: bindingsKeys) {

                //If we are on the bindings for ?address
                if (bindingsKey.equals("?address")) {
                    URL url = null ;
                    try {
                        //append the address to the geocoderURL and encode the URL, create a new URL object
                        url = new URL(geocoderURL) ;
                        //Geta Reader on the URL
                        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())) ;
                        //Read from it, split the retruned value into placeTripleTokens and extract the longitude and latitude values
                        //Append them to longTriple and latTriple
                        while ((geocoderResult = in.readLine()) != null) {
                            System.out.println("Result: " + geocoderResult) ;
                            geocoderResultTokens = geocoderResult.split(GraphUtil.delimiter) ;
                            if (geocoderResultTokens[4] != null) {
                                //long and lat values are at tokends[0] and placeTripleTokens[1] respectively
                                longTriple += geocoderResultTokens[0] ;
                                latTriple += geocoderResultTokens[1] ;
                            }
                        }
                        in.close();
                    } catch (MalformedURLException e) {
                        System.out.println(e.getMessage()) ;
                    } catch (IOException ioe) {
                        System.out.println(ioe.getMessage()) ;
                    }
                } else if (bindingsKey.equals("?place")) {
                    //If We're on the place binding, prepend that value onto longTriple and latTriple
                    value = bindings.get(bindingsKey) ;
                    longTriple = value + longTriple ;
                    latTriple = value + latTriple ;
                } else {
                    //Continue for all other bindings
                    continue ;
                }
            }
            //Add a triple for each binding to triples
            triples.add(longTriple) ;
            triples.add(latTriple) ;
        }
        return triples ;
    }//end makeTriples() 
}//end class
