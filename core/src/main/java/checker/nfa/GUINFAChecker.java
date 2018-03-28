package checker.nfa;

import edu.duke.cs.jflap.automata.State;
import edu.duke.cs.jflap.automata.Transition;
import edu.duke.cs.jflap.automata.fsa.FSATransition;
import edu.duke.cs.jflap.automata.fsa.FiniteStateAutomaton;
import net.automatalib.automata.fsa.NFA;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

public class GUINFAChecker<AI,AO,CI,CO> extends NFAChecker {


    public GUINFAChecker(){
        super();
    }

    @Override
    public boolean checkAbstractInput(Object abstractInput) {
        return true;
    }

    @Override
    public boolean checkAbstractOutput(Object abstractOutput) {
        return true;
    }

    @Override
    public boolean checkConcreteOutput(Object concreteOutput) {
        return true;
    }

    @Override
    public void intergrateNFA(String groupID, String nfaID, FiniteStateAutomaton automaton){
        getGroupedNFAs().putIfAbsent(groupID, new FiniteStateAutomaton());
        FiniteStateAutomaton groupedNFA = (FiniteStateAutomaton) getGroupedNFAs().get(groupID); //TODO: Why is this cast necessary??
        Map<String,State> newStates = new HashMap<>(); //The new states created in the groupedNFA

        for (State state: automaton.getStates()){
            State newState = groupedNFA.createState(new Point());
            newState.setName(nfaID + "_" + state.getName());
            newState.setLabel(state.getName());
            newStates.put(newState.getLabel(), newState);
            if (automaton.isFinalState(state)) {
                groupedNFA.addFinalState(newState);
            }
        }

        if (automaton.getInitialState() != null) {
            groupedNFA.setInitialState(newStates.get(automaton.getInitialState().getName()));
        }

        for (Transition transition: automaton.getTransitions()){
            String oldFromStateName = transition.getFromState().getName();
            State newFromState = newStates.get(oldFromStateName);
            String oldToStateName = transition.getToState().getName();
            State newToState = newStates.get(oldToStateName);
            String label = ((FSATransition)transition).getLabel();
            groupedNFA.addTransition(new FSATransition(newFromState,newToState, label));
        }

        //Binds all the states with the same previous name together
        Set<State> oldStates = Arrays.stream(groupedNFA.getStates()).filter(state -> !newStates.values().contains(state)).collect(Collectors.toSet());
        for (State oldState: oldStates){
            String oldName = oldState.getLabel();
            if (newStates.containsKey(oldName)){
                State newState = newStates.get(oldName);
                groupedNFA.addTransition(new FSATransition(oldState,newState,""));
                groupedNFA.addTransition(new FSATransition(newState,oldState,""));
            }
        }

        addGroupedNFA(groupID, groupedNFA);
    }

    @Override
    public void deintergrateNFA(String groupID, String nfaID, FiniteStateAutomaton automaton) {
        FiniteStateAutomaton groupedNFA = (FiniteStateAutomaton) getGroupedNFAs().get(groupID);
        for (State state: automaton.getStates()){
            if (state.getName().matches(nfaID+"_.*")){
                groupedNFA.removeState(state);
            }
        }
        Set<String> removedSymbols = new HashSet<>(getAlphabet(automaton));
        removedSymbols.remove(getGroupedNFAsAlphabets().get(groupID));
        removedSymbols.forEach(removedSymbol -> ( (Set<String>) getGroupedNFAsWithSymbolMappings().get(removedSymbol)).remove(groupID));
    }

    @Override
    public void replaceNFA(String groupID, String nfaID, FiniteStateAutomaton automaton) {
        deintergrateNFA(groupID, nfaID, automaton);
        intergrateNFA(groupID, nfaID, automaton);
    }

}
