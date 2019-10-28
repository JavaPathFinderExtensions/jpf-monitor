package gov.nasa.jpf.listener.monitor;

import java.util.HashMap;

public class State {
    private String name;
    protected HashMap<Event, State> eventHandler;


    public State(String name) {
        this.name = name;
        this.eventHandler = new HashMap<>();
    }

    public String getName() {
        return name;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        return name.equals(state.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
