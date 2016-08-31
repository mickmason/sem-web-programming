/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package section_7_examples;
import com.hp.hpl.jena.rdf.model.* ;
import com.hp.hpl.jena.query.* ;
import java.io.IOException;
import java.util.Map ;
import java.util.HashMap ;
import java.util.Set ;
import java.util.List ;
import java.util.ArrayList ;
import java.net.SocketException ;
/**
 *
 * @author D06110699
 */
public class MusicArtistsReviewsEx6_2 {

    public static void main(String[] args) {
        //Country of interest:
        final String country = "Ireland" ;

        //The DBpedia SPARQL endpoint:
        //final String dBpediaEndpoint = "http://dbpedia.org/sparql?default-graph-uri=http%3A//dbpedia.org&query" ;
        final String dBpediaEndpoint = "http://dbpedia.org/sparql" ;
        //Set of namespace uris to use
        final String owl = "http://www.w3.org/2002/07/owl#" ;
        final String fb = "http://rdf.freebase.com/ns/" ;
        final String foaf = "http://xmlns.com/foaf/0.1/" ;
        final String rev = "http://purl.org/stuff/rev#" ;
        final String dc = "http://purl.org/dc/elements/1.1/" ;
        final String rdfs = "http://www.w3.org/2000/01/rdf-schema#" ;

        //Create a Map of these when the graph is created first, these can be added as namespace mappings
        Map<String, String> namespaces = new HashMap<String, String>() ;
        namespaces.put("owl", owl) ;
        namespaces.put("fb", fb) ;
        namespaces.put("foaf", foaf) ;
        namespaces.put("rev", rev) ;
        namespaces.put("dc", dc) ;
        namespaces.put("rdfs", rdfs) ;

        /*
         *  The first query is to DBpedia.
         *  Use the country value to get a list of rock bands from that country
         *  Then use the CONSTRUCT to save the mappings between the DBpedia id for each band and the Freebase ID for that band
         *  This is given in the DBpedia info as an owl:sameAs property on the DBpedia band ID
        */
        String dBpediaQueryStr = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                                 + "PREFIX owl: <" +owl+ ">"
                                 + "PREFIX dbpp: <http://dbpedia.org/property/>"
                                 + "PREFIX dbpo: <http://dbpedia.org/ontology/>"
                                 + "PREFIX dbpr: <http://dbpedia.org/resource/>"

                                 //Construct a graph from the query solutions:
                                 + "CONSTRUCT {"
                                    + " ?band owl:sameAs ?link "
                                 + "} WHERE {"
                                    + " ?loc dbpp:commonName \"" + country + "\"@en . "
                                    + " ?band dbpo:hometown ?loc . "
                                    + " ?band rdf:type dbpo:MusicalArtist . "
                                    + " ?band owl:sameAs ?link . "
                                    + " FILTER regex(?link, \"freebase\")"
                                 + "} LIMIT 10" ;

        Query dBQuery = QueryFactory.create(dBpediaQueryStr) ;
        QueryExecution dBQueryExec = QueryExecutionFactory.sparqlService(dBpediaEndpoint, dBQuery) ;

        Model musicModel = ModelFactory.createDefaultModel() ;
        //Add namespace prefixes to music graph
        Set<String> namespaceKeys = namespaces.keySet() ;
        for (String namespaceKey: namespaceKeys) {
            musicModel.setNsPrefix(namespaceKey, namespaces.get(namespaceKey)) ;
        }
        System.out.println("Collecting DBpedia info...") ;
        musicModel.add(dBQueryExec.execConstruct()) ;
        System.out.println("DBpedia info collected and added to graph.") ;
        System.out.println("Graph size: " + musicModel.size() + " statements.") ;
        /*
            musicModel now contains mappings between DBpedia IDs for Irish bands and Freebase IDs for those bands
            This is an example of one of the triples contained in the graph:

            <rdf:Description rdf:about="http://dbpedia.org/resource/Christy_Dignam">
                <owl:sameAs rdf:resource="http://rdf.freebase.com/ns/m/0f6g1b"/>
            </rdf:Description>
         *
         *  The next stage is to get the rest of the info from Freebase.
            First, strip out the Freebase IDs for each artist, then dereference the Freebase IDs, adding the info returned to the
         *  musicModel
         */
        //musicModel.write(System.out) ;

        String fbIDsQueryStr =  "PREFIX owl: <" + owl + ">"
                                + "SELECT ?fbid "
                                + "WHERE { "
                                + " ?dbid owl:sameAs ?fbid . "
                                + "} " ;

        QueryExecution qx = QueryExecutionFactory.create(fbIDsQueryStr, musicModel) ;
        ResultSet results = qx.execSelect() ;
        //List of Freebase IDs
        List<String> resultsList = new ArrayList<String>() ;
        while (results.hasNext()) {
            QuerySolution sol = results.nextSolution() ;
            System.out.println(sol.get("?fbid").toString()) ;
            resultsList.add(sol.get("?fbid").toString()) ;
        }
        Model musModelTemp = ModelFactory.createDefaultModel() ;
        System.out.println("Collecting Freebase info...") ;
        for (String fbId: resultsList) {
            try {
                musicModel.read(fbId) ;
            } catch (Exception e) {
                System.out.println(fbId) ;
            }
            
        }

        //musModelTemp.read("http://rdf.freebase.com/ns/m/01rq9_c") ;
        //musicModel = musModelTemp ;
        System.out.println("Freebase info collected and added to graph.") ;
        System.out.println("Graph size: " + musicModel.size() + " statements.") ;
        //musicModel.write(System.out) ;

        /*
            Now have information for all bands for which we had a Freebase Id
         *  Get bbc Ids from the graph for each band - should be an owl:sameAs property that contains "bbc" in its value
         *  Then use that to get all artist info for each artist from, BBC.
         */
         //Get the BBC Id value for the artist
        String bbcIdsQueryStr = "PREFIX owl: <" + owl + ">"
                                + "PREFIX fb: <" + fb + ">"
                                + " SELECT ?bbcid "
                                + "WHERE {"
                                    + " ?artist owl:sameAs ?bbcid . "
                                    + " FILTER regex(str(?bbcid), \"bbc\", \"i\" ) "
                                + "}" ;
        qx = QueryExecutionFactory.create(bbcIdsQueryStr, musicModel) ;
        try {
            results = qx.execSelect() ;
            resultsList = new ArrayList<String>() ;
            while (results.hasNext()) {
                //Add each query result to a list - BBC Id
                resultsList.add(results.next().get("?bbcid").toString());
            }
            System.out.println("Collecting bbc info...") ;
            //musModelTemp = ModelFactory.createDefaultModel() ;
            for (String result: resultsList) {
                //For each BBC Id, add the info at the BBC site for that Id to the graph
                musicModel.read(result) ;
            }
            //musicModel = musModelTemp ;
        } finally { qx.close() ; }

        System.out.println("BBC info collected.") ;
        System.out.println("Graph size: " + musicModel.size() + " statements.") ;
        //musicModel.write(System.out) ;
        /*
         *  Next get the reviews (which are available) for each release for each artist from the bbc data now in the graph.
         *  NOTE: The BBC data is much better curated than that in DBpedia and Freebase.
         *
        */
        
        String bbcReviewQueryStr =  "PREFIX foaf: <" + foaf + ">"
                                    + "PREFIX dc: <" + dc + ">"
                                    + "PREFIX rev: <" + rev + ">"
                                    + "SELECT ?reviewUrl "
                                    + "WHERE {"
                                        + " ?artist foaf:made ?r . "
                                        + " ?r rev:hasReview ?reviewUrl "
                                    + "}" ;

        qx = QueryExecutionFactory.create(bbcReviewQueryStr, musicModel) ;
        try {
            results = qx.execSelect() ;
            //Add the reviews to a list, then add each into the graph:
            resultsList = new ArrayList<String>() ;
            while (results.hasNext()) {
                resultsList.add(results.nextSolution().get("?reviewUrl").toString());
            }
        } finally { qx.close() ; }

        //Get the review info from BBC and add it to the graph
        System.out.println("Collecting artists' release review info from BBC...") ;
        for (String result: resultsList) {
            musicModel.read(result) ;
        }
        System.out.println("Release review info add to graph. Graph size now " + musicModel.size() + " statements.") ;
        musicModel.write(System.out) ;

        /*
            Final query. Extract an artist name, a release title and review text for each release that artist has made
         *  where a review has been added.
         *
         */
         String finalQueryStr =  "PREFIX fb: <" + fb + ">"
                                 + "PREFIX owl: <" +owl+ ">"
                                 + "PREFIX dc: <" + dc + ">"
                                 + "PREFIX foaf: <" + foaf + ">"
                                 + "PREFIX rev: <" + rev + ">"
                                 + ""
                                 + " SELECT ?artistname ?title ?revtext "
                                 + "WHERE {"
                                    + "?fbartist fb:type.object.name ?artistname ."
                                    + "?fbartist owl:sameAs ?bbcartists ."
                                    + "?bbcartists foaf:made ?bbcrelease . "
                                    + "?bbcrelease dc:title ?title . "
                                    + "?bbcrelease rev:hasReview ?rev . "
                                    + "?rev rev:text ?revtext . "
                                    + "FILTER ( lang(?artistname) = \"en\" ) "
                                 + "}" ;
        qx = QueryExecutionFactory.create(finalQueryStr, musicModel) ;
        System.out.println() ;
        System.out.println("Artist release and review informaton for artists from " + country + ".") ;
        System.out.println("---------------------------------------------------------------------------------") ;
       try {
            results = qx.execSelect() ;
            while (results.hasNext()) {
                QuerySolution sol = results.nextSolution() ;
                System.out.print("Artist: " + sol.get("?artistname").toString()) ;
                System.out.println("\tRelease: " + sol.get("?title").toString()) ;
                System.out.print("Review: ") ;
                System.out.println(sol.get("?revtext").toString()) ;
                System.out.println("---------------------------------------------------------------------------------") ;
            }
        } finally { qx.close() ; }  

        
        
    }//end main()
}//end class