/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package section_7_examples;

import com.hp.hpl.jena.query.* ;
import com.hp.hpl.jena.rdf.model.* ;
import com.hp.hpl.jena.util.FileManager ;
import com.hp.hpl.jena.vocabulary.RDFS ;
import com.hp.hpl.jena.sparql.vocabulary.FOAF ;
import java.io.* ;
import java.util.* ;
/**
 *
 * @author Michael
 */
public class FOAFGraphCrawlerEx6_1 {
    public static void main(String[] args) {
        String startUri = "http://api.hi5.com/rest/profile/foaf/358280494" ;
        final int steps = 2 ;
        
        String FOAFNs = "<http://xmlns.com/foaf/0.1/>" ;
        String RDFSNs = "<http://www.w3.org/2000/01/rdf-schema#>" ;

        Set<String> current = new HashSet<String>() ;
        Set<String> visited = new HashSet<String>() ;

        Model FOAFModel = ModelFactory.createDefaultModel() ;

        current.add(startUri) ;
        int i = 0 ;
        
        while (i < steps) {
            for (String uri: current) {
                visited.add(uri) ;

                Set<String> nextStep = new HashSet<String>();

                Model tempFOAFModel = ModelFactory.createDefaultModel() ;
                InputStream in = FileManager.get().open(uri) ;
                RDFReader reader = tempFOAFModel.getReader("RDF/XML") ;
                reader.read(tempFOAFModel, in, null) ;
//                tempFOAFModel.write(System.out) ;

                //get all seeAlso triples for Toby
                StmtIterator iter = tempFOAFModel.listStatements(null, RDFS.seeAlso, (RDFNode) null) ;
                Map<String, String> blankNodesToURIsMap = new HashMap<String, String>() ;

                //For each blank node in the graph (FOAF Persons in the graph including Toby)
                while (iter.hasNext()) {
                    Statement stmt = iter.nextStatement() ;
                    //Map the blank node id to the URI for the person
                    blankNodesToURIsMap.put(stmt.getSubject().toString(), stmt.getObject().toString()) ;
                }
                //Get the root node - img property or "me" node - represents the Person who is the subject of the graph
                iter = tempFOAFModel.listStatements(null, FOAF.img, (RDFNode) null) ;
                if (iter.hasNext()) {
                    while (iter.hasNext()) {
                        Statement stmt = iter.nextStatement() ;
                        //Add the blank node that has the img property to the blank nodes map. It's value is the current uri - ie the current subject of the FOAF graph
                        blankNodesToURIsMap.put(stmt.getSubject().toString(), uri) ;
                    }
                //if there is no image node, add a mapping for a node with an ID of "me".
                } else {
                    blankNodesToURIsMap.put("", uri) ;
                    blankNodesToURIsMap.put("me", uri) ;
                }

                //Rename blank nodes as their seeAlso URIs and add them to the FOAF graph.
                //Get all statements from the model
                iter = tempFOAFModel.listStatements() ;
                //For each subject
                Resource s = FOAFModel.createResource() ;
                //For each object
                RDFNode o = FOAFModel.createResource() ;

                while (iter.hasNext()) {
                    Statement stmt = iter.nextStatement() ;
                    if (blankNodesToURIsMap.containsKey(stmt.getSubject().toString())) {
                        //If the subject of the current statement is in the blank nodes map, initalise the new subject resource to the URI value for that blank node
                        s = FOAFModel.createResource(blankNodesToURIsMap.get(stmt.getSubject().toString())) ;
                    } else {
                        //Else, the new resource is assigned the URI of the current node in the current graph
                        s= FOAFModel.createResource(stmt.getSubject().toString()) ;
                    }
                    //Do similar for the object values for each statement in the current graph
                    if (blankNodesToURIsMap.containsKey(stmt.getObject().toString())) {
                        o = FOAFModel.createResource(blankNodesToURIsMap.get(stmt.getObject().toString())) ;
                    } else {
                        if (stmt.getObject().isURIResource()) {
                            o = FOAFModel.createResource(stmt.getObject().toString()) ;
                        } else if (stmt.getObject().isLiteral()){
                            o = FOAFModel.createLiteral(stmt.getObject().toString()) ;
                        }
                    }
                    //Need to get namespaces used in the input graph and add them to the FOAFGraph
                    /*
                        A more robust way of doing this is to get all the namespace prefix mappings from the input graph
                     *  and add them to the FOAFGraph if it does not already contain them. 
                     */
                    String ns = stmt.getPredicate().getNameSpace() ;
                    if (ns.equals("http://xmlns.com/foaf/0.1/")) {
                        FOAFModel.setNsPrefix("foaf", ns) ;
                    } else if (ns.equals("http://purl.org/net/inkel/rdf/schemas/lang/1.1#")) {
                        FOAFModel.setNsPrefix("lang", ns) ;
                    }
                    Property p  = FOAFModel.createProperty(stmt.getPredicate().toString());
                    FOAFModel.add(s, p, o) ;
                }

                //tempFOAFModel.write(System.out) ;
                String graphQuery = "PREFIX foaf:" + FOAFNs
                                    + "PREFIX rdfs:" + RDFSNs
                                    + "SELECT ?seeAlsoUrl "
                                    + "WHERE {"
                                    + "?a foaf:knows ?b ."
                                    + "?b rdfs:seeAlso ?seeAlsoUrl "
                                    + "}" ;
                QueryExecution seeAlsoURLsExec = QueryExecutionFactory.create(graphQuery, tempFOAFModel) ;
                ResultSet results = seeAlsoURLsExec.execSelect() ;

                try {
                    if (!results.hasNext()) {
                        System.out.println("No results...") ;
                        System.exit(1);
                    }
                    while (results.hasNext()) {
                        QuerySolution solution = results.nextSolution() ;
                        
                        String seeAlsoUri = "" ;
                        seeAlsoUri = solution.get("?seeAlsoUrl").toString() ;
                        if (!visited.contains(seeAlsoUri) && !nextStep.contains(seeAlsoUri)) {
                            nextStep.add(seeAlsoUri) ;
                        }
                    }
                } finally {
                    seeAlsoURLsExec.close() ;
                }
                current = nextStep ;
            }//end for uri in current
            i++ ;
        }//end while i in step
        
        FOAFModel.write(System.out) ;
        String knowsQueryStr = "PREFIX foaf:" + FOAFNs
                                        + "SELECT ?nick ?knows "
                                        + "WHERE {"
                                        + "?a foaf:nick ?nick ."
                                        + "?a foaf:knows ?b ."
                                        + "?b foaf:nick ?knows "
                                        + "}" ;
        QueryExecution seeAlsoURLsExec = QueryExecutionFactory.create(knowsQueryStr, FOAFModel) ;
        ResultSet results = seeAlsoURLsExec.execSelect();
        if (!results.hasNext()) {
            System.out.println("No results") ;
        }
        while (results.hasNext()) {
            QuerySolution solution = results.nextSolution() ;
            System.out.println(solution.get("?nick").toString() + " knows " + solution.get("?knows").toString() + ".");
        }
    }//end main()
}//end class
