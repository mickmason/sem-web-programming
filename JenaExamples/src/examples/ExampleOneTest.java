/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples;
import com.hp.hpl.jena.rdf.model.* ;
/**
 *  Example that uses Jena to create a Model, some Resources within that model, then adds
 *  Properties to the Resources and does some basic query on them
 *
 *  @author Michael
 */
public class ExampleOneTest {

    public static void main(String[] args){

        /*
         *  Here the simple example of adding data to a graph is executed using Jena.
         *  A Model is created, then the relevant resources.
         *  Properties and thier values are added to the Resources.
         * 
         */
        //Create the Model
        Model moviesModel = ModelFactory.createDefaultModel() ;
        //Create resources
        Resource bladeRunner = moviesModel.createResource("blade_runner") ;
        Resource ridleyScott = moviesModel.createResource("ridley_scott") ;
        //Create Properties
        Property name = moviesModel.createProperty("name") ;
        Property directedBy = moviesModel.createProperty("directed_by") ;
        Property releaseDate = moviesModel.createProperty("release_date") ;

        //Add the properties to resources
        bladeRunner.addProperty(directedBy, ridleyScott) ;
        bladeRunner.addProperty(name, "Blade Runner") ;
        bladeRunner.addProperty(releaseDate, "June 25, 1982") ;
        ridleyScott.addProperty(name, "Ridley Scott") ; 

        moviesModel.add(bladeRunner, name, "Blade Runner").add(bladeRunner, directedBy, ridleyScott).add(bladeRunner, releaseDate, "June 25, 1982");
        moviesModel.add(ridleyScott, name, "Ridley Scott") ;
        
        //Now retrieve all of the Statements added to the Model by retrieving a Statement iterator over the Statements in the model
        //Model.listStatements() with no arguments returns an iterator over all Statements in the model
        StmtIterator  iter = moviesModel.listStatements() ;
        System.out.println("All triples added:") ;
        while (iter.hasNext()) {
            //Access the subject (Resource), predicate (Property), object (RDF node) of the Statement
            //Get the Statement first:
            Statement stmt = iter.nextStatement() ;
            Resource subj = stmt.getSubject() ;
            Property pred = stmt.getPredicate() ;
            RDFNode obj = stmt.getObject() ;

            System.out.print(subj.toString() + " " + pred.toString() + " ") ;
            if (obj instanceof Resource) {
                System.out.println(obj.toString()) ;
            } else {
                System.out.println("\"" + obj + "\"") ;
            }
        }
        //Do some basic querying
        //Get the value for the property "directed_by" for the Resource "blade_runner"
        System.out.println() ;
        System.out.println("Running a basic query for who directed Blade Runner: ") ;
        iter = moviesModel.listStatements(bladeRunner, directedBy, (RDFNode) null) ;
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement() ;
            System.out.print(stmt.getSubject().toString() + " " + stmt.getPredicate().toString() + " ") ;
            if (stmt.getObject() instanceof Resource) {
                System.out.println(stmt.getObject().toString()) ;
            } else {
                System.out.println("\"" + stmt.getObject() + "\"") ;
            }
        }
    }//end main()
}//end class
