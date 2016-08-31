package section_7_examples;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.hp.hpl.jena.rdf.model.* ;
import com.hp.hpl.jena.query.* ;
import com.hp.hpl.jena.util.FileManager ;
import java.io.* ;
import java.net.* ;
/**
 *
 * @author Michael
 */
public class CatherInTheRyeTest {

    public static void main(String[] args) {
        Model catcherModel = ModelFactory.createDefaultModel() ;
        //Get the data about Catcher in the Rye from Freebase
        //String for the FBase url
        String fBaseUrlStr = "http://rdf.freebase.com/ns/en.alex_lifeson" ;
        try {
            RDFReader reader = catcherModel.getReader() ;
            InputStream in = FileManager.get().open(fBaseUrlStr) ;
            reader.read(catcherModel, in, null) ;
            RDFWriter writer = catcherModel.getWriter() ;
            writer.write(catcherModel, System.out, null) ;
        } catch (Exception e) {
            e.getStackTrace() ;
        }

        //Do some stuff with it...
    }//end main

}//end class
