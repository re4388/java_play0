package com.ben;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogBackExample {

    private static final Logger logger = LoggerFactory.getLogger(LogBackExample.class);


    public static void main(String[] args) {

        logger.trace("This is TRACE");
        logger.debug("This is DEBUG");
        logger.info("This is INFO");
        logger.warn("This is WARN");
        logger.error("This is ERROR");
    }
}
