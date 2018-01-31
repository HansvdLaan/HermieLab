package proxy;

import adapter.Adapter;
import de.learnlib.api.SUL;
import de.learnlib.api.SULException;
import mapper.ConcreteInvocation;

import javax.annotation.Nullable;

public class Proxy<CO> implements SUL<ConcreteInvocation,CO> {

    private Adapter<?,CO> adapter;

    public Proxy(Adapter<?,CO> adapter){
        this.adapter = adapter;
    }

    @Override
    public void pre() {
        adapter.preQuery();
    }

    @Override
    public void post() {
        adapter.postQuery();
    }

    @Nullable
    @Override
    public CO step(@Nullable ConcreteInvocation in) throws SULException {
        CO ret;
        assert in != null;
        if (in.getInputInstanceID().equals(in.getOutputInstanceID())) {
            adapter.preInputInvocation();
            ret = adapter.process(in.getInputInstanceID());
            adapter.postInputInvocation();
        } else {
            adapter.preInputInvocation();
            adapter.processInput(in.getInputInstanceID());
            adapter.postInputInvocation();
            adapter.preOutputInvocation();
            ret = adapter.processOutput(in.getOutputInstanceID());
            adapter.preOutputInvocation();
        }
        return ret;
    }

    public Adapter getAdapter(){
        return adapter;
    }

    public void setAdapter(Adapter adapter){
        this.adapter = adapter;
    }

    @Override
    public boolean canFork() {
        return false;
    }
}
