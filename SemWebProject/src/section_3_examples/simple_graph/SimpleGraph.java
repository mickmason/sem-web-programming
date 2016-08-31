package section_3_examples.simple_graph;
import section_3_examples.graph_utils.GraphUtil;
import section_3_examples.graph_utils.TripleReader;
import section_3_examples.graph_utils.* ;

/**
 * This class represents the runtime triplets that can exist in a triplet store and which refer to one subject. One predicate can be used and many object can be provided.
 * It provides three Map objects each one representing a permutation of subject-predicate-object entities. Map objects are implemented as HashMaps. 
 * Each HashMap is keyed off a string onto a Map which is keyed off a String onto a Set of Strings. The Set of strings controls duplicates.
 *
 * Code index:
 * Constructor - lines 30 - 38.
 *
 * ADD TRIPLE METHODS:
 * --------------------
 * addTriple() - lines 47 - 51.
 * Add triple permutations - lines 57 - 144.
 *      addSpo() - 59 - 88.
 *      addPos() - 90 - 118.
 *      addOsp() - 121 - 147.
 *
 * REMOVE TRIPLE METHODS:
 * -----------------------
 * removeTriple() - 160 - 164.
 * Remove tripe permutations - 169 - 194.
 *      removeSpo() - 170 - 182.
 *      removePos() - 184 - 193.
 *      removeOsp() - 195 - 197.
 *
 * QUERY METHOD:
 * --------------
 * Based on the example given in Phython for same in Chapter 2 of Programming the Semantic Web by Segaran, Taylor and Evans
 *
 * @author D06110699
 */

import section_3_examples.inference_rules.InferenceRule;
import java.util.* ;
public class SimpleGraph {
     
    /* Subject/object/predicate permutations: */
    //subject-predicate-object
    private Map<String, Map> spo ;
    //predicate-object-subject
    private Map<String, Map> pos ;
    //object-predicate-subject
    private Map<String, Map> osp ;
    /*
        Constructior. On graph initialisation, create new HashMaps for each of the subject/object/predicate perms
        Note that the value for each HashMap is a HashMap.
        So, for example, for the spo triple, the key in the mapping is the subject and the value is a mapping of the predicate to the object
        And so on for the others.
     */
    public SimpleGraph() {
        /* Intitialise HashMaps */
        //Subject-predicate-object index:
        this.spo = new HashMap<String, Map>() ;
        //predicate-object-subject index:
        this.pos = new HashMap<String, Map>()  ;
        //object-predicate-subject index:
        this.osp = new HashMap<String, Map>() ;
    }//end constructor

    /* 
     *  Triple adder method.
     *  Adds triples to the graph.
     *  It's adds triple in each permutation of subject, predicate and object that is given -> spo, pos, osp buy calling addSpo(), addPos(), addOsp()
    */
    //Add a triple
    public void addTriple(String subject, String predicate, String object) {
        addSpo(subject, predicate, object) ;
        addPos(predicate, object, subject) ;
        addOsp(object, subject, predicate) ;
    }//end addTriple()

