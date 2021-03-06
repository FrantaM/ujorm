/*
 * Person.java
 *
 * Created on 9. June 2007, 22:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package samples.map.PersonExt;

import org.ujorm.Key;
import org.ujorm.implementation.map.*;
public class Person extends MapUjoExt<Person> {
    
  public static final Key<Person, String > NAME = newKey("Name");
  public static final Key<Person, Boolean> MALE = newKey("Male");
  public static final Key<Person, Double > CASH = newKey("Cash");

  static {
     init(Person.class );
  }
    
  public void addCash(double cash) {
    double newPrice = get(CASH) + cash;
    set(CASH, newPrice);
  }
  
  public void addCash_old(double cash) {
    double newPrice = CASH.of(this) + cash;
    CASH.setValue(this, newPrice);
  }  
}
