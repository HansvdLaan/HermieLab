package checker;

import adapter.Adapter;
import checker.nfa.GUINFAChecker;
import checker.nfa.NFAChecker;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class GUINFACheckerTests {

    @Rule public final ExpectedException exception = ExpectedException.none();
    private static Adapter adapter;
    private static NFAChecker checker;
    public static CheckerSettings settings;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException {
        settings = CheckerUtils.loadCheckerSettings().get(0);
        checker = new GUINFAChecker();
        checker.addNFAs(settings.getNFAs("guichecker"));
    }

    @Before
    public void reset(){
        checker.reset();
    }

    @Test
    public void testCorrectSequence1(){
        assertTrue(checker.checkConcreteInput("P1"));
        assertTrue(checker.checkConcreteInput("D1"));
        assertTrue(checker.checkConcreteInput("D1"));
        assertTrue(checker.checkConcreteInput("R1"));
        assertTrue(checker.checkConcreteInput("P2"));
        assertTrue(checker.checkConcreteInput("D2"));
        assertTrue(checker.checkConcreteInput("D1"));
        assertTrue(checker.checkConcreteInput("R2"));
    }

    @Test
    public void testSequenceWithUnkownSymbol(){
        assertTrue(checker.checkConcreteInput("P3"));
    }

    @Test
    public void testIncorrectSequence1(){
        assertTrue(checker.checkConcreteInput("P1"));
        assertTrue(checker.checkConcreteInput("D1"));
        assertTrue(checker.checkConcreteInput("D1"));
        assertTrue(checker.checkConcreteInput("R1"));
        assertTrue(checker.checkConcreteInput("P2"));
        assertFalse(checker.checkConcreteInput("P2"));
    }

    @Test
    public void testIncorrectSequence2(){
        assertTrue(checker.checkConcreteInput("P1"));
        assertTrue(checker.checkConcreteInput("D1"));
        assertTrue(checker.checkConcreteInput("D1"));
        assertTrue(checker.checkConcreteInput("R1"));
        assertTrue(checker.checkConcreteInput("P2"));
        assertFalse(checker.checkConcreteInput("P2"));
        assertFalse(checker.checkConcreteInput("D2"));
    }

}