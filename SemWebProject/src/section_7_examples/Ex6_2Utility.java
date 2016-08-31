/*
 * This is a set of static utility methods for use in example_6_2 which uses data from a number of sources to build up a graph
 * containing information about bands from some country.
 */

package section_7_examples;
import com.hp.hpl.jena.rdf.model.* ;
import com.hp.hpl.jena.util.FileManager ;

/**
 *
 * @author Michael
 */
public class Ex6_2Utility {

    public static void main(String[] args) {
        Model m = ModelFactory.createDefaultModel() ;

        m.read("http://xmlns.com/foaf/0.1/made") ;
        m.write(System.out, "N3") ;
     }
    
}
