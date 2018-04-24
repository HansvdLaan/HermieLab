package hermielab.core.checker;

import hermielab.core.adapter.Adapter;
import hermielab.core.checker.nfa.GUINFAChecker;
import hermielab.core.checker.nfa.NFAChecker;
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

public final class GUINFACheckerTests {

    @Rule public final ExpectedException exception = ExpectedException.none();
    private static Adapter adapter;
    private static NFAChecker checker;
    public static CheckerSettings settings;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException, DocumentException {
        Document checkersDocument = LearningSetupUtils.toDocument(Paths.get("tests","hermielab","core","checker","checkers.xml").toFile());
        settings = LearningSetupUtils.parseCheckers(checkersDocument, Paths.get("tests","hermielab","core","checker"));
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