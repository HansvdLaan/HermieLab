package files.mappings;

import files.FileGenerator;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.junit.BeforeClass;
import org.junit.Test;
import settings.Settings;
import settings.containers.GeneratorInformationElement;

import java.util.HashMap;
import java.util.Map;

import static files.FileTestUtils.getContent;
import static files.FileTestUtils.testElem;


public final class MappingsFileGeneratorTests {

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
        settings.addSettingsByTypeAndID(type,ID1,new GeneratorInformationElement(type,ID1,params1));

        Map<String, Object> params2 = new HashMap<>();
        params2.put("output","output1");
        params2.put("input","buttonRocket1Release");
        params2.put("id",ID2);
        params2.put("type",type);
        settings.addSettingsByTypeAndID(type,ID2,new GeneratorInformationElement(type,ID2,params2));

        Map<String, Object> params3 = new HashMap<>();
        params3.put("output","output1");
        params3.put("input","buttonRocket1Drag");
        params3.put("id",ID3);
        params3.put("type",type);
        settings.addSettingsByTypeAndID(type,ID3,new GeneratorInformationElement(type,ID3,params3));
    }

    @Test
    public void abstractSymbolTransformationTest() throws ClassNotFoundException {
        FileGenerator generator = new FileGenerator("abstractsymboltest", "mappings");
        generator.setSettings(settings);
        generator.addTransformation(MappingsTransformations.abstractSymbolTransformation);
        generator.applyTransformations();

        generator.writeToFile("src/test/resources/testoutput/mappings");

        testElem(generator.getDocument().getRootElement(), "mappings", 3);

        Element symbol1 = getContent(generator.getDocument().getRootElement()).get(0);
        testElem(symbol1, "abstractsymbol", 4);

        Element symbol2 = getContent(generator.getDocument().getRootElement()).get(1);
        testElem(symbol2, "abstractsymbol", 4);

        Element symbol3 = getContent(generator.getDocument().getRootElement()).get(2);
        testElem(symbol3, "abstractsymbol", 4);

    }

}