package utils;

import org.dom4j.Element;

import javax.tools.JavaFileObject;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Hans on 1-2-2018.
 */
public class FileTestUtils {

    public static void testElem(Element element, String expectedName, String expectedText){
        assertEquals(expectedName,element.getName());
        assertEquals(expectedText,element.getText());
    }

    public static void testElem(Element element, String expectedName, int exprectedContentSize){
        assertEquals(expectedName,element.getName());
        assertEquals(exprectedContentSize,element.content().size());
    }

    public static void testElem(Element element, String expectedName, String expectedText, int exprectedContentSize){
        assertEquals(expectedName,element.getName());
        assertEquals(expectedText,element.getText());
        assertEquals(exprectedContentSize,element.content().size());
    }

    public static List<Element> getContent(Element element){
        List<Element> returnList = new ArrayList<>();
        element.content().forEach(content -> returnList.add((Element) content));
        return returnList;
    }

    public static JavaFileObject loadJavaFileObjectt(){
        Path resourceDirectory = Paths.get("tests","testcode","rocketgame","");
        return null;
    }
}
