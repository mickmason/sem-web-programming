/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples;
import com.hp.hpl.jena.rdf.model.* ;
import com.hp.hpl.jena.sparql.vocabulary.FOAF ;
import com.hp.hpl.jena.vocabulary.RDF ;
import com.hp.hpl.jena.util.FileManager ;
import java.io.* ;
/**
 *
 * @author Michael
 */
public class FreebaseSPARQLTest {
    public static void main(String[] args) {

        Model freebaseModel = ModelFactory.createDefaultModel() ;

        InputStream in = FileManager.get().open("C:\\Users\\Michael\\DT249\\year_4\\final_year_project\\semweb_programming\\chapter_4\\data\\moviedata-large\\moviedata-large.xml") ;
        
        //create a model from the large movies data from Freebase
        freebaseModel.read(in, null) ;

    }//end main()
}//end class
