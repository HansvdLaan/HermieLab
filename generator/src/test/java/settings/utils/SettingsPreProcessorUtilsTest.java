package settings;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.BeforeClass;
import org.junit.Test;
import settings.annotations.PreProcessorUtilsTestProcessor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Types;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.testing.compile.Compiler.javac;
import static org.junit.Assert.assertEquals;


public final class SettingsPreProcessorUtilsTest {

    static Document settings0;
    static PreProcessorUtilsTestProcessor processor;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException, DocumentException {
        settings0 = SettingsParser.toDocument(new File(SettingsPreProcessorUtilsTest.class.getResource("/settings/settings0.xml").getPath()));
        processor = (PreProcessorUtilsTestProcessor) javac().withProcessors(new PreProcessorUtilsTestProcessor()).compile(
                JavaFileObjects.forResource("GameController.java"),
                JavaFileObjects.forResource("rocketgame/RocketGame.java")
        ).compiler().processors().get(0);
        SettingsPreProcessorUtils.getInstance().setPrEnv(processor.getPreEnv());
    }

    @Test
    //These test should just not crash
    public void classParsing() throws ClassNotFoundException {
        SettingsPreProcessorUtils.getClass("bool");
        SettingsPreProcessorUtils.getClass("byte");
        SettingsPreProcessorUtils.getClass("short");
        SettingsPreProcessorUtils.getClass("int");
        SettingsPreProcessorUtils.getClass("long");
        SettingsPreProcessorUtils.getClass("float");
        SettingsPreProcessorUtils.getClass("double");
        SettingsPreProcessorUtils.getClass("char");
        SettingsPreProcessorUtils.getClass("void");
        SettingsPreProcessorUtils.getClass("GameController");
    }

    @Test
    public void methodParsing() throws ClassNotFoundException, NoSuchMethodException {
        TypeElement gameController = SettingsPreProcessorUtils.getClass("GameController");
        SettingsPreProcessorUtils.getMethod(gameController, "method1()");
        SettingsPreProcessorUtils.getMethod(gameController, "method2()");
        SettingsPreProcessorUtils.getMethod(gameController, "method2(int)");
        TypeElement rocketGame = SettingsPreProcessorUtils.getClass("rocketgame.RocketGame");
        SettingsPreProcessorUtils.getMethod(rocketGame, "start(javafx.stage.Stage)");
    }

    @Test
    public void fieldParsing() throws ClassNotFoundException {
        TypeElement gameController = SettingsPreProcessorUtils.getClass("GameController");
        VariableElement var1 = SettingsPreProcessorUtils.getField(gameController, "field");
        VariableElement var2 = SettingsPreProcessorUtils.getField(gameController, "thread");
    }

    @Test
    public void methodFieldParsing() throws ClassNotFoundException, NoSuchMethodException {
        TypeElement gameController = SettingsPreProcessorUtils.getClass("GameController");
        VariableElement var1 = SettingsPreProcessorUtils.getField(gameController, "field");
        VariableElement var2 = SettingsPreProcessorUtils.getField(gameController, "thread");
        SettingsPreProcessorUtils.getMethod(gameController, var1, "matches(java.lang.String)");
        SettingsPreProcessorUtils.getMethod(gameController, var2, "getState()");

    }

}