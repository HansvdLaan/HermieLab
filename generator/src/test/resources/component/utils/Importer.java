package component.utils;
import annotations.util.ParameterGenerator;
import settings.transformers.ParameterGeneratorTransformer;
import settings.transformers.PredicateTransformer;
public class Importer {

    public Importer(){
        //To make sure certain classes are loaded.
        System.out.print(PredicateTransformer.getInstance().toString());
        System.out.print(ParameterGeneratorTransformer.getInstance().toString());
    }
}
