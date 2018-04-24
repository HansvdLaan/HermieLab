package hermielab.core.checker;

import hermielab.core.adapter.Adapter;
import hermielab.core.adapter.MyAdapter;
import hermielab.core.checker.predicate.PredicateChecker;
import hermielab.core.learningsetup.LearningSetupUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class PredicateCheckerTests {

    @Rule public final ExpectedException exception = ExpectedException.none();
    private static Adapter adapter;
    private static PredicateChecker checker;
    public static CheckerSettings settings;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException, DocumentException {
        Adapter adapter = new MyAdapter();
        Document checkersDocument = LearningSetupUtils.toDocument(Paths.get("tests","hermielab","core","checker","checkers.xml").toFile());
        settings = LearningSetupUtils.parseCheckers(checkersDocument,Paths.get("tests","hermielab","core","checker"));
        checker = new PredicateChecker(adapter, settings.getAllPredicates());
    }

    @Before
    public void reset(){
        checker.reset();
    }

    @Test
    public void testTruePredicates1(){
        assertTrue(checker.checkConcreteInput("CI1"));
    }

    @Test
    public void testTruePredicates2(){
        assertTrue(checker.checkConcreteInput("CI2"));
    }

    @Test
    public void testFalsePredicates2(){
        assertFalse(checker.checkConcreteInput("CI3"));
    }




}