package testcode.rocketgame.controller;

//import testgame.RocketGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class RootController {

    public BorderPane root;

    public void resetGame(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RocketGame.class.getResource("/testgame/images/GameView.fxml"));
            AnchorPane employeeOperationsView = (AnchorPane) loader.load();

            root.setCenter(employeeOperationsView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
