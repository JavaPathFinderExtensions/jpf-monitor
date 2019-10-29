package gov.nasa.jpf.listener.monitor;

import java.util.LinkedList;
import java.util.ListIterator;

public class PropertyMonitor {
    private boolean not;
    private LinkedList<State> stateSequence;
    private ListIterator<State> stateIterator;
    private State currentState;

    public PropertyMonitor(boolean not) {
        this.not = not;
        this.stateSequence = new LinkedList<>();
        this.currentState = null;
        this.stateIterator = null;
    }

    public PropertyMonitor addStateLast(State state) {
        stateSequence.addLast(state);
        return this;
    }

    protected boolean isActivated() {
        return stateIterator != null;
    }

    protected void activate() {
        if (stateSequence.size() != 0) {
            this.stateIterator = stateSequence.listIterator();
        }
    }

    protected void deactivate() {
        this.stateIterator = null;
    }

    protected State getStartingState() {
        return stateSequence.getFirst();
    }

    // must be called when activated is true.
    protected boolean check(State inputState) {
        assert (isActivated());
        assert (stateIterator.hasNext());
        State nextState = stateIterator.next();
        if (!nextState.equals(inputState)) {  // not matching next state specification, terminate iterator
            deactivate();
            return not;
        }
        else {
            if (!stateIterator.hasNext()) { // end is reached
                deactivate();
                return !not;
            }
            return true;
        }
    }


}
