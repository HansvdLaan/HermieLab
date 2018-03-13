package checker;

import adapter.Adapter;
import checker.nfa.SingleNFAChecker;
import learningsetup.LearningSetupUtils;
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

public final class SingleNFACheckerTests {

    @Rule public final ExpectedException exception = ExpectedException.none();
    private static Adapter adapter;
    private static SingleNFAChecker checker;
    public static CheckerSettings settings;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException, DocumentException {
        Document checkersDocument = LearningSetupUtils.toDocument(Paths.get("tests","checker","checkers.xml").toFile());
        settings = LearningSetupUtils.parseCheckers(checkersDocument,Paths.get("tests","checker"));
        checker = new SingleNFAChecker();
        checker.addNFAs(settings.getNFAs("nfachecker"));
    }

    @Before
    public void reset(){
        checker.reset();
    }

    @Test
    public void testCorrectSequence1(){
        assertTrue(checker.checkConcreteInput("CI1"));
        assertTrue(checker.checkConcreteInput("CI2"));
        assertTrue(checker.checkConcreteInput("CI1"));
        assertTrue(checker.checkConcreteInput("CI3"));
    }

    @Test
    public void testSequenceWithUnkownSymbol(){
        assertTrue(checker.checkConcreteInput("CI4"));
    }

    @Test
    public void testIncorrectSequence1(){
        assertTrue(checker.checkConcreteInput("CI1"));
        assertTrue(checker.checkConcreteInput("CI3"));
        assertFalse(checker.checkConcreteInput("CI2"));
    }

    @Test
    public void testIncorrectSequence2(){
        assertTrue(checker.checkConcreteInput("CI1"));
        assertTrue(checker.checkConcreteInput("CI3"));
        assertFalse(checker.checkConcreteInput("CI2"));
        assertFalse(checker.checkConcreteInput("CI1"));
    }

}