package files.mappings;

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


public final class MappingsFileGeneratorTests {

    private static Settings settings;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException, DocumentException {
        settings = new Settings();

        String type = "abstractsymbolmapping";
        String ID1 = "symbol1";
        String ID2 = "symbol2";
        String ID3 = "symbol3";

        Map<String, Object> params1 = new HashMap<>();
        params1.put("concreteoutput","output1");
        params1.put("concreteinput","buttonRocket1Press");
        params1.put("ID",ID1);
        params1.put("transform","transformID");
        params1.put("type",type);
        settings.addElement(new GeneratorInformationElement(type,ID1,params1));

        Map<String, Object> params2 = new HashMap<>();
        params2.put("concreteoutput","output1");
        params2.put("concreteinput","buttonRocket1Release");
        params2.put("ID",ID2);
        params2.put("transform","transformID");
        params2.put("type",type);
        settings.addElement(new GeneratorInformationElement(type,ID2,params2));

        Map<String, Object> params3 = new HashMap<>();
        params3.put("concreteoutput","output1");
        params3.put("concreteinput","buttonRocket1Drag");
        params3.put("ID",ID3);
        params3.put("transform","transformID");
        params3.put("type",type);
        settings.addElement(new GeneratorInformationElement(type,ID3,params3));
    }

    @Test
    public void abstractSymbolTransformationTest() throws ClassNotFoundException {
        FileGenerator generator = new FileGenerator("abstractsymboltest", "mappings");
        generator.setSettings(settings);
        generator.addTransformation(MappingsTransformations.abstractSymbolTransformation);
        generator.applyTransformations();

        generator.writeToFile("test-output/mappings");

        FileTestUtils.testElem(generator.getDocument().getRootElement(), "mappings", 3);

        Element symbol1 = FileTestUtils.getContent(generator.getDocument().getRootElement()).get(0);
        FileTestUtils.testElem(symbol1, "mapping", 4);

        Element symbol2 = FileTestUtils.getContent(generator.getDocument().getRootElement()).get(1);
        FileTestUtils.testElem(symbol2, "mapping", 4);

        Element symbol3 = FileTestUtils.getContent(generator.getDocument().getRootElement()).get(2);
        FileTestUtils.testElem(symbol3, "mapping", 4);

    }

}