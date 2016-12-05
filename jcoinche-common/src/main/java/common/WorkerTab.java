package common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaso on 22/11/16.
 */

public class WorkerTab {
    private Map<Integer, Runnable> tabWorker = new HashMap<>();

    public int addWorker(int id, Runnable worker) {
        if (id >= 0) {
            tabWorker.put(id, worker);
            return (0);
        }
        return (-1);
    }

    public int runWorker(int id) {
        if (id >= 0 && tabWorker.get(id) != null) {
            tabWorker.get(id).run();
            return (0);
        }
        return (-1);
    }
}
