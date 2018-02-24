package settings;

import com.google.testing.compile.JavaFileObjects;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.BeforeClass;
import org.junit.Test;
import settings.containers.GeneratorInformationElement;
import settings.transformations.GeneralTransformations;
import utils.ClassUtils;
import utils.ClassUtilsTest;
import utils.DummyPreProcessor;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.google.testing.compile.Compiler.javac;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public final class SettingsPreProcessorTest {

    public static Settings startSettings;
    public static Settings reflectionSettings;
    public static Settings functionSymbolsSettings;
    public static Settings widgetSymbolsSettings;
    public static Settings fieldMethodsSettings;

    public static Settings concreteInputFieldMethodsSettings;
    public static Settings concreteInputWidgetSettings;

    public static Settings parameterSettings;
    public static Settings predicateSettings;

    @BeforeClass
    public static void setUpProcessor() throws DocumentException, MalformedURLException {
        Document settings = SettingsParser.toDocument(new File(ClassUtilsTest.class.getResource("/settings/settings0.xml").getPath()));
        DummyPreProcessor processor = (DummyPreProcessor) javac().withProcessors(new DummyPreProcessor()).compile(
                JavaFileObjects.forResource(Paths.get("tests","testcode","GameController.java").toFile().toURL()),
                JavaFileObjects.forResource(Paths.get("tests","testcode","rocketgame","RocketGame.java").toFile().toURL())
        ).compiler().processors().get(0);
        ClassUtils.getInstance().setPrEnv(processor.getPreEnv());
    }

    @BeforeClass
    public static void setUpStartSettings() throws NoSuchMethodException, DocumentException {
        startSettings = new Settings();
        Map<String,Object> startParameters = new HashMap<>();
        startParameters.put("automaton","dfa");
        startParameters.put("method","start(javafx.stage.Stage)");
        startParameters.put("class","RocketGame");
        startParameters.put("order",0);
        startSettings.addSettingsByTypeAndID("start","startTestID",
                new GeneratorInformationElement("startTestID","start",startParameters));
    }

    @BeforeClass
    public static void setUpReflectionSettings() {
        reflectionSettings = new Settings();
        Map<String, Object> reflectionParameters1 = new HashMap<>();
        reflectionParameters1.put("class","testcode.rocketgame.RocketGame");
        reflectionParameters1.put("method","start(javafx.stage.Stage)");
        Map<String, Object> reflectionParameters2 = new HashMap<>();
        reflectionParameters2.put("class","testcode.GameController");
        reflectionParameters2.put("field","thread");
        reflectionParameters2.put("method","getState()");
        reflectionSettings.addSettingsByTypeAndID("noType","id1", new GeneratorInformationElement("id1","notype",reflectionParameters1));
        reflectionSettings.addSettingsByTypeAndID("noType","id2", new GeneratorInformationElement("id2","notype",reflectionParameters2));
    }

    @BeforeClass
    public static void setUpFunctionSymbolSettings(){
        functionSymbolsSettings = new Settings();

        String type = "functionsymbol";
        String ID1 = "permissionFalse";
        String ID2 = "permissionTrue";

        Map<String, Object> abstractSymbolParameters1 = new HashMap<>();
        abstractSymbolParameters1.put("output","output");
        abstractSymbolParameters1.put("nfapredicate","testNFA#NFARepeatSequence2[2]#G1");
        abstractSymbolParameters1.put("input","rectractPermission");
        abstractSymbolParameters1.put("predicate","p1");
        abstractSymbolParameters1.put("method","setPermission(boolean)");
        abstractSymbolParameters1.put("id",ID1);
        abstractSymbolParameters1.put("type",type);
        abstractSymbolParameters1.put("class","GamceController");
        abstractSymbolParameters1.put("param#0","Bool:False");
        functionSymbolsSettings.addSettingsByTypeAndID(type,ID1,new GeneratorInformationElement(type,ID1,abstractSymbolParameters1));

        Map<String, Object> abstractSymbolParameters2 = new HashMap<>();
        abstractSymbolParameters2.put("output","output");
        abstractSymbolParameters2.put("nfapredicate","testNFA#NFARepeatSequence2[2]#G1");
        abstractSymbolParameters2.put("input","rectractPermission");
        abstractSymbolParameters2.put("predicate","p1");
        abstractSymbolParameters2.put("method","setPermission(boolean)");
        abstractSymbolParameters2.put("id",ID2);
        abstractSymbolParameters2.put("type",type);
        abstractSymbolParameters2.put("class","GamceController");
        abstractSymbolParameters2.put("param#0","Bool:False");
        functionSymbolsSettings.addSettingsByTypeAndID(type,ID2,new GeneratorInformationElement(type,ID2,abstractSymbolParameters2));

}

    @BeforeClass
    public static void setUpWidgetSymbolsSettings(){
        widgetSymbolsSettings = new Settings();

        String type = "widgetsymbol";
        String ID = "buttonRocket1";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("field","buttonRocket1");
        parameters.put("event#0","press");
        parameters.put("event#1","release");
        parameters.put("event#2","drag");
        parameters.put("class","testcode.GameController");
        parameters.put("output#0","output0");
        parameters.put("output#1","output1");
        widgetSymbolsSettings.addSettingsByTypeAndID(type,ID,new GeneratorInformationElement(type,ID,parameters));
    }

    @BeforeClass
    public static void setUpFieldMethodSymbolSettings(){
        fieldMethodsSettings = new Settings();

        String type = "fieldmethodsymbol";
        String ID = "button1RocketField";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("field","buttonRocket1");
        parameters.put("method#1","setVisible(bool)");
        parameters.put("method#2","setVisible(bool)");
        parameters.put("class","testcode.GameController");
        parameters.put("param#1","Bool:True");
        parameters.put("param#2","Bool:False");
        parameters.put("output#0","output");
        fieldMethodsSettings.addSettingsByTypeAndID(type,ID,new GeneratorInformationElement(type,ID,parameters));
    }


    @BeforeClass
    public static void setUpConcreteInputFieldMethodSettings(){
        concreteInputFieldMethodsSettings = new Settings();

        String type = "inputfieldmethod";
        String ID = "button1RocketField";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("field","buttonRocket1");
        parameters.put("method#1","setVisible(bool)");
        parameters.put("method#2","setVisible(bool)");
        parameters.put("class","testcode.GameController");
        parameters.put("param#1","Bool:True");
        parameters.put("param#2","Bool:False");
        concreteInputFieldMethodsSettings.addSettingsByTypeAndID(type,ID,new GeneratorInformationElement(type,ID,parameters));
    }

    @BeforeClass
    public static void setUpConcreteInputWidgetSettings(){
        concreteInputWidgetSettings = new Settings();

        String type = "inputwidget";
        String ID = "buttonRocket1";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("field","buttonRocket1");
        parameters.put("event#0","press");
        parameters.put("event#1","release");
        parameters.put("event#2","drag");
        parameters.put("class","testcode.GameController");
        concreteInputWidgetSettings.addSettingsByTypeAndID(type,ID,new GeneratorInformationElement(type,ID,parameters));
    }

    @BeforeClass
    public static void setUpParameterSettings(){
        parameterSettings = new Settings();

        String type = "parameter";
        String ID = "ThreadParam";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("field","thread");
        parameters.put("class","testcode.GameController");

        parameterSettings.addSettingsByTypeAndID(type,ID, new GeneratorInformationElement(type,ID, parameters));
    }

    @BeforeClass
    public static void setUpPredicateSettings(){
        predicateSettings = new Settings();

        String type = "inputwidget";
        String ID = "buttonRocket1";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("field","buttonRocket1");
        parameters.put("event#0","press");
        parameters.put("event#1","release");
        parameters.put("event#2","drag");
        parameters.put("class","testcode.GameController");
        parameters.put("predicate#0","utils:isNull(field)");
        parameters.put("nfapredicate#0","testNFA#NFARepeatSequence2[2]#G1");

        predicateSettings.addSettingsByTypeAndID(type,ID,new GeneratorInformationElement(type,ID,parameters));
    }

    @Test
    public void copySettingsTest(){
        SettingsPreProcessor processor = new SettingsPreProcessor(startSettings);
        processor.addTransformation(GeneralTransformations.copyInitToSettings);
        processor.applyTransformations();
        assertEquals(processor.getInit(),processor.getSettings());
    }
    @Test
    //These test should just not crash
    public void stringToReflectionTest() {
        SettingsPreProcessor processor = new SettingsPreProcessor(reflectionSettings);
        processor.addTransformation(GeneralTransformations.copyInitToSettings);
        processor.addTransformation(GeneralTransformations.stringToReflectionTransformation);
        processor.applyTransformations();

    }

    @Test
    public void functionSymbolTransformationTest(){
        SettingsPreProcessor processor = new SettingsPreProcessor(functionSymbolsSettings);
        processor.addTransformation(GeneralTransformations.copyInitToSettings);
        processor.addTransformation(GeneralTransformations.FunctionSymbolTransformation);
        processor.applyTransformations();
    }

    @Test
    public void widgetSymbolTransformationTest(){
        SettingsPreProcessor processor = new SettingsPreProcessor(widgetSymbolsSettings);
        processor.addTransformation(GeneralTransformations.copyInitToSettings);
        processor.addTransformation(GeneralTransformations.WidgetSymbolTransformation);
        processor.applyTransformations();
    }

    @Test
    public void fieldMethodSymbolTransformationTest(){
        SettingsPreProcessor processor = new SettingsPreProcessor(fieldMethodsSettings);
        processor.addTransformation(GeneralTransformations.copyInitToSettings);
        processor.addTransformation(GeneralTransformations.FieldMethodSymbolTransformation);
        processor.applyTransformations();
    }

    @Test
    public void concreteInputFieldTransformationTest(){
        SettingsPreProcessor processor = new SettingsPreProcessor(concreteInputFieldMethodsSettings);
        processor.addTransformation(GeneralTransformations.copyInitToSettings);
        processor.addTransformation(GeneralTransformations.InputFieldMethodTransformation);
        processor.applyTransformations();
        processor.getSettings();
    }

    @Test
    public void concreteInputWidgetTransformationTest(){
        SettingsPreProcessor processor = new SettingsPreProcessor(concreteInputWidgetSettings);
        processor.addTransformation(GeneralTransformations.copyInitToSettings);
        processor.addTransformation(GeneralTransformations.InputWidgetTransformation);
        processor.applyTransformations();
        processor.getSettings();
    }

    @Test
    public void widgetSymbolTransformationChainTest(){
        SettingsPreProcessor processor = new SettingsPreProcessor(widgetSymbolsSettings);
        processor.addTransformation(GeneralTransformations.copyInitToSettings);
        processor.addTransformation(GeneralTransformations.WidgetSymbolTransformation);
        processor.addTransformation(GeneralTransformations.InputWidgetTransformation);
        processor.addTransformation(GeneralTransformations.FieldMethodSymbolTransformation);
        processor.addTransformation(GeneralTransformations.InputFieldMethodTransformation);
        processor.applyTransformations();
    }

    @Test
    public void fieldMethodSymbolTransformationChainTest(){
        SettingsPreProcessor processor = new SettingsPreProcessor(fieldMethodsSettings);
        processor.addTransformation(GeneralTransformations.copyInitToSettings);
        processor.addTransformation(GeneralTransformations.FieldMethodSymbolTransformation);
        processor.addTransformation(GeneralTransformations.InputFieldMethodTransformation);
        processor.applyTransformations();
    }

    @Test
    public void parameterTransformationTest(){
        SettingsPreProcessor processor = new SettingsPreProcessor(parameterSettings);
        processor.addTransformation(GeneralTransformations.copyInitToSettings);
        processor.addTransformation(GeneralTransformations.ParameterToParameterGeneratorTransformation);
        processor.applyTransformations();
    }

    @Test
    public void predicateHandlingTest(){
        SettingsPreProcessor processor = new SettingsPreProcessor(predicateSettings);
        processor.addTransformation(GeneralTransformations.copyInitToSettings);
        processor.addTransformation(GeneralTransformations.InputWidgetTransformation);
        processor.addTransformation(GeneralTransformations.InputFieldMethodTransformation);
        processor.applyTransformations();
        processor.getSettings();
    }



}