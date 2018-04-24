package testcode.rocketgame.controller;

import hermielab.annotations.abstraction.FieldMethodSymbol;
import hermielab.annotations.abstraction.WidgetSymbol;
import testcode.rocketgame.model.Game;
import testcode.rocketgame.model.RocketStatus;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import hermielab.annotations.abstraction.FunctionSymbol;

public class GameController {

    public ImageView rocket1Image;
    public Label rocket1StatusLabel;
    public ImageView rocket2Image;
    public Label rocket2StatusLabel;
    private Game game;
    private boolean rocket2activated;
    public Circle permissionIndicator;

    @WidgetSymbol(widgetID = "buttonRocket1", outputIDs = {"output"}, events = {"press","release","drag"})
    @FieldMethodSymbol(fieldID = "button1RocketField", outputIDs = "output", fieldMethods = {"setVisible(bool)","setVisible(bool)"}, parameters = {"Bool:True","Bool:False"})
    public Button buttonRocket1;

    @WidgetSymbol(widgetID = "buttonRocket2", outputIDs = {"output"}, events = {"press","release","drag"})
    public Button buttonRocket2;

    public GameController(){
        game = new Game();
        rocket2activated = false;

        game.getRocketStatuses().addListener(new ListChangeListener<RocketStatus>() {
            @Override
            public void onChanged(Change<? extends RocketStatus> c) {
                c.next();
                if (c.wasAdded()) {
                    if (c.getList().get(0).equals(RocketStatus.ROCKET_STANDBY)) {
                        rocket1StatusLabel.setText("STANDBY");
                        rocket1Image.setVisible(true);
                    } else if (c.getList().get(0).equals(RocketStatus.ROCKET_ARMED)) {
                        rocket1StatusLabel.setText("ARMED");
                    } else if (c.getList().get(0).equals(RocketStatus.ROCKET_LAUNCHED)) {
                        rocket1StatusLabel.setText("LAUNCHED");
                        rocket1Image.setVisible(false);
                    }
                }
            }
        });
        game.getRocketStatuses().addListener(new ListChangeListener<RocketStatus>() {
            @Override
            public void onChanged(Change<? extends RocketStatus> c) {
                c.next();
                if (c.wasAdded() && rocket2activated) {
                    if (c.getList().get(1).equals(RocketStatus.ROCKET_STANDBY)) {
                        rocket2StatusLabel.setText("STANDBY");
                        rocket2Image.setVisible(true);
                    } else if (c.getList().get(1).equals(RocketStatus.ROCKET_ARMED)) {
                        rocket2StatusLabel.setText("ARMED");
                    } else if (c.getList().get(1).equals(RocketStatus.ROCKET_LAUNCHED)) {
                        rocket2StatusLabel.setText("LAUNCHED");
                        rocket2Image.setVisible(false);
                    }
                }
            }
        });
    }

    @FunctionSymbol(symbolID = "permissionTrue",outputID = "output",
            nfapredicates = {"testNFA#NFARepeatSequence2[1]#G1"}, predicates = {"p1"}, parameters = {"Bool:True"})
    @FunctionSymbol(symbolID = "permissionFalse",outputID = "output",
            nfapredicates = {"testNFA#NFARepeatSequence2[1]#G1"}, predicates = {"p1"}, parameters = {"Bool:False"})
    public void setPermission(boolean permission){
        game.setPermission(permission);
        if (permission){
            permissionIndicator.setFill(Paint.valueOf("GREEN"));
        } else {
            permissionIndicator.setFill(Paint.valueOf("RED"));
        }
    }

    @FXML
    public void initialize(){
        rocket2Image.setVisible(false);
        permissionIndicator.setFill(Paint.valueOf("GREEN"));
    }
}

