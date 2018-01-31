package checker;

import adapter.Adapter;
import mapper.ConcreteInvocation;

public class PredicateChecker<AI,AO,CO> implements Checker<AI,AO, ConcreteInvocation,CO> {

    private Adapter adapter;

    public PredicateChecker(Adapter adapter){
        this.adapter = adapter;
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
    public boolean checkConcreteInput(ConcreteInvocation concreteInput) {
        return adapter.checkPredicates(concreteInput.getPredicateIDs());
    }

    @Override
    public boolean checkConcreteOutput(CO concreteOutput) {
        return true;
    }

    @Override
    public void reset() {

    }
}
