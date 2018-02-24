package checker;

import adapter.Adapter;
import adapter.MyAdapter;
import checker.nfa.SingleNFAChecker;
import checker.predicate.PredicateChecker;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class PredicateCheckerTests {

    @Rule public final ExpectedException exception = ExpectedException.none();
    private static Adapter adapter;
    private static PredicateChecker checker;
    public static CheckerSettings settings;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException {
        Adapter adapter = new MyAdapter();
        settings = CheckerUtils.loadCheckerSettings().get(0);
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