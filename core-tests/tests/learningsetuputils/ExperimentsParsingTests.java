package learningsetuputils;

import hermielab.core.learningsetup.AbstractExperiment;
import hermielab.core.learningsetup.LearningSetupUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class ExperimentsParsingTests {

    static Document experiments;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException, DocumentException {
        Path experimentsPath = Paths.get("tests","learningsetuputils","experiments.xml");
        //TODO: Error if file not found!
        experiments = LearningSetupUtils.toDocument(experimentsPath.toFile());
    }

    @Test
    public void abstractSymbolTransformationTest() throws ClassNotFoundException {
        Map<String, AbstractExperiment> parseExperiments = LearningSetupUtils.parseExperiments(experiments);
        assertEquals(1, parseExperiments.keySet().size());
        assertTrue(parseExperiments.keySet().contains("experiment1"));
        System.out.println(parseExperiments);
    }

}