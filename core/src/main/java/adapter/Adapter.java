package adapter;

import java.util.List;
import java.util.Optional;

public abstract class Adapter<AO, CO> {

    public CO process(String inputID) {
        Optional<CO> output = processInput(inputID);
        if (output.isPresent()){
            return output.get();
        } else {
            throw new IllegalArgumentException("method with inputID: " + inputID + " returned an empty optional");
        }
    }

    public CO process(String inputID, String outputID) {
        processInput(inputID);
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

}
