package component.adapter;

import adapter.Adapter;
import component.ComponentGenerator;
import settings.Settings;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Hans on 4-2-2018.
 */
public class AdapterGenerator extends ComponentGenerator {

    public static String CLASSNAME = "ApplicationAdapter";
    public static List<String> METHODTYPEORDER = Arrays.asList("constructor","parametergenerator","predicate","inputfunction","outputfunction");
    public static List<String> FIELDTYPEORDER = Arrays.asList("");

    public AdapterGenerator(Settings settings){
        super(settings,CLASSNAME);
        setFieldTypeOrder(FIELDTYPEORDER);
        setMethodTypeOrder(METHODTYPEORDER);
        addTransformation(AdapterTransformations.GenerateWrappedMethods);
        this.addTransformation(AdapterTransformations.GenerateConstructor);
        this.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        this.addTransformation(AdapterTransformations.GenerateSwitchCaseAggregators);
        this.addTransformation(AdapterTransformations.GenerateAggregators);
    }
    public AdapterGenerator() {
        super(CLASSNAME);
        setFieldTypeOrder(FIELDTYPEORDER);
        setMethodTypeOrder(METHODTYPEORDER);
        addTransformation(AdapterTransformations.GenerateWrappedMethods);
        this.addTransformation(AdapterTransformations.GenerateConstructor);
        this.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        this.addTransformation(AdapterTransformations.GenerateSwitchCaseAggregators);
        this.addTransformation(AdapterTransformations.GenerateAggregators);
    }
}
