package checker;

public interface Checker<AI,AO,CI,CO> {

    public boolean checkAbstractInput(AI abstractInput);

    public boolean checkAbstractOutput(AO abstractOutput);

    public boolean checkConcreteInput(CI concreteInput);

    public boolean checkConcreteOutput(CO concreteOutput);

    public void reset();
}
