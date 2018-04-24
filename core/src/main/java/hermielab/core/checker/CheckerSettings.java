package hermielab.core.checker;

import edu.duke.cs.jflap.automata.fsa.FiniteStateAutomaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckerSettings {

    private Map<String,Map<String,Map<String,FiniteStateAutomaton>>> nfaMap; //S1 = CheckerID, S2 = GroupID, S3 = NFAID
    private Map<String,List<String>> predicates; //Key = CI, Value = Predicates of this CI

    public CheckerSettings(){
        nfaMap = new HashMap<>();
        predicates = new HashMap<>();
    }

    public Map<String,Map<String,Map<String,FiniteStateAutomaton>>> getAllNFAs(){
        return nfaMap;
    }

    public void setAllNFAs(Map<String,Map<String,Map<String,FiniteStateAutomaton>>> settings) {
        this.nfaMap = settings;
    }

    public Map<String,Map<String,FiniteStateAutomaton>> getNFAs(String checkerID) {
        nfaMap.putIfAbsent(checkerID, new HashMap<>());
        return nfaMap.get(checkerID);
    }

    public void setNFAs(String checkerID, Map<String,Map<String,FiniteStateAutomaton>> settings){
        nfaMap.put(checkerID, settings);
    }

    public void addNFAs(String checkerID, Map<String,Map<String,FiniteStateAutomaton>> settings){
        getNFAs(checkerID).putAll(settings);
    }

    public Map<String, FiniteStateAutomaton> getNFAs(String checkerID, String groupID){
        return nfaMap.get(checkerID).get(groupID);
    }

    public void setNFAs(String checkerID, String groupID, Map<String, FiniteStateAutomaton> settings){
        nfaMap.get(checkerID).put(groupID,settings);
    }

    public void addNFA(String checkerID, String groupID, Map<String, FiniteStateAutomaton> settings){
        nfaMap.get(checkerID).putIfAbsent(groupID, new HashMap<>());
        nfaMap.get(checkerID).put(groupID, settings);
    }


    public FiniteStateAutomaton getNFA(String checkerID, String groupID, String nfaID){
        return nfaMap.get(checkerID).get(groupID).get(nfaID);
    }

    public void addNFA(String checkerID, String groupID, String nfaID, FiniteStateAutomaton nfa){
        nfaMap.putIfAbsent(checkerID,new HashMap<>());
        nfaMap.get(checkerID).putIfAbsent(groupID, new HashMap<>());
        nfaMap.get(checkerID).get(groupID).put(nfaID,nfa);
    }

    public Map<String,List<String>> getAllPredicates(){
        return predicates;
    }

    public List<String> getPredicates(String symbol){
        return predicates.get(symbol);
    }

    public void addPredicates(String symbol, List<String> predicates){
        this.predicates.putIfAbsent(symbol, new ArrayList<>());
        this.predicates.get(symbol).addAll(predicates);
    }

    public void setPredicates(String symbol, List<String> predicates){
        this.predicates.put(symbol,predicates);
    }

}
