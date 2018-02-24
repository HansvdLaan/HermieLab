package adapter;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class MyAdapterTests {

    @Rule public final ExpectedException exception = ExpectedException.none();
    static MyAdapter adapter; //Use String[] for performance issues

    @BeforeClass
    public static void setUp() throws NoSuchMethodException {
        adapter = new MyAdapter();
    }

    @Test
    public void processBasicTest(){
        assertEquals(true,adapter.process("a","this"));
        assertEquals(false,adapter.process("b","this"));
        assertEquals(true,adapter.process("c","this"));
    }

    @Test
    public void processIllegalArgumentTest(){
        exception.expect(IllegalArgumentException.class);
        adapter.process("d","e");
    }

    @Test
    public void processInputBasicTest(){
        Optional outa = adapter.processInput("a");
        assertTrue(outa.isPresent());
        assertEquals(true, outa.get());
        Optional outb = adapter.processInput("b");
        assertTrue(outb.isPresent());
        assertEquals(false, outb.get());
        Optional outc = adapter.processInput("c");
        assertTrue(outc.isPresent());
        assertEquals(true, outc.get());
        Optional outd = adapter.processInput("d");
        assertFalse(outd.isPresent());
    }

    @Test
    public void processInputIllegalArgumentTest(){
        exception.expect(IllegalArgumentException.class);
        adapter.processInput("e");
    }

    @Test
    public void processOutputBasicTest(){
        adapter.processInput("a");
        assertEquals(true, adapter.processOutput("output"));
        adapter.processInput("b");
        assertEquals(false, adapter.processOutput("output"));
        adapter.processInput("c");
        assertEquals(true, adapter.processOutput("output"));
        adapter.processInput("d");
        assertEquals(true, adapter.processOutput("output"));
    }

    @Test
    public void processOutputIllegalArgumentTest(){
        exception.expect(IllegalArgumentException.class);
        adapter.processOutput("a");
    }

    @Test
    public void checkBasicTest(){
        assertEquals(true,adapter.checkPredicate("aPredicate"));
        assertEquals(true, adapter.checkPredicate("bPredicate"));
        assertEquals(false,adapter.checkPredicate("cPredicate"));
        assertEquals(true,adapter.checkPredicate("dPredicate"));
    }

    @Test
    public void checkIllegalArgumentTest(){
        exception.expect(IllegalArgumentException.class);
        adapter.processOutput("ePredicate");
    }

    @Test
    public void transformBasicTest(){
        assertEquals("tB:true",adapter.transformOutput("booleanTransform",true));
        assertEquals("tS:a", adapter.transformOutput("stringTransform","a"));
    }

    @Test
    public void transformIllegalArgumentTest(){
        exception.expect(IllegalArgumentException.class);
        adapter.transformOutput("integerTransform",null);
    }

    @Test
    public void transformIllegalConcreteOutputTest(){
        exception.expect(ClassCastException.class);
        assertEquals("tB:true",adapter.transformOutput("booleanTransform",42));
    }
}