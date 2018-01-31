package mapper;

import adapter.Adapter;
import de.learnlib.api.SULException;
import de.learnlib.mapper.api.Mapper;

import java.util.HashMap;
import java.util.Map;

public class SULMapper<AI,AO,CI,CO> implements Mapper<AI,AO,CI,CO> {

    private Map<AI, CI> inputMappings;
    private Map<CO, AO> directOutputMappings;
    private Map<Class, String> indirectOutputMappings; //Methods should be static, should be simple translate methods.
    private Adapter<AO,CO> adapter;


    public SULMapper(Adapter adapter){
        this.adapter = adapter;
        inputMappings = new HashMap<>();
        directOutputMappings = new HashMap<>();
        indirectOutputMappings = new HashMap<>();
    }

    @Override
    public void pre() {

    }

    @Override
    public void post() {

    }

    @Override
    public CI mapInput(AI abstractInput) {
        return inputMappings.get(abstractInput);
    }

    @Override
    public AO mapOutput(CO concreteOutput) {
        pre();
        AO output = null;
        try {
            if (directOutputMappings.containsKey(concreteOutput)) {
                output = directOutputMappings.get(concreteOutput);
            } else {
                for (Class c: indirectOutputMappings.keySet()){
                    if (c.isInstance(concreteOutput)){
                        try {
                            output = adapter.transformOutput(indirectOutputMappings.get(c), concreteOutput);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                if (output == null) {
                    output = (AO) concreteOutput;
                }
            }
            return output;
        }
        finally {
            post();
        }
    }

    @Override
    public MappedException<? extends AO> mapWrappedException(SULException exception) throws SULException {
        return null; //TODO
    }

    @Override
    public MappedException<? extends AO> mapUnwrappedException(RuntimeException exception) throws SULException, RuntimeException {
        return null; //TODO
    }


    public Map<AI, CI> getInputMappings() {
        return inputMappings;
    }

    public Map<CO, AO> getDirectOutputMappings() {
        return directOutputMappings;
    }

    public Map<Class, String> getIndirectOutputMappings() {
        return indirectOutputMappings;
    }

}
