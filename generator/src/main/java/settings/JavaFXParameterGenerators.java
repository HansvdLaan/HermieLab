package settings;

import javafx.event.Event;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaFXParameterGenerators {

    private static JavaFXParameterGenerators ourInstance = new JavaFXParameterGenerators();

    public static Map<String,Map<String,Object>> getMappings(){
        Map<String,Map<String,Object>> mappings = new HashMap<>();
        List<String> IDs = Arrays.asList("javaFX:mouse_drag_detect","javaFX:mouse_click","javaFX:mouse_drag","javaFX:mouse_enter",
                "javaFX:mouse_exit","javaFX:mouse_move","javaFX:mouse_press","javaFX:mouse_release");
        List<String> methods = Arrays.asList("generatePMBDragDetectedEvent()","generatePMBClickedEvent()","generatePMBDraggedEvent()","generatePMBEnteredEvent()",
                "generatePMBExitedEvent()","generatePMBMovedEvent()","generatePMBPressedEvent()","generatePMBReleasedEvent()");
        for (int i = 0; i < IDs.size(); i++){
            String ID = IDs.get(i);
            String method = methods.get(i);
            mappings.put(ID, new HashMap<>());
            mappings.get(ID).put("method",method);
            mappings.get(ID).put("class","settings.JavaFXParameterGenerators");
        }
        return mappings;
    }

    // Primary Mouse Button Events //
    public static Event generatePMBDragDetectedEvent(){
        return new MouseEvent(MouseEvent.DRAG_DETECTED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false,
                true, false, false, true, false, false, null);
    }

    public static Event generatePMBClickedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false,
                true, false, false, true, false, false, null);
    }

    public static Event generatePMBDraggedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_DRAGGED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false,
                true, false, false, true, false, false, null);
    }

    public static Event generatePMBEnteredEvent(){
        return new MouseEvent(MouseEvent.MOUSE_ENTERED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false,
                true, false, false, true, false, false, null);
    }

    public static Event generatePMBExitedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_EXITED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false,
                true, false, false, true, false, false, null);
    }

    public static Event generatePMBMovedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_MOVED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false,
                true, false, false, true, false, false, null);
    }

    public static Event generatePMBPressedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_PRESSED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false,
                true, false, false, true, false, false, null);
    }

    public static Event generatePMBReleasedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_RELEASED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false,
                true, true, false, true, false, false, null);
    }


    // Secondary Mouse Button Events //
    public static Event generateSMBDragDetectedEvent(){
        return new MouseEvent(MouseEvent.DRAG_DETECTED, 0,
                0, 0, 0, MouseButton.SECONDARY, 1, false, false, false, false,
                false, false, true, true, false, false, null);
    }

    public static Event generateSMBClickedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, MouseButton.SECONDARY, 1, false, false, false, false,
                false, false, true, true, false, false, null);
    }

    public static Event generateSMBDraggedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_DRAGGED, 0,
                0, 0, 0, MouseButton.SECONDARY, 1, false, false, false, false,
                false, false, true,  true, false, false, null);
    }

    public static Event generateSMBEnteredEvent(){
        return new MouseEvent(MouseEvent.MOUSE_ENTERED, 0,
                0, 0, 0, MouseButton.SECONDARY, 1, false, false, false, false,
                false, false, true,  true, false, false, null);
    }

    public static Event generateSMBExitedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_EXITED, 0,
                0, 0, 0, MouseButton.SECONDARY, 1, false, false, false, false,
                false, false, true,  true, false, false, null);
    }

    public static Event generateSMBMovedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_MOVED, 0,
                0, 0, 0, MouseButton.SECONDARY, 1, false, false, false, false,
                false, false, true,  true, false, false, null);
    }

    public static Event generateSMBPressedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_PRESSED, 0,
                0, 0, 0, MouseButton.SECONDARY, 1, false, false, false, false,
                false, false, true,  true, false, false, null);
    }

    public static Event generateSMBReleasedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_RELEASED, 0,
                0, 0, 0, MouseButton.SECONDARY, 1, false, false, false, false,
                false, false, true,  true, false, false, null);
    }


    // Middle Mouse Button Events //
    public static Event generateMMBDragDetectedEvent(){
        return new MouseEvent(MouseEvent.DRAG_DETECTED, 0,
                0, 0, 0, MouseButton.MIDDLE, 1, false, false, false, false,
                false, true, false, true, false, false, null);
    }

    public static Event generateMMBClickedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, MouseButton.MIDDLE, 1, false, false, false, false,
                false, true, false, true, false, false, null);
    }

    public static Event generateMMBDraggedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_DRAGGED, 0,
                0, 0, 0, MouseButton.MIDDLE, 1, false, false, false, false,
                false, true, false, true, false, false, null);
    }

    public static Event generateMMBEnteredEvent(){
        return new MouseEvent(MouseEvent.MOUSE_ENTERED, 0,
                0, 0, 0, MouseButton.MIDDLE, 1, false, false, false, false,
                false, true, false, true, false, false, null);
    }

    public static Event generateMMBExitedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_EXITED, 0,
                0, 0, 0, MouseButton.MIDDLE, 1, false, false, false, false,
                false, true, false, true, false, false, null);
    }

    public static Event generateMMBMovedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_MOVED, 0,
                0, 0, 0, MouseButton.MIDDLE, 1, false, false, false, false,
                false, true, false, true, false, false, null);
    }

    public static Event generateMMBPressedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_PRESSED, 0,
                0, 0, 0, MouseButton.MIDDLE, 1, false, false, false, false,
                false, true, false, true, false, false, null);
    }

    public static Event generateMMBReleasedEvent(){
        return new MouseEvent(MouseEvent.MOUSE_RELEASED, 0,
                0, 0, 0, MouseButton.MIDDLE, 1, false, false, false, false,
                false, true, false, true, false, false, null);
    }


    public static JavaFXParameterGenerators getInstance() {
        return ourInstance;
    }

    private JavaFXParameterGenerators() {
    }

}
