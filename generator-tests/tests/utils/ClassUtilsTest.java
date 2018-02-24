package utils;

import com.google.testing.compile.JavaFileObjects;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.BeforeClass;
import org.junit.Test;
import settings.SettingsParser;

import javax.lang.model.element.*;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Paths;

import static com.google.testing.compile.Compiler.javac;


public final class ClassUtilsTest {

    static Document document;
    static DummyPreProcessor processor;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException, DocumentException, MalformedURLException {
        document = SettingsParser.toDocument(new File(ClassUtilsTest.class.getResource("/settings/settings0.xml").getPath()));
        processor = (DummyPreProcessor) javac().withProcessors(new DummyPreProcessor()).compile(
                JavaFileObjects.forResource(Paths.get("tests","testcode","GameController.java").toFile().toURL()),
                JavaFileObjects.forResource(Paths.get("tests","testcode","rocketgame","RocketGame.java").toFile().toURL())
        ).compiler().processors().get(0);
        ClassUtils.getInstance().setPrEnv(processor.getPreEnv());
    }

    @Test
    //These test should just not crash
    public void classParsing() throws ClassNotFoundException {
        ClassUtils.getClass("bool");
        ClassUtils.getClass("byte");
        ClassUtils.getClass("short");
        ClassUtils.getClass("int");
        ClassUtils.getClass("long");
        ClassUtils.getClass("float");
        ClassUtils.getClass("double");
        ClassUtils.getClass("char");
        ClassUtils.getClass("void");
        ClassUtils.getClass("testcode.GameController");
    }

    @Test
    public void methodParsing() throws ClassNotFoundException, NoSuchMethodException {
        TypeElement gameController = ClassUtils.getClass("testcode.GameController");
        ClassUtils.getMethod(gameController, "method1()");
        ClassUtils.getMethod(gameController, "method2()");
        ClassUtils.getMethod(gameController, "method2(int)");
        TypeElement rocketGame = ClassUtils.getClass("testcode.rocketgame.RocketGame");
        ClassUtils.getMethod(rocketGame, "start(javafx.stage.Stage)");
    }

    @Test
    public void fieldParsing() throws ClassNotFoundException {
        TypeElement gameController = ClassUtils.getClass("testcode.GameController");
        VariableElement var1 = ClassUtils.getField(gameController, "field");
        VariableElement var2 = ClassUtils.getField(gameController, "thread");
    }

    @Test
    public void methodFieldParsing() throws ClassNotFoundException, NoSuchMethodException {
        TypeElement gameController = ClassUtils.getClass("testcode.GameController");
        VariableElement var1 = ClassUtils.getField(gameController, "field");
        VariableElement var2 = ClassUtils.getField(gameController, "thread");
        ClassUtils.getMethod(gameController, var1, "matches(java.lang.String)");
        ClassUtils.getMethod(gameController, var2, "getState()");

    }

}