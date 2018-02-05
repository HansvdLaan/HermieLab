package adapter;

import java.util.Optional;

public class MyAdapter extends Adapter<String,Object>{

    private boolean correct;

    public MyAdapter() {}

    @Override
    public Optional<Object> processInput(String inputID) {
        switch (inputID){
            case "a":
                return Optional.of(this.a());
            case "b":
                return Optional.of(this.b());
            case "c":
                return Optional.of(this.c());
            case "d":
                this.d();
                return Optional.empty();
            default:
                throw new IllegalArgumentException("unkown inputID: " + inputID);
        }
    }

    @Override
    public Boolean processOutput(String outputID) {
        switch (outputID){
            case "output":
                return this.output();
            case "this":
                return (Boolean) this.getInputCO();
            default:
                throw new IllegalArgumentException("unkown outputID: " + outputID);
        }
    }

    @Override
    public boolean checkPredicate(String predicateID) {
        switch (predicateID){
            case "aPredicate":
                return this.aPredicate();
            case "bPredicate":
                return this.bPredicate();
            case "cPredicate":
                return this.cPredicate();
            case "dPredicate":
                return this.dPredicate();
            default:
                throw new IllegalArgumentException("unkown predicateID: " + predicateID);
        }
    }

    @Override
    public String transformOutput(String transformID, Object concreteOutput) {
        switch (transformID){
            case "booleanTransform":
                return booleanTransform((Boolean) concreteOutput);
            case "stringTransform":
                return stringTransform((String) concreteOutput);
            default:
                throw new IllegalArgumentException("unkown transformID:" + transformID);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void preQuery() {

    }

    @Override
    public void postQuery() {

    }

    @Override
    public void preInputInvocation() {

    }

    @Override
    public void postInputInvocation() {

    }

    @Override
    public void preOutputInvocation() {

    }

    @Override
    public void postOutputInvocation() {

    }

    public boolean a() {
        correct = true;
        return true;
    }

    public boolean b() {
        correct = false;
        return false;
    }

    public boolean c() {
        correct = true;
        return true;
    }

    public void d() {
        correct = true;
    }

    public boolean output() {
        return correct;
    }

    public boolean aPredicate(){
        return true;
    }

    public boolean bPredicate(){
        return true;
    }

    public boolean cPredicate(){
        return false;
    }

    public boolean dPredicate(){
        return true;
    }

    public static String booleanTransform(Boolean bool){
        return "tB:" + String.valueOf(bool);
    }
    public static String stringTransform(String string){
        return "tS:" + String.valueOf(string);
    }

}
