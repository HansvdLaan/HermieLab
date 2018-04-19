package settings.transformations;

import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Test;
import settings.Settings;
import settings.TransformationTestHelperFunctions;
import settings.containers.GeneratorInformationElement;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class ConcreteElementsTransformationsTests {

    public static GeneratorInformationElement inputWidgetDummy;
    public static GeneratorInformationElement inputFieldMethodDummy;
    public static GeneratorInformationElement inputFunctionDummy;
    public static GeneratorInformationElement outputFieldMethodDummy;
    public static GeneratorInformationElement outputFunctionDummy;

    @Before
    public void setUp() throws NoSuchMethodException, DocumentException {
        inputWidgetDummy = buildInputWidgetDummy();
        inputFieldMethodDummy = buildInputFieldMethodDummy();
        inputFunctionDummy = buildInputFunctionDummy();
        outputFieldMethodDummy = buildOutputFieldMethodDummy();
        outputFunctionDummy = buildOutputFunctionDummy();
    }

    public static GeneratorInformationElement buildInputWidgetDummy() {
        return TransformationTestHelperFunctions.generateElement("inputwidget","widgetID",
                "class","classX",
                "field","buttonX",
                "predicate","predicateID1",
                "event", Arrays.asList("mouse_press","mouse_release","mouse_drag"));
    }

    public static GeneratorInformationElement buildInputFieldMethodDummy() {
        return TransformationTestHelperFunctions.generateElement("inputfieldmethod","inputFieldMethodID",
                "class","classX",
                "field","fieldX",
                "method",Arrays.asList("methodA()","methodB(bool)","methodC(int,int)"),
                "parameter",Arrays.asList("parametergeneratorID1","int:2","parametergeneratorID3"),
                "predicate",Arrays.asList("predicateID1","javaFX:isAccessible","predicateID2"),
                "nfapredicate", "myNFA#generalNFA[0]#myGroup");
    }

    public static GeneratorInformationElement buildInputFunctionDummy() {
        return TransformationTestHelperFunctions.generateElement("inputfunction","inputFunctionID",
                "class","classX",
                "method","methodD(int,int,int)",
                "parameter",Arrays.asList("parametergeneratorID1","int:2","parametergeneratorID2"),
                "predicate",Arrays.asList("predicateID1","javaFX:isAccessible","predicateID2"),
                "nfapredicate", "myNFA#generalNFA[0]#myGroup");
    }

    public static GeneratorInformationElement buildOutputFieldMethodDummy() {
        return TransformationTestHelperFunctions.generateElement("outputfieldmethod","outputFieldMethodID",
                "class","classX",
                "field","fieldX",
                "method",Arrays.asList("methodA()","methodB(bool)","methodC(int,int)"),
                "parameter",Arrays.asList("parametergeneratorID1","int:2","parametergeneratorID3"));
    }

    public static GeneratorInformationElement buildOutputFunctionDummy() {
        return TransformationTestHelperFunctions.generateElement("outputfunction","outputFunctionID",
                "class","classX",
                "method","methodD(int,int,int)",
                "parameter",Arrays.asList("parametergeneratorID1","int:2","parametergeneratorID2"));
    }

    @Test //TODO: Finish
    public void processInputWidgetsTest(){
        Settings settings = TransformationTestHelperFunctions.excecuteTransformation(
                new Settings(inputWidgetDummy), ConcreteElementsTransformations.ProcessInputWidgets);

        assertEquals(7, settings.size());
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, inputWidgetDummy.getType(), inputWidgetDummy.getID()));
        assertEquals(5, inputWidgetDummy.getChildren().size());
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, "parameter", inputWidgetDummy.getID() + "_Parameter"));
        assertTrue(TransformationTestHelperFunctions.containsElement(settings,"predicate", inputWidgetDummy.getID() + "_isAccessible"));
        assertTrue(TransformationTestHelperFunctions.containsReference(settings,"predicate", "predicateID1"));
        assertTrue(TransformationTestHelperFunctions.containsElement(settings,"inputfieldmethod", inputWidgetDummy.getID() + "_mouse_press"));
        assertTrue(TransformationTestHelperFunctions.containsElement(settings,"inputfieldmethod", inputWidgetDummy.getID() + "_mouse_release"));
        assertTrue(TransformationTestHelperFunctions.containsElement(settings,"inputfieldmethod", inputWidgetDummy.getID() + "_mouse_drag"));
    };

   @Test
    public void processInputFieldMethodsTest(){
//
//       GeneratorInformationElement elem = new GeneratorInformationElement("type","ID");
//       elem.addAttribute("t","b1");
//       elem.addAttribute("t","b2");
//       elem.addAttribute("t","a3");

       Settings settings = TransformationTestHelperFunctions.excecuteTransformation(
               new Settings(inputFieldMethodDummy), ConcreteElementsTransformations.ProcessInputFieldMethods);

       assertEquals(9, settings.size());
       assertTrue(TransformationTestHelperFunctions.containsElement(settings, inputFieldMethodDummy.getType(), inputFieldMethodDummy.getID()));
       assertEquals(4, inputFieldMethodDummy.getChildren().size());
       assertTrue(TransformationTestHelperFunctions.containsElement(settings, new GeneratorInformationElement("inputfunction","inputFieldMethodID_Method1",
               inputFieldMethodDummy,
               "class","classX",
               "field","fieldX",
               "method","methodA()",
               "predicate",Arrays.asList("predicateID1","javaFX:isAccessible","predicateID2"),
               "nfapredicate", "myNFA#generalNFA[0]#myGroup")));
       assertTrue(TransformationTestHelperFunctions.containsElement(settings, new GeneratorInformationElement("inputfunction","inputFieldMethodID_Method2",
               inputFieldMethodDummy,
               "class","classX",
               "field","fieldX",
               "method","methodB(bool)",
               "parameter","parametergeneratorID1",
               "predicate",Arrays.asList("predicateID1","javaFX:isAccessible","predicateID2"),
               "nfapredicate", "myNFA#generalNFA[0]#myGroup")));
       assertTrue(TransformationTestHelperFunctions.containsElement(settings, new GeneratorInformationElement("inputfunction","inputFieldMethodID_Method3",
               inputFieldMethodDummy,
               "class","classX",
               "field","fieldX",
               "method","methodC(int,int)",
               "parameter",Arrays.asList("int:2","parametergeneratorID3"),
               "predicate",Arrays.asList("predicateID1","javaFX:isAccessible","predicateID2"),
               "nfapredicate", "myNFA#generalNFA[0]#myGroup")));
       assertTrue(TransformationTestHelperFunctions.containsReference(settings, "parametergenerator", "parametergeneratorID1"));
       assertTrue(TransformationTestHelperFunctions.containsReference(settings, "parametergenerator", "parametergeneratorID2"));
       assertTrue(TransformationTestHelperFunctions.containsReference(settings, "predicate", "predicateID1"));
       assertTrue(TransformationTestHelperFunctions.containsReference(settings, "predicate", "predicateID2"));
       assertTrue(TransformationTestHelperFunctions.containsElement(settings, "predicate", "javaFX:isAccessible"));
    };

    @Test
    public void processInputFunctionsTest(){
        Settings settings = TransformationTestHelperFunctions.excecuteTransformation(
                new Settings(inputFunctionDummy), ConcreteElementsTransformations.ProcessInputFunctions);

        assertEquals(6, settings.size());
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, inputFunctionDummy.getType(), inputFunctionDummy.getID()));
        assertEquals(1, inputFunctionDummy.getChildren().size());
        assertTrue(TransformationTestHelperFunctions.containsReference(settings, "parametergenerator", "parametergeneratorID1"));
        assertTrue(TransformationTestHelperFunctions.containsReference(settings, "parametergenerator", "parametergeneratorID2"));
        assertTrue(TransformationTestHelperFunctions.containsReference(settings, "predicate", "predicateID1"));
        assertTrue(TransformationTestHelperFunctions.containsReference(settings, "predicate", "predicateID2"));
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, "predicate", "javaFX:isAccessible"));
    };

    @Test
    public void processOutputFieldMethodsTest(){
        Settings settings = TransformationTestHelperFunctions.excecuteTransformation(
                new Settings(outputFieldMethodDummy), ConcreteElementsTransformations.ProcessOutputFieldMethods);

        assertEquals(6, settings.size());
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, outputFieldMethodDummy.getType(), outputFieldMethodDummy.getID()));
        assertEquals(3, outputFieldMethodDummy.getChildren().size());
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, new GeneratorInformationElement("outputfunction","outputFieldMethodID_Method1",
                outputFieldMethodDummy,
                "class","classX",
                "field","fieldX",
                "method","methodA()")));
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, new GeneratorInformationElement("outputfunction","outputFieldMethodID_Method2",
                outputFieldMethodDummy,
                "class","classX",
                "field","fieldX",
                "method","methodB(bool)",
                "parameter","parametergeneratorID1")));
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, new GeneratorInformationElement("outputfunction","outputFieldMethodID_Method3",
                outputFieldMethodDummy,
                "class","classX",
                "field","fieldX",
                "method","methodC(int,int)",
                "parameter",Arrays.asList("int:2","parametergeneratorID3"))));
        assertTrue(TransformationTestHelperFunctions.containsReference(settings, "parametergenerator", "parametergeneratorID1"));
        assertTrue(TransformationTestHelperFunctions.containsReference(settings, "parametergenerator", "parametergeneratorID2"));
    };

    @Test
    public void processOutputFunctionsTest(){
        Settings settings = TransformationTestHelperFunctions.excecuteTransformation(
                new Settings(outputFunctionDummy), ConcreteElementsTransformations.ProcessOutputFunctions);

        assertEquals(3, settings.size());
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, outputFunctionDummy.getType(), outputFunctionDummy.getID()));
        assertEquals(0, outputFunctionDummy.getChildren().size());
        assertTrue(TransformationTestHelperFunctions.containsReference(settings, "parametergenerator", "parametergeneratorID1"));
        assertTrue(TransformationTestHelperFunctions.containsReference(settings, "parametergenerator", "parametergeneratorID2"));
    };

    @Test //TODO: What is the JavaFX event???
    public void generateInputFieldFromInputWidgetTest(){
        GeneratorInformationElement parameterElement = new Settings(ConcreteElementsTransformations.generateParametersFromInputWidgets(
                new Settings(), inputWidgetDummy).getGeneratedElements()).getSettingsByTypeAndID("parameter",inputWidgetDummy.getID() + "_Parameter");
        GeneratorInformationElement predicateElement = new Settings(ConcreteElementsTransformations.generatePredicatesFromInputWidget(
                new Settings(), inputWidgetDummy, parameterElement).getGeneratedElements()).getSettingsByTypeAndID("predicate",inputWidgetDummy.getID() + "_isAccessible");
        Settings settings = new Settings(ConcreteElementsTransformations.generateInputFieldsFromInputWidget(
                new Settings(), inputWidgetDummy, Arrays.asList(predicateElement)).getGeneratedElements());

        assertEquals(3, settings.size());
        assertEquals(0, inputWidgetDummy.getChildren().size());
        assertTrue(TransformationTestHelperFunctions.containsElements(settings, new GeneratorInformationElement("inputfieldmethod",inputWidgetDummy.getID() + "_mouse_press",
                inputWidgetDummy,
                "class","classX",
                "field","buttonX",
                "method","fireEvent(javafx.event.Event)",
                "parameter","javaFX:mouse_press",
                "predicate",Arrays.asList("predicateID1","inputWidgetID_isAccessible"),
                "nfapredicate","inputWidgetIDNFA#widgetNFA[PRESS]#GUI"
                )));
        assertTrue(TransformationTestHelperFunctions.containsElements(settings, new GeneratorInformationElement("inputfieldmethod",inputWidgetDummy.getID() + "_mouse_release",
                inputWidgetDummy,
                "class","classX",
                "field","buttonX",
                "method","fireEvent(javafx.event.Event)",
                "parameter","javaFX:mouse_release",
                "predicate",Arrays.asList("predicateID1","inputWidgetID_isAccessible"),
                "nfapredicate","inputWidgetIDNFA#widgetNFA[RELEASE]#GUI"
                )));
        assertTrue(TransformationTestHelperFunctions.containsElements(settings, new GeneratorInformationElement("inputfieldmethod",inputWidgetDummy.getID() + "_mouse_drag",
                inputWidgetDummy,
                "class","classX",
                "field","buttonX",
                "method","fireEvent(javafx.event.Event)",
                "parameter","javaFX:mouse_drag",
                "predicate",Arrays.asList("predicateID1","inputWidgetID_isAccessible"),
                "nfapredicate","inputWidgetIDNFA#widgetNFA[DRAG]#GUI"
        )));
    }

    @Test
    public void generateParametersFromInputWidget(){
        Settings settings = new Settings(ConcreteElementsTransformations.generateParametersFromInputWidgets(
                new Settings(), inputWidgetDummy).getGeneratedElements());
        assertEquals(1, settings.size());
        assertEquals(0, inputWidgetDummy.getChildren().size());
        assertTrue(TransformationTestHelperFunctions.containsElements(settings,
                new GeneratorInformationElement("parameter",inputWidgetDummy.getID() + "_Parameter", inputWidgetDummy,
                "class","classX",
                "field","buttonX")));
    }

    @Test
    public void generatePredicatesFromInputWidget(){
        GeneratorInformationElement parameterElement = new Settings(ConcreteElementsTransformations.generateParametersFromInputWidgets(
                new Settings(), inputWidgetDummy).getGeneratedElements()).getSettingsByTypeAndID("parameter",inputWidgetDummy.getID() + "_Parameter");
        Settings settings = new Settings(ConcreteElementsTransformations.generatePredicatesFromInputWidget(
                new Settings(), inputWidgetDummy, parameterElement).getGeneratedElements());
        assertEquals(1, settings.size());
        assertEquals(0, inputWidgetDummy.getChildren().size());
        assertTrue(TransformationTestHelperFunctions.containsElements(settings, new GeneratorInformationElement("predicate",inputWidgetDummy.getID() + "_isAccessible",
                inputWidgetDummy,
                "class","settings.JavaFXPredicates",
                "method","isAccessible(javafx.scene.Node)",
                "parameter",inputWidgetDummy.getID() + "_Parameter")));
    }

}