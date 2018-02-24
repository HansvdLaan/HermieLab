package checker.nfa;

import edu.duke.cs.jflap.automata.fsa.FiniteStateAutomaton;

import java.util.*;

public class SingleNFAChecker extends NFAChecker {

    public SingleNFAChecker(){
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
        if (getGroupedNFAsAlphabets().containsKey(groupID)){
            throw new IllegalArgumentException("SingleNFAChecker already has a NFA in the group:" + groupID + ", a SingleNFAChecker can only have one NFA per group");
        } else {
            addGroupedNFA(groupID, automaton);
        }
    }

    @Override
    public void deintergrateNFA(String groupID, String nfaID, FiniteStateAutomaton automaton){
        if (!getGroupedNFAsAlphabets().containsKey(groupID)){
            throw new IllegalArgumentException("SingleNFAChecker does not contain the NFA group:" + groupID);
        } else {
            removeGroupedNFA(groupID);
        }
    }

    @Override
    public void replaceNFA(String groupID, String nfaID, FiniteStateAutomaton automaton){
        removeNFA(groupID, nfaID, automaton);
        addNFA(groupID, nfaID, automaton);
    }


}
