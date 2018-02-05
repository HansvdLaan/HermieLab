package settings.utils;

import com.google.testing.compile.JavaFileObjects;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.BeforeClass;
import org.junit.Test;
import settings.SettingsParser;
import testutils.DummyPreProcessor;

import javax.lang.model.element.*;
import java.io.File;

import static com.google.testing.compile.Compiler.javac;
import static org.junit.Assert.assertEquals;


public final class ClassUtilsTest {

    static Document document;
    static DummyPreProcessor processor;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException, DocumentException {
        document = SettingsParser.toDocument(new File(ClassUtilsTest.class.getResource("/settings/settings0.xml").getPath()));
        processor = (DummyPreProcessor) javac().withProcessors(new DummyPreProcessor()).compile(
                JavaFileObjects.forResource("rocketgame/RocketGame.java"),
                JavaFileObjects.forResource("GameController.java")

                //JavaFileObjects.forResource("testgame/model/Game.java"),
                //JavaFileObjects.forResource("testgame/model/RocketStatus.java"),
                //JavaFileObjects.forResource("testgame/controller/GameController.java"),
                //JavaFileObjects.forResource("testgame/controller/RootController.java")

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
        ClassUtils.getClass("GameController");
    }

    @Test
    public void methodParsing() throws ClassNotFoundException, NoSuchMethodException {
        TypeElement gameController = ClassUtils.getClass("GameController");
        ClassUtils.getMethod(gameController, "method1()");
        ClassUtils.getMethod(gameController, "method2()");
        ClassUtils.getMethod(gameController, "method2(int)");
        TypeElement rocketGame = ClassUtils.getClass("RocketGame");
        ClassUtils.getMethod(rocketGame, "start(javafx.stage.Stage)");
    }

    @Test
    public void fieldParsing() throws ClassNotFoundException {
        TypeElement gameController = ClassUtils.getClass("GameController");
        VariableElement var1 = ClassUtils.getField(gameController, "field");
        VariableElement var2 = ClassUtils.getField(gameController, "thread");
    }

    @Test
    public void methodFieldParsing() throws ClassNotFoundException, NoSuchMethodException {
        TypeElement gameController = ClassUtils.getClass("GameController");
        VariableElement var1 = ClassUtils.getField(gameController, "field");
        VariableElement var2 = ClassUtils.getField(gameController, "thread");
        ClassUtils.getMethod(gameController, var1, "matches(java.lang.String)");
        ClassUtils.getMethod(gameController, var2, "getState()");

    }

}