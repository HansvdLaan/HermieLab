package testcode.rocketgame;

import annotations.setup.Start;
import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import annotations.setup.PostQuery;

import java.io.IOException;

public class RocketGame extends Application {

    //Main class which extends from Application Class
    public static void main(String[] args) {
        launch(args);
    }

    //This is our PrimaryStage (It contains everything)
    private Stage primaryStage;

    //This is the BorderPane of RootLayout
    private BorderPane rootLayout;

    @Override
    @Start(automaton = "DFA", params = "")
    public void start(Stage primaryStage) throws IOException {
        //1) Declare a primary stage (Everything will be on this stage)

        this.primaryStage = primaryStage;

        //Optional: Set a title for primary stage
        this.primaryStage.setTitle("JavaFX HermieLab Test - Simple Rocket Game");

        //2) Initialize RootLayout
        initRootLayout();

        //3) Display the EmployeeOperations View
        showGameView();
    }


    //Initializes the root layout.
    public void initRootLayout() {
        try {
            //First, load root layout from RootLayout.fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RocketGame.class.getResource("/testgame/images/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            //Second, show the scene containing the root layout.
            Scene scene = new Scene(rootLayout); //We are sending rootLayout to the Scene.
            primaryStage.setScene(scene); //Set the scene in primary stage.

            //Third, show the primary stage
            primaryStage.show(); //Display the primary stage
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Shows the game view inside the root layout.
    @PostQuery(order = 0, params = "")
    public void showGameView() {
        try {
            //First, load EmployeeOperationsView from EmployeeOperations.fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RocketGame.class.getResource("/testgame/images/GameView.fxml"));
            AnchorPane employeeOperationsView = (AnchorPane) loader.load();

            // Set Employee Operations view into the center of root layout.
            rootLayout.setCenter(employeeOperationsView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

