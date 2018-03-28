package learningsetuputils;

import learningsetup.AbstractExperiment;
import learningsetup.LearningSetupUtils;
import mapper.ConcreteInvocation;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public final class ExperimentsParsingTests {

    static Document experiments;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException, DocumentException {
        Path experimentsPath = Paths.get("tests","learningsetuputils","experiments.xml");
        experiments = LearningSetupUtils.toDocument(experimentsPath.toFile());
    }

    @Test
    public void abstractSymbolTransformationTest() throws ClassNotFoundException {
        Map<String, AbstractExperiment> parseExperiments = LearningSetupUtils.parseExperiments(experiments);
        System.out.println(parseExperiments);
    }

}