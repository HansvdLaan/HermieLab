package settings;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.BeforeClass;
import org.junit.Test;
import settings.containers.GeneratorInformationElement;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class SettingsParserTests {

    static Document document;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException, DocumentException {
        document = SettingsParser.toDocument(new File(SettingsParserTests.class.getResource("/settings/settings0.xml").getPath()));
    }

    @Test
    public void basicInputMapping(){
        Settings settings = SettingsParser.readSettings(document);
        //Test if all the keys are correctly parsed
        assertEquals(6,settings.getAllSettings().keySet().size());


        assertEquals(2, settings.getSettingsByType("widgetsymbol").keySet().size());
        assertTrue(settings.getSettingsByType("widgetsymbol").keySet().contains("buttonRocket1"));
        assertTrue(settings.getSettingsByType("widgetsymbol").keySet().contains("buttonRocket2"));

        assertEquals(1, settings.getSettingsByType("outputfunction").keySet().size());
        assertTrue(settings.getSettingsByType("outputfunction").keySet().contains("output1"));

        assertEquals(1, settings.getSettingsByType("fieldmethodsymbol").keySet().size());
        assertTrue(settings.getSettingsByType("fieldmethodsymbol").keySet().contains("button1RocketField"));

        assertEquals(2, settings.getSettingsByType("functionsymbol").keySet().size());
        assertTrue(settings.getSettingsByType("functionsymbol").keySet().contains("permissionTrue"));
        assertTrue(settings.getSettingsByType("functionsymbol").keySet().contains("permissionFalse"));

        assertEquals(1, settings.getSettingsByType("start").keySet().size());
        assertTrue(settings.getSettingsByType("start").keySet().contains("start0"));

        assertEquals(1, settings.getSettingsByType("postquery").keySet().size());
        assertTrue(settings.getSettingsByType("postquery").keySet().contains("postquery0"));

    }

}