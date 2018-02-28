package files.experiments;

import files.FileGenerator;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.junit.BeforeClass;
import org.junit.Test;
import settings.Settings;
import settings.containers.GeneratorInformationElement;
import utils.FileTestUtils;

import java.util.HashMap;
import java.util.Map;

import static utils.FileTestUtils.testElem;


public final class ExperimentFileGeneratorTests {

    private static Settings settings;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException, DocumentException {
        settings = new Settings();

        String type = "abstractsymbol";
        String ID1 = "symbol1";
        String ID2 = "symbol2";
        String ID3 = "symbol3";

        Map<String, Object> params1 = new HashMap<>();
        params1.put("output","output1");
        params1.put("input","buttonRocket1Press");
        params1.put("id",ID1);
        params1.put("type",type);
        settings.addElement(new GeneratorInformationElement(type,ID1,params1));

        Map<String, Object> params2 = new HashMap<>();
        params2.put("output","output1");
        params2.put("input","buttonRocket1Release");
        params2.put("id",ID2);
        params2.put("type",type);
        settings.addElement(new GeneratorInformationElement(type,ID2,params2));

        Map<String, Object> params3 = new HashMap<>();
        params3.put("output","output1");
        params3.put("input","buttonRocket1Drag");
        params3.put("id",ID3);
        params3.put("type",type);
        settings.addElement(new GeneratorInformationElement(type,ID3,params3));

        Map<String,Object> params4 = new HashMap<>();
        params4.put("automaton","DFA");
        params4.put("id","startMethod");
        params4.put("type","start");
        settings.addElement( new GeneratorInformationElement("start","startMethod", params4));
    }

    @Test
    public void experimentTransformationTest() throws ClassNotFoundException {
        FileGenerator generator = new FileGenerator("experiment", "experiments");
        generator.setSettings(settings);
        generator.addTransformation(ExperimentTransformations.experimentTransformations);
        generator.applyTransformations();

        generator.writeToFile("test-output/experiments");

        FileTestUtils.testElem(generator.getDocument().getRootElement(), "experiments", 1);

        Element experiment = FileTestUtils.getContent(generator.getDocument().getRootElement()).get(0);
        FileTestUtils.testElem(experiment, "experiment", 5);

        Element alphabet = FileTestUtils.getContent(experiment).get(4);
        FileTestUtils.testElem(alphabet, "alphabet", 3);
    }

}