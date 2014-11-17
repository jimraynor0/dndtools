package org.toj.dnd.irctoolkit.util;

import org.apache.log4j.Logger;

public class IdUtil {
    private static long splitSecCount = 1;
    private static long lastTime;

    private static Logger log = Logger.getLogger(IdUtil.class);

    public static synchronized String generateId() {
        long time = System.currentTimeMillis();
        log.debug("generating id on: " + time);
        if (lastTime == time) {
            splitSecCount++;
        } else {
            splitSecCount = 1;
        }
        lastTime = time;
        String id = time + "." + splitSecCount;
        log.debug("returning id: " + id);
        return id;
    }
}
