package hermielab.annotations.abstraction;

import hermielab.annotations.abstraction.repeat.WidgetSymbols;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/**
 * The annotation type {@code hermielab.annotations.abstraction.WidgetSymbol} is
 * used to create input symbols for events which could be handled by this widget.
 * When you work with WidgetSymbols, please make sure to have the widgetNFA.jff file in the same
 * folder as your checkers.xml file. The widgetNFA.jff file can be found in the resource
 * folder of the HermieLab project.
 */
@Target(ElementType.FIELD)
@Repeatable(WidgetSymbols.class)
public @interface WidgetSymbol {
    /**
     * The ID for this widget. This ID is used in the name generation for the input symbols.
     * @return the costum ID of this field
     */
    String widgetID();
    /**
     * The events for which the handling input symbols should be created. Currently the supported events are:
     * "mouse_drag_detect", "mouse_click", "mouse_drag", "mouse_enter", "mouse_exit",
     * "mouse_move", "mouse_press" and "mouse_release"
     * @return the events for which the handling input symbols should be created
     */
    String[] events();
    /**
     * The output functions to get the output of the newly defined input symbols.
     * Use "this" to use the output of the input function as output.
     * @return output methods;
     */
    String[] outputIDs();
    /**
     * Predicates which should hold before any of the generated input symbols can be passed to the SUT.
     * @return predicates of the input symbols
     */
    String[] predicates() default {};
    String[] nfapredicates() default {};
}
