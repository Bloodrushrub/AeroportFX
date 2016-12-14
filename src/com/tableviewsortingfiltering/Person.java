package com.tableviewsortingfiltering;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Data model class for the person table.
 * 
 * @author Author
 */
public class Person 
{
   private final StringProperty FIRSTNAME;
   private final StringProperty LASTNAME;

   public Person(String firstName, String lastName) 
   {
      this.FIRSTNAME = new SimpleStringProperty(firstName);
      this.LASTNAME = new SimpleStringProperty(lastName);
   }
	
   public final String getFirstName() 
   {
      return FIRSTNAME.get();
   }

   public final void setFirstName(String firstName)
   {
      this.FIRSTNAME.set(firstName);
   }
	
   public StringProperty firstNameProperty() 
   {
      return FIRSTNAME;
   }

   public final String getLastName()
   {
      return LASTNAME.get();
   }

   public final void setLastName(String lastName) 
   {
      this.LASTNAME.set(lastName);
   }
	
   public StringProperty lastNameProperty() 
   {
      return LASTNAME;
   }
}