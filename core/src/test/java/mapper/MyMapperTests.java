package mapper;

import adapter.Adapter;
import adapter.MyAdapter;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public final class MyMapperTests {

    static SULMapper<String,String,ConcreteInvocation,Object> mapper; //Use String[] for performance issues
    static Adapter adapter;

    @BeforeClass
    public static void setUp() throws NoSuchMethodException {
        com.sun.javafx.application.PlatformImpl.startup(()->{});
        adapter = new MyAdapter();
        mapper = new SULMapper<>(adapter);
        mapper.getInputMappings().put("a",new ConcreteInvocation("a_button", "output", new ArrayList<String>(Arrays.asList("aPredicate"))));
        mapper.getInputMappings().put("b",new ConcreteInvocation("b_button", "output", new ArrayList<String>(Arrays.asList("bPredicate"))));
        mapper.getInputMappings().put("c",new ConcreteInvocation("c_button", "output", new ArrayList<String>(Arrays.asList("cPredicate"))));

        mapper.getDirectOutputMappings().put(Boolean.FALSE,"F-0");
        mapper.getDirectOutputMappings().put(Boolean.TRUE,"T-1");
        mapper.getIndirectOutputMappings().put(Boolean.class,"booleanTransform");
        mapper.getIndirectOutputMappings().put(String.class,"stringTransform");
    }

    @Test
    public void basicInputMapping(){
        ConcreteInvocation c1 = mapper.mapInput("a");
        ConcreteInvocation c2 =  mapper.mapInput("b");
        ConcreteInvocation c3 = mapper.mapInput("c");
        assertEquals("a_button", c1.getInputInstanceID());
        assertEquals("output", c1.getOutputInstanceID());
        assertEquals( new ArrayList<String>(Arrays.asList("aPredicate")), c1.getPredicateIDs());
        assertEquals("b_button", c2.getInputInstanceID());
        assertEquals("output", c2.getOutputInstanceID());
        assertEquals( new ArrayList<String>(Arrays.asList("bPredicate")), c2.getPredicateIDs());
        assertEquals("c_button", c3.getInputInstanceID());
        assertEquals("output", c3.getOutputInstanceID());
        assertEquals( new ArrayList<String>(Arrays.asList("cPredicate")), c3.getPredicateIDs());
    }

    @Test
    public void outputMapping(){
        assertEquals("F-0",mapper.mapOutput(false));
        assertEquals("T-1", mapper.mapOutput(true));
        assertEquals("tS:" + "a", mapper.mapOutput("a"));
        mapper.getDirectOutputMappings().remove(false);
        mapper.getDirectOutputMappings().remove(true);
        assertEquals("tB:true", mapper.mapOutput(true));
        mapper.getIndirectOutputMappings().remove(String.class);
        assertEquals("a", mapper.mapOutput("a"));
    }
}