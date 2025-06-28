package com.example.school.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Supplier;

public class LogHelper {
    private static final Logger logger = LoggerFactory.getLogger(LogHelper.class);
    public static void logMethodAndArgsLvlDebug(String methodName, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(
                    "Method [{}] invoked with args: {}",
                    methodName,
                    args
            );
        }
    }
    public static void logExceptionsLvlError(Exception e,Object... args) {
        logger.error("Method [{}] invoked with args: {}, and throw {}",e.getStackTrace()[0].getMethodName(),args,e.getClass().getName());
    }


}
