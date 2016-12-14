// the table should be filtered whenever the user enters something in the text field 
package com.tableviewsortingfiltering;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList; // -> FilteredList -> SortedList (Decorator)
import javafx.collections.transformation.FilteredList; // JavaFX 8
import javafx.collections.transformation.SortedList; // JavaFX 8
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 * View-Controller for the person table.
 *
 * @author Author
 */
public class PersonTableController
{
    @FXML
    private TextField filterTextField;

    @FXML
    private TableView<Person> personTable;

    @FXML
    private TableColumn<Person, String> firstNameColumn;

    @FXML
    private TableColumn<Person, String> lastNameColumn;

    private final ObservableList<Person> observableList
            = FXCollections.observableArrayList();

    /**
     * Add some sample data in the constructor.
     */
    public PersonTableController() 
    {
        observableList.add(new Person("Robert", "Penn"));
        observableList.add(new Person("John", "Right"));
        observableList.add(new Person("Kelly", "Ross"));
        observableList.add(new Person("John", "Ross"));
        observableList.add(new Person("Ivan", "Ivanov"));
        observableList.add(new Person("Olga", "Ross"));
        observableList.add(new Person("Anna", "Best"));
        observableList.add(new Person("Mary", "May"));
        observableList.add(new Person("Mary", "Ross"));
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     *
     * Initializes the table columns and sets up sorting and filtering.
     */
    @FXML
    private void initialize() 
    {
        // 1. Setting data properties to columns;
        // sCVF() sets the value of the property cellValueFactory
        firstNameColumn.setCellValueFactory(cellData ->// CellDataFeatures<Person, String>
                cellData.getValue().firstNameProperty());
        
        // note: set TableView to be editable first;
        // sets the value of the property cellFactory; (<cell editing part)
        // the cell factory is responsible for rendering the data contained within
        // each TableCell for a single table column 
        firstNameColumn.setCellFactory(
                TextFieldTableCell.forTableColumn());
        
        firstNameColumn.setOnEditCommit( // lambda implements EventHandler
                (TableColumn.CellEditEvent<Person, String> event) ->
                {
                   ((Person) event.getTableView()
                                            .getItems()
                                            .get(event.getTablePosition().getRow())
                   ).setFirstName(event.getNewValue());
                } 
           ); // (cell editing part>; press enter to finish cell editing; 
              // re-enter filter field after editing if something was there)  
        
        lastNameColumn.setCellValueFactory(cellData ->
                cellData.getValue().lastNameProperty());
        
        lastNameColumn.setCellFactory(
                TextFieldTableCell.forTableColumn());
        
        lastNameColumn.setOnEditCommit(  
              // anonymous inner class implements EventHandler
              new EventHandler<TableColumn.CellEditEvent<Person, String>>() 
              {
                 @Override
                 public void handle(
                         TableColumn.CellEditEvent<Person, String> event) 
                 {
                    ((Person) event.getTableView()
                                             .getItems()
                                             .get(event.getTablePosition().getRow())
                    ).setLastName(event.getNewValue());
                 }
              }
           );

        // 2. Wrap the ObservableList into a FilteredList;
        // the FilteredList filters the list depending on a specified Predicate;
        // the initial Predicate is always true: p -> true 
        FilteredList<Person> filteredList = new FilteredList<>(observableList, p -> true);

        // 3. Add a ChangeListener to the filter text field; whenever the user changes 
        // the text, the Predicate of our FilteredList is updated; here, a filter that matches
        // whenever firstName or lastName contains the filter String
        filterTextField.textProperty().addListener((observable, oldValue, newValue) ->
                   {
                       // sets the value of the property predicate that matches elems in FL
                       filteredList.setPredicate(person -> 
                       {
                           // if filter text is empty, display all persons
                           if (newValue == null || newValue.isEmpty()) 
                           {
                              return true;
                           }

                           // compares first name and last name of every person with filter text
                           String lowerCaseFilter = newValue.toLowerCase();
                           if (person.getFirstName()
                                          .toLowerCase()
                                          .contains(lowerCaseFilter)) // contains() used
                           {
                              return true; // filter matches first name
                           } 
                           else if (person.getLastName()
                                                 .toLowerCase()
                                                 .indexOf(lowerCaseFilter) != -1) // indexOf() used
                           {
                              return true; // filter matches last name
                           }
                           return false; // filter does not match
                       });
                   });

        // 4. FilteredList is unmodifiable, so it cannot be sorted;
        // one needs to wrap it in SortedList for this purpose 
        SortedList<Person> sortedList = new SortedList<>(filteredList);

        // 5. Bind the SortedList comparator to the TableView comparator:
        // a click on the column header changes the sorting of the TableView;
        // but now one must bind the sorting of a separate SortedList to the TableView;
        // NB: TableView will return back to the original, unsorted state after three clicks
        // on the column header
        sortedList.comparatorProperty().bind(personTable.comparatorProperty());

        // 6. Add sorted (and filtered) data to the table:
        personTable.setItems(sortedList);
    } // end method initialize()
} // end class PersonTableController