    /* 
        Add triples in each permutation of subject, predicate and object
    */
    //Add spo:
    private void addSpo(String sub, String pred, String obj) {
        /* Add the spo permutaion */
        if (!spo.containsKey(sub)) {
            //Subject does not exist in the graph
            Map<String, Set> po = new HashMap<String, Set>() ;
            Set<String> o = new HashSet<String>() ;
            o.add(obj) ;
            po.put(pred, o) ;
            spo.put(sub, po) ;
        } else {
            //Subject exists in the set
            Map<String, Set> po = (HashMap) spo.get(sub) ;
            if (!po.containsKey(pred)) {
                //Predicate does not exist in the Map keyed off the subject
                Set<String> o = new HashSet<String>() ;
                o.add(obj) ;
                po.put(pred, o) ;
                spo.put(sub, po) ;
            } else {
                //Predicate exists in the predicte Map
                Set<String> o = (HashSet) po.get(pred) ;
                if (!o.contains(obj)) {
                    //The object is not in the predicate Set
                    o.add(obj) ;
                    po.put(pred, o) ;
                    spo.put(sub, po) ;
                }
            }
        }
    }//end addSpo()
    private void addPos(String pred, String obj, String sub) {
        /* Add the pos permutaion */
        Map<String, Set> os = new HashMap<String, Set>() ;
        Set<String> s = new HashSet<String>() ;
        if (!pos.containsKey(pred)) {
            //Predicate does not exist in the graph
            s.add(sub) ;
            os.put(obj, s) ;
            pos.put(pred, os) ;
        } else {
            //Predicate exists in the graph so get the object-subject mappings mapped off the predicate and check if the object exists there
            os = pos.get(pred) ;
            if (!os.containsKey(obj)) {
                //Object does not exist in the o-s map, add the subject to the set and put it all back in pos.
                s.add(sub) ;
                os.put(obj, s) ;
                pos.put(pred, os) ;
            } else {
                s = (HashSet) os.get(obj) ;
                //Object exists in the object Map
                if (!s.contains(sub)) {
                    //The object is not in the predicate Set
                    s.add(sub) ;
                    os.put(obj, s) ;
                    pos.put(pred, os) ;
                }
            }
        }
    }//end addPos()
    private void addOsp(String obj, String sub, String pred) {
        /* Add the osp permutaion */
        Map<String, Set> sp = new HashMap<String, Set>() ;
        Set<String> p =  new HashSet<String>() ;
        if (!osp.containsKey(obj)) {
            //Object does not exist in the graph
            p.add(pred) ;
            sp.put(sub, p) ;
            osp.put(obj, sp) ;
        } else {
            sp = (HashMap) osp.get(obj) ;
            //Object exists in the graph
            if (!sp.containsKey(sub)) {
                //Subject does not exist in the Map keyed off the subject
                p.add(pred) ;
                sp.put(sub, p) ;
                osp.put(obj, sp) ;
            } else {
                //Subject exists in the Map
                p = (HashSet) sp.get(sub) ;
                if (!p.contains(pred)) {
                    //The object is not in the predicate Set
                    p.add(pred) ;
                    sp.put(sub, p) ;
                    osp.put(obj, sp) ;
                }
            }
        }//end add osp
    }//end addOsp()

    /*
     * Triple remover. Makes a call to methods which remove triples based on arguments to the method
     * Makes call to remover methods for each permutation of subject predicate and object
     */
    public void removeTriple(String subject, String predicate, String object)  {
        removeSpo(subject, predicate, object) ;
        removePos(predicate, object) ;
        removeOsp(object) ;
    }//end removeTriple()
    /*
     * Removers for each permutation of subject, predicate and object
     */
    private void removeSpo(String sub, String pred, String obj) {
        /* Removes and spo triple based on args passed for each */
        //Get the pred-obj mapping for the subject
        Map<String, Set> po = (HashMap<String, Set>) spo.get(sub) ;
        if (po == null) {
            System.out.println("Sorry, triple " + sub + ", " + pred + ", " + obj + " not found") ;
        }
        //Get the set of objects mapped to the predicate
        Set<String> o = (HashSet<String>) po.get(pred) ;
        //remove the object
        o.remove(obj) ;
        //Check now for an empty object set, remove the whole triple if the set is empty
        if (o.isEmpty()) {
            spo.remove(sub) ;
        }
    }//end removeSpo()
    private void removePos(String pred, String obj) {
        /* Removes the pos permutation for the triple */
        //Get the obj-sub mappings for the predicate
        Map<String, Set> os = (HashMap<String, Set>) pos.get(pred) ;
        //Simply remove the object-subject mapping
        os.remove(obj) ;
        if (os.isEmpty()) {
            pos.remove(pred) ;
        }
    }//end removePos()s
    private void removeOsp(String obj) {
        osp.remove(obj) ;
    }//end removeOsp()

