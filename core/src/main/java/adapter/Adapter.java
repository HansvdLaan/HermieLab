package adapter;

import java.util.List;
import java.util.Optional;

public abstract class Adapter<AO, CO> {

    private Optional<CO> output;

    public CO process(String inputID, String outputID) {
        output = processInput(inputID);
        return processOutput(outputID);
    }

    public abstract Optional<CO> processInput(String inputID);

    public abstract CO processOutput(String outputID);

    public boolean checkPredicates(List<String> predicateIDs) {
        boolean valid = true;
        for (String predicateID: predicateIDs){
            valid = valid && checkPredicate(predicateID);
        }
        return valid;
    }

    public abstract boolean checkPredicate(String predicateID);

    public abstract AO transformOutput(String id, CO concreteOutput); //Could change object to AO

    public abstract void start();

    public abstract void initialize();

    public abstract void shutdown();

    public abstract void preQuery();

    public abstract void postQuery();

    public abstract void preInputInvocation();

    public abstract void postInputInvocation();

    public abstract void preOutputInvocation();

    public abstract void postOutputInvocation();

    public CO getInputCO() {
        if (output.isPresent()) {
            return this.output.get();
        }
        else {
            throw new IllegalStateException("Optional output is empty");
        }
    }

}
