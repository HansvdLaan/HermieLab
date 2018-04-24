package hermielab.core.checker.predicate;

import hermielab.core.adapter.Adapter;
import hermielab.core.checker.Checker;

import java.util.*;

public class PredicateChecker<AI,AO,CI,CO> implements Checker<AI,AO,CI,CO> {

    private Adapter adapter;
    private Map<String,List<String>> predicates;

    public PredicateChecker(Adapter adapter, Map<String,List<String>> predicates){
        this.adapter = adapter;
        this.predicates = predicates;
    }

    @Override
    public boolean checkAbstractInput(AI abstractInput) {
        return true;
    }

    @Override
    public boolean checkAbstractOutput(AO abstractOutput) {
        return true;
    }

    @Override
    public boolean checkConcreteInput(CI concreteInput) {
        String symbol = concreteInput.toString();
        List<String> predicates = this.predicates.get(symbol);
        return adapter.checkPredicates(predicates);
    }

    @Override
    public boolean checkConcreteOutput(CO concreteOutput) {
        return true;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public Map<String, List<String>> getPredicates() {
        return predicates;
    }

    public void setPredicates(Map<String, List<String>> predicates) {
        this.predicates = predicates;
    }

    public void addPredicate(String inputID, String predicateID){
        predicates.putIfAbsent(inputID, new ArrayList<>());
        predicates.get(inputID).add(predicateID);
    }

    public void addPredicates(String inputID, List<String> predicateIDs){
        predicateIDs.forEach(predicateID -> addPredicate(inputID, predicateID));
    }

    @Override
    public void reset() {

    }
}
