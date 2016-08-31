package triple_store.graph_utils;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.* ;
import java.util.* ;
/**
 *
 * @author Michael
 */
public class TripleReader {

    private File file = null ;
    private FileReader fr = null ;
    private BufferedReader br = null ;

    public TripleReader(String fileName) {
        this.file = new File(fileName) ;
        try {
            this.fr = new FileReader(file) ;
            this.br = new BufferedReader(fr) ;
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage()) ;
        }
    }

    public Set<String> readTriplesFromFile() {
        Set<String> triples = new HashSet<String>() ;
        String[] tokens = new String[3] ;
        String line ;
        String delimiter = ",(?! )" ;
        try {
            while ((line = br.readLine()) != null) {
                //read a line from the given file, parse it, add the sub, ob, pred to a String and add each String to the return Set
                tokens = line.split(delimiter) ;
                String triple = tokens[0] + "," + tokens[1] + "," + tokens[2];
                triples.add(triple);
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getCause() + " " + ioe.getMessage()) ;
        } finally {
            try {
                fr.close() ;
                br.close() ;
            } catch (IOException ioe) {
                System.out.println(ioe.getCause() + ": " + ioe.getMessage()) ;
            }
        }
        return triples ;
    }//end readTriplesFromFile() 

}//end class
