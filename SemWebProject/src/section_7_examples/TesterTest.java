/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package section_7_examples ;
/**
 *
 * @author Michael
 */
public class TesterTest {
    public static void main(String[] args) {        
        TheClass theClass = new TheClass() ;
        String result = method1(theClass.number) + method2();
        System.out.println(result) ;
        System.out.println("The class number is " + theClass.number) ;
        
    }//main()

    public static String method1(int number) {
        number++ ;
        System.out.println("In method1 the number is " + number) ;
        return "Method one " ;
    }
    public static String method2() {
        return "Method two " ;
    }
}
class TheClass {
    public int number ;
    public TheClass() {
        this.number = 1 ;
    }
}
