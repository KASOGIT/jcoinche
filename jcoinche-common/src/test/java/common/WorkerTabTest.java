package common;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kj on 27/11/16.
 */
public class WorkerTabTest {

    WorkerTab test = new WorkerTab();

    private void test() {

    }
    @Test
    public void addWorker() throws Exception {
        assertEquals(0, test.addWorker(2, this::test));
        assertEquals(-1, test.addWorker(-42, null));
    }

    @Test
    public void runWorker() throws Exception {
        test.addWorker(2, this::test);
        assertEquals(0, test.runWorker(2));
        assertEquals(-1, test.runWorker(-42));
    }

}