    /*  Query method */
    public Set<String> queryTriples(String sub, String pred, String obj) {
        Set<String> results = new HashSet<String>() ;
        if (!sub.equals("None")) {
            //Subject given
            //Get out the pred-obj Map mapped to the subject
            Map<String, Set> po = (HashMap<String, Set>) spo.get(sub) ;
            if (po == null) { return results ; }
            if (!pred.equals("None")) {
                //subject, predicate given, find object
                //And the object set for that predicate
                Set<String> o = (HashSet) po.get(pred) ;
                if (o == null) { return results ; }
                if (!obj.equals("None")) {
                    //sub pred obj
                    //Object is given too so just return the request if the object set contains the requested object ie su, pred and object are present a
                    String result = sub + "," + pred + "," ;
                    if (o.contains(obj)) {
                        //If the set contains the requested object add it to the result List
                        result = result + obj ;
                    } else { return results ; }
                    results.add(result) ;
                } else {
                    //sub pred "None"
                    //Object value is not given so iterate over the object set and add each value to the result List
                    Iterator objIterator = o.iterator() ;
                    while (objIterator.hasNext()) {
                        String result = sub + "," + pred + "," + (String) objIterator.next() ;
                        results.add(result);
                    }
                }//end if obj != "None"
            } else {
                //Predicate is not given sub None ...
                if (!obj.equals("None")) {
                    //sub "None" obj
                    //Object is given so find the predicates that match the object and subject given.
                    //Looking for the predicate so use osp
                    Map<String, Set> sp = (HashMap) osp.get(obj) ;
                    if (sp == null) { return results ; }
                    //And the predicate set
                    Set<String> p = (HashSet) sp.get(sub) ;
                    if (p == null) { return results ; }
                        Iterator predIterator = p.iterator() ;
                        while (predIterator.hasNext()) {
                            //Object is given so find the
                            String result = sub + "," + (String) predIterator.next() + "," + obj ;
                            results.add(result) ;
                    }
                } else {
                   //sub "None" "None"
                   //Looking for ALL predicate-object maps for the subject:
                   //Get the entrySet for these from the po.map returned previously from the spo index
                   Set poMappings = po.entrySet() ;
                   //Iterate over the entries
                   Iterator poMappingsIterator = poMappings.iterator() ;
                   while (poMappingsIterator.hasNext()) {
                        //For each entry get the key value (pred) and add it to the result array along with the subject
                        Map.Entry<String, Set> poEntry = (Map.Entry<String, Set>) poMappingsIterator.next() ;
                        String result = sub + "," + poEntry.getKey() + ",";
                        //get the value set of objects as a Set of Strings
                        Set<String> objectSet = (HashSet) poEntry.getValue() ;
                        //Then get each object string and add it to the current result and add the result to the results List
                        Iterator objectSetIterator = objectSet.iterator() ;
                        while (objectSetIterator.hasNext()) {
                            result = result + (String) objectSetIterator.next() ;
                        }
                        results.add(result);
                   }
                }//end if sub "None" "None"
            }//end if pred != "None"
        //end if sub != "None"
        } else {
            //sub.equals("None")
            if (!pred.equals("None")) {
                //sub not given, predicate given 
                //Will use the pos index from here
                Map<String, Map> os = pos.get(pred) ;
                if (os == null) { return results ; }
                if (!obj.equals("None")) {
                    //object is given so, looking for the subjects in the pos index - None obj pred
                    Set<String> s = (HashSet<String>) os.get(obj) ;
                    if (s == null) { return results ; }
                    //Iterate over the set of subjects and return a result for each
                    Iterator subjIterator = s.iterator() ;
                    while (subjIterator.hasNext()) {
                        String result = (String) subjIterator.next() + "," + pred + "," + obj ;
                        results.add(result) ;
                    }
                } else {
                    //None pred None: Predicate given, subject and object are not
                    //Need all obj-sub maps for the given predicate:
                    Set objSubjectMaps = os.entrySet() ;
                    //Iterate over these Map entries, get each entry and extract its key and the set of subjects associated with each
                    Iterator objSubjIterator = objSubjectMaps.iterator() ;
                    while (objSubjIterator.hasNext()) {
                        Map.Entry<String, Set> obSubEntry = (Map.Entry<String, Set>) objSubjIterator.next() ;
                        Set<String> subjects = (HashSet<String>) obSubEntry.getValue() ;
                        Iterator subjectsIterator = subjects.iterator() ;
                        while (subjectsIterator.hasNext()) {
                            String result = (String) subjectsIterator.next() + "," + pred + "," + obSubEntry.getKey() ;
                            results.add(result) ;
                        }
                    }
                }
            } else {
                //Subject not given, predicate not given - None None ...
                //Need to work on the osp index:
                Map<String, Set> sp = osp.get(obj) ;
                if (!obj.equals("None")) {
                    //None None obj
                    //Need all entries in the sp map
                    Set subPred = sp.entrySet() ;
                    Iterator subPredIterator = subPred.iterator() ;
                    while (subPredIterator.hasNext()) {
                        Map.Entry<String, Set> subPredEntry = (Map.Entry<String, Set>) subPredIterator.next() ;
                        Set<String> predicates = subPredEntry.getValue() ;
                        Iterator predsIterator = predicates.iterator() ;
                        while (predsIterator.hasNext()) {
                            String result = subPredEntry.getKey() + "," + (String) predsIterator.next() + ",";
                            if (osp.containsKey(obj)) {
                                result = result + obj ;
                            }
                            results.add(result);
                        }
                    }
                } else {
                    //None None None
                    //Get all entries:
                    Set spoEntries = spo.entrySet() ;
                    //Get an iterator over them:
                    Iterator spoEntriesIterator = spoEntries.iterator() ;
                    while (spoEntriesIterator.hasNext()) {
                        //Get each entry
                        Map.Entry<String, Map> spoEntry = (Map.Entry<String, Map>) spoEntriesIterator.next() ;
                        //Get the po Map for each:
                        Map<String, Set> poMap = (HashMap<String, Set>) spoEntry.getValue() ;
                        //Get the entries for that and an iterator over them:
                        Set poEntries = poMap.entrySet() ;
                        Iterator poEntriesIterator = poEntries.iterator() ;
                        //for each of them, get the key and value and add them and the spoEntry key to a result String[]
                        while (poEntriesIterator.hasNext()) {
                            Map.Entry<String, Set> poEntry = (Map.Entry<String, Set>) poEntriesIterator.next() ;
                            Set<String> objects = (HashSet<String>) poEntry.getValue() ;
                            Iterator objectsIterator = objects.iterator() ;
                            while (objectsIterator.hasNext()) {
                                String result = spoEntry.getKey() + "," + poEntry.getKey()  + "," + (String) objectsIterator.next() ;
                                results.add(result);
                            }
                        }
                    }
                }//end if else obj != None
            }//end if else  pred != None
        }
        return results ;
    }//end queryTriples()

