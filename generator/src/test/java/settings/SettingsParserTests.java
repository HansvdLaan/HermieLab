package settings;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.BeforeClass;
import org.junit.Test;
import package2.Settings;
import settings.containers.GeneratorInformationElement;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public final class SettingsParserTests {

    static Document settings0;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException, DocumentException {
        settings0 = SettingsParser.toDocument(new File(SettingsParserTests.class.getResource("/settings/settings0.xml").getPath()));
    }

    @Test
    public void basicInputMapping(){
        System.out.println(settings0);
        Settings settings = SettingsParser.readSettings(settings0);
        System.out.println(settings);

        //Test if all the keys are correctly parsed
        assertEquals(6,settings.getAllSettings().keySet().size());

        //Test if the abstract input symbol was correctly parsed
        assertEquals(2, settings.getSettingsByType("functionsymbol").keySet().size());
        GeneratorInformationElement abstractInputSymbol1 = settings.getSettingsByType("functionsymbol").get("permissionTrue");

        //Looks like it's working. TODO: build these tests


    }

}