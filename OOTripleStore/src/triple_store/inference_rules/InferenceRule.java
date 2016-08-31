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
public interface InferenceRule {
    public List<String[]> getQueries() ;
    public List<String> makeTriples(Set<HashMap<String, String>> bindings) ;
}