    //This is a convienience method for queries where two argument values are given and it returns the missing value - sub pred or obj
    public String singleValueQuery(String sub, String pred, String obj) {
        String returnValue = "" ;
        //This method calls queryTriples() so this will be needed for the value found in response to the call
        Set<String> foundSet ;
        String foundStrings = "" ;

        //Going to need to split each string retruned from queryTriples into a sub, pred or obj
        String delimiter = ",(?! )" ;
        String[] tokens = new String[3] ;
        
        if (sub.equals("None")) {
                foundSet = (HashSet<String>) queryTriples("None", pred, obj) ;
                if (!foundSet.isEmpty()) {
                    Iterator foundSetIterator = foundSet.iterator() ;
                    while (foundSetIterator.hasNext()) {
                        foundStrings = (String) foundSetIterator.next() ;
                        tokens = foundStrings.split(delimiter) ;
                    }
                    returnValue = tokens[0] ;
               }
        } else if (pred.equals("None")) {
                foundSet = (HashSet<String>) queryTriples(sub, "None", obj) ;
                if (!foundSet.isEmpty()) {
                    Iterator foundSetIterator = foundSet.iterator() ;
                    while (foundSetIterator.hasNext()) {
                        foundStrings = (String) foundSetIterator.next() ;
                        tokens = foundStrings.split(delimiter) ;
                    }
                    returnValue = tokens[1] ;
                }
        } else if (obj.equals("None")) {
                foundSet = (HashSet<String>) queryTriples(sub, pred, "None") ;
                if (!foundSet.isEmpty()) {
                    Iterator foundSetIterator = foundSet.iterator() ;
                    while (foundSetIterator.hasNext()) {
                        foundStrings = (String) foundSetIterator.next() ;
                        tokens = foundStrings.split(delimiter) ;
                    }
                    returnValue = tokens[2] ;
                }
        }
        return returnValue ;
    }
    //Merge this with another graph:
    public void mergeGraph(Set<String> graphToMergeEntries) {
        Iterator graphIterator = graphToMergeEntries.iterator() ;
        String subject ;
        String predicate ;
        String object ;
        while (graphIterator.hasNext()) {
            String triple = "" ;
            triple = (String) graphIterator.next() ;
            String[] tokens  = new String[3] ;
            String delimiter = ",(?! )" ;
            tokens = triple.split(delimiter) ;
            subject = tokens[0] ;
            predicate = tokens[1] ;
            object = tokens[2] ;
            this.addTriple(subject, predicate, object);
        }
    }

