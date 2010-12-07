package com.devx.naming.test.java;

import com.devx.naming.ContextImpl;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;

public class javaURLContextFactory implements ObjectFactory {
    public Object getObjectInstance(Object obj, Name name, Context nameCtx,
            Hashtable environment) throws Exception {
        return new ContextImpl() {
            public Object lookup(String name) throws NamingException {
                if (name.equals("java:/test")) {
                    return "Hello, java:/test!";
                } else {
                    throw new NameNotFoundException("test.java.javaURLContextFactory called");
                }
            }
        };
    }
}
