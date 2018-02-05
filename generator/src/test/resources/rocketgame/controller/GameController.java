package rocketgame.controller;

import annotations.abstraction.FieldMethodSymbol;
import annotations.abstraction.WidgetSymbol;
import rocketgame.model.Game;
import rocketgame.model.RocketStatus;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import annotations.abstraction.FunctionSymbol;

public class GameController {

    public ImageView rocket1Image;
    public Label rocket1StatusLabel;
    public ImageView rocket2Image;
    public Label rocket2StatusLabel;
    private Game game;
    private boolean rocket2activated;
    public Circle permissionIndicator;

    @WidgetSymbol(inputWidgetID = "buttonRocket1", outputFunctionIDs = {"output"}, events = {"press","release","drag"}, params = "")
    @FieldMethodSymbol(inputFieldID = "button1RocketField",outputFunctionIDs = "output", fieldMethods = {"setVisible(bool)","setVisible(bool)"}, params = {"param#1:Bool:True","param#2:Bool:False"})
    public Button buttonRocket1;

    @WidgetSymbol(inputWidgetID = "buttonRocket2", outputFunctionIDs = {"output"}, events = {"press","release","drag"}, params = "")
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

    @FunctionSymbol(inputSymbolID = "permissionTrue",inputFunctionID = "givePermission",outputFunctionID = "output",
            params = {"nfapredicate:testNFA#NFARepeatSequence2[1]#G1","predicate:p1","param#1:Bool:True"})
    @FunctionSymbol(inputSymbolID = "permissionFalse",inputFunctionID = "retractPermission",outputFunctionID = "output",
            params = {"nfapredicate:testNFA#NFARepeatSequence2[2]#G1","predicate:p1","param#1:Bool:False"})
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

