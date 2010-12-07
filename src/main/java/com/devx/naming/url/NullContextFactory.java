package com.devx.naming.url;

import com.devx.naming.ContextImpl;

import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.ObjectFactory;
import javax.naming.*;
import java.util.Hashtable;

/**
 * Another pkg crazy class
 * @author Alexandre Gandra Lages
 */
public class NullContextFactory implements InitialContextFactory, ObjectFactory {
    public Context getInitialContext(Hashtable environment) throws NamingException {
        return new ContextImpl() {
            public Object lookup(String name) throws NamingException {
                throw new NameNotFoundException(name + ". NullContextFactory does not implement a true lookup!");
            }
        };
    }

    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws Exception {
        throw new UnsupportedOperationException("NullContextFactory does not implement getObjectInstance");
    }
}
