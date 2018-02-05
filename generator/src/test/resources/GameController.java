import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import javafx.scene.control.Button;
import testutils.DummyAnnotation;

/**
 * Created by Hans on 4-1-2018.
 */

public class GameController {

    @DummyAnnotation
    public String field;

    @DummyAnnotation
    public Thread thread;

    @DummyAnnotation
    public Button buttonRocket1;

    public boolean method1(){
        return false;
    }

    public boolean method2(int testParam) {
        return false;
    }

    public boolean method2(){
        return false;
    }
}

