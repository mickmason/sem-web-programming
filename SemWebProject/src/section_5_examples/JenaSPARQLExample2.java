/*
 * This examples shows the use of the SPARQL SELECT and CONSTRUCT query forms, how to retrieve results in XML format
 * and how to FILTER and modify results with ORDER BY, LIMIT, OFFSET and DISTINCT
 *
 * 
 */

package section_5_examples;

import com.hp.hpl.jena.query.* ;
import com.hp.hpl.jena.rdf.model.* ;
import com.hp.hpl.jena.util.FileManager ;
import java.io.* ;

public class JenaSPARQLExample2 {
    public static void main(String[] args) {
        Model moviesModel = ModelFactory.createDefaultModel() ;
        InputStream in = FileManager.get().open("C:\\Users\\Michael\\DT249\\year_4\\final_year_project\\semweb_programming\\chapter_4\\data\\moviedata-large\\moviedata-large.xml") ;
        moviesModel.read(in, null) ;
        
        /* Example 6.2 Using Jena to create SPARQL CONSTRUCT queries */
        System.out.println("Example 6.2: Using Jena to create a SPARQL CONSTRUCT query") ;
        System.out.println("CONSTRUCT ?who empl:was_employed_in ?year") ;
        //Create the query string
        final String FB = "<http://rdf.freebase.com/ns/>" ;
        final String emplNs = "<http://employment.history/>" ;
        String queryStr =   "PREFIX fb:" + FB
                            + "PREFIX empl:" + emplNs
                            + "CONSTRUCT {"
                            + "?who empl:was_employed_in ?year"
                            + "} "
                            + "WHERE {"                                    
                                    + "?film fb:film.film.initial_release_date ?year . "
                                    + "?film fb:film.film.starring ?who ."
                                    + "FILTER(?year > \"2003\")"
                            + "} LIMIT 20" ;
        QueryExecution exec = QueryExecutionFactory.create(queryStr, moviesModel) ;
        Model resultsModel = ModelFactory.createDefaultModel() ;
        try {
            resultsModel = exec.execConstruct() ;
            //Access the results in the Model:
            if (resultsModel.isEmpty()) {
                System.out.println("No results were returned") ;
            } else {
                resultsModel.write(System.out) ;
            }
        } finally { exec.close() ; }


        /* 
            Example 6.3: An ASK query
            In this example the resultsModel that was just created is used.
         *  The ASK is if it contains the triple:
         *  http://rdf.freebase.com/ns/en.dakota_fanning <http://employment.history/was_employed_in> 2005
         *  Does the model have a triples that asserts that Dakota Fanning was employed in 2005.
         */
        System.out.println() ;
        System.out.println("Example 6.3: An ASK query:") ;
        queryStr =   "PREFIX fb:" + FB
                     + "PREFIX empl:" + emplNs
                     + "ASK {"
                        + "fb:en.elisabeth_shue empl:was_employed_in \"2005\" ."
                     + "} " ;
        exec = QueryExecutionFactory.create(queryStr, resultsModel) ;
        Boolean result = true ;
        try {
            result = exec.execAsk() ;
            //Access the results in the Model:
            if (result) {
                System.out.println("Yep!") ;
            } else {
                System.out.println("Nope") ;
            }
        } finally { exec.close() ; }

        System.out.println() ;
        System.out.println("Example 6.4a: A DESCRIBE query:") ;
        queryStr = "PREFIX fb:" + FB
                     + "DESCRIBE fb:en.robert_de_niro " ;
        exec = QueryExecutionFactory.create(queryStr, moviesModel) ;
        Model describeModel = ModelFactory.createDefaultModel() ;
        try {
            describeModel =  exec.execDescribe() ;
            describeModel.write(System.out) ;
        } finally {
            exec.close() ;
        }
        
        System.out.println() ;
        System.out.println("Example 6.4b: A DESCRIBE query:") ;
        queryStr = "PREFIX fb:" + FB
                     + "DESCRIBE ?x  "
                     + "WHERE {"
                        + "?x fb:film.film.starring fb:en.robert_de_niro ."
                     + "} " ;

        exec = QueryExecutionFactory.create(queryStr, moviesModel) ;
        describeModel = ModelFactory.createDefaultModel() ;
        try {
            describeModel =  exec.execDescribe() ;
            describeModel.write(System.out) ;
        } finally {
            exec.close() ;
        }
    }//main
}//class
