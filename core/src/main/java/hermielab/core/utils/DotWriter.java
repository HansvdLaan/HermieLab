package hermielab.core.utils;

import net.automatalib.automata.Automaton;
import net.automatalib.graphs.dot.GraphDOTHelper;
import net.automatalib.util.graphs.dot.GraphDOT;
import net.automatalib.words.Alphabet;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Hans on 19-4-2018.
 */
public class DotWriter {

    public static String toDot(Alphabet alphabet, Automaton automaton){
        StringWriter stringWriter = new StringWriter(0);
        try {
            GraphDOT.write(automaton, alphabet, stringWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }
}
