package components.utils;

import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import com.squareup.javapoet.TypeSpec;
import component.ComponentGenerator;
import component.adapter.AdapterGenerator;
import component.adapter.AdapterTransformations;
import org.dom4j.DocumentException;
import org.junit.BeforeClass;
import org.junit.Test;
import settings.Settings;
import settings.containers.GeneratorInformationElement;
import utils.ClassUtils;
import utils.DummyPreProcessor;

import javax.tools.JavaFileObject;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;


public final class AggregatorGeneratorTests {


    public static Settings inputFunctionSettings; //Input function with no parameters

    public static Settings outputFunctionSettings; //Input function with no parameters

    public static Settings predicateSettings; //Predicate function with no parameters

    public static Settings parameterGeneratorSettings; //User-defined field parameter

    @BeforeClass
    public static void setUpProcessor() throws DocumentException, MalformedURLException {
        DummyPreProcessor processor = (DummyPreProcessor) Compiler.javac().withProcessors(new DummyPreProcessor()).compile(
                JavaFileObjects.forResource(Paths.get("tests","testcode","utils","GeneratorUtilsTestClass.java").toFile().toURL()),
                JavaFileObjects.forResource(Paths.get("tests","testcode","utils","Importer.java").toFile().toURL())
                ).compiler().processors().get(0);
        ClassUtils.getInstance().setPrEnv(processor.getPreEnv());
    }
    
    //Basic Scenario, just a few Input Functions, a few Output Functions, a few Predicate Functions and a start Method
    //There is one Input, one Output and one Predicate function which only has a reference
    //There is one basic ParameterGenerator and one Predicate reference.
    public static Settings scenario1Settings;

    
    /// The Setup Methods ///
    @BeforeClass
    public static void setUpInputFunctionSettings() {
        String type = "inputfunction";

        inputFunctionSettings = new Settings();
        Map<String,Object> data1 = new HashMap<>();
        data1.put("class","testcode.utils.GeneratorUtilsTestClass");
        data1.put("method","method1()");
        GeneratorInformationElement element1 = new GeneratorInformationElement(type, "InputFunction1", data1);
        inputFunctionSettings.addSettingsByTypeAndID(element1.getType(), element1.getId(), element1);

        Map<String,Object> data2 = new HashMap<>();
        data2.put("class","testcode.utils.GeneratorUtilsTestClass");
        data2.put("method","method2(boolean)");
        data2.put("param#1","boolean:true");
        GeneratorInformationElement element2 = new GeneratorInformationElement(type, "InputFunction2", data2);
        inputFunctionSettings.addSettingsByTypeAndID(element2.getType(), element2.getId(), element2);

        Map<String,Object> data3 = new HashMap<>();
        data3.put("class","testcode.utils.GeneratorUtilsTestClass");
        data3.put("method","method3(java.lang.String)");
        data3.put("param#1","string:blabla");
        GeneratorInformationElement element3 = new GeneratorInformationElement(type, "InputFunction3", data3);
        inputFunctionSettings.addSettingsByTypeAndID(element3.getType(), element3.getId(), element3);

        Map<String,Object> data4 = new HashMap<>();
        data4.put("class","testcode.utils.GeneratorUtilsTestClass");
        data4.put("method","method4(javafx.scene.input.MouseEvent)");
        data4.put("param#1","javaFX:mouse_press");
        GeneratorInformationElement element4 = new GeneratorInformationElement(type, "InputFunction4", data4);
        inputFunctionSettings.addSettingsByTypeAndID(element4.getType(), element4.getId(), element4);

        Map<String,Object> data5 = new HashMap<>();
        data5.put("class","testcode.utils.GeneratorUtilsTestClass");
        data5.put("method","method5(testcode.utils.UserClass)");
        GeneratorInformationElement element5 = new GeneratorInformationElement(type, "InputFunction5", data5);
        inputFunctionSettings.addSettingsByTypeAndID(element5.getType(), element5.getId(), element5);

        inputFunctionSettings.addSettingsByTypeAndID("inputfunction", "InputFunction6", null);
    }

    @BeforeClass
    public static void setUpOutputFunctionSettings() {
        String type = "outputfunction";

        outputFunctionSettings = new Settings();
        Map<String,Object> data1 = new HashMap<>();
        data1.put("class","testcode.utils.GeneratorUtilsTestClass");
        data1.put("method","method1()");
        GeneratorInformationElement element1 = new GeneratorInformationElement(type, "OutputFunction1", data1);
        outputFunctionSettings.addSettingsByTypeAndID(element1.getType(), element1.getId(), element1);

        Map<String,Object> data2 = new HashMap<>();
        data2.put("class","testcode.utils.GeneratorUtilsTestClass");
        data2.put("method","method2(boolean)");
        data2.put("param#1","boolean:true");
        GeneratorInformationElement element2 = new GeneratorInformationElement(type, "OutputFunction2", data2);
        outputFunctionSettings.addSettingsByTypeAndID(element2.getType(), element2.getId(), element2);

        Map<String,Object> data3 = new HashMap<>();
        data3.put("class","testcode.utils.GeneratorUtilsTestClass");
        data3.put("method","method3(java.lang.String)");
        data3.put("param#1","string:blabla");
        GeneratorInformationElement element3 = new GeneratorInformationElement(type, "OutputFunction3", data3);
        outputFunctionSettings.addSettingsByTypeAndID(element3.getType(), element3.getId(), element3);

        Map<String,Object> data4 = new HashMap<>();
        data4.put("class","testcode.utils.GeneratorUtilsTestClass");
        data4.put("method","method4(javafx.scene.input.MouseEvent)");
        data4.put("param#1","javaFX:mouse_press");
        GeneratorInformationElement element4 = new GeneratorInformationElement(type, "OutputFunction4", data4);
        outputFunctionSettings.addSettingsByTypeAndID(element4.getType(), element4.getId(), element4);

        Map<String,Object> data5 = new HashMap<>();
        data5.put("class","testcode.utils.GeneratorUtilsTestClass");
        data5.put("method","method5(testcode.utils.UserClass)");
        GeneratorInformationElement element5 = new GeneratorInformationElement(type, "OutputFunction5", data5);
        outputFunctionSettings.addSettingsByTypeAndID(element5.getType(), element5.getId(), element5);

        outputFunctionSettings.addSettingsByTypeAndID("inputfunction", "OutputFunction6", null);
    }

