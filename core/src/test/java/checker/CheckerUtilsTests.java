package checker;

import adapter.MyAdapter;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

import static org.junit.Assert.*;

public final class CheckerUtilsTests {

    @Rule public final ExpectedException exception = ExpectedException.none();
    public static CheckerSettings settings;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException {
        settings = CheckerUtils.loadCheckerSettings().get(0);
    }

    @Test
    public void basicParsingTest(){
        System.out.println(settings);
    }

}