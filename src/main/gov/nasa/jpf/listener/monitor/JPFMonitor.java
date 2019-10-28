package gov.nasa.jpf.listener.monitor;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.util.JPFLogger;
import gov.nasa.jpf.vm.*;

import java.util.HashSet;

public class JPFMonitor extends PropertyListenerAdapter {
    static JPFLogger log = JPF.getLogger("gov.nasa.jpf.listener.monitor");

    String msg;
    private State currentState;
    private State initialState;
    private HashSet<State> allStates;

    public JPFMonitor (Config config) {
    }

    public JPFMonitor (Config config, String[] allStates ,State initialState) {
        for (String s: allStates) {
            this.allStates.add(new State(s));
        }
        this.initialState = initialState;
    }

    public JPFMonitor addTransition(State sourceState, State destState, Event event ) {
        assert (allStates.contains(sourceState));
        assert (allStates.contains(destState));
        sourceState.eventHandler.put(event, destState);
    }


    @Override
    public boolean check(Search search, VM vm) {
        return (msg == null);
    }

    @Override
    public String getErrorMessage() {
        return msg;
    }

    protected String getMessage (String details, Instruction insn){
        String s = "failed assertion";

        if (details != null){
            s += ": \"";
            s += details;
            s += '"';
        }

        s += " at ";
        s += insn.getSourceLocation();

        return s;
    }

    @Override
    public void executeInstruction (VM vm, ThreadInfo ti, Instruction insn){

    }

    @Override
    public void reset() {
        msg = null;
    }
}
