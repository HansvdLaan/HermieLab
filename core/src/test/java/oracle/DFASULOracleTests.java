package oracle;

import adapter.Adapter;
import checker.Checker;
import checker.PredicateChecker;
import de.learnlib.api.MembershipOracle;
import mapper.ConcreteInvocation;
import mapper.SULMapper;
import net.automatalib.commons.util.IPair;
import net.automatalib.words.WordBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import proxy.Proxy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class DFASULOracleTests {

    static MembershipOracle.DFAMembershipOracle dfaMembershipOracle;


    @BeforeClass
    public static void setUp() throws NoSuchMethodException {
        com.sun.javafx.application.PlatformImpl.startup(()->{});
        Adapter adapter = new RocketGameAdapter();
        Checker predicateChecker = new PredicateChecker(adapter);
//
//        //TODO: this should be loaded from the xml;
//        IPair<FiniteStateAutomaton,List<String>> nfaButton1 = new IPair<>(FSAGenerator.generateFreshNFA(
//                Collections.singletonList("b1")),
//                new ArrayList<String>(Arrays.asList("button1Predicate"))
//        );
//        IPair<FiniteStateAutomaton,List<String>> nfaButton2 = new IPair<>(FSAGenerator.generateFreshNFA(
//                Collections.singletonList("b2")),
//                new ArrayList<String>(Arrays.asList("button2Predicate"))
//        );
//        Set<IPair> nfaSet = new HashSet();
//        nfaSet.add(nfaButton1);
//        nfaSet.add(nfaButton2);
//
//        //Checker checker2 = new JavaFXGUIChecker(adapter, nfaSet);

        SULMapper<String, String, ConcreteInvocation, Boolean[]> mapper = new SULMapper(adapter);

        //TODO: this should be loaded from the xml
        mapper.getInputMappings().put("b1P",new ConcreteInvocation("button1P", "output", new ArrayList<String>(Arrays.asList("button1Predicate"))));
        mapper.getInputMappings().put("b1R",new ConcreteInvocation("button1R", "output", new ArrayList<String>(Arrays.asList("button1Predicate"))));
        mapper.getInputMappings().put("b2P",new ConcreteInvocation("button2P", "output", new ArrayList<String>(Arrays.asList("button2Predicate"))));
        mapper.getInputMappings().put("b2R",new ConcreteInvocation("button2R", "output", new ArrayList<String>(Arrays.asList("button2Predicate"))));

        mapper.getIndirectOutputMappings().put(Boolean[].class,"rocket1ArmedFilter");

        Proxy<Boolean[]> proxy = new Proxy(adapter);


        dfaMembershipOracle = new DFASULOracle(proxy,mapper,new ArrayList<Checker>(Arrays.asList(predicateChecker)));
    }

    @Before
    public void before(){
    }

    @After
    public void after(){
    }

    @Test
    public void simpleOracleTestValid1(){
        WordBuilder<String> wb = new WordBuilder<>(1);
        wb.append("b1P");
        assertTrue((Boolean) dfaMembershipOracle.answerQuery((wb.toWord())));
    }

    @Test
    public void simpleOracleTestValid2(){
        WordBuilder<String> wb = new WordBuilder<>(3);
        wb.append("b1P");
        wb.append("b1R");
        wb.append("b1P");
        assertTrue((Boolean) dfaMembershipOracle.answerQuery((wb.toWord())));
    }

    @Test
    public void simpleOracleTestValid3(){
        WordBuilder<String> wb = new WordBuilder<>(5);
        wb.append("b1P");
        wb.append("b1R");
        wb.append("b2P");
        wb.append("b2R");
        wb.append("b1P");
        assertTrue((Boolean) dfaMembershipOracle.answerQuery((wb.toWord())));
    }

    @Test
    public void simpleOracleTestInvalid1(){
        WordBuilder<String> wb = new WordBuilder<>(1);
        wb.append("b1R");
        assertFalse((Boolean) dfaMembershipOracle.answerQuery((wb.toWord())));
    }

    @Test
    public void simpleOracleTestInvalid2(){
        WordBuilder<String> wb = new WordBuilder<>(1);
        wb.append("b2P");
        assertFalse((Boolean) dfaMembershipOracle.answerQuery((wb.toWord())));
    }

    @Test
    public void simpleOracleTestInvalid3(){
        WordBuilder<String> wb = new WordBuilder<>(1);
        wb.append("b1R");
        assertFalse((Boolean) dfaMembershipOracle.answerQuery((wb.toWord())));
    }
    @Test
    public void simpleOracleTestInvalid4(){
        WordBuilder<String> wb = new WordBuilder<>(3);
        wb.append("b1P");
        wb.append("b1P");
        wb.append("b1P");
        assertFalse((Boolean) dfaMembershipOracle.answerQuery((wb.toWord())));
    }
}