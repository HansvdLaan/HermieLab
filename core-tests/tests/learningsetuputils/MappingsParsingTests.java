package learningsetuputils;

import hermielab.core.learningsetup.LearningSetupUtils;
import hermielab.core.mapper.ConcreteInvocation;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public final class MappingsParsingTests {

    static Document mappings;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException, DocumentException {
        Path mappingsPath = Paths.get("tests","learningsetuputils","mappings.xml");
        //TODO: Error if file not found!
        mappings = LearningSetupUtils.toDocument(mappingsPath.toFile());
    }

    @Test
    public void abstractSymbolTransformationTest() throws ClassNotFoundException {
        Map<String, Map<String,Object>> parsedMappings = LearningSetupUtils.parseMappings(mappings);
        assertTrue(parsedMappings.containsKey("inputmapping"));

        assertTrue(parsedMappings.get("inputmapping").containsKey("buttonRocket1_Symbol1"));
        assertTrue(parsedMappings.get("inputmapping").get("buttonRocket1_Symbol1").equals(
                new ConcreteInvocation("buttonRocket1_mouse_drag","output", Collections.emptyList())));
        assertTrue(parsedMappings.get("inputmapping").containsKey("buttonRocket1_Symbol2"));
        assertTrue(parsedMappings.get("inputmapping").get("buttonRocket1_Symbol2").equals(
                new ConcreteInvocation("buttonRocket1_mouse_press","output", Collections.emptyList())));
        assertTrue(parsedMappings.get("inputmapping").containsKey("buttonRocket1_Symbol3"));
        assertTrue(parsedMappings.get("inputmapping").get("buttonRocket1_Symbol3").equals(
                new ConcreteInvocation("buttonRocket1_mouse_release","output", Collections.emptyList())));

        assertTrue(parsedMappings.get("inputmapping").containsKey("buttonRocket2_Symbol1"));
        assertTrue(parsedMappings.get("inputmapping").get("buttonRocket2_Symbol1").equals(
                new ConcreteInvocation("buttonRocket2_mouse_drag","output", Collections.emptyList())));
        assertTrue(parsedMappings.get("inputmapping").containsKey("buttonRocket2_Symbol2"));
        assertTrue(parsedMappings.get("inputmapping").get("buttonRocket2_Symbol2").equals(
                new ConcreteInvocation("buttonRocket2_mouse_press","output", Collections.emptyList())));
        assertTrue(parsedMappings.get("inputmapping").containsKey("buttonRocket2_Symbol3"));
        assertTrue(parsedMappings.get("inputmapping").get("buttonRocket2_Symbol3").equals(
                new ConcreteInvocation("buttonRocket2_mouse_release","output", Collections.emptyList())));

        assertTrue(parsedMappings.get("inputmapping").containsKey("permissionFalse"));
        assertTrue(parsedMappings.get("inputmapping").get("permissionFalse").equals(
                new ConcreteInvocation("permissionFalse","output", Collections.emptyList())));
        assertTrue(parsedMappings.get("inputmapping").containsKey("permissionTrue"));
        assertTrue(parsedMappings.get("inputmapping").get("permissionTrue").equals(
                new ConcreteInvocation("permissionTrue","output", Collections.emptyList())));
    }

}