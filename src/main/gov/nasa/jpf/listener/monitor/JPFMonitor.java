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
    private HashSet<State> allStates;
    private PropertyMonitor checkingProperty;

    public JPFMonitor (Config config, HashSet<State> allStates, State initialState) {
        this.allStates = allStates;
        this.currentState = initialState;
    }

    public JPFMonitor addTransition(State sourceState, State destState, Event event ) {
        assert (allStates.contains(sourceState));
        assert (allStates.contains(destState));
        if (event != null) {
            sourceState.eventHandler.put(event, destState);
        }
        return this;
    }

    public JPFMonitor addCheckingProperty(PropertyMonitor property) {
        this.checkingProperty = property;
        return this;
    }


    private void handleEvent(Event event) {
        if (currentState.eventHandler.containsKey(event)) {
            State nextState = currentState.eventHandler.get(event);
            if (!this.checkingProperty.isActivated()) {
                if (nextState.equals(checkingProperty.getStartingState())) { // enter property monitor
                    checkingProperty.activate();
                    if (!checkingProperty.check(nextState)) {
                        reportPropertyViolation();
                        return;
                    }
                } // nothing bad happened, make the transition
                currentState = nextState;
            }
            else { // already in property monitor
                if (!checkingProperty.check(nextState)) {
                    reportPropertyViolation();
                    return;
                }
                currentState = nextState;
            }
        }
    }

    protected void reportPropertyViolation (){
        this.msg = "property specification violated:\n";
    }

    @Override
    public boolean check(Search search, VM vm) {
        return (msg == null);
    }

    @Override
    public String getErrorMessage() {
        return msg;
    }

    @Override
    public void methodEntered(VM vm, ThreadInfo currentThread, MethodInfo enteredMethod) {
        this.handleEvent(new Event(Event.Trigger.METHODCALL, enteredMethod.getName()));
    }


    @Override
    public void reset() {
        msg = null;
        checkingProperty.deactivate();
    }
}
