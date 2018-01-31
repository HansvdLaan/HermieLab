package mapper;

import java.util.List;

public class ConcreteInvocation {

    private String inputInstanceID;
    private String outputInstanceID;
    private List<String> predicateIDs; //atm only for the inputs

    public ConcreteInvocation(String inputInstanceID, String outputInstanceID, List<String> predicateIDs) {
        this.inputInstanceID = inputInstanceID;
        this.outputInstanceID = outputInstanceID;
        this.predicateIDs = predicateIDs;
    }

    public String getInputInstanceID() {
        return inputInstanceID;
    }

    public void setInputInstanceID(String instanceID) {
        this.inputInstanceID = instanceID;
    }

    public String getOutputInstanceID() {
        return outputInstanceID;
    }

    public void setOutputInstanceID(String outputInstanceID) {
        this.outputInstanceID = outputInstanceID;
    }

    public List<String> getPredicateIDs() {
        return predicateIDs;
    }

    public void setPredicateIDs(List<String> predicateIDs) {
        this.predicateIDs = predicateIDs;
    }

    @Override
    public String toString() {
        return "ConcreteInvocation{" +
                "inputInstanceID='" + inputInstanceID + '\'' +
                ", outputInstanceID='" + outputInstanceID + '\'' +
                ", predicates=" + predicateIDs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConcreteInvocation that = (ConcreteInvocation) o;

        if (inputInstanceID != null ? !inputInstanceID.equals(that.inputInstanceID) : that.inputInstanceID != null)
            return false;
        if (outputInstanceID != null ? !outputInstanceID.equals(that.outputInstanceID) : that.outputInstanceID != null)
            return false;
        return predicateIDs != null ? predicateIDs.equals(that.predicateIDs) : that.predicateIDs == null;
    }

}
