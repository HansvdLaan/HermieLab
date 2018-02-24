package components.utils;

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

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.google.testing.compile.Compiler.javac;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public final class WrapperGeneratorTests {


    public static Settings simpleInputFunctionSettings; //Input function with no parameters
    public static Settings parametrizedInputFunctionSettings1; //Input function with a primitive parameter
    public static Settings parametrizedInputFunctionSettings2; //Input function with a String parameter
    public static Settings parametrizedInputFunctionSettings3; //Input function with a javaFX parameter generator
    public static Settings parametrizedInputFunctionSettings4; //Input function with no parameter while it should have a parameter
    public static Settings referencedInputFunctionSettings; //Referenced input function

    public static Settings simpleOutputFunctionSettings; //Input function with no parameters
    public static Settings parametrizedOutputFunctionSettings1; //Input function with a primitive parameter
    public static Settings parametrizedOutputFunctionSettings2; //Input function with a String parameter
    public static Settings parametrizedOutputFunctionSettings3; //Input function with a javaFX parameter generator
    public static Settings parametrizedOutputFunctionSettings4; //Input function with no parameter while it should have a parameter
    public static Settings referencedOutputFunctionSettings; //Referenced output function

    public static Settings simplePredicateSettings; //Predicate function with no parameters
    public static Settings parametrizedPredicateSettings1; //Predicate function with a primitive parameter
    public static Settings parametrizedPredicateSettings2; //Predicate function with a String parameter
    public static Settings parametrizedPredicateSettings3; //Predicate function with a javaFX parameter generator
    public static Settings parametrizedPredicateSettings4; //Predicate function with no parameter while it should have a parameter
    public static Settings referencedPredicateSettings; //Referenced predicate function 

    public static Settings fieldParameterGeneratorSettings; //User-defined field parameter
    public static Settings methodParameterGeneratorSettings; //User-defined parameter generator
    public static Settings referencedParameterGeneratorSettings; //Referenced parameter generator

    @BeforeClass
    public static void setUpProcessor() throws DocumentException, MalformedURLException {
        DummyPreProcessor processor = (DummyPreProcessor) javac().withProcessors(new DummyPreProcessor()).compile(
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

        simpleInputFunctionSettings = new Settings();
        Map<String,Object> data1 = new HashMap<>();
        data1.put("class","testcode.utils.GeneratorUtilsTestClass");
        data1.put("method","method1()");
        GeneratorInformationElement element1 = new GeneratorInformationElement(type, "InputFunction1", data1);
        simpleInputFunctionSettings.addSettingsByTypeAndID(element1.getType(), element1.getId(), element1);

        parametrizedInputFunctionSettings1 = new Settings();
        Map<String,Object> data2 = new HashMap<>();
        data2.put("class","testcode.utils.GeneratorUtilsTestClass");
        data2.put("method","method2(boolean)");
        data2.put("param#1","boolean:true");
        GeneratorInformationElement element2 = new GeneratorInformationElement(type, "InputFunction2", data2);
        parametrizedInputFunctionSettings1.addSettingsByTypeAndID(element2.getType(), element2.getId(), element2);

        parametrizedInputFunctionSettings2 = new Settings();
        Map<String,Object> data3 = new HashMap<>();
        data3.put("class","testcode.utils.GeneratorUtilsTestClass");
        data3.put("method","method3(java.lang.String)");
        data3.put("param#1","string:blabla");
        GeneratorInformationElement element3 = new GeneratorInformationElement(type, "InputFunction3", data3);
        parametrizedInputFunctionSettings2.addSettingsByTypeAndID(element3.getType(), element3.getId(), element3);

        parametrizedInputFunctionSettings3 = new Settings();
        Map<String,Object> data4 = new HashMap<>();
        data4.put("class","testcode.utils.GeneratorUtilsTestClass");
        data4.put("method","method4(javafx.scene.input.MouseEvent)");
        data4.put("param#1","javaFX:mouse_press");
        GeneratorInformationElement element4 = new GeneratorInformationElement(type, "InputFunction4", data4);
        parametrizedInputFunctionSettings3.addSettingsByTypeAndID(element4.getType(), element4.getId(), element4);

        parametrizedInputFunctionSettings4 = new Settings();
        Map<String,Object> data5 = new HashMap<>();
        data5.put("class","testcode.utils.GeneratorUtilsTestClass");
        data5.put("method","method5(testcode.utils.UserClass)");
        GeneratorInformationElement element5 = new GeneratorInformationElement(type, "InputFunction5", data5);
        parametrizedInputFunctionSettings4.addSettingsByTypeAndID(element5.getType(), element5.getId(), element5);

        referencedInputFunctionSettings = new Settings();
        referencedInputFunctionSettings.addSettingsByTypeAndID("inputfunction", "InputFunction6", null);
    }

    @BeforeClass
    public static void setUpOutputFunctionSettings() {
        String type = "outputfunction";

        simpleOutputFunctionSettings = new Settings();
        Map<String,Object> data1 = new HashMap<>();
        data1.put("class","testcode.utils.GeneratorUtilsTestClass");
        data1.put("method","method1()");
        GeneratorInformationElement element1 = new GeneratorInformationElement(type, "OutputFunction1", data1);
        simpleOutputFunctionSettings.addSettingsByTypeAndID(element1.getType(), element1.getId(), element1);

        parametrizedOutputFunctionSettings1 = new Settings();
        Map<String,Object> data2 = new HashMap<>();
        data2.put("class","testcode.utils.GeneratorUtilsTestClass");
        data2.put("method","method2(boolean)");
        data2.put("param#1","boolean:true");
        GeneratorInformationElement element2 = new GeneratorInformationElement(type, "OutputFunction2", data2);
        parametrizedOutputFunctionSettings1.addSettingsByTypeAndID(element2.getType(), element2.getId(), element2);

        parametrizedOutputFunctionSettings2 = new Settings();
        Map<String,Object> data3 = new HashMap<>();
        data3.put("class","testcode.utils.GeneratorUtilsTestClass");
        data3.put("method","method3(java.lang.String)");
        data3.put("param#1","string:blabla");
        GeneratorInformationElement element3 = new GeneratorInformationElement(type, "OutputFunction3", data3);
        parametrizedOutputFunctionSettings2.addSettingsByTypeAndID(element3.getType(), element3.getId(), element3);

        parametrizedOutputFunctionSettings3 = new Settings();
        Map<String,Object> data4 = new HashMap<>();
        data4.put("class","testcode.utils.GeneratorUtilsTestClass");
        data4.put("method","method4(javafx.scene.input.MouseEvent)");
        data4.put("param#1","javaFX:mouse_press");
        GeneratorInformationElement element4 = new GeneratorInformationElement(type, "OutputFunction4", data4);
        parametrizedOutputFunctionSettings3.addSettingsByTypeAndID(element4.getType(), element4.getId(), element4);

        parametrizedOutputFunctionSettings4 = new Settings();
        Map<String,Object> data5 = new HashMap<>();
        data5.put("class","testcode.utils.GeneratorUtilsTestClass");
        data5.put("method","method5(testcode.utils.UserClass)");
        GeneratorInformationElement element5 = new GeneratorInformationElement(type, "OutputFunction5", data5);
        parametrizedOutputFunctionSettings4.addSettingsByTypeAndID(element5.getType(), element5.getId(), element5);

        referencedOutputFunctionSettings = new Settings();
        referencedOutputFunctionSettings.addSettingsByTypeAndID("inputfunction", "OutputFunction6", null);
    }

    @BeforeClass
    public static void setUpPredicateFunctionSettings() {
        String type = "predicate";

        simplePredicateSettings = new Settings();
        Map<String,Object> data1 = new HashMap<>();
        data1.put("class","testcode.utils.GeneratorUtilsTestClass");
        data1.put("method","method1()");
        GeneratorInformationElement element1 = new GeneratorInformationElement(type, "Predicate1", data1);
        simplePredicateSettings.addSettingsByTypeAndID(element1.getType(), element1.getId(), element1);

        parametrizedPredicateSettings1 = new Settings();
        Map<String,Object> data2 = new HashMap<>();
        data2.put("class","testcode.utils.GeneratorUtilsTestClass");
        data2.put("method","method2(boolean)");
        data2.put("param#1","boolean:true");
        GeneratorInformationElement element2 = new GeneratorInformationElement(type, "Predicate2", data2);
        parametrizedPredicateSettings1.addSettingsByTypeAndID(element2.getType(), element2.getId(), element2);

        parametrizedPredicateSettings2 = new Settings();
        Map<String,Object> data3 = new HashMap<>();
        data3.put("class","testcode.utils.GeneratorUtilsTestClass");
        data3.put("method","method3(java.lang.String)");
        data3.put("param#1","string:blabla");
        GeneratorInformationElement element3 = new GeneratorInformationElement(type, "Predicate3", data3);
        parametrizedPredicateSettings2.addSettingsByTypeAndID(element3.getType(), element3.getId(), element3);

        parametrizedPredicateSettings3 = new Settings();
        Map<String,Object> data4 = new HashMap<>();
        data4.put("class","testcode.utils.GeneratorUtilsTestClass");
        data4.put("method","method4(javafx.scene.input.MouseEvent)");
        data4.put("param#1","javaFX:mouse_press");
        GeneratorInformationElement element4 = new GeneratorInformationElement(type, "Predicate4", data4);
        parametrizedPredicateSettings3.addSettingsByTypeAndID(element4.getType(), element4.getId(), element4);

        parametrizedPredicateSettings4 = new Settings();
        Map<String,Object> data5 = new HashMap<>();
        data5.put("class","testcode.utils.GeneratorUtilsTestClass");
        data5.put("method","method5(testcode.utils.UserClass)");
        GeneratorInformationElement element5 = new GeneratorInformationElement(type, "Predicate5", data5);
        parametrizedPredicateSettings4.addSettingsByTypeAndID(element5.getType(), element5.getId(), element5);

        referencedPredicateSettings = new Settings();
        referencedPredicateSettings.addSettingsByTypeAndID("inputfunction", "Predicate6", null);
    }

    @BeforeClass
    public static void setUpParameterGeneratorSettings() {
        String type = "predicate";

        fieldParameterGeneratorSettings = new Settings();
        Map<String,Object> data1 = new HashMap<>();
        data1.put("class","testcode.utils.GeneratorUtilsTestClass");
        data1.put("field","fieldX");
        GeneratorInformationElement element1 = new GeneratorInformationElement(type, "ParameterGenerator1", data1);
        fieldParameterGeneratorSettings.addSettingsByTypeAndID(element1.getType(), element1.getId(), element1);

        methodParameterGeneratorSettings = new Settings();
        Map<String,Object> data2 = new HashMap<>();
        data2.put("class","testcode.utils.GeneratorUtilsTestClass");
        data2.put("method","method1()");
        GeneratorInformationElement element2 = new GeneratorInformationElement(type, "ParameterGenerator2", data2);
        methodParameterGeneratorSettings.addSettingsByTypeAndID(element2.getType(), element2.getId(), element2);

        referencedParameterGeneratorSettings = new Settings();
        referencedParameterGeneratorSettings.addSettingsByTypeAndID("parametergenerator", "ParameterGenerator2", null);
    }

    /// Testing Input Functions ///
    
    @Test
    public void basicInputFunctionTest(){
        ComponentGenerator generator = new AdapterGenerator(simpleInputFunctionSettings);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"inputFunction1",0));
        assertTrue(JavaPoetTestUtils.isCorrectWrapperMethod(adapter, "inputFunction1", "method1",
                new ArrayList<>(), Void.TYPE));
    }
    
    @Test
    public void InputFunctionWithPrimitiveParameter(){
        ComponentGenerator generator = new AdapterGenerator(parametrizedInputFunctionSettings1);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"inputFunction2",0));
        assertTrue(JavaPoetTestUtils.isCorrectWrapperMethod(adapter, "inputFunction2", "method2",
                Arrays.asList("true"), Void.TYPE));
    }

    @Test
    public void InputFunctionWithStringParameter(){
        ComponentGenerator generator = new AdapterGenerator(parametrizedInputFunctionSettings2);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"inputFunction3",0));
        assertTrue(JavaPoetTestUtils.isCorrectWrapperMethod(adapter, "inputFunction3", "method3",
                Arrays.asList("\"blabla\""), Void.TYPE));
    }

    @Test
    public void InputFunctionWithJavaFXParameterGenerator(){
        ComponentGenerator generator = new AdapterGenerator(parametrizedInputFunctionSettings3);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"inputFunction4",0));
        assertTrue(JavaPoetTestUtils.isCorrectWrapperMethod(adapter, "inputFunction4", "method4",
                Arrays.asList("settings.JavaFXParameterGenerators.generatePMBPressedEvent()"), Void.TYPE));
    }

    @Test
    public void InputFunctionWithMissingParameter(){
        ComponentGenerator generator = new AdapterGenerator(parametrizedInputFunctionSettings4);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"inputFunction5",0));
        assertTrue(JavaPoetTestUtils.isCorrectWrapperMethod(adapter, "inputFunction5", "method5",
                Arrays.asList(), Void.TYPE));
    }

    @Test
    public void InputFunctionReferenceOnly(){
        ComponentGenerator generator = new AdapterGenerator(referencedInputFunctionSettings);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"inputFunction6",0));
        JavaPoetTestUtils.isEmptyWrapperMethod(adapter, "inputFunction6");
    }

    /// Testing Output Functions ///

    @Test
    public void basicOutputFunctionTest(){
        ComponentGenerator generator = new AdapterGenerator(simpleOutputFunctionSettings);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"outputFunction1",0));
        assertTrue(JavaPoetTestUtils.isCorrectWrapperMethod(adapter, "outputFunction1", "method1",
                new ArrayList<>(), Boolean.TYPE));
    }

    @Test
    public void OutputFunctionWithPrimitiveParameter(){
        ComponentGenerator generator = new AdapterGenerator(parametrizedOutputFunctionSettings1);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"outputFunction2",0));
        assertTrue(JavaPoetTestUtils.isCorrectWrapperMethod(adapter, "outputFunction2", "method2",
                Arrays.asList("true"), Boolean.TYPE));
    }

    @Test
    public void OutputFunctionWithStringParameter(){
        ComponentGenerator generator = new AdapterGenerator(parametrizedOutputFunctionSettings2);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"outputFunction3",0));
        assertTrue(JavaPoetTestUtils.isCorrectWrapperMethod(adapter, "outputFunction3", "method3",
                Arrays.asList("\"blabla\""), Boolean.TYPE));
    }

    @Test
    public void OutputFunctionWithJavaFXParameterGenerator(){
        ComponentGenerator generator = new AdapterGenerator(parametrizedOutputFunctionSettings3);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"outputFunction4",0));
        assertTrue(JavaPoetTestUtils.isCorrectWrapperMethod(adapter, "outputFunction4", "method4",
                Arrays.asList("settings.JavaFXParameterGenerators.generatePMBPressedEvent()"), Boolean.TYPE));
    }

    @Test
    public void OutputFunctionWithMissingParameter(){
        ComponentGenerator generator = new AdapterGenerator(parametrizedOutputFunctionSettings4);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"outputFunction5",0));
        assertTrue(JavaPoetTestUtils.isCorrectWrapperMethod(adapter, "outputFunction5", "method5",
                Arrays.asList(), Boolean.TYPE));
    }

    @Test
    public void OutputFunctionReferenceOnly(){
        ComponentGenerator generator = new AdapterGenerator(referencedOutputFunctionSettings);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"outputFunction6",0));
        JavaPoetTestUtils.isEmptyWrapperMethod(adapter, "outputFunction6");
    }
    
    
    /// Testing Predicates ///

    @Test
    public void basicPredicateTest(){
        ComponentGenerator generator = new AdapterGenerator(simplePredicateSettings);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"predicate1",0));
        assertTrue(JavaPoetTestUtils.isCorrectWrapperMethod(adapter, "predicate1", "method1",
                new ArrayList<>(), Boolean.TYPE));
    }

    @Test
    public void PredicateWithPrimitiveParameter(){
        ComponentGenerator generator = new AdapterGenerator(parametrizedPredicateSettings1);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"predicate2",0));
        assertTrue(JavaPoetTestUtils.isCorrectWrapperMethod(adapter, "predicate2", "method2",
                Arrays.asList("true"), Boolean.TYPE));
    }

    @Test
    public void PredicateWithStringParameter(){
        ComponentGenerator generator = new AdapterGenerator(parametrizedPredicateSettings2);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"predicate3",0));
        assertTrue(JavaPoetTestUtils.isCorrectWrapperMethod(adapter, "predicate3", "method3",
                Arrays.asList("\"blabla\""), Boolean.TYPE));
    }

    @Test
    public void PredicateWithJavaFXParameterGenerator(){
        ComponentGenerator generator = new AdapterGenerator(parametrizedPredicateSettings3);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"predicate4",0));
        assertTrue(JavaPoetTestUtils.isCorrectWrapperMethod(adapter, "predicate4", "method4",
                Arrays.asList("settings.JavaFXParameterGenerators.generatePMBPressedEvent()"), Boolean.TYPE));
    }

    @Test
    public void PredicateWithMissingParameter(){
        ComponentGenerator generator = new AdapterGenerator(parametrizedPredicateSettings4);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"predicate5",0));
        assertTrue(JavaPoetTestUtils.isCorrectWrapperMethod(adapter, "predicate5", "method5",
                Arrays.asList(), Boolean.TYPE));
    }

    @Test
    public void PredicateReferenceOnly(){
        ComponentGenerator generator = new AdapterGenerator(referencedPredicateSettings);
        generator.addTransformation(AdapterTransformations.GenerateWrappedMethods);
        generator.applyTransformations();

        TypeSpec adapter = generator.generateComponent();
        assertTrue(JavaPoetTestUtils.containsMethod(adapter,"predicate6",0));
        JavaPoetTestUtils.isEmptyWrapperMethod(adapter, "predicate6");
    }
    
    /// Testing Parameter Generators ///
    @Test
    public void ParameterGeneratorOfField(){
    }

    @Test
    public void ParameterGeneratorOfMethod(){
    }

    @Test
    public void ParameterGeneratorReferenceOnly(){
    }
}