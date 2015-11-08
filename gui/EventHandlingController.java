package gui;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import majestic.GetTopBackLinks;
import tag_trends.parser.Country;


/**
 * View-Controller for the person table.
 * 
 * @author Marco Jakob
 */
public class EventHandlingController {
	
	@FXML
	private Button myButton = new Button();
	
	@FXML
	private ComboBox<String> myComboBox=new ComboBox<String>();
	private ObservableList<String> locations = FXCollections.observableArrayList();
	
	@FXML
	private DatePicker dateFrom = new DatePicker(LocalDate.of(2015, 11, 07));
	
	@FXML
	private DatePicker dateTo = new DatePicker(LocalDate.now());
	
	
	public static String graph = "";
	/**
	 * The constructor (is called before the initialize()-method).
	 */
	public EventHandlingController() {
		// Create some sample data for the ComboBox and ListView.
//		myComboBoxData.add(new Person("Hans", "Muster"));
//		myComboBoxData.add(new Person("Ruth", "Mueller"));
//		myComboBoxData.add(new Person("Heinz", "Kurz"));
//		myComboBoxData.add(new Person("Cornelia", "Meier"));
//		myComboBoxData.add(new Person("Werner", "Meyer"));
//		
//		listViewData.add(new Person("Lydia", "Kunz"));
//		listViewData.add(new Person("Anna", "Best"));
//		listViewData.add(new Person("Stefan", "Meier"));
//		listViewData.add(new Person("Martin", "Mueller"));
	}
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		
	    Map<String,Integer> countryList = Country.loadCountries();
//	    Vector locations = new Vector();
	    Object[] keys = countryList.keySet().toArray();
	    Arrays.sort(keys);
	    for (int i=0;i<keys.length;i++) locations.add((String) keys[i]);		
		myComboBox.setItems(locations);
;
		
		myButton.setOnAction((event) -> {
			
			LocalDate localDateFrom = dateFrom.getValue();
			if (localDateFrom==null) localDateFrom = LocalDate.of(2015, 11, 07);
			Instant instantFrom = Instant.from(localDateFrom.atStartOfDay(ZoneId.systemDefault()));
			Date dateFrom = Date.from(instantFrom);
			LocalDate localDateTo = dateTo.getValue();
			if (localDateTo==null) localDateTo = LocalDate.now();
			Instant instantTo = Instant.from(localDateTo.atStartOfDay(ZoneId.systemDefault()));
			Date dateTo = Date.from(instantTo);

			graph = GetTopBackLinks.analyse(localDateFrom.toString(), localDateTo.toString(), myComboBox.getValue());
			
//            Parent root = FXMLLoader.load(getClass().getResource("PersonOverview.fxml"));
            StackPane pane = new StackPane();
//            ImageView newImage = new ImageView(new Image(graph));
            ImageView newImage = new ImageView(new File(graph).toURI().toString());
//            File gfile = new File(graph);
//            System.out.println(gfile.exists());
//            System.out.println(gfile.getAbsolutePath());
//            Image img = new Image(graph);
//            newImage.setImage(img);
            newImage.setPreserveRatio(true);
            pane.getChildren().add(newImage);
            Stage s = new Stage();
            
            s.setScene(new Scene(pane, 1200, 1000));
            s.show();
//            this.primaryStage.setScene(new Scene(root, 600, 200));
//            this.primaryStage.getScene();
////            ComboBox cb = (ComboBox) this.primaryStage.getScene().lookup("combo");
//            this.primaryStage.show();

			
			
//			System.out.println(dateFrom.getValue().toString());
//			Test.findTags(dateFrom.getValue().toString(), dateFrom.getValue().toString(), myComboBox.getValue());
//			  Test.findTags(dateFrom.getYear()+"-"+modelFrom.getMonth()+"-"+modelFrom.getDay(), modelTo.getYear()+"-"+modelTo.getMonth()+"-"+modelTo.getDay(), (String)locationList.getSelectedItem());

//			System.exit(1);
		});
		
		// Handle Button event.
//		myButton.setOnAction((event) -> {
//			outputTextArea.appendText("Button Action\n");
//		});
		
//		// Handle CheckBox event.
//		myCheckBox.setOnAction((event) -> {
//			boolean selected = myCheckBox.isSelected();
//			outputTextArea.appendText("CheckBox Action (selected: " + selected + ")\n");
//		});
		
		// Init ComboBox items.
//		myComboBox.setItems(myComboBoxData);
		
//		// Define rendering of the list of values in ComboBox drop down. 
//		myComboBox.setCellFactory((comboBox) -> {
//			return new ListCell<Person>() {
//				@Override
//				protected void updateItem(Person item, boolean empty) {
//					super.updateItem(item, empty);
//					
//					if (item == null || empty) {
//						setText(null);
//					} else {
//						setText(item.getFirstName() + " " + item.getLastName());
//					}
//				}
//			};
//		});
		
//		// Define rendering of selected value shown in ComboBox.
//		myComboBox.setConverter(new StringConverter<Person>() {
//			@Override
//			public String toString(Person person) {
//				if (person == null) {
//					return null;
//				} else {
//					return person.getFirstName() + " " + person.getLastName();
//				}
//			}

//			@Override
//			public Person fromString(String personString) {
//				return null; // No conversion fromString needed.
//			}
//		});
		
//		// Handle ComboBox event.
//		myComboBox.setOnAction((event) -> {
//			Person selectedPerson = myComboBox.getSelectionModel().getSelectedItem();
//			outputTextArea.appendText("ComboBox Action (selected: " + selectedPerson.toString() + ")\n");
//		});
//		
//		// Handle Hyperlink event.
//		myHyperlink.setOnAction((event) -> {
//			outputTextArea.appendText("Hyperlink Action\n");
//		});
//		
//		// Init ListView.
//		myListView.setItems(listViewData);
//		myListView.setCellFactory((list) -> {
//			return new ListCell<Person>() {
//				@Override
//				protected void updateItem(Person item, boolean empty) {
//					super.updateItem(item, empty);
//					
//					if (item == null || empty) {
//						setText(null);
//					} else {
//						setText(item.getFirstName() + " " + item.getLastName());
//					}
//				}
//			};
//		});
//		
//		// Handle ListView selection changes.
//		myListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//			outputTextArea.appendText("ListView Selection Changed (selected: " + newValue.toString() + ")\n");
//		});
//		
//		// Handle Slider value change events.
//		mySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
//			outputTextArea.appendText("Slider Value Changed (newValue: " + newValue.intValue() + ")\n");
//		});
//		
//		// Handle TextField text changes.
//		myTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//			outputTextArea.appendText("TextField Text Changed (newValue: " + newValue + ")\n");
//		});
//		
//		// Handle TextField enter key event.
//		myTextField.setOnAction((event) -> {
//			outputTextArea.appendText("TextField Action\n");
//		});
		
	}
	
}