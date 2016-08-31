/*
 * Class that contains the examples for Section 6 - The SPARQL query language for RDF
 */

package section_5_examples;
import com.hp.hpl.jena.query.* ;
import com.hp.hpl.jena.rdf.model.* ;
import com.hp.hpl.jena.util.FileManager ;
import java.io.* ;
/**
 *
 * @author Michael
 */
public class JenaSPARQLExample1 {

    public static void main(String[] args) {
        System.out.println("JenaSPARQLExamples\n") ;
        //Example 6.1 A SELECT query:
        /*
            Selects a film ID, its name and release date for all films in the dataset
            It queries the moviedata-large.xml dataset
         *
         */
        System.out.println() ;
        System.out.println("Example 6.1: A simple SELECT query") ;

        //Create a model and read moviesdata-large.xml into it
        Model moviesModel = ModelFactory.createDefaultModel() ;
        InputStream in = FileManager.get().open("C:\\Users\\Michael\\DT249\\year_4\\final_year_project\\semweb_programming\\chapter_4\\data\\moviedata-large\\moviedata-large.xml") ;        
        moviesModel.read(in, null) ;
        
        //Example 6.1 a basic SELECT query:
        //Create a Freebase prefix "FB" then create the query String
        final String FB = "<http://rdf.freebase.com/ns/>" ;
        String queryString =    "PREFIX fb:" + FB
                                + "SELECT ?filmId ?year "
                                + "WHERE {"
                                + "?filmId fb:film.film.initial_release_date ?year . "
                                + "} LIMIT 10" ;

        //Create the Query:
        Query moviesQuery = QueryFactory.create(queryString) ;
        //Create a QueeryExecution object to run the query:
        QueryExecution execute = QueryExecutionFactory.create(moviesQuery, moviesModel) ;

        //Execute the query and access the results:
        try {
        ResultSet results = execute.execSelect() ;
        //Access each result, find out its type - Resource, Literal or RDFNode and print the results:
        System.out.println("Accessing results...") ;
        while (results.hasNext()) {
            //Get each next solution as a QuerySolution
            QuerySolution solut = results.next() ;
            //Access each solution variable result as an RDFNode
            RDFNode filmId = solut.get("?filmId") ;
            RDFNode year = solut.get("?year") ;
            //Check the RDFNode type for each result and print the results:
            if (filmId.isResource()) {
                Resource filmIdAsRes = filmId.asResource() ;
                System.out.print("Film ID (as Resource): " + filmIdAsRes.toString() + "\t") ;
            } else if (filmId.isLiteral()) {
                Literal filmIdLit = filmId.asLiteral() ;
                System.out.print("Film ID (as Literal): " + filmIdLit + "\t") ;
            } else {
                System.out.print("Film ID (as RDFNode): " + filmId.toString() + "\t") ;
            }

            if (year.isResource()) {
                Resource yearAsRes = year.asResource() ;
                System.out.println("\tYear (Resource): " + yearAsRes.toString() + "") ;
            } else if (year.isLiteral()) {
                Literal yearIdLit = year.asLiteral() ;
                System.out.println("\tYear (Literal): " + yearIdLit + "\t\t") ;
            } else {
                System.out.println("\tYear (RDFNode): " + year.toString() + "\t\t") ;
            }
            System.out.println() ;
        }
        //Close the QueryExecution:
        } finally { execute.close(); }

        //It also possible to have the results formatted by ResultSetFormatter:
        //Re-excute the query
        //Note here the use of the query String as argument to QueryExecutionFactory.create() 
        //--> this cuts out the step of having to create a Query object first:
        execute = QueryExecutionFactory.create(queryString, moviesModel) ;
        try {
            ResultSet results = execute.execSelect() ;
            System.out.println() ;
            System.out.println("Results as text: ") ;
            //Get the results as text - gives a tabular format:
            String res = ResultSetFormatter.asText(results);
            System.out.println(res) ;
        } finally { execute.close() ; }

         //Rre-excute the query
        execute = QueryExecutionFactory.create(moviesQuery, moviesModel) ;
        try {
            ResultSet results = execute.execSelect() ;
            System.out.println() ;
            System.out.println("Results as XML: ") ;
            //Get the results as XML - creats an XML file out fo tehresults:
            String res = ResultSetFormatter.asXMLString(results);
            System.out.println(res) ;

        } finally { execute.close() ; }


    }//end main()
}//end class()
