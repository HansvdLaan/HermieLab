package rocketgame.model;

import annotations.concrete.OutputFunction;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import java.util.LinkedList;

public class Game {

    private RocketStatus rocket1Status;
    private RocketStatus rocket2Status;
    private boolean permission;

    public ObservableList<RocketStatus> getRocketStatuses() {
        return rocketStatuses;
    }

    private ObservableList<RocketStatus> rocketStatuses;

    public Game(){
        rocket1Status = RocketStatus.ROCKET_STANDBY;
        rocket2Status = RocketStatus.ROCKET_STANDBY;
        rocketStatuses = new ObservableListWrapper<RocketStatus>(new LinkedList<>());
        rocketStatuses.add(0,rocket1Status);
        rocketStatuses.add(1,rocket2Status);
        permission = true;
    }

    public RocketStatus getRocket1Status() {
        return rocket1Status;
    }

    public void setRocket1Status(RocketStatus rocket1Status) {
        this.rocket1Status = rocket1Status;
        rocketStatuses.remove(0);
        rocketStatuses.add(0,rocket1Status);
    }

    public RocketStatus getRocket2Status() {
        return rocket2Status;
    }

    public void setRocket2Status(RocketStatus rocket2Status) {
        this.rocket2Status = rocket2Status;
        rocketStatuses.remove(1);
        rocketStatuses.add(1,rocket2Status);
    }

    public boolean hasPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    @OutputFunction(id = "output1", params = "")
    @Override
    public String toString() {
        return "Game{" +
                "rocket1Status=" + rocket1Status +
                ", rocket2Status=" + rocket2Status +
                ", permission=" + permission +
                ", rocketStatuses=" + rocketStatuses +
                '}';
    }
}
