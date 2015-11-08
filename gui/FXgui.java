package gui;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import tag_trends.parser.Country;

public class FXgui extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("#TrendingInternet");

        initRootLayout();

//        showPersonOverview();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            Map<String,Integer> countryList = Country.loadCountries();
            Vector locations = new Vector();
//            Object[] keys = countryList.keySet().toArray();
//            Arrays.sort(keys);
//            for (int i=0;i<keys.length;i++) locations.add(keys[i]);
//            locationList = new JComboBox(locations);
//            locationList.setSelectedItem("Worldwide");
            
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            Parent root = FXMLLoader.load(getClass().getResource("PersonOverview.fxml"));
            this.primaryStage.setScene(new Scene(root, 600, 200));
            this.primaryStage.getScene();
//            ComboBox cb = (ComboBox) this.primaryStage.getScene().lookup("combo");
            this.primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            FXMLLoader.load(getClass().getResource("PersonOverview.fxml"));
//            loader.setLocation(FXgui.class.getResource("PersonOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            Parent root = FXMLLoader.load(getClass().getResource("PersonOverview.fxml"));
            this.primaryStage.setScene(new Scene(root, 300, 275));
            this.primaryStage.show();
            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}