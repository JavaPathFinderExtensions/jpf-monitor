package gov.nasa.jpf.listener.monitor;

import java.util.Objects;

public class Event {
    private Trigger triggerType;
    private String triggerName;

    public Event(Trigger triggerType, String triggerName) {
        this.triggerType = triggerType;
        this.triggerName = triggerName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return this.triggerType.equals(event.triggerType) && this.triggerName.equals(event.triggerName);

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.triggerType, this.triggerName);
    }

    @Override
    public String toString() {
        return triggerType.toString() + "-" + triggerName;
    }

    public enum Trigger
    {
        METHODCALL;
        // waiting to be added
    }

}
