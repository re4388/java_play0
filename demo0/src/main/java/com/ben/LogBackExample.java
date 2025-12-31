package com.ben;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogBackExample {

    private static final Logger LOG = LoggerFactory.getLogger(LogBackExample.class);

    public static void main(String[] args) {
        LOG.trace("This is TRACE");
        LOG.debug("This is DEBUG");
        LOG.info("This is INFO");
        LOG.warn("This is WARN");
        LOG.error("This is ERROR");
    }
}
