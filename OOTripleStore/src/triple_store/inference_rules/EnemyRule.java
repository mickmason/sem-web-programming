/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package triple_store.inference_rules;
import java.util.* ;

/**
 *
 * @author Michael
 */
public class EnemyRule implements InferenceRule {
    public List<String[]> getQueries() {

        List<String[]> queries = new ArrayList<String[]>() ;

        String[] partnerEnemy = new String[3] ;

        partnerEnemy[0] = "?person,enemy,?enemy" ;
        partnerEnemy[1] = "?rel,with,?person" ;
        partnerEnemy[2] = "?rel,with,?partner" ;

        queries.add(partnerEnemy) ;

        return queries ;
    }
    public List<String> makeTriples(Set<HashMap<String, String>> bindingsSet) {
        List<String> newTriples = new ArrayList<String>() ;
        String newTriple ;
        String partner = "" ;
        String enemy = "" ;

        for (HashMap<String, String> bindings: bindingsSet) {
            Set<String> bindingKeys = bindings.keySet() ;
            for (String bindingKey: bindingKeys) {
                if (bindings.get(bindingKey).equals("None") || bindingKey.equals("?rel") || bindingKey.equals("?person")) {
                    continue ;
                }
                if (bindingKey.equals("?partner")) {
                    partner = bindings.get(bindingKey) ;
                } else if (bindingKey.equals("?enemy")) {
                    enemy = bindings.get(bindingKey) ;
                } else { continue ; }
                
            }
            newTriple = partner + ",enemy," + enemy ;
            System.out.println(newTriple) ;
            newTriples.add(newTriple) ;
        }
        System.out.println(newTriples.size() + "new triples created.") ;
        System.out.println() ;
        return newTriples ;
    }

}
