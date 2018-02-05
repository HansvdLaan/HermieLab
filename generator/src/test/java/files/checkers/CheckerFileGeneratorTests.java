package files.checkers;

import files.FileGenerator;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.junit.BeforeClass;
import org.junit.Test;
import settings.Settings;
import settings.containers.GeneratorInformationElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static files.FileTestUtils.getContent;
import static files.FileTestUtils.testElem;


public final class CheckerFileGeneratorTests {

    private static Settings settings;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException, DocumentException {
        settings = new Settings();

        String type = "inputfunction";
        String ID1 = "permissionFalse";
        String ID2 = "permissionTrue";

        Map<String, Object> params1 = new HashMap<>();
        params1.put("output","output");
        params1.put("nfapredicate","testNFA#NFARepeatSequence2[2]#G1");
        params1.put("input","rectractPermission");
        params1.put("predicate","p1");
        params1.put("method","setPermission(boolean)");
        params1.put("id",ID1);
        params1.put("type",type);
        params1.put("class","GamceController");
        params1.put("param#0","Bool:False");
        settings.addSettingsByTypeAndID(type,ID1,new GeneratorInformationElement(type,ID1,params1));

        Map<String, Object> params2 = new HashMap<>();
        params2.put("output","output");
        params2.put("nfapredicate","testNFA#NFARepeatSequence2[2]#G1");
        params2.put("input","rectractPermission");
        params2.put("predicate","p1");
        params2.put("method","setPermission(boolean)");
        params2.put("id",ID2);
        params2.put("type",type);
        params2.put("class","GamceController");
        params2.put("param#0","Bool:False");
        settings.addSettingsByTypeAndID(type,ID2,new GeneratorInformationElement(type,ID2,params2));
    }

    @Test
    public void nfaTransformationTest() throws ClassNotFoundException {
        FileGenerator generator = new FileGenerator("nfatest","checkers");
        generator.setSettings(settings);
        generator.addTransformation(CheckerTransformations.nfaCheckerTransformation);
        generator.applyTransformations();

        generator.writeToFile("src/test/resources/testoutput/checkers");

        testElem(generator.getDocument().getRootElement(), "checkers",1);

        Element nfaChecker = getContent(generator.getDocument().getRootElement()).get(0);
        testElem(nfaChecker, "nfachecker",1);

        Element group = getContent(nfaChecker).get(0);
        testElem(group, "group", 2);

        Element id = getContent(group).get(0);
        testElem(id, "id", "G1",1);

        Element nfa = getContent(group).get(1);
        testElem(nfa, "nfa", "",3);
    }

    @Test
    public void predicateTransformationTest() throws ClassNotFoundException {
        FileGenerator generator = new FileGenerator("predicatetest","checkers");
        generator.setSettings(settings);
        generator.addTransformation(CheckerTransformations.predicateCheckerTransformation);
        generator.applyTransformations();

        generator.writeToFile("src/test/resources/testoutput/checkers");

        testElem(generator.getDocument().getRootElement(), "checkers",1);

        Element predicateChecker = getContent(generator.getDocument().getRootElement()).get(0);
        testElem(predicateChecker, "predicatechecker",2);

        List<Element> inputs = getContent(predicateChecker);
        testElem(inputs.get(0),"input",2);
        testElem(inputs.get(1),"input",2);

        testElem(getContent(inputs.get(0)).get(0),"id","permissionFalse",1);
        testElem(getContent(inputs.get(0)).get(1),"predicate","p1",1);
        testElem(getContent(inputs.get(1)).get(0),"id","permissionTrue",1);
        testElem(getContent(inputs.get(1)).get(1),"predicate","p1",1);
    }

    @Test
    public void allTransformationsTest() throws ClassNotFoundException {
        FileGenerator generator = new FileGenerator("alltransforms","checkers");
        generator.setSettings(settings);
        generator.addTransformation(CheckerTransformations.nfaCheckerTransformation);
        generator.addTransformation(CheckerTransformations.predicateCheckerTransformation);
        generator.applyTransformations();

        generator.writeToFile("src/test/resources/testoutput/checkers");
    }

}