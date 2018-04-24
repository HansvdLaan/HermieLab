package hermielab.core.checker.nfa;

import edu.duke.cs.jflap.automata.State;
import edu.duke.cs.jflap.automata.fsa.FSATransition;
import edu.duke.cs.jflap.automata.fsa.FiniteStateAutomaton;
import edu.duke.cs.jflap.file.XMLCodec;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class NFABuilder {

    public static FiniteStateAutomaton generateNFA(File file, Map<String,Set<String>> transitionMappings){
        FiniteStateAutomaton templateNFA = (FiniteStateAutomaton)new XMLCodec().decode(file, null);
        FiniteStateAutomaton NFA = new FiniteStateAutomaton();
        Map<String,State> newStates = new HashMap<>();

        for (State state: templateNFA.getStates()){
            State newState = NFA.createState(new Point());
            newState.setName(state.getName());
            newState.setLabel(state.getLabel());
            newStates.put(newState.getName(), newState);
            if (templateNFA.isFinalState(state)) {
                NFA.addFinalState(newState);
            }
        }

        if (templateNFA.getInitialState() != null) {
            NFA.setInitialState(newStates.get(templateNFA.getInitialState().getName()));
        }

        List<FSATransition> transitions = Arrays.stream(templateNFA.getTransitions()).map(t -> (FSATransition) t).collect(Collectors.toList());
        for (FSATransition transition: transitions){
            State newFromState = newStates.get(transition.getFromState().getName());
            State newToState = newStates.get(transition.getToState().getName());
            if (!transitionMappings.containsKey(transition.getLabel())){
                NFA.addTransition(new FSATransition(newFromState, newToState, ""));
            } else {
                for (String newLabel: transitionMappings.get(transition.getLabel())){
                    NFA.addTransition(new FSATransition(newFromState, newToState, newLabel));
                }
            }
        }

        return NFA;
    }
}
