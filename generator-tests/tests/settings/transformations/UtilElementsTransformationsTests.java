package settings.transformations;

import org.dom4j.DocumentException;
import org.junit.BeforeClass;
import org.junit.Test;
import settings.Settings;
import settings.TransformationTestHelperFunctions;
import settings.containers.GeneratorInformationElement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class UtilElementsTransformationsTests {

    public static GeneratorInformationElement parameterElementDummy;
    public static GeneratorInformationElement parameterGeneratorElementDummy;
    public static GeneratorInformationElement predicateElementDummy;
    public static GeneratorInformationElement transformElementDummy;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException, DocumentException {
        parameterElementDummy = buildParameterElementDummy();
        parameterGeneratorElementDummy = buildParameterGeneratorElementDummy();
        predicateElementDummy = buildPredicateElementDummy();
        transformElementDummy= buildTransformElementDummy();
    }

    public static GeneratorInformationElement buildParameterElementDummy(){
        String type = "parameter";
        String ID = "TestParameterID";

        return TransformationTestHelperFunctions.generateElement(type,ID,
                "class","classX",
                "field", "fieldX",
                "someParam","valueX");
    }

    public static GeneratorInformationElement buildParameterGeneratorElementDummy(){
        String type = "parametergenerator";
        String ID = "TestParameterGeneratorID";

        return TransformationTestHelperFunctions.generateElement(type,ID,
                "class","classX",
                "field", "fieldX",
                "someParam","valueX");
    }

    public static GeneratorInformationElement buildPredicateElementDummy(){
        String type = "predicate";
        String ID = "TestPredicateID";

        return TransformationTestHelperFunctions.generateElement(type,ID,
                "class", "classX",
                "method","methodX",
                "someParam", "valueX");
    }

    public static GeneratorInformationElement buildTransformElementDummy(){
        //TODO
        return null;
    }

    @Test
    public void processParametersCorrectTest(){
        Settings settings = TransformationTestHelperFunctions.excecuteTransformation(
                new Settings(parameterElementDummy), UtilElementsTransformations.ProcessParameters);

        assertEquals(2, settings.size());
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, parameterElementDummy.getType(), parameterElementDummy.getID()));
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, "parametergenerator", parameterElementDummy.getID()));
    }

//    @Test(expected = IncompleteElementException.class)
//    public void processParametersIncompleteParameterTest(){
//        GeneratorInformationElement element = buildParameterElementDummy();
//        TransformationTestHelperFunctions.excecuteTransformation(
//                new Settings(parameterElementDummy), UtilElementsTransformations.ProcessParameters);
//    }

    @Test
    public void processParameterGeneratorTest(){
        Settings settings = TransformationTestHelperFunctions.excecuteTransformation(
                new Settings(parameterGeneratorElementDummy), UtilElementsTransformations.ProcessParameterGenerators);

        assertEquals(1, settings.size());
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, parameterGeneratorElementDummy.getType(), parameterGeneratorElementDummy.getID()));

    }

//    @Test(expected = IncompleteElementException.class)
//    public void processParameterGeneratorIncompleteParameterGeneratorTest(){
//        ///
//    }

    @Test
    public void processPredicateTest(){
        Settings settings = TransformationTestHelperFunctions.excecuteTransformation(
                new Settings(predicateElementDummy), UtilElementsTransformations.ProcessPredicates);

        assertEquals(1, settings.size());
        assertTrue(TransformationTestHelperFunctions.containsElement(settings, predicateElementDummy.getType(), predicateElementDummy.getID()));
    }

//    @Test(expected = IncompleteElementException.class)
//    public void processPredicateIncompletePredicateTest(){
//
//    }
//
//    @Test
//    public void processTransformTest(){
//        //TODO
//    }
//
//    @Test(expected = IncompleteElementException.class)
//    public void processTransformIncompleteTransformTest(){
//        //TODO
//    }

}