    //Method that queries using queryClauses language variables given in the form ?variablename to constrain queries
    //Returns a Set of Maps. Each map represents bindings between a queryClauses constraint and the values found for it
    public Set<HashMap<String, String>> qlQuery(List<String[]> graphPatterns) {

        //Set of results to return. This be a Set of Map bindings of ?variable supplied to values
        Set<HashMap<String, String>> finalBindingsSet = new HashSet<HashMap<String, String>>() ;

        /*  Get each clause out of the queryClauses clauses List
         *  Query clauses are in the form "?variablename, pred, object" or "subject, pred, ?variablename" etc.
         *  each query clause is represented as an element of the queryClauses String array.
         *  ?variablename represents a node in the graph about which we are seeking information.
         *  Need to find which arguments are constraint variables, and their positon in the clause.
         */
        for (String[] triplePatterns: graphPatterns) {
        //This will be a Set of Map bindings of ?variable to values for each clause ie results for each iteration
        Set<HashMap<String, String>> bindingsSet = new HashSet<HashMap<String, String>>() ;

            //Get each query clause
            //for each queryClause in queryClauses:
            for (String triplePattern: triplePatterns) {
                //Get each sub, pred, obj in the current clause
                String[] triplePatternTokens = triplePattern.split(GraphUtil.delimiter) ;

                //String array for clause elements reformatted to be passed to queryTriples()
                //If constraint ?variables have been included in the query, they will be included here
                String[] queryTokens = new String[3] ;

                //This is used to track the positon of the constraint variables in each clause
                //Key = ?variablename Value=position in the queryClauses clause ie 0 - 2
                //
                Map<String, Integer> positions = new HashMap<String, Integer>() ;

                //System.out.println("Query clause: " + queryClauseTokens[0] + ", " + queryClauseTokens[1] + ", " + queryClauseTokens[2]) ;
                int i = 0 ; //counter
                for (String tripleToken: triplePatternTokens) {
                    //If the item starts with ? then this is a variable.
                    //Add "None" to queryTokens to pass to queryTriples() - in the position corresponding to where vaiable appeared in the triple pattern
                    //and add the ?variable and the position it occurred at to the positions Map
                    if (tripleToken.startsWith("?")) {
                        queryTokens[i] = "None" ;
                        positions.put(triplePatternTokens[i], i) ;
                    } else {
                        //Otherwise add the search value to the queryTokens at the relevant position.
                        queryTokens[i] = triplePatternTokens[i] ;
                    }
                    i++ ;
                }
                //Run a queryTriples() for the current queryClause
                //This will return a Set of triples as results for each of the terms passed to queryTriples()
                Set<String> queryResultsRows = queryTriples(queryTokens[0], queryTokens[1], queryTokens[2]) ;
                //If the set of results bindings is empty, we are in the first pass through the queries
                if (bindingsSet.isEmpty()) {
                    //Go over each triple in the results for the first query clause constraint
                    for (String queryResultsRow: queryResultsRows) {
                       //Split each result row out into its sub, pred, obj
                       String[] queryResultsRowTokens = queryResultsRow.split(GraphUtil.delimiter) ;

                       //Set up a Map to bind each individual result to its ?variable - this will be added to results bindings at teh end of the loop
                       HashMap<String, String> binding = new HashMap<String, String>() ;

                       //Need to retrieve the positions of the constraints ?variables found earlier
                       Set<String> variables = positions.keySet() ;

                       for (String variable: variables) {
                           //Get the position in the query clause that the ?variable occurred at
                           int position = positions.get(variable) ;
                           //Then add the value found at that position in current result row to the result map for this result
                           //Bind the result to the query variable in the results set
                           //Eg - ?company: ABC
                           binding.put(variable, queryResultsRowTokens[position]) ;
                       }
                       //Add the Map created to the resultSet
                       bindingsSet.add(binding);
                       
                    }
                 //If the bindingsSet is not empty - here we create an intersection of results bindings for each query clause that includes a particular ql variable
                 } else {
                 /*
                        After first iteration want to compare what we have in the current bindingsSet with what's been returned from queryTriples()
                        for the current qlQuery clause.
                 */
                    //Need a new result set
                    //New bindings are added to this, then this is assigned to the results bindings to be returned
                    Set<HashMap<String, String>> newBindingsSet = new HashSet<HashMap<String, String>>() ;

                    //For each result in the bindingsSet now
                    for (Map<String, String> binding: bindingsSet) {
                         //For each row in the current results from queryTriple()
                         for (String queryResultsRow: queryResultsRows) {
                            //Split the row
                            String[] queryResultsRowTokens = queryResultsRow.split(GraphUtil.delimiter) ;
                            boolean validMatch = true ;

                            //Clone the current resultMap
                            HashMap<String, String> bindingTemp = new HashMap<String, String>(binding) ;

                            //Need the positions of the ?variables passed to queryTriples() for the current iteration
                            Set<String> variables = positions.keySet() ;

                            for (String variable: variables) {
                                //Get the position in the queryClauses clause the constraint occurred at
                                int position = positions.get(variable) ;
                                //Check if it is in the current set of bindings
                                if (bindingTemp.containsKey(variable)) {
                                    //If so, retrieve the value for it and compare it with the value at the same position in the current result row
                                    if (!bindingTemp.get(variable).equals(queryResultsRowTokens[position])) {
                                        //if it does not match, set this flag to false - the value for this result will not be added to the result bindings
                                        validMatch = false ;
                                    }
                                //If it is not in the current bindings add it and its value - new variable
                                } else {
                                    bindingTemp.put(variable, queryResultsRowTokens[position]) ;
                                }
                            }
                            if (validMatch) {
                                //The values for the result in this row == that in the current bindings, add this to the new bindings set
                                newBindingsSet.add(bindingTemp);
                                //System.out.println(newResultSet) ;
                            }
                        }//end for queryresultRow in queryResultRows
                    }//end for binding in bindingsSet
                    bindingsSet = newBindingsSet ;
                }//end if-else

            }//for queryClause i queryClauses[] - end loop over each clause for this query
            finalBindingsSet.addAll(bindingsSet);
            System.out.println(finalBindingsSet.size() + " results for query");

        }//for queryClauses[] in queries - end loop over each query in the list of queries
        /*
            Return the Set of bindings for each query constriant in each query passed to the method
        */
        return finalBindingsSet ;
    }//end queryClauses

