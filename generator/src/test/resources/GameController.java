import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import javafx.scene.control.Button;
import settings.annotations.FieldTest;
import settings.annotations.MethodFieldTest;
import settings.annotations.MethodTest;

/**
 * Created by Hans on 4-1-2018.
 */

public class GameController {

    @FieldTest
    public String field;
    @FieldTest
    public Thread thread;

    public Button buttonRocket1;

    @MethodTest
    public boolean method1(){
        return false;
    }

    @MethodTest
    public boolean method2(int testParam) {
        return false;
    }

    @MethodTest
    public boolean method2(){
        return false;
    }
}

