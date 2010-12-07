package com.devx.naming;

import javax.naming.*;
import java.util.Hashtable;

/**
 * ContextImpl implements the mock context interface
 * @author Alexandre Gandra Lages
 */
public abstract class ContextImpl implements Context {
    public Object lookup(Name name) throws NamingException {
        return null;
    }

    public abstract Object lookup(String name) throws NamingException;

    public void bind(Name name, Object obj) throws NamingException {
    }

    public void bind(String name, Object obj) throws NamingException {
    }

    public void rebind(Name name, Object obj) throws NamingException {
    }

    public void rebind(String name, Object obj) throws NamingException {
    }

    public void unbind(Name name) throws NamingException {
    }

    public void unbind(String name) throws NamingException {
    }

    public void rename(Name oldName, Name newName) throws NamingException {
    }

    public void rename(String oldName, String newName) throws NamingException {
    }

    public NamingEnumeration list(Name name) throws NamingException {
        return null;
    }

    public NamingEnumeration list(String name) throws NamingException {
        return null;
    }

    public NamingEnumeration listBindings(Name name) throws NamingException {
        return null;
    }

    public NamingEnumeration listBindings(String name) throws NamingException {
        return null;
    }

    public void destroySubcontext(Name name) throws NamingException {
    }

    public void destroySubcontext(String name) throws NamingException {
    }

    public Context createSubcontext(Name name) throws NamingException {
        return null;
    }

    public Context createSubcontext(String name) throws NamingException {
        return null;
    }

    public Object lookupLink(Name name) throws NamingException {
        return null;
    }

    public Object lookupLink(String name) throws NamingException {
        return null;
    }

    public NameParser getNameParser(Name name) throws NamingException {
        return null;
    }

    public NameParser getNameParser(String name) throws NamingException {
        return null;
    }

    public Name composeName(Name name, Name prefix) throws NamingException {
        return null;
    }

    public String composeName(String name, String prefix) throws NamingException {
        return null;
    }

    public Object addToEnvironment(String propName, Object propVal) throws NamingException {
        return null;
    }

    public Object removeFromEnvironment(String propName) throws NamingException {
        return null;
    }

    public Hashtable getEnvironment() throws NamingException {
        return null;
    }

    public void close() throws NamingException {
    }

    public String getNameInNamespace() throws NamingException {
        return null;
    }
}