    /*
     *  Method that applies a particular InferenceRule to the graph.
     *  Takes a rule as argument, gets the queries associated with the rule,
     */
    public void applyInferenceRule(InferenceRule infRule) {
        //pass a Set of bindings to InferenceRule.makeTriples.
        //This will return a List of triples to add to the graph
        List<String> newTriplesList = infRule.makeTriples(this.qlQuery( (ArrayList<String[]>) infRule.getQueries())) ;
        for (String newTriple: newTriplesList) {
            String[] newTriplesTokens = newTriple.split(GraphUtil.delimiter) ;
            this.addTriple(newTriplesTokens[0], newTriplesTokens[1], newTriplesTokens[2]);
        }
    }//end applyInferenceRule

    /*
        Method that performs a bfs on the graph.
     *  startNode represents the start node in the graph to begin at and endNode represents the the search node
     *  The goal is to find if a path exists between startNode and endNode, if so, return the path traversed to get from the start to the end.
     */
    //startNode is the node to start from, endNode is the target node, graph is an instance of this
    public List graphBfs(String startNode, String endNode, SimpleGraph graph) {
        /*
         *  List for results - it will contain each of the nodes traversed between the start node and end node if one is found
         */
        List<String> path = new ArrayList<String>() ;
        /*
         *  Nodes to visit in the graph
         *  Following the typical OO bfs pattern, a queue of nodes to visit is used. Nodes are added to the list on each iteration.;
         *  
            Each element of the Queue is a Map which contains a Mapping of a node name to the path that was traversed to get to that node.
         *  A node's path is represented by an ArrayList
         */
        Queue<HashMap<String, ArrayList>> actorIdsAndPaths = new LinkedList<HashMap<String, ArrayList>>() ;
        //Single mapping for an actor ID node to it's path as value
        Map<String, ArrayList> actorIdAndParents = new HashMap<String, ArrayList>() ;
        //add the first actorId and his path which is an empty arraylist
        actorIdAndParents.put(startNode, (ArrayList) path) ;
        //add the map to the Queue
        actorIdsAndPaths.add((HashMap)actorIdAndParents) ;
        
        //A List of unvisited nodes is kept
        List<String> visitedNodes = new ArrayList<String>() ;

        //Begin the loop:
        int iteration = 1 ;
        //While there are more nodes to visit
        while (!actorIdsAndPaths.isEmpty()) {
           System.out.println("Iteration: " + iteration) ;
           iteration++ ;

           /*
            *-------------------------------------------------------------------------------------------------------------------------------
                Each iteration needs to take into account the current actor node and the nodes representing films that she has appeared in
                Collect all movie ids for films this actor has starred in
            *-------------------------------------------------------------------------------------------------------------------------------
           */
           //Queue for the movieIds that the current actor appeared in - will need to bfs over this as well. 
           Queue<HashMap<String, ArrayList>> movieIdsAndParents = new LinkedList<HashMap<String, ArrayList>>() ;

           //Get the current actorId = current node
           //First extract the Map for this actorId and the path to it
           actorIdAndParents = (HashMap) actorIdsAndPaths.remove() ;
           //Then get the actor Id as a String and it's path will be a list. This list will need to be the path to next movie node
           Set<String> keys =  actorIdAndParents.keySet() ;
           for (String actorId: keys) {
                //Based on the actorId get the movie node ids for the movies she appeared in.
                Set<String> movieIds  = (HashSet<String>) this.queryTriples("None", "starring", actorId) ;
                for (String movieId: movieIds) {
                    String[] movieIdTokens = movieId.split(GraphUtil.delimiter) ;
                    //.if this movie node has not been visited add it to visited, get its path and add it to the movie nodes queue
                    if (!visitedNodes.contains(movieIdTokens[0])) {
                        //Add this move node to the visited child nodes
                        visitedNodes.add(movieIdTokens[0]) ;
                        //add this actorId's path to the current movie's path
                        ArrayList<String> movieParents = new ArrayList<String>() ;
                        movieParents.addAll(actorIdAndParents.get(actorId)) ;
                        //Then add the actorId
                        movieParents.add(actorId);

                        //Map this movie node id to its path
                        HashMap<String, ArrayList> movieIdAndParents = new HashMap<String, ArrayList>() ;
                        //Add it to the movie nodes queue
                        movieIdAndParents.put(movieIdTokens[0], movieParents) ;
                        movieIdsAndParents.add(movieIdAndParents) ;
                    }
                }
            }

           Queue<HashMap<String, ArrayList>> nextActorIdsAndPaths = new LinkedList<HashMap<String, ArrayList>>() ;
           //Iterate over the movie nodes for this actor. Try to find a film that starred endNode actor
           while (!movieIdsAndParents.isEmpty()) {
                //Get the next movie node
                HashMap<String, ArrayList> movieIdAndParents = new HashMap<String, ArrayList>() ;
                movieIdAndParents = movieIdsAndParents.remove() ;
                keys = movieIdAndParents.keySet() ;
                for (String movieId: keys) {
                    //get the next set of actor nodes
                    HashMap<String, ArrayList> nextActorIdAndParents = new HashMap<String, ArrayList>() ;
                    //Query for all actors in the film
                    Set<String> nextActors = (HashSet<String>) this.queryTriples(movieId, "starring", "None") ;
                    for (String nextActorId: nextActors) {
                        String[] nextActorIdTokens = nextActorId.split(GraphUtil.delimiter) ;
                        //If not in visited nodes add it
                        if (!visitedNodes.contains(nextActorIdTokens[2])) {
                            visitedNodes.add(nextActorIdTokens[2]) ;
                            //Record the path to this actor node --> movie node path + movie node:
                            ArrayList<String> nextActorParents = new ArrayList<String>() ;
                            nextActorParents.addAll(movieIdAndParents.get(movieId)) ;
                            nextActorParents.add(movieId) ;
                                //If the current actor is the target actor...
                                if (nextActorIdTokens[2].equals(endNode)) {
                                    //Found the end node - shout out loud:
                                    System.out.println("Node found") ;
                                    //Record the current acotor to the path and return it
                                    nextActorParents.add(nextActorIdTokens[2]) ;
                                    return path = nextActorParents ;
                                } else {
                                    nextActorIdAndParents.put(nextActorIdTokens[2], nextActorParents) ;
                                    nextActorIdsAndPaths.add(nextActorIdAndParents);
                                }
                        }
                    }
                }
            }
          //We haven't found the actor - move to the next acotr in the actors queue
          actorIdsAndPaths = nextActorIdsAndPaths ;
        }//end while actorIds not empty
        //No luck!
        return path ;
    }//end bfs()

    //This will read a Set of triples from a file and add them to this graph
    public boolean readTriples(String fileName) {
        boolean ok = false ;
        //Going to need to split each string retruned from into a sub, pred, obj
        String delimiter = ",(?! )" ;
        String[] tokens = new String[3] ;
        TripleReader tripleReader = new TripleReader(fileName) ;
        Set<String> newTriples = (HashSet<String>) tripleReader.readTriplesFromFile() ;
        if (newTriples.size() > 0) {
            Iterator newTriplesIterator = newTriples.iterator() ;
            String triple ;
            while (newTriplesIterator.hasNext()) {
                triple = (String) newTriplesIterator.next() ;
                tokens = triple.split(delimiter) ;
                this.addTriple(tokens[0], tokens[1], tokens[2]);
            }
            ok = true ;
        } else {
            ok = false ;
        }
        return ok ;
    }//end readTriples
    //Index accessors:
    public Map<String, Map> getSpo() {
        return this.spo ;
    }
    public Map<String, Map> getPos() {
        return this.pos ;
    }
    public Map<String, Map> getOsp() {
        return this.osp ;
    }
}//end SimpleGraph