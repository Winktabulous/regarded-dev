package com.zenyte.cores;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A hidden exception handler for logging silent thread death from the slow executor pool.
 * @author David O'Neill (dlo3)
 */
public final class SlowThreadHandler implements Thread.UncaughtExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(SlowThreadHandler.class);

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        logger.error("(" + thread.getName() + ", slow pool) - Printing trace");
        throwable.printStackTrace();
    }

}
