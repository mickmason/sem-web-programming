package triple_store_examples;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import triple_store.simple_graph.SimpleGraph ;
import triple_store.graph_utils.GraphUtil ;
import triple_store.inference_rules.GeocodeRule ;
import triple_store.inference_rules.InferenceRule ;
/**
 *
 * @author Michael
 */
public class GeocodeRuleTest {
    public static void main(String[] args) {
        //Create a graph for places and add a new triple
        SimpleGraph geoGraph = new SimpleGraph() ;
        geoGraph.addTriple("White House", "address", "1600 Pennsylvania Ave, Washington DC");
        System.out.println(geoGraph.queryTriples("None", "address", "None").toString()) ;

        //Create an InfRule instance:
        InferenceRule geocodeRule = new GeocodeRule() ;
        geoGraph.applyInferenceRule(geocodeRule);
        System.out.println(geoGraph.queryTriples("None", "longitude", "None").toString()) ;
        System.out.println(geoGraph.queryTriples("None", "latitude", "None").toString()) ;
    }//end main
}//end class
