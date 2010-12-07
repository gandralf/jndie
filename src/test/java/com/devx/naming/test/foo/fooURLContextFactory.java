package com.devx.naming.test.foo;

import com.devx.naming.ContextImpl;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;

/**
 * @author gandralf
 */
public class fooURLContextFactory implements ObjectFactory {
    public Object getObjectInstance(Object obj, Name name, Context nameCtx,
            Hashtable environment) throws Exception {
        ContextImpl result = new ContextImpl() {
            public Object lookup(String name) throws NamingException {
                if (name.equals("foo:/foo")) {
                    return "Hello, foo!";
                } else {
                    throw new NameNotFoundException(name);
                }
            }
        };

        return result;
    }
}
