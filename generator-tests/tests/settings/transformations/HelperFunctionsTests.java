package settings.transformations;

import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import settings.Settings;
import settings.SettingsPreProcessor;
import settings.TransformationTestHelperFunctions;
import settings.containers.GeneratorInformationElement;
import settings.transformations.exceptions.IncompleteElementException;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class HelperFunctionsTests {

    public static GeneratorInformationElement completeDummyElement;  //Dummy element with Output Function, Parameter Generators & Predicates
    public static GeneratorInformationElement templatedPredicateAndParameterGeneratorDummyElement;
    public static GeneratorInformationElement primitiveParametersDummyElement;

    @Before
    public void setUp() throws NoSuchMethodException, DocumentException {
        completeDummyElement = buildParameterElementDummy();
        templatedPredicateAndParameterGeneratorDummyElement = buildJavaFXPredicateAndParameterGeneratorDummyElement();
        primitiveParametersDummyElement = buildPrimitiveParametersDummyElement();
    }

    public static GeneratorInformationElement buildParameterElementDummy(){
        String type = "sometype";
        String ID = "ElementID";

        return TransformationTestHelperFunctions.generateElement(type,ID,
                "output","outputID",
                "parameter", Arrays.asList("paramID1","paramID2","javaFX:mouse_click"),
                "predicate",Arrays.asList("predicateID1","predicateID2","javaFX:isAccessible","javaFX:isAccessible(someParam)"));
    }

    public static GeneratorInformationElement buildJavaFXPredicateAndParameterGeneratorDummyElement(){
        String type = "sometype";
        String ID = "ElementID";

        return TransformationTestHelperFunctions.generateElement(type,ID,
                "parameter", Arrays.asList("extension:mouse_click","extension:mouse_move)"),
                "predicate",Arrays.asList("extension:p1","extension:p2"));
    }

    public static GeneratorInformationElement buildPrimitiveParametersDummyElement(){
        String type = "sometype";
        String ID = "ElementID";

        return TransformationTestHelperFunctions.generateElement(type,ID,
                "parameter", Arrays.asList("string:some_string", "byte:+120", "char:a",
        "short:10","int:42","integer:43","long:232432","float:1.00","double:1.01","boolean:true","bool:false"));
    }

    @Test
    public void processOutputFunctionsReferencesTest(){
        Settings result = HelperFunctions.processOutputFunctionReferences(new Settings(),completeDummyElement).getNewSettings();
        assertTrue(TransformationTestHelperFunctions.containsReference(result,"output","outputID"));
        assertEquals(1,result.size());
    }

    @Test
    public void processParameterGeneratorsReferencesTest(){
        Settings result = HelperFunctions.processParameterGeneratorReferences(new Settings(),completeDummyElement).getNewSettings();
        assertTrue(TransformationTestHelperFunctions.containsReference(result,"parametergenerator","paramID1"));
        assertTrue(TransformationTestHelperFunctions.containsReference(result,"parametergenerator","paramID2"));
        assertEquals(2,result.size());
    }

    @Test
    public void processPredicatesReferencesTest(){
        Settings result = HelperFunctions.processPredicateReferences(new Settings(),completeDummyElement).getNewSettings();
        assertEquals(4, result.size());
        assertEquals(0, completeDummyElement.getChildren().size());
        assertTrue(TransformationTestHelperFunctions.containsReference(result,"predicate","predicateID1"));
        assertTrue(TransformationTestHelperFunctions.containsReference(result,"predicate","predicateID2"));
        assertTrue(TransformationTestHelperFunctions.containsElement(result,"predicate","javaFX:isAccessible"));
        assertTrue(TransformationTestHelperFunctions.containsElement(result,"predicate","javaFX:isAccessible(someParam)"));
    }

    @Test
    public void addSelfTest(){
        Settings result = HelperFunctions.addSelf(new Settings(),completeDummyElement).getNewSettings();
        assertEquals(1, result.size());
        assertTrue(TransformationTestHelperFunctions.containsElements(result, completeDummyElement));
    }

    @Test(expected = IncompleteElementException.class)
    public void addSelfWhenNullTest(){
        HelperFunctions.addSelf(new Settings(),null);
    }

    @Test
    public void addElementTest(){
        Settings result = HelperFunctions.addElement(new Settings(),completeDummyElement).getNewSettings();
        assertEquals(1, result.size());
        assertTrue(TransformationTestHelperFunctions.containsElements(result, completeDummyElement));
    }

    @Test
    public void addReferenceTest(){
        Settings result = HelperFunctions.addReference(new Settings(),"sometype","someid").getNewSettings();
        assertEquals(1, result.size());
        assertTrue(TransformationTestHelperFunctions.containsReference(result, "sometype","someid"));
    }

    @Test
    public void elementContainsVariablesVariablePresentTest(){
        //TODO
    }

    @Test
    public void elementContainsVariablesVariablePresentAndNullTest(){
        //TODO
    }

    @Test
    public void elementContainsVariablesVariableNonPresentTest(){
        //TODO
    }

    @Test
    public void elementContainsNonNullVariablesVariablePresentTest(){
        //TODO
    }

    @Test
    public void elementContainsNonNullVariablesVariablePresentAndNullTest(){
        //TODO
    }

    @Test
    public void elementContainsNonNullVariablesVariableNonPresentTest(){
        //TODO
    }
}