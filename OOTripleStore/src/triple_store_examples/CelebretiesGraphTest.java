package triple_store_examples;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import triple_store.inference_rules.InferenceRule;
import triple_store.inference_rules.EnemyRule;
import triple_store.graph_utils.GraphUtil;
import triple_store.simple_graph.SimpleGraph;
import java.util.* ;

/**
 *
 * @author Michael
 */
public class CelebretiesGraphTest {
    public static void main(String[] args) {
        SimpleGraph celebsGraph = new SimpleGraph() ;
        String fileName = "C:\\Users\\Michael\\DT249\\year_4\\final_year_project\\semweb_programming\\celeb_triples.txt" ;

        if (celebsGraph.readTriples(fileName)) {
            System.out.println("Triples read from " + fileName + " ok.") ;
        } else {
            System.out.println("Problem reading from " + fileName + ".") ;
            System.exit(1);
        }

        //Get all relationships involving Justin Timberlake
        System.out.println() ;
        System.out.println("Get all relationships involving Justin Timberlake:") ;
        //Get relationship ids for all his relationships
        Set<String> justTimbRelTriples = (HashSet<String>) celebsGraph.queryTriples("None", "with", "Justin Timberlake") ;
        Set<String> justTimbRelIds = new TreeSet<String>() ;

        //For each triple in the set, get its id
        for (String relTriple: justTimbRelTriples) {
            GraphUtil.placeTripleTokens = relTriple.split(GraphUtil.delimiter) ;
            justTimbRelIds.add(GraphUtil.placeTripleTokens[0]) ;
        }
        System.out.println("Relationship ids involving Justin Timberlake:") ;
        System.out.println(justTimbRelIds) ;

        Set<String> justTimbRelNamesTriples = new TreeSet<String> () ;
        //for each relId
        for (String relId: justTimbRelIds) {
            System.out.print("Relationship " + relId + ": ") ;
            //Get the triples associated with that relId (there will be 2)
            justTimbRelNamesTriples = celebsGraph.queryTriples(relId, "with", "None") ;
            int count = 0 ;
            //For each triple in that set
            for (String jtRelNamesTriple: justTimbRelNamesTriples) {
                GraphUtil.placeTripleTokens = jtRelNamesTriple.split(GraphUtil.delimiter) ;
                if (count == 0) {
                    //Print out the name in the triple:
                    System.out.print(GraphUtil.placeTripleTokens[2] + ", ") ;
                    count = 1 ;
                } else {
                    //Print out the name in the triple:
                    System.out.print(GraphUtil.placeTripleTokens[2] + ".") ;
                    count = 0 ;
                }
            }
            System.out.println() ;
        }

        //Celebreties who have dated more than one actor
        System.out.println() ;
        System.out.println("Celebreties who have dated more than one actor:") ;
        Set<String> celebsWhoDatedMoreThanOneActor = new HashSet<String>() ;
        //for each celebrity in a relationship, find out if two or more of thier partners has starred in a film
        //First get all relationship triples
        Set<String> relationshipTriplesAll = celebsGraph.queryTriples("None", "with", "None") ;
        //For each triple
        for (String relTripleAll: relationshipTriplesAll) {
            GraphUtil.placeTripleTokens = relTripleAll.split(GraphUtil.delimiter) ;
            //Get the relationshipId involved
            String relId = GraphUtil.placeTripleTokens[0] ;
            //Get all the relationship triples for that id - should be 2
            Set<String> celebRelsTriples = celebsGraph.queryTriples(relId, "with", "None") ;
            //System.out.println(celebRelsTriples) ;
            //For each of these
            for (String celebRelsTriple: celebRelsTriples) {
                //Get the celeb involved
                GraphUtil.placeTripleTokens = celebRelsTriple.split(GraphUtil.delimiter) ;
                String celeb = GraphUtil.placeTripleTokens[2] ;
                //And get all of the relationships they have been involved in
                Set<String> celebsRelsTriples  = celebsGraph.queryTriples("None", "with", celeb) ;
                //For each of these get the id and the other person involved
                //Need a counter for the number of partners celb has had who have appeared in movies
                int counter = 0 ;
                for (String celebsRelsTriple: celebsRelsTriples) {
                    GraphUtil.placeTripleTokens = celebsRelsTriple.split(GraphUtil.delimiter) ;
                    //And get each triple for that id - should be 2 again
                    Set<String> relPartnersTriples = celebsGraph.queryTriples(GraphUtil.placeTripleTokens[0], "with", "None") ;
                    //For each of these
                    for (String relPartnersTriple: relPartnersTriples) {
                        //Get the other person involved
                        GraphUtil.placeTripleTokens = relPartnersTriple.split(GraphUtil.delimiter) ;
                        //Make sure we are not talking about the celeb
                        if (!GraphUtil.placeTripleTokens[2].equals(celeb)) {
                            //Get all movies the partner has been in
                            Set<String> partnerMovies = celebsGraph.queryTriples(GraphUtil.placeTripleTokens[2], "starred_in", "None") ;
                            if (!partnerMovies.isEmpty()) {
                                //Increment the counter
                                counter++ ;
                                if (counter > 3) {
                                    //If counter is 4 or more - there are two relationship triples for each relationship - add the celebrity
                                    celebsWhoDatedMoreThanOneActor.add(celeb) ;
                                }
                            }
                        }
                    }
                }
                //celebsWhoDatedMoreThanOneActor.add(GraphUtil.placeTripleTokens[2]) ;

            }
        }
        System.out.println(celebsWhoDatedMoreThanOneActor.size() + " matches to the query:") ;
        GraphUtil.showResults(celebsWhoDatedMoreThanOneActor);
        
        //Find all musicians who have also had a stint in rehab
        System.out.println() ;
        System.out.print("Musicains who have had a stint in rehab - ") ;
        //All musician triples
        Set<String> musicainTriples = (HashSet<String>) celebsGraph.queryTriples("None", "released_album", "None") ;
        Set<String> rehabMusicains = new HashSet<String>() ;
        for (String musicianTriple: musicainTriples) {
            GraphUtil.placeTripleTokens = musicianTriple.split(GraphUtil.delimiter) ;
            String musician = GraphUtil.placeTripleTokens[0] ;
            Set<String> rehabStints = (HashSet<String>) celebsGraph.queryTriples("None", "person", "None") ;
            for (String rehabStint: rehabStints) {
                GraphUtil.placeTripleTokens = rehabStint.split(GraphUtil.delimiter) ;
                if (GraphUtil.placeTripleTokens[2].equals(musician) && !rehabMusicains.contains(musician)) {
                    rehabMusicains.add(musician) ;
                }
            }
        }
        //Show the results
        System.out.println(rehabMusicains.size() + " matches: ") ;
        GraphUtil.showResults(rehabMusicains) ;

        /*
         *  Execute a rule to find the enemies of celebrities partners.
         *  That means, if the celeb has an enemy, then that person is also the enemy of thier partner
         */
        System.out.println() ;
        Set<String> currentEnemyTriples = celebsGraph.queryTriples("None", "enemy", "None") ;
        System.out.println(currentEnemyTriples.size() + " enemy triples before EnemyRule:") ;
        for (String currentEnemyTriple: currentEnemyTriples) {
            System.out.println(currentEnemyTriple) ;
        }
        System.out.println() ;
        InferenceRule enemyRule = new EnemyRule() ;
        celebsGraph.applyInferenceRule(enemyRule);
        Set<String> newTriples = celebsGraph.queryTriples("None", "enemy", "None") ;
        System.out.println(newTriples.size() + " enemy triples after EnemyRule:") ;
        for (String newTriple: newTriples) {
            System.out.println(newTriple) ;
        }         
    }//end main()
}//end class
