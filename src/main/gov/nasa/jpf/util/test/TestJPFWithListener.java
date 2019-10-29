package gov.nasa.jpf.util.test;

import gov.nasa.jpf.Error;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.JPFListener;

import java.util.List;

public class TestJPFWithListener extends TestJPF{


    protected boolean verifyNoPropertyViolationWithListeners (JPFListener[] listeners, String...args){
        if (runDirectly) {
            return true;
        } else {
            noPropertyViolationWithListeners(listeners, getCaller(), args);
            return false;
        }
    }

    protected JPF noPropertyViolationWithListeners (JPFListener[] listeners, StackTraceElement testMethod, String... args) {
        JPF jpf = null;

        report(args);

        try {
            jpf = createAndRunJPFWithListeners(listeners, testMethod, args);
        } catch (Throwable t) {
            // we get as much as one little hickup and we declare it failed
            t.printStackTrace();
            fail("JPF internal exception executing: ", args, t.toString());
            return jpf;
        }

        List<Error> errors = jpf.getSearchErrors();
        if ((errors != null) && (errors.size() > 0)) {
            fail("JPF found unexpected errors: " + (errors.get(0)).getDescription());
        }

        return jpf;
    }

    protected JPF createAndRunJPFWithListeners (JPFListener[] listeners, StackTraceElement testMethod, String[] args) {
        JPF jpf = createJPF( testMethod, args);
        for (JPFListener l: listeners) {
            jpf.addListener(l);
        }

        if (jpf != null){
            jpf.run();
        }
        return jpf;
    }

}
