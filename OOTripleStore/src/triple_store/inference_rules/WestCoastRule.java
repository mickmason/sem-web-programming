package triple_store.inference_rules;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.* ;
/**
 *
 * @author Michael
 */
public class WestCoastRule implements InferenceRule {

    public List<String[]> getQueries() {
        List<String[]> queries = new ArrayList<String[]>() ;
        //SanFrancisco query
        String[] sfoQuery = new String[1] ;
        sfoQuery[0] = "?company,headquarters,San_Francisco_California" ;
        queries.add(sfoQuery) ;
        String[] seaQuery = new String[1] ;
        seaQuery[0] = "?company,headquarters,Seattle_Washington" ;
        queries.add(seaQuery) ;
        String[] laxQuery = new String[1] ;
        laxQuery[0] = "?company,headquarters,Los_Angeles_California" ;
        queries.add(laxQuery) ;
        String[] porQuery = new String[1] ;
        porQuery[0] = "?company,headquarters,Portland_Oregon" ;
        queries.add(porQuery) ;
        return queries ;
    }

    public List<String> makeTriples(Set<HashMap<String, String>> bindingsSet) {
        List<String> newTriples = new ArrayList<String>() ;
        //for each binding in the Set
        for (HashMap<String, String> bindings: bindingsSet) {
            Set<String> keys = bindings.keySet() ;
            for (String key: keys) {
                String company = bindings.get(key)  ;
                if (!company.equals("None")) {
                    newTriples.add(company + ",on_coast,west_coast") ;
                } else { continue ; }
            }
        }
        return newTriples ;
    }

}
