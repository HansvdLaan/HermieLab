package hermielab.core.checker.nfa;

import hermielab.core.checker.Checker;
import edu.duke.cs.jflap.automata.fsa.FSATransition;
import edu.duke.cs.jflap.automata.fsa.FiniteStateAutomaton;

import java.util.*;
import java.util.stream.Collectors;

public abstract class NFAChecker<AI,AO,CI,CO> implements Checker<AI,AO,CI,CO>{

    private Map<String,FiniteStateAutomaton> groupedNFAs;
    private Map<String, Set<String>> symbolToGroupedNFAMappings;
    private Map<String, Set<String>> groupedNFAsAlphabets;
    private Map<String,StepFSASimulator> FSASimulators;

    private Map<String,Map<String,FiniteStateAutomaton>> singleNFAs;
    private Map<String, Set<String>> singleNFAsAlphabets;


    public NFAChecker(){
        this.groupedNFAs = new HashMap<>();
        this.groupedNFAsAlphabets = new HashMap<>();
        this.singleNFAs = new HashMap<>();
        this.singleNFAsAlphabets = new HashMap<>();
        this.FSASimulators = new HashMap<>();
        this.symbolToGroupedNFAMappings = new HashMap<>();
    }

    @Override
    public boolean checkConcreteInput(CI concreteInput) {
        String symbol = concreteInput.toString();
        boolean accepted = true;
        Set<String> groupIDs = getGroupedNFAsWithSymbol(symbol);
        if (groupIDs != null) {
            for (String groupID : groupIDs) {
                StepFSASimulator simulator = getFSASimulators().get(groupID);
                accepted = simulator.checkStep(symbol);
                if (!accepted) {
                    break;
                }
            }
        }
        return accepted;
    }

    @Override
    public void reset() {
        for (StepFSASimulator simulator: getFSASimulators().values()){
            simulator.initialize();
        }
    }

    public void addNFA(String groupID, String nfaID, FiniteStateAutomaton automaton){
        singleNFAs.putIfAbsent(groupID, new HashMap<>());
        singleNFAs.get(groupID).put(nfaID, automaton);
        singleNFAsAlphabets.put(nfaID, getAlphabet(automaton));
        intergrateNFA(groupID, nfaID, automaton);
    }

    public void addNFAs(Map<String,Map<String, FiniteStateAutomaton>> nfas){
        for (String groupID: nfas.keySet()) {
            for (String nfaID : nfas.get(groupID).keySet()) {
                FiniteStateAutomaton nfa = nfas.get(groupID).get(nfaID);
                addNFA(groupID, nfaID, nfa);
            }
        }
    }

    public void addGroupedNFA(String groupID, FiniteStateAutomaton automaton){
        getGroupedNFAs().put(groupID, automaton);
        Set<String> alphabet = getAlphabet(automaton);
        getGroupedNFAsAlphabets().put(groupID,alphabet);
        alphabet.forEach(symbol -> addSymbolToNFAGroupMapping(symbol, groupID));
        setFSASimulator(groupID, new StepFSASimulator(automaton));
    }

    public void removeNFA(String groupID, String nfaID, FiniteStateAutomaton automaton){
        singleNFAs.get(groupID).remove(nfaID);
        singleNFAsAlphabets.remove(nfaID);
        deintergrateNFA(groupID, nfaID, automaton);
    }

    public void removeGroupedNFA(String groupID){
        Set<String> alphabet = getGroupedNFAsAlphabets().get(groupID); //TODO: why do I need this cast?
        getGroupedNFAsAlphabets().remove(groupID);
        alphabet.forEach(symbol -> getGroupedNFAsWithSymbolMappings().get(symbol).remove(groupID));
        getGroupedNFAs().remove(groupID);//TODO: why do I need this cast?
        getFSASimulators().remove(groupID);
    }
    public abstract void intergrateNFA(String groupID, String nfaID, FiniteStateAutomaton automaton);

    public abstract void deintergrateNFA(String groupID, String nfaID, FiniteStateAutomaton automaton);

    public abstract void replaceNFA(String groupID, String nfaID, FiniteStateAutomaton automaton);

    public Map<String, FiniteStateAutomaton> getGroupedNFAs() {
        return groupedNFAs;
    }

    public Map<String, Set<String>> getGroupedNFAsAlphabets() {
        return groupedNFAsAlphabets;
    }

    public Map<String, Map<String, FiniteStateAutomaton>> getSingleNFAs() {
        return singleNFAs;
    }

    public Map<String, Set<String>> getSingleNFAsAlphabets() {
        return singleNFAsAlphabets;
    }

    public Map<String, StepFSASimulator> getFSASimulators() {
        return FSASimulators;
    }

    public void setFSASimulator(String groupID, StepFSASimulator simulator){
        getFSASimulators().put(groupID, simulator);
    }

    public Set<String> getAlphabet(FiniteStateAutomaton automaton){
        return Arrays.stream(automaton.getTransitions()).map(t -> (FSATransition) t).map(FSATransition::getLabel).
                filter(string -> !string.equals("")) //lambda (empty) transitions are not part of the alphabet
                .collect(Collectors.toSet());
    }

    public Set<String> getGroupedNFAsWithSymbol(String symbol) {
        return symbolToGroupedNFAMappings.get(symbol);
    }

    public Map<String, Set<String>> getGroupedNFAsWithSymbolMappings() {
        return symbolToGroupedNFAMappings;
    }

    public void addSymbolToNFAGroupMapping(String symbol, Set<String> nfaIDs) {
        nfaIDs.forEach(nfaID -> addSymbolToNFAGroupMapping(symbol, nfaID));
    }

    public void addSymbolToNFAGroupMapping(String symbol, String nfaID) {
        this.symbolToGroupedNFAMappings.putIfAbsent(symbol, new HashSet<>());
        this.symbolToGroupedNFAMappings.get(symbol).add(nfaID);
    }

}