    @BeforeClass
    public static void setUpPredicateFunctionSettings() {
        String type = "predicate";

        predicateSettings = new Settings();
        Map<String,Object> data1 = new HashMap<>();
        data1.put("class","testcode.utils.GeneratorUtilsTestClass");
        data1.put("method","method1()");
        GeneratorInformationElement element1 = new GeneratorInformationElement(type, "Predicate1", data1);
        predicateSettings.addSettingsByTypeAndID(element1.getType(), element1.getId(), element1);

        Map<String,Object> data2 = new HashMap<>();
        data2.put("class","testcode.utils.GeneratorUtilsTestClass");
        data2.put("method","method2(boolean)");
        data2.put("param#1","boolean:true");
        GeneratorInformationElement element2 = new GeneratorInformationElement(type, "Predicate2", data2);
        predicateSettings.addSettingsByTypeAndID(element2.getType(), element2.getId(), element2);

        Map<String,Object> data3 = new HashMap<>();
        data3.put("class","testcode.utils.GeneratorUtilsTestClass");
        data3.put("method","method3(java.lang.String)");
        data3.put("param#1","string:blabla");
        GeneratorInformationElement element3 = new GeneratorInformationElement(type, "Predicate3", data3);
        predicateSettings.addSettingsByTypeAndID(element3.getType(), element3.getId(), element3);

        Map<String,Object> data4 = new HashMap<>();
        data4.put("class","testcode.utils.GeneratorUtilsTestClass");
        data4.put("method","method4(javafx.scene.input.MouseEvent)");
        data4.put("param#1","javaFX:mouse_press");
        GeneratorInformationElement element4 = new GeneratorInformationElement(type, "Predicate4", data4);
        predicateSettings.addSettingsByTypeAndID(element4.getType(), element4.getId(), element4);

        Map<String,Object> data5 = new HashMap<>();
        data5.put("class","testcode.utils.GeneratorUtilsTestClass");
        data5.put("method","method5(testcode.utils.UserClass)");
        GeneratorInformationElement element5 = new GeneratorInformationElement(type, "Predicate5", data5);
        predicateSettings.addSettingsByTypeAndID(element5.getType(), element5.getId(), element5);

        predicateSettings.addSettingsByTypeAndID("inputfunction", "Predicate6", null);
    }

    @BeforeClass
    public static void setUpParameterGeneratorSettings() {
        String type = "predicate";

        parameterGeneratorSettings = new Settings();
        Map<String,Object> data1 = new HashMap<>();
        data1.put("class","testcode.utils.GeneratorUtilsTestClass");
        data1.put("field","fieldX");
        GeneratorInformationElement element1 = new GeneratorInformationElement(type, "ParameterGenerator1", data1);
        parameterGeneratorSettings.addSettingsByTypeAndID(element1.getType(), element1.getId(), element1);

        Map<String,Object> data2 = new HashMap<>();
        data2.put("class","testcode.utils.GeneratorUtilsTestClass");
        data2.put("method","method1()");
        GeneratorInformationElement element2 = new GeneratorInformationElement(type, "ParameterGenerator2", data2);
        parameterGeneratorSettings.addSettingsByTypeAndID(element2.getType(), element2.getId(), element2);

        parameterGeneratorSettings.addSettingsByTypeAndID("parametergenerator", "ParameterGenerator2", null);
    }

    /// Testing Input Functions ///
    
    @Test
    public void basicInputFunctionAggregatorTest(){
        ComponentGenerator generator = new AdapterGenerator(inputFunctionSettings);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.addTransformation(AdapterTransformations.GenerateSwitchCaseAggregators);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();

    }

    /// Testing Output Functions ///

    @Test
    public void basicOutputFunctionAggregatorTest(){
        ComponentGenerator generator = new AdapterGenerator(outputFunctionSettings);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.addTransformation(AdapterTransformations.GenerateSwitchCaseAggregators);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
    }

    /// Testing Predicates ///

    @Test
    public void basicPredicateAggregatorTest(){
        ComponentGenerator generator = new AdapterGenerator(predicateSettings);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.addTransformation(AdapterTransformations.GenerateSwitchCaseAggregators);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
    }

    

}