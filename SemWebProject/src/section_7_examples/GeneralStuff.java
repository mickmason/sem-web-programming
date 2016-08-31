/*
 * This is a set of general investigations of various stuff.
 */

package section_7_examples;
import com.hp.hpl.jena.rdf.model.* ;
import com.hp.hpl.jena.util.FileManager ;

/**
 *
 * @author Michael
 */
public class GeneralStuff {

    public static void main(String[] args) {
        Model m = ModelFactory.createDefaultModel() ;

        m.read("http://xmlns.com/foaf/0.1/made") ;
        m.write(System.out, "N3") ;
        
     }//end main()
}
