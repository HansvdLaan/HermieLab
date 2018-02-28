package files.checkers;

import files.FileGenerator;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.junit.BeforeClass;
import org.junit.Test;
import settings.Settings;
import settings.containers.GeneratorInformationElement;
import utils.FileTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.FileTestUtils.testElem;


public final class CheckerFileGeneratorTests {

    private static Settings settings;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException, DocumentException {
        settings = new Settings();

        String type = "inputfunction";
        String ID1 = "permissionFalse";
        String ID2 = "permissionTrue";
        String ID3 = "permissionAlsoFalse";

        Map<String, Object> params1 = new HashMap<>();
        params1.put("output","output");
        params1.put("nfapredicate","testNFA#NFARepeatSequence2[1]#G1#p1#!p1");
        params1.put("input","rectractPermission");
        params1.put("predicate","p1");
        params1.put("method","setPermission(boolean)");
        params1.put("id",ID1);
        params1.put("type",type);
        params1.put("class","GamceController");
        params1.put("param#0","Bool:False");
        settings.addElement(new GeneratorInformationElement(type,ID1,params1));

        Map<String, Object> params2 = new HashMap<>();
        params2.put("output","output");
        params2.put("nfapredicate","testNFA#NFARepeatSequence2[2]#G1#_#_");
        params2.put("input","rectractPermission");
        params2.put("predicate","p1");
        params2.put("method","setPermission(boolean)");
        params2.put("id",ID2);
        params2.put("type",type);
        params2.put("class","GamceController");
        params2.put("param#0","Bool:False");
        settings.addElement(new GeneratorInformationElement(type,ID2,params2));

        Map<String, Object> params3 = new HashMap<>();
        params3.put("output","output");
        params3.put("nfapredicate","testNFA#NFARepeatSequence2[2]#G1#_#p1");
        params3.put("input","rectractPermission2");
        params3.put("predicate","p1");
        params3.put("method","setPermission(boolean)");
        params3.put("id",ID3);
        params3.put("type",type);
        params3.put("class","GamceController");
        params3.put("param#0","Bool:False");
        settings.addElement(new GeneratorInformationElement(type,ID3,params3));
    }

    @Test
    public void nfaTransformationTest() throws ClassNotFoundException {
        FileGenerator generator = new FileGenerator("nfatest","checkers");
        generator.setSettings(settings);
        generator.addTransformation(CheckerTransformations.nfaCheckerTransformation);
        generator.applyTransformations();

        generator.writeToFile("test-output/checkers");

        FileTestUtils.testElem(generator.getDocument().getRootElement(), "checkers",1);

        Element nfaChecker = FileTestUtils.getContent(generator.getDocument().getRootElement()).get(0);
        FileTestUtils.testElem(nfaChecker, "nfachecker",1);

        Element group = FileTestUtils.getContent(nfaChecker).get(0);
        FileTestUtils.testElem(group, "group", 2);

        Element id = FileTestUtils.getContent(group).get(0);
        FileTestUtils.testElem(id, "id", "G1",1);

        Element nfa = FileTestUtils.getContent(group).get(1);
        FileTestUtils.testElem(nfa, "nfa", "",5);
    }

    @Test
    public void predicateTransformationTest() throws ClassNotFoundException {
        FileGenerator generator = new FileGenerator("predicatetest","checkers");
        generator.setSettings(settings);
        generator.addTransformation(CheckerTransformations.predicateCheckerTransformation);
        generator.applyTransformations();

        generator.writeToFile("test-output/checkers");

        FileTestUtils.testElem(generator.getDocument().getRootElement(), "checkers",1);

        Element predicateChecker = FileTestUtils.getContent(generator.getDocument().getRootElement()).get(0);
        FileTestUtils.testElem(predicateChecker, "predicatechecker",3);

        List<Element> inputs = FileTestUtils.getContent(predicateChecker);
        FileTestUtils.testElem(inputs.get(0),"input",2);
        FileTestUtils.testElem(inputs.get(1),"input",2);

        FileTestUtils.testElem(FileTestUtils.getContent(inputs.get(0)).get(0),"id","permissionFalse",1);
        FileTestUtils.testElem(FileTestUtils.getContent(inputs.get(0)).get(1),"predicate","p1",1);
        FileTestUtils.testElem(FileTestUtils.getContent(inputs.get(1)).get(0),"id","permissionTrue",1);
        FileTestUtils.testElem(FileTestUtils.getContent(inputs.get(1)).get(1),"predicate","p1",1);
    }

    @Test
    public void allTransformationsTest() throws ClassNotFoundException {
        FileGenerator generator = new FileGenerator("alltransforms","checkers");
        generator.setSettings(settings);
        generator.addTransformation(CheckerTransformations.nfaCheckerTransformation);
        generator.addTransformation(CheckerTransformations.predicateCheckerTransformation);
        generator.applyTransformations();

        generator.writeToFile("test-output/checkers");
    }

}