package com.zenyte.cores;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;


/**
 * A subtyped {@link ScheduledThreadPoolExecutor} with error logging
 * cabailities.
 * @author David O'Neill
 */
public final class SlowThreadPoolExecutor extends ScheduledThreadPoolExecutor {

	private static final Logger logger = LoggerFactory.getLogger(SlowThreadPoolExecutor.class);

    /**
     * Construct a {@link SlowThreadPoolExecutor} object backed
     * by a {@link SlowThreadFactory}.
     * @param corePoolSize the number of threads to hold in the pool
     * @param threadFactory the {@code ThreadFactory}
     */
    public SlowThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, final String name) {
        super(corePoolSize, threadFactory);
        this.name = name;
        logger.info(name + " open. Fixed thread pool size: " + corePoolSize);
    }

    private final String name;

    @Override
    public void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if(t != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            logger.info(name + " caught an exception.");
            logger.error(sw.toString());
        }
    }
}
