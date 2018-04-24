package hermielab.generator.settings.transformations;

import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Test;
import hermielab.generator.settings.Settings;
import hermielab.generator.settings.TransformationTestHelperFunctions;
import hermielab.generator.settings.containers.GeneratorInformationElement;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class AbstractElementsTransformationsTests {

    public static GeneratorInformationElement widgetSymbolDummy;
    public static GeneratorInformationElement fieldMethodSymbolDummy;
    public static GeneratorInformationElement functionSymbolDummy;

    @Before
    public void setUp() throws NoSuchMethodException, DocumentException {
        widgetSymbolDummy = buildWidgetSymbolDummy();
        fieldMethodSymbolDummy = buildFieldMethodSymbolDummy();
        functionSymbolDummy = buildFunctionSymbolDummy();
    }

    public static GeneratorInformationElement buildWidgetSymbolDummy() {
        return TransformationTestHelperFunctions.generateElement("widgetsymbol","widgetSymbolID",
                "class","classX",
                "field","buttonX",
                "predicate","predicateID1",
                "event", Arrays.asList("mouse_press","mouse_release","mouse_drag"),
                "output","outputID");
    }

    public static GeneratorInformationElement buildFieldMethodSymbolDummy() {
        return TransformationTestHelperFunctions.generateElement("fieldmethodsymbol","fieldMethodSymbolID",
                "class","classX",
                "field","fieldX",
                "method",Arrays.asList("methodA()","methodB(bool)","methodC(int,int)"),
                "parameter",Arrays.asList("parametergeneratorID1","int:2","parametergeneratorID3"),
                "predicate",Arrays.asList("predicateID1","javaFX:isAccessible","predicateID2"),
                "nfapredicate", "myNFA#generalNFA[0]#myGroup",
                "output","outputID");
    }

    public static GeneratorInformationElement buildFunctionSymbolDummy() {
        return TransformationTestHelperFunctions.generateElement("functionsymbol","functionSymbolID",
                "class","classX",
                "method","methodD(int,int,int)",
                "parameter",Arrays.asList("parametergeneratorID1","int:2","parametergeneratorID2"),
                "predicate",Arrays.asList("predicateID1","javaFX:isAccessible","predicateID2"),
                "nfapredicate", "myNFA#generalNFA[0]#myGroup",
                "output","outputID");
    }

    @Test //TODO: Finish
    public void processWidgetSymbolsTest(){
        Settings settings = TransformationTestHelperFunctions.excecuteTransformation(
                new Settings(widgetSymbolDummy), AbstractElementsTransformations.ProcessWidgetSymbols);

        assertEquals(3, settings.size());
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, widgetSymbolDummy));
        assertEquals(1, widgetSymbolDummy.getChildren().size());
        GeneratorInformationElement element = ConcreteElementsTransformationsTests.buildInputWidgetDummy();
        element.setID("widgetSymbolID");
        element.addParent(widgetSymbolDummy);
        assertTrue(TransformationTestHelperFunctions.containsElement(settings,element));
    };

   @Test
    public void processFieldMethodSymbolsTest(){

       Settings settings = TransformationTestHelperFunctions.excecuteTransformation(
               new Settings(fieldMethodSymbolDummy), AbstractElementsTransformations.ProcessFieldMethodSymbols);

       assertEquals(3, settings.size());
       assertTrue(TransformationTestHelperFunctions.containsElement(settings, fieldMethodSymbolDummy));
       assertEquals(1, fieldMethodSymbolDummy.getChildren().size());
       GeneratorInformationElement element = ConcreteElementsTransformationsTests.buildInputFieldMethodDummy();
       element.setID("fieldMethodSymbolID");
       element.addParent(fieldMethodSymbolDummy);
       assertTrue(TransformationTestHelperFunctions.containsElement(settings,element));
    };

    @Test
    public void processFunctionSymbolsTest(){
        Settings settings = TransformationTestHelperFunctions.excecuteTransformation(
                new Settings(functionSymbolDummy), AbstractElementsTransformations.ProcessFunctionSymbols);

        assertEquals(3, settings.size());
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, functionSymbolDummy));
        assertEquals(1, functionSymbolDummy.getChildren().size());
        GeneratorInformationElement element = ConcreteElementsTransformationsTests.buildInputFunctionDummy();
        element.setID("functionSymbolID");
        element.addParent(functionSymbolDummy);
        assertTrue(TransformationTestHelperFunctions.containsElement(settings,element));
    };

    @Test
    public void completeTransformationChainTest(){
        Settings settings = TransformationTestHelperFunctions.excecuteTransformation(
                new Settings(widgetSymbolDummy, fieldMethodSymbolDummy, functionSymbolDummy),
                AbstractElementsTransformations.ProcessWidgetSymbols,
                AbstractElementsTransformations.ProcessFieldMethodSymbols,
                AbstractElementsTransformations.ProcessFunctionSymbols,
                ConcreteElementsTransformations.ProcessInputWidgets,
                ConcreteElementsTransformations.ProcessInputFieldMethods,
                ConcreteElementsTransformations.ProcessInputFunctions,
                ConcreteElementsTransformations.ProcessOutputFieldMethods,
                ConcreteElementsTransformations.ProcessOutputFunctions,
                UtilElementsTransformations.ProcessParameters,
                UtilElementsTransformations.ProcessParameterGenerators,
                UtilElementsTransformations.ProcessPredicates);

        assertEquals(25, settings.size());
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, widgetSymbolDummy));
        assertEquals(1, widgetSymbolDummy.getChildren().size());
        assertEquals(10,HelperFunctions.getDescendants(settings,widgetSymbolDummy).size());
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, fieldMethodSymbolDummy));
        assertEquals(1, fieldMethodSymbolDummy.getChildren().size());
        assertEquals(5,HelperFunctions.getDescendants(settings,fieldMethodSymbolDummy).size());
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, functionSymbolDummy));
        assertEquals(1, functionSymbolDummy.getChildren().size());
        assertEquals(2,HelperFunctions.getDescendants(settings,functionSymbolDummy).size());
    };

    @Test
    public void generateInputWidgetsFromWidgetSymbolTest(){
        //TODO
    }

    @Test
    public void generateInputFieldMethodsFromFieldMethodSymbolTest(){
        //TODO
    }

    @Test
    public void generateInputFunctionsFromFunctionSymbol(){
        //TODO
    }

    @Test
    public void generateAbstractSymbolsFromAnySymbol(){
        Settings settings = TransformationTestHelperFunctions.excecuteTransformation(
                new Settings(widgetSymbolDummy, fieldMethodSymbolDummy, functionSymbolDummy),
                AbstractElementsTransformations.ProcessWidgetSymbols,
                AbstractElementsTransformations.ProcessFieldMethodSymbols,
                AbstractElementsTransformations.ProcessFunctionSymbols,
                ConcreteElementsTransformations.ProcessInputWidgets,
                ConcreteElementsTransformations.ProcessInputFieldMethods,
                ConcreteElementsTransformations.ProcessInputFunctions,
                ConcreteElementsTransformations.ProcessOutputFieldMethods,
                ConcreteElementsTransformations.ProcessOutputFunctions,
                UtilElementsTransformations.ProcessParameters,
                UtilElementsTransformations.ProcessParameterGenerators,
                UtilElementsTransformations.ProcessPredicates,
                AbstractElementsTransformations.ProcessSymbolsToGenerateMappings);

        assertEquals(32, settings.size());
        assertEquals(7, settings.getSettingsByType("abstractsymbolmapping").size());
        System.out.println(settings.getSettingsByType("abstractsymbolmapping"));
    }
    

}