package testcode.utils;
import hermielab.generator.settings.transformers.ParameterGeneratorTransformer;
import hermielab.generator.settings.transformers.PredicateTransformer;
public class Importer {

    public Importer(){
        //To make sure certain classes are loaded.
        System.out.print(PredicateTransformer.getInstance().toString());
        System.out.print(ParameterGeneratorTransformer.getInstance().toString());
    }
}
