package com.devx.naming.url.java;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;

/**
 * I'm not on the mood of writing javadoc comments about something that I
 * really don't know exactly how works. Anyway, I made that to make this 
 * work within tomcat.
 * @author Alexandre Gandra Lages
 */
public class javaURLContextFactory implements ObjectFactory {
    public Object getObjectInstance(Object obj, Name name, Context nameCtx,
            Hashtable environment) throws Exception {
        return null;
    }

}
