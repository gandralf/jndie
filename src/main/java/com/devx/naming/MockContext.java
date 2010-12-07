package com.devx.naming;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Main context factory entry. MockContext provides a lookup service for
 * entries found on mock-jndi.xml file for the current context and, 
 * if such entry is not found, invokes the auxiliary naming service.
 * It also provides {@link #enterContext(String)} and {@link #leaveContext()}
 * static methods.
 * 
 * @author Alexandre Gandra Lages
 */
public class MockContext extends ContextImpl {
    private Context additionalContext;
    private static final Map<Thread, ContextEnvironment> CONTEXT_MAP = new HashMap<Thread, ContextEnvironment>();

    static {
        // Some really ugly code, just to make sure that MockContextFactory 
        // was initialized, configuring pkgs environment entries and enabling 
        // lookups. Instead of whining, see the MockContextFactory.init method
        new MockContextFactory();
    }

    /**
     * Creates a <code>MockContext</code> without any auxiliary context.
     * @throws NamingException if an error happens on loading a shitty mock-jndi.xml
     */
    public MockContext() throws NamingException {
    	this(null);
    }
    
    /**
     * Creates a <code>MockContext</code> with an auxiliary context. This means that 
     * a lookup is made on mock-jndi.xml and, if not found, on 
     * <code>additionalContext</code>
     * @throws NamingException if an error happens on loading a shitty mock-jndi.xml
     */
	public MockContext(Context additionalContext) throws NamingException {
    	this.additionalContext = additionalContext;
		try {
            GlobalContextLoader.init();
        } catch (IOException e) {
            throw (NamingException) new NamingException(e.getMessage()).initCause(e);
        }
    }

    public Object lookup(String name) throws NamingException {
        Object result = mockLookup(name);
        if (result == null && additionalContext != null) {
            result = additionalContext.lookup(name);
        } else if (result == null) {
            throw new NameNotFoundException(name);
        }
        
        return result;
    }

    protected Object mockLookup(String name) throws NamingException {
        Object result = null;
        
        if (name.startsWith("java:comp/env")) {
            ContextEnvironment contextEnvironment = CONTEXT_MAP.get(Thread.currentThread());
            // Se existe uma pilha que indica o contexto para a thread atual,
            // e se ela nao estiver vazia...
            if (contextEnvironment != null && contextEnvironment.namingStack.size() > 0) {
                Stack<Map<String, Object>> envStack = contextEnvironment.namingStack;
                Map<String, Object> environment = envStack.peek();
                if (environment != null) {
                	if (!"java:comp/env".equals(name)) {
                		result = environment.get(name.substring("java:comp/env/".length()));
                	} else {
                		return new MockEnvContext(environment);
                	}
                }
            }
            
            // Se naum achou na pilha de contextos, checa o environment default
            if (result == null) {
                GlobalContextLoader contextLoader = GlobalContextLoader.getInstance();
                String defaultEnvironment = contextLoader.getDefaultEnvironment();
                if (defaultEnvironment != null) {
                	EnvironmentManager environment = (EnvironmentManager) contextLoader.getObjectMap().get(defaultEnvironment);
                	result = environment.getEnvironment().get(name.substring("java:comp/env/".length()));
                }
            }
        } else {
            GlobalContextLoader contextLoader = GlobalContextLoader.getInstance();
            result = contextLoader.getObjectMap().get(name); 
        }
        
        return result;
    }

    static void enterContext(Map<String, Object> environment) {
        synchronized(CONTEXT_MAP) {
            Thread key = Thread.currentThread();
            ContextEnvironment contextEnvironment = CONTEXT_MAP.get(key);
            if (contextEnvironment == null) {
                contextEnvironment = new ContextEnvironment();
                CONTEXT_MAP.put(key, contextEnvironment);
            }

            contextEnvironment.enter(environment);
        }
    }

    public static void enterContext(String name) throws NamingException {
        try {
            GlobalContextLoader.init();
        } catch (IOException e) {
            throw (NamingException) new NamingException(e.toString()).initCause(e);
        }

        EnvironmentManager beanEnvironment = (EnvironmentManager) GlobalContextLoader.getInstance().getObjectMap().get(name);
        if (beanEnvironment == null) {
        	throw new IllegalArgumentException("Environment not found: " + name);
        }
        Map<String, Object> environment = beanEnvironment.getEnvironment();

        enterContext(environment);
    }

    public static void leaveContext() {
        synchronized(CONTEXT_MAP) {
            Thread key = Thread.currentThread();
            ContextEnvironment environment = CONTEXT_MAP.get(key);
            if (environment.leave()) {
                CONTEXT_MAP.remove(key);
            }
        }
    }

}
