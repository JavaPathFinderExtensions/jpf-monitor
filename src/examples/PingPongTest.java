import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPFListener;
import org.junit.Test;
import gov.nasa.jpf.listener.monitor.*;
import gov.nasa.jpf.util.test.TestJPFWithListener;

import java.util.HashSet;

public class PingPongTest extends TestJPFWithListener{
    @Test
    public void pingFollowedByPong() {
        State entry = new State("entry");
        State receivedPing = new State("receivedPing");
        State receivedPong = new State("receivedPong");
        Event pingEvent = new Event(Event.Trigger.METHODCALL, "ping");
        Event pongEvent = new Event(Event.Trigger.METHODCALL, "pong");
        HashSet<State> pingPong = new HashSet<>();
        pingPong.add(entry);
        pingPong.add(receivedPing);
        pingPong.add(receivedPong);
        JPFMonitor monitor = new JPFMonitor(new Config(new String[]{}), pingPong, entry);
        PropertyMonitor property = new PropertyMonitor(false);
        property.addStateLast(receivedPing);
        property.addStateLast(receivedPong);
        monitor.addCheckingProperty(property);
        if (verifyNoPropertyViolationWithListeners(new JPFListener[]{monitor}, "examples/PingPongTest.jpf")) {
            PingPong.ping();
            PingPong.pong();

        }

    }
}
