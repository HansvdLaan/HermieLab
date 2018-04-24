package hermielab.generator.component.adapter;

import hermielab.core.adapter.Adapter;
import hermielab.generator.component.ComponentGenerator;
import hermielab.generator.settings.Settings;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Hans on 4-2-2018.
 */
public class AdapterGenerator extends ComponentGenerator {

    public static String CLASSNAME = "ApplicationAdapter";
    public static String PACKAGENAME = "hermielab";
    public static List<String> METHODTYPEORDER = Arrays.asList("constructor","parametergenerator","predicate","inputfunction","outputfunction",
            "start", "initialize", "shutdown",
            "prequery","postquery",
            "preinputinvocation","postinputinvocation",
            "preoutputinvocation","postoutputinvocation","transform");
    public static List<String> FIELDTYPEORDER = Arrays.asList("instance");

    public AdapterGenerator(Settings settings){
        super(settings, PACKAGENAME, CLASSNAME);
        setFieldTypeOrder(FIELDTYPEORDER);
        setMethodTypeOrder(METHODTYPEORDER);
        setSuperClass(Adapter.class);
        this.addTransformation(AdapterTransformations.GenerateInstanceMangerField);
        this.addTransformation(AdapterTransformations.GenerateConstructor);
        this.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        this.addTransformation(AdapterTransformations.GenerateSwitchCaseAggregators);
        this.addTransformation(AdapterTransformations.GenerateAggregators);

    }
}
