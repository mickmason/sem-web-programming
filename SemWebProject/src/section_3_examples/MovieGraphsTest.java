package section_3_examples;

/*
 * Main class for SimpleGraph tests
 */
import section_3_examples.graph_utils.TripleReader;
import section_3_examples.simple_graph.SimpleGraph;
import java.util.* ;
/**
 *
 * @author Michael
 */
public class MovieGraphsTest {

   public static void main(String[] args) {
        String subject = "Francis Ford Copolla" ;
        String predicate = "directed" ;
        String object = "The Godfather" ;

        //Going to need to split each string retruned from into a sub, pred, obj
        String delimiter = ",(?! )" ;
        String[] tokens = new String[3] ;
        
        //Create a  new SimpleGraph
        SimpleGraph graphOne = new SimpleGraph() ;

        //Add a triple
        graphOne.addTriple(subject, predicate, object) ;

        System.out.println("Graph1: Francis Ford Coppola directed The Godfather: ") ;
        System.out.println("Subject-predicate-object: " + graphOne.getSpo()) ;
        System.out.println("Predicate-object-subject: " + graphOne.getPos()) ;
        System.out.println("Object-subject-predicate: " + graphOne.getOsp()) ;

        subject = "the_godfather" ;
        predicate = "name" ;
        object = "The Godfather" ;
        graphOne.addTriple(subject, predicate, object) ;

        System.out.println() ;
        System.out.println("Graph1: the_godfather name 'The Godfather': ") ;
        System.out.println("Subject-predicate-object: " + graphOne.getSpo()) ;
        System.out.println("Predicate-object-subject: " + graphOne.getPos()) ;
        System.out.println("Object-subject-predicate: " + graphOne.getOsp()) ;

        subject = "francis_ford_copolla" ;
        predicate = "name" ;
        object = "Francis Ford Copolla" ;
        graphOne.addTriple(subject, predicate, object) ;

        System.out.println() ;
        System.out.println("Graph1: francis_ford_coppola name 'Francis Ford Copolla': ") ;
        System.out.println("Subject-predicate-object: " + graphOne.getSpo()) ;
        System.out.println("Predicate-object-subject: " + graphOne.getPos()) ;
        System.out.println("Object-subject-predicate: " + graphOne.getOsp()) ;
        graphOne.addTriple(subject, predicate, object) ;

        SimpleGraph graphTwo = new SimpleGraph() ;

        subject = "stanley_kubrick" ;
        predicate = "name" ;
        object = "Stanley Kubrick" ;

        graphTwo.addTriple(subject, predicate, object) ;

        System.out.println() ;
        System.out.println("Graph2: stanley_kubrick name 'Stanley Kubrick': ") ;
        System.out.println("Subject-predicate-object: " + graphTwo.getSpo()) ;
        System.out.println("Predicate-object-subject: " + graphTwo.getPos()) ;
        System.out.println("Object-subject-predicate: " + graphTwo.getOsp()) ;
        System.out.println() ;

        subject = "2001_a_space_odyssey" ;
        predicate = "name" ;
        object = "2001, A Space Odyssey" ;

        graphTwo.addTriple(subject, predicate, object) ;
        System.out.println("Graph2: 2001_a_space_odyssey name '2001, A Space Odyssey': ") ;
        System.out.println("Subject-predicate-object: " + graphTwo.getSpo()) ;
        System.out.println("Predicate-object-subject: " + graphTwo.getPos()) ;
        System.out.println("Object-subject-predicate: " + graphTwo.getOsp()) ;
        System.out.println() ;

        subject = "Stanley Kubrick" ;
        predicate = "directed" ;
        object = "2001, A Space Odyssey" ;

        graphTwo.addTriple(subject, predicate, object) ;

        System.out.println("Graph2: Stanley Kubrick directed '2001, A Space Odyssey': ") ;
        System.out.println("Subject-predicate-object: " + graphTwo.getSpo()) ;
        System.out.println("Predicate-object-subject: " + graphTwo.getPos()) ;
        System.out.println("Object-subject-predicate: " + graphTwo.getOsp()) ;
        System.out.println() ;

        System.out.println("Query all of Graph1: ") ;
        Set<String> results = (HashSet<String>) graphOne.queryTriples("None", "None", "None") ;
        Iterator resultsIterator = results.iterator() ;
        String result = "" ;
        while (resultsIterator.hasNext()) {
            result = (String) resultsIterator.next() ;
            System.out.println(result) ;
        }

        System.out.println("Query all of Graph2: ") ;
        results = (HashSet<String>) graphTwo.queryTriples("None", "None", "None") ;
        while (resultsIterator.hasNext()) {
            result = (String) resultsIterator.next() ;
            System.out.println(result) ;
        }

        System.out.println("Merge graph2 into graph1: ") ;
        graphOne.mergeGraph((HashSet<String>) graphTwo.queryTriples("None", "None", "None") );

       

        System.out.println("Query all of Graph1: ") ;
        results = (HashSet<String>) graphOne.queryTriples("None", "None", "None") ;
        while (resultsIterator.hasNext()) {
            result = (String) resultsIterator.next() ;
            System.out.println(result) ;
        }

        //Now read a graph from a file:
        TripleReader tripleReader = new TripleReader("C:\\Users\\Michael\\DT249\\year_4\\final_year_project\\semweb_programming\\movies.txt") ;
        Set<String> newTriples = (HashSet<String>) tripleReader.readTriplesFromFile() ;
        SimpleGraph movieGraph = new SimpleGraph() ;
        if (newTriples.size() > 0) {
            System.out.println("Well something happened!!") ;
            Iterator newTriplesIterator = newTriples.iterator() ;
            String triple ;
            while (newTriplesIterator.hasNext()) {
                triple = (String) newTriplesIterator.next() ;
                tokens = triple.split(delimiter) ;
                movieGraph.addTriple(tokens[0], tokens[1], tokens[2]);
            }
        }
        //Run some queries over the movieGraph
       
        /* This qlQuery gets the names of all the actors who appeared in Bladerunner */

        System.out.println() ;
        System.out.println("Names of all the actors in Bladerunner: ") ;
            
        //First use the singleValueQuery() method to get the ID for Bladerunner
        String bladeRunnerID = movieGraph.singleValueQuery("None", "name", "Blade Runner") ;
        //Going to need a Set for actorIDs in Bladerunner
        Set<String> bladerunnerActorIdsTriples ;
        String bladerunnerActorTriple  = "" ;
        //Get the actorIDs triples
        bladerunnerActorIdsTriples = (HashSet<String>) movieGraph.queryTriples(bladeRunnerID, "starring", "None") ;
        Iterator bladerunnerActorIdsTriplesIterator = bladerunnerActorIdsTriples.iterator() ;
        //For each one
        while (bladerunnerActorIdsTriplesIterator.hasNext()) {
            //Unwrap the string triple in it
            bladerunnerActorTriple = (String) bladerunnerActorIdsTriplesIterator.next() ;
            //Split the result
            tokens = bladerunnerActorTriple.split(delimiter) ;
                    //Then do a single value qlQuery on the graph for the proper name for each actor based on the actor id retrieved previously
                    System.out.println(movieGraph.singleValueQuery(tokens[2], "name", "None") ) ;

                    //All done

            }

       System.out.println() ;
       /* This one gets all Movies that Harrison Ford has appeared in */
       System.out.println("Names of all films that Harrison Ford has appeared in:") ;
       //For Harrison Ford unique ID
       String harrisonFordId = "" ;
       //Need a set for triples returned from queryTriples() for films starring harrisonFordId
       Set<String> fordMovieIdsTriples = null ;
       //String for a single triple
       String fordMovieTriple = "" ;
       //Set for the names of all Ford films
       Set<String> fordMovieNames = new HashSet<String>() ;
        //Get his Id
        harrisonFordId = movieGraph.singleValueQuery("None", "name", "Harrison Ford") ;
        System.out.println("Harrison Ford ID: " + harrisonFordId) ;
        //Query for the ids of films he has appeared in
        fordMovieIdsTriples = (HashSet<String>) movieGraph.queryTriples("None", "starring", harrisonFordId) ;
        Iterator fordMovieIdsTriplesIterator = fordMovieIdsTriples.iterator() ;
        //For each element of the Set
        while (fordMovieIdsTriplesIterator.hasNext()) {
            //Get each element
            fordMovieTriple = (String) fordMovieIdsTriplesIterator.next() ;
            //Split it into 3 string tokens (moiveID, "starring", "harrisonFordId")
            tokens = fordMovieTriple.split(delimiter) ;
            //Query for the name only of each movie with the movieId token retrieved
            fordMovieNames.add((String) movieGraph.singleValueQuery(tokens[0], "name", "None")) ;
        }
        //Show the names:
        showResults(fordMovieNames) ;

       /* Get names of all films directed by Stephen Speilberg */
       System.out.println() ;
       System.out.println("Get the names of all films directed by Steven Spielberg:") ;
       String spielbergId = "" ;
       //movieId, "directed_by", spielbergId
       Set<String> spielbergMovieIdTriples = null ;
       //Each individual triple will go here
       String spielbergMovieTriple = "" ;
       //Set of proper names will go here
       Set<String> spielbergMovieNames = new HashSet<String>() ;
           //Get his Id
           spielbergId = movieGraph.singleValueQuery("None", "name", "Steven Spielberg") ;
           System.out.println("Speilber ID: " + spielbergId) ;
           //Set of movieId, "directed_by", spielbergId elements
           spielbergMovieIdTriples = (HashSet<String>) movieGraph.queryTriples("None", "directed_by", spielbergId) ;
           Iterator spielbergMovieTripsIterator = spielbergMovieIdTriples.iterator() ;
           while (spielbergMovieTripsIterator.hasNext()) {
               //each individual element of that Set:
                spielbergMovieTriple = (String) spielbergMovieTripsIterator.next() ;
                //Split in into movieID, "directed_by", spielbergId tokens
                tokens = spielbergMovieTriple.split(delimiter) ;
                //Use the movieId token to get the proper names of the films and add that to the Set of movie names
                spielbergMovieNames.add((String) movieGraph.singleValueQuery(tokens[0], "name", "None")) ;
           }
           //Show the results:
           showResults(spielbergMovieNames) ;

           System.out.println() ;
           System.out.println("Now get the names of all movies that Spielberg direceted and Ford starred in: ") ;
           System.out.println("(The intersection of the set of Ford movie names and the set of Spielberg movie names.)") ;
           //Get the intersection of the set of Spielberg movie names and Ford movie names:
           spielbergMovieNames.retainAll(fordMovieNames) ;
           //Show the result - movies that Spielberg directed and Ford appeared in:
           showResults(spielbergMovieNames) ;

           //All done
   }//end main()

   public static void showResults(Collection<String> results) {
        Iterator resultsIterator = results.iterator() ;
        String result = "" ;
        while (resultsIterator.hasNext()) {
            result = (String) resultsIterator.next() ;
            System.out.println(result) ;
        }
   }//end showResults
}//end class