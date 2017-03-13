package org.tweb.application.util;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by jonas on 1/31/17.
 */
public class LogMethod {
    @AroundInvoke
    public Object log(InvocationContext ctx) throws Exception {
        Logger logger = Logger.getLogger(ctx.getTarget().getClass().getName());
        logger.fine("Calling method " + ctx.getMethod().getName() + " with args " + Arrays.toString(ctx.getParameters()));
        Object returnMe = ctx.proceed();
        //logger.info("after call to " + ctx.getMethod() + " returned " + returnMe);
        return returnMe;
    }
}
