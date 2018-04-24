package hermielab.generator.component.injector;

import hermielab.generator.component.ComponentGenerator;
import hermielab.generator.settings.Settings;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Hans on 4-2-2018.
 */
public class ApplicationInjectorGenerator extends ComponentGenerator {

    public static String CLASSNAME = "ApplicationInjector";
    public static String PACKAGENAME = "hermielab";
    public static List<String> METHODTYPEORDER = Arrays.asList("constructor","instancemethod");
    public static List<String> FIELDTYPEORDER = Arrays.asList("instancefield");

    public ApplicationInjectorGenerator(Settings settings){
        super(settings, PACKAGENAME, CLASSNAME);
        setFieldTypeOrder(FIELDTYPEORDER);
        setMethodTypeOrder(METHODTYPEORDER);
        //getInterfaces().add(InstanceManager.class);

        //this.addTransformation(ApplicationInjectorTransformations.GenerateClassInstancesField);
        this.addTransformation(ApplicationInjectorTransformations.GenerateObservedClasssesField);
        //this.addTransformation(ApplicationInjectorTransformations.GenerateOurInstanceField);

        //this.addTransformation(ApplicationInjectorTransformations.GenerateGetInstanceMethod);
        //this.addTransformation(ApplicationInjectorTransformations.GenerateGetClassInstanceMethod);
        this.addTransformation(ApplicationInjectorTransformations.GenerateInjectMethod);
        this.addTransformation(ApplicationInjectorTransformations.GenerateFillObservedClassesSetMethod);
        //this.addTransformation(ApplicationInjectorTransformations.GenerateAddClassInstanceMethod);

        this.addTransformation(ApplicationInjectorTransformations.GenerateConstructor);
    }
}
