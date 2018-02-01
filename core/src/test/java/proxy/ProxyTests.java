package proxy;

import edu.duke.cs.jflap.automata.fsa.FiniteStateAutomaton;
import example_components.adapter.Adapter;
import example_components.checker.Checker;
import example_components.checker.JavaFXGUIChecker;
import example_components.checker.SimplePredicateChecker;
import example_components.checker.tests.RocketGameAdapter;
import example_components.mapper.ConcreteInvocation;
import example_components.mapper.SULMapper;
import example_components.proxy.Proxy;
import jflap.FSAGenerator;
import net.automatalib.commons.util.IPair;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class ProxyTests {

    static Proxy<Boolean[]> proxy1;
    static Proxy<Boolean[]> proxy2;
    static Proxy<Boolean[]> proxy3;
    static Proxy<Boolean[]> proxy4;

    static ConcreteInvocation b1p;
    static ConcreteInvocation b1r;
    static ConcreteInvocation b2p;
    static ConcreteInvocation b2r;


    @BeforeClass
    public static void setUp() throws NoSuchMethodException {
        com.sun.javafx.application.PlatformImpl.startup(()->{});
        Adapter adapter = new RocketGameAdapter();
        Checker checker1 = new SimplePredicateChecker(adapter);

        //TODO: this should be loaded from the xml;
        IPair<FiniteStateAutomaton,List<String>> nfaButton1 = new IPair<>(FSAGenerator.generateFreshNFA(
                Collections.singletonList("b1")),
                new ArrayList<String>(Arrays.asList("button1Predicate"))
        );
        IPair<FiniteStateAutomaton,List<String>> nfaButton2 = new IPair<>(FSAGenerator.generateFreshNFA(
                Collections.singletonList("b2")),
                new ArrayList<String>(Arrays.asList("button2Predicate"))
        );
        Set<IPair> nfaSet = new HashSet();
        nfaSet.add(nfaButton1);
        nfaSet.add(nfaButton2);

        Checker checker2 = new JavaFXGUIChecker(adapter, nfaSet);

        SULMapper<String, String, ConcreteInvocation, Boolean[]> mapper = new SULMapper(adapter);

        b1p = new ConcreteInvocation("button1P", "output", new ArrayList<String>(Arrays.asList("button1Predicate")));
        b1r = new ConcreteInvocation("button1R", "output", new ArrayList<String>(Arrays.asList("button1Predicate")));
        b2p = new ConcreteInvocation("button2P", "output", new ArrayList<String>(Arrays.asList("button2Predicate")));
        b2r = new ConcreteInvocation("button2R", "output", new ArrayList<String>(Arrays.asList("button2Predicate")));

        //TODO: this should be loaded from the xml
        mapper.getInputMappings().put("b1P",b1p);
        mapper.getInputMappings().put("b1R",b1r);
        mapper.getInputMappings().put("b2P",b2p);
        mapper.getInputMappings().put("b2R",b2r);

        mapper.getIndirectOutputMappings().put(Boolean[].class,"rocket1ArmedFilter");

        proxy1 = new Proxy(adapter);
        proxy2 = new Proxy(adapter);
        proxy3 = new Proxy(adapter);
        proxy4 = new Proxy(adapter);
    }

    @Before
    public void before(){
        proxy1.pre();
        proxy2.pre();
        proxy3.pre();
        proxy4.pre();
    }

    @After
    public void after(){
        proxy1.post();
        proxy2.post();
        proxy3.post();
        proxy4.post();
    }

    @Test
    public void simpleProxy1Test(){
        assertTrue(proxy1.step(b1p)[0]);
        assertFalse(proxy1.step(b1r)[0]);
        assertFalse(proxy1.step(b2r)[0]);
        assertFalse(proxy1.step(b2p)[0]);
    }


    @Test
    public void simpleProxy3Test(){
        assertTrue(proxy3.step(b1p)[0]);
        assertFalse(proxy3.step(b1r)[0]);
        assertFalse(proxy3.step(b2r)[0]);
        assertFalse(proxy3.step(b2p)[0]);
    }

    @Test
    public void simpleProxy3TestValid(){
        assertTrue(proxy2.step(b1p)[0]);
        assertFalse(proxy2.step(b1r)[0]);
        assertFalse(proxy2.step(b2r)[0]);
        assertFalse(proxy2.step(b2r)[0]);
    }
}