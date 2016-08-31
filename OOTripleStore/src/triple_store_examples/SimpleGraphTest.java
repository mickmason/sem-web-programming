package triple_store_examples;

/*
 * Main class for SimpleGraph tests
 */
import triple_store.simple_graph.SimpleGraph;
import java.util.* ;
import java.io.* ;
/**
 *
 * @author Michael
 */
public class SimpleGraphTest {
   public static void main(String[] args) {
        int choice = 0 ;
        String triple = "";

        String subject = "None" ;
        String predicate = "None" ;
        String object = "None" ;

        SimpleGraph simpleGraph = new SimpleGraph() ;

        String[] tokens = new String[3] ;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)) ;

        do {
            //do while choice != 10
            int count = 0 ;
            do {
                //Present menu
                if (count > 0) { System.out.println("That was not a valid choice. ") ; }
                System.out.println("1. Add a triple.") ;
                System.out.println("2. Remove a triple.") ;
                System.out.println("3. Make a query.") ;
                System.out.println("10. Quit.") ;
                System.out.print("Please chose an option: ") ;
                try {
                    choice = (int) Integer.parseInt(br.readLine()) ;
                } catch (IOException ioe) {
                    System.out.println("IO Exception") ;
                    System.out.println(ioe.getCause()) ;
                } catch (NumberFormatException nfe) {
                    System.out.println("Error - Invalid argument " + nfe.getMessage().toLowerCase() + ".") ;
                } finally {
                  count++ ;
                }
            } while(choice != 1 && choice != 2 && choice != 3 && choice !=10) ;

            switch (choice) {
                //what to do with choice
                case 1:
                    System.out.print("Please enter a triple delimited by \", \": ");
                    try {
                        triple = br.readLine();
                    } catch (IOException ioe) {
                        System.out.println(ioe.getCause()) ;
                        System.out.println("IOException thrown: " + ioe.getMessage()) ;
                    }
                    String delimiters = ", " ;
                    tokens = triple.split(delimiters) ;

                    subject = tokens[0] ;
                    predicate = tokens[1] ;
                    object = tokens[2] ;

                    System.out.println("Adding subject: " + tokens[0] + ", predicate: " + tokens[1] + ", object: " + tokens[2]) ;
                    simpleGraph.addTriple(subject, predicate, object) ;
                    break ;
                case 2: 
                    System.out.print("Please enter the triple you wish to remove: ") ;
                    try {
                        triple = br.readLine();
                    } catch (IOException ioe) {
                        System.out.println(ioe.getCause()) ;
                        System.out.println("IOException thrown: " + ioe.getMessage()) ;
                    }
                    delimiters = ", " ;
                    tokens = triple.split(delimiters) ;

                    subject = tokens[0] ;
                    predicate = tokens[1] ;
                    object = tokens[2] ;

                    System.out.println("Token[0]: " + tokens[0] + ", Token[1]: " + tokens[1] + ", Token[2]: " + tokens[2]) ;
                    simpleGraph.removeTriple(subject, predicate, object) ;
                    break ;
                case 3:
                    //Make a qlQuery
                   System.out.print("Please enter query terms: ");
                   try {
                        triple = br.readLine();
                    } catch (IOException ioe) {
                        System.out.println(ioe.getCause()) ;
                        System.out.println("IOException thrown: " + ioe.getMessage()) ;
                    } finally {
                    }
                    delimiters = ", " ;
                    tokens = triple.split(delimiters) ;

                    subject = tokens[0] ;
                    predicate = tokens[1] ;
                    object = tokens[2] ;

                    tokens = triple.split(delimiters) ;

                    subject = tokens[0] ;
                    predicate = tokens[1] ;
                    object = tokens[2] ;

                    System.out.println("Querying for: "+ tokens[0] + ", " + tokens[1] + ", " + tokens[2]) ;
                    //Make queries over triples
                        Set<String> results = (HashSet<String>) simpleGraph.queryTriples(subject, predicate, object) ;
                        Iterator resultsIterator = results.iterator() ;
                        while (resultsIterator.hasNext()) {
                            String result = "" ;
                            result = (String) resultsIterator.next() ;
                            System.out.println(result) ;
                            
                        }
                        System.out.println() ;
                    //end case 3: qlQuery triples
                    break ;
                case 10:
                    break ;
                default:
                    System.out.println("That was not a valid option.") ;
                    break;
           }//end switch (choice)

           count = 0 ;
           while (choice != 1 && choice != 2 && choice != 3 && choice != 10) {
                if (count > 0) { System.out.println("That was not a valid choice. ") ; }
                System.out.println("1. Add a triple.") ;
                System.out.println("2. Remove a triple.") ;
                System.out.println("3. Make a query.") ;
                System.out.println("10. Quit.") ;
                System.out.print("Please chose an option: ") ;
                try {
                    choice = (int) Integer.parseInt(br.readLine()) ;
                } catch (IOException ioe) {
                    System.out.println("IO Exception") ;
                    System.out.println(ioe.getCause()) ;
                } catch (NumberFormatException nfe) {
                    System.out.println("Error - Invalid argument " + nfe.getMessage().toLowerCase() + ".") ;
                } finally {
                    count++ ;
                }
           } 
        } while (choice != 10) ;

        try {
            br.close() ;
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage()) ;
        }
        System.out.println("Quitting. Bye...") ;
    }//end main()
}//end class