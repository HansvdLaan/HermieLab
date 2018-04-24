package hermielab.annotations.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * The annotation type {@code hermielab.annotations.setup.Start} is
 * used to indicate which method should be called to start the SUL and which kind of automaton should be learned.
 */
@Target(ElementType.METHOD)
public @interface Start {
    /**
     * Type of automaton to be learned by the experiment
     * Set this to "DFA" to learn a "DFA", set this to "MealyMachine" to learn a Mealy Machine
     * @return type of automaton to be learned by the experiment
     */
    String automaton();
}
